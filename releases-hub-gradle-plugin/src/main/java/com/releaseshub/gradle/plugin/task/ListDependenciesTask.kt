package com.releaseshub.gradle.plugin.task


import com.releaseshub.gradle.plugin.common.AbstractTask


open class ListDependenciesTask : AbstractTask() {

	var dependenciesFilesPaths = mutableListOf<String>()
	var includes = mutableListOf<String>()
	var excludes = mutableListOf<String>()

	init {
		description = "List all dependencies"
	}

	override fun onExecute() {
		dependenciesFilesPaths.forEach {
			println(it)
			project.rootProject.file(it).forEachLine { line ->
				val dependency = ArtifactExtractor.extractArtifact(line)
				if (dependency != null && dependency.match(includes, excludes)) {
					println(" - $dependency")
				}
			}
			println()
		}
	}

}
