package com.wizard.official.platform.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "echo")
public class EchoMojo extends AbstractMojo {

	public void execute() throws MojoExecutionException {
		getLog().info("[com.wizard.official.platform.plugin]");
	}
}
