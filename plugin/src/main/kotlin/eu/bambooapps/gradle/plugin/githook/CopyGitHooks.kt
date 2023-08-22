package eu.bambooapps.gradle.plugin.githook

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

abstract class CopyGitHooks : DefaultTask() {

    @get:InputDirectory
    abstract val gitHooksDirectory: DirectoryProperty

    @get:OutputDirectory
    abstract val gitHooksDestinationDirectory: DirectoryProperty

    @get:Inject
    abstract val fileSystemOperations: FileSystemOperations

    @TaskAction
    fun copyGitHooks() {
        fileSystemOperations.copy {
            from(gitHooksDirectory) {
                include("**/*.sh")
                rename("(.*).sh", "$1")
            }
            into(gitHooksDestinationDirectory)
        }
    }
}