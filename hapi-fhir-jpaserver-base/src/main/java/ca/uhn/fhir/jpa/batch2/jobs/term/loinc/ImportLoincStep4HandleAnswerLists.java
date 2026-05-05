package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.ValueSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.term.loinc.BaseLoincHandler.LOINC_WEBSITE_URL;
import static ca.uhn.fhir.jpa.term.loinc.BaseLoincHandler.REGENSTRIEF_INSTITUTE_INC;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_ANSWERLIST_FILE;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_ANSWERLIST_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_ANSWERLIST_VERSION;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

// FIXME: make sure we don't expand ValueSets until status = active
public class ImportLoincStep4HandleAnswerLists extends BaseImportLoincStepWithValueSetsAndConceptMaps<ImportLoincStep4HandleAnswerLists.MyContext> {
	private static final Logger ourLog = LoggerFactory.getLogger(ImportLoincStep4HandleAnswerLists.class);

	@Override
	protected MyContext newContextObject(StepExecutionDetails<LoincJobImportParameters, ImportLoincFileSetJson> theStepExecutionDetails) {
		return new MyContext();
	}

	@Nonnull
	@Override
	protected LoincUploadPropertiesEnum provideFileNameDefault() {
		return LOINC_ANSWERLIST_FILE_DEFAULT;
	}

	@Nonnull
	@Override
	protected LoincUploadPropertiesEnum provideFileNamePropertyFileKey() {
		return LOINC_ANSWERLIST_FILE;
	}

	@Override
	protected void handleRecord(LoincJobImportParameters theJobParameters, MyContext theContext, CSVRecord theRecord, CodeSystem theCodeSystemToPopulate, ImportLoincFileSetJson theData) {
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
			theCodeSystemToPopulate
				.addConcept()
				.setCode(answerListId)
				.setDisplay(answerListName);
			theContext.getAnswerListCodes().add(answerListId);
		}

		// Answer list ValueSet
		String codeSystemVersionId = theData.getLoincCodeSystem().getVersion();
		ValueSet vs = getValueSet(theJobParameters, theData, theContext,
			answerListId, "http://loinc.org/vs/" + answerListId, answerListName, LOINC_ANSWERLIST_VERSION.getCode());
		if (vs.getIdentifier().isEmpty()) {
			vs.addIdentifier().setSystem("urn:ietf:rfc:3986").setValue("urn:oid:" + answerListOid);
		}

		if (isNotBlank(answerString)) {

			// Answer code
			if (!theContext.getAnswerListCodes().contains(answerString)) {
				theContext.getAnswerListCodes().add(answerString);

				theCodeSystemToPopulate
					.addConcept()
					.setCode(answerString)
					.setDisplay(displayText);
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


	protected static class MyContext extends BaseContext {
		private final Set<String> myAnswerListCodes = new HashSet<>();

		public Set<String> getAnswerListCodes() {
			return myAnswerListCodes;
		}
	}

}
