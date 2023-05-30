package external;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import jdk.tool.Tool;
import jdk.tool.ToolInstaller;
import jdk.tool.ToolProgram;

public record MavenInstaller(String name) implements ToolInstaller {
  public MavenInstaller() {
    this("maven");
  }

  @Override
  public List<Tool> install(Path folder, String version) throws Exception {
    var base = "https://repo.maven.apache.org/maven2/org/apache/maven";
    var mavenWrapperProperties = folder.resolve("maven-wrapper.properties");
    if (Files.notExists(mavenWrapperProperties)) {
      Files.writeString(
          mavenWrapperProperties,
          // language=properties
          """
          distributionUrl=%s/apache-maven/%s/apache-maven-%s-bin.zip
          """
              .formatted(base, version, version));
    }
    var uri = URI.create(base + "/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar#SIZE=62547");
    var mavenWrapperJar = ToolInstaller.download(uri, folder.resolve("maven-wrapper.jar"));
    var mavenWrapperProgram =
        ToolProgram.findJavaDevelopmentKitTool(
                "java",
                "-D" + "maven.multiModuleProjectDirectory=.",
                "--class-path=" + mavenWrapperJar,
                "org.apache.maven.wrapper.MavenWrapperMain")
            .orElseThrow();
    return List.of(Tool.of(namespace(), name(), version, mavenWrapperProgram));
  }
}
