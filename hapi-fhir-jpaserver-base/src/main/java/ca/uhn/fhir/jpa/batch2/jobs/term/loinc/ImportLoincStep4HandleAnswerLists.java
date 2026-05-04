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
public class ImportLoincStep4HandleAnswerLists extends BaseImportLoincStep<ImportLoincStep4HandleAnswerLists.MyContext> {
	private static final Logger ourLog = LoggerFactory.getLogger(ImportLoincStep4HandleAnswerLists.class);

	@Autowired
	private IFhirResourceDaoValueSet<ValueSet> myValueSetDao;

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
		if (!theContext.answerListCodes().contains(answerListId)) {
			theCodeSystemToPopulate
				.addConcept()
				.setCode(answerListId)
				.setDisplay(answerListName);
			theContext.answerListCodes().add(answerListId);
		}

		// Answer list ValueSet
		String valueSetId;
		String codeSystemVersionId = theData.getLoincCodeSystem().getVersion();
		if (codeSystemVersionId != null) {
			valueSetId = answerListId + "-" + codeSystemVersionId;
		} else {
			valueSetId = answerListId;
		}
		ValueSet vs = getValueSet(theJobParameters, theData, theContext,
			valueSetId, "http://loinc.org/vs/" + answerListId, answerListName, LOINC_ANSWERLIST_VERSION.getCode());
		if (vs.getIdentifier().isEmpty()) {
			vs.addIdentifier().setSystem("urn:ietf:rfc:3986").setValue("urn:oid:" + answerListOid);
		}

		if (isNotBlank(answerString)) {

			// Answer code
			if (!theContext.answerListCodes().contains(answerString)) {
				theContext.answerListCodes().add(answerString);

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


	ValueSet getValueSet(
		LoincJobImportParameters theJobParameters, ImportLoincFileSetJson theData, MyContext theContext, String theValueSetId, String theValueSetUri, String theValueSetName, String theVersionPropertyName) {

		String version;
		String codeSystemVersion = theData.getLoincCodeSystem().getVersion();
		if (isNotBlank(theVersionPropertyName) && theJobParameters.getProperties().getProperty(theVersionPropertyName) != null) {
			if (codeSystemVersion != null) {
				version = theJobParameters.getProperties().getProperty(theVersionPropertyName) + "-" + codeSystemVersion;
			} else {
				version = theJobParameters.getProperties().getProperty(theVersionPropertyName);
			}
		} else {
			version = codeSystemVersion;
		}

		ValueSet vs;
		if (!theContext.idToValueSet().containsKey(theValueSetId)) {
			vs = new ValueSet();
			vs.setUrl(theValueSetUri);
			vs.setId(theValueSetId);
			vs.setVersion(version);
			vs.setStatus(Enumerations.PublicationStatus.DRAFT);
			vs.setPublisher(REGENSTRIEF_INSTITUTE_INC);
			vs.addContact()
				.setName(REGENSTRIEF_INSTITUTE_INC)
				.addTelecom()
				.setSystem(ContactPoint.ContactPointSystem.URL)
				.setValue(LOINC_WEBSITE_URL);
			vs.setCopyright(theData.getLoincCodeSystem().getCopyright());
			theContext.idToValueSet().put(theValueSetId, vs);
			theData.addResourceToActivate("ValueSet/" + theValueSetId);
		} else {
			vs = theContext.idToValueSet().get(theValueSetId);
		}

		if (isBlank(vs.getName()) && isNotBlank(theValueSetName)) {
			vs.setName(theValueSetName);
		}

		return vs;
	}

	@Override
	protected void afterCsvProcessingComplete(MyContext theCodeExtractionContext, CodeSystem theCodeSystemToPopulate, StepExecutionDetails<LoincJobImportParameters, ImportLoincFileSetJson> theStepExecutionDetails) {
		super.afterCsvProcessingComplete(theCodeExtractionContext, theCodeSystemToPopulate, theStepExecutionDetails);

		for (ValueSet valueSet : theCodeExtractionContext.idToValueSet.values()) {

			try {
				SystemRequestDetails requestDetails = theStepExecutionDetails.newSystemRequestDetails();
				IdType existingId = new IdType(valueSet.getIdElement().getIdPart());
				ValueSet existing = myValueSetDao.read(existingId, requestDetails);

				int addedCodes = 0;
				Set<String> existingCodes = existing.getCompose().getIncludeFirstRep().getConcept().stream().map(t -> t.getCode()).collect(Collectors.toSet());
				for (ValueSet.ConceptReferenceComponent toAdd : valueSet.getCompose().getIncludeFirstRep().getConcept()) {
					if (!existingCodes.contains(toAdd.getCode())) {
						existing.getCompose().getIncludeFirstRep().addConcept(toAdd);
						addedCodes++;
					}
				}

				ourLog
					.atInfo()
					.setMessage("Updating existing LOINC ValueSet {} to add {} codes")
					.addArgument(valueSet.getId())
					.addArgument(addedCodes)
					.log();
				requestDetails = theStepExecutionDetails.newSystemRequestDetails();
				myValueSetDao.update(existing, requestDetails);

			} catch (ResourceNotFoundException | ResourceGoneException e) {
				ourLog
					.atInfo()
					.setMessage("Creating new LOINC ValueSet {}")
					.addArgument(valueSet.getId())
					.log();
				SystemRequestDetails requestDetails = theStepExecutionDetails.newSystemRequestDetails();
				myValueSetDao.update(valueSet, requestDetails);
			}

		}

		ourLog.info("Processed {} LOINC Answer List codes in {} ValueSets", theCodeExtractionContext.answerListCodes().size(), theCodeExtractionContext.idToValueSet().size());
	}


	protected record MyContext(Set<String> answerListCodes, Map<String, ValueSet> idToValueSet) {
		public MyContext() {
			this(new HashSet<>(), new HashMap<>());
		}
	}

}
