grammar jspparser;

script		: greeting* EOF;
greeting	: 'hello' Name;
 
Name		: ('a'..'z' | 'A'..'Z')+;

WS			: (' '|'\t'|'\r'|'\n')+ {skip();};
COMMENT		: '#'(~'\n')* {skip();};

ILLEGAL		: .;
