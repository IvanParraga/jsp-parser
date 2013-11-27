package name.iparraga.model;

public class HtmlCode extends Code {
	public HtmlCode(String code) {
		super(code);
	}

	public void appendCode(String code) {
		this.code += code;
	}

	@Override
	public void toCode(StringBuilder code) {
		String codeToWrite = this.code.trim();
		if ("".equals(codeToWrite)) {
			return;
		}

		code.append("\t\tout.print(");
		code.append(codeToWrite);
		code.append(")\n");
	}
}
