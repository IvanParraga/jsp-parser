package name.iparraga.model;

import java.util.LinkedList;
import java.util.List;

public class MainClass {
	List<ClassToken> tokens = new LinkedList<>();

	private final String package_;
	private final String className;


	public MainClass(String package_, String className) {
		super();
		this.package_ = package_;
		this.className = className;
	}

	public String toCode() {
		StringBuilder code = writePackage();
		writeCodeFromTokens(code);
		writeClassBody(code);

		return code.toString();
	}

	private StringBuilder writePackage() {
		StringBuilder code = new StringBuilder("package ");
		code.append(package_);
		code.append(";\n");
		return code;
	}

	private void writeCodeFromTokens(StringBuilder code) {
		for (ClassToken token : tokens) {
			token.toCode(code);
		}
	}

	private void writeClassBody(StringBuilder code) {
		code.append("public class ");
		code.append(className);
		code.append(" {\n}");
	}

	public void add(ClassToken token) {
		tokens.add(token);
	}
}
