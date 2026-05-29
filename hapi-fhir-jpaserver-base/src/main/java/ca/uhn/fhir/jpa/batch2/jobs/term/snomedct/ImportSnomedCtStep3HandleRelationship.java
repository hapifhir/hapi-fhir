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
package ca.uhn.fhir.jpa.batch2.jobs.term.snomedct;

import ca.uhn.fhir.batch2.api.AttachmentDetails;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyMetadataAttachmentJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincJobAppCtx;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemVersionDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptPropertyDao;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConceptProperty;
import ca.uhn.fhir.jpa.util.QueryChunker;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.r4.model.CodeSystem;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseExpandDistributionIntoFilesStep.newCsvParser;
import static ca.uhn.fhir.jpa.term.api.ITermLoaderSvc.SCT_URI;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @see ImportLoincJobAppCtx#importLoincStep2Concepts()
 */
public class ImportSnomedCtStep3HandleRelationship
		extends BaseImportSnomedCtStep<ImportSnomedCtStep3HandleRelationship.ImportSnomedCtContext> {

	@Autowired
	private IHapiTransactionService myTxService;

	@Autowired
	private ITermCodeSystemVersionDao myTermCodeSystemVersionDao;

	@Autowired
	private ITermConceptPropertyDao myTermConceptPropertyDao;

	@Nonnull
	@Override
	protected List<LoincFileNameSpecification> getFilesToProcess(
			StepExecutionDetails<ImportSnomedCtJobParameters, ?> theStepExecutionDetails) {
		return List.of(new LoincFileNameSpecification(
			FileHandlingType.TSV_SPLIT_WITH_REPEAT_HEADER_5000_LINE_CHUNKS,
			t->t.contains("sct2_Relationship_Full")));
	}

	@Override
	protected ImportSnomedCtContext newContextObject(
			StepExecutionDetails<ImportSnomedCtJobParameters, TerminologyFileSetJson> theStepExecutionDetails) {
		return new ImportSnomedCtContext();
	}

	@Nonnull
	@Override
	protected RunOutcome run(@Nonnull StepExecutionDetails<ImportSnomedCtJobParameters, TerminologyFileSetJson> theStepExecutionDetails, @Nonnull IJobDataSink<TerminologyFileSetJson> theDataSink, ImportTerminologyMetadataAttachmentJson theJobMetadata, ImportSnomedCtContext theContext) {
		String jobInstanceId = theStepExecutionDetails.getInstance().getInstanceId();

		TerminologyFileSetJson theData = theStepExecutionDetails.getData();
		String attachmentId = theData.getAttachmentId();

		Set<String> allIds = new HashSet<>();

		AttachmentDetails attachment = myJobPersistence.fetchAttachmentById(jobInstanceId, attachmentId);
		try (InputStream inputStream = attachment.getInputStream()) {
			InputStreamReader reader = new InputStreamReader(
				BOMInputStream.builder().setInputStream(inputStream).get(), StandardCharsets.UTF_8);
			CSVParser csvReader = newCsvParser(',', reader);
			for (CSVRecord record : csvReader.getRecords()) {
				boolean active = "1".equals(record.get("active"));
				if (active) {
					String sourceId = record.get("sourceId");
					allIds.add(sourceId);
					String destinationId = record.get("destinationId");
					allIds.add(destinationId);
				}

			}
		} catch (IOException e) {
			throw new JobExecutionFailedException(
				// FIXME: add code
				Msg.code(1) + "Failed to read file attachment: " + e.getMessage(), e);
		}

		Map<String, String> idToCode = new HashMap<>();

		myTxService
			.withSystemRequestOnDefaultPartition()
				.execute(()->{
					TermCodeSystemVersion csv = myTermCodeSystemVersionDao.findByCodeSystemUriAndVersion(SCT_URI, theJobMetadata.getCodeSystemStagingVersionId());
					QueryChunker.chunk(allIds, ids -> {
						List<TermConceptProperty> properties = myTermConceptPropertyDao.findByCodeSystemVersionAndCodeAndFetchConcept(csv, "id", ids);
						for (TermConceptProperty property : properties) {
							idToCode.put(property.getValue(), property.getConcept().getCode());
						}
					});
				});

		theContext.setSnomedIdToCode(idToCode);

		return super.run(theStepExecutionDetails, theDataSink, theJobMetadata, theContext);
	}

	@Override
	protected void handleRecord(
			StepExecutionDetails<ImportSnomedCtJobParameters, TerminologyFileSetJson> theStepExecutionDetails,
			ImportTerminologyMetadataAttachmentJson theJobMetadata,
			ImportSnomedCtJobParameters theJobParameters,
			ImportSnomedCtContext theContext,
			CSVRecord theRecord,
			CodeSystem theCodeSystemToPopulate,
			TerminologyFileSetJson theData,
			String theSourceFilename) {

		String sourceId = theRecord.get("sourceId");
		String destinationId = theRecord.get("destinationId");
		String typeId = theRecord.get("typeId");
		boolean active = "1".equals(theRecord.get("active"));

		// The concept with ID "116680003" denotes the concept "Is a (attribute)".
		// https://docs.snomed.org/snomed-international-documents/snomed-ct-glossary/r/relationship-type
		if (isNotBlank(sourceId)
			&& isNotBlank(destinationId)
			&& "116680003".equals(typeId)
			&& !sourceId.equals(destinationId)) {

			// Source = child, destination = parent
			String sourceCode = theContext.getSnomedIdToCode().get(sourceId);
			String destinationCode = theContext.getSnomedIdToCode().get(destinationId);

			// FIXME: remove
			if (sourceCode == null || destinationCode == null) {
				return;
			}

			Validate.notNull(sourceCode, "Source code not found for id: %s" , sourceId);
			Validate.notNull(destinationCode, "Target code not found for id: %s" , destinationId);

			super.getOrAddParentChildHierarchy(theContext, destinationCode, sourceCode);
		}

	}


	protected static class ImportSnomedCtContext extends MyBaseContext {


		private Map<String, String> mySnomedIdToCode;

		public void setSnomedIdToCode(Map<String, String> theSnomedIdToCode) {
			mySnomedIdToCode = theSnomedIdToCode;
		}

		public Map<String, String> getSnomedIdToCode() {
			return mySnomedIdToCode;
		}
	}

}
