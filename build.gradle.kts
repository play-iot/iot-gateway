import org.gradle.internal.jvm.Jvm
import org.gradle.util.GradleVersion
import java.time.Instant
import java.util.jar.Attributes.Name

plugins {
    `java-library`
    `java-library-distribution`
    `maven-publish`
    jacoco
    signing
    id(PluginLibs.sonarQube) version PluginLibs.Version.sonarQube
    id(PluginLibs.nexusStaging) version PluginLibs.Version.nexusStaging
}
val jacocoHtml: String? by project
val semanticVersion: String by project
val buildHash: String by project
project.tasks["sonarqube"].group = "analysis"
project.tasks["sonarqube"].dependsOn("build", "jacocoRootReport")

allprojects {
    group = "io.github.zero88.qwe"

    repositories {
        mavenLocal()
        maven { url = uri("https://oss.sonatype.org/content/groups/public/") }
        maven { url = uri("https://maven.mangoautomation.net/repository/ias-snapshot/") }
        maven { url = uri("https://maven.mangoautomation.net/repository/ias-release/") }
        mavenCentral()
        jcenter()
    }
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "java-library-distribution")
    apply(plugin = "eclipse")
    apply(plugin = "idea")
    apply(plugin = "jacoco")
    apply(plugin = "signing")
    apply(plugin = "maven-publish")
    apply(from = "$rootDir/buildSrc/generated.gradle")
    project.version = "$version$semanticVersion"
    project.ext.set("baseName", ProjectUtils.computeBaseName(project))
    project.ext.set("title", findProperty("title") ?: project.ext.get("baseName"))
    project.ext.set("description", findProperty("description") ?: "An IoT Gateway on Edge Compute: ${project.name}")

    afterEvaluate {
        if (setOf("service").contains(project.name)) {
            project.tasks.forEach { it.enabled = false }
        } else {
            println("- Project Name:     ${project.ext.get("baseName")}")
            println("- Project Title:    ${project.ext.get("title")}")
            println("- Project Group:    ${project.group}")
            println("- Project Version:  ${project.version}")
            println("- Gradle Version:   ${GradleVersion.current()}")
            println("- Java Version:     ${Jvm.current()}")
        }
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        withJavadocJar()
        withSourcesJar()
    }

    dependencies {
        compileOnly(UtilLibs.lombok)
        annotationProcessor(UtilLibs.lombok)

        testImplementation(TestLibs.junit5Api)
        testImplementation(TestLibs.junit5Engine)
        testImplementation(TestLibs.junit5Vintage)
        testImplementation(TestLibs.jsonAssert)
        testCompileOnly(UtilLibs.lombok)
        testAnnotationProcessor(UtilLibs.lombok)
    }

    tasks {
        jar {
            manifest {
                attributes(
                    mapOf(
                        Name.MANIFEST_VERSION.toString() to "1.0",
                        Name.IMPLEMENTATION_TITLE.toString() to archiveBaseName.get(),
                        Name.IMPLEMENTATION_VERSION.toString() to project.version,
                        "Created-By" to GradleVersion.current(),
                        "Build-Jdk" to Jvm.current(),
                        "Build-By" to project.property("buildBy"),
                        "Build-Hash" to project.property("buildHash"),
                        "Build-Date" to Instant.now()
                    )
                )
            }
        }
        javadoc {
            title = "${project.ext.get("title")} ${project.version} API"
            options {
                this as StandardJavadocDocletOptions
                tags = mutableListOf(
                    "apiNote:a:API Note:", "implSpec:a:Implementation Requirements:",
                    "implNote:a:Implementation Note:"
                )
            }
        }
        test {
            useJUnitPlatform()
        }
        withType<Jar>().configureEach {
            archiveBaseName.set(project.ext.get("baseName") as String)
        }
        withType<Sign>().configureEach {
            onlyIf { project.hasProperty("release") }
        }
        distZip {
            onlyIf { project.ext.has("standalone") && "true" == project.ext.get("standalone") }
            destinationDirectory.set(rootProject.buildDir.resolve("distributions"))
            into("${project.ext.get("baseName")}-${project.version}/conf") {
                from(project.buildDir.resolve("generated/conf"))
            }
        }
        distTar {
            onlyIf { project.ext.has("standalone") && "true" == project.ext.get("standalone") }
            destinationDirectory.set(rootProject.buildDir.resolve("distributions"))
            into("${project.ext.get("baseName")}-${project.version}/conf") {
                from(project.buildDir.resolve("generated/conf"))
            }
        }
    }

    distributions {
        main {
            distributionBaseName.set("${project.ext.get("baseName")}")
        }
    }

    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = project.group as String?
                artifactId = project.ext.get("baseName") as String
                version = project.version as String?
                from(components["java"])

                versionMapping {
                    usage("java-api") {
                        fromResolutionOf("runtimeClasspath")
                    }
                    usage("java-runtime") {
                        fromResolutionResult()
                    }
                }
                pom {
                    name.set(project.ext.get("title") as String)
                    description.set(project.ext.get("description") as String)
                    url.set("https://github.com/zero88/qwe-iot-gateway")
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("https://github.com/zero88/qwe-iot-gateway/blob/master/LICENSE")
                        }
                    }
                    developers {
                        developer {
                            id.set("zero88")
                            email.set("sontt246@gmail.com")
                        }
                    }
                    scm {
                        connection.set("scm:git:git://git@github.com:zero88/qwe-iot-gateway.git")
                        developerConnection.set("scm:git:ssh://git@github.com:zero88/qwe-iot-gateway.git")
                        url.set("https://github.com/zero88/qwe-iot-gateway")
                    }
                }
            }
        }
        repositories {
            maven {
                val path = if (project.hasProperty("github")) {
                    "${project.property("github.nexus.url")}/${project.property("nexus.username")}/${rootProject.name}"
                } else {
                    val releasesRepoUrl = project.property("ossrh.release.url")
                    val snapshotsRepoUrl = project.property("ossrh.snapshot.url")
                    if (project.hasProperty("release")) releasesRepoUrl else snapshotsRepoUrl
                }
                url = path?.let { uri(it) }!!
                credentials {
                    username = project.property("nexus.username") as String?
                    password = project.property("nexus.password") as String?
                }
            }
        }
    }

    signing {
        useGpgCmd()
        sign(publishing.publications["maven"])
    }
}

task<JacocoReport>("jacocoRootReport") {
    dependsOn(subprojects.map { it.tasks.withType<Test>() })
    dependsOn(subprojects.map { it.tasks.withType<JacocoReport>() })
    additionalSourceDirs.setFrom(subprojects.map { it.sourceSets.main.get().allSource.srcDirs })
    sourceDirectories.setFrom(subprojects.map { it.sourceSets.main.get().allSource.srcDirs })
    classDirectories.setFrom(subprojects.map { it.sourceSets.main.get().output })
    executionData.setFrom(project.fileTree(".") {
        include("**/build/jacoco/test.exec")
    })
    reports {
        csv.isEnabled = false
        xml.isEnabled = true
        xml.destination = file("${buildDir}/reports/jacoco/coverage.xml")
        html.isEnabled = (jacocoHtml ?: "true").toBoolean()
        html.destination = file("${buildDir}/reports/jacoco/html")
    }
}

sonarqube {
    properties {
        property("jacocoHtml", "false")
        property("sonar.sourceEncoding", "UTF-8")
        property("sonar.coverage.jacoco.xmlReportPaths", "${buildDir}/reports/jacoco/coverage.xml")
    }
}

task<Sign>("sign") {
    dependsOn(subprojects.map { it.tasks.withType<Sign>() })
}

nexusStaging {
    packageGroup = "io.github.zero88"
    username = project.property("nexus.username") as String?
    password = project.property("nexus.password") as String?
}

tasks {
    test {
        useJUnitPlatform()
    }
    jar {
        onlyIf { project != rootProject }
    }
    distTar {
        onlyIf { project != rootProject }
    }
    distZip {
        onlyIf { project != rootProject }
    }
}
