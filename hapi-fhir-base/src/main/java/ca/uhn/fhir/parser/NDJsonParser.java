package ca.uhn.fhir.parser;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.BundleUtil;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;


/**
 * This class is the FHIR NDJSON parser/encoder. Users should not interact with this class directly, but should use
 * {@link FhirContext#newNDJsonParser()} to get an instance.
 */
public class NDJsonParser extends BaseParser {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(NDJsonParser.class);

        private IParser myJsonParser;
        private FhirContext myFhirContext;

	/**
	 * Do not use this constructor, the recommended way to obtain a new instance of the NDJSON parser is to invoke
	 * {@link FhirContext#newNDJsonParser()}.
	 *
	 * @param theParserErrorHandler
	 */
	public NDJsonParser(FhirContext theContext, IParserErrorHandler theParserErrorHandler) {
		super(theContext, theParserErrorHandler);
                myFhirContext = theContext;

                myJsonParser = theContext.newJsonParser();
	}

        @Override
        public IParser setPrettyPrint(boolean thePrettyPrint) {
                myJsonParser.setPrettyPrint(thePrettyPrint);
                return this;
        }

        @Override
        public EncodingEnum getEncoding() {
                return EncodingEnum.NDJSON;
        }

	@Override
	protected void doEncodeResourceToWriter(IBaseResource theResource, Writer theWriter, EncodeContext theEncodeContext) throws IOException {
                // We only encode bundles to NDJSON.
                if (!(IBaseBundle.class.isAssignableFrom(theResource.getClass()))) {
                        throw new IllegalArgumentException(Msg.code(1833) + "NDJsonParser can only encode Bundle types.  Received " + theResource.getClass().getName());
                }

                // Ok, convert the bundle to a list of resources.
                List<IBaseResource> theBundleResources = BundleUtil.toListOfResources(myFhirContext, (IBaseBundle) theResource);

                // Now we write each one in turn.
                // Use newline only as a line separator, not at the end of the file.
                boolean isFirstResource = true;
                for (IBaseResource theBundleEntryResource : theBundleResources) {
                        if (!(isFirstResource)) {
                                theWriter.write("\n");
                        }
                        isFirstResource = false;

                        myJsonParser.encodeResourceToWriter(theBundleEntryResource, theWriter);
                }
	}

	@Override
	public <T extends IBaseResource> T doParseResource(Class<T> theResourceType, Reader theReader) throws DataFormatException {
                // We can only parse to bundles.
                if ((theResourceType != null) && (!(IBaseBundle.class.isAssignableFrom(theResourceType)))) {
                        throw new DataFormatException(Msg.code(1834) + "NDJsonParser can only parse to Bundle types.  Received " + theResourceType.getName());
                }

                try {
                        // Now we go through line-by-line parsing the JSON and then stuffing it into a bundle.
                        BundleBuilder myBuilder = new BundleBuilder(myFhirContext);
                        myBuilder.setType("collection");
                        BufferedReader myBufferedReader = new BufferedReader(theReader);
                        String jsonString = myBufferedReader.readLine();
                        while (jsonString != null) {
                                // And add it to a collection in a Bundle.
                                // The string must be trimmed, as per the NDJson spec 3.2
                                myBuilder.addCollectionEntry(myJsonParser.parseResource(jsonString.trim()));
                                // Try to read another line.
                                jsonString = myBufferedReader.readLine();
                        }

                        return (T) myBuilder.getBundle();
                } catch (IOException err) {
                        throw new DataFormatException(Msg.code(1835) + err.getMessage());
                }
	}
}
