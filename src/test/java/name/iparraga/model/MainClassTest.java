package name.iparraga.model;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.annotations.Test;

public class MainClassTest {
	@Test
	public void generateEmptyClass() throws IOException {
		String package_ = "com.test";
		String className = "EmptyClass";
		String apiPath = "/test.jsp";
		String jspSourcePath = "modules/war/jsp/tests.jsp";

		MainClass emptyClass = new MainClass(package_, className, apiPath, jspSourcePath);

		String expectedCode = readClassFile(className);
		String actualCode = emptyClass.toCode();

		assertEquals(actualCode, expectedCode);
	}

	@Test
	public void generateCodeClass() throws IOException {
		String package_ = "com.test";
		String className = "CodeClass";
		String apiPath = "/test.jsp";
		String jspSourcePath = "modules/war/jsp/tests.jsp";

		MainClass codeClass = new MainClass(package_, className, apiPath, jspSourcePath);
		codeClass.addCode(new Code("	// some intereting code"));
		String actualCode = codeClass.toCode();

		String expectedCode = readClassFile(className);
		assertEquals(actualCode, expectedCode);
	}

	@Test
	public void generateCodeWithSource() throws IOException {
		String package_ = "com.test";
		String className = "JspSourceClass";
		String apiPath = "/test.jsp";
		String jspSourcePath = "modules/war/jsp/tests.jsp";

		MainClass jspSourceClass = new MainClass(package_, className, apiPath, jspSourcePath);
		jspSourceClass.addSourceJsp("<% /* original code */ %>");

		String expectedCode = readClassFile(className);
		String actualCode = jspSourceClass.toCode();

		assertEquals(actualCode, expectedCode);
	}

	private String readClassFile(String className) throws IOException {
		URI path = getPath(className + ".java");
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return Charset.forName("UTF8").decode(ByteBuffer.wrap(encoded)).toString();
	}

	private URI getPath(String file) {
		try {
			return getClass().getResource(file).toURI();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
}
