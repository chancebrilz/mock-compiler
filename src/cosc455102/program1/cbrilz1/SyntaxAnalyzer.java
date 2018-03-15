package cosc455102.program1.cbrilz1;

/**
 * COSC 455 Programming Languages: Implementation and Design.
 *
 * A Simple Lexical Analyzer Adapted from Sebesta (2010) by Josh Dehlinger
 * further modified by Adam Conover (2012-2018)
 *
 * This syntax analyzer implements a top-down, left-to-right, recursive-descent
 * parser based on the production rules for the simple English language provided
 * by Weber in Section 2.2. Helper methods to get, set and reset the error flag.
 *
 * @UPDATED: 03/15/2018 by Chance Brilz for COSC 455
 */

public class SyntaxAnalyzer {

	int nodeCount = 0;
	private final LexicalAnalyzer lexer; // The lexer which will provide the tokens

	/**
	 * The constructor initializes the terminal literals in their vectors.
	 */
	public SyntaxAnalyzer(LexicalAnalyzer lexer) {
		this.lexer = lexer;
	}

	/**
	 * Begin analyzing...
	 */
	public void analyze() throws ParseException {
		System.out.println("digraph ParseTree {");
		System.out.printf("\t{\"%s\" [label=\"PARSE TREE\" shape=diamond]};%n", nodeCount);

		start();

		System.out.println("}");

		// Open the default web browser.
		openWebGraphViz();
	}

	/**
	 * Invoke the Start Rule *
	 */
	private void start() throws ParseException {
		Sentence(nodeCount);
	}

	// <S> ::= <NP> <V> <NP> <EOS>
	protected void Sentence(int from) throws ParseException {
		int node = ++nodeCount;
		log("<S>", from, node);

		NounPhrase(node);
		Verb(node);
		NounPhrase(node);

		if(TOKEN.PREPOSITION == lexer.curToken) {
			PrepostionalNounPhrase(node);
		} else if(TOKEN.CONJUNCTION == lexer.curToken) {
			Conjuction(node);
			Sentence(node);
		}

		EndOfSentance(node);

	}

	// <NP> ::= <A> <AN>
	void NounPhrase(int from) throws ParseException {
		int node = ++nodeCount;
		log("<NP>", from, node);

		Article(node);

		AdjNoun(node);

		if(TOKEN.ADVERB == lexer.curToken) {
			Adverb(node);
		}

	}

	void PrepostionalNounPhrase(int from) throws ParseException {
		int node = ++nodeCount;
		log("<PNP>", from, node);

		if(TOKEN.PREPOSITION == lexer.curToken) {
			Preposition(node);
		}

		NounPhrase(node);

	}

	void AdjectiveTail(int from) throws ParseException {
		int node = ++nodeCount;
		log("<ADJ_TAIL>", from, ++nodeCount, lexer.lexemeBuffer.toString());

		Adjective(node);

		if(TOKEN.ADJECTIVE_DIVIDER == lexer.curToken) {
			lexer.parseNextToken(); // skip comma and go onto next Adjective
			AdjectiveTail(node);
		}
	}

	// <AN> ::= <ADJ> <N> | <N>  
	void AdjNoun(int from) throws ParseException {
		int node = ++nodeCount;
		log("<AN>", from, node);

		if (TOKEN.ADJECTIVE == lexer.curToken) {
			AdjectiveTail(node);
		}

		Noun(node);
	}

	void Conjuction(int from) throws ParseException {
		log("<CON>", from, ++nodeCount, lexer.lexemeBuffer.toString());

		if (TOKEN.CONJUNCTION != lexer.curToken) {
			raiseException(TOKEN.CONJUNCTION, from);
		}

		lexer.parseNextToken();
	}


	// This method implements the BNF rule for a verb
	void Verb(int from) throws ParseException {
		log("<V>", from, ++nodeCount, lexer.lexemeBuffer.toString());

		if (TOKEN.VERB != lexer.curToken) {
			raiseException(TOKEN.VERB, from);
		}

		lexer.parseNextToken();
	}

	void Adverb(int from) throws ParseException {
		log("<AD>", from, ++nodeCount, lexer.lexemeBuffer.toString());

		if (TOKEN.ADVERB != lexer.curToken) {
			raiseException(TOKEN.ADVERB, from);
		}

		lexer.parseNextToken();
	}

	// This method implements the BNF rule for a noun
	// <N> ::= dog | cat | rat
	void Noun(int from) throws ParseException {
		log("<N>", from, ++nodeCount, lexer.lexemeBuffer.toString());

		if (TOKEN.NOUN != lexer.curToken) {
			raiseException(TOKEN.NOUN, from);
		}

		lexer.parseNextToken();
	}

	// This method implements the BNF rule for an article
	// <A> ::= a | the
	void Article(int from) throws ParseException {
		log("<A>", from, ++nodeCount, lexer.lexemeBuffer.toString());

		if (TOKEN.ARTICLE != lexer.curToken) {
			raiseException(TOKEN.ARTICLE, from);
		}

		lexer.parseNextToken();
	}

	// This method implements the BNF rule for an adjective
	// <A> ::= a | the
	void Adjective(int from) throws ParseException {
		log("<ADJ>", from, ++nodeCount, lexer.lexemeBuffer.toString());

		if (TOKEN.ADJECTIVE != lexer.curToken) {
			raiseException(TOKEN.ADJECTIVE, from);
		}

		lexer.parseNextToken();
	}


	void Preposition(int from) throws ParseException {
		log("<PREP>", from, ++nodeCount, lexer.lexemeBuffer.toString());

		if (TOKEN.PREPOSITION != lexer.curToken) {
			raiseException(TOKEN.PREPOSITION, from);
		}

		lexer.parseNextToken();
	}

	// End of statement, however it's been defined.
	void EndOfSentance(int from) throws ParseException {
		log("<EOS>", from, ++nodeCount);

		if (TOKEN.EOS != lexer.curToken) {
			raiseException(TOKEN.EOS, from);
		}
	}

	// Show our progress as we go...
	private void log(String bnf, int from, int to) {
		final String t = "\t\"%s\" -> {\"%s\" [label=\"%s\", shape=oval]};%n";
		System.out.printf(t, from, to, bnf);
	}

	private void log(String bnf, int from, int to, String lexeme) {
		log(bnf, from, to);
		
		final String t = "\t\"%s\" -> {\"%s\" [label=\"%s\", shape=rect]};%n";
		System.out.printf(t, to, to + "_term", lexeme);
	}

	// Handle all of the errors in one place for cleaner parser code.
	private void raiseException(TOKEN expected, int from) throws ParseException {
		final String template = "SYNTAX ERROR: '%s' was expected but '%s' was found.";
		String err = String.format(template, expected.toString(), lexer.lexemeBuffer);

		System.out.printf("\t\"%s\" -> {\"%s\"};%n}%n", from, err);
		throw new ParseException(err);
	}

	/**
	 * To automatically open a browser...
	 */
	private void openWebGraphViz() {
		System.out.println("\nCopy/Paste the above output into: http://www.webgraphviz.com/\n");

		// Automatically open the default browser with the url:
//		try {
//			if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
//				Desktop.getDesktop().browse(new URI("http://www.webgraphviz.com/"));
//			}
//		} catch (IOException | URISyntaxException ex) {
//			java.util.logging.Logger.getAnonymousLogger().log(java.util.logging.Level.WARNING, "Could not open browser", ex);
//		}
	}
}
