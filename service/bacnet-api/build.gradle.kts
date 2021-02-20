import io.github.zero88.qwe.gradle.app.task.SystemdServiceExtension.Arch

dependencies {
    api(project(":connector:bacnet"))
    api(ZeroLibs.qwe_cache)
    api(ZeroLibs.qwe_micro)
    api(LogLibs.logback)
    //Remove it if deploy under gateway
    api(ZeroLibs.qwe_storage_json)
    api(ZeroLibs.qwe_scheduler)
    api(ZeroLibs.qwe_http_server)

    testImplementation(ZeroLibs.qwe_http_client)
    testImplementation(testFixtures(ZeroLibs.qwe_base))
}

docker {
    qweApplication {
        enabled.set(true)
        dockerfile {
            ports.set(listOf(8888, 5000, 47808))
            configFile.set("bacnet.json")
        }
    }
}

qwe {
    application.set(true)
    app {
        logging {
            otherLoggers.set(mapOf("com.serotonin.bacnet4j" to "info"))
        }
        systemd {
            enabled.set(true)
            arch.set(Arch.ARM_V7)
            workingDir.set("/app/bacnet-api")
            serviceName.set("bacnet-api")
            configFile.set("bacnet.json")
        }
    }
}
