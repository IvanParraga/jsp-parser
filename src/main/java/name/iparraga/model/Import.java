package name.iparraga.model;

public class Import extends ClassToken {
	private final String import_;

	public Import(String import_) {
		this.import_ = import_;
	}

	public String getImport_() {
		return import_;
	}

	@Override
	public void toCode(StringBuilder code) {
		code.append("import ");
		code.append(import_);
		code.append(";\n");
	}
}
