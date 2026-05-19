package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.term.TermLoaderSvcImpl;
import ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class ImportLoincStep2HandleConcepts
		extends BaseImportLoincStep<ImportLoincStep2HandleConcepts.CodeExtractionContext> {
	private static final Logger ourLog = LoggerFactory.getLogger(ImportLoincStep2HandleConcepts.class);

	@Nonnull
	@Override
	protected List<LoincFileNameSpecification> getFilesToProcess() {
		return List.of(new LoincFileNameSpecification(
				LoincUploadPropertiesEnum.LOINC_FILE, LoincUploadPropertiesEnum.LOINC_FILE_DEFAULT));
	}

	@Override
	protected CodeExtractionContext newContextObject(
			StepExecutionDetails<ImportLoincJobParameters, ImportLoincFileSetJson> theStepExecutionDetails) {
		return new ImportLoincStep2HandleConcepts.CodeExtractionContext(theStepExecutionDetails.getData());
	}

	@Override
	protected void handleRecord(
		StepExecutionDetails<ImportLoincJobParameters, ImportLoincFileSetJson> theStepExecutionDetails, ImportLoincJobParameters theJobParameters,
		CodeExtractionContext theContext,
		CSVRecord theRecord,
		CodeSystem theCodeSystemToPopulate,
		ImportLoincFileSetJson theData, String theSourceFilename) {
		String code = trim(theRecord.get("LOINC_NUM"));
		if (isNotBlank(code)) {
			String longCommonName = trim(theRecord.get("LONG_COMMON_NAME"));
			String shortName = trim(theRecord.get("SHORTNAME"));
			String consumerName = trim(theRecord.get("CONSUMER_NAME"));
			String display = TermLoaderSvcImpl.firstNonBlank(longCommonName, shortName, consumerName);

			CodeSystem.ConceptDefinitionComponent concept = theCodeSystemToPopulate.addConcept();
			concept.setCode(code);
			concept.setDisplay(display);

			if (isNotBlank(shortName) && !display.equalsIgnoreCase(shortName)) {
				CodeSystem.ConceptDefinitionDesignationComponent shortNameDesignation = concept.addDesignation();
				shortNameDesignation.setUse(new Coding(null, null, "ShortName"));
				shortNameDesignation.setValue(shortName);
			}

			for (String nextPropertyName : theContext.propertyNamesToTypes().keySet()) {
				if (!theRecord.toMap().containsKey(nextPropertyName)) {
					continue;
				}

				CodeSystem.PropertyType nextPropertyType =
						theContext.propertyNamesToTypes().get(nextPropertyName);

				String nextPropertyValue = theRecord.get(nextPropertyName);
				if (isNotBlank(nextPropertyValue)) {
					nextPropertyValue = trim(nextPropertyValue);

					switch (nextPropertyType) {
						case STRING:
							concept.addProperty().setCode(nextPropertyName).setValue(new StringType(nextPropertyValue));
							ourLog.trace(
									"Adding string property: {} to concept.code {}",
									nextPropertyName,
									concept.getCode());
							break;

						case CODING:
							// "Coding" property types are handled by loincCodingProperties, partlink, hierarchy,
							// RsnaPlaybook or DocumentOntology handlers
							break;

						case DECIMAL:
						case CODE:
						case INTEGER:
						case BOOLEAN:
						case DATETIME:
						case NULL:
							throw new InternalErrorException(Msg.code(915)
									+ "Don't know how to handle LOINC property of type: " + nextPropertyType);
					}
				}
			}

			boolean existingValue = theContext.seenCodes().add(code);
			if (!existingValue) {
				// FIXME: add code
				throw new JobExecutionFailedException(
						Msg.code(1) + "The code " + code + " has appeared more than once");
			}
		}
	}

	@Override
	protected void afterCsvProcessingComplete(
			CodeExtractionContext theCodeExtractionContext,
			CodeSystem theCodeSystemToPopulate,
			StepExecutionDetails<ImportLoincJobParameters, ImportLoincFileSetJson> theStepExecutionDetails) {
		super.afterCsvProcessingComplete(theCodeExtractionContext, theCodeSystemToPopulate, theStepExecutionDetails);
		ourLog.info(
				"LOINC CodeSystem populated with {} concepts",
				theCodeSystemToPopulate.getConcept().size());
	}

	@Nonnull
	private static Map<String, CodeSystem.PropertyType> extractPropertyNamesFromCodeSystem(
			ImportLoincFileSetJson data) {
		Map<String, CodeSystem.PropertyType> propertyNamesToTypes = new HashMap<>();
		for (CodeSystem.PropertyComponent nextProperty :
				data.getLoincCodeSystem().getProperty()) {
			String nextPropertyCode = nextProperty.getCode();
			CodeSystem.PropertyType nextPropertyType = nextProperty.getType();
			if (isNotBlank(nextPropertyCode)) {
				propertyNamesToTypes.put(nextPropertyCode, nextPropertyType);
			}
		}
		assert propertyNamesToTypes.size() > 1;
		return propertyNamesToTypes;
	}

	protected record CodeExtractionContext(
			Map<String, CodeSystem.PropertyType> propertyNamesToTypes, Set<String> seenCodes) {

		/**
		 * Constructor
		 */
		public CodeExtractionContext(ImportLoincFileSetJson theData) {
			this(extractPropertyNamesFromCodeSystem(theData), new HashSet<>());
		}
	}
}
