grammar jspparser;

@members {
Helper helper = new Helper();
}

jspFile : 
	pageDirective
	| EOF {helper.debug("jspFile " + $jspFile.text);}
	;

pageDirective :
	'<%@page' AnyChar* '%>'
	;
	
AnyChar : .;
