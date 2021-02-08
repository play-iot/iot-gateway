dependencies {
    api(BACnetLibs.bacnet)
    api(ZeroLibs.qwe_protocol)

    testImplementation(testFixtures(ZeroLibs.qwe_base))
}
