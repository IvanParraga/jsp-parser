package name.iparraga.parser;

import static org.testng.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import name.iparraga.model.Code;
import name.iparraga.model.Import;
import name.iparraga.model.MainClass;

import org.testng.annotations.Test;

public class JspParserTest {
	@Test
	public void run() throws FileNotFoundException {
		String path = getPath("campanya_add_dominio_run.jsp");
		JspParser parser = new JspParser(getFileContent(path), path, "");
		parser.run();
	}

	@Test
	public void recognizeImportDirective() throws FileNotFoundException {
		String jspName = "LanguageDirective";
		String package_ = "foo";
		String path = getPath(jspName + ".jsp");
		JspParser parser = new JspParser(getFileContent(path), path, package_);

		MainClass actualMainClass = parser.run();

		MainClass expectedMainClass = getExpectedClass(jspName);
		expectedMainClass.addImport(new Import("java.rmi.NoSuchObjectException"));
		expectedMainClass.addImport(new Import("java.lang.Exception"));
		assertEquals(actualMainClass.toCode(), expectedMainClass.toCode());
	}

	@Test
	public void ignoreOtherDirectives() throws FileNotFoundException {
		String jspName = "OtherDirectives";
		String package_ = "foo";
		String path = getPath(jspName + ".jsp");
		JspParser parser = new JspParser(getFileContent(path), path, package_);

		MainClass actualMainClass = parser.run();

		MainClass expectedMainClass = getExpectedClass(jspName);
		expectedMainClass.addImport(new Import("java.rmi.NoSuchObjectException"));
		assertEquals(actualMainClass.toCode(), expectedMainClass.toCode());
	}

	@Test
	public void scriptlet() throws FileNotFoundException {
		String jspName = "Scriptlet";
		String package_ = "foo";
		String path = getPath("Scriptlet.jsp");
		JspParser parser = new JspParser(getFileContent(path), path,package_);

		MainClass actualMainClass = parser.run();

		MainClass expectedMainClass = getExpectedClass(jspName);
		expectedMainClass.addCode(new Code("// some intereting code"));
		assertEquals(actualMainClass.toCode(), expectedMainClass.toCode());
	}


	private String getPath(String file) {
		return getClass().getResource(file).getPath();
	}

	private Reader getFileContent(String filePath) throws FileNotFoundException {
		Reader fileContent = new FileReader(filePath);
		return fileContent;
	}

	private MainClass getExpectedClass(String jspName) {
		String className = jspName;
		String apiUrl = "/" + jspName + ".jsp";
		String apiPath = getPath(jspName + ".jsp");

		return new MainClass(
				"foo",
				className,
				apiUrl,
				apiPath);
	}
}
