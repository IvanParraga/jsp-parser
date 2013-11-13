grammar jspparser;

@members {
Helper helper = new Helper();
}

jspFile : classToken+;
classToken : comment;
comment : '<%--' Text '--%>' {helper.addComment($Text.text);};
greeting : 'hello' Name;
 
Name : ('a'..'z' | 'A'..'Z')+;
Text : ('a'..'z' | 'A'..'Z' | WS)+;
WS: (' '|'\t'|'\r'|'\n')+ {skip();};
ILLEGAL : .;

