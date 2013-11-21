package name.iparraga.model;

public class Declaration extends ClassToken {
	public String declarationCode;

	public Declaration(String declarationCode) {
		this.declarationCode = declarationCode;
	}

	@Override
	public void toCode(StringBuilder code) {
		code.append(declarationCode);
		code.append("\n");
	}
}
