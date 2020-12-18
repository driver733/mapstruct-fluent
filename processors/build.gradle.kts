plugins {
    id("com.driver733.gradle-kotlin-setup-plugin")
}

subprojects {
    dependencies {
        kapt("com.google.auto.service:auto-service:1.0-rc6")
        implementation("com.google.auto.service:auto-service:1.0-rc6")
        implementation("com.squareup:kotlinpoet:1.5.0")
    }
}

