plugins {
    id(ZeroLibs.Plugins.docker) version ZeroLibs.Version.qwe
    id(ZeroLibs.Plugins.root) version ZeroLibs.Version.qwe apply false
    id(PluginLibs.nexusStaging) version PluginLibs.Version.nexusStaging
}

apply(plugin = ZeroLibs.Plugins.root)

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
    apply(plugin = "eclipse")
    apply(plugin = "idea")
    apply(plugin = ZeroLibs.Plugins.docker)

    afterEvaluate {
        if (setOf("service").contains(project.name)) {
            project.tasks.forEach { it.enabled = false }
        }
    }

    docker {
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
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

    qwe {
        zero88.set(true)
        publishingInfo {
            enabled.set(true)
            homepage.set("https://github.com/zero88/qwe-iot-gateway")
            license {
                name.set("The Apache License, Version 2.0")
                url.set("https://github.com/zero88/qwe-iot-gateway/blob/master/LICENSE")
            }
            scm {
                connection.set("scm:git:git://git@github.com:zero88/qwe-iot-gateway.git")
                developerConnection.set("scm:git:ssh://git@github.com:zero88/qwe-iot-gateway.git")
                url.set("https://github.com/zero88/qwe-iot-gateway")
            }
        }
    }
}

nexusStaging {
    packageGroup = "io.github.zero88"
    username = project.property("nexus.username") as String?
    password = project.property("nexus.password") as String?
}
