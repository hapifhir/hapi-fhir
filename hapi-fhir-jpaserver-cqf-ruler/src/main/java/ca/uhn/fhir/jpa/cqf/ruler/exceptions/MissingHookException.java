package ca.uhn.fhir.jpa.cqf.ruler.exceptions;

public class MissingHookException extends RuntimeException {

    public MissingHookException(String message) {
        super(message);
    }

}
