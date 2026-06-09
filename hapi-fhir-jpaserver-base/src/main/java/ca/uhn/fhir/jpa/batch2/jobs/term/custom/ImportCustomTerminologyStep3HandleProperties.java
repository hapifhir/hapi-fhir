package ca.uhn.fhir.jpa.batch2.jobs.term.custom;

import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseImportTerminologyFileCsvStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseImportTerminologyFileStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyJobParameters;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyMetadataAttachmentJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.common.hapi.validation.util.TermConceptPropertyTypeEnum;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Type;

import java.util.List;

import static ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants.CUSTOM_PROPERTIES_FILE;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class ImportCustomTerminologyStep3HandleProperties
		extends BaseImportTerminologyFileCsvStep<
				ImportTerminologyJobParameters, BaseImportTerminologyFileStep.MyBaseContext> {

	private final FhirContext myCanonicalFhirContext = FhirContext.forR4Cached();

	public static final String CODE = "CODE";
	public static final String KEY = "KEY";
	public static final String VALUE = "VALUE";
	public static final String TYPE = "TYPE";

	@Nonnull
	@Override
	public List<LoincFileNameSpecification> getFilesToProcess(
			StepExecutionDetails<ImportTerminologyJobParameters, ?> theStepExecutionDetails) {
		return List.of(new LoincFileNameSpecification(
				FileHandlingType.CSV_SPLIT_WITH_REPEAT_HEADER_1000_LINE_CHUNKS,
				t -> t.endsWith(CUSTOM_PROPERTIES_FILE)));
	}

	@Override
	public boolean mustFindFile() {
		return false;
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
		String code = trim(theRecord.get(CODE));
		String key = trim(theRecord.get(KEY));

		if (isNotBlank(code) && isNotBlank(key)) {
			String value = trim(theRecord.get(VALUE));
			String type = trim(theRecord.get(TYPE));

			CodeSystem.ConceptDefinitionComponent concept = getOrAddConcept(theContext, code);

			TermConceptPropertyTypeEnum typeEnum = TermConceptPropertyTypeEnum.fromString(type);
			Validate.notNull(typeEnum, "Invalid property type[%s] for key: %s", type, key);

			CodeSystem.ConceptPropertyComponent conceptProperty = concept.addProperty();
			conceptProperty.setCode(key);

			switch (typeEnum) {
				case BOOLEAN, INTEGER, DECIMAL, DATETIME, CODE, STRING -> {
					IPrimitiveType<?> valueInstance = (IPrimitiveType<?>) myCanonicalFhirContext
							.getElementDefinition(typeEnum.getDatatype())
							.newInstance();
					valueInstance.setValueAsString(value);
					conceptProperty.setValue((Type) valueInstance);
				}
				case CODING -> {
					Coding coding = new Coding();
					myCanonicalFhirContext.newJsonParser().parseInto(value, coding);
					conceptProperty.setValue(coding);
				}
				default -> throw new IllegalArgumentException(
						Msg.code(2970) + "Unable to handle property type: " + type);
			}
		}
	}

	@Override
	protected MyBaseContext newContextObject(
			StepExecutionDetails<ImportTerminologyJobParameters, TerminologyFileSetJson> theStepExecutionDetails) {
		return new MyBaseContext();
	}
}
