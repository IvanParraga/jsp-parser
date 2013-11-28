package name.iparraga.parser;

import static org.testng.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import name.iparraga.model.BundleVariable;
import name.iparraga.model.Code;
import name.iparraga.model.HtmlCode;
import name.iparraga.model.Import;
import name.iparraga.model.MainClass;

import org.testng.annotations.Test;

public class JspParserTest {
	@Test
	public void run() throws IOException {
		String path = getPath("campanya_add_dominio_run.jsp");
		JspParser parser = new JspParser(getFileContent(path), path, "", "/thing.jsp");
		parser.run();
	}

	private JspParser getParser(String jspName) throws FileNotFoundException {
		String path = getPath(jspName + ".jsp");
		return new JspParser(getFileContent(path), path, "foo", "/" + jspName + ".jsp");
	}

	@Test
	public void recognizeImportDirective() throws IOException {
		String jspName = "LanguageDirective";
		JspParser parser = getParser(jspName);

		MainClass actualMainClass = parser.run();

		MainClass expectedMainClass = getExpectedClass(jspName);
		expectedMainClass.addImport(new Import("java.rmi.NoSuchObjectException"));
		expectedMainClass.addImport(new Import("java.lang.Exception"));
		assertEquals(actualMainClass.toCode(), expectedMainClass.toCode());
	}

	@Test
	public void ignoreOtherDirectives() throws IOException {
		String jspName = "OtherDirectives";
		JspParser parser = getParser(jspName);

		MainClass actualMainClass = parser.run();

		MainClass expectedMainClass = getExpectedClass(jspName);
		expectedMainClass.addImport(new Import("java.rmi.NoSuchObjectException"));
		assertEquals(actualMainClass.toCode(), expectedMainClass.toCode());
	}

	@Test
	public void scriptlet() throws IOException {
		String jspName = "Scriptlet";
		JspParser parser = getParser(jspName);

		MainClass actualMainClass = parser.run();

		MainClass expectedMainClass = getExpectedClass(jspName);
		expectedMainClass.addCode(new Code("// some intereting code"));
		assertEquals(actualMainClass.toCode(), expectedMainClass.toCode());
	}

	@Test
	public void html() throws IOException {
		String jspName = "Html";
		JspParser parser = getParser(jspName);

		MainClass actualMainClass = parser.run();

		MainClass expectedMainClass = getExpectedClass(jspName);
		expectedMainClass.addCode(new HtmlCode("<html>"));
		expectedMainClass.addCode(new Code(" // some intereting code "));
		expectedMainClass.addCode(new HtmlCode("</html>"));
		assertEquals(actualMainClass.toCode(), expectedMainClass.toCode());
	}

	@Test
	public void attributeSet() throws IOException {
		String jspName = "AttributeSet";
		JspParser parser = getParser(jspName);

		MainClass actualMainClass = parser.run();

		MainClass expectedMainClass = getExpectedClass(jspName);
		expectedMainClass.addBundle(new SetBundle("locales.campanyas.campanyas_objetivos_create"));
		expectedMainClass.addBundle(new BundleVariable("key1","var1","request"));
		expectedMainClass.addBundle(new BundleVariable("key2","var2","request"));
		expectedMainClass.addBundle(new BundleVariable("key3","var3","session"));
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

		MainClass mainClass =  new MainClass("foo", className, apiUrl, apiPath);

		return mainClass;
	}
}
