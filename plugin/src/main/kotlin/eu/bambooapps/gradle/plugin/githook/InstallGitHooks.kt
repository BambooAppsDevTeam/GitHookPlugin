package eu.bambooapps.gradle.plugin.githook

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import javax.inject.Inject

abstract class InstallGitHooks : DefaultTask() {
    @get:Inject
    abstract val execOperations: ExecOperations

    @get:OutputDirectory
    abstract val gitHooksDestinationDirectory: DirectoryProperty

    @TaskAction
    fun runTask() {
        execOperations.exec {
            executable = "chmod"
            args = listOf("-R", "+x", gitHooksDestinationDirectory.asFile.get().path)
        }
    }
}
