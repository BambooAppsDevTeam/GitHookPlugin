package eu.bambooapps.gradle.plugin.githook

import org.gradle.api.file.DirectoryProperty

interface GitHooksExtension {
    val gitHooksDirectory: DirectoryProperty
    val gitDirectory: DirectoryProperty
}
