package name.iparraga.parser;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.testng.annotations.Test;

public class JspParserTest {
	@Test
	public void run() throws FileNotFoundException {

		String path = getPath("JspWithComments.jsp");
		JspParser parser = new JspParser(new FileReader(path));
		parser.run();
	}

	private String getPath(String file) {
		return getClass().getResource(file).getPath();
	}
}
