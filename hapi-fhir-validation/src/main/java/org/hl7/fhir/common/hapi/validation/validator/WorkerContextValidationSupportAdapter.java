package org.hl7.fhir.common.hapi.validation.validator;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.Logs;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.fhir.ucum.UcumService;
import org.hl7.fhir.common.hapi.validation.support.SnapshotGeneratingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportUtils;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.TerminologyServiceException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.context.IContextResourceLoader;
import org.hl7.fhir.r5.context.IWorkerContext;
import org.hl7.fhir.r5.context.IWorkerContextManager;
import org.hl7.fhir.r5.fhirpath.FHIRPathEngine;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.CodeableConcept;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.ElementDefinition;
import org.hl7.fhir.r5.model.NamingSystem;
import org.hl7.fhir.r5.model.OperationOutcome;
import org.hl7.fhir.r5.model.PackageInformation;
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.Resource;
import org.hl7.fhir.r5.model.StringType;
import org.hl7.fhir.r5.model.StructureDefinition;
import org.hl7.fhir.r5.model.ValueSet;
import org.hl7.fhir.r5.profilemodel.PEBuilder;
import org.hl7.fhir.r5.terminologies.expansion.ValueSetExpansionOutcome;
import org.hl7.fhir.r5.terminologies.utilities.CodingValidationRequest;
import org.hl7.fhir.r5.terminologies.utilities.TerminologyServiceErrorClass;
import org.hl7.fhir.r5.terminologies.utilities.ValidationResult;
import org.hl7.fhir.r5.utils.validation.IResourceValidator;
import org.hl7.fhir.r5.utils.validation.ValidationContextCarrier;
import org.hl7.fhir.utilities.FhirPublication;
import org.hl7.fhir.utilities.TimeTracker;
import org.hl7.fhir.utilities.i18n.I18nBase;
import org.hl7.fhir.utilities.npm.BasePackageCacheManager;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.hl7.fhir.utilities.validation.ValidationOptions;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.context.support.IValidationSupport.CodeValidationIssueCoding.INVALID_DISPLAY;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * HAPI uses a bunch of utility classes from corelib (org.hl7.fhir.core). These include the
 * {@link org.hl7.fhir.validation.instance.InstanceValidator} (FHIR resource validator),
 * {@link FHIRPathEngine} (FHIRPath engine), and
 * {@link org.hl7.fhir.r5.conformance.profile.ProfileUtilities} (used for snapshot generation).
 * These tools use the {@link IWorkerContext} interface as their way of requesting
 * StructureDefinitions, concept lookups/validations, and other infrastructural things that
 * get supplied by the runtime environment.
 * <p>
 * HAPI FHIR uses {@link IValidationSupport} for this purpose, although the scopes and
 * intended design of these two interfaces don't line up exactly. This class is a
 * bridge between {@link IWorkerContext} and {@link IValidationSupport}. You can think
 * of it mostly as a dumb passthrough, where the method flow looks like the following
 * example:
 * </p>
 * <pre>
 *    InstanceValidator (is validating and needs a StructureDefinition)
 *           |
 *           | IWorkerContext#fetchResource("StructureDefinition", url)
 *           V
 *    VersionSpecificWorkerContextWrapper (implements IWorkerContext)
 *           |
 *           | IValidationSupport#fetchResource("StructureDefinition", "url")
 *           V
 *    ValidationSupportChain (implements IValidationSupport)
 * </pre>
 * <p>
 * It does also add a couple of important functions:
 * </p>
 * <ul>
 * <li>
 *     The {@link IWorkerContext} interface expects resources to be supplied using the
 *     canonical FHIR version (currently R5), where {@link IValidationSupport} works with
 *     whatever version the associated {@link FhirContext} uses. So for example if
 *     we're validating in an R4 server, the InstanceValidator asks this class
 *     for an R5 StructureDefinition, and this class asks the {@link IValidationSupport}
 *     for an R4 StructureDefinition and then converts it before returning it.
 * </li>
 * <li>
 *     The validator also needs snapshots to be generated on StructureDefinitions before
 *     it can use them. It will generate a snapshot itself if a differential profile
 *     is supplied, but this is inefficient since it will do this every time it gets
 *     such a profile. So this class generates and caches the snapshot.
 * </li>
 * </ul>
 */
public class WorkerContextValidationSupportAdapter extends I18nBase implements IWorkerContext {
	public static final FhirContext FHIR_CONTEXT_R5 = FhirContext.forR5();
	private static final Logger ourLog = Logs.getTerminologyTroubleshootingLog();
	/**
	 * When we fetch conformance resources such as StructureDefinitions from {@link IValidationSupport}
	 * they will be returned using whatever version of FHIR the underlying infrastructure is
	 * configured to support. But we need to convert it to R5 since that's what the validator
	 * uses. In order to avoid repeated conversions, we put the converted version of the resource
	 * in the {@link org.hl7.fhir.instance.model.api.IAnyResource#getUserData(String)} map
	 * using this key. Since conformance resources returned by validation support are typically
	 * cached by {@link org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain},
	 * the converted version gets cached too.
	 */
	private static final String TO_CANONICAL_USERDATA_KEY =
			WorkerContextValidationSupportAdapter.class.getName() + "_TO_CANONICAL_USERDATA_KEY";

	/**
	 * This serves a similar purpose as TO_CANONICAL_USERDATA_KEY, expect this key stores
	 * the conversion back from the canonical version.
	 */
	private static final String FROM_CANONICAL_USERDATA_KEY =
			WorkerContextValidationSupportAdapter.class.getName() + "_FROM_CANONICAL_USERDATA_KEY";

	private IValidationSupport myValidationSupport;
	private VersionCanonicalizer myVersionCanonicalizer;
	private volatile List<StructureDefinition> myAllStructures;
	private volatile Set<String> myAllPrimitiveTypes;
	private Parameters myExpansionProfile;
	private volatile FHIRPathEngine myFHIRPathEngine;
	private FhirContext myFhirContext;

	/**
	 * Constructor
	 */
	public WorkerContextValidationSupportAdapter() {
		this(null);
	}

	/**
	 * Constructor
	 *
	 * @param theValidationSupport Can be null, in which case {@link #setValidationSupport(IValidationSupport)} should be called
	 */
	public WorkerContextValidationSupportAdapter(@Nullable IValidationSupport theValidationSupport) {
		if (theValidationSupport != null) {
			setValidationSupport(theValidationSupport);
		}
	}

	/**
	 * Provides the {@link IValidationSupport} module that backs this adapter.
	 */
	public void setValidationSupport(IValidationSupport theValidationSupport) {
		if (myValidationSupport == theValidationSupport) {
			return;
		}
		Validate.isTrue(
				myValidationSupport == null, "Can not set the validation support after it has already been set");
		myValidationSupport = theValidationSupport;
		myFhirContext = theValidationSupport.getFhirContext();
		myVersionCanonicalizer = new VersionCanonicalizer(myFhirContext);
		setValidationMessageLanguage(getLocale());
	}

	@Override
	public Set<String> getBinaryKeysAsSet() {
		throw new UnsupportedOperationException(Msg.code(2118));
	}

	@Override
	public boolean hasBinaryKey(String s) {
		return myValidationSupport.fetchBinary(s) != null;
	}

	@Override
	public byte[] getBinaryForKey(String s) {
		return myValidationSupport.fetchBinary(s);
	}

	@Override
	public int loadFromPackage(NpmPackage pi, IContextResourceLoader loader) throws FHIRException {
		throw new UnsupportedOperationException(Msg.code(652));
	}

	@Override
	public int loadFromPackage(NpmPackage pi, IContextResourceLoader loader, Set<String> types) throws FHIRException {
		throw new UnsupportedOperationException(Msg.code(653));
	}

	@Override
	public int loadFromPackageAndDependencies(NpmPackage pi, IContextResourceLoader loader, BasePackageCacheManager pcm)
			throws FHIRException {
		throw new UnsupportedOperationException(Msg.code(654));
	}

	@Override
	public boolean hasPackage(String id, String ver) {
		throw new UnsupportedOperationException(Msg.code(655));
	}

	@Override
	public boolean hasPackage(PackageInformation packageInformation) {
		return false;
	}

	@Override
	public PackageInformation getPackage(String id, String ver) {
		return null;
	}

	@Override
	public int getClientRetryCount() {
		throw new UnsupportedOperationException(Msg.code(656));
	}

	@Override
	public IWorkerContext setClientRetryCount(int value) {
		throw new UnsupportedOperationException(Msg.code(657));
	}

	@Override
	public TimeTracker clock() {
		return null;
	}

	@Override
	public IWorkerContextManager.IPackageLoadingTracker getPackageTracker() {
		throw new UnsupportedOperationException(Msg.code(2235));
	}

	@Override
	public IWorkerContext setPackageTracker(IWorkerContextManager.IPackageLoadingTracker packageTracker) {
		throw new UnsupportedOperationException(Msg.code(2266));
	}

	@Override
	public String getSpecUrl() {
		return "";
	}

	@Override
	public PEBuilder getProfiledElementBuilder(
			PEBuilder.PEElementPropertiesPolicy thePEElementPropertiesPolicy, boolean theB) {
		throw new UnsupportedOperationException(Msg.code(2264));
	}

	@Override
	public PackageInformation getPackageForUrl(String s) {
		throw new UnsupportedOperationException(Msg.code(2236));
	}

	@Override
	public Parameters getExpansionParameters() {
		if (Objects.isNull(myExpansionProfile)) {
			myExpansionProfile = new Parameters();
		}
		return myExpansionProfile;
	}

	@Override
	public void setExpansionParameters(Parameters expParameters) {
		setExpansionProfile(expParameters);
	}

	public void setExpansionProfile(Parameters expParameters) {
		myExpansionProfile = expParameters;
	}

	private List<StructureDefinition> allStructureDefinitions() {

		List<StructureDefinition> retVal = myAllStructures;
		if (retVal == null) {
			List<IBaseResource> allStructureDefinitions = myValidationSupport.fetchAllStructureDefinitions();
			assert allStructureDefinitions != null;

			/*
			 * This method (allStructureDefinitions()) gets called recursively - As we
			 * try to return all StructureDefinitions, we want to generate snapshots for
			 * any that don't already have a snapshot. But the snapshot generator in turn
			 * also calls allStructureDefinitions() - That specific call doesn't require
			 * that the returned SDs have snapshots generated though.
			 *
			 * So, we first just convert to canonical version and store a list containing
			 * the canonical versions. That way any recursive calls will return the
			 * stored list. But after that we'll generate all the snapshots and
			 * store that list instead. If the canonicalization fails with an
			 * unexpected exception, we wipe the stored list. This is probably an
			 * unrecoverable failure since this method will probably always
			 * fail if it fails once. But at least this way we're likley to
			 * generate useful error messages for the user.
			 */
			retVal = allStructureDefinitions.stream()
					.map(myVersionCanonicalizer::structureDefinitionToCanonical)
					.collect(Collectors.toList());
			myAllStructures = retVal;

			try {
				retVal = new ArrayList<>();
				for (IBaseResource next : allStructureDefinitions) {
					Resource converted = convertToCanonicalVersionAndGenerateSnapshot(next, false);
					retVal.add((StructureDefinition) converted);
				}
				myAllStructures = retVal;
			} catch (Exception e) {
				ourLog.error("Failure during snapshot generation", e);
				myAllStructures = null;
			}
		}

		return retVal;
	}

	@Override
	public void cacheResource(Resource res) {}

	@Override
	public void cacheResourceFromPackage(Resource res, PackageInformation packageDetails) throws FHIRException {}

	@Override
	public void cachePackage(PackageInformation packageInformation) {}

	@Nonnull
	private ValidationResult convertValidationResult(
			String theSystem, @Nullable IValidationSupport.CodeValidationResult theResult) {
		ValidationResult retVal = null;
		if (theResult != null) {
			String code = theResult.getCode();
			String display = theResult.getDisplay();

			String issueSeverityCode = theResult.getSeverityCode();
			String message = theResult.getMessage();
			ValidationMessage.IssueSeverity issueSeverity = null;
			if (issueSeverityCode != null) {
				issueSeverity = ValidationMessage.IssueSeverity.fromCode(issueSeverityCode);
			} else if (isNotBlank(message)) {
				issueSeverity = ValidationMessage.IssueSeverity.INFORMATION;
			}

			CodeSystem.ConceptDefinitionComponent conceptDefinitionComponent = null;
			if (code != null) {
				conceptDefinitionComponent = new CodeSystem.ConceptDefinitionComponent()
						.setCode(code)
						.setDisplay(display);
			}

			retVal = new ValidationResult(
					issueSeverity,
					message,
					theSystem,
					theResult.getCodeSystemVersion(),
					conceptDefinitionComponent,
					display,
					getIssuesForCodeValidation(theResult.getIssues()));
		}

		if (retVal == null) {
			retVal = new ValidationResult(ValidationMessage.IssueSeverity.ERROR, "Validation failed", null);
		}

		return retVal;
	}

	private List<OperationOutcome.OperationOutcomeIssueComponent> getIssuesForCodeValidation(
			List<IValidationSupport.CodeValidationIssue> theIssues) {
		List<OperationOutcome.OperationOutcomeIssueComponent> issueComponents = new ArrayList<>();

		for (IValidationSupport.CodeValidationIssue issue : theIssues) {
			OperationOutcome.IssueSeverity severity =
					OperationOutcome.IssueSeverity.fromCode(issue.getSeverity().getCode());
			OperationOutcome.IssueType issueType =
					OperationOutcome.IssueType.fromCode(issue.getType().getCode());
			String diagnostics = issue.getDiagnostics();

			IValidationSupport.CodeValidationIssueDetails details = issue.getDetails();
			CodeableConcept codeableConcept = new CodeableConcept().setText(details.getText());
			details.getCodings().forEach(detailCoding -> codeableConcept
					.addCoding()
					.setSystem(detailCoding.getSystem())
					.setCode(detailCoding.getCode()));

			OperationOutcome.OperationOutcomeIssueComponent issueComponent =
					new OperationOutcome.OperationOutcomeIssueComponent()
							.setSeverity(severity)
							.setCode(issueType)
							.setDetails(codeableConcept)
							.setDiagnostics(diagnostics);
			issueComponent
					.addExtension()
					.setUrl("http://hl7.org/fhir/StructureDefinition/operationoutcome-message-id")
					.setValue(new StringType("Terminology_PassThrough_TX_Message"));
			issueComponents.add(issueComponent);
		}
		return issueComponents;
	}

	@Override
	public ValueSetExpansionOutcome expandVS(ValueSet source, boolean cacheOk, boolean Hierarchical) {
		IBaseResource convertedSource;
		try {
			convertedSource = myVersionCanonicalizer.valueSetFromValidatorCanonical(source);
		} catch (FHIRException e) {
			throw new InternalErrorException(Msg.code(661) + e);
		}
		IValidationSupport.ValueSetExpansionOutcome expanded =
				myValidationSupport.expandValueSet(newValidationSupportContext(), null, convertedSource);

		ValueSet convertedResult = null;
		if (expanded.getValueSet() != null) {
			try {
				convertedResult = myVersionCanonicalizer.valueSetToValidatorCanonical(expanded.getValueSet());
			} catch (FHIRException e) {
				throw new InternalErrorException(Msg.code(662) + e);
			}
		}

		String error = expanded.getError();
		TerminologyServiceErrorClass result = null;

		return new ValueSetExpansionOutcome(convertedResult, error, result, expanded.getErrorIsFromServer());
	}

	@Override
	public ValueSetExpansionOutcome expandVS(ValueSet valueSet, boolean b, boolean b1, int i) {
		return null;
	}

	@Override
	public ValueSetExpansionOutcome expandVS(
			Resource src,
			ElementDefinition.ElementDefinitionBindingComponent binding,
			boolean cacheOk,
			boolean Hierarchical) {
		ValueSet valueSet = fetchResource(ValueSet.class, binding.getValueSet(), src);
		return expandVS(valueSet, cacheOk, Hierarchical);
	}

	@Override
	public ValueSetExpansionOutcome expandVS(
			ITerminologyOperationDetails iTerminologyOperationDetails,
			ValueSet.ConceptSetComponent conceptSetComponent,
			boolean b,
			boolean b1)
			throws TerminologyServiceException {
		return null;
	}

	/*@Override
	public ValueSetExpansionOutcome expandVS(ValueSet.ConceptSetComponent inc, boolean hierarchical, boolean noInactive)
			throws TerminologyServiceException {
		throw new UnsupportedOperationException(Msg.code(664));
	}*/

	@Override
	public Locale getLocale() {
		return myFhirContext.getLocalizer().getLocale();
	}

	@Override
	public void setLocale(Locale locale) {
		// ignore
	}

	@Override
	public CodeSystem fetchCodeSystem(String system) {
		IBaseResource fetched = myValidationSupport.fetchCodeSystem(system);
		if (fetched == null) {
			return null;
		}
		try {
			return myVersionCanonicalizer.codeSystemToValidatorCanonical(fetched);
		} catch (FHIRException e) {
			throw new InternalErrorException(Msg.code(665) + e);
		}
	}

	@Override
	public CodeSystem fetchCodeSystem(String system, String verison) {
		IBaseResource fetched = myValidationSupport.fetchCodeSystem(system);
		if (fetched == null) {
			return null;
		}
		try {
			return myVersionCanonicalizer.codeSystemToValidatorCanonical(fetched);
		} catch (FHIRException e) {
			throw new InternalErrorException(Msg.code(1992) + e);
		}
	}

	@Override
	public CodeSystem fetchCodeSystem(String system, FhirPublication fhirVersion) {
		return null;
	}

	@Override
	public CodeSystem fetchCodeSystem(String system, String version, FhirPublication fhirVersion) {
		return null;
	}

	@Override
	public CodeSystem fetchSupplementedCodeSystem(String system) {
		return null;
	}

	@Override
	public CodeSystem fetchSupplementedCodeSystem(String system, String version) {
		return null;
	}

	@Override
	public CodeSystem fetchSupplementedCodeSystem(String system, FhirPublication fhirVersion) {
		return null;
	}

	@Override
	public CodeSystem fetchSupplementedCodeSystem(String system, String version, FhirPublication fhirVersion) {
		return null;
	}

	@Override
	public <T extends Resource> T fetchResourceRaw(Class<T> class_, String uri) {
		return fetchResource(class_, uri);
	}

	@Override
	public <T extends Resource> T fetchResource(Class<T> class_, String theUri) {
		if (isBlank(theUri)) {
			return null;
		}

		if (StringUtils.countMatches(theUri, "|") > 1) {
			ourLog.warn("Unrecognized profile uri: {}", theUri);
		}

		String resourceType = getResourceType(class_);
		@SuppressWarnings("unchecked")
		T retVal = (T) fetchResource(resourceType, theUri);

		return retVal;
	}

	@Override
	public Resource fetchResourceById(String type, String uri) {
		throw new UnsupportedOperationException(Msg.code(666));
	}

	@Override
	public Resource fetchResourceById(String type, String uri, FhirPublication fhirVersion) {
		return null;
	}

	@Override
	public <T extends Resource> T fetchResourceWithException(Class<T> class_, String uri) throws FHIRException {
		T retVal = fetchResource(class_, uri);
		if (retVal == null) {
			throw new FHIRException(
					Msg.code(667) + "Can not find resource of type " + class_.getSimpleName() + " with uri " + uri);
		}
		return retVal;
	}

	@Override
	public <T extends Resource> T fetchResource(Class<T> class_, String uri, String version) {
		return fetchResource(class_, uri + "|" + version);
	}

	@Override
	public <T extends Resource> T fetchResource(Class<T> class_, String uri, FhirPublication fhirVersion) {
		return null;
	}

	@Override
	public <T extends Resource> T fetchResource(
			Class<T> class_, String uri, String version, FhirPublication fhirVersion) {
		return null;
	}

	@Override
	public <T extends Resource> T fetchResource(Class<T> class_, String uri, Resource canonicalForSource) {
		return fetchResource(class_, uri);
	}

	@Override
	public <T extends Resource> List<T> fetchResourcesByType(Class<T> class_, FhirPublication fhirVersion) {
		return null;
	}

	@Override
	public <T extends Resource> T fetchResourceWithException(Class<T> class_, String uri, Resource sourceOfReference)
			throws FHIRException {
		throw new UnsupportedOperationException(Msg.code(2214));
	}

	@Override
	public List<String> getResourceNames() {
		return new ArrayList<>(myFhirContext.getResourceTypes());
	}

	@Override
	public List<String> getResourceNames(FhirPublication fhirVersion) {
		return null;
	}

	@Override
	public Set<String> getResourceNamesAsSet() {
		return myFhirContext.getResourceTypes();
	}

	@Override
	public Set<String> getResourceNamesAsSet(FhirPublication theFhirVersion) {
		return null;
	}

	@Override
	public StructureDefinition fetchTypeDefinition(String theTypeName) {
		return fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/" + theTypeName);
	}

	@Override
	public StructureDefinition fetchTypeDefinition(String theTypeName, FhirPublication theFhirVersion) {
		return null;
	}

	@Override
	public List<StructureDefinition> fetchTypeDefinitions(String theTypeName) {
		List<StructureDefinition> allStructures = new ArrayList<>(allStructureDefinitions());
		allStructures.removeIf(sd -> !sd.hasType() || !sd.getType().equals(theTypeName));
		return allStructures;
	}

	@Override
	public List<StructureDefinition> fetchTypeDefinitions(String theTypeName, FhirPublication theFhirVersion) {
		return null;
	}

	@Override
	public boolean isPrimitiveType(String theType) {
		return allPrimitiveTypes().contains(theType);
	}

	private Set<String> allPrimitiveTypes() {
		Set<String> retVal = myAllPrimitiveTypes;
		if (retVal == null) {
			// Collector may be changed to Collectors.toUnmodifiableSet() when switching to Android API level >= 33
			retVal = allStructureDefinitions().stream()
					.filter(structureDefinition ->
							structureDefinition.getKind() == StructureDefinition.StructureDefinitionKind.PRIMITIVETYPE)
					.map(StructureDefinition::getName)
					.filter(Objects::nonNull)
					.collect(collectingAndThen(toSet(), Collections::unmodifiableSet));
			myAllPrimitiveTypes = retVal;
		}

		return retVal;
	}

	@Override
	public boolean isDataType(String theType) {
		return !isPrimitiveType(theType);
	}

	@Override
	public UcumService getUcumService() {
		throw new UnsupportedOperationException(Msg.code(676));
	}

	@Override
	public void setUcumService(UcumService ucumService) {
		throw new UnsupportedOperationException(Msg.code(677));
	}

	@Override
	public String getVersion() {
		return myFhirContext.getVersion().getVersion().getFhirVersionString();
	}

	@Override
	public <T extends Resource> boolean hasResource(Class<T> class_, String uri) {
		if (isBlank(uri)) {
			return false;
		}

		String resourceType = getResourceType(class_);
		return fetchResource(resourceType, uri) != null;
	}

	@Override
	public <T extends Resource> boolean hasResource(Class<T> class_, String uri, Resource sourceOfReference) {
		return false;
	}

	@Override
	public <T extends Resource> boolean hasResource(Class<T> class_, String uri, FhirPublication fhirVersion) {
		return false;
	}

	@Override
	public boolean isNoTerminologyServer() {
		return false;
	}

	@Override
	public Set<String> getCodeSystemsUsed() {
		throw new UnsupportedOperationException(Msg.code(681));
	}

	@Override
	public IResourceValidator newValidator() {
		throw new UnsupportedOperationException(Msg.code(684));
	}

	@Override
	public Map<String, NamingSystem> getNSUrlMap() {
		throw new UnsupportedOperationException(Msg.code(2265));
	}

	@Override
	public org.hl7.fhir.r5.context.ILoggingService getLogger() {
		return null;
	}

	@Override
	public void setLogger(@Nonnull org.hl7.fhir.r5.context.ILoggingService logger) {
		throw new UnsupportedOperationException(Msg.code(687));
	}

	@Override
	public boolean supportsSystem(String system) {
		return myValidationSupport.isCodeSystemSupported(newValidationSupportContext(), system);
	}

	@Override
	public boolean supportsSystem(String system, FhirPublication fhirVersion) throws TerminologyServiceException {
		return supportsSystem(system);
	}

	@Override
	public ValueSetExpansionOutcome expandVS(
			ValueSet source, boolean cacheOk, boolean heiarchical, boolean incompleteOk) {
		return null;
	}

	@Override
	public ValueSetExpansionOutcome expandVS(String s, boolean b, boolean b1, int i) {
		return null;
	}

	@Override
	public ValidationResult validateCode(
			ValidationOptions theOptions, String system, String version, String code, String display) {
		ConceptValidationOptions validationOptions = convertConceptValidationOptions(theOptions);
		return doValidation(null, validationOptions, system, code, display);
	}

	@Override
	public ValidationResult validateCode(
			ValidationOptions theOptions,
			String theSystem,
			String version,
			String theCode,
			String display,
			ValueSet theValueSet) {

		ConceptValidationOptions validationOptions = convertConceptValidationOptions(theOptions);
		return doValidation(theValueSet, validationOptions, theSystem, theCode, display);
	}

	@Override
	public ValidationResult validateCode(ValidationOptions theOptions, String code, ValueSet theValueSet) {
		String system = ValidationSupportUtils.extractCodeSystemForCode(theValueSet, code);

		ConceptValidationOptions validationOptions =
				convertConceptValidationOptions(theOptions).setInferSystem(true);

		return doValidation(theValueSet, validationOptions, system, code, null);
	}

	@Override
	public ValidationResult validateCode(ValidationOptions theOptions, Coding theCoding, ValueSet theValueSet) {
		ConceptValidationOptions validationOptions = convertConceptValidationOptions(theOptions);
		String system = theCoding.getSystem();
		String code = theCoding.getCode();
		String display = theCoding.getDisplay();

		return doValidation(theValueSet, validationOptions, system, code, display);
	}

	@Override
	public ValidationResult validateCode(
			ValidationOptions options, Coding code, ValueSet vs, ValidationContextCarrier ctxt) {
		return validateCode(options, code, vs);
	}

	@Override
	public void validateCodeBatch(
			ValidationOptions options, List<? extends CodingValidationRequest> codes, ValueSet vs) {
		for (CodingValidationRequest next : codes) {
			ValidationResult outcome = validateCode(options, next.getCoding(), vs);
			next.setResult(outcome);
		}
	}

	@Override
	public void validateCodeBatchByRef(
			ValidationOptions validationOptions, List<? extends CodingValidationRequest> list, String s) {
		ValueSet valueSet = fetchResource(ValueSet.class, s);
		validateCodeBatch(validationOptions, list, valueSet);
	}

	@Nonnull
	private ValidationResult doValidation(
			@Nullable ValueSet theValueSet,
			ConceptValidationOptions theValidationOptions,
			String theSystem,
			String theCode,
			String theDisplay) {

		IBaseResource convertedVs = getOrConvertValueSet(theValueSet);

		IValidationSupport.CodeValidationResult result;
		if (convertedVs != null) {
			result = validateCodeInValueSet(convertedVs, theValidationOptions, theSystem, theCode, theDisplay);
		} else {
			result = validateCodeInCodeSystem(theValidationOptions, theSystem, theCode, theDisplay);
		}
		return convertValidationResult(theSystem, result);
	}

	private IBaseResource getOrConvertValueSet(ValueSet theValueSet) {
		if (theValueSet == null) {
			return null;
		}

		IBaseResource convertedValueSet = (IBaseResource) theValueSet.getUserData(FROM_CANONICAL_USERDATA_KEY);
		if (convertedValueSet != null) {
			final IBaseResource finalConvertedValueSet = convertedValueSet;
			ourLog.atDebug()
					.setMessage("Using user data - Key: {}, ValueSet: {}")
					.addArgument(FROM_CANONICAL_USERDATA_KEY)
					.addArgument(() -> toResourceId(finalConvertedValueSet))
					.log();
			return convertedValueSet;
		}
		convertedValueSet = myVersionCanonicalizer.valueSetFromValidatorCanonical(theValueSet);
		cacheConvertedValueSet(theValueSet, convertedValueSet);

		return convertedValueSet;
	}

	private void cacheConvertedValueSet(ValueSet theValueSet, IBaseResource theConvertedValueSet) {
		if (theValueSet == null || theConvertedValueSet == null) {
			return;
		}
		theValueSet.setUserData(FROM_CANONICAL_USERDATA_KEY, theConvertedValueSet);
		ourLog.atDebug()
				.setMessage("Added user data - Key: {}, ValueSet: {}")
				.addArgument(FROM_CANONICAL_USERDATA_KEY)
				.addArgument(() -> toResourceId(theConvertedValueSet))
				.log();
	}

	private @Nullable String toResourceId(IBaseResource theResource) {
		if (theResource == null) {
			return "unknown";
		} else if (theResource.getIdElement() == null) {
			return theResource.fhirType() + "/";
		}
		return theResource.getIdElement().getIdPart();
	}

	private IValidationSupport.CodeValidationResult validateCodeInValueSet(
			IBaseResource theValueSet,
			ConceptValidationOptions theValidationOptions,
			String theSystem,
			String theCode,
			String theDisplay) {
		IValidationSupport.CodeValidationResult result = myValidationSupport.validateCodeInValueSet(
				newValidationSupportContext(), theValidationOptions, theSystem, theCode, theDisplay, theValueSet);
		if (result != null && isNotBlank(theSystem)) {
			/* We got a value set result, which could be successful, or could contain errors/warnings. The code
			might also be invalid in the code system, so we will check that as well and add those issues
			to our result.
			*/
			IValidationSupport.CodeValidationResult codeSystemResult =
					validateCodeInCodeSystem(theValidationOptions, theSystem, theCode, theDisplay);
			final boolean valueSetResultContainsInvalidDisplay = result.getIssues().stream()
					.anyMatch(WorkerContextValidationSupportAdapter::hasInvalidDisplayDetailCode);
			if (codeSystemResult != null) {
				for (IValidationSupport.CodeValidationIssue codeValidationIssue : codeSystemResult.getIssues()) {
					/* Value set validation should already have checked the display name. If we get INVALID_DISPLAY
					issues from code system validation, they will only repeat what was already caught.
					*/
					if (!hasInvalidDisplayDetailCode(codeValidationIssue) || !valueSetResultContainsInvalidDisplay) {
						result.addIssue(codeValidationIssue);
					}
				}
			}
		}
		return result;
	}

	@Nonnull
	private ValidationSupportContext newValidationSupportContext() {
		return new ValidationSupportContext(myValidationSupport);
	}

	private IValidationSupport.CodeValidationResult validateCodeInCodeSystem(
			ConceptValidationOptions theValidationOptions, String theSystem, String theCode, String theDisplay) {
		return myValidationSupport.validateCode(
				newValidationSupportContext(), theValidationOptions, theSystem, theCode, theDisplay, null);
	}

	@Override
	public ValidationResult validateCode(ValidationOptions theOptions, CodeableConcept code, ValueSet theVs) {

		List<ValidationResult> validationResultsOk = new ArrayList<>();
		List<OperationOutcome.OperationOutcomeIssueComponent> issues = new ArrayList<>();
		for (Coding next : code.getCoding()) {
			if (!next.hasSystem()) {
				String message =
						"Coding has no system. A code with no system has no defined meaning, and it cannot be validated. A system should be provided";
				OperationOutcome.OperationOutcomeIssueComponent issue =
						new OperationOutcome.OperationOutcomeIssueComponent()
								.setSeverity(OperationOutcome.IssueSeverity.WARNING)
								.setCode(OperationOutcome.IssueType.NOTFOUND)
								.setDiagnostics(message)
								.setDetails(new CodeableConcept().setText(message));

				issues.add(issue);
			}
			ValidationResult retVal = validateCode(theOptions, next, theVs);
			if (retVal.isOk()) {
				validationResultsOk.add(retVal);
			} else {
				issues.addAll(retVal.getIssues());
			}
		}

		if (!code.getCoding().isEmpty()) {
			if (!myValidationSupport.isCodeableConceptValidationSuccessfulIfNotAllCodingsAreValid()) {
				if (validationResultsOk.size() == code.getCoding().size()) {
					return validationResultsOk.get(0);
				}
			} else {
				if (!validationResultsOk.isEmpty()) {
					return validationResultsOk.get(0);
				}
			}
		}

		return new ValidationResult(ValidationMessage.IssueSeverity.ERROR, null, issues);
	}

	public void invalidateCaches() {
		// nothing for now
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Resource> List<T> fetchResourcesByType(Class<T> theClass) {
		if (theClass.equals(StructureDefinition.class)) {
			return (List<T>) allStructureDefinitions();
		}
		throw new UnsupportedOperationException(Msg.code(650) + "Unable to fetch resources of type: " + theClass);
	}

	@Override
	public <T extends Resource> List<T> fetchResourcesByUrl(Class<T> class_, String url) {
		throw new UnsupportedOperationException(Msg.code(2509) + "Can't fetch all resources of url: " + url);
	}

	@Override
	public boolean isForPublication() {
		return false;
	}

	@Override
	public void setForPublication(boolean b) {
		throw new UnsupportedOperationException(Msg.code(2351));
	}

	@Override
	public OIDSummary urlsForOid(String oid, String resourceType) {
		return null;
	}

	@Override
	public <T extends Resource> T findTxResource(Class<T> class_, String canonical, Resource sourceOfReference) {
		if (canonical == null) {
			return null;
		}
		return fetchResource(class_, canonical, sourceOfReference);
	}

	@Override
	public <T extends Resource> T findTxResource(Class<T> class_, String canonical) {
		if (canonical == null) {
			return null;
		}

		return fetchResource(class_, canonical);
	}

	@Override
	public <T extends Resource> T findTxResource(Class<T> class_, String canonical, String version) {
		if (canonical == null) {
			return null;
		}

		return fetchResource(class_, canonical, version);
	}

	@Override
	public Boolean subsumes(ValidationOptions optionsArg, Coding parent, Coding child) {
		throw new UnsupportedOperationException(Msg.code(2489));
	}

	@Override
	public boolean isServerSideSystem(String url) {
		return false;
	}

	private IBaseResource fetchResource(String theResourceType, String theUrl) {
		String fetchResourceName = theResourceType;
		if (myFhirContext.getVersion().getVersion() == FhirVersionEnum.DSTU2) {
			if ("CodeSystem".equals(fetchResourceName)) {
				fetchResourceName = "ValueSet";
			}
		}

		Class<? extends IBaseResource> fetchResourceType;
		if (fetchResourceName.equals("Resource")) {
			fetchResourceType = null;
		} else {
			fetchResourceType =
					myFhirContext.getResourceDefinition(fetchResourceName).getImplementingClass();
		}

		IBaseResource fetched = myValidationSupport.fetchResource(fetchResourceType, theUrl);

		if (fetched == null) {
			return null;
		}

		return convertToCanonicalVersionAndGenerateSnapshot(fetched, true);
	}

	@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
	private Resource convertToCanonicalVersionAndGenerateSnapshot(
			@Nonnull IBaseResource theResource, boolean thePropagateSnapshotException) {
		Resource canonical;
		synchronized (theResource) {
			canonical = (Resource) theResource.getUserData(TO_CANONICAL_USERDATA_KEY);
			if (canonical == null) {
				boolean storeCanonical = true;
				canonical = myVersionCanonicalizer.resourceToValidatorCanonical(theResource);

				if (canonical instanceof StructureDefinition canonicalSd) {
					if (canonicalSd.getSnapshot().isEmpty()) {
						IBaseResource resource = theResource;
						try {

							SnapshotGeneratingValidationSupport snapshotGenerator =
									new SnapshotGeneratingValidationSupport(myFhirContext, this);
							resource = snapshotGenerator.generateSnapshot(
									newValidationSupportContext(), resource, "", null, "");
							Validate.isTrue(
									resource != null,
									"StructureDefinition %s has no snapshot, and no snapshot generator is configured",
									canonicalSd.getUrl());

						} catch (BaseServerResponseException e) {
							if (thePropagateSnapshotException) {
								throw e;
							}
							String message = e.toString();
							Throwable rootCause = ExceptionUtils.getRootCause(e);
							if (rootCause != null) {
								message = rootCause.getMessage();
							}
							ourLog.warn(
									"Failed to generate snapshot for profile with URL[{}]: {}",
									canonicalSd.getUrl(),
									message);
							storeCanonical = false;
						}

						canonical = myVersionCanonicalizer.resourceToValidatorCanonical(resource);
					}
				}

				String sourcePackageId =
						(String) theResource.getUserData(DefaultProfileValidationSupport.SOURCE_PACKAGE_ID);
				if (sourcePackageId != null) {
					canonical.setSourcePackage(new PackageInformation(sourcePackageId, null, null, new Date()));
				}

				if (storeCanonical) {
					theResource.setUserData(TO_CANONICAL_USERDATA_KEY, canonical);
				}
			}
		}
		return canonical;
	}

	private FHIRPathEngine getFHIRPathEngine() {
		FHIRPathEngine retVal = myFHIRPathEngine;
		if (retVal == null) {
			retVal = new FHIRPathEngine(this);
			myFHIRPathEngine = retVal;
		}
		return retVal;
	}

	private static <T extends Resource> String getResourceType(Class<T> theClass) {
		if (theClass.getSimpleName().equals("Resource")) {
			return "Resource";
		}
		return FHIR_CONTEXT_R5.getResourceType(theClass);
	}

	private static boolean hasInvalidDisplayDetailCode(IValidationSupport.CodeValidationIssue theIssue) {
		return theIssue.hasIssueDetailCode(INVALID_DISPLAY.getCode());
	}

	public static ConceptValidationOptions convertConceptValidationOptions(ValidationOptions theOptions) {
		ConceptValidationOptions retVal = new ConceptValidationOptions();
		if (theOptions.isGuessSystem()) {
			retVal = retVal.setInferSystem(true);
		}
		return retVal;
	}

	@Nonnull
	public static WorkerContextValidationSupportAdapter newVersionSpecificWorkerContextWrapper(
			IValidationSupport theValidationSupport) {
		return new WorkerContextValidationSupportAdapter(theValidationSupport);
	}
}
