package name.iparraga.model;

public class ExpressionCode extends Code {

	public ExpressionCode(String content) {
		super(content);
	}

	@Override
	public void toCode(StringBuilder code) {
		String codeToWrite = this.code.trim();
		if ("".equals(codeToWrite)) {
			return;
		}

		code.append("\t\tout.print(");
		code.append(this.code);
		code.append(");\n");
	}
}
