grammar jspparser;

@parser::members {
Helper helper;

public jspparserParser(TokenStream input, Helper helper) {
	this(input);
	this.helper = helper;
}
}

jspFile : 
	.*? (directive | comment | scriptlet | declaration | xmlEntity)* WS? EOF
	{helper.debug("jspFile " + $jspFile.text);}
	;

directive 
	: (importDirective | otherDirective)
	{helper.debug("match directive" + $directive.text);}
	;

importDirective : importOpen '"' importDeclaration '"' (WS|ANY)* DIRECTIVE_CLOSE WS?
	{helper.addImport($importDeclaration.text);} 
	;
	
otherDirective : DIRECTIVE_OPEN .*? DIRECTIVE_CLOSE WS?;

importOpen : DIRECTIVE_OPEN WS? 'page' WS 'import' WS? '=' WS?;
importDeclaration
    :   'static'? qualifiedName ('.' '*')?
    ; 

scriptlet 
	: SCRIPTLET_OPEN .*? DIRECTIVE_CLOSE WS?
	{helper.addCode($scriptlet.text);}
	;
	
comment
	: COMMENT_OPEN .*? COMMENT_CLOSE WS?
	{helper.addComment($comment.text);}
	;	
	
declaration
	: DECLARATION_OPEN .*? DIRECTIVE_CLOSE WS?
	{helper.addDeclaration($declaration.text);}
	;
	
xmlEntity
	: '<' .*? '/>' WS?
	{helper.debug("match xmlEntity: " + $xmlEntity.text);}
	;
	
qualifiedName
    :   Identifier ('.' Identifier)*
    ;

Identifier
    :   JavaLetter JavaLetterOrDigit*
    ;

fragment
JavaLetter
    :   [a-zA-Z$_] // these are the "java letters" below 0xFF
    |   // covers all characters above 0xFF which are not a surrogate
        ~[\u0000-\u00FF\uD800-\uDBFF]
        {Character.isJavaIdentifierStart(_input.LA(-1))}?
    |   // covers UTF-16 surrogate pairs encodings for U+10000 to U+10FFFF
        [\uD800-\uDBFF] [\uDC00-\uDFFF]
        {Character.isJavaIdentifierStart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)))}?
    ;

fragment
JavaLetterOrDigit
    :   [a-zA-Z0-9$_] // these are the "java letters or digits" below 0xFF
    |   // covers all characters above 0xFF which are not a surrogate
        ~[\u0000-\u00FF\uD800-\uDBFF]
        {Character.isJavaIdentifierPart(_input.LA(-1))}?
    |   // covers UTF-16 surrogate pairs encodings for U+10000 to U+10FFFF
        [\uD800-\uDBFF] [\uDC00-\uDFFF]
        {Character.isJavaIdentifierPart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)))}?
    ;

DIRECTIVE_OPEN : '<%@'; 
DIRECTIVE_CLOSE : '%>';
DECLARATION_OPEN : '<%!';
SCRIPTLET_OPEN : '<%'; 
COMMENT_OPEN : '<%--';
COMMENT_CLOSE : '--%>';
WS  :  [ \t\r\n\u000C]+;
ANY : .;
