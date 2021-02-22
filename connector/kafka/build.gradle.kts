dependencies {
    api(ZeroLibs.qwe_base)
    api(VertxLibs.kafka) {
        exclude("org.slf4j:slf4j-log4j12")
        exclude("log4j:log4j")
    }

    api(KafkaLibs.kafkaClient)
    api(KafkaLibs.kafka) {
        exclude(KafkaLibs.scalaLibName)
    }
    api(LogLibs.log4jOverSlf4j)
    api("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.12")

    testImplementation(testFixtures(ZeroLibs.qwe_base))
//    testCompile project (":core:base").sourceSets.test.output
//    testCompile project (":core:httpserver")
//    testCompile project (":core:httpserver").sourceSets.test.output
    testImplementation(KafkaLibs.scala)
    testImplementation(KafkaLibs.debezium)
    testImplementation(
        group = "io.debezium",
        name = "debezium-core",
        version = "$project.debeziumVersion",
        classifier = "tests"
    )
}
