@file:Suppress("UnstableApiUsage") // not a problem to use these incubating APIs

plugins {
    // Apply the Java Gradle plugin development plugin to add support for developing Gradle plugins
    `java-gradle-plugin`
    `kotlin-dsl`

    id("io.gitlab.arturbosch.detekt") version "1.23.1"
    id("com.gradle.plugin-publish") version "1.2.1"
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

gradlePlugin {
    // Define the plugin
    val githook by plugins.creating {
        id = "eu.bambooapps.gradle.plugin.githook"
        implementationClass = "eu.bambooapps.gradle.plugin.githook.GitHookPlugin"
    }
}

gradlePlugin.testSourceSets.add(sourceSets["functionalTest"])

tasks.named<Task>("check") {
    // Include functionalTest as part of the check lifecycle
    dependsOn(testing.suites.named("functionalTest"))
}
