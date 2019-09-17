package ca.uhn.fhir.jpa.util;

/*-
 * #%L
 * Smile CDR - CDR
 * %%
 * Copyright (C) 2016 - 2018 Simpatico Intelligent Systems Inc
 * %%
 * All rights reserved.
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
