grammar jspparser;
options {tokenVocab=jsplexer;}

@parser::members {
Helper helper;

public jspparserParser(TokenStream input, Helper helper) {
	this(input);
	this.helper = helper;
}
}


jspFile : 
	(directive | comment | scriptlet | declaration | scopeVar | xmlEntity)* WS? EOF
	{helper.debug("jspFile " + $jspFile.text);}
	;

directive 
	: (importDirective | otherDirective)
	{helper.debug("match directive" + $directive.text);}
	;

importDirective : importOpen QUOTED_CONTENT WS? DIRECTIVE_CLOSE WS?
	{helper.addImport($QUOTED_CONTENT.text);} 
	;
	
otherDirective : DIRECTIVE_OPEN .*? DIRECTIVE_CLOSE WS?;

importOpen : DIRECTIVE_OPEN WS? PAGE WS IMPORT WS? EQUAL WS?;
importDeclaration
    :   STATIC? qualifiedName (DOT STAR)?
    ; 

scriptlet 
	: SCRIPTLET_OPEN .*? SCRIPTLET_CLOSE WS?
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

scopeVar
	locals [
		java.util.Map<String, String> scopeVarMap = new java.util.HashMap<>();
	]
	: SCOPE_VAR_OPEN WS? (scopePair)+ XML_CLOSE WS?	
	{helper.addScopeVar($scopeVarMap);}
	;
	
scopePair
	: Identifier WS? EQUAL WS? QUOTED_CONTENT WS?
	{
		$scopeVar::scopeVarMap.put($Identifier.text, $QUOTED_CONTENT.text);
	}
	;
	
xmlEntity
	: XML_OPEN .*? XML_CLOSE WS?
	{helper.debug("match xmlEntity: " + $xmlEntity.text);}
	;
	
qualifiedName
    :   Identifier (DOT Identifier)*
    ;
