package external;

import static java.lang.System.Logger.Level.DEBUG;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.spi.ToolProvider;
import jdk.tool.Tool;
import jdk.tool.ToolInstaller;
import jdk.tool.ToolProgram;
import jdk.tool.ToolRunner;
import jdk.tool.Toolbox;

public record EchoToolInstaller(String name) implements ToolInstaller {
  private static final System.Logger logger = System.getLogger(EchoToolInstaller.class.getName());

  public EchoToolInstaller() {
    this("echo");
  }

  @Override
  public List<Tool> install(Path folder, String version) throws Exception {
    var echoJava = folder.resolve("Echo.java");
    if (Files.notExists(echoJava)) {
      logger.log(DEBUG, "Writing Java source file -> {0}", folder.toUri());
      Files.writeString(echoJava, ECHO_JAVA);
    }
    var echoJar = folder.resolve("echo.jar");
    if (Files.notExists(echoJar)) {
      logger.log(DEBUG, "Compiling executable Java archive -> {0}", folder.toUri());
      var runner = (ToolRunner) Toolbox.ofSystemPrinter().withTool("javac", "jar");
      runner.run("javac", "--release", "9", echoJava.toString());
      runner.runCompactCommand(
          """
          jar
            --create
            --file %s
            --main-class Echo
            -C %s .
          """
              .formatted(echoJar, folder));
    }
    var namespace = namespace();
    var name = name();
    var echoTool = new EchoTool();
    var echoSource = ToolProgram.findJavaDevelopmentKitTool("java", echoJava).orElseThrow();
    var echoBinary = ToolProgram.findJavaDevelopmentKitTool("java", "-jar", echoJar).orElseThrow();
    return List.of(
        Tool.of(namespace, name, version, echoTool),
        Tool.of(namespace, name, version + "-tool", echoTool),
        Tool.of(namespace, name + ".java", version, echoSource),
        Tool.of(namespace, name + ".jar", version, echoBinary));
  }

  private static final String ECHO_JAVA = // language=java
      """
      class Echo {
        public static void main(String... args) {
          System.out.println(args.length == 0 ? "<silence...>" : String.join(" ", args));
        }
      }
      """;

  record EchoTool(String name) implements ToolProvider {
    EchoTool() {
      this("echo");
    }

    @Override
    public int run(PrintWriter out, PrintWriter err, String... args) {
      out.println(args.length == 0 ? "<silence...>" : String.join(" ", args));
      return 0;
    }
  }
}
