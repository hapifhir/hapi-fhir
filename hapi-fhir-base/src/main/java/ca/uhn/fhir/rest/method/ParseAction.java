package ca.uhn.fhir.rest.method;

import java.io.IOException;
import java.io.Writer;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.parser.IParser;

/**
 * @author Peter Van Houte
 *
 * @param <T> A functional class that parses an outcome
 */
public abstract class ParseAction<T> {

    protected T theOutcome;
    
    protected ParseAction(T outcome) {
        this.theOutcome = outcome;
    }

    public abstract void execute(IParser parser, Writer writer) throws IOException;

    public static ParseAction<TagList> create(TagList outcome) {
        return outcome == null ? null : new ParseAction<TagList>(outcome) {
            @Override
            public void execute(IParser theParser, Writer theWriter) throws IOException {
                theParser.encodeTagListToWriter(this.theOutcome, theWriter);
            }
        };
    }
    
    public static ParseAction<IBaseResource> create(IBaseResource outcome) {
        return outcome == null ? null : new ParseAction<IBaseResource>(outcome) {
            @Override
            public void execute(IParser theParser, Writer theWriter) throws IOException {
                theParser.encodeResourceToWriter(this.theOutcome, theWriter);
            }
        };
    }

}
