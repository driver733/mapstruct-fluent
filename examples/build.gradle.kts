plugins {
    id("com.driver733.gradle-kotlin-setup-plugin")
}

subprojects {
    dependencies {
        testImplementation("io.kotest:kotest-assertions-compiler:4.3.0")
    }
}

