package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyMetadataAttachmentJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.PrimitiveType;
import org.hl7.fhir.r4.model.ValueSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincJobAppCtx.LOINC_WEBSITE_URL;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincJobAppCtx.REGENSTRIEF_INSTITUTE_INC;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseImportLoincStepWithValueSetsAndConceptMaps<
				CT extends BaseImportLoincStepWithValueSetsAndConceptMaps.MyBaseContext>
		extends BaseImportLoincStep<CT> {

	private static final Logger ourLog = LoggerFactory.getLogger(BaseImportLoincStepWithValueSetsAndConceptMaps.class);

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Nonnull
	protected CodeSystem.ConceptDefinitionComponent getOrAddConcept(
			CT theContext, CodeSystem theCodeSystemToPopulate, String theCode) {
		CodeSystem.ConceptDefinitionComponent loincCode;
		loincCode = theContext.getCodeToConcept().get(theCode);
		if (loincCode == null) {
			loincCode = theCodeSystemToPopulate.addConcept();
			loincCode.setCode(theCode);
			theContext.getCodeToConcept().put(theCode, loincCode);
		}
		return loincCode;
	}

	protected void addConceptMapEntry(CT theContext, ConceptMapping theMapping) {
		Validate.notBlank(theMapping.getConceptMapId(), "ConceptMap ID must not be blank");

		if (isBlank(theMapping.getSourceCode())) {
			return;
		}
		if (isBlank(theMapping.getTargetCode())) {
			return;
		}

		theContext.getIdToConceptMappings().put(theMapping.getConceptMapId(), theMapping);
	}

	protected ValueSet getOrAddValueSet(
			StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails,
			ImportTerminologyMetadataAttachmentJson theJobMetadata,
			TerminologyFileSetJson theData,
			CT theContext,
			String theValueSetId,
			String theValueSetUri,
			String theValueSetName,
			String theVersionPropertyName) {

		String version;
		String codeSystemVersion = theJobMetadata.getCodeSystem().getVersion();
		assert isNotBlank(codeSystemVersion);

		Properties jobProperties = getJobProperties(theStepExecutionDetails);
		if (isNotBlank(theVersionPropertyName) && isNotBlank(jobProperties.getProperty(theVersionPropertyName))) {
			version = jobProperties.getProperty(theVersionPropertyName) + "-" + codeSystemVersion;
		} else {
			version = codeSystemVersion;
		}

		ValueSet vs;
		String valueSetId = theValueSetId + "-" + codeSystemVersion;

		if (!theContext.getIdToValueSet().containsKey(valueSetId)) {
			vs = new ValueSet();
			vs.setUrl(theValueSetUri);
			vs.setId(valueSetId);
			vs.setVersion(version);
			vs.setStatus(Enumerations.PublicationStatus.DRAFT);
			vs.setPublisher(REGENSTRIEF_INSTITUTE_INC);
			vs.addContact()
					.setName(REGENSTRIEF_INSTITUTE_INC)
					.addTelecom()
					.setSystem(ContactPoint.ContactPointSystem.URL)
					.setValue(LOINC_WEBSITE_URL);
			vs.setCopyright(theJobMetadata.getCodeSystem().getCopyright());
			theContext.getIdToValueSet().put(valueSetId, vs);
			theData.addResourceToActivate("ValueSet/" + valueSetId);
		} else {
			vs = theContext.getIdToValueSet().get(valueSetId);
		}

		if (isBlank(vs.getName()) && isNotBlank(theValueSetName)) {
			vs.setName(theValueSetName);
		}

		return vs;
	}

	void addCodeAsIncludeToValueSet(ValueSet theVs, String theCodeSystemUrl, String theCode, String theDisplayName) {
		ValueSet.ConceptSetComponent include = null;
		for (ValueSet.ConceptSetComponent next : theVs.getCompose().getInclude()) {
			if (next.getSystem().equals(theCodeSystemUrl)) {
				include = next;
				break;
			}
		}
		if (include == null) {
			include = theVs.getCompose().addInclude();
			include.setSystem(theCodeSystemUrl);
			if (StringUtils.isNotBlank(theVs.getVersion())) {
				include.setVersion(theVs.getVersion());
			}
		}

		boolean found = false;
		for (ValueSet.ConceptReferenceComponent next : include.getConcept()) {
			if (next.getCode().equals(theCode)) {
				found = true;
			}
		}
		if (!found) {
			include.addConcept().setCode(theCode).setDisplay(theDisplayName);
		}
	}

	@Override
	protected void syncToDb(
			ImportTerminologyMetadataAttachmentJson theJobMetadata,
			CT theCodeExtractionContext,
			CodeSystem theCodeSystemToPopulate,
			StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails) {
		super.syncToDb(theJobMetadata, theCodeExtractionContext, theCodeSystemToPopulate, theStepExecutionDetails);

		syncConceptMapsToDb(theJobMetadata, theCodeExtractionContext, theStepExecutionDetails);
		syncValueSetsToDb(theCodeExtractionContext, theStepExecutionDetails);
	}

	private void syncConceptMapsToDb(
			ImportTerminologyMetadataAttachmentJson theJobMetadata,
			CT theCodeExtractionContext,
			StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails) {
		IFhirResourceDao conceptMapDao = myDaoRegistry.getResourceDao("ConceptMap");
		for (Map.Entry<String, Collection<ConceptMapping>> entry :
				theCodeExtractionContext.getIdToConceptMappings().asMap().entrySet()) {

			String conceptMapId = entry.getKey();
			Collection<ConceptMapping> mappings = entry.getValue();

			executeInNewTransactionWithRetry(
					() -> {
						syncConceptMapToDb(
								theJobMetadata, theStepExecutionDetails, conceptMapId, conceptMapDao, mappings);
						return null;
					},
					theStepExecutionDetails);
		}
	}

	private void syncConceptMapToDb(
			ImportTerminologyMetadataAttachmentJson theJobMetadata,
			StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails,
			String conceptMapId,
			IFhirResourceDao conceptMapDao,
			Collection<ConceptMapping> mappings) {
		ourLog.info("Checking for existence of ConceptMap: {}", conceptMapId);

		ConceptMap conceptMap;
		try {
			SystemRequestDetails requestDetails = theStepExecutionDetails.newSystemRequestDetails();
			IdType existingId = new IdType(conceptMapId);
			conceptMap = (ConceptMap) conceptMapDao.read(existingId, requestDetails);
			ourLog.info("Found existing ConceptMap: {}", conceptMapId);
			assert conceptMap != null : "Reading ConceptMap " + conceptMapId + " returned null";

		} catch (ResourceNotFoundException | ResourceGoneException e) {
			ConceptMapping firstMapping = mappings.iterator().next();

			ourLog.info("Creating new ConceptMap: {}", conceptMapId);
			getRecordsAddedCounter(theStepExecutionDetails).incrementConceptMapsAdded(1);

			conceptMap = new ConceptMap();
			conceptMap.setUrl(firstMapping.getConceptMapUri());
			conceptMap.setName(firstMapping.getConceptMapName());
			conceptMap.setVersion(firstMapping.getConceptMapVersion());
			conceptMap.setPublisher(REGENSTRIEF_INSTITUTE_INC);
			conceptMap
					.addContact()
					.setName(REGENSTRIEF_INSTITUTE_INC)
					.addTelecom()
					.setSystem(ContactPoint.ContactPointSystem.URL)
					.setValue(LOINC_WEBSITE_URL);

			String copyright = firstMapping.getCopyright();
			if (!copyright.contains("LOINC")) {
				String loincCopyrightStatement = theJobMetadata.getCodeSystem().getCopyright();
				copyright = loincCopyrightStatement + (loincCopyrightStatement.endsWith(".") ? " " : ". ") + copyright;
			}
			conceptMap.setCopyright(copyright);
		}

		int addedMappings = 0;
		int skippedMappings = 0;
		for (ConceptMapping nextMapping : mappings) {

			ConceptMap.SourceElementComponent source = null;
			ConceptMap.ConceptMapGroupComponent group = null;

			for (ConceptMap.ConceptMapGroupComponent next : conceptMap.getGroup()) {
				if (next.getSource().equals(nextMapping.getSourceCodeSystem())) {
					if (next.getTarget().equals(nextMapping.getTargetCodeSystem())) {
						if (!defaultString(nextMapping.getTargetCodeSystemVersion())
								.equals(defaultString(next.getTargetVersion()))) {
							continue;
						}
						group = next;
						break;
					}
				}
			}
			if (group == null) {
				group = conceptMap.addGroup();
				group.setSource(nextMapping.getSourceCodeSystem());
				group.setSourceVersion(nextMapping.getSourceCodeSystemVersion());
				group.setTarget(nextMapping.getTargetCodeSystem());
				group.setTargetVersion(defaultIfBlank(nextMapping.getTargetCodeSystemVersion(), null));
			}

			for (ConceptMap.SourceElementComponent next : group.getElement()) {
				if (next.getCode().equals(nextMapping.getSourceCode())) {
					source = next;
				}
			}
			if (source == null) {
				source = group.addElement();
				source.setCode(nextMapping.getSourceCode());
				source.setDisplay(nextMapping.getSourceDisplay());
			}

			boolean found = false;
			for (ConceptMap.TargetElementComponent next : source.getTarget()) {
				if (next.getCode().equals(nextMapping.getTargetCode())) {
					found = true;
				}
			}
			if (!found) {
				source.addTarget()
						.setCode(nextMapping.getTargetCode())
						.setDisplay(nextMapping.getTargetDisplay())
						.setEquivalence(nextMapping.getEquivalence());
				addedMappings++;
			} else {
				skippedMappings++;
				ourLog.atDebug()
						.setMessage("Not going to add a mapping from [{}/{}] to [{}/{}] because one already exists")
						.addArgument(nextMapping.getSourceCodeSystem())
						.addArgument(nextMapping.getSourceCode())
						.addArgument(nextMapping.getTargetCodeSystem())
						.addArgument(nextMapping.getTargetCode())
						.log();
			}
		}

		if (addedMappings > 0) {

			SystemRequestDetails requestDetails = theStepExecutionDetails.newSystemRequestDetails();
			if (conceptMap.getId() == null) {
				/*
				 * Create but with an assigned ID. We do this as a create instead of an update
				 * in order to avoid the possibility of a race condition where a new ConceptMap
				 * is created by another thread while we are trying to also create it here, since
				 * this would result in us overwriting the other thread's ConceptMap.
				 */
				conceptMap.setId(conceptMapId);
				conceptMap.setUserData(
						JpaConstants.RESOURCE_ID_SERVER_ASSIGNED_VALUE,
						conceptMap.getIdElement().getIdPart());
				conceptMapDao.create(conceptMap, requestDetails);
			} else {
				conceptMapDao.update(conceptMap, requestDetails);
			}

			getRecordsAddedCounter(theStepExecutionDetails).incrementConceptMapMappingsAdded(addedMappings);
		}

		ourLog.atInfo()
				.setMessage("Adding {} mappings and skipped {} pre-existing mappings to LOINC ConceptMap {}")
				.addArgument(addedMappings)
				.addArgument(skippedMappings)
				.addArgument(conceptMap.getId())
				.log();
	}

	private void syncValueSetsToDb(
			CT theCodeExtractionContext,
			StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails) {
		IFhirResourceDao valueSetDao = myDaoRegistry.getResourceDao("ValueSet");
		for (ValueSet valueSet : theCodeExtractionContext.getIdToValueSet().values()) {
			executeInNewTransactionWithRetry(
					() -> {
						syncValueSetToDb(theStepExecutionDetails, valueSet, valueSetDao);
						return null;
					},
					theStepExecutionDetails);
		}
	}

	private void syncValueSetToDb(
			StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails,
			ValueSet valueSet,
			IFhirResourceDao valueSetDao) {
		try {
			SystemRequestDetails requestDetails = theStepExecutionDetails.newSystemRequestDetails();
			IdType existingId = new IdType(valueSet.getIdElement().getIdPart());
			ValueSet existing = (ValueSet) valueSetDao.read(existingId, requestDetails);
			assert existing != null : "Reading ValueSet " + valueSet.getId() + " returned null";

			/*
			 * A ValueSet already exists with the given ID, so we'll merge the contents
			 * of our ValueSet into it and save it.
			 */

			int addedCodes = 0;

			for (ValueSet.ConceptSetComponent sourceInclude :
					valueSet.getCompose().getInclude()) {
				ValueSet.ConceptSetComponent targetInclude = findOrAddMatchingConceptSetComponent(
						existing.getCompose().getInclude(), sourceInclude);

				// Add codes
				Set<String> existingCodes = targetInclude.getConcept().stream()
						.map(ValueSet.ConceptReferenceComponent::getCode)
						.collect(Collectors.toSet());
				for (ValueSet.ConceptReferenceComponent toAdd : sourceInclude.getConcept()) {
					if (!existingCodes.contains(toAdd.getCode())) {
						existing.getCompose().getIncludeFirstRep().addConcept(toAdd);
						addedCodes++;
					}
				}
			}

			if (isNotBlank(valueSet.getName())) {
				getRecordsAddedCounter(theStepExecutionDetails).incrementOtherChanges(1);
				existing.setName(valueSet.getName());
			}

			ourLog.atInfo()
					.setMessage("Updating existing LOINC ValueSet {} to add {} codes")
					.addArgument(valueSet.getId())
					.addArgument(addedCodes)
					.log();

			requestDetails = theStepExecutionDetails.newSystemRequestDetails();
			valueSetDao.update(existing, requestDetails);

			getRecordsAddedCounter(theStepExecutionDetails).incrementValueSetCodesAdded(addedCodes);

		} catch (ResourceNotFoundException | ResourceGoneException e) {

			/*
			 * Ok, we didn't find an existing ValueSet with the given ID, so we'll
			 * store the ValueSet as a new one.
			 */
			getRecordsAddedCounter(theStepExecutionDetails).incrementValueSetsAdded(1);

			int codeCount = 0;
			if (valueSet.hasCompose()
					&& valueSet.getCompose().hasInclude()
					&& valueSet.getCompose().getIncludeFirstRep().hasConcept()) {
				codeCount = Math.toIntExact(valueSet.getCompose().getIncludeFirstRep().getConcept().size());
			}

			ourLog.atInfo()
					.setMessage("Creating new LOINC ValueSet {} with {} code inclusions")
					.addArgument(valueSet.getId())
					.addArgument(codeCount)
					.log();
			SystemRequestDetails requestDetails = theStepExecutionDetails.newSystemRequestDetails();

			/*
			 * Create but with an assigned ID. We do this as a create instead of an update
			 * in order to avoid the possibility of a race condition where a new ValueSet
			 * is created by another thread while we are trying to also create it here, since
			 * this would result in us overwriting the other thread's ValueSet.
			 */
			valueSet.setUserData(
					JpaConstants.RESOURCE_ID_SERVER_ASSIGNED_VALUE,
					valueSet.getIdElement().getIdPart());
			valueSetDao.create(valueSet, requestDetails);

			getRecordsAddedCounter(theStepExecutionDetails).incrementValueSetCodesAdded(codeCount);
		}
	}

	private ValueSet.ConceptSetComponent findOrAddMatchingConceptSetComponent(
			List<ValueSet.ConceptSetComponent> theTargetList, ValueSet.ConceptSetComponent theSetToFind) {
		ConceptSetComponentIdentity toFind = new ConceptSetComponentIdentity(theSetToFind);
		for (ValueSet.ConceptSetComponent next : theTargetList) {
			ConceptSetComponentIdentity nextIdentity = new ConceptSetComponentIdentity(next);
			if (toFind.equals(nextIdentity)) {
				return next;
			}
		}

		// Not found
		ValueSet.ConceptSetComponent newSet = new ValueSet.ConceptSetComponent();
		theTargetList.add(newSet);

		newSet.setSystem(theSetToFind.getSystem());
		newSet.setValueSet(theSetToFind.getValueSet());
		return newSet;
	}

	private record ConceptSetComponentIdentity(String system, Set<String> valueSets) {
		public ConceptSetComponentIdentity(ValueSet.ConceptSetComponent theSetToFind) {
			this(
					theSetToFind.getSystem(),
					theSetToFind.getValueSet().stream()
							.map(PrimitiveType::getValue)
							.filter(StringUtils::isNotBlank)
							.collect(Collectors.toSet()));
		}
	}

	protected static class MyBaseContext {

		private final Map<String, ValueSet> myIdToValueSet = new HashMap<>();
		private final SetMultimap<String, ConceptMapping> myIdToConceptMappings =
				MultimapBuilder.hashKeys().linkedHashSetValues().build();
		private final Map<String, CodeSystem.ConceptDefinitionComponent> myCodeToConcept = new HashMap<>();

		public MyBaseContext() {
		}

		public Map<String, CodeSystem.ConceptDefinitionComponent> getCodeToConcept() {
			return myCodeToConcept;
		}

		public SetMultimap<String, ConceptMapping> getIdToConceptMappings() {
			return myIdToConceptMappings;
		}

		public Map<String, ValueSet> getIdToValueSet() {
			return myIdToValueSet;
		}
	}

	protected static class ConceptMapping {

		private String myCopyright;
		private String myConceptMapId;
		private String myConceptMapUri;
		private String myConceptMapVersion;
		private String myConceptMapName;
		private String mySourceCodeSystem;
		private String mySourceCodeSystemVersion;
		private String mySourceCode;
		private String mySourceDisplay;
		private String myTargetCodeSystem;
		private String myTargetCode;
		private String myTargetDisplay;
		private Enumerations.ConceptMapEquivalence myEquivalence;
		private String myTargetCodeSystemVersion;

		String getConceptMapId() {
			return myConceptMapId;
		}

		ConceptMapping setConceptMapId(String theConceptMapId) {
			myConceptMapId = theConceptMapId;
			return this;
		}

		String getConceptMapName() {
			return myConceptMapName;
		}

		ConceptMapping setConceptMapName(String theConceptMapName) {
			myConceptMapName = theConceptMapName;
			return this;
		}

		String getConceptMapUri() {
			return myConceptMapUri;
		}

		ConceptMapping setConceptMapUri(String theConceptMapUri) {
			myConceptMapUri = theConceptMapUri;
			return this;
		}

		String getConceptMapVersion() {
			return myConceptMapVersion;
		}

		ConceptMapping setConceptMapVersion(String theConceptMapVersion) {
			myConceptMapVersion = theConceptMapVersion;
			return this;
		}

		String getCopyright() {
			return myCopyright;
		}

		ConceptMapping setCopyright(String theCopyright) {
			myCopyright = theCopyright;
			return this;
		}

		Enumerations.ConceptMapEquivalence getEquivalence() {
			return myEquivalence;
		}

		ConceptMapping setEquivalence(Enumerations.ConceptMapEquivalence theEquivalence) {
			myEquivalence = theEquivalence;
			return this;
		}

		String getSourceCode() {
			return mySourceCode;
		}

		ConceptMapping setSourceCode(String theSourceCode) {
			mySourceCode = theSourceCode;
			return this;
		}

		String getSourceCodeSystem() {
			return mySourceCodeSystem;
		}

		ConceptMapping setSourceCodeSystem(String theSourceCodeSystem) {
			mySourceCodeSystem = theSourceCodeSystem;
			return this;
		}

		String getSourceCodeSystemVersion() {
			return mySourceCodeSystemVersion;
		}

		ConceptMapping setSourceCodeSystemVersion(String theSourceCodeSystemVersion) {
			mySourceCodeSystemVersion = theSourceCodeSystemVersion;
			return this;
		}

		String getSourceDisplay() {
			return mySourceDisplay;
		}

		ConceptMapping setSourceDisplay(String theSourceDisplay) {
			mySourceDisplay = theSourceDisplay;
			return this;
		}

		String getTargetCode() {
			return myTargetCode;
		}

		ConceptMapping setTargetCode(String theTargetCode) {
			myTargetCode = theTargetCode;
			return this;
		}

		String getTargetCodeSystem() {
			return myTargetCodeSystem;
		}

		ConceptMapping setTargetCodeSystem(String theTargetCodeSystem) {
			myTargetCodeSystem = theTargetCodeSystem;
			return this;
		}

		String getTargetCodeSystemVersion() {
			return myTargetCodeSystemVersion;
		}

		ConceptMapping setTargetCodeSystemVersion(String theTargetCodeSystemVersion) {
			myTargetCodeSystemVersion = theTargetCodeSystemVersion;
			return this;
		}

		String getTargetDisplay() {
			return myTargetDisplay;
		}

		ConceptMapping setTargetDisplay(String theTargetDisplay) {
			myTargetDisplay = theTargetDisplay;
			return this;
		}

		@Override
		public boolean equals(Object theO) {
			if (!(theO instanceof ConceptMapping that)) {
				return false;
			}
			return Objects.equals(myCopyright, that.myCopyright)
					&& Objects.equals(myConceptMapId, that.myConceptMapId)
					&& Objects.equals(myConceptMapUri, that.myConceptMapUri)
					&& Objects.equals(myConceptMapVersion, that.myConceptMapVersion)
					&& Objects.equals(myConceptMapName, that.myConceptMapName)
					&& Objects.equals(mySourceCodeSystem, that.mySourceCodeSystem)
					&& Objects.equals(mySourceCodeSystemVersion, that.mySourceCodeSystemVersion)
					&& Objects.equals(mySourceCode, that.mySourceCode)
					&& Objects.equals(mySourceDisplay, that.mySourceDisplay)
					&& Objects.equals(myTargetCodeSystem, that.myTargetCodeSystem)
					&& Objects.equals(myTargetCode, that.myTargetCode)
					&& Objects.equals(myTargetDisplay, that.myTargetDisplay)
					&& myEquivalence == that.myEquivalence
					&& Objects.equals(myTargetCodeSystemVersion, that.myTargetCodeSystemVersion);
		}

		@Override
		public int hashCode() {
			return Objects.hash(
					myCopyright,
					myConceptMapId,
					myConceptMapUri,
					myConceptMapVersion,
					myConceptMapName,
					mySourceCodeSystem,
					mySourceCodeSystemVersion,
					mySourceCode,
					mySourceDisplay,
					myTargetCodeSystem,
					myTargetCode,
					myTargetDisplay,
					myEquivalence,
					myTargetCodeSystemVersion);
		}
	}
}
