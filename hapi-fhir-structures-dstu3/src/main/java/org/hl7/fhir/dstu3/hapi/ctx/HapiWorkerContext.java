package org.hl7.fhir.dstu3.hapi.ctx;

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
import org.hl7.fhir.dstu3.context.IWorkerContext;
import org.hl7.fhir.dstu3.formats.IParser;
import org.hl7.fhir.dstu3.formats.ParserType;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.ConceptMap;
import org.hl7.fhir.dstu3.model.ExpansionProfile;
import org.hl7.fhir.dstu3.model.MetadataResource;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.ResourceType;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.dstu3.terminologies.ValueSetExpander;
import org.hl7.fhir.dstu3.utils.INarrativeGenerator;
import org.hl7.fhir.dstu3.utils.validation.IResourceValidator;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.utilities.i18n.I18nBase;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueSeverity;
import org.hl7.fhir.utilities.validation.ValidationOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public final class HapiWorkerContext extends I18nBase implements IWorkerContext {
  private static final Logger ourLog = LoggerFactory.getLogger(HapiWorkerContext.class);
  private final FhirContext myCtx;
  private final Cache<String, Resource> myFetchedResourceCache;
  private IValidationSupport myValidationSupport;
  private ExpansionProfile myExpansionProfile;

  public HapiWorkerContext(FhirContext theCtx, IValidationSupport theValidationSupport) {
    Validate.notNull(theCtx, "theCtx must not be null");
    Validate.notNull(theValidationSupport, "theValidationSupport must not be null");
    myCtx = theCtx;
    myValidationSupport = theValidationSupport;

    long timeoutMillis = 10 * DateUtils.MILLIS_PER_SECOND;
    if (System.getProperties().containsKey(Constants.TEST_SYSTEM_PROP_VALIDATION_RESOURCE_CACHES_MS)) {
      timeoutMillis = Long.parseLong(System.getProperty(Constants.TEST_SYSTEM_PROP_VALIDATION_RESOURCE_CACHES_MS));
    }
    myFetchedResourceCache = Caffeine.newBuilder().expireAfterWrite(timeoutMillis, TimeUnit.MILLISECONDS).build();

    // Set a default locale
    setValidationMessageLanguage(getLocale());
  }

  @Override
  @CoverageIgnore
  public List<MetadataResource> allConformanceResources() {
    throw new UnsupportedOperationException(Msg.code(610));
  }

  @Override
  public List<StructureDefinition> allStructures() {
    return myValidationSupport.fetchAllStructureDefinitions();
  }

  @Override
  public ValueSetExpansionComponent expandVS(ConceptSetComponent theInc, boolean theHierarchical) {
    ValueSet input = new ValueSet();
    input.getCompose().addInclude(theInc);
    IValidationSupport.ValueSetExpansionOutcome output = myValidationSupport.expandValueSet(new ValidationSupportContext(myValidationSupport), null, input);
    ValueSet outputValueSet = (ValueSet) output.getValueSet();
    if (outputValueSet != null) {
      return outputValueSet.getExpansion();
    } else {
      return null;
    }
  }

  @Override
  public StructureDefinition fetchTypeDefinition(String theCode) {
    return fetchResource(org.hl7.fhir.dstu3.model.StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/" + theCode);
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
  public <T extends Resource> T fetchResource(Class<T> theClass, String theUri) {
    Validate.notBlank(theUri, "theUri must not be null or blank");
    if (myValidationSupport == null) {
      return null;
    } else {
      try {
        //noinspection unchecked
        return (T) myFetchedResourceCache.get(theUri, t -> {
          T resource = myValidationSupport.fetchResource(theClass, theUri);
          if (resource == null) {
            throw new IllegalArgumentException(Msg.code(611));
          }
          return resource;
        });
      } catch (IllegalArgumentException e) {
        return null;
      }
    }
  }

  @Override
  public <T extends Resource> T fetchResourceWithException(Class<T> theClass_, String theUri) throws FHIRException {
    T retVal = fetchResource(theClass_, theUri);
    if (retVal == null) {
      throw new FHIRException(Msg.code(612) + "Unable to fetch " + theUri);
    }
    return retVal;
  }

  @Override
  public List<ConceptMap> findMapsForSource(String theUrl) {
    throw new UnsupportedOperationException(Msg.code(613));
  }

  @Override
  public ValueSetExpander.ValueSetExpansionOutcome expandVS(ValueSet source, boolean cacheOk, boolean heiarchical) {
    throw new UnsupportedOperationException(Msg.code(614));
  }

  @Override
  public String getAbbreviation(String theName) {
    throw new UnsupportedOperationException(Msg.code(615));
  }

  @Override
  public ExpansionProfile getExpansionProfile() {
    return myExpansionProfile;
  }

  @Override
  public void setExpansionProfile(ExpansionProfile theExpProfile) {
    myExpansionProfile = theExpProfile;
  }

  @Override
  public INarrativeGenerator getNarrativeGenerator(String thePrefix, String theBasePath) {
    throw new UnsupportedOperationException(Msg.code(616));
  }

  @Override
  public IResourceValidator newValidator() throws FHIRException {
    throw new UnsupportedOperationException(Msg.code(617));
  }

  @Override
  public IParser getParser(ParserType theType) {
    throw new UnsupportedOperationException(Msg.code(618));
  }

  @Override
  public IParser getParser(String theType) {
    throw new UnsupportedOperationException(Msg.code(619));
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
  public Set<String> getResourceNamesAsSet() {
    return new HashSet<>(getResourceNames());
  }

  @Override
  public List<String> getTypeNames() {
    throw new UnsupportedOperationException(Msg.code(620));
  }

  @Override
  public String getVersion() {
    return myCtx.getVersion().getVersion().getFhirVersionString();
  }

  @Override
  @CoverageIgnore
  public boolean hasCache() {
    throw new UnsupportedOperationException(Msg.code(621));
  }

  @Override
  public <T extends Resource> boolean hasResource(Class<T> theClass_, String theUri) {
    throw new UnsupportedOperationException(Msg.code(622));
  }

  @Override
  public boolean isNoTerminologyServer() {
    return false;
  }

  @Override
  public IParser newJsonParser() {
    throw new UnsupportedOperationException(Msg.code(623));
  }

  @Override
  public IParser newXmlParser() {
    throw new UnsupportedOperationException(Msg.code(624));
  }

  @Override
  public String oid2Uri(String theCode) {
    throw new UnsupportedOperationException(Msg.code(625));
  }

  @Override
  public void setLogger(ILoggingService theLogger) {
    throw new UnsupportedOperationException(Msg.code(626));
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
  public ValidationResult validateCode(CodeableConcept theCode, ValueSet theVs) {
    for (Coding next : theCode.getCoding()) {
      ValidationResult retVal = validateCode(next, theVs);
      if (retVal.isOk()) {
        return retVal;
      }
    }

    return new ValidationResult(IssueSeverity.ERROR, null);
  }

  @Override
  public ValidationResult validateCode(Coding theCode, ValueSet theVs) {
    String system = theCode.getSystem();
    String code = theCode.getCode();
    String display = theCode.getDisplay();
    return validateCode(system, code, display, theVs);
  }

  @Override
  public ValidationResult validateCode(String theSystem, String theCode, String theDisplay) {
    ValidationOptions options = new ValidationOptions();
    IValidationSupport.CodeValidationResult result = myValidationSupport.validateCode(new ValidationSupportContext(myValidationSupport), convertConceptValidationOptions(options), theSystem, theCode, theDisplay, null);
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

  public static ConceptValidationOptions convertConceptValidationOptions(ValidationOptions theOptions) {
    ConceptValidationOptions retVal = new ConceptValidationOptions();
    if (theOptions.isGuessSystem()) {
      retVal = retVal.setInferSystem(true);
    }
    return retVal;
  }

  @Override
  public ValidationResult validateCode(String theSystem, String theCode, String theDisplay, ConceptSetComponent theVsi) {
    throw new UnsupportedOperationException(Msg.code(627));
  }

  @Override
  public ValidationResult validateCode(String theSystem, String theCode, String theDisplay, ValueSet theVs) {

    IValidationSupport.CodeValidationResult outcome;
    ValidationOptions options = new ValidationOptions();
    if (isNotBlank(theVs.getUrl())) {
      outcome = myValidationSupport.validateCode(new ValidationSupportContext(myValidationSupport), convertConceptValidationOptions(options), theSystem, theCode, theDisplay, theVs.getUrl());
    } else {
      outcome = myValidationSupport.validateCodeInValueSet(new ValidationSupportContext(myValidationSupport), convertConceptValidationOptions(options), theSystem, theCode, theDisplay, theVs);
    }

    if (outcome != null && outcome.isOk()) {
      ConceptDefinitionComponent definition = new ConceptDefinitionComponent();
      definition.setCode(theCode);
      definition.setDisplay(outcome.getDisplay());
      return new ValidationResult(definition);
    }

    return new ValidationResult(IssueSeverity.ERROR, "Unknown code[" + theCode + "] in system[" + Constants.codeSystemWithDefaultDescription(theSystem) + "]");
  }

}
