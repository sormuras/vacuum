package project;

import java.io.PrintWriter;
import java.util.List;
import java.util.spi.ToolProvider;
import jdk.tool.Toolbox;

public record Build(String name) implements ToolProvider {
  public static void main(String... args) {
    Toolbox.ofSystemPrinter().run(new Build(), args);
  }

  public Build() {
    this("build");
  }

  @Override
  public int run(PrintWriter out, PrintWriter err, String... args) {
    var runner = Toolbox.of(out, err).withTool(new Clean(), new Compile());
    if (List.of(args).contains("--clean")) runner.run("clean");
    runner.run("compile");
    return 0;
  }
}
