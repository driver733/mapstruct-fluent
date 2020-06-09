plugins {
    id("com.driver733.gradle-kotlin-setup-plugin")
}

dependencies {
    kapt(project(":processors:processor"))

    testImplementation("org.assertj:assertj-core:3.11.1")
    testImplementation("org.spekframework.spek2:spek-dsl-jvm:2.0.9")
    testRuntimeOnly("org.spekframework.spek2:spek-runtime-jvm:2.0.9")
}
