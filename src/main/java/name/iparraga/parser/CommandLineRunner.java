package name.iparraga.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import name.iparraga.model.MainClass;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandLineRunner {
	private static Logger logger = LoggerFactory.getLogger(CommandLineRunner.class);

	private final static String OUTPUT_BASE_DIR = "o";
	private final static String PACKAGE = "p";
	private final static String INCLUDE_JSP = "n";
	private static final String INPUT_BASE_DIR = "i";

	public static void main(String[] args) throws IOException {

		Options options = createOptions();
		CommandLine commandLine = null;
		try {
			commandLine = new GnuParser().parse(options, args);
		} catch (ParseException e) {
			gentlyExit(options);
		}
		checkOptions(commandLine, options);
		Configuration configuration = createConfiguration(commandLine);

		long initialTimeStamp = System.currentTimeMillis();
		for (String inputFile : configuration.inputFiles) {
			Path pathFromBaseDir = null;
			try {
				pathFromBaseDir =
						getPathRelativeToBaseDir(configuration.inputBaseDir, inputFile);
			} catch (IllegalArgumentException e) {
				logger.error(
						"Ignoring " + inputFile + " because has no base dir "
						+ configuration.inputBaseDir);

				continue;
			}

			Path pathDirectoryFromBaseDir = pathFromBaseDir.getParent();
			String actualPackage = createActualPackage(
					pathDirectoryFromBaseDir, configuration.package_);

			long iterationTimeStamp = System.currentTimeMillis();

			MainClass mainClass = parse(
					inputFile, actualPackage, pathFromBaseDir);

			postParsingAdjustments(configuration, inputFile, mainClass);

			String code = mainClass.toCode();
			Path fileOutputPath = getFileOutputPath(
					configuration.outputDirectory, actualPackage, mainClass);

			writeFile(fileOutputPath, code);

			logMetrics(
				initialTimeStamp, iterationTimeStamp, inputFile, fileOutputPath);
		}
	}

	private static void postParsingAdjustments(Configuration configuration,
			String inputFile, MainClass mainClass) throws IOException {
		Path filePath = Paths.get(inputFile);

		if (configuration.includeJsp) {
			mainClass.addSourceJsp(getFileContent(filePath));
		}

		if (inputFile.contains("ubicacion_create_run.jsp")) {
			Path parent = filePath.getParent();
			String ubication = parent.getName(parent.getNameCount() - 1).toString();
			String newClassName = mainClass.getClassName() + ubication;
			mainClass.setClassName(newClassName);
		}
	}

	private static void logMetrics(long initialTimeStamp, long iterationTimeStamp,
			String inputFile, Path outputFile) {
		long now = System.currentTimeMillis();

		long timeFromStart = now - initialTimeStamp;
		String timeFromStartStr =
				DurationFormatUtils.formatDurationHMS(timeFromStart);

		long timeIteration = now - iterationTimeStamp;
		String timeIterationStr =
				DurationFormatUtils.formatDurationHMS(timeIteration);

		logger.info(
				inputFile + '\t' + outputFile + '\t'
				+ timeIterationStr + '\t' + timeFromStartStr);
	}

	private static String createActualPackage(Path relativePath, String package_) {
		String pathToPackage = relativePath.toString().replaceAll("/", ".");
		if ("".equalsIgnoreCase(package_)) {
			return pathToPackage;
		}
		return package_ + "." + pathToPackage;
	}

	private static Path getPathRelativeToBaseDir(String inputBaseDirStr,
			String inputFilePathStr) {
		Path inputBaseDir = Paths.get(inputBaseDirStr);
		Path inputFilePath = Paths.get(inputFilePathStr);

		if (!inputFilePath.startsWith(inputBaseDir)) {
			throw new IllegalArgumentException();
		}

		return inputFilePath.subpath(
				inputBaseDir.getNameCount(),inputFilePath.getNameCount());
	}

	private static Options createOptions() {
		Options options = new Options();

		Option outputOption = createOption(
				OUTPUT_BASE_DIR, "output-dir",
				"the output directory where the class will be generated");
		outputOption.setRequired(true);
		options.addOption(outputOption);

		options.addOption(createOption(
			PACKAGE, "package", "the base package where to place the generated file"));

		options.addOption(createOption(
			INCLUDE_JSP, "include-jsp",
			"if true, the generated file will include original file as a comment"));

		Option inputBaseDir = createOption(
				INPUT_BASE_DIR, "input-base-dir",
				"generated classes' package will be relative to this path");
		inputBaseDir.setRequired(true);
		options.addOption(inputBaseDir);

		return options;
	}

	private static Option createOption(String shortName, String longName,
			String argDescription) {
		Option option = new Option(shortName, longName, true, argDescription);
		return option;
	}

	private static void checkOptions(CommandLine commandLine, Options options) {
		if (!commandLine.hasOption(OUTPUT_BASE_DIR)) {
			System.err.println("Invalid properties");
			gentlyExit(options);
		}
	}

	private static void gentlyExit(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		String commandLineSyntax = createCommandLineSyntax();
		formatter.printHelp(commandLineSyntax, options);

		System.exit(-1);
	}

	private static String createCommandLineSyntax() {
		return "java -jar JavaParser.jar <files-to-parse> -i <arg> -o <arg> -p <arg>";
	}

	private static Configuration createConfiguration(CommandLine commandLine) {
		Configuration config = new Configuration();

		config.inputBaseDir =
				commandLine.getOptionValue(CommandLineRunner.INPUT_BASE_DIR);

		config.inputFiles = commandLine.getArgs();
		config.outputDirectory = commandLine.getOptionValue(OUTPUT_BASE_DIR);
		config.package_ = commandLine.getOptionValue(PACKAGE, "");
		String includeJsp = commandLine.getOptionValue(INCLUDE_JSP, "true");
		config.includeJsp = includeJsp.equalsIgnoreCase("true");

		return config;
	}

	private static MainClass parse(
			String inputFile, String package_, Path apiPath)
			throws FileNotFoundException {

		JspParser parser = new JspParser(
				new FileReader(inputFile),
				inputFile,
				package_,
				apiPath.toString());
		try {
			return parser.run();
		} catch (Exception e) {
			throw new RuntimeException("Problems parsing " + inputFile, e);
		}
	}

	private static Path getFileOutputPath(
			String outputDirectory, String actualPackage, MainClass mainClass) {

		return Paths.get(
				outputDirectory + File.separator +
				packageToFilePath(actualPackage) + File.separator +
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
		return package_.replaceAll("\\.", File.separator);
	}

	private static class Configuration {
		String inputBaseDir;
		String[] inputFiles;
		String outputDirectory;
		String package_;
		boolean includeJsp;

		@Override
		public String toString() {
			return "Configuration [inputBaseDir=" + inputBaseDir
					+ ", inputFiles=" + Arrays.toString(inputFiles)
					+ ", outputDirectory=" + outputDirectory + ", package_="
					+ package_ + ", includeJsp=" + includeJsp + "]";
		}
	}
}