import java.net.URI
import java.util.*

plugins {
    id("com.driver733.gradle-kotlin-setup-plugin") version "1.1.3"
    `maven-publish`
    signing
    id("io.codearte.nexus-staging") version "0.21.2"
    id("de.marcphilipp.nexus-publish") version "0.4.0"
}



allprojects {
    repositories {
        mavenCentral()
    }

    group = "com.driver733.mapstruct-fluent"
    version = rootProject.version

    apply<de.marcphilipp.gradle.nexus.NexusPublishPlugin>()

    nexusPublishing {
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
    numberOfRetries = 100
    delayBetweenRetriesInMillis = 6000
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

                configure<PublishingExtension> {
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
                finalizedBy(
                        ":closeRepository",
                        ":releaseRepository"
                )
            }

            tasks.getByPath(":closeRepository").apply {
                mustRunAfter(releaseSubprojects().map { it.tasks.getByPath(":publishToSonatype") })
            }

            tasks.getByPath(":releaseRepository").apply {
                mustRunAfter(":closeRepository")
            }
        }

fun releaseSubprojects() =
        subprojects.filter { listOf("processor", "processor-spring").contains(it.name) }