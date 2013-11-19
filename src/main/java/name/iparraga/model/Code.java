package name.iparraga.model;

public class Code extends ClassToken {
	private final String code;


	public Code(String code) {
		super();
		this.code = code;
	}

	@Override
	public void toCode(StringBuilder code) {
		code.append(this.code);
		code.append("\n");
	}
}
