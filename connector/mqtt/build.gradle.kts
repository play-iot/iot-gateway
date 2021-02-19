dependencies {
    api(ZeroLibs.qwe_base)
    api(ZeroLibs.qwe_cache)
    api(ZeroLibs.qwe_scheduler_core)
    api(ZeroLibs.qwe_protocol)
    api(VertxLibs.mqtt)
    testImplementation(testFixtures(ZeroLibs.qwe_base))
}
