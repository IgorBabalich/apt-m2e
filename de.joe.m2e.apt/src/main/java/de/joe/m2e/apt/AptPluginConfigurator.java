package de.joe.m2e.apt;

import java.util.List;

import org.apache.maven.plugin.MojoExecution;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.eclipse.m2e.core.project.configurator.AbstractProjectConfigurator;
import org.eclipse.m2e.core.project.configurator.ProjectConfigurationRequest;
import org.eclipse.m2e.jdt.IClasspathDescriptor;
import org.eclipse.m2e.jdt.IJavaProjectConfigurator;

/**
 * @author Stefan Wokusch
 */
public class AptPluginConfigurator extends AbstractProjectConfigurator implements IJavaProjectConfigurator {
  public void configure(ProjectConfigurationRequest request, IProgressMonitor monitor) throws CoreException {

  }

  public void configureRawClasspath(ProjectConfigurationRequest request, IClasspathDescriptor classpath,
      IProgressMonitor monitor) throws CoreException {

    final IProject project = request.getProject();
    final IJavaProject javaProject = JavaCore.create(project);
    final IMavenProjectFacade facade = request.getMavenProjectFacade();
    
    List<MojoExecution> exec = getMojoExecutions(request, monitor);
    Xpp3Dom configuration = exec.get(0).getConfiguration();
    
    String sourceAttribute = configuration.getChild("sourceOutputDirectory").getValue();
    IPath sourceGenerated=javaProject.getPath().append(sourceAttribute);
    classpath.addSourceEntry(sourceGenerated, facade.getOutputLocation(), true);
    
    String resouceAttribute = configuration.getChild("outputDirectory").getValue();
    
    // Only add Resources when it differs from the source
    if(!sourceAttribute.equals(resouceAttribute)){
      IPath resourceGenerated=javaProject.getPath().append(sourceAttribute);
    	classpath.addSourceEntry(resourceGenerated, facade.getOutputLocation(), true);
    }
  }

  public void configureClasspath(IMavenProjectFacade facade, IClasspathDescriptor classpath, IProgressMonitor monitor)
      throws CoreException {
    // All Done in getRawClassPath
  }
}
