package external;

import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import jdk.tool.Tool;
import jdk.tool.ToolInstaller;
import jdk.tool.ToolProgram;

public record GoogleJavaFormatInstaller(String name) implements ToolInstaller {
  public GoogleJavaFormatInstaller() {
    this("google-java-format");
  }

  private Map<String, String> assets(String version) {
    var name = "google-java-format";
    var from = "https://github.com/google/" + name;
    if (version.equals("1.15.0")) {
      return Map.of(
          name + "-1.15.0-all-deps.jar",
          from + "/releases/download/v1.15.0/" + name + "-1.15.0-all-deps.jar#SIZE=3519780",
          "README.md",
          from + "/raw/v1.15.0/README.md#SIZE=6270");
    }
    if (version.equals("1.16.0")) {
      return Map.of(
          name + "-1.16.0-all-deps.jar",
          from + "/releases/download/v1.16.0/" + name + "-1.16.0-all-deps.jar#SIZE=3511159",
          "README.md",
          from + "/raw/v1.16.0/README.md#SIZE=6023");
    }
    return Map.of(
        (name + "-%s-all-deps.jar").formatted(version),
        (from + "/releases/download/v%1$s/" + name + "-%1$s-all-deps.jar").formatted(version),
        "README.md",
        (from + "/raw/v%s/README.md").formatted(version));
  }

  @Override
  public List<Tool> install(Path folder, String version) {
    Path jar = null;
    for (var asset : assets(version).entrySet()) {
      var source = URI.create(asset.getValue());
      var target = ToolInstaller.download(source, folder.resolve(asset.getKey()));
      if (jar == null && target.toString().endsWith(".jar")) jar = target;
    }
    var program = ToolProgram.findJavaDevelopmentKitTool("java", "-jar", jar).orElseThrow();
    return List.of(Tool.of(namespace(), name(), version, program));
  }
}
