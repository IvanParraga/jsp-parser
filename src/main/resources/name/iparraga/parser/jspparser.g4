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
	(
	directive 
	| comment 
	| scriptlet 
	| declaration 
	| bundleSet 
	| bundleVar
	| xmlEntity 
	| expression 
	| other)* 
	EOF
	{helper.debug("jspFile " + $jspFile.text);}
	;

directive 
	: (importDirective | otherDirective)
	;

importDirective : importOpen QUOTED_CONTENT WS? DIRECTIVE_CLOSE WS?
	{
		helper.debug("match import" + $importDirective.text);
		helper.addImport($QUOTED_CONTENT.text);
	} 
	;
	
otherDirective 
	: 
	DIRECTIVE_OPEN .*? DIRECTIVE_CLOSE WS?
	{helper.debug("match other directive" + $otherDirective.text);}
	;

importOpen : DIRECTIVE_OPEN WS? PAGE WS IMPORT WS? EQUAL WS?;
importDeclaration
    :   STATIC? qualifiedName (DOT STAR)?
    ; 

scriptlet 
	: SCRIPTLET_OPEN .*? DIRECTIVE_CLOSE WS?
	{helper.addCode($scriptlet.text);}
	;
	
expression 
	: EXPRESSION_OPEN .*? DIRECTIVE_CLOSE WS?
	{helper.addExpression($expression.text);}
	;	
	
comment
	: COMMENT_OPEN .*? COMMENT_CLOSE WS?
	{helper.addComment($comment.text);}
	;	
	
declaration
	: DECLARATION_OPEN .*? DIRECTIVE_CLOSE WS?
	{helper.addDeclaration($declaration.text);}
	;

bundleSet
	locals [
		java.util.Map<String, String> scopeVarMap = new java.util.HashMap<>();
	]
	: BUNDLE_SET_OPEN WS? (scopePairBundleSet)+ XML_CLOSE WS?	
	{helper.addBundleSet($scopeVarMap);}
	;

bundleVar
	locals [
		java.util.Map<String, String> scopeVarMap = new java.util.HashMap<>();
	]
	: BUNDLE_VAR_OPEN WS? (scopePairBundleVar)+ XML_CLOSE WS?	
	{helper.addBundleVar($scopeVarMap);}
	;
	
scopePairBundleVar
	: Identifier WS? EQUAL WS? QUOTED_CONTENT WS?
	{
		$bundleVar::scopeVarMap.put($Identifier.text, $QUOTED_CONTENT.text);
	}
	;
	
scopePairBundleSet
	: Identifier WS? EQUAL WS? QUOTED_CONTENT WS?
	{
		$bundleSet::scopeVarMap.put($Identifier.text, $QUOTED_CONTENT.text);
	}
	;	
	
xmlEntity
	: XML_OPEN .*? XML_CLOSE WS?
	{helper.debug("match xmlEntity: " + $xmlEntity.text);}
	;
	
other 
	: OTHER
	{helper.addHtml($OTHER.text);}
	;
	
qualifiedName
    :   Identifier (DOT Identifier)*
    ;
