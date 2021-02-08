/*
 * This file was generated by the Gradle 'init' task.
 *
 * The settings file is used to specify which projects to include in your build.
 *
 * Detailed information about configuring a multi-project build in Gradle can be found
 * in the user manual at https://docs.gradle.org/6.7/userguide/multi_project_builds.html
 */

rootProject.name = "qwe-iot"
include(":data", ":connector")

include(
    "connectors:bacnet:mixin",
    "connectors:bacnet:base",
    "connectors:bacnet:simulator",
    "connectors:bacnet:service",
    "connectors:bacnet:server"
)
