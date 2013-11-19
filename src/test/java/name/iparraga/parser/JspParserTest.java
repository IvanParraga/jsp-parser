package name.iparraga.parser;

import static org.testng.Assert.assertEquals;

import java.io.FileNotFoundException;

import name.iparraga.model.Code;
import name.iparraga.model.Import;
import name.iparraga.model.MainClass;

import org.testng.annotations.Test;

public class JspParserTest {
//	@Test
//	public void run() throws FileNotFoundException {
//		String path = getPath("campanya_add_dominio_run.jsp");
//		JspParser parser = new JspParser(path);
//		parser.run();
//	}

	@Test
	public void recognizeImportDirective() throws FileNotFoundException {
		String path = getPath("LanguageDirective.jsp");
		JspParser parser = new JspParser(path);

		MainClass actualMainClass = parser.run();

		MainClass expectedMainClass = new MainClass("foo", "foo", "foo");
		expectedMainClass.addImport(new Import("java.rmi.NoSuchObjectException"));
		expectedMainClass.addImport(new Import("java.lang.Exception"));
		assertEquals(actualMainClass.toCode(), expectedMainClass.toCode());
	}

	@Test
	public void ignoreOtherDirectives() throws FileNotFoundException {
		String path = getPath("OtherDirectives.jsp");
		JspParser parser = new JspParser(path);

		MainClass actualMainClass = parser.run();

		MainClass expectedMainClass = new MainClass("foo", "foo", "foo");
		expectedMainClass.addImport(new Import("java.rmi.NoSuchObjectException"));
		assertEquals(actualMainClass.toCode(), expectedMainClass.toCode());
	}

	@Test
	public void scriptlet() throws FileNotFoundException {
		String path = getPath("Scriptlet.jsp");
		JspParser parser = new JspParser(path);

		MainClass actualMainClass = parser.run();

		MainClass expectedMainClass = new MainClass("foo", "foo", "foo");
		expectedMainClass.addCode(new Code("// some intereting code"));
		assertEquals(actualMainClass.toCode(), expectedMainClass.toCode());
	}


	private String getPath(String file) {
		return getClass().getResource(file).getPath();
	}
}
