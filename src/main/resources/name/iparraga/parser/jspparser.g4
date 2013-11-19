grammar jspparser;

@members {
Helper helper = new Helper();
}

jspFile : 
	(directive) * EOF 
	{helper.debug("jspFile " + $jspFile.text);}
	;

directive : importDirective | otherDirective;

importDirective : importOpen '"' importDeclaration '"' (WS|ANY)* DIRECTIVE_CLOSE WS?
	{helper.addImport($importDeclaration.text);} 
	;

otherDirective : DIRECTIVE_OPEN .*? DIRECTIVE_CLOSE;

importOpen : DIRECTIVE_OPEN WS? 'page' WS 'import' WS? '=' WS?;
importDeclaration
    :   'static'? qualifiedName ('.' '*')?
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
WS  :  [ \t\r\n\u000C]+;
ANY : .;
