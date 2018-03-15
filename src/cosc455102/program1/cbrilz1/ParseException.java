package cosc455102.program1.cbrilz1;

/**
 * Parsing exception. For simplicity, this is used for both lexical errors as
 * well as parsing errors. A more complete design would include a hierarchy of
 * specific error types.
 *
 * @author Adam J. Conover, D.Sc. <aconover@towson.edu>
 */
@SuppressWarnings("serial")
public class ParseException extends Exception {

    /**
     * Construct a new Error Message.
     * 
     * @param errMsg The text of the error.
     */
    public ParseException(String errMsg) {
        super(errMsg);
    }
}
