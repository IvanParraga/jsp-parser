package name.iparraga.model;

import java.util.LinkedList;
import java.util.List;

public class MainClass {
	List<ClassToken> tokens = new LinkedList<>();

	private static final String STANDARD_IMPORTS =
		"import java.io.IOException;\n" +
		"import java.io.StringWriter;\n" +
		"import java.io.Writer;\n" +
		"import javax.ejb.Stateless;\n" +
		"import javax.ws.rs.GET;\n" +
		"import javax.ws.rs.Path;\n";

	private final String package_;
	private final String className;
	private final String source;

	private StringBuilder code;

	public MainClass(String package_, String className, String source) {
		this.package_ = package_;
		this.className = className;
		this.source = source;
	}

	public String toCode() {
		code = new StringBuilder();
		writePackage();
		writeStandardImports();
		writeCodeFromTokens();
		writeTags();
		writeClassBody();

		return code.toString();
	}

	private StringBuilder writePackage() {
		code.append("package ");
		code.append(package_);
		code.append(";\n");
		return code;
	}

	private void writeStandardImports() {
		code.append(STANDARD_IMPORTS);
	}

	private void writeCodeFromTokens() {
		for (ClassToken token : tokens) {
			token.toCode(code);
		}
	}

	private void writeTags() {
		code.append("@Stateless\n");
		code.append("@Path(\"");
		code.append(source);
		code.append("\")\n");
	}

	private void writeClassBody() {
		writeClassStart();
		writeDoRunMethod();
		writeClassEnd();
	}

	private void writeClassStart() {
		code.append("public class ");
		code.append(className);
		code.append(" {\n");
	}

	private void writeDoRunMethod() {
		code.append("\t@GET\n");
		code.append("\tpublic String doRun() throws IOException {\n");
		code.append("\t\tWriter out = new StringWriter();\n");
		code.append("\t\treturn out.toString();\n");
		code.append("\t}\n");
	}

	private void writeClassEnd() {
		code.append("}");
	}

	public void add(ClassToken token) {
		tokens.add(token);
	}
}
