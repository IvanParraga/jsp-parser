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
		String className = "Test";
		MainClass emptyClass = new MainClass(package_, className);

		String expectedCode = readFile("EmptyClass.java");
		String actualCode = emptyClass.toCode();

		assertEquals(actualCode, expectedCode);
	}

	@Test
	public void generateClassWithComments() throws IOException {
		String package_ = "com.test";
		String className = "Test";

		MainClass emptyClass = new MainClass(package_, className);
		emptyClass.add(new Comment("/**\n * Comment before\n */"));
		emptyClass.add(new Import("java.util.*"));
		emptyClass.add(new Comment("/**\n * Class comment\n */"));

		String expectedCode = readFile("ClassWithComments.java");
		String actualCode = emptyClass.toCode();
		assertEquals(actualCode, expectedCode);
	}

	private String readFile(String file) throws IOException {
		URI path = getPath(file);
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
