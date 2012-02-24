package de.joe.m2e.apt;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.apache.maven.plugin.MojoExecution;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
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

  private static final String BUNDLE_ID = "de.joe.m2e.apt";

  public void configure(ProjectConfigurationRequest request, IProgressMonitor monitor) throws CoreException {

  }

  public void configureRawClasspath(ProjectConfigurationRequest request, IClasspathDescriptor classpath,
      IProgressMonitor monitor) throws CoreException {
    final ILog log = Platform.getLog(Platform.getBundle(BUNDLE_ID));
    

    final IProject project = request.getProject();
    final IJavaProject javaProject = JavaCore.create(project);
    final IMavenProjectFacade facade = request.getMavenProjectFacade();

    List<MojoExecution> exec = getMojoExecutions(request, monitor);
    Xpp3Dom configuration = exec.get(0).getConfiguration();


    try {
      String sourceAttribute = configuration.getChild("sourceOutputDirectory").getValue();
      if (sourceAttribute != null)
        createSource(classpath, project, javaProject, facade, sourceAttribute);
    } catch (Throwable e) {
      log.log(new Status(IStatus.ERROR, BUNDLE_ID, "Cant set sourceOutputDirectory to Buildpath", e));
    }
    try {
      String resouceAttribute = configuration.getChild("outputDirectory").getValue();
      if (resouceAttribute != null)
        createSource(classpath, project, javaProject, facade, resouceAttribute);

    } catch (Throwable e) {
      log.log(new Status(IStatus.ERROR, BUNDLE_ID, "Cant set sourceOutputDirectory to Buildpath", e));
    }
  }

  protected void createSource(IClasspathDescriptor classpath, final IProject project, final IJavaProject javaProject,
      final IMavenProjectFacade facade, String sourceAttribute) throws IOException, CoreException {
    if (!new File(sourceAttribute).isAbsolute()) {
      sourceAttribute = project.getLocationURI().toString().replace("file:/", "") + "/" + sourceAttribute;
    }
    final File file = new File(sourceAttribute);
    final File canonicalFile = file.getCanonicalFile();
    if (!canonicalFile.exists())
      canonicalFile.mkdirs();
    URI uri = canonicalFile.toURI();
    uri = project.getPathVariableManager().convertToRelative(uri, false, "TEST");

    final String relative = uri.toString().replace("PROJECT_LOC", "");
    IFolder folder = project.getFolder(relative);

    IPath path = folder.getProjectRelativePath();
    path = javaProject.getPath().append(path);


    if (!classpath.containsPath(path))
      classpath.addSourceEntry(path, facade.getOutputLocation(), true);
  }

  public void configureClasspath(IMavenProjectFacade facade, IClasspathDescriptor classpath, IProgressMonitor monitor)
      throws CoreException {
    // All Done in getRawClassPath
  }
}
