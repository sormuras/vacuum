import run.bach.Configuration;
import run.bach.Configuration.Toolbox;
import run.bach.Configuration.Toolbox.Call;
import run.bach.Configuration.Toolbox.Task;
import run.bach.Configuration.Toolbox.Tool;

@Configuration(
    toolbox =
        @Toolbox(
            withLoadingToolProviderServices = true,
            tools = {"java", "jfr"},
            tasks = {
              @Task(name = "build", calls = @Call(tool = "project/build")),
              @Task(
                  name = "versions",
                  // parallel = true,
                  calls = {
                    @Call(tool = "jar", args = "--version"),
                    @Call(tool = "java", args = "--version"),
                    @Call(tool = "javac", args = "--version"),
                    @Call(tool = "jreleaser", args = "--version"),
                    @Call(tool = "google-java-format", args = "--version"),
                    @Call(tool = "maven", args = "--version")
                  })
            },
            withInstallingTools = {
              @Tool(name = "echo", version = "99"),
              @Tool(name = "google-java-format", version = "1.17.0"),
              @Tool(name = "jreleaser", version = "1.6.0"),
              @Tool(name = "maven", version = "3.9.2")
            }))
module project {
  requires run.bach;
  requires jdk.compiler; // provides "javac"
  requires jdk.jartool; // provides "jar"
  requires jdk.jlink; // provides "jlink" and "jmod"

  provides java.util.spi.ToolProvider with
      project.Build, // provides "build"
      project.Clean, // provides "clean"
      project.Compile, // provides "compile"
      project.Format, // provides "format", installs "google-java-format"
      project.Play; // provides "play" (via "java")
  provides jdk.tool.ToolInstaller with
      external.EchoToolInstaller, // installs "echo" tools
      external.GoogleJavaFormatInstaller, // installs "google-java-format" tool
      external.JReleaserInstaller, // installs "jreleaser" tool
      external.MavenInstaller; // installs "maven" tool
}
