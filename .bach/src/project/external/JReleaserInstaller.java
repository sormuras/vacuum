package external;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;
import jdk.tool.Tool;
import jdk.tool.ToolInstaller;
import jdk.tool.ToolProgram;

public record JReleaserInstaller(String name) implements ToolInstaller {
  public JReleaserInstaller() {
    this("jreleaser");
  }

  private String getPropertiesStringOf(String version) {
    return switch (version) {
      case "1.6.0" -> // language=properties
      """
      jreleaser-tool-provider-1.6.0.jar=https://github.com/jreleaser/jreleaser/releases/download/v1.6.0/jreleaser-tool-provider-1.6.0.jar#SIZE=38672254&SHA-256=c3dace207ed9ca3f2bb1c6c3e86871b351e091a58d238da3e583f415e1f548c5
      README.adoc=https://github.com/jreleaser/jreleaser/raw/v1.6.0/README.adoc#SIZE=6160
      """;
      case "1.5.1" -> // language=properties
      """
      jreleaser-tool-provider-1.5.1.jar=https://github.com/jreleaser/jreleaser/releases/download/v1.5.1/jreleaser-tool-provider-1.5.1.jar#SIZE=38467468&SHA-256=5a71d5c5ecf349b491f9d6db04bb3f5ffd79d7a6df9a42069466c9f02cfd8873
      README.adoc=https://github.com/jreleaser/jreleaser/raw/v1.5.1/README.adoc#SIZE=5527
      """;
      default -> // language=properties
      """
      jreleaser-tool-provider-%1$s.jar=https://github.com/jreleaser/jreleaser/releases/download/v%1$s/jreleaser-tool-provider-%1$s.jar
      README.adoc=https://github.com/jreleaser/jreleaser/raw/v%1$s/README.adoc
      """
          .formatted(version);
    };
  }

  @Override
  public List<Tool> install(Path folder, String version) {
    var properties = new Properties();
    try (var reader = new StringReader(getPropertiesStringOf(version))) {
      properties.load(reader);
    } catch (IOException exception) {
      throw new RuntimeException("Loading properties failed", exception);
    }
    Path jar = null;
    for (var name : properties.stringPropertyNames()) {
      if (name.startsWith("@")) continue;
      var source = URI.create(properties.getProperty(name));
      var target = ToolInstaller.download(source, folder.resolve(name));
      if (jar == null && target.toString().endsWith(".jar")) jar = target;
    }
    var program = ToolProgram.findJavaDevelopmentKitTool("java", "-jar", jar).orElseThrow();
    return List.of(Tool.of(namespace(), name(), version, program));
  }
}
