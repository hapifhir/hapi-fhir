package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyMetadataAttachmentJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ValueSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum.LOINC_ANSWERLIST_VERSION;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

/**
 * @see ImportLoincJobAppCtx#importLoincStep5AnswerLists()
 */
public class ImportLoincStep5HandleAnswerLists
		extends BaseImportLoincStep<ImportLoincStep5HandleAnswerLists.MyContext> {
	private static final Logger ourLog = LoggerFactory.getLogger(ImportLoincStep5HandleAnswerLists.class);

	@Override
	protected MyContext newContextObject(
			StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails) {
		return new MyContext(theStepExecutionDetails);
	}

	@Nonnull
	@Override
	protected List<LoincFileNameSpecification> getFilesToProcess(
			StepExecutionDetails<ImportLoincJobParameters, ?> theStepExecutionDetails) {
		return List.of(new LoincFileNameSpecification(
				FileHandlingType.CSV_SPLIT_WITH_REPEAT_HEADER_50000_LINE_CHUNKS,
				LoincUploadPropertiesEnum.LOINC_ANSWERLIST_FILE,
				LoincUploadPropertiesEnum.LOINC_ANSWERLIST_FILE_DEFAULT));
	}

	@Override
	protected void handleRecord(
			StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails,
			ImportTerminologyMetadataAttachmentJson theJobMetadata,
			ImportLoincJobParameters theJobParameters,
			MyContext theContext,
			CSVRecord theRecord,
			CodeSystem theCodeSystemToPopulate,
			TerminologyFileSetJson theData,
			String theSourceFilename) {
		// this is the code for the list (will repeat)
		String answerListId = trim(theRecord.get("AnswerListId"));
		String answerListName = trim(theRecord.get("AnswerListName"));
		String answerListOid = trim(theRecord.get("AnswerListOID"));
		// this is the code for the actual answer (will not repeat)
		String answerString = trim(theRecord.get("AnswerStringId"));
		String displayText = trim(theRecord.get("DisplayText"));

		/*
		These are not yet used
		String externallyDefined = trim(theRecord.get("ExtDefinedYN"));
		String extenrallyDefinedCs = trim(theRecord.get("ExtDefinedAnswerListCodeSystem"));
		String externallyDefinedLink = trim(theRecord.get("ExtDefinedAnswerListLink"));
		String sequenceNumber = trim(theRecord.get("SequenceNumber"));
		String extCodeId = trim(theRecord.get("ExtCodeId"));
		String extCodeDisplayName = trim(theRecord.get("ExtCodeDisplayName"));
		String extCodeSystem = trim(theRecord.get("ExtCodeSystem"));
		String extCodeSystemVersion = trim(theRecord.get("ExtCodeSystemVersion"));
		 */

		// Answer list code
		if (!theContext.getAnswerListCodes().contains(answerListId)) {
			theCodeSystemToPopulate.addConcept().setCode(answerListId).setDisplay(answerListName);
			theContext.getAnswerListCodes().add(answerListId);
		}

		// Answer list ValueSet
		String codeSystemVersionId = theJobMetadata.getCodeSystem().getVersion();
		ValueSet vs = getOrAddValueSet(
				theStepExecutionDetails,
				theJobMetadata,
				theData,
				theContext,
				answerListId,
				"http://loinc.org/vs/" + answerListId,
				answerListName,
				LOINC_ANSWERLIST_VERSION.getCode());
		if (vs.getIdentifier().isEmpty()) {
			vs.addIdentifier().setSystem("urn:ietf:rfc:3986").setValue("urn:oid:" + answerListOid);
		}

		if (isNotBlank(answerString)) {

			// Answer code
			if (!theContext.getAnswerListCodes().contains(answerString)) {
				theContext.getAnswerListCodes().add(answerString);

				theCodeSystemToPopulate.addConcept().setCode(answerString).setDisplay(displayText);
			}

			vs.getCompose()
					.getIncludeFirstRep()
					.setSystem(ITermLoaderSvc.LOINC_URI)
					.setVersion(codeSystemVersionId)
					.addConcept()
					.setCode(answerString)
					.setDisplay(displayText);
		}
	}

	protected static class MyContext extends MyBaseContext {
		private final Set<String> myAnswerListCodes = new HashSet<>();

		public MyContext(StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theData) {
			super();
		}

		public Set<String> getAnswerListCodes() {
			return myAnswerListCodes;
		}
	}
}
