package ca.uhn.fhir.jpa.util;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class JsonUtil {

	private static final ObjectMapper ourMapperPrettyPrint;
	private static final ObjectMapper ourMapperNonPrettyPrint;

	static {
		ourMapperPrettyPrint = new ObjectMapper();
		ourMapperPrettyPrint.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		ourMapperPrettyPrint.enable(SerializationFeature.INDENT_OUTPUT);

		ourMapperNonPrettyPrint = new ObjectMapper();
		ourMapperNonPrettyPrint.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		ourMapperNonPrettyPrint.disable(SerializationFeature.INDENT_OUTPUT);
	}

	/**
	 * Parse JSON
	 */
	public static <T> T deserialize(@Nonnull String theInput, @Nonnull Class<T> theType) throws IOException {
		return ourMapperPrettyPrint.readerFor(theType).readValue(theInput);
	}

	/**
	 * Encode JSON
	 */
	public static String serialize(@Nonnull Object theInput) throws IOException {
		return serialize(theInput, true);
	}

	/**
	 * Encode JSON
	 */
	public static String serialize(@Nonnull Object theInput, boolean thePrettyPrint) throws IOException {
		StringWriter sw = new StringWriter();
		if (thePrettyPrint) {
			ourMapperPrettyPrint.writeValue(sw, theInput);
		} else {
			ourMapperNonPrettyPrint.writeValue(sw, theInput);
		}
		return sw.toString();
	}

	/**
	 * Encode JSON
	 */
	public static void serialize(@Nonnull Object theInput, @Nonnull Writer theWriter) throws IOException {
		// Note: We append a string here rather than just having ourMapper write directly
		// to the Writer because ourMapper seems to close the writer for some stupid
		// reason.. There's probably a way of preventing that bit I'm not sure what that
		// is and it's not a big deal here.
		theWriter.append(serialize(theInput));
	}

}
