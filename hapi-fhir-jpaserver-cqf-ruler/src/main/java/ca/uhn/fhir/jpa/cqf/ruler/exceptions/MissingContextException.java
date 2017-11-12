package ca.uhn.fhir.jpa.cqf.ruler.exceptions;

public class MissingContextException extends RuntimeException {

    public MissingContextException(String message) {
        super(message);
    }

}
