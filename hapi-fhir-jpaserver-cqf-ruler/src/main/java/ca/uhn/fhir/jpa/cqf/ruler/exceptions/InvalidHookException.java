package ca.uhn.fhir.jpa.cqf.ruler.exceptions;

public class InvalidHookException extends RuntimeException {

    public InvalidHookException(String message) {
        super(message);
    }

}
