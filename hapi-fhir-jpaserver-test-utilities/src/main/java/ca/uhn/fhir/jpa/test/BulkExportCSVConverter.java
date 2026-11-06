/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
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
package ca.uhn.fhir.jpa.test;

import ca.uhn.fhir.jpa.util.CsvUtil;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportResourceList;
import ca.uhn.fhir.rest.api.server.bulk.ConvertedFile;
import ca.uhn.fhir.rest.api.server.bulk.ConvertedFiles;
import ca.uhn.fhir.rest.api.server.bulk.IResourceConverter;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVPrinter;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.io.IOException;
import java.util.Map;

public class BulkExportCSVConverter implements IResourceConverter {

	/**
	 * Interface to allow the creator to define how a resource
	 * should be written to the csvprinter
	 */
	public interface IResourceToCSV {
		/**
		 * Write the provided resource into the provided CSVPrinter
		 * to match with the headers provided.
		 *
		 * Each resource is different so it's on the caller to define how
		 * resources are printed.
		 */
		void process(CSVPrinter thePrinter, IBaseResource theResource) throws IOException;
	}

	/**
	 * A map of resourcetype -> list of headers (in csv)
	 */
	private final Map<String, String[]> myHeadersMap;

	/**
	 * The processor.
	 */
	private final IResourceToCSV myCsvPrinter;

	public BulkExportCSVConverter(Map<String, String[]> theResourceTypeToHeaders,
								  IResourceToCSV thePrinter) {
		myHeadersMap = theResourceTypeToHeaders;
		myCsvPrinter = thePrinter;
	}

	@Override
	@Nonnull
	public ConvertedFiles consume(@Nonnull BulkExportResourceList theResources, @Nonnull BulkExportJobParameters theJobParameters) {
		ConvertedFiles files = new ConvertedFiles();

		ConvertedFile file = new ConvertedFile();
		if (!theResources.getResources().isEmpty()) {
			// each invocation will have the same resources
			file.setResourceType(theResources.getResources().get(0).fhirType());
		}
		file.setMimeType("text/csv");

		/*
		 * each resource will have to be parsed on its own into
		 * its own csv
		 */
		String resourceType = file.getResourceType();
		byte[] bytes = CsvUtil.writeCsvToByteArray(
			myHeadersMap.get(resourceType),
			printer -> {
				for (IBaseResource resource : theResources.getResources()) {
					myCsvPrinter.process(printer, resource);
				}
			}
		);
		file.setBytes(bytes);

		// we can control how many of these we want per input resources.
		// but there's only a small number of resources so
		// we'll only return the 1 file per resource input
		files.addFile(file);

		return files;
	}
}
