package name.iparraga.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import name.iparraga.model.MainClass;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CommandLineRunner {
	private final static String INPUT_FILE = "i";
	private final static String OUTPUT_BASE_DIR = "o";
	private final static String PACKAGE = "p";
	private final static String INCLUDE_JSP = "n";

	public static void main(String[] args) throws IOException {
		Options options = createOptions();
		CommandLine commandLine = null;
		try {
			commandLine = new GnuParser().parse(options, args);
		} catch (ParseException e) {
			gentlyExit(options);
		}
		checkOptions(commandLine, options);
		Configuration configuration = getOptions(commandLine);

		MainClass mainClass = executeParser(configuration);

		if (configuration.includeJsp) {
			Path filePath = Paths.get(configuration.inputFile);
			mainClass.addSourceJsp(getFileContent(filePath));
		}

		String code = mainClass.toCode();
		Path fileOutputPath = getFileOutputPath(configuration, mainClass);
		writeFile(fileOutputPath, code);
	}

	private static Options createOptions() {
		Options options = new Options();

		options.addOption(
			createOption(INPUT_FILE, "input-file", "the JSP input file"));

		options.addOption(
			createOption(
				OUTPUT_BASE_DIR, "output-dir",
				"the output directory where the class will be generated"));

		options.addOption(createOption(
			PACKAGE, "package", "the package where to place the generated file"));

		options.addOption(createOption(
			INCLUDE_JSP, "include-jsp",
			"if true, the generated file will include original file as a comment"));

		return options;
	}

	private static Option createOption(String shortName, String longName,
			String argDescription) {
		return new Option(shortName, longName, true, argDescription);
	}

	private static void checkOptions(CommandLine commandLine, Options options) {
		if (
				!commandLine.hasOption(INPUT_FILE)
				|| !commandLine.hasOption(OUTPUT_BASE_DIR)) {
			System.err.println("Invalid properties");
			gentlyExit(options);
		}
	}

	private static void gentlyExit(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp( "java -jar JavaParser.jar", options);
		System.exit(-1);
	}

	private static Configuration getOptions(CommandLine commandLine) {
		Configuration config = new Configuration();

		config.inputFile = commandLine.getOptionValue(INPUT_FILE);
		config.outputDirectory = commandLine.getOptionValue(OUTPUT_BASE_DIR);
		config.package_ = commandLine.getOptionValue(PACKAGE, "");
		String includeJsp = commandLine.getOptionValue(INCLUDE_JSP, "true");
		config.includeJsp = includeJsp.equalsIgnoreCase("true");

		return config;
	}

	private static MainClass executeParser(Configuration configuration)
			throws FileNotFoundException {

		JspParser parser = new JspParser(
				new FileReader(configuration.inputFile),
				configuration.inputFile, configuration.package_);
		return parser.run();
	}

	private static Path getFileOutputPath(
			Configuration config, MainClass mainClass) {
		return Paths.get(
				config.outputDirectory + File.separator +
				packageToFilePath(config.package_) + File.separator +
				mainClass.getClassName() + ".java");
	}

	private static String getFileContent(Path path) throws IOException {
		final byte[] bytes = Files.readAllBytes(path);
		return new String(bytes, Charset.forName("UTF8"));
	}

	private static void writeFile(Path path, String code)
			throws IOException {
		final byte[] bytes = code.getBytes(Charset.forName("UTF8"));
		Files.createDirectories(path.getParent());
		Files.write(path, bytes);
	}

	private static String packageToFilePath(String package_) {
		return package_.replaceAll(".", File.separator);
	}

	private static class Configuration {
		String inputFile;
		String outputDirectory;
		String package_;
		boolean includeJsp;
	}
}