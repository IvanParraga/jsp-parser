package name.iparraga.parser;

import java.io.FileNotFoundException;
import java.io.FileReader;

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

	public static void main(String[] args) throws FileNotFoundException {
		Options options = createOptions();
		CommandLine commandLine = null;
		try {
			commandLine = new GnuParser().parse(options, args);
		} catch (ParseException e) {
			gentlyExit(options);
		}
		checkOptions(commandLine, options);
		Configuration configuration = getOptions(commandLine);

		JspParser parser = new JspParser(
				new FileReader(configuration.inputFile),
				configuration.inputFile, configuration.package_);
		MainClass class_ = parser.run();
		System.out.print(class_.toCode());
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
		config.outputFile = commandLine.getOptionValue(OUTPUT_BASE_DIR);
		config.package_ = commandLine.getOptionValue(INPUT_FILE, "");
		String includeJsp = commandLine.getOptionValue(INCLUDE_JSP, "false");
		config.includeJsp = includeJsp.equalsIgnoreCase("true");

		return config;
	}

	private static class Configuration {
		String inputFile;
		String outputFile;
		String package_;
		boolean includeJsp;
	}
}