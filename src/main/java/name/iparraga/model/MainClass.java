package name.iparraga.model;

import java.util.LinkedList;
import java.util.List;

public class MainClass {
	private final List<Comment> comments = new LinkedList<>();

	private final List<Import> imports = new LinkedList<>();

	private final List<Code> codes = new LinkedList<>();

	private static final String STANDARD_IMPORTS =
		"import java.io.IOException;\n" +
		"import java.io.StringWriter;\n" +
		"import java.io.Writer;\n" +
		"import javax.ejb.Stateless;\n" +
		"import javax.servlet.http.HttpServletRequest;\n" +
		"import javax.servlet.http.HttpSession;\n" +
		"import javax.ws.rs.GET;\n" +
		"import javax.ws.rs.Path;\n" +
		"import javax.ws.rs.Produces;\n" +
		"import javax.ws.rs.core.Context;\n";

	private final String package_;
	private final String className;
	private final String apiPath;
	private final String jspSourcePath;
	private String sourceJspCode;

	private StringBuilder code;

	public MainClass(String package_, String className,
			String apiPath, String jspSourcePath) {

		this.package_ = package_;
		this.className = className;
		this.apiPath = apiPath;
		this.jspSourcePath = jspSourcePath;
	}

	public String toCode() {
		code = new StringBuilder();
		writeMetadata();
		writePackage();
		writeStandardImports();
		writeAddedImports();
		writeComments();
		writeTags();
		writeClassBody();
		writeSourceJspCodeIfPresent();

		return code.toString();
	}

	private void writeMetadata() {
		code.append(
			"/*\n" +
			" * This class was automatically generated when transforming PPI to a JEE app\n" +
			" * on November 2013.\n" +
			" * \n" +
			" * The code of the generator can be found at:\n" +
			" * https://github.com/ivanator/jsp-parser\n" +
			" * \n" +
			" * Class derived from this source JSP:\n" +
			" * ");

		code.append(jspSourcePath);
		code.append("\n */\n");
	}

	private void writeComments() {
		for (Comment import_ : comments) {
			import_.toCode(code);
		}
	}

	private void writePackage() {
		if (!"".equalsIgnoreCase(package_)) {
			code.append("package ");
			code.append(package_);
			code.append(";\n");
		}
	}

	private void writeStandardImports() {
		code.append(STANDARD_IMPORTS);
	}

	private void writeAddedImports() {
		for (Import import_ : imports) {
			import_.toCode(code);
		}
	}

	private void writeTags() {
		code.append("@Stateless\n");
		code.append("@Path(\"");
		code.append(apiPath);
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
		writeDoRunMethodBegining();
		writeCodes();
		writeDoRunMethodEnding();
	}

	private void writeDoRunMethodBegining() {
		code.append("\t@GET\n");
		code.append("\t@Produces(\"application/json; charset=UTF-8\")\n");
		code.append("\tpublic String doRun(\n");
		code.append("\t\t\t@Context HttpServletRequest request,\n");
		code.append("\t\t\t@Context HttpSession session");
		code.append(") throws IOException {\n");
		code.append("\t\tWriter out = new StringWriter();\n");
	}

	private void writeCodes() {
		for (ClassToken token : codes) {
			token.toCode(code);
		}
	}

	private void writeDoRunMethodEnding() {
		code.append("\t\treturn out.toString();\n");
		code.append("\t}\n");
	}

	private void writeClassEnd() {
		code.append("}");
	}

	private void writeSourceJspCodeIfPresent() {
		if (sourceJspCode != null) {
			code.append("\n/*\nOriginal JSP code as follows:\n---- ---- ----\n");
			code.append(sourceJspCode);
			code.append("\n---- ---- ----\n*/");
		}
	}


	public void addImport(Import import_) {
		imports.add(import_);
	}

	@Override
	public int hashCode() {
		return toCode().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MainClass other = (MainClass) obj;

		String thisCode = toCode();
		String otherCode = other.toCode();

		return thisCode.equals(otherCode);
	}

	public void addCode(Code code) {
		codes.add(code);
	}

	public void addSourceJsp(String sourceJspCode) {
		this.sourceJspCode = sourceJspCode;
	}

	public String getClassName() {
		return className;
	}

	public void addComment(Comment comment) {
		comments.add(comment);
	}
}
