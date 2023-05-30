package project;

import java.io.PrintWriter;
import java.util.spi.ToolProvider;
import jdk.tool.Toolbox;

public record Clean(String name) implements ToolProvider {
  public static void main(String... args) {
    Toolbox.ofSystemPrinter().run(new Clean(), args);
  }

  public Clean() {
    this("clean");
  }

  @Override
  public int run(PrintWriter out, PrintWriter err, String... args) {
    return 0;
  }
}
