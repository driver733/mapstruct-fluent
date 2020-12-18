plugins {
    id("com.driver733.gradle-kotlin-setup-plugin")
}

dependencies {
    kapt(project(":processors:processor"))
}
