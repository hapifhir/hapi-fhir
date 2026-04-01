/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

/**
 * Utilities for working with CSV files.
 */
public class CsvUtil {

	/**
	 * Non-instantiable
	 */
	private CsvUtil() {
		// nothing
	}

	@FunctionalInterface
	public interface ICsvProducer {

		void accept(CSVPrinter thePrinter) throws IOException;
	}

	/**
	 * Produce CSV text from a callback.
	 *
	 * @param theHeaders     The header names, which will be printed on the first line of the output CSV
	 * @param theCsvProducer A callback to actually write the CSV data
	 * @return A byte array containing the CSV data encoded in UTF-8
	 * @since 8.10.0
	 */
	public static byte[] writeCsvToByteArray(String[] theHeaders, ICsvProducer theCsvProducer) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Appendable appendable = new OutputStreamWriter(out, StandardCharsets.UTF_8);

		CSVFormat format = CSVFormat.DEFAULT
				.builder()
				.setHeader(theHeaders)
				.setRecordSeparator('\n')
				.setQuoteMode(QuoteMode.MINIMAL)
				.build();

		try {
			CSVPrinter csvPrinter = new CSVPrinter(appendable, format);
			theCsvProducer.accept(csvPrinter);
			csvPrinter.close(true);
		} catch (IOException e) {
			throw new InternalErrorException(Msg.code(2889) + "Failed to write CSV: " + e.getMessage(), e);
		}

		return out.toByteArray();
	}
}
