plugins {
    id("com.driver733.gradle-kotlin-setup-plugin")
}

dependencies {
    implementation(project(":processors:common"))
    implementation("com.squareup:kotlinpoet:1.5.0")
    implementation("com.google.auto.service:auto-service:1.0-rc6")
    kapt("com.google.auto.service:auto-service:1.0-rc6")
}
