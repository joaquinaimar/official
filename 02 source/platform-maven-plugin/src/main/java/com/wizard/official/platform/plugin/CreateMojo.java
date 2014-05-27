package com.wizard.official.platform.plugin;

import java.io.File;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "create")
public class CreateMojo extends AbstractMojo {

	@Parameter(defaultValue = "${project.compileSourceRoots}", readonly = true, required = true)
	private List<String> compileSourceRoots;

	@Parameter(defaultValue = "${project.compileClasspathElements}", readonly = true, required = true)
	private List<String> classpathElements;

	@Parameter(defaultValue = "${project.build.outputDirectory}")
	private File outputDirectory;

	public void execute() throws MojoExecutionException, MojoFailureException {

		for (int i = 0; i < compileSourceRoots.size(); i++)
			getLog().info(
					"compileSourceRoots" + i + ":" + compileSourceRoots.get(i));

		for (int i = 0; i < classpathElements.size(); i++)
			getLog().info(
					"classpathElements" + i + ":" + classpathElements.get(i));

		getLog().info("outputDirectory:" + outputDirectory.getPath());
	}
}
