package cosc455102.program1.cbrilz1;

/**
 *COURSE: COSC455.102
 * Name: Brilz, Chance
 */

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public enum TOKEN {

	ARTICLE("a", "the"), // a list of articles
	NOUN("dog", "cat", "rat", "house", "tree"), // a list of nouns
	VERB("loves", "hates", "eats", "chases", "stalks"), // a list of verbs
	ADVERB("quickly", "silently", "slowly"), // list of adverbs
	ADJECTIVE("furry", "fast", "lazy", "sneaky", "tall"), // list of adjectives
	ADJECTIVE_DIVIDER(","),
	CONJUNCTION("and", "or"), // list of conjunctions
	PREPOSITION("around", "up", "over"), // list of prepositions
	EOS(TOKEN.EOS_MARKER, ".", "!"), // End of statement marker. (A character not used for anything else.)
	UNKNOWN(); // An unknown token

	// The lexemes under this token
	private final Set<String> lexemeList;

	// The marker symbol for statement termination (if used)
	public static final String EOS_MARKER = "$$";

	// Construct the token with the list of lexems
	private TOKEN(String... tokenStrings) {
		this.lexemeList = Collections.unmodifiableSortedSet(
			new TreeSet<>(Arrays.asList(tokenStrings)));		
	}

	/**
	 * Gets a token from a lexeme by the following rules (in order).
	 *
	 * 1. If the lexeme is blank return EOF (end of file). 2. If the lexeme
	 * corresponds to a known token, return the token. 3. if the lexeme is a
	 * sequence of digits, it is a LITERAL. 4. if the lexeme is anything else,
	 * it must be an identifier.
	 *
	 * @param string The Lexeme
	 * @return The Token
	 */
	public static TOKEN fromLexeme(final String string) {
		// Just to be safe...
		String lexeme = string.trim();

		// An empty string should mean no more tokens to process.
		if (lexeme.isEmpty())
			return EOS;

		// Search through ALL lexemes looking for a match.
		for (TOKEN t : TOKEN.values()) {
			if (t.lexemeList.contains(lexeme)) {
				// early bailout.
				return t;
			}
		}

		// If nothing matches assume an ID.  (What other choice do we have?)
		return UNKNOWN;
	}

}
