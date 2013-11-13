package name.iparraga.parser;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JspParser implements ANTLRErrorListener {
	private static Logger logger = LoggerFactory.getLogger(JspParser.class);

	private final List<RecognitionException> errors = new LinkedList<>();

	private final Reader input;

	public JspParser(Reader input) {
		this.input = input;
	}

	public void reportError(RecognitionException e) {
		errors.add(e);
	}

	public void run() {
		ANTLRInputStream antlrStream;
		try {
			antlrStream = new ANTLRInputStream(input);
			jspparserLexer lexer = new jspparserLexer(antlrStream);
			jspparserParser parser = new jspparserParser(new BufferedTokenStream(
					lexer));
			parser.addErrorListener(this);
			parser.jspFile();
			logger.debug(parser.helper.getMainClass().toCode());

			if (hasErrors()) {
				logger.info(Arrays.toString(errors.toArray()));
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private boolean hasErrors() {
		return !errors.isEmpty();
	}

	@Override
	public void syntaxError(Recognizer<?, ?> recognizer,
			Object offendingSymbol, int line, int charPositionInLine,
			String msg, RecognitionException e) {
		errors.add(e);
	}

	@Override
	public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex,
			int stopIndex, boolean exact, BitSet ambigAlts, ATNConfigSet configs) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reportAttemptingFullContext(Parser recognizer, DFA dfa,
			int startIndex, int stopIndex, BitSet conflictingAlts,
			ATNConfigSet configs) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reportContextSensitivity(Parser recognizer, DFA dfa,
			int startIndex, int stopIndex, int prediction, ATNConfigSet configs) {
		// TODO Auto-generated method stub

	}
}
