grammar jspparser;

@members {
Helper helper = new Helper();
}

jspFile : directive* | EOF {helper.debug("jspFile " + $jspFile.text);};
classToken : directive;
comment : '<%--' e=.* '--%>' {helper.addComment($e.text);};
directive : 'X' e=.* 'X' {helper.debug("Directive " + $e.text);};
directiveType : (importDirective | otherDirective);
importDirective : 'page' WS 'import="' qualifiedName '"' OPTIONAL_WS {helper.addImport($qualifiedName.text);};
otherDirective : LINE_ANY_TEXT;

qualifiedName
    :   Identifier ('.' Identifier)*
    ;
    
Identifier
    :   JavaLetter JavaLetterOrDigit*
    ;


WS: (' '|'\t'|'\r'|'\n')+ {skip();};
OPTIONAL_WS : WS*;
ILLEGAL : .;

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

ANY_TEXT
    :  .*? -> skip
    ;

LINE_ANY_TEXT
    :  ~[\r\n]* -> skip
    ;    

