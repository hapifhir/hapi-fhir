package org.hl7.fhir.r4.hapi.ctx;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.util.CoverageIgnore;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;
import org.fhir.ucum.UcumService;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.TerminologyServiceException;
import org.hl7.fhir.r4.context.IWorkerContext;
import org.hl7.fhir.r4.formats.IParser;
import org.hl7.fhir.r4.formats.ParserType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ElementDefinition.ElementDefinitionBindingComponent;
import org.hl7.fhir.r4.model.MetadataResource;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ResourceType;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.StructureMap;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.r4.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.r4.terminologies.ValueSetExpander;
import org.hl7.fhir.r4.utils.validation.IResourceValidator;
import org.hl7.fhir.utilities.TranslationServices;
import org.hl7.fhir.utilities.i18n.I18nBase;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueSeverity;
import org.hl7.fhir.utilities.validation.ValidationOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public final class HapiWorkerContext extends I18nBase implements IWorkerContext {
  private final FhirContext myCtx;
  private final Cache<String, Resource> myFetchedResourceCache;
  private IValidationSupport myValidationSupport;
  private Parameters myExpansionProfile;
  private String myOverrideVersionNs;

  public HapiWorkerContext(FhirContext theCtx, IValidationSupport theValidationSupport) {
    Validate.notNull(theCtx, "theCtx must not be null");
    Validate.notNull(theValidationSupport, "theValidationSupport must not be null");
    myCtx = theCtx;
    myValidationSupport = theValidationSupport;

    long timeoutMillis = 10 * DateUtils.MILLIS_PER_SECOND;
    if (System.getProperties().containsKey(ca.uhn.fhir.rest.api.Constants.TEST_SYSTEM_PROP_VALIDATION_RESOURCE_CACHES_MS)) {
      timeoutMillis = Long.parseLong(System.getProperty(Constants.TEST_SYSTEM_PROP_VALIDATION_RESOURCE_CACHES_MS));
    }

    myFetchedResourceCache = Caffeine.newBuilder().expireAfterWrite(timeoutMillis, TimeUnit.MILLISECONDS).build();

    // Set a default locale
    setValidationMessageLanguage(getLocale());
  }

  @Override
  public List<StructureDefinition> allStructures() {
    return myValidationSupport.fetchAllStructureDefinitions();
  }

  @Override
  public List<StructureDefinition> getStructures() {
    return allStructures();
  }

  @Override
  public CodeSystem fetchCodeSystem(String theSystem) {
    if (myValidationSupport == null) {
      return null;
    } else {
      return (CodeSystem) myValidationSupport.fetchCodeSystem(theSystem);
    }
  }

  @Override
  public List<ConceptMap> findMapsForSource(String theUrl) {
    throw new UnsupportedOperationException(Msg.code(258));
  }

  @Override
  public String getAbbreviation(String theName) {
    throw new UnsupportedOperationException(Msg.code(259));
  }

  @Override
  public org.hl7.fhir.r4.utils.INarrativeGenerator getNarrativeGenerator(String thePrefix, String theBasePath) {
    throw new UnsupportedOperationException(Msg.code(260));
  }

  @Override
  public IParser getParser(ParserType theType) {
    throw new UnsupportedOperationException(Msg.code(261));
  }

  @Override
  public IParser getParser(String theType) {
    throw new UnsupportedOperationException(Msg.code(262));
  }

  @Override
  public List<String> getResourceNames() {
    List<String> result = new ArrayList<>();
    for (ResourceType next : ResourceType.values()) {
      result.add(next.name());
    }
    Collections.sort(result);
    return result;
  }

  @Override
  public IParser newJsonParser() {
    throw new UnsupportedOperationException(Msg.code(263));
  }

  @Override
  public IResourceValidator newValidator() {
    throw new UnsupportedOperationException(Msg.code(264));
  }

  @Override
  public IParser newXmlParser() {
    throw new UnsupportedOperationException(Msg.code(265));
  }

  @Override
  public String oid2Uri(String theCode) {
    throw new UnsupportedOperationException(Msg.code(266));
  }

  @Override
  public boolean supportsSystem(String theSystem) {
    if (myValidationSupport == null) {
      return false;
    } else {
      return myValidationSupport.isCodeSystemSupported(new ValidationSupportContext(myValidationSupport), theSystem);
    }
  }

  @Override
  public Set<String> typeTails() {
    return new HashSet<>(Arrays.asList("Integer", "UnsignedInt", "PositiveInt", "Decimal", "DateTime", "Date", "Time", "Instant", "String", "Uri", "Oid", "Uuid", "Id", "Boolean", "Code",
      "Markdown", "Base64Binary", "Coding", "CodeableConcept", "Attachment", "Identifier", "Quantity", "SampledData", "Range", "Period", "Ratio", "HumanName", "Address", "ContactPoint",
      "Timing", "Reference", "Annotation", "Signature", "Meta"));
  }

  @Override
  public ValidationResult validateCode(ValidationOptions theOptions, CodeableConcept theCode, ValueSet theVs) {
    for (Coding next : theCode.getCoding()) {
      ValidationResult retVal = validateCode(theOptions, next, theVs);
      if (retVal.isOk()) {
        return retVal;
      }
    }

    return new ValidationResult(IssueSeverity.ERROR, null);
  }

  @Override
  public ValidationResult validateCode(ValidationOptions theOptions, Coding theCode, ValueSet theVs) {
    String system = theCode.getSystem();
    String code = theCode.getCode();
    String display = theCode.getDisplay();
    return validateCode(theOptions, system, code, display, theVs);
  }

  @Override
  public ValidationResult validateCode(ValidationOptions theOptions, String theSystem, String theCode, String theDisplay) {
    IValidationSupport.CodeValidationResult result = myValidationSupport.validateCode(new ValidationSupportContext(myValidationSupport), convertConceptValidationOptions(theOptions), theSystem, theCode, theDisplay, null);
    if (result == null) {
      return null;
    }

    IssueSeverity severity = null;
    if (result.getSeverity() != null) {
      severity = IssueSeverity.fromCode(result.getSeverityCode());
    }

    ConceptDefinitionComponent definition = new ConceptDefinitionComponent().setCode(result.getCode());
    return new ValidationResult(severity, result.getMessage(), definition);
  }

  @Override
  public ValidationResult validateCode(ValidationOptions theOptions, String theSystem, String theCode, String theDisplay, ConceptSetComponent theVsi) {
    throw new UnsupportedOperationException(Msg.code(267));
  }


  @Override
  public ValidationResult validateCode(ValidationOptions theOptions, String theSystem, String theCode, String theDisplay, ValueSet theVs) {

    IValidationSupport.CodeValidationResult outcome;
    if (isNotBlank(theVs.getUrl())) {
      outcome = myValidationSupport.validateCode(new ValidationSupportContext(myValidationSupport), convertConceptValidationOptions(theOptions), theSystem, theCode, theDisplay, theVs.getUrl());
    } else {
      outcome = myValidationSupport.validateCodeInValueSet(new ValidationSupportContext(myValidationSupport), convertConceptValidationOptions(theOptions), theSystem, theCode, theDisplay, theVs);
    }

    if (outcome != null && outcome.isOk()) {
      ConceptDefinitionComponent definition = new ConceptDefinitionComponent();
      definition.setCode(theCode);
      definition.setDisplay(outcome.getDisplay());
      return new ValidationResult(definition);
    }

    return new ValidationResult(IssueSeverity.ERROR, "Unknown code[" + theCode + "] in system[" + Constants.codeSystemWithDefaultDescription(theSystem) + "]");
  }

  @Override
  public ValidationResult validateCode(ValidationOptions theOptions, String code, ValueSet vs) {
    ValidationOptions options = theOptions.guessSystem();
    return validateCode(options, null, code, null, vs);
  }

  @Override
  @CoverageIgnore
  public List<MetadataResource> allConformanceResources() {
    throw new UnsupportedOperationException(Msg.code(268));
  }

  @Override
  public void generateSnapshot(StructureDefinition p) throws FHIRException {
    throw new UnsupportedOperationException(Msg.code(269));
  }

  @Override
  public Parameters getExpansionParameters() {
    return myExpansionProfile;
  }

  @Override
  public void setExpansionProfile(Parameters theExpParameters) {
    myExpansionProfile = theExpParameters;
  }

  @Override
  @CoverageIgnore
  public boolean hasCache() {
    throw new UnsupportedOperationException(Msg.code(270));
  }

  @Override
  public ValueSetExpander.ValueSetExpansionOutcome expandVS(ValueSet theSource, boolean theCacheOk, boolean theHierarchical) {
    throw new UnsupportedOperationException(Msg.code(271));
  }

  @Override
  public ValueSetExpander.ValueSetExpansionOutcome expandVS(ConceptSetComponent theInc, boolean theHierarchical) throws TerminologyServiceException {
    ValueSet input = new ValueSet();
    input.getCompose().addInclude(theInc);
    IValidationSupport.ValueSetExpansionOutcome output = myValidationSupport.expandValueSet(new ValidationSupportContext(myValidationSupport), null, input);
    return new ValueSetExpander.ValueSetExpansionOutcome((ValueSet) output.getValueSet(), output.getError(), null);
  }

  @Override
  public ILoggingService getLogger() {
    throw new UnsupportedOperationException(Msg.code(272));
  }

  @Override
  public void setLogger(ILoggingService theLogger) {
    throw new UnsupportedOperationException(Msg.code(273));
  }

  @Override
  public String getVersion() {
    return myCtx.getVersion().getVersion().getFhirVersionString();
  }

  @Override
  public UcumService getUcumService() {
    throw new UnsupportedOperationException(Msg.code(274));
  }

  @Override
  public void setUcumService(UcumService ucumService) {
    throw new UnsupportedOperationException(Msg.code(275));
  }

  @Override
  public boolean isNoTerminologyServer() {
    return false;
  }

  @Override
  public TranslationServices translator() {
    throw new UnsupportedOperationException(Msg.code(276));
  }

  @Override
  public List<StructureMap> listTransforms() {
    throw new UnsupportedOperationException(Msg.code(277));
  }

  @Override
  public StructureMap getTransform(String url) {
    throw new UnsupportedOperationException(Msg.code(278));
  }

  @Override
  public String getOverrideVersionNs() {
    return myOverrideVersionNs;
  }

  @Override
  public void setOverrideVersionNs(String value) {
    myOverrideVersionNs = value;
  }

  @Override
  public StructureDefinition fetchTypeDefinition(String typeName) {
    return fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/" + typeName);
  }

  @Override
  public String getLinkForUrl(String corePath, String url) {
    throw new UnsupportedOperationException(Msg.code(279));
  }

  @Override
  public List<String> getTypeNames() {
    throw new UnsupportedOperationException(Msg.code(280));
  }

  @Override
  public <T extends org.hl7.fhir.r4.model.Resource> T fetchResource(Class<T> theClass, String theUri) {
    if (myValidationSupport == null) {
      return null;
    } else {
      @SuppressWarnings("unchecked")
      T retVal = (T) myFetchedResourceCache.get(theUri, t -> myValidationSupport.fetchResource(theClass, theUri));
      return retVal;
    }
  }

  @Override
  public <T extends org.hl7.fhir.r4.model.Resource> T fetchResourceWithException(Class<T> theClass, String theUri) throws FHIRException {
    T retVal = fetchResource(theClass, theUri);
    if (retVal == null) {
      throw new FHIRException(Msg.code(281) + "Could not find resource: " + theUri);
    }
    return retVal;
  }

  @Override
  public org.hl7.fhir.r4.model.Resource fetchResourceById(String theType, String theUri) {
    throw new UnsupportedOperationException(Msg.code(282));
  }

  @Override
  public <T extends org.hl7.fhir.r4.model.Resource> boolean hasResource(Class<T> theClass_, String theUri) {
    throw new UnsupportedOperationException(Msg.code(283));
  }

  @Override
  public void cacheResource(org.hl7.fhir.r4.model.Resource theRes) throws FHIRException {
    throw new UnsupportedOperationException(Msg.code(284));
  }

  @Override
  public Set<String> getResourceNamesAsSet() {
    return myCtx.getResourceTypes();
  }

  @Override
  public ValueSetExpander.ValueSetExpansionOutcome expandVS(ElementDefinitionBindingComponent theBinding, boolean theCacheOk, boolean theHierarchical) throws FHIRException {
    throw new UnsupportedOperationException(Msg.code(285));
  }

  public static ConceptValidationOptions convertConceptValidationOptions(ValidationOptions theOptions) {
    ConceptValidationOptions retVal = new ConceptValidationOptions();
    if (theOptions.isGuessSystem()) {
      retVal = retVal.setInferSystem(true);
    }
    return retVal;
  }

}
