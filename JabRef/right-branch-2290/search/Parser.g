header {
package net.sf.jabref.search;
import java.io.StringReader;
}

class SearchExpressionParser extends Parser;

options {
	importVocab = SearchExpressionLexer; // use vocab generated by lexer
	exportVocab = SearchExpressionParser;
	defaultErrorHandler = false;
	buildAST = true;
	k = 3;
}

tokens {
	RegularExpression;
	And;
	Or;
	Not;
	ExpressionSearch;
}

// ---------- Java Source Code ----------

{
	public boolean caseSensitive = false;
    public boolean regex = true;
	/** Creates a parser and lexer instance and tests the specified String.
	  * Returns the AST if s is in valid syntax for advanced field search, null otherwise. */
	public static AST checkSyntax(String s, boolean caseSensitive, boolean regex) {
		// Is there some way to prevent instance creation here?
		// How can a parser and/or lexer be reused?
		SearchExpressionParser parser = new SearchExpressionParser(new SearchExpressionLexer(
				new StringReader(s)));
		parser.caseSensitive = caseSensitive;
		parser.regex = regex;
		try {
			parser.searchExpression();
			return parser.getAST();
		} catch (Exception e) {
			return null;
		}
	}
}

// ---------- Text and Regular Expressions ----------

quotedRegularExpression[boolean caseSensitive, boolean regex]:
		var_s:STRING
			{
				## = astFactory.make((new ASTArray(2)).add(new RegExNode(RegularExpression,var_s.getText(),caseSensitive,regex)).add(##));
			}
		;

simpleRegularExpression[boolean caseSensitive, boolean regex]:
		var_s:FIELDTYPE
			{
				## = astFactory.make((new ASTArray(2)).add(new RegExNode(RegularExpression,var_s.getText(),caseSensitive,regex)).add(##));
			}
		;

// ---------- Condition and Expressions ----------

searchExpression:
	condition EOF;

condition:
		(expression LITERAL_and condition) => expression LITERAL_and! condition { ## = #( [And], ##); }
		|
		(expression LITERAL_or condition) => expression LITERAL_or! condition { ## = #( [Or], ##); }
		|
		expression // negation is done in expression
		;

expression:
		expressionSearch
		|
		LPAREN! condition RPAREN!
		|
		LITERAL_not! expressionSearch { ## = #( [Not], ## ); }         // NOT single expression
		|
		LITERAL_not! LPAREN! condition RPAREN! { ## = #( [Not], ## ); } // NOT ( ... )
		;

expressionSearch:
		quotedRegularExpression[false,true] compareType quotedRegularExpression[caseSensitive,regex]
			{ ## = #( [ExpressionSearch], ## ); }
		|
		simpleRegularExpression[false,true] compareType quotedRegularExpression[caseSensitive,regex]
			{ ## = #( [ExpressionSearch], ## ); }
		|
		simpleRegularExpression[false,true] compareType simpleRegularExpression[caseSensitive,regex]
			{ ## = #( [ExpressionSearch], ## ); }
		|
		quotedRegularExpression[false,true] compareType simpleRegularExpression[caseSensitive,regex]
			{ ## = #( [ExpressionSearch], ## ); }
		;

compareType:
		LITERAL_contains | LITERAL_matches | EQUAL | EEQUAL | NEQUAL
		;

