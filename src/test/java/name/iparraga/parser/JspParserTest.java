package name.iparraga.parser;

import static org.testng.Assert.assertEquals;

import java.io.FileNotFoundException;

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
	public void recognizeLanguageDirective() throws FileNotFoundException {
		String path = getPath("LanguageDirective.jsp");
		JspParser parser = new JspParser(path);

		MainClass actualMainClass = parser.run();

		MainClass expectedMainClass = new MainClass("foo", "foo", "foo");
		assertEquals(actualMainClass.toCode(), expectedMainClass.toCode());
	}


	private String getPath(String file) {
		return getClass().getResource(file).getPath();
	}
}
