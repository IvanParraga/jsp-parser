grammar jspparser;

@members {
Helper helper = new Helper();
}


	

options { 
	output = AST;
	rewrite = true;
}

tokens {
    ELEMENT;
    ATTRIBUTE;
    ATTRIBUTES;
    JSP_DIRECTIVE;
}

@lexer::members {
    
    int tags;
    enum Quote { NONE, SINGLE, DOUBLE};
    Quote quote = Quote.NONE;
    List<Integer> tagStack = new ArrayList<Integer>();
    List<Quote> quoteStack = new ArrayList<Quote>();
    
    void trim( int front, int back ) {
    	text = getText();
    	setText( text.substring( front, text.length() - back ) );
    }
    
    boolean inTag() {
    	return tags > 0;
   	}
   	
   	void pushTag() {
		tagStack.add( tags ); 
		tags = 0;
	}
	
	void popTag() {
	   	tags = tagStack.remove( tagStack.size() - 1 ); 
	}
	
	void pushQuote() {
		quoteStack.add( quote ); 
		quote = Quote.NONE;
	}		

	void popQuote() {
	   	quote = quoteStack.remove( quoteStack.size() - 1 ); 
	}
}

jspFile  
	: ( 
		child
		| PROCESSING_INSTRUCTION
		| DOCTYPE_DEFINITION
		| jspDirective
	) *
	EOF!
	;

child
	: element[false]
	| CDATA 
	| PCDATA
    | COMMENT 
	| jspCrap
	;
		

jspDirective
	: JSP_DIRECTIVE_OPEN name=GENERIC_ID ( 
		attributes+=attribute 
	) *
	JSP_DIRECTIVE_CLOSE
	-> ^( JSP_DIRECTIVE $name ^( ATTRIBUTES $attributes* ) )
	{helper.debug("!!!!!!!!!!!!!!" + $JSP_DIRECTIVE.text)}
	;

jspCrap
	: JSP_EXPRESSION 
	| JSP_SCRIPTLET 
	| JSP_COMMENT
	| EL_EXPR
	;

element[boolean attributeAllowed]
    : r=startTag^ (
    	TAG_CLOSE! ( (
    		( { attributeAllowed }?=> attribute )
    		| child
	    ) + endTag[$r.tagName]!
	    | endTag[$r.tagName] ) 
	    | TAG_EMPTY_CLOSE !
	)
    ;

startTag
	returns [ String tagName ]
	: TAG_START_OPEN name=GENERIC_ID ( 
		attributes+=attribute 
		| attributes+=jspCrap 
		| attributes+=element[true]
	) *
	{ $tagName = $name.text; } 
	-> ^( ELEMENT $name ^( ATTRIBUTES $attributes* ) )
	;

attribute
	: name=GENERIC_ID ATTR_EQ ATTR_VALUE_OPEN ( 
		value+=attributeValue
	) * ATTR_VALUE_CLOSE
	-> ^( ATTRIBUTE $name $value* )
	;

attributeValue
	: PCDATA 
	| element[false]
	| jspCrap
	; 


/* 
 * The result token for this rule is an empty JSP_COMMENT that helps distinguish between empty 
 * elements and elements with empty bodies. Also see rule 'element'. Note that the JSP comment
 * will be turned into an XML comment by Jsp2JspX so <foo></foo> will end up as <foo><!----></foo>. 
 * That way it will become <foo></foo> on the client side. Without the comment it would be collapsed 
 * to <foo/> by the JSP engine's XML parser and sent to the client like that. This would definetely 
 * break the page in any browser, unless it is served as application/xml+xhtml.   
 */
     
endTag[ String tagName ]
	: t=TAG_END_OPEN name=GENERIC_ID { $name.text.equals( $tagName ) }? TAG_CLOSE
	-> COMMENT[$t,""]
	;

CDATA 
	: '<![CDATA[' ( options { greedy = false; } : . )* ']]>' { trim( 9, 3 ); }
	;

COMMENT
	: { ! inTag() }?=> '<!--' ( options { greedy = false; } : . )* '-->' { trim( 4, 3 ); }
	;

TAG_START_OPEN 
	: '<' { tags++; pushQuote(); } 
	;

TAG_END_OPEN 
	: '</' { tags++; pushQuote(); } 
	;

TAG_CLOSE 
	: { inTag() }?=> '>' { tags--; popQuote(); } 
	;

TAG_EMPTY_CLOSE 
	: { inTag() }?=> '/>' { tags--; popQuote(); } 
	;

ATTR_EQ 
	: { inTag() }?=> '=' ;

ATTR_VALUE_OPEN
	: { inTag() }?=> (
		{ quote != Quote.DOUBLE }?=> '"' { quote = Quote.DOUBLE; }
		| { quote != Quote.DOUBLE }?=> '\'' { quote = Quote.SINGLE; }
	  )
	  { pushTag(); }
	;
	
ATTR_VALUE_CLOSE
	: { ! inTag() }?=> (
		  { quote == Quote.DOUBLE }?=> '"' 
		| { quote == Quote.SINGLE }?=> '\''
	)
	{ quote = Quote.NONE; popTag(); }
    ;

EL_EXPR 
	: '${' ( options { greedy = false; } : . )* '}' { trim( 2, 1 ); }
	;

PCDATA
	: { ! inTag() }?=> (
		  { quote == Quote.NONE }?=> ~ ( '<' ) ( ~ ( '<' | '$' ) )*
		| { quote == Quote.DOUBLE }?=> ~ ( '<' | '"' ) ( ~ ( '<' | '$' | '"' ) )* 
		| { quote == Quote.SINGLE }?=> ~ ( '<' | '\'' ) ( ~ ( '<' | '$' | '\'' ) )* 
	)
	;

GENERIC_ID
    : { inTag() }?=> ( LETTER | '_' | ':' ) ( NAMECHAR )*
    ;

fragment NAMECHAR : LETTER | DIGIT | '.' | '-' | '_' | ':' ;
fragment DIGIT : '0'..'9' ;
fragment LETTER : 'a'..'z' | 'A'..'Z' ;

WHITESPACE
	: { inTag() }?=> (' '|'\r'|'\t'|'\u000C'|'\n') { $channel = HIDDEN; } 
	;
    
PROCESSING_INSTRUCTION 
	: { ! inTag() }?=> '<?' ( options { greedy = false; } : . )* '?>' { trim( 2, 2 ); }
	;

DOCTYPE_DEFINITION
	: { ! inTag() }?=> '<!DOCTYPE' ( options { greedy = false; } : . )* '>'
	;
	
JSP_DIRECTIVE_OPEN
	: { ! inTag() }?=> '<%@' { tags++; }
	;

JSP_DIRECTIVE_CLOSE
	: { inTag() }?=> '%>' { tags--; }
	;

JSP_COMMENT
	: '<%--' ( options { greedy = false; } : . )* '--%>' { trim( 4, 4 ); }
	;

JSP_EXPRESSION
	: '<%=' ( options { greedy = false; } : . )* '%>' { trim( 3, 2 ); }
	;	

JSP_SCRIPTLET
	: '<%' ~( '@' | '=' | '-' ) ( options { greedy = false; } : . )* '%>' { trim( 2, 2 ); }
	;	

