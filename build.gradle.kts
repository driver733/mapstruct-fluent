import java.net.URI
import java.time.Duration
import java.util.*

plugins {
    id("com.driver733.gradle-kotlin-setup-plugin") version "6.0.2"
    id("io.codearte.nexus-staging") version "0.21.2"
    id("de.marcphilipp.nexus-publish") version "0.4.0"
    `maven-publish`
    signing
}

allprojects {
    repositories {
        mavenCentral()
        jcenter()
    }

    group = "com.driver733.mapstruct-fluent"
    version = rootProject.version

    apply<com.driver733.gradle.plugin.kotlinsetup.GradleKotlinSetupPluginPlugin>()
    apply<de.marcphilipp.gradle.nexus.NexusPublishPlugin>()

    dependencies {
        implementation("org.mapstruct:mapstruct:1.4.2.Final")
        kapt("org.mapstruct:mapstruct-processor:1.4.2.Final")
    }

    detekt {
        ignoreFailures = false
        autoCorrect = true
    }

    nexusPublishing {
        connectTimeout.set(Duration.ofMinutes(30))
        clientTimeout.set(Duration.ofMinutes(30))
        repositories {
            packageGroup.set("com.driver733")
            sonatype {
                if (extra.has("ossSonatypeUsername")) {
                    username.set(extra["ossSonatypeUsername"] as String)
                }
                if (extra.has("ossSonatypePassword")) {
                    password.set(extra["ossSonatypePassword"] as String)
                }
            }
        }
    }
}

nexusStaging {
    packageGroup = "com.driver733"
    numberOfRetries = 300
    delayBetweenRetriesInMillis = TimeUnit.SECONDS.toMillis(6).toInt()
    if (extra.has("ossSonatypeUsername")) {
        username = extra["ossSonatypeUsername"] as String
    }
    if (extra.has("ossSonatypePassword")) {
        password = extra["ossSonatypePassword"] as String
    }
}

releaseSubprojects()
        .onEach {
            with(it) {
                apply<SigningPlugin>()
                apply<MavenPublishPlugin>()
                apply<JavaPlugin>()

                tasks.withType(Javadoc::class) {
                    setExcludes(setOf("**/*.kt"))
                    options.encoding = "UTF-8"
                }

                configure<JavaPluginExtension> {
                    withSourcesJar()
                    withJavadocJar()
                }

                publishing {
                    publications {
                        create<MavenPublication>(name) {
                            from(components["java"])
                            pom {
                                name.set("Mapstruct-fluent")
                                description.set("Generator of fluent extension functions for mapstruct mappers")
                                url.set("https://github.com/driver733/mapstruct-fluent")
                                licenses {
                                    license {
                                        name.set("The MIT License")
                                        url.set("http://www.opensource.org/licenses/mit-license.php")
                                        distribution.set("repo")
                                    }
                                }
                                issueManagement {
                                    system.set("Github")
                                    url.set("https://github.com/driver733/mapstruct-fluent/issues")
                                }
                                developers {
                                    developer {
                                        id.set("driver733")
                                        name.set("Mikhail Yakushin")
                                        email.set("driver733@gmail.com")
                                    }
                                }
                                scm {
                                    connection.set("scm:git:git@github.com/driver733/mapstruct-fluent.git")
                                    developerConnection.set("scm:git:git@github.com/driver733/mapstruct-fluent.git")
                                    url.set("https://github.com/driver733/mapstruct-fluent")
                                }
                            }
                        }
                    }
                    repositories {
                        maven {
                            credentials {
                                if (extra.has("ossSonatypeUsername")) {
                                    username = extra["ossSonatypeUsername"] as String
                                }
                                if (extra.has("ossSonatypePassword")) {
                                    password = extra["ossSonatypePassword"] as String
                                }
                            }
                            val snapshotsRepoUrl = "https://oss.sonatype.org/content/repositories/snapshots/"
                            val releasesRepoUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
                            url = URI(if (version.toString().endsWith("-SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
                        }
                    }

                }

                signing {
                    if (extra.has("signing.password")) {
                        extra["signing.password"] = extra["signing.password"]
                                .let { s -> s as String }
                                .let { s -> Base64.getDecoder().decode(s) }
                                .toString(Charsets.UTF_8)
                    }
                    extra["signing.secretKeyRingFile"] = rootProject
                            .projectDir
                            .toPath()
                            .resolve("secrets/secring.gpg")
                            .toAbsolutePath()
                            .toString()
                    sign(publishing.publications[name])
                }

                tasks.javadoc {
                    if (JavaVersion.current().isJava9Compatible) {
                        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
                    }
                }

            }
        }.take(1).forEach { subproject ->
            subproject.tasks.getByPath(":publishToSonatype").apply {
                finalizedBy(":closeAndReleaseRepository")
            }
            tasks.getByPath(":closeAndReleaseRepository").apply {
                mustRunAfter(releaseSubprojects().map { it.path + ":publishToSonatype" })
            }

        }

exampleSubprojects()
        .forEach {
            it.dependencies {
                testImplementation("io.kotest:kotest-assertions-compiler:4.3.0")
            }
        }

processorSubprojects()
        .forEach {
            it.dependencies {
                kapt("com.google.auto.service:auto-service:1.0-rc6")
                implementation("com.google.auto.service:auto-service:1.0-rc6")
            }
        }

releaseSubprojects()
        .forEach {
            it.dependencies {
                implementation("com.squareup:kotlinpoet:1.5.0")
            }
        }

fun releaseSubprojects() =
        subprojects
                .filter { it.name in listOf("processor", "processor-spring", "common") }

fun processorSubprojects() =
        subprojects
                .filter { it.name in listOf("processor", "processor-spring") }

fun exampleSubprojects() =
        subprojects
                .filter { it.name in listOf("example", "example-spring") }
