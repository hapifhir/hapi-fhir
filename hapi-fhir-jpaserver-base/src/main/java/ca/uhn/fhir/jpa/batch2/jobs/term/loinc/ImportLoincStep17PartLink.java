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
package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyJobParameters;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyMetadataAttachmentJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_LINK_FILE_PRIMARY;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_LINK_FILE_PRIMARY_DEFAULT;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_LINK_FILE_SUPPLEMENTARY;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_LINK_FILE_SUPPLEMENTARY_DEFAULT;
import static org.apache.commons.lang3.StringUtils.trim;

/**
 * @see ImportLoincJobAppCtx#importLoincStep17PartLink()
 */
public class ImportLoincStep17PartLink extends BaseImportLoincStep<ImportLoincStep17PartLink.MyBaseContext> {
	private static final Logger ourLog = LoggerFactory.getLogger(ImportLoincStep17PartLink.class);

	@Override
	protected MyBaseContext newContextObject(
			StepExecutionDetails<ImportTerminologyJobParameters, TerminologyFileSetJson> theStepExecutionDetails) {
		return new MyBaseContext();
	}

	@Nonnull
	@Override
	public List<LoincFileNameSpecification> getFilesToProcess(
			StepExecutionDetails<ImportTerminologyJobParameters, ?> theStepExecutionDetails) {
		return List.of(
				new LoincFileNameSpecification(
						FileHandlingType.CSV_SPLIT_WITH_REPEAT_HEADER_1000_LINE_CHUNKS,
						LoincUploadPropertiesEnum.LOINC_PART_LINK_FILE,
						LoincUploadPropertiesEnum.LOINC_PART_LINK_FILE_DEFAULT),
				new LoincFileNameSpecification(
						FileHandlingType.CSV_SPLIT_WITH_REPEAT_HEADER_1000_LINE_CHUNKS,
						LOINC_PART_LINK_FILE_PRIMARY,
						LOINC_PART_LINK_FILE_PRIMARY_DEFAULT),
				new LoincFileNameSpecification(
						FileHandlingType.CSV_SPLIT_WITH_REPEAT_HEADER_1000_LINE_CHUNKS,
						LOINC_PART_LINK_FILE_SUPPLEMENTARY,
						LOINC_PART_LINK_FILE_SUPPLEMENTARY_DEFAULT));
	}

	@Override
	protected void handleRecord(
			StepExecutionDetails<ImportTerminologyJobParameters, TerminologyFileSetJson> theStepExecutionDetails,
			ImportTerminologyMetadataAttachmentJson theJobMetadata,
			ImportTerminologyJobParameters theJobParameters,
			MyBaseContext theContext,
			CSVRecord theRecord,
			CodeSystem theCodeSystemToPopulate,
			TerminologyFileSetJson theData,
			String theSourceFilename) {

		String loincNumber = trim(theRecord.get("LoincNumber"));
		String property = trim(theRecord.get("Property"));
		String partName = trim(theRecord.get("PartName"));
		String partNumber = trim(theRecord.get("PartNumber"));

		/*
		 * Property has the form http://loinc.org/property/COMPONENT
		 * but we want just the COMPONENT part
		 */
		int lastSlashIdx = property.lastIndexOf("/");
		String propertyPart = property.substring(lastSlashIdx + 1);

		CodeSystem.PropertyType propertyType =
				theContext.getPropertyNameToType(theJobMetadata).get(propertyPart);
		if (propertyType == null) {
			return;
		}

		CodeSystem.ConceptDefinitionComponent concept = getOrAddConcept(theContext, loincNumber);

		// Filter duplicates
		Optional<CodeSystem.ConceptPropertyComponent> existingProperty = concept.getProperty().stream()
				.filter(t -> t.getCode().equals(propertyPart))
				.findFirst();
		if (existingProperty.isPresent()) {
			return;
		}

		ourLog.debug("Adding new property {} = {}", propertyPart, partNumber);
		CodeSystem.ConceptPropertyComponent newProperty = concept.addProperty();
		newProperty.setCode(propertyPart);
		if (propertyType == CodeSystem.PropertyType.STRING) {
			newProperty.setValue(new StringType(partName));
		} else if (propertyType == CodeSystem.PropertyType.CODING) {
			newProperty.setValue(new Coding(ITermLoaderSvc.LOINC_URI, partNumber, partName));
		} else {
			throw new InternalErrorException(
					Msg.code(914) + "Don't know how to handle property of type: " + propertyType);
		}
	}
}
