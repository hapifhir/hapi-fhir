package ca.uhn.fhir.jpa.bulk.imp.job;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.batch.log.Logs;
import ca.uhn.fhir.jpa.bulk.export.job.BulkExportJobConfig;
import ca.uhn.fhir.jpa.bulk.imp.model.ParsedBulkImportRecord;
import ca.uhn.fhir.jpa.bulk.imp.model.RawBulkImportRecord;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Nonnull;

public class BulkImportFileProcessor implements ItemProcessor<RawBulkImportRecord, ParsedBulkImportRecord> {

	@Value("#{stepExecutionContext['" + BulkExportJobConfig.JOB_UUID_PARAMETER + "']}")
	private String myJobUuid;
	@Value("#{stepExecutionContext['" + BulkImportPartitioner.FILE_INDEX + "']}")
	private int myFileIndex;

	@Autowired
	private FhirContext myFhirContext;

	@Override
	public ParsedBulkImportRecord process(@Nonnull RawBulkImportRecord theLine) throws Exception {
		Logs.getBatchTroubleshootingLog().debug("Parsing file index {} for job: {}", myFileIndex, myJobUuid);

		String inputContent = theLine.getRowContent();
		IBaseResource parsed = myFhirContext.newJsonParser().parseResource(inputContent);
		String tenantName = theLine.getTenantName();
		return new ParsedBulkImportRecord(tenantName, parsed);
	}

}
