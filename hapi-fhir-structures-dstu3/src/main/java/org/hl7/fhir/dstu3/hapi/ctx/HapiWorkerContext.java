package org.hl7.fhir.dstu3.hapi.ctx;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.CoverageIgnore;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.dstu3.context.IWorkerContext;
import org.hl7.fhir.dstu3.formats.IParser;
import org.hl7.fhir.dstu3.formats.ParserType;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.dstu3.terminologies.ValueSetExpander;
import org.hl7.fhir.dstu3.terminologies.ValueSetExpanderFactory;
import org.hl7.fhir.dstu3.terminologies.ValueSetExpanderSimple;
import org.hl7.fhir.dstu3.utils.INarrativeGenerator;
import org.hl7.fhir.dstu3.utils.IResourceValidator;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.TerminologyServiceException;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueSeverity;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public final class HapiWorkerContext implements IWorkerContext, ValueSetExpander, ValueSetExpanderFactory {
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
  }

  @Override
  @CoverageIgnore
  public List<MetadataResource> allConformanceResources() {
    throw new UnsupportedOperationException();
  }

  @Override
  public List<StructureDefinition> allStructures() {
    return myValidationSupport.fetchAllStructureDefinitions(myCtx);
  }

  @Override
  public ValueSetExpansionOutcome expand(ValueSet theSource, ExpansionProfile theProfile) {
    ValueSetExpansionOutcome vso;
    try {
      vso = getExpander().expand(theSource, theProfile);
    } catch (InvalidRequestException e) {
      throw e;
    } catch (TerminologyServiceException e) {
      throw new InvalidRequestException(e.getMessage(), e);
    } catch (Exception e) {
      throw new InternalErrorException(e);
    }
    if (vso.getError() != null) {
      throw new InvalidRequestException(vso.getError());
    } else {
      return vso;
    }
  }

  @Override
  public ValueSetExpansionOutcome expandVS(ValueSet theSource, boolean theCacheOk, boolean theHeiarchical) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ValueSetExpansionComponent expandVS(ConceptSetComponent theInc, boolean theHeiarchical) {
    return myValidationSupport.expandValueSet(myCtx, theInc);
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
      return myValidationSupport.fetchCodeSystem(myCtx, theSystem);
    }
  }

  @Override
  public <T extends Resource> T fetchResource(Class<T> theClass, String theUri) {
    if (myValidationSupport == null) {
      return null;
    } else {
      @SuppressWarnings("unchecked")
      T retVal = (T) myFetchedResourceCache.get(theUri, t->{
        return myValidationSupport.fetchResource(myCtx, theClass, theUri);
      });
      return retVal;
    }
  }

  @Override
  public <T extends Resource> T fetchResourceWithException(Class<T> theClass_, String theUri) throws FHIRException {
    T retVal = fetchResource(theClass_, theUri);
    if (retVal == null) {
      throw new FHIRException("Unable to fetch " + theUri);
    }
    return retVal;
  }

  @Override
  public List<ConceptMap> findMapsForSource(String theUrl) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getAbbreviation(String theName) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ValueSetExpander getExpander() {
    ValueSetExpanderSimple retVal = new ValueSetExpanderSimple(this, this);
    retVal.setMaxExpansionSize(Integer.MAX_VALUE);
    return retVal;
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
    throw new UnsupportedOperationException();
  }

  @Override
  public IResourceValidator newValidator() throws FHIRException {
    throw new UnsupportedOperationException();
  }

  @Override
  public IParser getParser(ParserType theType) {
    throw new UnsupportedOperationException();
  }

  @Override
  public IParser getParser(String theType) {
    throw new UnsupportedOperationException();
  }

  @Override
  public List<String> getResourceNames() {
    List<String> result = new ArrayList<String>();
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
    throw new UnsupportedOperationException();
  }

  @Override
  public String getVersion() {
    return myCtx.getVersion().getVersion().getFhirVersionString();
  }

  @Override
  @CoverageIgnore
  public boolean hasCache() {
    throw new UnsupportedOperationException();
  }

  @Override
  public <T extends Resource> boolean hasResource(Class<T> theClass_, String theUri) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isNoTerminologyServer() {
    return false;
  }

  @Override
  public IParser newJsonParser() {
    throw new UnsupportedOperationException();
  }

  @Override
  public IParser newXmlParser() {
    throw new UnsupportedOperationException();
  }

  @Override
  public String oid2Uri(String theCode) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setLogger(ILoggingService theLogger) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean supportsSystem(String theSystem) {
    if (myValidationSupport == null) {
      return false;
    } else {
      return myValidationSupport.isCodeSystemSupported(myCtx, theSystem);
    }
  }

  @Override
  public Set<String> typeTails() {
    return new HashSet<String>(Arrays.asList("Integer", "UnsignedInt", "PositiveInt", "Decimal", "DateTime", "Date", "Time", "Instant", "String", "Uri", "Oid", "Uuid", "Id", "Boolean", "Code",
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
    IValidationSupport.CodeValidationResult result = myValidationSupport.validateCode(myCtx, theSystem, theCode, theDisplay, (String)null);
    if (result == null) {
      return null;
    }
    return new ValidationResult((IssueSeverity)result.getSeverity(), result.getMessage(), (ConceptDefinitionComponent)result.asConceptDefinition());
  }

  @Override
  public ValidationResult validateCode(String theSystem, String theCode, String theDisplay, ConceptSetComponent theVsi) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ValidationResult validateCode(String theSystem, String theCode, String theDisplay, ValueSet theVs) {

    /*
     * The following valueset is a special case, since the BCP codesystem is very difficult to expand
     */
    if ("http://hl7.org/fhir/ValueSet/languages".equals(theVs.getUrl())) {
      ConceptDefinitionComponent definition = new ConceptDefinitionComponent();
      definition.setCode(theSystem);
      definition.setDisplay(theCode);
      return new ValidationResult(definition);
    }

    /*
     * The following valueset is a special case, since the mime types codesystem is very difficult to expand
     */
    if ("http://hl7.org/fhir/ValueSet/mimetypes".equals(theVs.getUrl())) {
      ConceptDefinitionComponent definition = new ConceptDefinitionComponent();
      definition.setCode(theSystem);
      definition.setDisplay(theCode);
      return new ValidationResult(definition);
    }

    IValidationSupport.CodeValidationResult outcome;
    if (isNotBlank(theVs.getUrl())) {
      outcome = myValidationSupport.validateCode(myCtx, theSystem, theCode, theDisplay, theVs.getUrl());
    } else {
      outcome = myValidationSupport.validateCodeInValueSet(myCtx, theSystem, theCode, theDisplay, theVs);
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
