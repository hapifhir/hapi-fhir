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
import org.hl7.fhir.r4.model.Enumerations;

import java.util.List;
import java.util.Properties;

import static ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyUtil.getJobProperties;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum.LOINC_CONCEPTMAP_VERSION;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

/**
 * @see ImportLoincJobAppCtx#importLoincStep8PartRelatedCodeMapping()
 */
public class ImportLoincStep8HandlePartRelatedCodeMapping
		extends BaseImportLoincStep<BaseImportLoincStep.MyBaseContext> {

	@Override
	protected MyBaseContext newContextObject(
			StepExecutionDetails<ImportTerminologyJobParameters, TerminologyFileSetJson> theStepExecutionDetails) {
		return new MyBaseContext();
	}

	@Nonnull
	@Override
	public List<LoincFileNameSpecification> getFilesToProcess(
			StepExecutionDetails<ImportTerminologyJobParameters, ?> theStepExecutionDetails) {
		return List.of(new LoincFileNameSpecification(
				FileHandlingType.CSV_SPLIT_WITH_REPEAT_HEADER_50000_LINE_CHUNKS,
				LoincUploadPropertiesEnum.LOINC_PART_RELATED_CODE_MAPPING_FILE,
				LoincUploadPropertiesEnum.LOINC_PART_RELATED_CODE_MAPPING_FILE_DEFAULT));
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
		String partNumber = trim(theRecord.get("PartNumber"));
		String partName = trim(theRecord.get("PartName"));
		String extCodeId = trim(theRecord.get("ExtCodeId"));
		// Trim NBSP
		extCodeId = extCodeId.replace("\u00A0", "");
		String extCodeDisplayName = trim(theRecord.get("ExtCodeDisplayName"));
		String extCodeSystem = trim(theRecord.get("ExtCodeSystem"));
		String mapType = trim(theRecord.get("Equivalence"));
		String extCodeSystemVersion = trim(theRecord.get("ExtCodeSystemVersion"));
		String extCodeSystemCopyrightNotice = trim(theRecord.get("ExtCodeSystemCopyrightNotice"));

		// CodeSystem version from properties file
		String codeSystemVersionId = theJobMetadata.getCodeSystem().getVersion();

		// ConceptMap version from properties files
		String loincPartMapVersion;
		Properties jobProperties = getJobProperties(myJobPersistence, theStepExecutionDetails);
		if (isNotBlank(jobProperties.getProperty(LOINC_CONCEPTMAP_VERSION.getCode()))) {
			loincPartMapVersion =
					jobProperties.getProperty(LOINC_CONCEPTMAP_VERSION.getCode()) + "-" + codeSystemVersionId;
		} else {
			loincPartMapVersion = codeSystemVersionId;
		}

		Enumerations.ConceptMapEquivalence equivalence =
				switch (trim(defaultString(mapType))) {
					case "", "equivalent" ->
					// 'equal' is more exact than 'equivalent' in the equivalence codes
					Enumerations.ConceptMapEquivalence.EQUAL;
					case "narrower" -> Enumerations.ConceptMapEquivalence.NARROWER;
					case "wider" -> Enumerations.ConceptMapEquivalence.WIDER;
					case "relatedto" -> Enumerations.ConceptMapEquivalence.RELATEDTO;
					default -> throw new InternalErrorException(
							Msg.code(916) + "Unknown equivalence '" + mapType + "' for PartNumber: " + partNumber);
				};

		String loincPartMapId;
		String loincPartMapUri;
		String loincPartMapName;
		switch (extCodeSystem) {
			case ITermLoaderSvc.SCT_URI -> {
				loincPartMapId = LOINC_SCT_PART_MAP_ID;
				loincPartMapUri = LOINC_SCT_PART_MAP_URI;
				loincPartMapName = LOINC_SCT_PART_MAP_NAME;
			}
			case "http://www.nlm.nih.gov/research/umls/rxnorm" -> {
				loincPartMapId = LOINC_RXNORM_PART_MAP_ID;
				loincPartMapUri = LOINC_RXNORM_PART_MAP_URI;
				loincPartMapName = LOINC_RXNORM_PART_MAP_NAME;
			}
			case "http://www.radlex.org" -> {
				loincPartMapId = LOINC_PART_TO_RID_PART_MAP_ID;
				loincPartMapUri = LOINC_PART_TO_RID_PART_MAP_URI;
				loincPartMapName = LOINC_PART_TO_RID_PART_MAP_NAME;
			}
			case "http://pubchem.ncbi.nlm.nih.gov" -> {
				loincPartMapId = LOINC_PUBCHEM_PART_MAP_ID;
				loincPartMapUri = LOINC_PUBCHEM_PART_MAP_URI;
				loincPartMapName = LOINC_PUBCHEM_PART_MAP_NAME;
			}
			default -> {
				loincPartMapId = extCodeSystem.replaceAll("[^a-zA-Z]", "");
				loincPartMapUri = extCodeSystem;
				loincPartMapName = "Unknown Mapping";
			}
		}

		String conceptMapVersion = theJobMetadata.getCodeSystem().getVersion();
		if (isNotBlank(conceptMapVersion)) {
			loincPartMapId += "-" + conceptMapVersion;
		}

		if (isBlank(extCodeSystemCopyrightNotice)) {
			extCodeSystemCopyrightNotice = CM_RSNA_COPYRIGHT;
		}

		addConceptMapEntry(
				theContext,
				new ConceptMapping()
						.setConceptMapId(loincPartMapId)
						.setConceptMapUri(loincPartMapUri)
						.setConceptMapVersion(loincPartMapVersion)
						.setConceptMapName(loincPartMapName)
						.setSourceCodeSystem(ITermLoaderSvc.LOINC_URI)
						.setSourceCodeSystemVersion(codeSystemVersionId)
						.setSourceCode(partNumber)
						.setSourceDisplay(partName)
						.setTargetCodeSystem(extCodeSystem)
						.setTargetCode(extCodeId)
						.setTargetDisplay(extCodeDisplayName)
						.setTargetCodeSystemVersion(extCodeSystemVersion)
						.setEquivalence(equivalence)
						.setCopyright(extCodeSystemCopyrightNotice));
	}
}
