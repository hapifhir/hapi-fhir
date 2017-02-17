package ca.uhn.fhir.rest.method;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.IOException;
import java.io.Writer;

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
    
}
