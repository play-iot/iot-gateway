dependencies {
    api(project(":connector:bacnet"))
    api(LogLibs.logback)
    testImplementation(testFixtures(ZeroLibs.qwe_base))
    testImplementation(VertxLibs.junit)
}
