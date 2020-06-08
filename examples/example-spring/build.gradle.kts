plugins {
    id("com.driver733.gradle-kotlin-setup-plugin")
}

dependencies {
    implementation(project(":processors:processor-spring"))
    kapt(project(":processors:processor-spring"))

    implementation("org.springframework.boot:spring-boot-starter:2.3.0.RELEASE")

    testImplementation("org.springframework.boot:spring-boot-starter-test:2.3.0.RELEASE")
    testImplementation("org.assertj:assertj-core:3.11.1")
    testImplementation("org.spekframework.spek2:spek-dsl-jvm:2.0.9")
    testRuntimeOnly("org.spekframework.spek2:spek-runtime-jvm:2.0.9")
}

tasks.test {
    useJUnitPlatform()
}

