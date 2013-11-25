package name.iparraga.parser;

import name.iparraga.model.Code;
import name.iparraga.model.MainClass;

import org.mockito.Mockito;
import org.testng.annotations.Test;

public class HelperTest {
	@Test
	public void addCode_noNewLine() {
		MainClass mainClassMock = Mockito.mock(MainClass.class);

		String code = "<% System.exit();\nPageContextFactory.setPageContext(pageContext); %>";
		Helper helper = new Helper(mainClassMock);
		helper.addCode(code);

		Mockito.verify(mainClassMock).addCode(new Code(" System.exit();\n "));
	}

	@Test
	public void addCode_newLine() {
		MainClass mainClassMock = Mockito.mock(MainClass.class);

		String code = "<% System.exit();\nPageContextFactory.setPageContext(pageContext);\n %>";
		Helper helper = new Helper(mainClassMock);
		helper.addCode(code);

		Mockito.verify(mainClassMock).addCode(new Code(" System.exit();\n "));
	}
}
