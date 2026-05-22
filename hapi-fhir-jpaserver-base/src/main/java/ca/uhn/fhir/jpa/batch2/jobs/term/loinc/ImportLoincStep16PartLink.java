package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum;
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

import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_LINK_FILE_PRIMARY;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_LINK_FILE_PRIMARY_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_LINK_FILE_SUPPLEMENTARY;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_LINK_FILE_SUPPLEMENTARY_DEFAULT;
import static org.apache.commons.lang3.StringUtils.trim;

public class ImportLoincStep16PartLink
		extends BaseImportLoincStepWithValueSetsAndConceptMaps<ImportLoincStep16PartLink.MyBaseContext> {
	private static final Logger ourLog = LoggerFactory.getLogger(ImportLoincStep16PartLink.class);

	@Override
	protected MyBaseContext newContextObject(
			StepExecutionDetails<ImportLoincJobParameters, ImportLoincFileSetJson> theStepExecutionDetails) {
		return new MyBaseContext(theStepExecutionDetails);
	}

	@Nonnull
	@Override
	public FileHandlingType getFileHandlingType() {
		return FileHandlingType.CSV_SPLIT_WITH_REPEAT_HEADER_1000_LINE_CHUNKS;
	}

	@Nonnull
	@Override
	protected List<LoincFileNameSpecification> getFilesToProcess(StepExecutionDetails<ImportLoincJobParameters, ?> theStepExecutionDetails) {
		return List.of(
				new LoincFileNameSpecification(
						LoincUploadPropertiesEnum.LOINC_PART_LINK_FILE,
						LoincUploadPropertiesEnum.LOINC_PART_LINK_FILE_DEFAULT),
				new LoincFileNameSpecification(LOINC_PART_LINK_FILE_PRIMARY, LOINC_PART_LINK_FILE_PRIMARY_DEFAULT),
				new LoincFileNameSpecification(
						LOINC_PART_LINK_FILE_SUPPLEMENTARY, LOINC_PART_LINK_FILE_SUPPLEMENTARY_DEFAULT));
	}

	@Override
	protected void handleRecord(
		StepExecutionDetails<ImportLoincJobParameters, ImportLoincFileSetJson> theStepExecutionDetails, ImportLoincJobParameters theJobParameters,
		MyBaseContext theContext,
		CSVRecord theRecord,
		CodeSystem theCodeSystemToPopulate,
		ImportLoincFileSetJson theData, String theSourceFilename) {

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
				theContext.getPropertyNameToType().get(propertyPart);
		if (propertyType == null) {
			return;
		}

		CodeSystem.ConceptDefinitionComponent concept =
				getOrAddConcept(theContext, theCodeSystemToPopulate, loincNumber);

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
