package cosc455102.program1.cbrilz1;

/**
 * COSC 455 Programming Languages: Implementation and Design.
 *
 * A Simple Lexical Analyzer Adapted from Sebesta (2010) by Josh Dehlinger
 * further modified by Adam Conover (2012-2017)
 *
 * This lexical analyzer simply finds lexemes separated by a single space and
 * places it in the Compiler classes currentToken global String. The lexical
 * analyzer here takes a source line and does a character- by-character analysis
 * to determine lexemes/tokens. Note that this lexical analyzer does not lookup
 * a lexeme to find its "class" to determine its token type, as shown in the
 * book. This lexical analyzer also limits each lexeme/token to 100 characters
 * or less.
 */
public class LexicalAnalyzer {

	private String sourceLine;
	private char nextChar;
	private int curPosition;
	protected TOKEN curToken;
	protected StringBuilder lexemeBuffer;

	private int tokenCount = 0;

	/**
	 * The main driver of this class. This method takes a "program", in this
	 * case a single line of text in the form of a sentence, and gets the first
	 * lexeme/token.
	 *
	 * @param programSource The source code for the program. A "real" lexer
	 * would generally read from the file as it was being tokenized, but
	 * accepting a simple string which contains the complete text of the program
	 * allows this class to just focus on one job: converting identifying the
	 * lexemes and converting them into tokens.
	 *
	 * @throws ParseException Thrown if a parsing error occurs.
	 */
	public void start(String programSource) throws ParseException {
		sourceLine = programSource;
		curPosition = 0;

		getNextChar();
		parseNextToken();
	}

	/**
	 * This method does a character-by-character analysis to get the next token
	 * and set it in the Compiler class's currentToken global String variable.
	 * This simple lexical analyzer does not differentiate between letters,
	 * digits and other special characters - it simply looks for characters,
	 * spaces and end of line characters to determine relevant tokens.
	 *
	 * @throws ParseException
	 */
	public void parseNextToken() throws ParseException {
		resetLexemeBuffer();

		// Ignore spaces and add the first character to the token
		getNextNonBlank();
		addCharToLexemeBuffer();
		getNextChar();

		// Continue gathering characters for the token
		while ((this.nextChar != '\n') && (this.nextChar != ' ')) {
			addCharToLexemeBuffer();
			getNextChar();
		}

		// Convert the gathered character array token into a String
		String lexeme = lexemeBuffer.toString().trim();

		// Set the new token
		this.curToken = TOKEN.fromLexeme(lexeme);
		this.tokenCount++;
	}

	/**
	 * The number of tokens read so far.
	 * 
	 * @return The current number of tokens read so far.
	 */
	public int getTokenCount() {
		return tokenCount;
	}

	/**
	 * This method gets the next character from the "program" string.
	 */
	private void getNextChar() {
		if (this.curPosition < this.sourceLine.length()) {
			this.nextChar = this.sourceLine.charAt(curPosition);
			this.curPosition++;
		} else {
			this.nextChar = '\n';
		}
	}

	/**
	 * A (trivial) helper method to determine if the current character is a
	 * space.
	 */
	private boolean isSpace(char c) {
		return (c == ' ');
	}

	/**
	 * A helper method to get the next non-blank character.
	 */
	private void getNextNonBlank() {
		while (isSpace(this.nextChar)) {
			getNextChar();
		}
	}

	/**
	 * This method adds the current character the the token after checking to
	 * make sure that the length of the token isn't too long, a lexical error in
	 * this case.
	 */
	private void addCharToLexemeBuffer() throws ParseException {
		if (this.lexemeBuffer.length() <= 98) {
			this.lexemeBuffer.append(this.nextChar);
		} else {
			throw new ParseException("LEXICAL ERROR: The found lexeme is too long! ");
		}
	}

	/**
	 * Simple method to reset the lexeme buffer.
	 */
	private void resetLexemeBuffer() {
		lexemeBuffer = new StringBuilder();
	}
}
