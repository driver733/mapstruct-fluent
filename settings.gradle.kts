rootProject.name = "mapstruct-fluent"

include("processors")
include("processors:common")
include("processors:processor")
include("processors:processor-spring")
include("examples")
include("examples:example")
include("examples:example-spring")

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}