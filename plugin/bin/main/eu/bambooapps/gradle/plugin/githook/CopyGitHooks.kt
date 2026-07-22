package eu.bambooapps.gradle.plugin.githook

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

@CacheableTask
abstract class CopyGitHooks : DefaultTask() {
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val gitHooksDirectory: DirectoryProperty

    @get:OutputDirectory
    abstract val gitHooksDestinationDirectory: DirectoryProperty

    @get:Inject
    abstract val fileSystemOperations: FileSystemOperations

    @TaskAction
    fun runTask() {
        fileSystemOperations.copy {
            from(gitHooksDirectory) {
                include("**/*.sh")
                rename("(.*).sh", "$1")
            }
            into(gitHooksDestinationDirectory)
        }
    }
}
