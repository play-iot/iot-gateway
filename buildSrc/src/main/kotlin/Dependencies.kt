object UtilLibs {

    object Version {

        const val lombok = "1.18.16"
    }

    const val lombok = "org.projectlombok:lombok:${Version.lombok}"
}

object PluginLibs {

    object Version {

        const val nexusStaging = "0.22.0"
    }

    const val nexusStaging = "io.codearte.nexus-staging"
}

object TestLibs {

    object Version {

        const val junit5 = "5.7.0"
        const val jsonAssert = "1.5.0"
    }

    const val junit5Api = "org.junit.jupiter:junit-jupiter-api:${Version.junit5}"
    const val junit5Engine = "org.junit.jupiter:junit-jupiter-engine:${Version.junit5}"
    const val junit5Vintage = "org.junit.vintage:junit-vintage-engine:${Version.junit5}"
    const val jsonAssert = "org.skyscreamer:jsonassert:${Version.jsonAssert}"
}

object BACnetLibs {
    object Version {

        const val bacnet = "5.0.2"
    }

    const val bacnet = "com.infiniteautomation:bacnet4j:${Version.bacnet}"
}

object VertxLibs {

    object Version {

        const val vertx = "4.0.0"
    }

    const val junit = "io.vertx:vertx-unit:${Version.vertx}"
    const val junit5 = "io.vertx:vertx-junit5:${Version.vertx}"

}

object LogLibs {

    object Version {

        const val slf4j = "1.7.30"
        const val logback = "1.2.3"
    }

    const val slf4j = "org.slf4j:slf4j-api:${Version.slf4j}"
    const val logback = "ch.qos.logback:logback-classic:${Version.logback}"
}

object ZeroLibs {
    object Version {

        const val utils = "1.0.1"
        const val jpaExt = "0.9.0"
        const val rSql = "0.9.0"
        const val qwe = "0.6.1-SNAPSHOT"
        const val qweSql = "0.0.1-SNAPSHOT"
    }

    const val utils = "io.github.zero88:java-utils:${Version.utils}"
    const val jpaExt = "io.github.zero88:jpa-ext:${Version.jpaExt}"
    const val rql_jooq = "io.github.zero88:rql-jooq:${Version.rSql}"
    const val qwe_base = "io.github.zero88.qwe:qwe-base:${Version.qwe}"
    const val qwe_cache = "io.github.zero88.qwe:qwe-cache:${Version.qwe}"
    const val qwe_protocol = "io.github.zero88.qwe:qwe-protocol:${Version.qwe}"
    const val qwe_http_metadata = "io.github.zero88.qwe:qwe-http-metadata:${Version.qwe}"
    const val qwe_http_server = "io.github.zero88.qwe:qwe-http-server:${Version.qwe}"
    const val qwe_http_client = "io.github.zero88.qwe:qwe-http-client:${Version.qwe}"
    const val qwe_micro_metadata = "io.github.zero88.qwe:qwe-micro-metadata:${Version.qwe}"
    const val qwe_micro_rpc = "io.github.zero88.qwe:qwe-micro-rpc:${Version.qwe}"
    const val qwe_micro = "io.github.zero88.qwe:qwe-micro:${Version.qwe}"
    const val qwe_scheduler_metadata = "io.github.zero88.qwe:qwe-scheduler-metadata:${Version.qwe}"
    const val qwe_scheduler = "io.github.zero88.qwe:qwe-scheduler:${Version.qwe}"
    const val qwe_storage_json = "io.github.zero88.qwe:qwe-storage-json:${Version.qwe}"

    object Plugins {
        const val docker = "io.github.zero88.qwe.gradle.docker"
        const val root = "io.github.zero88.qwe.gradle.root"
    }
}
