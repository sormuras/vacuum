package project;

import java.io.PrintWriter;
import jdk.tool.ToolCall;
import jdk.tool.ToolInstaller;
import jdk.tool.Toolbox;
import run.bach.Bach;
import run.bach.BachOperator;

public record Format(String name) implements BachOperator {
  public static void main(String... args) {
    var installer = ToolInstaller.installer(".bach/var/cache/toolbox");
    var tools = installer.install("google-java-format", "1.17.0");
    var toolbox = Toolbox.ofSystemPrinter().withTools(tools);
    var bach = new Bach(toolbox);
    bach.run(new Format(), args);
  }

  public Format() {
    this("format");
  }

  @Override
  public int run(PrintWriter out, PrintWriter err, String... args) {
    var format = ToolCall.of("google-java-format");
    if (args.length != 0) {
      run(format.with(args));
    } else {
      run(format.with("--replace").withFindFiles("**/*.java"));
    }
    return 0;
  }
}
