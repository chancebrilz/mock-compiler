package cosc455102.program1.cbrilz1;

/**
 *COURSE: COSC455.102
 * Name: Brilz, Chance
*/

import static java.lang.System.out;

import java.io.*;

public class Compiler {

    /**
     * It is assumed that the first argument provided is the name of the source file that is to be
     * "compiled".
     *
     * @param args
     *
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        //args = new String[]{"<some hard coded path for testing... if necesary>"};

        if (args.length < 1) {
            out.println("Compilers need to be told what to compile!!!");
        } else {
            // Java 7 "try-with-resource" to create the file input buffer.
            try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
                /* This version is for a file that contains MULTIPLE PROGRAMS
                 where one program exists on each line of the input file. */
                processOneProgramPer_LINE(br);

                /* This version is for a file that contains a SINGLE PROGRAM
                 which spans all of the lines of the input file. */
//                processOneProgramPer_FILE(br);
            }
        }
    }

    /**
     * Reads each line of the input file and invokes the lexer and parser for each.
     */
    static void processOneProgramPer_LINE(BufferedReader br) throws IOException {
        // Each Line of input.
        String sourceLine;

        // Create the new lexer.
        LexicalAnalyzer lexer = new LexicalAnalyzer();

        // Read each line in the source file to be compiled as a unique sentence
        // to check against the grammar.
        while ((sourceLine = br.readLine()) != null) {
            sourceLine = sourceLine.trim();
            // Ignore empty lines
            if (sourceLine.length() <= 0) {
                continue;
            }

            // Ignore Comment lines
            if (sourceLine.startsWith("#")) {
                System.out.println("Comment: " + sourceLine.substring(1));
                continue;
            }

            // Parse the given sentence against the given grammar. We assume that the
            // sentence, <S>, production is the start state.
            try {
                // Start the lexer (ONLY ONE OF THE BELOW IS NEEDED)...

                /* Example of padding the end of each physical line with and
                 End of Statement marker.  If course, this assumes each line
                 if INPUT is its own statment. */
                lexer.start(sourceLine + " " + TOKEN.EOS_MARKER);

                /* Example of just passing the lines as-is, assuming that the
                 grammar does not care about statement delimiting. */
                //lexer.start(sourceLine);

                /* Create a new syntax analyzer over the provided lexer. */
                SyntaxAnalyzer parser = new SyntaxAnalyzer(lexer);

                /* Start the parser. */
                parser.analyze();

                out.println("The sentence '" + sourceLine + "' follows the BNF grammar.");
            } catch (ParseException error) {
                // If a syntax error was found, print that the sentence does not follow the grammar.
                out.println("SYNTAX ERROR: " + error.getMessage());
                out.println("FAIL: The sentence '" + sourceLine + "' does not follow the BNF grammar.");
            }

            out.println("-----------------------------------------------------------");
        }
    }

}
