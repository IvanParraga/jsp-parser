package name.iparraga.parser;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStream;

public abstract class AntlrParser extends Parser {
	private final Helper helper;

	public AntlrParser(TokenStream input, Helper helper) {
		super(input);
		this.helper = helper;
	}
}
