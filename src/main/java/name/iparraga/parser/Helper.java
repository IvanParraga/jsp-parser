package name.iparraga.parser;

import java.util.Map;

import name.iparraga.model.Code;
import name.iparraga.model.Comment;
import name.iparraga.model.Declaration;
import name.iparraga.model.HtmlCode;
import name.iparraga.model.Import;
import name.iparraga.model.MainClass;
import name.iparraga.model.ScopeVariable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Helper {
	private static final Logger logger = LoggerFactory.getLogger(Helper.class);
	private final MainClass class_;
	private boolean startCode = false;

	public Helper(MainClass mainClass) {
		class_ = mainClass;
		logger.debug("Helper initialited\n");
	}

	public void addComment(String comment) {
		logger.debug("adding comment " + comment);
		String commentCode = comment.replace("<%--", "").replace("--%>","").trim();
		commentCode = "/*\n" + commentCode + "\n*/";

		if (startCode) {
			class_.addCode(new Code(commentCode));
		} else {
			class_.addComment(new Comment(commentCode));
		}
	}

	public void addDeclaration(String declaration) {
		logger.debug("adding declaration " + declaration);
		String declarationCode = declaration.replace("<%!", "").replace("%>","").trim();

		class_.addDeclaration(new Declaration(declarationCode));
	}


	public void addImport(String import_) {
		logger.debug("adding import " + import_);
		class_.addImport(new Import(unquote(import_)));
	}

	public void addCode(String code) {
		logger.debug("adding code " + code);
		startCode = true;
		code = code.replace("<%", "").replace("%>","");
		code = changePageContextToServletContext(code);
		class_.addCode(new Code(code));
	}

	public void addScopeVar(Map<String,String> var) {
		logger.debug("adding scope var " + var);
		String key = unquote(var.get("key"));
		String value = unquote(var.get("var"));
		String scope = unquote(var.get("scope"));
		ScopeVariable variable = new ScopeVariable(key, value, scope);
		class_.addScopeVariable(variable);
	}

	private String unquote(String quotedString) {
		quotedString = quotedString.substring(1);
		return quotedString.substring(0, quotedString.length() - 1);
	}

	private String changePageContextToServletContext(String code) {
		code = code.replaceAll(
			"PageContextFactory.setPageContext\\(pageContext\\);(\n)?", "");

		code = code.replaceAll("pageContext","request");
		return code;
	}

	public void addHtml(String content) {
		HtmlCode code = new HtmlCode(content);
		class_.addHtml(code);
	}

	public void debug(String msg) {
		logger.debug("-->" + msg + "<--");
	}


	public MainClass getMainClass() {
		return class_;
	}
}
