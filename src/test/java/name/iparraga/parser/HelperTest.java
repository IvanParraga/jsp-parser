package name.iparraga.parser;

import name.iparraga.model.Code;
import name.iparraga.model.MainClass;

import org.mockito.Mockito;
import org.testng.annotations.Test;

public class HelperTest {
	@Test
	public void addCode_removePageContextNoNewLine() {
		MainClass mainClassMock = Mockito.mock(MainClass.class);

		String code = "<% System.exit();\nPageContextFactory.setPageContext(pageContext); %>";
		Helper helper = new Helper(mainClassMock);
		helper.addCode(code);

		Mockito.verify(mainClassMock).addCode(new Code(" System.exit();\n "));
	}

	@Test
	public void addCode_removePageContextNewLine() {
		MainClass mainClassMock = Mockito.mock(MainClass.class);

		String code = "<% System.exit();\nPageContextFactory.setPageContext(pageContext);\n %>";
		Helper helper = new Helper(mainClassMock);
		helper.addCode(code);

		Mockito.verify(mainClassMock).addCode(new Code(" System.exit();\n "));
	}

	@Test
	public void addCode_replacePageContextByRequestContext() {
		MainClass mainClassMock = Mockito.mock(MainClass.class);

		String code =
			"<% System.exit();\n"
			+ "PageContextFactory.setPageContext(pageContext);\n"
			+ "pageContext.getAttribute(\"a\");\n"
			+ " %>";
		Helper helper = new Helper(mainClassMock);
		helper.addCode(code);

		String expectedCode =
				" System.exit();\n"
				+ "request.getAttribute(\"a\");\n ";
		Mockito.verify(mainClassMock).addCode(new Code(expectedCode));
	}
}
