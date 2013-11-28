lexer grammar jsplexer;

@lexer::members {
boolean inJsp = false;
}

PAGE : 'page' {inJsp}?;

EQUAL : '=' {inJsp}?;

IMPORT : 'import' {inJsp}?;

STATIC : 'static' {inJsp}?;

DOT : '.' {inJsp}?;

STAR : '*' {inJsp}?;

Identifier 
    :   JavaLetter JavaLetterOrDigit* {inJsp}? 
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
QUOTED_CONTENT : '"' ~[\r\n"]*? '"' {inJsp}?;
BUNDLE_VAR_OPEN : '<fmt:message' {!inJsp}? {inJsp=true;};
BUNDLE_SET_OPEN : '<fmt:setBundle' {!inJsp}? {inJsp=true;};
XML_OPEN : '<fmt:' {inJsp}? {inJsp=false;};
XML_CLOSE : '/>' {inJsp}? {inJsp=false;};
DIRECTIVE_OPEN : '<%@' {!inJsp}? {inJsp=true;}; 
DIRECTIVE_CLOSE : '%>' {inJsp}? {inJsp=false;};
DECLARATION_OPEN : '<%!' {!inJsp}? {inJsp=true;};
EXPRESSION_OPEN : '<%=' {!inJsp}? {inJsp=true;};
COMMENT_OPEN : '<%--' {!inJsp}? {inJsp=true;};
COMMENT_CLOSE : '--%>' {inJsp}? {inJsp=false;};
SCRIPTLET_OPEN : '<%' {!inJsp}? {inJsp=true;};
WS  :  [ \t\r\n\u000C]+ {inJsp}?;
OTHER : .+? {!inJsp}?;
OTHER_IN_JSP : .+? {inJsp}?;
