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
