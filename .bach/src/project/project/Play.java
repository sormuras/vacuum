package project;

import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.spi.ToolProvider;
import jdk.tool.ToolProgram;
import jdk.tool.Toolbox;

public record Play(String name) implements ToolProvider {
  public static void main(String... args) {
    Toolbox.ofSystemPrinter().run(new Play(), args);
  }

  public Play() {
    this("play");
  }

  @Override
  public int run(PrintWriter out, PrintWriter err, String... args) {
    Toolbox.of(out, err)
        .withTool(ToolProgram.findJavaDevelopmentKitTool("java").orElseThrow())
        .run(
            "java",
            "--module-path",
            Path.of(".bach", "out", "main", "modules").toString(),
            "--module",
            "de.caladon.vacuum.game/de.caladon.vacuum.game.Game");
    return 0;
  }
}
