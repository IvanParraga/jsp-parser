package name.iparraga.parser;

import java.io.FileNotFoundException;

import org.testng.annotations.Test;

public class JspParserTest {
	@Test
	public void run() throws FileNotFoundException {

		String path = getPath("campanya_add_dominio_run.jsp");
		JspParser parser = new JspParser(path);
		parser.run();
	}

	private String getPath(String file) {
		return getClass().getResource(file).getPath();
	}
}
