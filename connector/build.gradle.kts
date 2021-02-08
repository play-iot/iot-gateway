dependencies {
    api(project(":data"))
    api(ZeroLibs.qwe_protocol)
    api(ZeroLibs.qwe_http_metadata)
    api(ZeroLibs.qwe_micro_rpc)
    api(ZeroLibs.qwe_scheduler_metadata)

    testImplementation(testFixtures(ZeroLibs.qwe_base))
}
