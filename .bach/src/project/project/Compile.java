package project;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.List;
import java.util.spi.ToolProvider;
import jdk.tool.Toolbox;

public record Compile(String name) implements ToolProvider {
  public static void main(String... args) {
    Toolbox.ofSystemPrinter().run(new Compile(), args);
  }

  public Compile() {
    this("compile");
  }

  @Override
  public int run(PrintWriter out, PrintWriter err, String... args) {
    var modules = List.of("de.caladon.vacuum.game");
    var classes = Path.of(".bach", "out", "main", "classes", "java-" + Runtime.version().feature());

    var runner = Toolbox.of(out, err).withTool("javac", "jar");
    runner.run(
        "javac",
        "--module",
        String.join(",", modules),
        "--module-source-path",
        "./*/main/java".replace('/', File.separatorChar),
        "-d",
        classes.toString());

    modules.stream()
        .parallel()
        .forEach(
            module ->
                runner.run(
                    "jar",
                    "--create",
                    "--file",
                    Path.of(".bach", "out", "main", "modules", module + ".jar").toString(),
                    "-C",
                    classes.resolve(module).toString(),
                    "."));
    return 0;
  }
}
