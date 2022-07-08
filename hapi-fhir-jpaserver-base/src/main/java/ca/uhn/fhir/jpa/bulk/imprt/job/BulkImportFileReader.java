package ca.uhn.fhir.jpa.bulk.imprt.job;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.batch.config.BatchConstants;
import ca.uhn.fhir.jpa.batch.log.Logs;
import ca.uhn.fhir.jpa.bulk.imprt.api.IBulkDataImportSvc;
import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobFileJson;
import ca.uhn.fhir.jpa.bulk.imprt.model.ParsedBulkImportRecord;
import ca.uhn.fhir.util.IoUtil;
import com.google.common.io.LineReader;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.StringReader;

@SuppressWarnings("UnstableApiUsage")
public class BulkImportFileReader implements ItemReader<ParsedBulkImportRecord> {

	@Autowired
	private IBulkDataImportSvc myBulkDataImportSvc;
	@Autowired
	private FhirContext myFhirContext;
	@Value("#{stepExecutionContext['" + BatchConstants.JOB_UUID_PARAMETER + "']}")
	private String myJobUuid;
	@Value("#{stepExecutionContext['" + BulkImportPartitioner.FILE_INDEX + "']}")
	private int myFileIndex;

	private StringReader myReader;
	private LineReader myLineReader;
	private int myLineIndex;
	private String myTenantName;

	@Override
	public ParsedBulkImportRecord read() throws Exception {

		if (myReader == null) {
			BulkImportJobFileJson file = myBulkDataImportSvc.fetchFile(myJobUuid, myFileIndex);
			myTenantName = file.getTenantName();
			myReader = new StringReader(file.getContents());
			myLineReader = new LineReader(myReader);
		}

		String nextLine = myLineReader.readLine();
		if (nextLine == null) {
			IoUtil.closeQuietly(myReader);
			return null;
		}

		Logs.getBatchTroubleshootingLog().debug("Reading line {} file index {} for job: {}", myLineIndex++, myFileIndex, myJobUuid);

		IBaseResource parsed = myFhirContext.newJsonParser().parseResource(nextLine);
		return new ParsedBulkImportRecord(myTenantName, parsed, myLineIndex);
	}
}
