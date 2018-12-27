package org.hl7.fhir.r4.hapi.ctx;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
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
import org.hl7.fhir.r4.hapi.ctx.IValidationSupport.CodeValidationResult;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.r4.model.ElementDefinition.ElementDefinitionBindingComponent;
import org.hl7.fhir.r4.model.ValueSet.ConceptReferenceComponent;
import org.hl7.fhir.r4.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.r4.model.ValueSet.ValueSetExpansionContainsComponent;
import org.hl7.fhir.r4.terminologies.ValueSetExpander;
import org.hl7.fhir.r4.terminologies.ValueSetExpanderFactory;
import org.hl7.fhir.r4.terminologies.ValueSetExpanderSimple;
import org.hl7.fhir.r4.utils.IResourceValidator;
import org.hl7.fhir.utilities.TranslationServices;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueSeverity;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public final class HapiWorkerContext implements IWorkerContext, ValueSetExpander, ValueSetExpanderFactory {
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
  }

  @Override
  public List<StructureDefinition> allStructures() {
    return myValidationSupport.fetchAllStructureDefinitions(myCtx);
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
  public List<ConceptMap> findMapsForSource(String theUrl) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getAbbreviation(String theName) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ValueSetExpander getExpander() {
    ValueSetExpanderSimple retVal = new ValueSetExpanderSimple(this);
    retVal.setMaxExpansionSize(Integer.MAX_VALUE);
    return retVal;
  }

  @Override
  public org.hl7.fhir.r4.utils.INarrativeGenerator getNarrativeGenerator(String thePrefix, String theBasePath) {
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
    List<String> result = new ArrayList<>();
    for (ResourceType next : ResourceType.values()) {
      result.add(next.name());
    }
    Collections.sort(result);
    return result;
  }

  @Override
  public IParser newJsonParser() {
    throw new UnsupportedOperationException();
  }

  @Override
  public IResourceValidator newValidator() {
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
  public boolean supportsSystem(String theSystem) {
    if (myValidationSupport == null) {
      return false;
    } else {
      return myValidationSupport.isCodeSystemSupported(myCtx, theSystem);
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
      if (retVal != null && retVal.isOk()) {
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
    CodeValidationResult result = myValidationSupport.validateCode(myCtx, theSystem, theCode, theDisplay);
    if (result == null) {
      return null;
    }
    return new ValidationResult(result.getSeverity(), result.getMessage(), result.asConceptDefinition());
  }

  @Override
  public ValidationResult validateCode(String theSystem, String theCode, String theDisplay, ConceptSetComponent theVsi) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ValidationResult validateCode(String theSystem, String theCode, String theDisplay, ValueSet theVs) {

    if (theVs != null && isNotBlank(theCode)) {
      for (ConceptSetComponent next : theVs.getCompose().getInclude()) {
        if (isBlank(theSystem) || theSystem.equals(next.getSystem())) {
          for (ConceptReferenceComponent nextCode : next.getConcept()) {
            if (theCode.equals(nextCode.getCode())) {
              CodeType code = new CodeType(theCode);
              return new ValidationResult(new ConceptDefinitionComponent(code));
            }
          }
        }
      }
    }

    boolean caseSensitive = true;
    if (isNotBlank(theSystem)) {
      CodeSystem system = fetchCodeSystem(theSystem);
      if (system == null) {
        return new ValidationResult(IssueSeverity.INFORMATION, "Code " + theSystem + "/" + theCode + " was not validated because the code system is not present");
      }

      if (system.hasCaseSensitive()) {
        caseSensitive = system.getCaseSensitive();
      }
    }

    String wantCode = theCode;
    if (!caseSensitive) {
      wantCode = wantCode.toUpperCase();
    }

    ValueSetExpansionOutcome expandedValueSet = null;

    /*
     * The following valueset is a special case, since the BCP codesystem is very difficult to expand
     */
    if (theVs != null && "http://hl7.org/fhir/ValueSet/languages".equals(theVs.getId())) {
      ValueSet expansion = new ValueSet();
      for (ConceptSetComponent nextInclude : theVs.getCompose().getInclude()) {
        for (ConceptReferenceComponent nextConcept : nextInclude.getConcept()) {
          expansion.getExpansion().addContains().setCode(nextConcept.getCode()).setDisplay(nextConcept.getDisplay());
        }
      }
      expandedValueSet = new ValueSetExpansionOutcome(expansion);
    }

    /*
     * The following valueset is a special case, since the mime types codesystem is very difficult to expand
     */
    if (theVs != null && "http://hl7.org/fhir/ValueSet/mimetypes".equals(theVs.getId())) {
      ValueSet expansion = new ValueSet();
      expansion.getExpansion().addContains().setCode(theCode).setSystem(theSystem).setDisplay(theDisplay);
      expandedValueSet = new ValueSetExpansionOutcome(expansion);
    }

    if (expandedValueSet == null) {
      expandedValueSet = expand(theVs, null);
    }

    for (ValueSetExpansionContainsComponent next : expandedValueSet.getValueset().getExpansion().getContains()) {
      String nextCode = next.getCode();
      if (!caseSensitive) {
        nextCode = nextCode.toUpperCase();
      }

      if (nextCode.equals(wantCode)) {
        if (theSystem == null || next.getSystem().equals(theSystem)) {
          ConceptDefinitionComponent definition = new ConceptDefinitionComponent();
          definition.setCode(next.getCode());
          definition.setDisplay(next.getDisplay());
          ValidationResult retVal = new ValidationResult(definition);
          return retVal;
        }
      }
    }

    return new ValidationResult(IssueSeverity.ERROR, "Unknown code[" + theCode + "] in system[" + theSystem + "]");
  }

  @Override
  public ValidationResult validateCode(String code, ValueSet vs) {
    return validateCode(null, code, null, vs);
  }

  @Override
  @CoverageIgnore
  public List<MetadataResource> allConformanceResources() {
    throw new UnsupportedOperationException();
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
    throw new UnsupportedOperationException();
  }

  @Override
  public ValueSetExpansionOutcome expand(ValueSet theSource, Parameters theProfile) {
    ValueSetExpansionOutcome vso;
    try {
      vso = getExpander().expand(theSource, theProfile);
    } catch (InvalidRequestException e) {
      throw e;
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
  public ValueSetExpansionOutcome expandVS(ConceptSetComponent theInc, boolean theHeiarchical) throws TerminologyServiceException {
    return myValidationSupport.expandValueSet(myCtx, theInc);
  }

  @Override
  public void setLogger(ILoggingService theLogger) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ILoggingService getLogger() {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getVersion() {
    return myCtx.getVersion().getVersion().getFhirVersionString();
  }

  @Override
  public UcumService getUcumService() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isNoTerminologyServer() {
    return false;
  }

  @Override
  public TranslationServices translator() {
    throw new UnsupportedOperationException();
  }

  @Override
  public List<StructureMap> listTransforms() {
    throw new UnsupportedOperationException();
  }

  @Override
  public StructureMap getTransform(String url) {
    throw new UnsupportedOperationException();
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
  public void setUcumService(UcumService ucumService) {
    throw new UnsupportedOperationException();
  }

  @Override
  public List<String> getTypeNames() {
    throw new UnsupportedOperationException();
  }

  @Override
  public <T extends org.hl7.fhir.r4.model.Resource> T fetchResource(Class<T> theClass, String theUri) {
    if (myValidationSupport == null) {
      return null;
    } else {
      @SuppressWarnings("unchecked")
      T retVal = (T) myFetchedResourceCache.get(theUri, t -> {
        return myValidationSupport.fetchResource(myCtx, theClass, theUri);
      });
      return retVal;
    }
  }

  @Override
  public <T extends org.hl7.fhir.r4.model.Resource> T fetchResourceWithException(Class<T> theClass, String theUri) throws FHIRException {
    T retVal = fetchResource(theClass, theUri);
    if (retVal == null) {
      throw new FHIRException("Could not find resource: " + theUri);
    }
    return retVal;
  }

  @Override
  public org.hl7.fhir.r4.model.Resource fetchResourceById(String theType, String theUri) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <T extends org.hl7.fhir.r4.model.Resource> boolean hasResource(Class<T> theClass_, String theUri) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void cacheResource(org.hl7.fhir.r4.model.Resource theRes) throws FHIRException {
    throw new UnsupportedOperationException();
  }

  @Override
  public Set<String> getResourceNamesAsSet() {
    return myCtx.getResourceNames();
  }

  @Override
  public ValueSetExpansionOutcome expandVS(ElementDefinitionBindingComponent theBinding, boolean theCacheOk, boolean theHeiarchical) throws FHIRException {
    throw new UnsupportedOperationException();
  }

}
