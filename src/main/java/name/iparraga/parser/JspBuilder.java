package name.iparraga.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import name.iparraga.model.MainClass;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.BufferedTokenStream;
import org.antlr.runtime.RecognitionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JspBuilder {
	private static Logger logger = LoggerFactory.getLogger(JspBuilder.class);

	private final List<SyntacticProblem> errors = new ArrayList<>();

	private final InputStream input;

	public JspBuilder(String filePath) throws FileNotFoundException {
		input = new FileInputStream(filePath);
	}

	public MainClass run() throws RecognitionException {
		ANTLRInputStream antlrStream;

		try {
			antlrStream = new ANTLRInputStream(input,"utf8");
			JspLexer lexer = new JspLexer(antlrStream);
			JspParser parser = new JspParser(new BufferedTokenStream(lexer));
			// parser.addErrorListener(this);
			parser.document();

			if (hasErrors()) {
				throw new RuntimeException("Problems parsing! "
						+ Arrays.toString(errors.toArray()));
			}

			 return parser.helper.getMainClass();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private boolean hasErrors() {
		return !errors.isEmpty();
	}

	private static class SyntacticProblem {
		int line;
		int charPositionInLine;
		String msg;

		public SyntacticProblem(int line, int charPositionInLine, String msg) {
			this.line = line;
			this.charPositionInLine = charPositionInLine;
			this.msg = msg;
		}

		@Override
		public String toString() {
			return "SyntacticProblem [line=" + line + ", charPositionInLine="
					+ charPositionInLine + ", msg=" + msg + "]";
		}
	}
}
