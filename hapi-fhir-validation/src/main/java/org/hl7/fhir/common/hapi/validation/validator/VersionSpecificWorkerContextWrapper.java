package org.hl7.fhir.common.hapi.validation.validator;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.sl.cache.CacheFactory;
import ca.uhn.fhir.sl.cache.LoadingCache;
import ca.uhn.fhir.system.HapiSystemProperties;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.fhir.ucum.UcumService;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportUtils;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.TerminologyServiceException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.context.IContextResourceLoader;
import org.hl7.fhir.r5.context.IWorkerContext;
import org.hl7.fhir.r5.context.IWorkerContextManager;
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
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class VersionSpecificWorkerContextWrapper extends I18nBase implements IWorkerContext {
	private static final Logger ourLog = LoggerFactory.getLogger(VersionSpecificWorkerContextWrapper.class);
	private final ValidationSupportContext myValidationSupportContext;
	private final VersionCanonicalizer myVersionCanonicalizer;
	private final LoadingCache<ResourceKey, IBaseResource> myFetchResourceCache;
	private volatile List<StructureDefinition> myAllStructures;
	private volatile Set<String> myAllPrimitiveTypes;
	private Parameters myExpansionProfile;

	public VersionSpecificWorkerContextWrapper(
			ValidationSupportContext theValidationSupportContext, VersionCanonicalizer theVersionCanonicalizer) {
		myValidationSupportContext = theValidationSupportContext;
		myVersionCanonicalizer = theVersionCanonicalizer;

		long timeoutMillis = HapiSystemProperties.getTestValidationResourceCachesMs();

		myFetchResourceCache = CacheFactory.build(timeoutMillis, 10000, key -> {
			String fetchResourceName = key.getResourceName();
			if (myValidationSupportContext
							.getRootValidationSupport()
							.getFhirContext()
							.getVersion()
							.getVersion()
					== FhirVersionEnum.DSTU2) {
				if ("CodeSystem".equals(fetchResourceName)) {
					fetchResourceName = "ValueSet";
				}
			}

			Class<? extends IBaseResource> fetchResourceType;
			if (fetchResourceName.equals("Resource")) {
				fetchResourceType = null;
			} else {
				fetchResourceType = myValidationSupportContext
						.getRootValidationSupport()
						.getFhirContext()
						.getResourceDefinition(fetchResourceName)
						.getImplementingClass();
			}

			IBaseResource fetched = myValidationSupportContext
					.getRootValidationSupport()
					.fetchResource(fetchResourceType, key.getUri());

			Resource canonical = myVersionCanonicalizer.resourceToValidatorCanonical(fetched);

			if (canonical instanceof StructureDefinition) {
				StructureDefinition canonicalSd = (StructureDefinition) canonical;
				if (canonicalSd.getSnapshot().isEmpty()) {
					ourLog.info("Generating snapshot for StructureDefinition: {}", canonicalSd.getUrl());
					fetched = myValidationSupportContext
							.getRootValidationSupport()
							.generateSnapshot(theValidationSupportContext, fetched, "", null, "");
					Validate.isTrue(
							fetched != null,
							"StructureDefinition %s has no snapshot, and no snapshot generator is configured",
							key.getUri());
					canonical = myVersionCanonicalizer.resourceToValidatorCanonical(fetched);
				}
			}

			return canonical;
		});

		setValidationMessageLanguage(getLocale());
	}

	@Override
	public Set<String> getBinaryKeysAsSet() {
		throw new UnsupportedOperationException(Msg.code(2118));
	}

	@Override
	public boolean hasBinaryKey(String s) {
		return myValidationSupportContext.getRootValidationSupport().fetchBinary(s) != null;
	}

	@Override
	public byte[] getBinaryForKey(String s) {
		return myValidationSupportContext.getRootValidationSupport().fetchBinary(s);
	}

	@Override
	public int loadFromPackage(NpmPackage pi, IContextResourceLoader loader) throws FHIRException {
		throw new UnsupportedOperationException(Msg.code(652));
	}

	@Override
	public int loadFromPackage(NpmPackage pi, IContextResourceLoader loader, List<String> types)
			throws FileNotFoundException, IOException, FHIRException {
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
		return myExpansionProfile;
	}

	@Override
	public void setExpansionParameters(Parameters expParameters) {
		setExpansionProfile(expParameters);
	}

	public void setExpansionProfile(Parameters expParameters) {
		myExpansionProfile = expParameters;
	}

	private List<StructureDefinition> allStructures() {

		List<StructureDefinition> retVal = myAllStructures;
		if (retVal == null) {
			retVal = new ArrayList<>();
			for (IBaseResource next :
					myValidationSupportContext.getRootValidationSupport().fetchAllStructureDefinitions()) {
				try {
					StructureDefinition converted = myVersionCanonicalizer.structureDefinitionToCanonical(next);
					retVal.add(converted);
				} catch (FHIRException e) {
					throw new InternalErrorException(Msg.code(659) + e);
				}
			}
			myAllStructures = retVal;
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
					getIssuesForCodeValidation(theResult.getCodeValidationIssues()));
		}

		if (retVal == null) {
			retVal = new ValidationResult(ValidationMessage.IssueSeverity.ERROR, "Validation failed", null);
		}

		return retVal;
	}

	private List<OperationOutcome.OperationOutcomeIssueComponent> getIssuesForCodeValidation(
			List<IValidationSupport.CodeValidationIssue> codeValidationIssues) {
		List<OperationOutcome.OperationOutcomeIssueComponent> issues = new ArrayList<>();

		for (IValidationSupport.CodeValidationIssue codeValidationIssue : codeValidationIssues) {

			CodeableConcept codeableConcept = new CodeableConcept().setText(codeValidationIssue.getMessage());
			codeableConcept.addCoding(
					"http://hl7.org/fhir/tools/CodeSystem/tx-issue-type",
					getIssueCodingFromCodeValidationIssue(codeValidationIssue),
					null);

			OperationOutcome.OperationOutcomeIssueComponent issue =
					new OperationOutcome.OperationOutcomeIssueComponent()
							.setSeverity(getIssueSeverityFromCodeValidationIssue(codeValidationIssue))
							.setCode(getIssueTypeFromCodeValidationIssue(codeValidationIssue))
							.setDetails(codeableConcept);
			issue.getDetails().setText(codeValidationIssue.getMessage());
			issue.addExtension()
					.setUrl("http://hl7.org/fhir/StructureDefinition/operationoutcome-message-id")
					.setValue(new StringType("Terminology_PassThrough_TX_Message"));
			issues.add(issue);
		}
		return issues;
	}

	private String getIssueCodingFromCodeValidationIssue(IValidationSupport.CodeValidationIssue codeValidationIssue) {
		switch (codeValidationIssue.getCoding()) {
			case VS_INVALID:
				return "vs-invalid";
			case NOT_FOUND:
				return "not-found";
			case NOT_IN_VS:
				return "not-in-vs";
			case INVALID_CODE:
				return "invalid-code";
			case INVALID_DISPLAY:
				return "invalid-display";
		}
		return null;
	}

	private OperationOutcome.IssueType getIssueTypeFromCodeValidationIssue(
			IValidationSupport.CodeValidationIssue codeValidationIssue) {
		switch (codeValidationIssue.getCode()) {
			case NOT_FOUND:
				return OperationOutcome.IssueType.NOTFOUND;
			case CODE_INVALID:
				return OperationOutcome.IssueType.CODEINVALID;
			case INVALID:
				return OperationOutcome.IssueType.INVALID;
		}
		return null;
	}

	private OperationOutcome.IssueSeverity getIssueSeverityFromCodeValidationIssue(
			IValidationSupport.CodeValidationIssue codeValidationIssue) {
		switch (codeValidationIssue.getSeverity()) {
			case FATAL:
				return OperationOutcome.IssueSeverity.FATAL;
			case ERROR:
				return OperationOutcome.IssueSeverity.ERROR;
			case WARNING:
				return OperationOutcome.IssueSeverity.WARNING;
			case INFORMATION:
				return OperationOutcome.IssueSeverity.INFORMATION;
		}
		return null;
	}

	@Override
	public ValueSetExpansionOutcome expandVS(ValueSet source, boolean cacheOk, boolean Hierarchical) {
		IBaseResource convertedSource;
		try {
			convertedSource = myVersionCanonicalizer.valueSetFromValidatorCanonical(source);
		} catch (FHIRException e) {
			throw new InternalErrorException(Msg.code(661) + e);
		}
		IValidationSupport.ValueSetExpansionOutcome expanded = myValidationSupportContext
				.getRootValidationSupport()
				.expandValueSet(myValidationSupportContext, null, convertedSource);

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
	public ValueSetExpansionOutcome expandVS(
			Resource src,
			ElementDefinition.ElementDefinitionBindingComponent binding,
			boolean cacheOk,
			boolean Hierarchical) {
		ValueSet valueSet = fetchResource(ValueSet.class, binding.getValueSet(), src);
		return expandVS(valueSet, cacheOk, Hierarchical);
	}

	@Override
	public ValueSetExpansionOutcome expandVS(ValueSet.ConceptSetComponent inc, boolean hierarchical, boolean noInactive)
			throws TerminologyServiceException {
		throw new UnsupportedOperationException(Msg.code(664));
	}

	@Override
	public Locale getLocale() {
		return myValidationSupportContext
				.getRootValidationSupport()
				.getFhirContext()
				.getLocalizer()
				.getLocale();
	}

	@Override
	public void setLocale(Locale locale) {
		// ignore
	}

	@Override
	public CodeSystem fetchCodeSystem(String system) {
		IBaseResource fetched =
				myValidationSupportContext.getRootValidationSupport().fetchCodeSystem(system);
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
		IBaseResource fetched =
				myValidationSupportContext.getRootValidationSupport().fetchCodeSystem(system);
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

		String uri = theUri;
		// handle profile version, if present
		if (theUri.contains("|")) {
			String[] parts = theUri.split("\\|");
			if (parts.length == 2) {
				uri = parts[0];
			} else {
				ourLog.warn("Unrecognized profile uri: {}", theUri);
			}
		}

		ResourceKey key = new ResourceKey(class_.getSimpleName(), uri);
		@SuppressWarnings("unchecked")
		T retVal = (T) myFetchResourceCache.get(key);

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
		return new ArrayList<>(myValidationSupportContext
				.getRootValidationSupport()
				.getFhirContext()
				.getResourceTypes());
	}

	@Override
	public List<String> getResourceNames(FhirPublication fhirVersion) {
		return null;
	}

	@Override
	public Set<String> getResourceNamesAsSet() {
		return myValidationSupportContext
				.getRootValidationSupport()
				.getFhirContext()
				.getResourceTypes();
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
		List<StructureDefinition> allStructures = new ArrayList<>(allStructures());
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
			retVal = allStructures().stream()
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
		return myValidationSupportContext
				.getRootValidationSupport()
				.getFhirContext()
				.getVersion()
				.getVersion()
				.getFhirVersionString();
	}

	@Override
	public <T extends Resource> boolean hasResource(Class<T> class_, String uri) {
		if (isBlank(uri)) {
			return false;
		}

		ResourceKey key = new ResourceKey(class_.getSimpleName(), uri);
		return myFetchResourceCache.get(key) != null;
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
	public void setLogger(org.hl7.fhir.r5.context.ILoggingService logger) {
		throw new UnsupportedOperationException(Msg.code(687));
	}

	@Override
	public boolean supportsSystem(String system) {
		return myValidationSupportContext
				.getRootValidationSupport()
				.isCodeSystemSupported(myValidationSupportContext, system);
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
		IBaseResource convertedVs = null;

		try {
			if (theValueSet != null) {
				convertedVs = myVersionCanonicalizer.valueSetFromValidatorCanonical(theValueSet);
			}
		} catch (FHIRException e) {
			throw new InternalErrorException(Msg.code(689) + e);
		}

		ConceptValidationOptions validationOptions = convertConceptValidationOptions(theOptions);

		return doValidation(convertedVs, validationOptions, theSystem, theCode, display);
	}

	@Override
	public ValidationResult validateCode(ValidationOptions theOptions, String code, ValueSet theValueSet) {
		IBaseResource convertedVs = null;
		try {
			if (theValueSet != null) {
				convertedVs = myVersionCanonicalizer.valueSetFromValidatorCanonical(theValueSet);
			}
		} catch (FHIRException e) {
			throw new InternalErrorException(Msg.code(690) + e);
		}

		String system = ValidationSupportUtils.extractCodeSystemForCode(theValueSet, code);

		ConceptValidationOptions validationOptions =
				convertConceptValidationOptions(theOptions).setInferSystem(true);

		return doValidation(convertedVs, validationOptions, system, code, null);
	}

	@Override
	public ValidationResult validateCode(ValidationOptions theOptions, Coding theCoding, ValueSet theValueSet) {
		IBaseResource convertedVs = null;

		try {
			if (theValueSet != null) {
				convertedVs = myVersionCanonicalizer.valueSetFromValidatorCanonical(theValueSet);
			}
		} catch (FHIRException e) {
			throw new InternalErrorException(Msg.code(691) + e);
		}

		ConceptValidationOptions validationOptions = convertConceptValidationOptions(theOptions);
		String system = theCoding.getSystem();
		String code = theCoding.getCode();
		String display = theCoding.getDisplay();

		return doValidation(convertedVs, validationOptions, system, code, display);
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
			IBaseResource theValueSet,
			ConceptValidationOptions theValidationOptions,
			String theSystem,
			String theCode,
			String theDisplay) {
		IValidationSupport.CodeValidationResult result;
		if (theValueSet != null) {
			result = validateCodeInValueSet(theValueSet, theValidationOptions, theSystem, theCode, theDisplay);
		} else {
			result = validateCodeInCodeSystem(theValidationOptions, theSystem, theCode, theDisplay);
		}
		return convertValidationResult(theSystem, result);
	}

	private IValidationSupport.CodeValidationResult validateCodeInValueSet(
			IBaseResource theValueSet,
			ConceptValidationOptions theValidationOptions,
			String theSystem,
			String theCode,
			String theDisplay) {
		IValidationSupport.CodeValidationResult result = myValidationSupportContext
				.getRootValidationSupport()
				.validateCodeInValueSet(
						myValidationSupportContext, theValidationOptions, theSystem, theCode, theDisplay, theValueSet);
		if (result != null) {
			/* We got a value set result, which could be successful, or could contain errors/warnings. The code
			might also be invalid in the code system, so we will check that as well and add those issues
			to our result.
			*/
			IValidationSupport.CodeValidationResult codeSystemResult =
					validateCodeInCodeSystem(theValidationOptions, theSystem, theCode, theDisplay);
			final boolean valueSetResultContainsInvalidDisplay = result.getCodeValidationIssues().stream()
					.anyMatch(codeValidationIssue -> codeValidationIssue.getCoding()
							== IValidationSupport.CodeValidationIssueCoding.INVALID_DISPLAY);
			if (codeSystemResult != null) {
				for (IValidationSupport.CodeValidationIssue codeValidationIssue :
						codeSystemResult.getCodeValidationIssues()) {
					/* Value set validation should already have checked the display name. If we get INVALID_DISPLAY
					issues from code system validation, they will only repeat what was already caught.
					*/
					if (codeValidationIssue.getCoding() != IValidationSupport.CodeValidationIssueCoding.INVALID_DISPLAY
							|| !valueSetResultContainsInvalidDisplay) {
						result.addCodeValidationIssue(codeValidationIssue);
					}
				}
			}
		}
		return result;
	}

	private IValidationSupport.CodeValidationResult validateCodeInCodeSystem(
			ConceptValidationOptions theValidationOptions, String theSystem, String theCode, String theDisplay) {
		return myValidationSupportContext
				.getRootValidationSupport()
				.validateCode(myValidationSupportContext, theValidationOptions, theSystem, theCode, theDisplay, null);
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
				for (OperationOutcome.OperationOutcomeIssueComponent issue : retVal.getIssues()) {
					issues.add(issue);
				}
			}
		}

		if (code.getCoding().size() > 0) {
			if (!myValidationSupportContext.isEnabledValidationForCodingsLogicalAnd()) {
				if (validationResultsOk.size() == code.getCoding().size()) {
					return validationResultsOk.get(0);
				}
			} else {
				if (validationResultsOk.size() > 0) {
					return validationResultsOk.get(0);
				}
			}
		}

		return new ValidationResult(ValidationMessage.IssueSeverity.ERROR, null, issues);
	}

	private static OperationOutcome.OperationOutcomeIssueComponent getOperationOutcomeTxIssueComponent(
			String message, OperationOutcome.IssueType issueCode, String txIssueTypeCode) {
		OperationOutcome.OperationOutcomeIssueComponent issue = new OperationOutcome.OperationOutcomeIssueComponent()
				.setSeverity(OperationOutcome.IssueSeverity.ERROR)
				.setDiagnostics(message);
		issue.getDetails().setText(message);

		issue.setCode(issueCode);
		issue.getDetails().addCoding("http://hl7.org/fhir/tools/CodeSystem/tx-issue-type", txIssueTypeCode, null);
		return issue;
	}

	public void invalidateCaches() {
		myFetchResourceCache.invalidateAll();
	}

	@Override
	public <T extends Resource> List<T> fetchResourcesByType(Class<T> theClass) {
		if (theClass.equals(StructureDefinition.class)) {
			return (List<T>) allStructures();
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

	public static ConceptValidationOptions convertConceptValidationOptions(ValidationOptions theOptions) {
		ConceptValidationOptions retVal = new ConceptValidationOptions();
		if (theOptions.isGuessSystem()) {
			retVal = retVal.setInferSystem(true);
		}
		return retVal;
	}

	@Nonnull
	public static VersionSpecificWorkerContextWrapper newVersionSpecificWorkerContextWrapper(
			IValidationSupport theValidationSupport) {
		VersionCanonicalizer versionCanonicalizer = new VersionCanonicalizer(theValidationSupport.getFhirContext());
		return new VersionSpecificWorkerContextWrapper(
				new ValidationSupportContext(theValidationSupport), versionCanonicalizer);
	}

	private static class ResourceKey {
		private final int myHashCode;
		private final String myResourceName;
		private final String myUri;

		private ResourceKey(String theResourceName, String theUri) {
			myResourceName = theResourceName;
			myUri = theUri;
			myHashCode = new HashCodeBuilder(17, 37)
					.append(myResourceName)
					.append(myUri)
					.toHashCode();
		}

		@Override
		public boolean equals(Object theO) {
			if (this == theO) {
				return true;
			}

			if (theO == null || getClass() != theO.getClass()) {
				return false;
			}

			ResourceKey that = (ResourceKey) theO;

			return new EqualsBuilder()
					.append(myResourceName, that.myResourceName)
					.append(myUri, that.myUri)
					.isEquals();
		}

		public String getResourceName() {
			return myResourceName;
		}

		public String getUri() {
			return myUri;
		}

		@Override
		public int hashCode() {
			return myHashCode;
		}
	}

	@Override
	public Boolean subsumes(ValidationOptions optionsArg, Coding parent, Coding child) {
		throw new UnsupportedOperationException(Msg.code(2489));
	}

	@Override
	public boolean isServerSideSystem(String url) {
		return false;
	}
}
