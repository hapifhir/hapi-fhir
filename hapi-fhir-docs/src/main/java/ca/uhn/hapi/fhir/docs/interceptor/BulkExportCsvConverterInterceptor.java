/*-
 * #%L
 * HAPI FHIR - Docs
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
package ca.uhn.hapi.fhir.docs.interceptor;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportResourceList;
import ca.uhn.fhir.rest.api.server.bulk.ConvertedFile;
import ca.uhn.fhir.rest.api.server.bulk.ConvertedFiles;
import ca.uhn.fhir.rest.api.server.bulk.IResourceConverter;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;

import java.nio.charset.StandardCharsets;

// Created by Claude Opus 5
// START SNIPPET: interceptor
@Interceptor
public class BulkExportCsvConverterInterceptor {

	public static final String CSV_CONTENT_TYPE = "text/csv";

	/**
	 * Chooses the converter to use for a given Bulk Export job. Returning <code>null</code>
	 * leaves the default behaviour in place, meaning that NDJSON is written by the built-in
	 * converter and any other format is rejected when the job is submitted.
	 *
	 * @param theParameters the parameters the export job was submitted with
	 * @return a converter for the requested output format, or <code>null</code> to use the default
	 */
	@Hook(Pointcut.STORAGE_BULK_EXPORT_RESOURCE_CONVERT)
	public IResourceConverter selectConverter(BulkExportJobParameters theParameters) {
		if (CSV_CONTENT_TYPE.equalsIgnoreCase(theParameters.getOutputFormat())) {
			return new CsvConverter();
		}
		return null;
	}

	private static class CsvConverter implements IResourceConverter {

		@Nonnull
		@Override
		public ConvertedFiles consume(
				@Nonnull BulkExportResourceList theResources, @Nonnull BulkExportJobParameters theJobParameters) {

			// The converter is never invoked with an empty list, and every resource in a
			// single invocation has the same resource type
			String resourceType = theResources.getResources().get(0).fhirType();

			StringBuilder contents = new StringBuilder("id,family,given\n");
			for (IBaseResource nextResource : theResources.getResources()) {
				contents.append(toCsvRow(nextResource));
			}

			ConvertedFile file = new ConvertedFile();
			file.setResourceType(resourceType);
			file.setMimeType(CSV_CONTENT_TYPE);
			file.setBytes(contents.toString().getBytes(StandardCharsets.UTF_8));

			// A converter may return more than one file for a single batch of resources
			return new ConvertedFiles().addFile(file);
		}

		private String toCsvRow(IBaseResource theResource) {
			String id = theResource.getIdElement().getIdPart();
			if (theResource instanceof Patient patient) {
				HumanName name = patient.getNameFirstRep();
				return "%s,%s,%s\n".formatted(id, name.getFamily(), name.getGivenAsSingleString());
			}
			return "%s,,\n".formatted(id);
		}
	}
}
// END SNIPPET: interceptor
