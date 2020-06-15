package org.springframework.build.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ProjectDependency;

/**
 * Gradle plugin that automatically updates testCompile dependencies to include
 * the test source sets of project dependencies.
 *
 * @author Phillip Webb
 */
class TestSourceSetDependenciesPlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		project.afterEvaluate {
			Set<ProjectDependency> projectDependencies = new LinkedHashSet<ProjectDependency>()
			collectProjectDependencies(projectDependencies, project)
			projectDependencies.each {
				project.dependencies.add("testCompile", it.dependencyProject.sourceSets.test.output)
			}
		}
	}

	private void collectProjectDependencies(Set<ProjectDependency> projectDependencies, Project project) {
		for (def configurationName in ["compile", "optional", "provided", "testCompile"]) {
			Configuration configuration = project.getConfigurations().findByName(configurationName)
			if (configuration) {
				configuration.dependencies.findAll { it instanceof ProjectDependency }.each {
					projectDependencies.add(it)
					collectProjectDependencies(projectDependencies, it.dependencyProject)
				}
			}
		}
	}

}
