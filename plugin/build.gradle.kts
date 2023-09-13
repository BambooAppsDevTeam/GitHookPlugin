@file:Suppress("UnstableApiUsage") // not a problem to use these incubating APIs

plugins {
    // Apply the Java Gradle plugin development plugin to add support for developing Gradle plugins
    `java-gradle-plugin`
    `kotlin-dsl`

    id("io.gitlab.arturbosch.detekt") version "1.23.1"
    id("com.gradle.plugin-publish") version "1.2.1"
}

kotlin {
    jvmToolchain(11)
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

testing {
    suites {
        // Configure the built-in test suite
        val test by getting(JvmTestSuite::class) {
            useKotlinTest(embeddedKotlinVersion)
        }

        // Create a new test suite
        val functionalTest by registering(JvmTestSuite::class) {
            useKotlinTest(embeddedKotlinVersion)

            dependencies {
                // functionalTest test suite depends on the production code in tests
                implementation(project())
            }

            targets {
                all {
                    // This test suite should run after the built-in test suite has run its tests
                    testTask.configure { shouldRunAfter(test) } 
                }
            }
        }
    }
}
version = "1.0.2"
group = "eu.bambooapps.gradle"

gradlePlugin {
    website = "https://github.com/bamboo-apps/GitHookPlugin"
    vcsUrl = "https://github.com/bamboo-apps/GitHookPlugin.git"
    val githook by plugins.creating {
        id = "eu.bambooapps.gradle.plugin.githook"
        displayName = "GitHook – store Git hooks in your Gradle project and add them to Git on demand"
        description =
            """A plugin that helps you with adding Git hooks to the project and ensuring that every developer has the same hooks
        """.trimMargin()
        tags = listOf("git", "git-hook")
        implementationClass = "eu.bambooapps.gradle.plugin.githook.GitHookPlugin"
    }
    testSourceSets.add(sourceSets["functionalTest"])
}

tasks.named("check") {
    // Include functionalTest as part of the check lifecycle
    dependsOn(testing.suites.named("functionalTest"))
}
