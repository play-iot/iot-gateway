dependencies {
    api(ZeroLibs.qwe_micro_rpc)
    api(project(":connector"))
    api(project(":connector:bacnet:mixin"))

    testImplementation(testFixtures(ZeroLibs.qwe_base))
}
