package org.hl7.fhir.dstu3.validation;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.dstu3.conformance.ProfileUtilities;
import org.hl7.fhir.dstu3.context.IWorkerContext;
import org.hl7.fhir.dstu3.context.IWorkerContext.ValidationResult;
import org.hl7.fhir.dstu3.elementmodel.Element;
import org.hl7.fhir.dstu3.elementmodel.Element.SpecialElement;
import org.hl7.fhir.dstu3.elementmodel.JsonParser;
import org.hl7.fhir.dstu3.elementmodel.Manager;
import org.hl7.fhir.dstu3.elementmodel.Manager.FhirFormat;
import org.hl7.fhir.dstu3.elementmodel.ObjectConverter;
import org.hl7.fhir.dstu3.elementmodel.ParserBase;
import org.hl7.fhir.dstu3.elementmodel.ParserBase.ValidationPolicy;
import org.hl7.fhir.dstu3.elementmodel.XmlParser;
import org.hl7.fhir.dstu3.formats.FormatUtilities;
import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.Attachment;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.DateType;
import org.hl7.fhir.dstu3.model.DecimalType;
import org.hl7.fhir.dstu3.model.DomainResource;
import org.hl7.fhir.dstu3.model.ElementDefinition;
import org.hl7.fhir.dstu3.model.ElementDefinition.AggregationMode;
import org.hl7.fhir.dstu3.model.ElementDefinition.ConstraintSeverity;
import org.hl7.fhir.dstu3.model.ElementDefinition.DiscriminatorType;
import org.hl7.fhir.dstu3.model.ElementDefinition.ElementDefinitionBindingComponent;
import org.hl7.fhir.dstu3.model.ElementDefinition.ElementDefinitionConstraintComponent;
import org.hl7.fhir.dstu3.model.ElementDefinition.ElementDefinitionSlicingDiscriminatorComponent;
import org.hl7.fhir.dstu3.model.ElementDefinition.PropertyRepresentation;
import org.hl7.fhir.dstu3.model.ElementDefinition.TypeRefComponent;
import org.hl7.fhir.dstu3.model.Enumeration;
import org.hl7.fhir.dstu3.model.Enumerations.BindingStrength;
import org.hl7.fhir.dstu3.model.ExpressionNode;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.IntegerType;
import org.hl7.fhir.dstu3.model.Period;
import org.hl7.fhir.dstu3.model.Quantity;
import org.hl7.fhir.dstu3.model.Questionnaire;
import org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemComponent;
import org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemOptionComponent;
import org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemType;
import org.hl7.fhir.dstu3.model.Range;
import org.hl7.fhir.dstu3.model.Ratio;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.SampledData;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.dstu3.model.StructureDefinition.ExtensionContext;
import org.hl7.fhir.dstu3.model.StructureDefinition.StructureDefinitionKind;
import org.hl7.fhir.dstu3.model.StructureDefinition.StructureDefinitionSnapshotComponent;
import org.hl7.fhir.dstu3.model.StructureDefinition.TypeDerivationRule;
import org.hl7.fhir.dstu3.model.TimeType;
import org.hl7.fhir.dstu3.model.Timing;
import org.hl7.fhir.dstu3.model.Type;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionContainsComponent;
import org.hl7.fhir.dstu3.utils.FHIRLexer.FHIRLexerException;
import org.hl7.fhir.dstu3.utils.FHIRPathEngine;
import org.hl7.fhir.dstu3.utils.FHIRPathEngine.IEvaluationContext;
import org.hl7.fhir.dstu3.utils.IResourceValidator;
import org.hl7.fhir.dstu3.utils.ToolingExtensions;
import org.hl7.fhir.dstu3.utils.ValidationProfileSet;
import org.hl7.fhir.dstu3.utils.ValidationProfileSet.ProfileRegistration;
import org.hl7.fhir.exceptions.DefinitionException;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.FHIRFormatError;
import org.hl7.fhir.exceptions.PathEngineException;
import org.hl7.fhir.exceptions.TerminologyServiceException;
import org.hl7.fhir.utilities.CommaSeparatedStringBuilder;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueSeverity;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueType;
import org.hl7.fhir.utilities.validation.ValidationMessage.Source;
import org.hl7.fhir.utilities.xhtml.NodeType;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import ca.uhn.fhir.util.ObjectUtil;


/**
 * Thinking of using this in a java program? Don't! 
 * You should use on of the wrappers instead. Either in HAPI, or use ValidationEngine
 * 
 * @author Grahame Grieve
 *
 */
/* 
 * todo:
 * check urn's don't start oid: or uuid: 
 */
public class InstanceValidator extends BaseValidator implements IResourceValidator {


  private IWorkerContext context;
  private FHIRPathEngine fpe; 

  // configuration items
  private CheckDisplayOption checkDisplay;
  private boolean anyExtensionsAllowed;
  private boolean errorForUnknownProfiles;
  private boolean noInvariantChecks;
  private boolean noTerminologyChecks;
  private BestPracticeWarningLevel bpWarnings;

  private List<String> extensionDomains = new ArrayList<String>();

  private IdStatus resourceIdRule;
  private boolean allowXsiLocation;

  // used during the build process to keep the overall volume of messages down
  private boolean suppressLoincSnomedMessages;

  private Bundle logical;

  // time tracking
  private long overall = 0;
  private long txTime = 0;
  private long sdTime = 0;
  private long loadTime = 0;
  private long fpeTime = 0;

  private boolean noBindingMsgSuppressed;
  private HashMap<Element, ResourceProfiles> resourceProfilesMap;
  private IValidatorResourceFetcher fetcher;
  long time = 0;

  /*
   * Keeps track of whether a particular profile has been checked or not yet
   */
  private class ProfileUsage {
    private StructureDefinition profile;
    private boolean checked;

    public ProfileUsage(StructureDefinition profile) {
      this.profile = profile;
      this.checked = false;
    }

    public boolean isChecked() {
      return checked;
    }

    public void setChecked() {
      this.checked = true;
    }

    public StructureDefinition getProfile() {
      return profile;
    }
  }

  /*
   * Keeps track of all profiles associated with a resource element and whether the resource has been checked against those profiles yet
   */
  public class ResourceProfiles {
    private Element resource;
    private Element owner;
    private NodeStack stack;
    private HashMap<StructureDefinition, ProfileUsage> profiles;
    private boolean processed;

    public ResourceProfiles(Element resource, NodeStack stack) {
      this.resource = resource;
      if (this.resource.getName().equals("contained"))
        this.owner = stack.parent.element;
      else
        this.owner = resource;
      this.stack = stack;
      this.profiles = new HashMap<StructureDefinition, ProfileUsage>();
      this.processed = false;
    }

    public boolean isProcessed() {
      return processed;
    }

    public void setProcessed() {
      processed = true;
    }

    public NodeStack getStack() {
      return stack;
    }

    public Element getOwner() {
      return owner;
    }

    public boolean hasProfiles() {
      return !profiles.isEmpty();
    }

    public void addProfiles(List<ValidationMessage> errors, ValidationProfileSet profiles, String path, Element element) throws FHIRException {
      for (ProfileRegistration profile : profiles.getCanonical())
        addProfile(errors, profile.getProfile(), profile.isError(), path, element);
    }
    
    public boolean addProfile(List<ValidationMessage> errors, String profile, boolean error, String path, Element element) {
      String effectiveProfile = profile;
      String version = null;
      if (profile.contains("|")) {
        effectiveProfile = profile.substring(0, profile.indexOf('|'));
        version = profile.substring(profile.indexOf('|')+1);
      }
      StructureDefinition sd = context.fetchResource(StructureDefinition.class, effectiveProfile);
      if (warningOrError(error, errors, IssueType.INVALID, element.line(), element.col(), path, sd != null, "StructureDefinition reference \"{0}\" could not be resolved", profile)) {
        if (rule(errors, IssueType.STRUCTURE, element.line(), element.col(), path, version==null || (sd.getVersion()!=null && sd.getVersion().equals(version)), 
             "Referenced version " + version + " does not match found version " + sd.getVersion() + " for profile " + sd.getUrl(), profile)) {
          if (rule(errors, IssueType.STRUCTURE, element.line(), element.col(), path, sd.hasSnapshot(),
              "StructureDefinition has no snapshot - validation is against the snapshot, so it must be provided")) {
            if (!profiles.containsKey(sd)) {
              profiles.put(sd,  new ProfileUsage(sd));
              addAncestorProfiles(sd);
              return true;
            }
          }
        }
      }
      return false;
    }

    public void addAncestorProfiles(StructureDefinition sd) {
      if (sd.getDerivation().equals(StructureDefinition.TypeDerivationRule.CONSTRAINT)) {
        StructureDefinition parentSd = context.fetchResource(StructureDefinition.class, sd.getBaseDefinition());
        if (parentSd != null && !profiles.containsKey(parentSd)) {
          ProfileUsage pu = new ProfileUsage(parentSd);
          pu.setChecked(); // We're going to check the child, so no need to check the parent
          profiles.put(parentSd, pu);
        }
      }
    }

    public List<ProfileUsage> uncheckedProfiles() {
      List<ProfileUsage> uncheckedProfiles = new ArrayList<ProfileUsage>();
      for (ProfileUsage profileUsage : profiles.values()) {
        if (!profileUsage.isChecked())
          uncheckedProfiles.add(profileUsage);
      }
      return uncheckedProfiles;
    }

    public boolean hasUncheckedProfiles() {
      return !uncheckedProfiles().isEmpty();
    }

    public void checkProfile(StructureDefinition profile) {
      ProfileUsage profileUsage = profiles.get(profile);
      if (profileUsage==null)
        throw new Error("Can't check profile that hasn't been added: " + profile.getUrl());
      else
        profileUsage.setChecked();
    }
  }

  public InstanceValidator(IWorkerContext theContext, IEvaluationContext hostServices) {
    super();
    this.context = theContext;
    fpe = new FHIRPathEngine(context);
    fpe.setHostServices(hostServices);
    source = Source.InstanceValidator;
  }

    public InstanceValidator(ValidationEngine engine) {
	    super();
	    this.context = engine.getContext();
		 fpe = engine.getFpe();
		 source = Source.InstanceValidator;
    }


  @Override
  public boolean isNoInvariantChecks() {
    return noInvariantChecks;
  }

  @Override
  public IResourceValidator setNoInvariantChecks(boolean value) {
    this.noInvariantChecks = value;
    return this;
  }

  public IValidatorResourceFetcher getFetcher() {
    return this.fetcher;
  }

  public IResourceValidator setFetcher(IValidatorResourceFetcher value) {
    this.fetcher = value;
    return this;
  }

  private boolean allowUnknownExtension(String url) {
    if (url.contains("example.org") || url.contains("acme.com") || url.contains("nema.org") || url.startsWith("http://hl7.org/fhir/tools/StructureDefinition/") || url.equals("http://hl7.org/fhir/StructureDefinition/structuredefinition-expression"))
      // Added structuredefinition-expression explicitly because it wasn't defined in the version of the spec it needs to be used with
      return true;
    for (String s : extensionDomains)
      if (url.startsWith(s))
        return true;
    return anyExtensionsAllowed;
  }

  private boolean isKnownExtension(String url) {
    // Added structuredefinition-expression explicitly because it wasn't defined in the version of the spec it needs to be used with
    if (url.contains("example.org") || url.contains("acme.com") || url.contains("nema.org") || url.startsWith("http://hl7.org/fhir/tools/StructureDefinition/") || url.equals("http://hl7.org/fhir/StructureDefinition/structuredefinition-expression"))
      return true;
    for (String s : extensionDomains)
      if (url.startsWith(s))
        return true;
    return false;
  }

  private void bpCheck(List<ValidationMessage> errors, IssueType invalid, int line, int col, String literalPath, boolean test, String message) {
    if (bpWarnings != null) {
      switch (bpWarnings) {
      case Error:
        rule(errors, invalid, line, col, literalPath, test, message);
      case Warning:
        warning(errors, invalid, line, col, literalPath, test, message);
      case Hint:
        hint(errors, invalid, line, col, literalPath, test, message);
      default: // do nothing
      }
    }
  }

  @Override
  public org.hl7.fhir.dstu3.elementmodel.Element validate(Object appContext, List<ValidationMessage> errors, InputStream stream, FhirFormat format) throws FHIRException, IOException {
    return validate(appContext, errors, stream, format, new ValidationProfileSet());
  }

  @Override
  public org.hl7.fhir.dstu3.elementmodel.Element validate(Object appContext, List<ValidationMessage> errors, InputStream stream, FhirFormat format, String profile) throws FHIRException, IOException {
    return validate(appContext, errors, stream, format,  new ValidationProfileSet(profile, true));
  }

  @Override
  public org.hl7.fhir.dstu3.elementmodel.Element validate(Object appContext, List<ValidationMessage> errors, InputStream stream, FhirFormat format, StructureDefinition profile) throws FHIRException, IOException {
    return validate(appContext, errors, stream, format, new ValidationProfileSet(profile));
  }

  @Override
  public org.hl7.fhir.dstu3.elementmodel.Element validate(Object appContext, List<ValidationMessage> errors, InputStream stream, FhirFormat format, ValidationProfileSet profiles) throws FHIRException, IOException {
    ParserBase parser = Manager.makeParser(context, format);
    if (parser instanceof XmlParser) 
      ((XmlParser) parser).setAllowXsiLocation(allowXsiLocation);
    parser.setupValidation(ValidationPolicy.EVERYTHING, errors); 
    long t = System.nanoTime();
    Element e = parser.parse(stream);
    loadTime = System.nanoTime() - t;
    if (e != null)
      validate(appContext, errors, e, profiles);
    return e;
  }

  @Override
  public org.hl7.fhir.dstu3.elementmodel.Element validate(Object appContext, List<ValidationMessage> errors, Resource resource) throws FHIRException, IOException {
    return validate(appContext, errors, resource, new ValidationProfileSet());
  }

  @Override
  public org.hl7.fhir.dstu3.elementmodel.Element validate(Object appContext, List<ValidationMessage> errors, Resource resource, String profile) throws FHIRException, IOException {
    return validate(appContext, errors, resource, new ValidationProfileSet(profile, true));
  }

  @Override
  public org.hl7.fhir.dstu3.elementmodel.Element validate(Object appContext, List<ValidationMessage> errors, Resource resource, StructureDefinition profile) throws FHIRException, IOException {
    return validate(appContext, errors, resource, new ValidationProfileSet(profile));
  }

  @Override
  public org.hl7.fhir.dstu3.elementmodel.Element validate(Object appContext, List<ValidationMessage> errors, Resource resource, ValidationProfileSet profiles) throws FHIRException, IOException {
    long t = System.nanoTime();
    Element e = new ObjectConverter(context).convert(resource);
    loadTime = System.nanoTime() - t;
    validate(appContext, errors, e, profiles);
    return e;
  }

  @Override
  public org.hl7.fhir.dstu3.elementmodel.Element validate(Object appContext, List<ValidationMessage> errors, org.w3c.dom.Element element) throws FHIRException, IOException {
    return validate(appContext, errors, element, new ValidationProfileSet());
  }

  @Override
  public org.hl7.fhir.dstu3.elementmodel.Element validate(Object appContext, List<ValidationMessage> errors, org.w3c.dom.Element element, String profile) throws FHIRException, IOException {
    return validate(appContext, errors, element, new ValidationProfileSet(profile, true));
  }

  @Override
  public org.hl7.fhir.dstu3.elementmodel.Element validate(Object appContext, List<ValidationMessage> errors, org.w3c.dom.Element element, StructureDefinition profile) throws FHIRException, IOException {
    return validate(appContext, errors, element, new ValidationProfileSet(profile));
  }

  @Override
  public org.hl7.fhir.dstu3.elementmodel.Element validate(Object appContext, List<ValidationMessage> errors, org.w3c.dom.Element element, ValidationProfileSet profiles) throws FHIRException, IOException {
    XmlParser parser = new XmlParser(context);
    parser.setupValidation(ValidationPolicy.EVERYTHING, errors); 
    long t = System.nanoTime();
    Element e = parser.parse(element);
    loadTime = System.nanoTime() - t;
    if (e != null) 
      validate(appContext, errors, e, profiles);
    return e;
  }

  @Override
  public org.hl7.fhir.dstu3.elementmodel.Element validate(Object appContext, List<ValidationMessage> errors, Document document) throws FHIRException, IOException {
    return validate(appContext, errors, document, new ValidationProfileSet());
  }

  @Override
  public org.hl7.fhir.dstu3.elementmodel.Element validate(Object appContext, List<ValidationMessage> errors, Document document, String profile) throws FHIRException, IOException {
    return validate(appContext, errors, document, new ValidationProfileSet(profile, true));
  }

  @Override
  public org.hl7.fhir.dstu3.elementmodel.Element validate(Object appContext, List<ValidationMessage> errors, Document document, StructureDefinition profile) throws FHIRException, IOException {
    return validate(appContext, errors, document, new ValidationProfileSet(profile));
  }

  @Override
  public org.hl7.fhir.dstu3.elementmodel.Element validate(Object appContext, List<ValidationMessage> errors, Document document, ValidationProfileSet profiles) throws FHIRException, IOException {
    XmlParser parser = new XmlParser(context);
    parser.setupValidation(ValidationPolicy.EVERYTHING, errors); 
    long t = System.nanoTime();
    Element e = parser.parse(document);
    loadTime = System.nanoTime() - t;
    if (e != null)
      validate(appContext, errors, e, profiles);
    return e;
  }

  @Override
  public org.hl7.fhir.dstu3.elementmodel.Element validate(Object appContext, List<ValidationMessage> errors, JsonObject object) throws FHIRException, IOException {
    return validate(appContext, errors, object, new ValidationProfileSet());
  }

  @Override
  public org.hl7.fhir.dstu3.elementmodel.Element validate(Object appContext, List<ValidationMessage> errors, JsonObject object, String profile) throws FHIRException, IOException {
    return validate(appContext, errors, object, new ValidationProfileSet(profile, true));
  }

  @Override
  public org.hl7.fhir.dstu3.elementmodel.Element validate(Object appContext, List<ValidationMessage> errors, JsonObject object, StructureDefinition profile) throws FHIRException, IOException {
    return validate(appContext, errors, object, new ValidationProfileSet(profile));
  }

  @Override
  public org.hl7.fhir.dstu3.elementmodel.Element validate(Object appContext, List<ValidationMessage> errors, JsonObject object, ValidationProfileSet profiles) throws FHIRException, IOException {
    JsonParser parser = new JsonParser(context);
    parser.setupValidation(ValidationPolicy.EVERYTHING, errors); 
    long t = System.nanoTime();
    Element e = parser.parse(object);
    loadTime = System.nanoTime() - t;
    if (e != null)
      validate(appContext, errors, e, profiles);
    return e;
  }

  @Override
  public void validate(Object appContext, List<ValidationMessage> errors, Element element) throws FHIRException, IOException {
    ValidationProfileSet profileSet = new ValidationProfileSet();
    validate(appContext, errors, element, profileSet);
  }

  private void validateRemainder(Object appContext, List<ValidationMessage> errors) throws IOException, FHIRException {
    boolean processedResource;
    do {
      processedResource = false;
      Set<Element> keys = new HashSet<Element>();
      keys.addAll(resourceProfilesMap.keySet());
      for (Element resource : keys) {
        ResourceProfiles rp = resourceProfilesMap.get(resource);
        if (rp.hasUncheckedProfiles()) {
          processedResource = true;
          start(appContext, errors, rp.getOwner(), resource, null, rp.getStack());
        }
      }
    } while (processedResource);
  }
  
  @Override
  public void validate(Object appContext, List<ValidationMessage> errors, Element element, String profile) throws FHIRException, IOException {
    validate(appContext, errors, element, new ValidationProfileSet(profile, true));
  }

  @Override
  public void validate(Object appContext, List<ValidationMessage> errors, Element element, StructureDefinition profile) throws FHIRException, IOException {
    validate(appContext, errors, element, new ValidationProfileSet(profile));
  }

  @Override
  public void validate(Object appContext, List<ValidationMessage> errors, Element element, ValidationProfileSet profiles) throws FHIRException, IOException {
    // this is the main entry point; all the other entry points end up here coming here...
    long t = System.nanoTime();
    boolean isRoot = false;
    if (resourceProfilesMap == null) {
      resourceProfilesMap = new HashMap<Element, ResourceProfiles>();
      isRoot = true;
    }
    validateResource(appContext, errors, element, element, null, profiles, resourceIdRule, new NodeStack(element));
    if (isRoot) {
      validateRemainder(appContext, errors);
      resourceProfilesMap = null;
    }
    overall = System.nanoTime() - t;
  }


  private boolean check(String v1, String v2) {
    return v1 == null ? Utilities.noString(v1) : v1.equals(v2);
  }

  private void checkAddress(List<ValidationMessage> errors, String path, Element focus, Address fixed) {
    checkFixedValue(errors, path + ".use", focus.getNamedChild("use"), fixed.getUseElement(), "use", focus);
    checkFixedValue(errors, path + ".text", focus.getNamedChild("text"), fixed.getTextElement(), "text", focus);
    checkFixedValue(errors, path + ".city", focus.getNamedChild("city"), fixed.getCityElement(), "city", focus);
    checkFixedValue(errors, path + ".state", focus.getNamedChild("state"), fixed.getStateElement(), "state", focus);
    checkFixedValue(errors, path + ".country", focus.getNamedChild("country"), fixed.getCountryElement(), "country", focus);
    checkFixedValue(errors, path + ".zip", focus.getNamedChild("zip"), fixed.getPostalCodeElement(), "postalCode", focus);

    List<Element> lines = new ArrayList<Element>();
    focus.getNamedChildren("line", lines);
    if (rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, lines.size() == fixed.getLine().size(),
        "Expected " + Integer.toString(fixed.getLine().size()) + " but found " + Integer.toString(lines.size()) + " line elements")) {
      for (int i = 0; i < lines.size(); i++)
        checkFixedValue(errors, path + ".coding", lines.get(i), fixed.getLine().get(i), "coding", focus);
    }
  }

  private void checkAttachment(List<ValidationMessage> errors, String path, Element focus, Attachment fixed) {
    checkFixedValue(errors, path + ".contentType", focus.getNamedChild("contentType"), fixed.getContentTypeElement(), "contentType", focus);
    checkFixedValue(errors, path + ".language", focus.getNamedChild("language"), fixed.getLanguageElement(), "language", focus);
    checkFixedValue(errors, path + ".data", focus.getNamedChild("data"), fixed.getDataElement(), "data", focus);
    checkFixedValue(errors, path + ".url", focus.getNamedChild("url"), fixed.getUrlElement(), "url", focus);
    checkFixedValue(errors, path + ".size", focus.getNamedChild("size"), fixed.getSizeElement(), "size", focus);
    checkFixedValue(errors, path + ".hash", focus.getNamedChild("hash"), fixed.getHashElement(), "hash", focus);
    checkFixedValue(errors, path + ".title", focus.getNamedChild("title"), fixed.getTitleElement(), "title", focus);
  }

  // public API

  private boolean checkCode(List<ValidationMessage> errors, Element element, String path, String code, String system, String display) throws TerminologyServiceException {
    long t = System.nanoTime();
    boolean ss = context.supportsSystem(system);
    txTime = txTime + (System.nanoTime() - t);
    if (ss) {
      t = System.nanoTime();
      ValidationResult s = context.validateCode(system, code, display);
      txTime = txTime + (System.nanoTime() - t);
      if (s == null || s.isOk())
        return true;
      if (s.getSeverity() == IssueSeverity.INFORMATION)
        hint(errors, IssueType.CODEINVALID, element.line(), element.col(), path, s == null, s.getMessage());
      else if (s.getSeverity() == IssueSeverity.WARNING)
        warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, s == null, s.getMessage());
      else
        return rule(errors, IssueType.CODEINVALID, element.line(), element.col(), path, s == null, s.getMessage());
      return true;
    } else if (system.startsWith("http://hl7.org/fhir")) {
      if (system.equals("http://hl7.org/fhir/sid/icd-10"))
        return true; // else don't check ICD-10 (for now)
      else {
        CodeSystem cs = getCodeSystem(system);
        if (warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, cs != null, "Unknown Code System " + system)) {
          ConceptDefinitionComponent def = getCodeDefinition(cs, code);
          if (warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, def != null, "Unknown Code (" + system + "#" + code + ")"))
            return warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, display == null || display.equals(def.getDisplay()), "Display should be '" + def.getDisplay() + "'");
        }
        return false;
      }
    } else if (system.startsWith("http://loinc.org")) {
      return true;
    } else if (system.startsWith("http://unitsofmeasure.org")) {
      return true;
    } else
      return true;
  }

  private void checkCodeableConcept(List<ValidationMessage> errors, String path, Element focus, CodeableConcept fixed) {
    checkFixedValue(errors, path + ".text", focus.getNamedChild("text"), fixed.getTextElement(), "text", focus);
    List<Element> codings = new ArrayList<Element>();
    focus.getNamedChildren("coding", codings);
    if (rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, codings.size() == fixed.getCoding().size(),
        "Expected " + Integer.toString(fixed.getCoding().size()) + " but found " + Integer.toString(codings.size()) + " coding elements")) {
      for (int i = 0; i < codings.size(); i++)
        checkFixedValue(errors, path + ".coding", codings.get(i), fixed.getCoding().get(i), "coding", focus);
    }
  }

  private void checkCodeableConcept(List<ValidationMessage> errors, String path, Element element, StructureDefinition profile, ElementDefinition theElementCntext)  {
    if (!noTerminologyChecks && theElementCntext != null && theElementCntext.hasBinding()) {
      ElementDefinitionBindingComponent binding = theElementCntext.getBinding();
      if (warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, binding != null, "Binding for " + path + " missing (cc)")) {
        if (binding.hasValueSet() && binding.getValueSet() instanceof Reference) {
          ValueSet valueset = resolveBindingReference(profile, binding.getValueSet(), profile.getUrl());
          if (warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, valueset != null, "ValueSet " + describeReference(binding.getValueSet()) + " not found")) {
            try {
              CodeableConcept cc = readAsCodeableConcept(element);
              if (!cc.hasCoding()) {
                if (binding.getStrength() == BindingStrength.REQUIRED)
                  rule(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "No code provided, and a code is required from the value set " + describeReference(binding.getValueSet()) + " (" + valueset.getUrl());
                else if (binding.getStrength() == BindingStrength.EXTENSIBLE) 
                  warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "No code provided, and a code should be provided from the value set " + describeReference(binding.getValueSet()) + " (" + valueset.getUrl());
              } else {
                long t = System.nanoTime();

                // Check whether the codes are appropriate for the type of binding we have
                boolean bindingsOk = true;
                if (binding.getStrength() != BindingStrength.EXAMPLE) {
                  boolean atLeastOneSystemIsSupported = false;
                  for (Coding nextCoding : cc.getCoding()) {
                    String nextSystem = nextCoding.getSystem();
                    if (isNotBlank(nextSystem) && context.supportsSystem(nextSystem)) {
                      atLeastOneSystemIsSupported = true;
                      break;
                    }
                  }

                  if (!atLeastOneSystemIsSupported && binding.getStrength() == BindingStrength.EXAMPLE) {
                    // ignore this since we can't validate but it doesn't matter..
                  } else {
                    ValidationResult vr = context.validateCode(cc, valueset);
                    if (!vr.isOk()) {
                      bindingsOk = false;
                      if (vr.getErrorClass() != null && vr.getErrorClass().isInfrastructure()) {
                        if (binding.getStrength() == BindingStrength.REQUIRED)
                          warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "Could not confirm that the codes provided are in the value set " + describeReference(binding.getValueSet()) + " and a code from this value set is required (class = "+vr.getErrorClass().toString()+")");
                        else if (binding.getStrength() == BindingStrength.EXTENSIBLE) {
                          if (binding.hasExtension("http://hl7.org/fhir/StructureDefinition/elementdefinition-maxValueSet")) 
                            checkMaxValueSet(errors, path, element, profile, (Reference) binding.getExtensionsByUrl("http://hl7.org/fhir/StructureDefinition/elementdefinition-maxValueSet").get(0).getValue(), cc);
                          else
                            warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "Could not confirm that the codes provided are in the value set " + describeReference(binding.getValueSet()) + " and a code should come from this value set unless it has no suitable code (class = "+vr.getErrorClass().toString()+")");
                        } else if (binding.getStrength() == BindingStrength.PREFERRED)
                          hint(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false,  "Could not confirm that the codes provided are in the value set " + describeReference(binding.getValueSet()) + " and a code is recommended to come from this value set (class = "+vr.getErrorClass().toString()+")");
                      } else {
                        if (binding.getStrength() == BindingStrength.REQUIRED)
                          rule(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "None of the codes provided are in the value set " + describeReference(binding.getValueSet()) + " (" + valueset.getUrl()+", and a code from this value set is required) (codes = "+ccSummary(cc)+")");
                        else if (binding.getStrength() == BindingStrength.EXTENSIBLE) {
                          if (binding.hasExtension("http://hl7.org/fhir/StructureDefinition/elementdefinition-maxValueSet")) 
                            checkMaxValueSet(errors, path, element, profile, (Reference) binding.getExtensionsByUrl("http://hl7.org/fhir/StructureDefinition/elementdefinition-maxValueSet").get(0).getValue(), cc);
                          else
                            warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "None of the codes provided are in the value set " + describeReference(binding.getValueSet()) + " (" + valueset.getUrl() + ", and a code should come from this value set unless it has no suitable code) (codes = "+ccSummary(cc)+")");
                        } else if (binding.getStrength() == BindingStrength.PREFERRED)
                          hint(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false,  "None of the codes provided are in the value set " + describeReference(binding.getValueSet()) + " (" + valueset.getUrl() + ", and a code is recommended to come from this value set) (codes = "+ccSummary(cc)+")");
                      }
                    } else if (vr.getMessage()!=null)
                      warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, vr.getMessage());
                  }
                  // Then, for any codes that are in code systems we are able
                  // to validate, we'll validate that the codes actually exist
                  if (bindingsOk) {
                    for (Coding nextCoding : cc.getCoding()) {
                      String nextCode = nextCoding.getCode();
                      String nextSystem = nextCoding.getSystem();
                      if (isNotBlank(nextCode) && isNotBlank(nextSystem) && context.supportsSystem(nextSystem)) {
                        ValidationResult vr = context.validateCode(nextSystem, nextCode, null);
                        if (!vr.isOk()) {
                          warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "Code {0} is not a valid code in code system {1}", nextCode, nextSystem);
                        }
                      }
                    }
                  }
                  txTime = txTime + (System.nanoTime() - t);
                }
              }
            } catch (Exception e) {
              warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "Error "+e.getMessage()+" validating CodeableConcept");
            }
          }
        } else if (binding.hasValueSet()) {
          hint(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "Binding by URI reference cannot be checked");
        } else if (!noBindingMsgSuppressed) {
          hint(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "Binding for path " + path + " has no source, so can't be checked");
        }
      }
    }
  }

  private void checkMaxValueSet(List<ValidationMessage> errors, String path, Element element, StructureDefinition profile, Reference maxVSUrl, CodeableConcept cc) {
    // TODO Auto-generated method stub
    ValueSet valueset = resolveBindingReference(profile, maxVSUrl, profile.getUrl());
    if (warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, valueset != null, "ValueSet " + describeReference(maxVSUrl) + " not found")) {
      try {
        long t = System.nanoTime();
        ValidationResult vr = context.validateCode(cc, valueset);
        txTime = txTime + (System.nanoTime() - t);
        if (!vr.isOk()) {
          rule(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "None of the codes provided are in the maximum value set " + describeReference(maxVSUrl) + " (" + valueset.getUrl()+", and a code from this value set is required) (codes = "+ccSummary(cc)+")");
        }
      } catch (Exception e) {
        warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "Error "+e.getMessage()+" validating CodeableConcept using maxValueSet");
      }
    }
  }

  private void checkMaxValueSet(List<ValidationMessage> errors, String path, Element element, StructureDefinition profile, Reference maxVSUrl, Coding c) {
    // TODO Auto-generated method stub
    ValueSet valueset = resolveBindingReference(profile, maxVSUrl, profile.getUrl());
    if (warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, valueset != null, "ValueSet " + describeReference(maxVSUrl) + " not found")) {
      try {
        long t = System.nanoTime();
        ValidationResult vr = context.validateCode(c, valueset);
        txTime = txTime + (System.nanoTime() - t);
        if (!vr.isOk()) {
          rule(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "The code provided is not in the maximum value set " + describeReference(maxVSUrl) + " (" + valueset.getUrl()+", and a code from this value set is required) (code = "+c.getSystem()+"#"+c.getCode()+")");
        }
      } catch (Exception e) {
        warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "Error "+e.getMessage()+" validating CodeableConcept using maxValueSet");
      }
    }
  }

  private void checkMaxValueSet(List<ValidationMessage> errors, String path, Element element, StructureDefinition profile, Reference maxVSUrl, String value) {
    // TODO Auto-generated method stub
    ValueSet valueset = resolveBindingReference(profile, maxVSUrl, profile.getUrl());
    if (warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, valueset != null, "ValueSet " + describeReference(maxVSUrl) + " not found")) {
      try {
        long t = System.nanoTime();
        ValidationResult vr = context.validateCode(null, value, null, valueset);
        txTime = txTime + (System.nanoTime() - t);
        if (!vr.isOk()) {
          if (vr.getErrorClass() != null && vr.getErrorClass().isInfrastructure()) 
            warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "The code provided could not be validated against the maximum value set " + describeReference(maxVSUrl) + " (" + valueset.getUrl()+"), (error = "+vr.getMessage()+")");
          else
            rule(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "The code provided is not in the maximum value set " + describeReference(maxVSUrl) + " (" + valueset.getUrl()+"), and a code from this value set is required) (code = "+value+"), (error = "+vr.getMessage()+")");
        }
      } catch (Exception e) {
        warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "Error "+e.getMessage()+" validating CodeableConcept using maxValueSet");
      }
    }
  }

  private String ccSummary(CodeableConcept cc) {
    CommaSeparatedStringBuilder b = new CommaSeparatedStringBuilder();
    for (Coding c : cc.getCoding()) 
      b.append(c.getSystem()+"#"+c.getCode());
    return b.toString();
  }

  private CodeableConcept readAsCodeableConcept(Element element) {
    CodeableConcept cc = new CodeableConcept();
    List<Element> list = new ArrayList<Element>();
    element.getNamedChildren("coding", list);
    for (Element item : list)
      cc.addCoding(readAsCoding(item));
    cc.setText(element.getNamedChildValue("text"));
    return cc;
  }

  private Coding readAsCoding(Element item) {
    Coding c = new Coding();
    c.setSystem(item.getNamedChildValue("system"));
    c.setVersion(item.getNamedChildValue("version"));
    c.setCode(item.getNamedChildValue("code"));
    c.setDisplay(item.getNamedChildValue("display"));
    return c;
  }

  private void checkCoding(List<ValidationMessage> errors, String path, Element focus, Coding fixed) {
    checkFixedValue(errors, path + ".system", focus.getNamedChild("system"), fixed.getSystemElement(), "system", focus);
    checkFixedValue(errors, path + ".code", focus.getNamedChild("code"), fixed.getCodeElement(), "code", focus);
    checkFixedValue(errors, path + ".display", focus.getNamedChild("display"), fixed.getDisplayElement(), "display", focus);
    checkFixedValue(errors, path + ".userSelected", focus.getNamedChild("userSelected"), fixed.getUserSelectedElement(), "userSelected", focus);
  }

  private void checkCoding(List<ValidationMessage> errors, String path, Element element, StructureDefinition profile, ElementDefinition theElementCntext, boolean inCodeableConcept)  {
    String code = element.getNamedChildValue("code");
    String system = element.getNamedChildValue("system");
    String display = element.getNamedChildValue("display");
    rule(errors, IssueType.CODEINVALID, element.line(), element.col(), path, isAbsolute(system), "Coding.system must be an absolute reference, not a local reference");

    if (system != null && code != null && !noTerminologyChecks) {
      rule(errors, IssueType.CODEINVALID, element.line(), element.col(), path, !isValueSet(system), "The Coding references a value set, not a code system (\""+system+"\")");
      try {
        if (checkCode(errors, element, path, code, system, display))
          if (theElementCntext != null && theElementCntext.hasBinding()) {
            ElementDefinitionBindingComponent binding = theElementCntext.getBinding();
            if (warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, binding != null, "Binding for " + path + " missing")) {
              if (binding.hasValueSet() && binding.getValueSet() instanceof Reference) {
                ValueSet valueset = resolveBindingReference(profile, binding.getValueSet(), profile.getUrl());
                if (warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, valueset != null, "ValueSet " + describeReference(binding.getValueSet()) + " not found")) {
                  try {
                    Coding c = readAsCoding(element);
                    long t = System.nanoTime();
                    ValidationResult vr = context.validateCode(c, valueset);
                    txTime = txTime + (System.nanoTime() - t);
                    if (!vr.isOk()) {
                      if (vr.IsNoService())
                        hint(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false,  "The value provided could not be validated in the absence of a terminology server");
                      else if (vr.getErrorClass() != null && !vr.getErrorClass().isInfrastructure()) {
                        if (binding.getStrength() == BindingStrength.REQUIRED)
                          warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "Could not confirm that the codes provided are in the value set " + describeReference(binding.getValueSet()) + " (" + valueset.getUrl()+", and a code from this value set is required)"); 
                        else if (binding.getStrength() == BindingStrength.EXTENSIBLE) {
                          if (binding.hasExtension("http://hl7.org/fhir/StructureDefinition/elementdefinition-maxValueSet")) 
                            checkMaxValueSet(errors, path, element, profile, (Reference) binding.getExtensionsByUrl("http://hl7.org/fhir/StructureDefinition/elementdefinition-maxValueSet").get(0).getValue(), c);
                          else
                            warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "Could not confirm that the codes provided are in the value set " + describeReference(binding.getValueSet()) + " (" + valueset.getUrl() + ", and a code should come from this value set unless it has no suitable code)");
                        } else if (binding.getStrength() == BindingStrength.PREFERRED)
                          hint(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false,  "Could not confirm that the codes provided are in the value set " + describeReference(binding.getValueSet()) + " (" + valueset.getUrl() + ", and a code is recommended to come from this value set)");
                      } else if (binding.getStrength() == BindingStrength.REQUIRED)
                        rule(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "The Coding provided is not in the value set " + describeReference(binding.getValueSet()) + " (" + valueset.getUrl() + ", and a code is required from this value set)"+(vr.getMessage() != null ? " (error message = "+vr.getMessage()+")" : ""));
                      else if (binding.getStrength() == BindingStrength.EXTENSIBLE) {
                        if (binding.hasExtension("http://hl7.org/fhir/StructureDefinition/elementdefinition-maxValueSet")) 
                          checkMaxValueSet(errors, path, element, profile, (Reference) binding.getExtensionsByUrl("http://hl7.org/fhir/StructureDefinition/elementdefinition-maxValueSet").get(0).getValue(), c);
                        else
                          warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "The Coding provided is not in the value set " + describeReference(binding.getValueSet()) + " (" + valueset.getUrl() + ", and a code should come from this value set unless it has no suitable code)"+(vr.getMessage() != null ? " (error message = "+vr.getMessage()+")" : ""));
                      } else if (binding.getStrength() == BindingStrength.PREFERRED)
                        hint(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false,  "The Coding provided is not in the value set " + describeReference(binding.getValueSet()) + " (" + valueset.getUrl() + ", and a code is recommended to come from this value set)"+(vr.getMessage() != null ? " (error message = "+vr.getMessage()+")" : ""));
                    }
                  } catch (Exception e) {
                    warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "Error "+e.getMessage()+" validating Coding");
                  }
                }
              } else if (binding.hasValueSet()) {
                hint(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "Binding by URI reference cannot be checked");
              } else if (!inCodeableConcept && !noBindingMsgSuppressed) {
                hint(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "Binding for path " + path + " has no source, so can't be checked");
              }
            }
          }
      } catch (Exception e) {
        rule(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "Error "+e.getMessage()+" validating Coding");
      }
    }
  }

  private boolean isValueSet(String url) {
    try {
      ValueSet vs = context.fetchResourceWithException(ValueSet.class, url);
      return vs != null;
    } catch (Exception e) {
      return false;
    }
  }

  private void checkContactPoint(List<ValidationMessage> errors, String path, Element focus, ContactPoint fixed) {
    checkFixedValue(errors, path + ".system", focus.getNamedChild("system"), fixed.getSystemElement(), "system", focus);
    checkFixedValue(errors, path + ".value", focus.getNamedChild("value"), fixed.getValueElement(), "value", focus);
    checkFixedValue(errors, path + ".use", focus.getNamedChild("use"), fixed.getUseElement(), "use", focus);
    checkFixedValue(errors, path + ".period", focus.getNamedChild("period"), fixed.getPeriod(), "period", focus);

  }

  protected void checkDeclaredProfiles(ResourceProfiles resourceProfiles, List<ValidationMessage> errors, Element resource, Element element, NodeStack stack) throws FHIRException {
    Element meta = element.getNamedChild("meta");
    if (meta != null) {
      List<Element> profiles = new ArrayList<Element>();
      meta.getNamedChildren("profile", profiles);
      int i = 0;
      for (Element profile : profiles) {
        String ref = profile.primitiveValue();
        String p = stack.addToLiteralPath("meta", "profile", ":" + Integer.toString(i));
        if (rule(errors, IssueType.INVALID, element.line(), element.col(), p, !Utilities.noString(ref), "StructureDefinition reference invalid")) {
          long t = System.nanoTime();
          resourceProfiles.addProfile(errors, ref, errorForUnknownProfiles, p, element);
          i++;
        }
      }
    }
  }

  private StructureDefinition checkExtension(Object appContext, List<ValidationMessage> errors, String path, Element resource, Element element, ElementDefinition def, StructureDefinition profile, NodeStack stack) throws FHIRException, IOException {
    String url = element.getNamedChildValue("url");
    boolean isModifier = element.getName().equals("modifierExtension");

    long t = System.nanoTime();
    StructureDefinition ex = context.fetchResource(StructureDefinition.class, url);
    sdTime = sdTime + (System.nanoTime() - t);
    if (ex == null) {
      if (rule(errors, IssueType.STRUCTURE, element.line(), element.col(), path, allowUnknownExtension(url), "The extension " + url + " is unknown, and not allowed here"))
        hint(errors, IssueType.STRUCTURE, element.line(), element.col(), path, isKnownExtension(url), "Unknown extension " + url);
    } else {
      if (def.getIsModifier())
        rule(errors, IssueType.STRUCTURE, element.line(), element.col(), path + "[url='" + url + "']", ex.getSnapshot().getElement().get(0).getIsModifier(),
            "Extension modifier mismatch: the extension element is labelled as a modifier, but the underlying extension is not");
      else
        rule(errors, IssueType.STRUCTURE, element.line(), element.col(), path + "[url='" + url + "']", !ex.getSnapshot().getElement().get(0).getIsModifier(),
            "Extension modifier mismatch: the extension element is not labelled as a modifier, but the underlying extension is");

      // two questions
      // 1. can this extension be used here?
      checkExtensionContext(errors, element, /* path+"[url='"+url+"']", */ ex, stack, ex.getUrl());

      if (isModifier)
        rule(errors, IssueType.STRUCTURE, element.line(), element.col(), path + "[url='" + url + "']", ex.getSnapshot().getElement().get(0).getIsModifier(),
            "The Extension '" + url + "' must be used as a modifierExtension");
      else
        rule(errors, IssueType.STRUCTURE, element.line(), element.col(), path + "[url='" + url + "']", !ex.getSnapshot().getElement().get(0).getIsModifier(),
            "The Extension '" + url + "' must not be used as an extension (it's a modifierExtension)");

      // check the type of the extension:
      Set<String> allowedTypes = listExtensionTypes(ex);
      String actualType = getExtensionType(element);
      if (actualType == null) 
        rule(errors, IssueType.STRUCTURE, element.line(), element.col(), path + "[url='" + url + "']", allowedTypes.isEmpty(), "The Extension '" + url + "' definition is for a complex extension, so it cannot contain a value");
      else 
        rule(errors, IssueType.STRUCTURE, element.line(), element.col(), path + "[url='" + url + "']", allowedTypes.contains(actualType), "The Extension '" + url + "' definition allows for the types "+allowedTypes.toString()+" but found type "+actualType);      
      
      // 3. is the content of the extension valid?
      validateElement(appContext, errors, ex, ex.getSnapshot().getElement().get(0), null, null, resource, element, "Extension", stack, false);

    }
    return ex;
  }

  private String getExtensionType(Element element) {
    for (Element e : element.getChildren()) {
      if (e.getName().startsWith("value")) {
        String tn = e.getName().substring(5);
        String ltn = Utilities.uncapitalize(tn);
        if (isPrimitiveType(ltn))
          return ltn;
        else
          return tn;
      }
    }
    return null;
  }

  private Set<String> listExtensionTypes(StructureDefinition ex) {
    ElementDefinition vd = null;
    for (ElementDefinition ed : ex.getSnapshot().getElement()) {
      if (ed.getPath().startsWith("Extension.value")) {
        vd = ed;
        break;
      }
    }
    Set<String> res = new HashSet<String>();
    if (vd != null && !"0".equals(vd.getMax())) {
      for (TypeRefComponent tr : vd.getType()) {
        res.add(tr.getCode());
      }
    }
    return res;
  }

  private boolean checkExtensionContext(List<ValidationMessage> errors, Element element, StructureDefinition definition, NodeStack stack, String extensionParent) {
    String extUrl = definition.getUrl();
    CommaSeparatedStringBuilder p = new CommaSeparatedStringBuilder();
    for (String lp : stack.getLogicalPaths())
      p.append(lp);
    if (definition.getContextType() == ExtensionContext.DATATYPE) {
      boolean ok = false;
      CommaSeparatedStringBuilder b = new CommaSeparatedStringBuilder();
      for (StringType ct : definition.getContext()) {
        b.append(ct.getValue());
        if (ct.getValue().equals("*") || stack.getLogicalPaths().contains(ct.getValue() + ".extension"))
          ok = true;
      }
      return rule(errors, IssueType.STRUCTURE, element.line(), element.col(), stack.getLiteralPath(), ok,
          "The extension " + extUrl + " is not allowed to be used on the logical path set [" + p.toString() + "] (allowed: datatype=" + b.toString() + ")");
    } else if (definition.getContextType() == ExtensionContext.EXTENSION) {
      boolean ok = false;
      for (StringType ct : definition.getContext())
        if (ct.getValue().equals("*") || ct.getValue().equals(extensionParent))
          ok = true;
      return rule(errors, IssueType.STRUCTURE, element.line(), element.col(), stack.getLiteralPath(), ok,
          "The extension " + extUrl + " is not allowed to be used with the extension '" + extensionParent + "'");
    } else if (definition.getContextType() == ExtensionContext.RESOURCE) {
      boolean ok = false;
      // String simplePath = container.getPath();
      // System.out.println(simplePath);
      // if (effetive.endsWith(".extension") || simplePath.endsWith(".modifierExtension"))
      // simplePath = simplePath.substring(0, simplePath.lastIndexOf('.'));
      CommaSeparatedStringBuilder b = new CommaSeparatedStringBuilder();
      for (StringType ct : definition.getContext()) {
        String c = ct.getValue();
        b.append(c);
        if (c.equals("*") || stack.getLogicalPaths().contains(c + ".extension") || (c.startsWith("@") && stack.getLogicalPaths().contains(c.substring(1) + ".extension")))
          ;
        ok = true;
      }
      return rule(errors, IssueType.STRUCTURE, element.line(), element.col(), stack.getLiteralPath(), ok,
          "The extension " + extUrl + " is not allowed to be used on the logical path set " + p.toString() + " (allowed: resource=" + b.toString() + ")");
    } else
      throw new Error("Unknown context type");
  }
  //
  // private String simplifyPath(String path) {
  // String s = path.replace("/f:", ".");
  // while (s.contains("["))
  // s = s.substring(0, s.indexOf("["))+s.substring(s.indexOf("]")+1);
  // String[] parts = s.split("\\.");
  // int i = 0;
  // while (i < parts.length && !context.getProfiles().containsKey(parts[i].toLowerCase()))
  // i++;
  // if (i >= parts.length)
  // throw new Error("Unable to process part "+path);
  // int j = parts.length - 1;
  // while (j > 0 && (parts[j].equals("extension") || parts[j].equals("modifierExtension")))
  // j--;
  // StringBuilder b = new StringBuilder();
  // boolean first = true;
  // for (int k = i; k <= j; k++) {
  // if (k == j || !parts[k].equals(parts[k+1])) {
  // if (first)
  // first = false;
  // else
  // b.append(".");
  // b.append(parts[k]);
  // }
  // }
  // return b.toString();
  // }
  //

  private void checkFixedValue(List<ValidationMessage> errors, String path, Element focus, org.hl7.fhir.dstu3.model.Element fixed, String propName, Element parent) {
    if ((fixed == null || fixed.isEmpty()) && focus == null)
      ; // this is all good
    else if (fixed == null && focus != null)
      rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, false, "Unexpected element " + focus.getName());
    else if (fixed != null && !fixed.isEmpty() && focus == null)
      rule(errors, IssueType.VALUE, parent == null ? -1 : parent.line(), parent == null ? -1 : parent.col(), path, false, "Missing element '" + propName+"'");
    else {
      String value = focus.primitiveValue();
      if (fixed instanceof org.hl7.fhir.dstu3.model.BooleanType)
        rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, check(((org.hl7.fhir.dstu3.model.BooleanType) fixed).asStringValue(), value),
            "Value is '" + value + "' but must be '" + ((org.hl7.fhir.dstu3.model.BooleanType) fixed).asStringValue() + "'");
      else if (fixed instanceof org.hl7.fhir.dstu3.model.IntegerType)
        rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, check(((org.hl7.fhir.dstu3.model.IntegerType) fixed).asStringValue(), value),
            "Value is '" + value + "' but must be '" + ((org.hl7.fhir.dstu3.model.IntegerType) fixed).asStringValue() + "'");
      else if (fixed instanceof org.hl7.fhir.dstu3.model.DecimalType)
        rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, check(((org.hl7.fhir.dstu3.model.DecimalType) fixed).asStringValue(), value),
            "Value is '" + value + "' but must be '" + ((org.hl7.fhir.dstu3.model.DecimalType) fixed).asStringValue() + "'");
      else if (fixed instanceof org.hl7.fhir.dstu3.model.Base64BinaryType)
        rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, check(((org.hl7.fhir.dstu3.model.Base64BinaryType) fixed).asStringValue(), value),
            "Value is '" + value + "' but must be '" + ((org.hl7.fhir.dstu3.model.Base64BinaryType) fixed).asStringValue() + "'");
      else if (fixed instanceof org.hl7.fhir.dstu3.model.InstantType)
        rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, check(((org.hl7.fhir.dstu3.model.InstantType) fixed).getValue().toString(), value),
            "Value is '" + value + "' but must be '" + ((org.hl7.fhir.dstu3.model.InstantType) fixed).asStringValue() + "'");
      else if (fixed instanceof org.hl7.fhir.dstu3.model.StringType)
        rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, check(((org.hl7.fhir.dstu3.model.StringType) fixed).getValue(), value),
            "Value is '" + value + "' but must be '" + ((org.hl7.fhir.dstu3.model.StringType) fixed).getValue() + "'");
      else if (fixed instanceof org.hl7.fhir.dstu3.model.UriType)
        rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, check(((org.hl7.fhir.dstu3.model.UriType) fixed).getValue(), value),
            "Value is '" + value + "' but must be '" + ((org.hl7.fhir.dstu3.model.UriType) fixed).getValue() + "'");
      else if (fixed instanceof org.hl7.fhir.dstu3.model.DateType)
        rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, check(((org.hl7.fhir.dstu3.model.DateType) fixed).getValue().toString(), value),
            "Value is '" + value + "' but must be '" + ((org.hl7.fhir.dstu3.model.DateType) fixed).getValue() + "'");
      else if (fixed instanceof org.hl7.fhir.dstu3.model.DateTimeType)
        rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, check(((org.hl7.fhir.dstu3.model.DateTimeType) fixed).getValue().toString(), value),
            "Value is '" + value + "' but must be '" + ((org.hl7.fhir.dstu3.model.DateTimeType) fixed).getValue() + "'");
      else if (fixed instanceof org.hl7.fhir.dstu3.model.OidType)
        rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, check(((org.hl7.fhir.dstu3.model.OidType) fixed).getValue(), value),
            "Value is '" + value + "' but must be '" + ((org.hl7.fhir.dstu3.model.OidType) fixed).getValue() + "'");
      else if (fixed instanceof org.hl7.fhir.dstu3.model.UuidType)
        rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, check(((org.hl7.fhir.dstu3.model.UuidType) fixed).getValue(), value),
            "Value is '" + value + "' but must be '" + ((org.hl7.fhir.dstu3.model.UuidType) fixed).getValue() + "'");
      else if (fixed instanceof org.hl7.fhir.dstu3.model.CodeType)
        rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, check(((org.hl7.fhir.dstu3.model.CodeType) fixed).getValue(), value),
            "Value is '" + value + "' but must be '" + ((org.hl7.fhir.dstu3.model.CodeType) fixed).getValue() + "'");
      else if (fixed instanceof org.hl7.fhir.dstu3.model.IdType)
        rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, check(((org.hl7.fhir.dstu3.model.IdType) fixed).getValue(), value),
            "Value is '" + value + "' but must be '" + ((org.hl7.fhir.dstu3.model.IdType) fixed).getValue() + "'");
      else if (fixed instanceof Quantity)
        checkQuantity(errors, path, focus, (Quantity) fixed);
      else if (fixed instanceof Address)
        checkAddress(errors, path, focus, (Address) fixed);
      else if (fixed instanceof ContactPoint)
        checkContactPoint(errors, path, focus, (ContactPoint) fixed);
      else if (fixed instanceof Attachment)
        checkAttachment(errors, path, focus, (Attachment) fixed);
      else if (fixed instanceof Identifier)
        checkIdentifier(errors, path, focus, (Identifier) fixed);
      else if (fixed instanceof Coding)
        checkCoding(errors, path, focus, (Coding) fixed);
      else if (fixed instanceof HumanName)
        checkHumanName(errors, path, focus, (HumanName) fixed);
      else if (fixed instanceof CodeableConcept)
        checkCodeableConcept(errors, path, focus, (CodeableConcept) fixed);
      else if (fixed instanceof Timing)
        checkTiming(errors, path, focus, (Timing) fixed);
      else if (fixed instanceof Period)
        checkPeriod(errors, path, focus, (Period) fixed);
      else if (fixed instanceof Range)
        checkRange(errors, path, focus, (Range) fixed);
      else if (fixed instanceof Ratio)
        checkRatio(errors, path, focus, (Ratio) fixed);
      else if (fixed instanceof SampledData)
        checkSampledData(errors, path, focus, (SampledData) fixed);

      else
        rule(errors, IssueType.EXCEPTION, focus.line(), focus.col(), path, false, "Unhandled fixed value type " + fixed.getClass().getName());
      List<Element> extensions = new ArrayList<Element>();
      focus.getNamedChildren("extension", extensions);
      if (fixed.getExtension().size() == 0) {
        rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, extensions.size() == 0, "No extensions allowed");
      } else if (rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, extensions.size() == fixed.getExtension().size(),
          "Extensions count mismatch: expected " + Integer.toString(fixed.getExtension().size()) + " but found " + Integer.toString(extensions.size()))) {
        for (Extension e : fixed.getExtension()) {
          Element ex = getExtensionByUrl(extensions, e.getUrl());
          if (rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, ex != null, "Extension count mismatch: unable to find extension: " + e.getUrl())) {
            checkFixedValue(errors, path, ex.getNamedChild("extension").getNamedChild("value"), e.getValue(), "extension.value", ex.getNamedChild("extension"));
          }
        }
      }
    }
  }

  private void checkHumanName(List<ValidationMessage> errors, String path, Element focus, HumanName fixed) {
    checkFixedValue(errors, path + ".use", focus.getNamedChild("use"), fixed.getUseElement(), "use", focus);
    checkFixedValue(errors, path + ".text", focus.getNamedChild("text"), fixed.getTextElement(), "text", focus);
    checkFixedValue(errors, path + ".period", focus.getNamedChild("period"), fixed.getPeriod(), "period", focus);

    List<Element> parts = new ArrayList<Element>();
    focus.getNamedChildren("family", parts);
    if (rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, parts.size() > 0 == fixed.hasFamily(),
        "Expected " + (fixed.hasFamily() ? "1" : "0") + " but found " + Integer.toString(parts.size()) + " family elements")) {
      for (int i = 0; i < parts.size(); i++)
        checkFixedValue(errors, path + ".family", parts.get(i), fixed.getFamilyElement(), "family", focus);
    }
    focus.getNamedChildren("given", parts);
    if (rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, parts.size() == fixed.getGiven().size(),
        "Expected " + Integer.toString(fixed.getGiven().size()) + " but found " + Integer.toString(parts.size()) + " given elements")) {
      for (int i = 0; i < parts.size(); i++)
        checkFixedValue(errors, path + ".given", parts.get(i), fixed.getGiven().get(i), "given", focus);
    }
    focus.getNamedChildren("prefix", parts);
    if (rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, parts.size() == fixed.getPrefix().size(),
        "Expected " + Integer.toString(fixed.getPrefix().size()) + " but found " + Integer.toString(parts.size()) + " prefix elements")) {
      for (int i = 0; i < parts.size(); i++)
        checkFixedValue(errors, path + ".prefix", parts.get(i), fixed.getPrefix().get(i), "prefix", focus);
    }
    focus.getNamedChildren("suffix", parts);
    if (rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, parts.size() == fixed.getSuffix().size(),
        "Expected " + Integer.toString(fixed.getSuffix().size()) + " but found " + Integer.toString(parts.size()) + " suffix elements")) {
      for (int i = 0; i < parts.size(); i++)
        checkFixedValue(errors, path + ".suffix", parts.get(i), fixed.getSuffix().get(i), "suffix", focus);
    }
  }

  private void checkIdentifier(List<ValidationMessage> errors, String path, Element element, ElementDefinition context) {
    String system = element.getNamedChildValue("system");
    rule(errors, IssueType.CODEINVALID, element.line(), element.col(), path, isAbsolute(system), "Identifier.system must be an absolute reference, not a local reference");
  }

  private void checkIdentifier(List<ValidationMessage> errors, String path, Element focus, Identifier fixed) {
    checkFixedValue(errors, path + ".use", focus.getNamedChild("use"), fixed.getUseElement(), "use", focus);
    checkFixedValue(errors, path + ".type", focus.getNamedChild("type"), fixed.getType(), "type", focus);
    checkFixedValue(errors, path + ".system", focus.getNamedChild("system"), fixed.getSystemElement(), "system", focus);
    checkFixedValue(errors, path + ".value", focus.getNamedChild("value"), fixed.getValueElement(), "value", focus);
    checkFixedValue(errors, path + ".period", focus.getNamedChild("period"), fixed.getPeriod(), "period", focus);
    checkFixedValue(errors, path + ".assigner", focus.getNamedChild("assigner"), fixed.getAssigner(), "assigner", focus);
  }

  private void checkPeriod(List<ValidationMessage> errors, String path, Element focus, Period fixed) {
    checkFixedValue(errors, path + ".start", focus.getNamedChild("start"), fixed.getStartElement(), "start", focus);
    checkFixedValue(errors, path + ".end", focus.getNamedChild("end"), fixed.getEndElement(), "end", focus);
  }

  private void checkPrimitive(Object appContext, List<ValidationMessage> errors, String path, String type, ElementDefinition context, Element e, StructureDefinition profile) throws FHIRException, IOException {
    if (isBlank(e.primitiveValue())) {
      rule(errors, IssueType.INVALID, e.line(), e.col(), path, e.hasChildren(), "primitive types must have a value or must have child extensions");
      return;
    }
    String regex = context.getExtensionString(ToolingExtensions.EXT_REGEX);
    if (regex!=null)
        rule(errors, IssueType.INVALID, e.line(), e.col(), path, e.primitiveValue().matches(regex), "Element value '" + e.primitiveValue() + "' does not meet regex '" + regex + "'");
    
    if (type.equals("boolean")) { 
      rule(errors, IssueType.INVALID, e.line(), e.col(), path, "true".equals(e.primitiveValue()) || "false".equals(e.primitiveValue()), "boolean values must be 'true' or 'false'");
    }
    if (type.equals("uri")) {
      rule(errors, IssueType.INVALID, e.line(), e.col(), path, !e.primitiveValue().startsWith("oid:"), "URI values cannot start with oid:");
      rule(errors, IssueType.INVALID, e.line(), e.col(), path, !e.primitiveValue().startsWith("uuid:"), "URI values cannot start with uuid:");
      rule(errors, IssueType.INVALID, e.line(), e.col(), path, e.primitiveValue().equals(e.primitiveValue().trim()), "URI values cannot have leading or trailing whitespace");
      rule(errors, IssueType.INVALID, e.line(), e.col(), path, !context.hasMaxLength() || context.getMaxLength()==0 ||  e.primitiveValue().length() <= context.getMaxLength(), "value is longer than permitted maximum length of " + context.getMaxLength());
      
      // now, do we check the URI target? 
      if (fetcher != null) {
        rule(errors, IssueType.INVALID, e.line(), e.col(), path, fetcher.resolveURL(appContext, path, e.primitiveValue()), "URL value '"+e.primitiveValue()+"' does not resolve");
      }
    }
    if (type.equalsIgnoreCase("string") && e.hasPrimitiveValue()) {
      if (rule(errors, IssueType.INVALID, e.line(), e.col(), path, e.primitiveValue() == null || e.primitiveValue().length() > 0, "@value cannot be empty")) {
        warning(errors, IssueType.INVALID, e.line(), e.col(), path, e.primitiveValue() == null || e.primitiveValue().trim().equals(e.primitiveValue()), "value should not start or finish with whitespace");
        rule(errors, IssueType.INVALID, e.line(), e.col(), path, !context.hasMaxLength() || context.getMaxLength()==0 ||  e.primitiveValue().length() <= context.getMaxLength(), "value is longer than permitted maximum length of " + context.getMaxLength());
      }
    }
    if (type.equals("dateTime")) {
      rule(errors, IssueType.INVALID, e.line(), e.col(), path, yearIsValid(e.primitiveValue()), "The value '" + e.primitiveValue() + "' does not have a valid year");
      rule(errors, IssueType.INVALID, e.line(), e.col(), path,
          e.primitiveValue()
          .matches("-?[0-9]{4}(-(0[1-9]|1[0-2])(-(0[0-9]|[1-2][0-9]|3[0-1])(T([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9](\\.[0-9]+)?(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?)?)?)?"),
          "Not a valid date time");
      rule(errors, IssueType.INVALID, e.line(), e.col(), path, !hasTime(e.primitiveValue()) || hasTimeZone(e.primitiveValue()), "if a date has a time, it must have a timezone");
      rule(errors, IssueType.INVALID, e.line(), e.col(), path, !context.hasMaxLength() || context.getMaxLength()==0 ||  e.primitiveValue().length() <= context.getMaxLength(), "value is longer than permitted maximum length of " + context.getMaxLength());
    }
    if (type.equals("date")) {
        rule(errors, IssueType.INVALID, e.line(), e.col(), path, yearIsValid(e.primitiveValue()), "The value '" + e.primitiveValue() + "' does not have a valid year");
        rule(errors, IssueType.INVALID, e.line(), e.col(), path, e.primitiveValue().matches("-?[0-9]{4}(-(0[1-9]|1[0-2])(-(0[0-9]|[1-2][0-9]|3[0-1]))?)?"),
            "Not a valid date");
        rule(errors, IssueType.INVALID, e.line(), e.col(), path, !context.hasMaxLength() || context.getMaxLength()==0 ||  e.primitiveValue().length() <= context.getMaxLength(), "value is longer than permitted maximum value of " + context.getMaxLength());
    }
    if (type.equals("integer")) {
      rule(errors, IssueType.INVALID, e.line(), e.col(), path, !context.hasMaxValueIntegerType() || !context.getMaxValueIntegerType().hasValue() || (context.getMaxValueIntegerType().getValue() >= new Integer(e.getValue()).intValue()), "value is greater than permitted maximum value of " + (context.hasMaxValueIntegerType() ? context.getMaxValueIntegerType() : ""));
      rule(errors, IssueType.INVALID, e.line(), e.col(), path, !context.hasMinValueIntegerType() || !context.getMinValueIntegerType().hasValue() || (context.getMinValueIntegerType().getValue() <= new Integer(e.getValue()).intValue()), "value is less than permitted minimum value of " + (context.hasMinValueIntegerType() ? context.getMinValueIntegerType() : ""));
    }
    if (type.equals("instant")) {
      rule(errors, IssueType.INVALID, e.line(), e.col(), path,
          e.primitiveValue().matches("-?[0-9]{4}-(0[1-9]|1[0-2])-(0[0-9]|[1-2][0-9]|3[0-1])T([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9](\\.[0-9]+)?(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))"),
          "The instant '" + e.primitiveValue() + "' is not valid (by regex)");
      rule(errors, IssueType.INVALID, e.line(), e.col(), path, yearIsValid(e.primitiveValue()), "The value '" + e.primitiveValue() + "' does not have a valid year");
    }

    if (type.equals("code") && e.primitiveValue() != null) {
      // Technically, a code is restricted to string which has at least one character and no leading or trailing whitespace, and where there is no whitespace
      // other than single spaces in the contents
      rule(errors, IssueType.INVALID, e.line(), e.col(), path, passesCodeWhitespaceRules(e.primitiveValue()), "The code '" + e.primitiveValue() + "' is not valid (whitespace rules)");
      rule(errors, IssueType.INVALID, e.line(), e.col(), path, !context.hasMaxLength() || context.getMaxLength()==0 ||  e.primitiveValue().length() <= context.getMaxLength(), "value is longer than permitted maximum length of " + context.getMaxLength());
    }

    if (context.hasBinding() && e.primitiveValue() != null) {
      checkPrimitiveBinding(errors, path, type, context, e, profile);
    }

    if (type.equals("xhtml")) {
      XhtmlNode xhtml = e.getXhtml();
      if (xhtml != null) { // if it is null, this is an error already noted in the parsers
        // check that the namespace is there and correct.
        String ns = xhtml.getNsDecl();
        rule(errors, IssueType.INVALID, e.line(), e.col(), path, FormatUtilities.XHTML_NS.equals(ns), "Wrong namespace on the XHTML ('"+ns+"')");
        // check that inner namespaces are all correct 
        checkInnerNS(errors, e, path, xhtml.getChildNodes());
        rule(errors, IssueType.INVALID, e.line(), e.col(), path, "div".equals(xhtml.getName()), "Wrong name on the XHTML ('"+ns+"') - must start with div");
        // check that no illegal elements and attributes have been used
        checkInnerNames(errors, e, path, xhtml.getChildNodes());
      } 
    }

    if (context.hasFixed())
      checkFixedValue(errors,path,e, context.getFixed(), context.getSliceName(), null);

    // for nothing to check
  }

  private void checkInnerNames(List<ValidationMessage> errors, Element e, String path, List<XhtmlNode> list) {
    for (XhtmlNode node : list) {
      if (node.getNodeType() == NodeType.Element) {
        rule(errors, IssueType.INVALID, e.line(), e.col(), path, Utilities.existsInList(node.getName(), 
            "p", "br", "div", "h1", "h2", "h3", "h4", "h5", "h6", "a", "span", "b", "em", "i", "strong",
            "small", "big", "tt", "small", "dfn", "q", "var", "abbr", "acronym", "cite", "blockquote", "hr", "address", "bdo", "kbd", "q", "sub", "sup",
            "ul", "ol", "li", "dl", "dt", "dd", "pre", "table", "caption", "colgroup", "col", "thead", "tr", "tfoot", "tbody", "th", "td",
            "code", "samp", "img", "map", "area"

            ), "Illegal element name in the XHTML ('"+node.getName()+"')");
        for (String an : node.getAttributes().keySet()) {
          boolean ok = an.startsWith("xmlns") || Utilities.existsInList(an, 
              "title", "style", "class", "id", "lang", "xml:lang", "dir", "accesskey", "tabindex",
              // tables
              "span", "width", "align", "valign", "char", "charoff", "abbr", "axis", "headers", "scope", "rowspan", "colspan") ||

              Utilities.existsInList(node.getName()+"."+an, "a.href", "a.name", "img.src", "img.border", "div.xmlns", "blockquote.cite", "q.cite",
                  "a.charset", "a.type", "a.name", "a.href", "a.hreflang", "a.rel", "a.rev", "a.shape", "a.coords", "img.src",
                  "img.alt", "img.longdesc", "img.height", "img.width", "img.usemap", "img.ismap", "map.name", "area.shape",
                  "area.coords", "area.href", "area.nohref", "area.alt", "table.summary", "table.width", "table.border",
                  "table.frame", "table.rules", "table.cellspacing", "table.cellpadding", "pre.space"
                  );
          if (!ok)
            rule(errors, IssueType.INVALID, e.line(), e.col(), path, false, "Illegal attribute name in the XHTML ('"+an+"' on '"+node.getName()+"')");
        }
        checkInnerNames(errors, e, path, node.getChildNodes());
      }
    }	
  }

  private void checkInnerNS(List<ValidationMessage> errors, Element e, String path, List<XhtmlNode> list) {
    for (XhtmlNode node : list) {
      if (node.getNodeType() == NodeType.Element) {
        String ns = node.getNsDecl();
        rule(errors, IssueType.INVALID, e.line(), e.col(), path, ns == null || FormatUtilities.XHTML_NS.equals(ns), "Wrong namespace on the XHTML ('"+ns+"')");
        checkInnerNS(errors, e, path, node.getChildNodes());
      }
    }	
  }

  private void checkPrimitiveBinding(List<ValidationMessage> errors, String path, String type, ElementDefinition elementContext, Element element, StructureDefinition profile) {
    // We ignore bindings that aren't on string, uri or code
    if (!element.hasPrimitiveValue() || !("code".equals(type) || "string".equals(type) || "uri".equals(type))) {
      return;
    }
    if (noTerminologyChecks)
      return;

    String value = element.primitiveValue();
    // System.out.println("check "+value+" in "+path);

    // firstly, resolve the value set
    ElementDefinitionBindingComponent binding = elementContext.getBinding();
    if (binding.hasValueSet() && binding.getValueSet() instanceof Reference) {
      ValueSet vs = resolveBindingReference(profile, binding.getValueSet(), profile.getUrl());
      if (warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, vs != null, "ValueSet {0} not found", describeReference(binding.getValueSet()))) {
        long t = System.nanoTime();
        ValidationResult vr = context.validateCode(null, value, null, vs);
        txTime = txTime + (System.nanoTime() - t);
        if (vr != null && !vr.isOk()) {
          if (vr.IsNoService())
            hint(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false,  "The value provided ('"+value+"') could not be validated in the absence of a terminology server");
          else if (binding.getStrength() == BindingStrength.REQUIRED)
            rule(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "The value provided ('"+value+"') is not in the value set " + describeReference(binding.getValueSet()) + " (" + vs.getUrl() + ", and a code is required from this value set)"+(vr.getMessage() != null ? " (error message = "+vr.getMessage()+")" : ""));
          else if (binding.getStrength() == BindingStrength.EXTENSIBLE) {
            if (binding.hasExtension("http://hl7.org/fhir/StructureDefinition/elementdefinition-maxValueSet")) 
              checkMaxValueSet(errors, path, element, profile, (Reference) binding.getExtensionsByUrl("http://hl7.org/fhir/StructureDefinition/elementdefinition-maxValueSet").get(0).getValue(), value);
            else
              warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "The value provided ('"+value+"') is not in the value set " + describeReference(binding.getValueSet()) + " (" + vs.getUrl() + ", and a code should come from this value set unless it has no suitable code)"+(vr.getMessage() != null ? " (error message = "+vr.getMessage()+")" : ""));
          } else if (binding.getStrength() == BindingStrength.PREFERRED)
            hint(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false,  "The value provided ('"+value+"') is not in the value set " + describeReference(binding.getValueSet()) + " (" + vs.getUrl() + ", and a code is recommended to come from this value set)"+(vr.getMessage() != null ? " (error message = "+vr.getMessage()+")" : ""));
        }
      }
    } else if (!noBindingMsgSuppressed)
      hint(errors, IssueType.CODEINVALID, element.line(), element.col(), path, !type.equals("code"), "Binding has no source, so can't be checked");
  }

  private void checkQuantity(List<ValidationMessage> errors, String path, Element focus, Quantity fixed) {
    checkFixedValue(errors, path + ".value", focus.getNamedChild("value"), fixed.getValueElement(), "value", focus);
    checkFixedValue(errors, path + ".comparator", focus.getNamedChild("comparator"), fixed.getComparatorElement(), "comparator", focus);
    checkFixedValue(errors, path + ".units", focus.getNamedChild("unit"), fixed.getUnitElement(), "units", focus);
    checkFixedValue(errors, path + ".system", focus.getNamedChild("system"), fixed.getSystemElement(), "system", focus);
    checkFixedValue(errors, path + ".code", focus.getNamedChild("code"), fixed.getCodeElement(), "code", focus);
  }

  // implementation

  private void checkRange(List<ValidationMessage> errors, String path, Element focus, Range fixed) {
    checkFixedValue(errors, path + ".low", focus.getNamedChild("low"), fixed.getLow(), "low", focus);
    checkFixedValue(errors, path + ".high", focus.getNamedChild("high"), fixed.getHigh(), "high", focus);

  }

  private void checkRatio(List<ValidationMessage> errors, String path, Element focus, Ratio fixed) {
    checkFixedValue(errors, path + ".numerator", focus.getNamedChild("numerator"), fixed.getNumerator(), "numerator", focus);
    checkFixedValue(errors, path + ".denominator", focus.getNamedChild("denominator"), fixed.getDenominator(), "denominator", focus);
  }

  private void checkReference(Object appContext, List<ValidationMessage> errors, String path, Element element, StructureDefinition profile, ElementDefinition container, String parentType, NodeStack stack) throws FHIRException, IOException {
    String ref = null;
    try {
      // Do this inside a try because invalid instances might provide more than one reference.
      ref = element.getNamedChildValue("reference");
    } catch (Error e) {
      
    }
    if (Utilities.noString(ref)) {
      // todo - what should we do in this case?
      warning(errors, IssueType.STRUCTURE, element.line(), element.col(), path, !Utilities.noString(element.getNamedChildValue("display")), "A Reference without an actual reference should have a display");
      return;
    }

    Element we = localResolve(ref, stack, errors, path);
    String refType;
    if (ref.startsWith("#")) {
      refType = "contained";
    } else {
      if (we == null) {
        refType = "remote";
      } else {
        refType = "bundle";
      }
    }
    String ft;
    if (we != null)
      ft = we.getType();
    else
      ft = tryParse(ref);
    ReferenceValidationPolicy pol = refType.equals("contained") ? ReferenceValidationPolicy.CHECK_VALID : fetcher == null ? ReferenceValidationPolicy.IGNORE : fetcher.validationPolicy(appContext, path, ref);

    if (pol.checkExists()) {
      if (we == null) {
        if (fetcher == null)
          throw new FHIRException("Resource resolution services not provided");
        we = fetcher.fetch(appContext, ref);
      }
      rule(errors, IssueType.STRUCTURE, element.line(), element.col(), path, we != null, "Unable to resolve resource '"+ref+"'");
    }

    if (we != null && pol.checkType()) {
      if (warning(errors, IssueType.STRUCTURE, element.line(), element.col(), path, ft!=null, "Unable to determine type of target resource")) {
        boolean ok = false;
        CommaSeparatedStringBuilder b = new CommaSeparatedStringBuilder();
        for (TypeRefComponent type : container.getType()) {
          if (!ok && type.getCode().equals("Reference")) {
            // we validate as much as we can. First, can we infer a type from the profile? 
            if (!type.hasTargetProfile() || type.getTargetProfile().equals("http://hl7.org/fhir/StructureDefinition/Resource"))
              ok = true;
            else {
              String pr = type.getTargetProfile();

              String bt = getBaseType(profile, pr);
              StructureDefinition sd = context.fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/" + bt);
              if (rule(errors, IssueType.STRUCTURE, element.line(), element.col(), path, bt != null, "Unable to resolve the profile reference '" + pr + "'")) {
                b.append(bt);
                ok = bt.equals(ft);
                if (ok && we!=null && pol.checkValid()) {
                  doResourceProfile(appContext, we, pr, errors, stack.push(we, -1, null, null), path, element);
                }
              } else
                ok = true; // suppress following check
              if (ok && type.hasAggregation()) {
                boolean modeOk;
                for (Enumeration<AggregationMode> mode : type.getAggregation()) {
                  if (mode.getValue().equals(AggregationMode.CONTAINED) && refType.equals("contained"))
                    ok = true;
                  else if (mode.getValue().equals(AggregationMode.BUNDLED) && refType.equals("bundled"))
                    ok = true;
                  else if (mode.getValue().equals(AggregationMode.REFERENCED) && (refType.equals("bundled")||refType.equals("remote")))
                    ok = true;
                }
                rule(errors, IssueType.STRUCTURE, element.line(), element.col(), path, ok, "Reference is " + refType + " which isn't supported by the specified aggregation mode(s) for the reference");
              }
            }
          }
          if (!ok && type.getCode().equals("*")) {
            ok = true; // can refer to anything
          }
        }
        rule(errors, IssueType.STRUCTURE, element.line(), element.col(), path, ok, "Invalid Resource target type. Found " + ft + ", but expected one of (" + b.toString() + ")");
      }
    }
    if (pol == ReferenceValidationPolicy.CHECK_VALID) {
      // todo....
    }
  }
  
  private void doResourceProfile(Object appContext, Element resource, String profile, List<ValidationMessage> errors, NodeStack stack, String path, Element element) throws FHIRException, IOException {
    ResourceProfiles resourceProfiles = addResourceProfile(errors, resource, profile, path, element, stack);
    if (resourceProfiles.isProcessed()) {
      start(appContext, errors, resource, resource, null, stack);  
    }
  }

  private ResourceProfiles getResourceProfiles(Element resource, NodeStack stack) {
    ResourceProfiles resourceProfiles = resourceProfilesMap.get(resource);
    if (resourceProfiles==null) {
      resourceProfiles = new ResourceProfiles(resource, stack);
      resourceProfilesMap.put(resource, resourceProfiles);
    }
    return resourceProfiles;
  }

  private ResourceProfiles addResourceProfile(List<ValidationMessage> errors, Element resource, String profile, String path, Element element, NodeStack stack) {
    ResourceProfiles resourceProfiles = getResourceProfiles(resource, stack);
    resourceProfiles.addProfile(errors, profile, errorForUnknownProfiles, path, element);
    return resourceProfiles;
  }

  private String checkResourceType(String type)  {
    long t = System.nanoTime();
    try {
      if (context.fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/" + type) != null)
        return type;
      else
        return null;
    } finally {
      sdTime = sdTime + (System.nanoTime() - t);
    }
  }

  private void checkSampledData(List<ValidationMessage> errors, String path, Element focus, SampledData fixed) {
    checkFixedValue(errors, path + ".origin", focus.getNamedChild("origin"), fixed.getOrigin(), "origin", focus);
    checkFixedValue(errors, path + ".period", focus.getNamedChild("period"), fixed.getPeriodElement(), "period", focus);
    checkFixedValue(errors, path + ".factor", focus.getNamedChild("factor"), fixed.getFactorElement(), "factor", focus);
    checkFixedValue(errors, path + ".lowerLimit", focus.getNamedChild("lowerLimit"), fixed.getLowerLimitElement(), "lowerLimit", focus);
    checkFixedValue(errors, path + ".upperLimit", focus.getNamedChild("upperLimit"), fixed.getUpperLimitElement(), "upperLimit", focus);
    checkFixedValue(errors, path + ".dimensions", focus.getNamedChild("dimensions"), fixed.getDimensionsElement(), "dimensions", focus);
    checkFixedValue(errors, path + ".data", focus.getNamedChild("data"), fixed.getDataElement(), "data", focus);
  }

  private void checkTiming(List<ValidationMessage> errors, String path, Element focus, Timing fixed) {
    checkFixedValue(errors, path + ".repeat", focus.getNamedChild("repeat"), fixed.getRepeat(), "value", focus);

    List<Element> events = new ArrayList<Element>();
    focus.getNamedChildren("event", events);
    if (rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, events.size() == fixed.getEvent().size(),
        "Expected " + Integer.toString(fixed.getEvent().size()) + " but found " + Integer.toString(events.size()) + " event elements")) {
      for (int i = 0; i < events.size(); i++)
        checkFixedValue(errors, path + ".event", events.get(i), fixed.getEvent().get(i), "event", focus);
    }
  }

  private boolean codeinExpansion(ValueSetExpansionContainsComponent cnt, String system, String code) {
    for (ValueSetExpansionContainsComponent c : cnt.getContains()) {
      if (code.equals(c.getCode()) && system.equals(c.getSystem().toString()))
        return true;
      if (codeinExpansion(c, system, code))
        return true;
    }    
    return false;
  }

  private boolean codeInExpansion(ValueSet vs, String system, String code) {
    for (ValueSetExpansionContainsComponent c : vs.getExpansion().getContains()) {
      if (code.equals(c.getCode()) && (system == null || system.equals(c.getSystem())))
        return true;
      if (codeinExpansion(c, system, code))
        return true;
    }
    return false;
  }

  private String describeReference(Type reference) {
    if (reference == null)
      return "null";
    if (reference instanceof UriType)
      return ((UriType) reference).getValue();
    if (reference instanceof Reference)
      return ((Reference) reference).getReference();
    return "??";
  }

  private String describeTypes(List<TypeRefComponent> types) {
    CommaSeparatedStringBuilder b = new CommaSeparatedStringBuilder();
    for (TypeRefComponent t : types) {
      b.append(t.getCode());
    }
    return b.toString();
  }

  protected ElementDefinition findElement(StructureDefinition profile, String name) {
    for (ElementDefinition c : profile.getSnapshot().getElement()) {
      if (c.getPath().equals(name)) {
        return c;
      }
    }
    return null;
  }

  public BestPracticeWarningLevel getBasePracticeWarningLevel() {
    return bpWarnings;
  }  

  private String getBaseType(StructureDefinition profile, String pr)  {
    StructureDefinition p = resolveProfile(profile, pr);
    if (p == null)
      return null;
    else 
      return p.getType();
  }

  @Override
  public CheckDisplayOption getCheckDisplay() {
    return checkDisplay;
  }

  //	private String findProfileTag(Element element) {
  //  	String uri = null;
  //	  List<Element> list = new ArrayList<Element>();
  //	  element.getNamedChildren("category", list);
  //	  for (Element c : list) {
  //	  	if ("http://hl7.org/fhir/tag/profile".equals(c.getAttribute("scheme"))) {
  //	  		uri = c.getAttribute("term");
  //	  	}
  //	  }
  //	  return uri;
  //  }

  private ConceptDefinitionComponent getCodeDefinition(ConceptDefinitionComponent c, String code) {
    if (code.equals(c.getCode()))
      return c;
    for (ConceptDefinitionComponent g : c.getConcept()) {
      ConceptDefinitionComponent r = getCodeDefinition(g, code);
      if (r != null)
        return r;
    }
    return null;
  }

  private ConceptDefinitionComponent getCodeDefinition(CodeSystem cs, String code) {
    for (ConceptDefinitionComponent c : cs.getConcept()) {
      ConceptDefinitionComponent r = getCodeDefinition(c, code);
      if (r != null)
        return r;
    }
    return null;
  }

  private Element getContainedById(Element container, String id) {
    List<Element> contained = new ArrayList<Element>();
    container.getNamedChildren("contained", contained);
    for (Element we : contained) {
      if (id.equals(we.getNamedChildValue("id")))
        return we;
    }   
    return null;
  }

  public IWorkerContext getContext() {
    return context;
  }

  private ElementDefinition getCriteriaForDiscriminator(String path, ElementDefinition element, String discriminator, StructureDefinition profile) throws DefinitionException {
    StructureDefinition sd = profile;
    ElementDefinition ed = element;
    
    if ("value".equals(discriminator) && element.hasFixed())
      return element;
    
    List<String> nodes = new ArrayList<String>();
    Matcher matcher = Pattern.compile("([a-zA-Z0-0]+(\\([^\\(^\\)]*\\))?)(\\.([a-zA-Z0-0]+(\\([^\\(^\\)]*\\))?))*").matcher(discriminator);
    while (matcher.find()) {
      if (!matcher.group(1).startsWith("@"))
        nodes.add(matcher.group(1));
      if (matcher.groupCount()>4 && matcher.group(4)!= null && !matcher.group(4).startsWith("@"))
        nodes.add(matcher.group(4));
    }
    
    for (String fullnode : nodes) {
      String node = fullnode.contains("(") ? fullnode.substring(0, fullnode.indexOf('(')) : fullnode;
      String qualifier = fullnode.contains("(") ? fullnode.substring(fullnode.indexOf('(') + 2, fullnode.length()-2) : null;
      if (qualifier!=null && !node.equals("extension"))
        throw new DefinitionException("Function specified other than 'extension()': " + discriminator);
      // get the children of element
      List<ElementDefinition> childDefinitions;
      childDefinitions = ProfileUtilities.getChildMap(sd, ed);
      // if that's empty, get the children of the type
      if (childDefinitions.isEmpty()) {
        if (ed.getType().size() == 0)
          throw new DefinitionException("Error in profile for " + path + " no children, no type");
        if (ed.getType().size() > 1)
          throw new DefinitionException("Error in profile for " + path + " multiple types defined in slice discriminator");
        if (ed.hasSlicing()) {
          List<ElementDefinition> slices = ProfileUtilities.getSliceList(profile, ed);
          boolean found = false;
          for (ElementDefinition sed: slices) {
            childDefinitions = ProfileUtilities.getChildMap(sd, sed);
            for (ElementDefinition t : childDefinitions) {
              if (tailMatches(t, node)) {
                found = true;
                if (qualifier!=null) {
                  List<ElementDefinition> childNodes = ProfileUtilities.getChildList(profile, t);
                  if (childNodes.isEmpty()) {
                    if (t.getType().size()==1 && t.getType().get(0).hasProfile() && t.getType().get(0).getProfile().equals(qualifier)) {
                      found = true;
                      break;
                    }
                      
                  } else {
                    for (ElementDefinition c : childNodes) {
                      if (c.getPath().endsWith(".url") && c.hasFixed() && c.getFixed() instanceof UriType && ((UriType)c.getFixed()).equals(qualifier)) {
                        found = true;
                        break;
                      }
                    }
                  }
                }
                if (found) {
                  ed = t;
                  break;
                }
              }
            }
          }
          if (found)
            continue;
        }
        if (ed.getType().get(0).hasProfile() && ed.getType().get(0).getCode().equals("Reference") && node.equals("reference")) {
          long t1 = System.nanoTime();
          sd = context.fetchResource(StructureDefinition.class, ed.getType().get(0).getTargetProfile());
          sdTime = sdTime + (System.nanoTime() - t1);
          // we've skipped the reference.
          ed = sd.getSnapshot().getElementFirstRep();
          continue;
        } else if (ed.getType().get(0).hasProfile()) {
          long t2 = System.nanoTime();
          sd = context.fetchResource(StructureDefinition.class, ed.getType().get(0).getProfile());
          sdTime = sdTime + (System.nanoTime() - t2);
        } else {
          long t2 = System.nanoTime();
          sd = context.fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/" + ed.getType().get(0).getCode());
          sdTime = sdTime + (System.nanoTime() - t2);
        }
        if (sd == null)
          throw new DefinitionException("Error in profile for " + path + ": unable to find type for "+ed.getId());
        childDefinitions = ProfileUtilities.getChildMap(sd, sd.getSnapshot().getElementFirstRep());
      }
      // find a single match. (no slicing!)
      boolean found = false;
      for (ElementDefinition t : childDefinitions) {
        if (tailMatches(t, node)) {
          found = true;
          if (qualifier!=null) {
            found = false;
            List<ElementDefinition> childNodes = ProfileUtilities.getChildList(profile, t);
            if (childNodes.isEmpty()) {
              if (t.getType().size()==1 && t.getType().get(0).hasProfile() && t.getType().get(0).getProfile().equals(qualifier)) {
                found = true;
                ed = t;
                break;
              }
                
            } else {
              for (ElementDefinition c : childNodes) {
                if (c.getPath().endsWith(".url") && c.hasFixed() && c.getFixed() instanceof UriType && ((UriType)c.getFixed()).equals(qualifier)) {
                  found = true;
                  ed = t;
                  break;
                }
              }
            }
          }
          if (found) {
            ed = t;
            break;
          }
        }
      }
      if (!found)
        throw new DefinitionException("Unable to find definition for discriminator "+discriminator+" from "+path+" in "+profile.getUrl());
    }
    return ed;
    //    List<ElementDefinition> snapshot = null;
    //    int index;
    //    if (childDefinitions.isEmpty()) {
    //      // going to look at the type
    //      if (ed.getType().size() == 0)
    //        throw new DefinitionException("Error in profile for " + path + " no children, no type");
    //      snapshot = type.getSnapshot().getElement();
    //      ed = snapshot.get(0);
    //      index = 0;
    //    } else {
    //      snapshot = childDefinitions;
    //      index = -1;
    //    }
    //    String originalPath = ed.getPath();
    //    String goal = originalPath + "." + discriminator;
    //
    //    index++;
    //    while (index < snapshot.size() && !snapshot.get(index).getPath().equals(originalPath)) {
    //      if (snapshot.get(index).getPath().equals(goal))
    //        return snapshot.get(index);
    //      index++;
    //    }
    //    throw new Error("Unable to find discriminator definition for " + goal + " in " + discriminator + " at " + path+" in "+profile.getUrl());
  }

  private boolean tailMatches(ElementDefinition t, String d) {
    String tail = tail(t.getPath());
    if (d.contains("["))
      return tail.startsWith(d.substring(0, d.indexOf('[')));
    else
      return tail.equals(d);
  }

  private Element getExtensionByUrl(List<Element> extensions, String urlSimple) {
    for (Element e : extensions) {
      if (urlSimple.equals(e.getNamedChildValue("url")))
        return e;
    }
    return null;
  }

  public List<String> getExtensionDomains() {
    return extensionDomains;
  }

  private Element getFromBundle(Element bundle, String ref, String fullUrl, List<ValidationMessage> errors, String path) {
    String targetUrl = null;
    String version = "";
    if (ref.startsWith("http") || ref.startsWith("urn")) {
      // We've got an absolute reference, no need to calculate
      if (ref.contains("/_history/")) {
        targetUrl = ref.substring(0, ref.indexOf("/_history/") - 1);
        version = ref.substring(ref.indexOf("/_history/") + 10);
      }  else
        targetUrl = ref;

    } else if (fullUrl == null) {
      //This isn't a problem for signatures - if it's a signature, we won't have a resolution for a relative reference.  For anything else, this is an error
      rule(errors, IssueType.REQUIRED, -1, -1, path, path.startsWith("Bundle.signature"), "Relative Reference appears inside Bundle whose entry is missing a fullUrl");
      return null;

    } else if (ref.split("/").length!=2) {
      rule(errors, IssueType.INVALID, -1, -1, path, false, "Relative URLs must be of the format [ResourceName]/[id].  Encountered " + ref);
      return null;

    } else {
      String base = "";
      if (fullUrl.startsWith("urn")) {
        String[] parts = fullUrl.split("\\:");
        for (int i=0; i < parts.length-1; i++) {
          base = base + parts[i] + ":";
        }
      } else {
        String[] parts;
        parts = fullUrl.split("/");
        for (int i=0; i < parts.length-2; i++) {
          base = base + parts[i] + "/";
        }
      }

      String id = null;
      if (ref.contains("/_history/")) {
        version = ref.substring(ref.indexOf("/_history/") + 10);
        id = ref.substring(0, ref.indexOf("/history/")-1);
      } else if (base.startsWith("urn"))
        id = ref.split("/")[1];
      else
        id = ref;

      targetUrl = base + id;
    }

    List<Element> entries = new ArrayList<Element>();
    bundle.getNamedChildren("entry", entries);
    Element match = null;
    for (Element we : entries) {
      if (we.getChildValue("fullUrl").equals(targetUrl)) {
        Element r = we.getNamedChild("resource");
        if (version.isEmpty()) {
          rule(errors, IssueType.FORBIDDEN, -1, -1, path, match==null, "Multiple matches in bundle for reference " + ref);
          match = r;
        } else {
          try {
            if (r.getChildren("meta").get(0).getChildValue("versionId").equals(version)) {
              rule(errors, IssueType.FORBIDDEN, -1, -1, path, match==null, "Multiple matches in bundle for reference " + ref);
              match = r;
            }
          } catch (Exception e) {
            warning(errors, IssueType.REQUIRED, -1, -1, path, r.getChildren("meta").size()==1 && r.getChildren("meta").get(0).getChildValue("versionId")!=null, "Entries matching fullURL " + targetUrl + " should declare meta/versionId because there are version-specific references");
            // If one of these things is null
          }
        }
      }
    }

    warning(errors, IssueType.REQUIRED, -1, -1, path, match!=null || !targetUrl.startsWith("urn"), "URN reference is not locally contained within the bundle " + ref);
    return match;
  }  

  private StructureDefinition getProfileForType(String type) {
    if (logical != null)
      for (BundleEntryComponent be : logical.getEntry()) {
        if (be.hasResource() && be.getResource() instanceof StructureDefinition) {
          StructureDefinition sd = (StructureDefinition) be.getResource();
          if (sd.getId().equals(type))
            return sd;
        }
      }

    long t = System.nanoTime();
    try {
      return context.fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/" + type);
    } finally {
      sdTime = sdTime + (System.nanoTime() - t);
    }
  }

  private Element getValueForDiscriminator(Object appContext, List<ValidationMessage> errors, Element element, String discriminator, ElementDefinition criteria, NodeStack stack) throws FHIRException, IOException  {
    String p = stack.getLiteralPath()+"."+element.getName();
    Element focus = element;
    String[] dlist = discriminator.split("\\.");
    for (String d : dlist) {
      if (focus.fhirType().equals("Reference") && d.equals("reference")) {
        String url = focus.getChildValue("reference");
        if (Utilities.noString(url))
          throw new FHIRException("No reference resolving discriminator "+discriminator+" from "+element.getProperty().getName());
        // Note that we use the passed in stack here. This might be a problem if the discriminator is deep enough? 
        Element target = resolve(appContext, url, stack, errors, p);
        if (target == null)
          throw new FHIRException("Unable to find resource "+url+" at "+d+" resolving discriminator "+discriminator+" from "+element.getProperty().getName());
        focus = target;
      } else if (d.equals("value") && focus.isPrimitive()) {
        return focus;
      } else {
        List<Element> children = focus.getChildren(d);
        if (children.isEmpty())
          throw new FHIRException("Unable to find "+d+" resolving discriminator "+discriminator+" from "+element.getProperty().getName());
        if (children.size() > 1)
          throw new FHIRException("Found "+Integer.toString(children.size())+" items for "+d+" resolving discriminator "+discriminator+" from "+element.getProperty().getName());
        focus = children.get(0);
        p = p + "."+d;
      }
    }
    return focus;
  }

  private CodeSystem getCodeSystem(String system) {
    long t = System.nanoTime();
    try {
      return context.fetchCodeSystem(system);
    } finally {
      txTime = txTime + (System.nanoTime() - t);
    }
  }

  private boolean hasTime(String fmt) {
    return fmt.contains("T");
  }

  private boolean hasTimeZone(String fmt) {
    return fmt.length() > 10 && (fmt.substring(10).contains("-") || fmt.substring(10).contains("+") || fmt.substring(10).contains("Z"));
  }

  private boolean isAbsolute(String uri) {
    return Utilities.noString(uri) || uri.startsWith("http:") || uri.startsWith("https:") || uri.startsWith("urn:uuid:") || uri.startsWith("urn:oid:") || uri.startsWith("urn:ietf:")
        || uri.startsWith("urn:iso:") || uri.startsWith("urn:iso-astm:") || isValidFHIRUrn(uri);
  }

  private boolean isValidFHIRUrn(String uri) {
    return (uri.equals("urn:x-fhir:uk:id:nhs-number")) || uri.startsWith("urn:"); // Anyone can invent a URN, so why should we complain?
  }

  public boolean isAnyExtensionsAllowed() {
    return anyExtensionsAllowed;
  }

  public boolean isErrorForUnknownProfiles() {
    return errorForUnknownProfiles;
  }

  public void setErrorForUnknownProfiles(boolean errorForUnknownProfiles) {
    this.errorForUnknownProfiles = errorForUnknownProfiles;
  }

  private boolean isParametersEntry(String path) {
    String[] parts = path.split("\\.");
    return parts.length > 2 && parts[parts.length - 1].equals("resource") && (pathEntryHasName(parts[parts.length - 2], "parameter") || pathEntryHasName(parts[parts.length - 2], "part"));
  }

  private boolean isBundleEntry(String path) {
    String[] parts = path.split("\\.");
    return parts.length > 2 && parts[parts.length - 1].equals("resource") && pathEntryHasName(parts[parts.length - 2], "entry");
  }

  private boolean isBundleOutcome(String path) {
    String[] parts = path.split("\\.");
    return parts.length > 2 && parts[parts.length - 1].equals("outcome") && pathEntryHasName(parts[parts.length - 2], "response");
  }


  private static boolean pathEntryHasName(String thePathEntry, String theName) {
    if (thePathEntry.equals(theName)) {
      return true;
    }
    if (thePathEntry.length() >= theName.length() + 3) {
      if (thePathEntry.startsWith(theName)) {
        if (thePathEntry.charAt(theName.length()) == '[') {
          return true;
        }
      }
    }
    return false;
  }

  private boolean isPrimitiveType(String code) {
    StructureDefinition sd = context.fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/"+code);
    return sd != null && sd.getKind() == StructureDefinitionKind.PRIMITIVETYPE;
  }



  public boolean isSuppressLoincSnomedMessages() {
    return suppressLoincSnomedMessages;
  }

  private boolean nameMatches(String name, String tail) {
    if (tail.endsWith("[x]"))
      return name.startsWith(tail.substring(0, tail.length() - 3));
    else
      return (name.equals(tail));
  }

  // private String mergePath(String path1, String path2) {
  // // path1 is xpath path
  // // path2 is dotted path
  // String[] parts = path2.split("\\.");
  // StringBuilder b = new StringBuilder(path1);
  // for (int i = 1; i < parts.length -1; i++)
  // b.append("/f:"+parts[i]);
  // return b.toString();
  // }

  private boolean passesCodeWhitespaceRules(String v) {
    if (!v.trim().equals(v))
      return false;
    boolean lastWasSpace = true;
    for (char c : v.toCharArray()) {
      if (c == ' ') {
        if (lastWasSpace)
          return false;
        else
          lastWasSpace = true;
      } else if (Character.isWhitespace(c))
        return false;
      else
        lastWasSpace = false;
    }
    return true;
  }

  private Element localResolve(String ref, NodeStack stack, List<ValidationMessage> errors, String path) {
    if (ref.startsWith("#")) {
      // work back through the contained list.
      // really, there should only be one level for this (contained resources cannot contain
      // contained resources), but we'll leave that to some other code to worry about
      while (stack != null && stack.getElement() != null) {
        if (stack.getElement().getProperty().isResource()) {
          // ok, we'll try to find the contained reference
          Element res = getContainedById(stack.getElement(), ref.substring(1));
          if (res != null)
            return res;
        }
        if (stack.getElement().getSpecial() == SpecialElement.BUNDLE_ENTRY) {
          return null; // we don't try to resolve contained references across this boundary
        }
        stack = stack.parent;
      }
      return null;
    } else {
      // work back through the contained list - if any of them are bundles, try to resolve
      // the resource in the bundle
      String fullUrl = null; // we're going to try to work this out as we go up
      while (stack != null && stack.getElement() != null) {
        if (stack.getElement().getSpecial() == SpecialElement.BUNDLE_ENTRY && fullUrl==null && stack.parent.getElement().getName().equals("entry")) {
          fullUrl = stack.parent.getElement().getChildValue("fullUrl"); // we don't try to resolve contained references across this boundary
          if (fullUrl==null)
            rule(errors, IssueType.REQUIRED, stack.parent.getElement().line(), stack.parent.getElement().col(), stack.parent.getLiteralPath(), fullUrl!=null, "Bundle entry missing fullUrl");
        }
        if ("Bundle".equals(stack.getElement().getType())) {
          Element res = getFromBundle(stack.getElement(), ref, fullUrl, errors, path);
          return res;
        }
        stack = stack.parent;
      }
    }
    return null;
  }

  private Element resolve(Object appContext, String ref, NodeStack stack, List<ValidationMessage> errors, String path) throws IOException, FHIRException {
    Element local = localResolve(ref, stack, errors, path);
    if (local!=null)
      return local;
    if (fetcher == null)
      return null;
    return fetcher.fetch(appContext, ref);
  }

  private ValueSet resolveBindingReference(DomainResource ctxt, Type reference, String uri) {
    if (reference instanceof UriType) {
      long t = System.nanoTime();
      ValueSet fr = context.fetchResource(ValueSet.class, ((UriType) reference).getValue().toString());
      txTime = txTime + (System.nanoTime() - t);
      return fr;
    }
    else if (reference instanceof Reference) {
      String s = ((Reference) reference).getReference();
      if (s.startsWith("#")) {
        for (Resource c : ctxt.getContained()) {
          if (c.getId().equals(s.substring(1)) && (c instanceof ValueSet))
            return (ValueSet) c;
        }
        return null;
      } else {
        long t = System.nanoTime();
        String ref = ((Reference) reference).getReference();
        if (!Utilities.isAbsoluteUrl(ref))
          ref = resolve(uri, ref);
        ValueSet fr = context.fetchResource(ValueSet.class, ref);
        txTime = txTime + (System.nanoTime() - t);
        return fr;
      }
    }
    else
      return null;
  }

  private String resolve(String uri, String ref) {
    String[] up = uri.split("\\/");
    String[] rp = ref.split("\\/");
    if (context.getResourceNames().contains(up[up.length-2]) && context.getResourceNames().contains(rp[0])) {
      StringBuilder b = new StringBuilder();
      for (int i = 0; i < up.length-2; i++) {
        b.append(up[i]);
        b.append("/");
      }
      b.append(ref);
      return b.toString();
    } else
      return ref;
  }

  private Element resolveInBundle(List<Element> entries, String ref, String fullUrl, String type, String id) {
    if (Utilities.isAbsoluteUrl(ref)) {
      // if the reference is absolute, then you resolve by fullUrl. No other thinking is required. 
      for (Element entry : entries) {
        String fu = entry.getNamedChildValue("fullUrl");
        if (ref.equals(fu))
          return entry;
      }
      return null;
    } else {
      // split into base, type, and id
      String u = null;
      if (fullUrl != null && fullUrl.endsWith(type+"/"+id))
        // fullUrl = complex
        u = fullUrl.substring((type+"/"+id).length())+ref;
      String[] parts = ref.split("\\/");
      if (parts.length >= 2) {
        String t = parts[0];
        String i = parts[1];
        for (Element entry : entries) {
          String fu = entry.getNamedChildValue("fullUrl");
          if (u != null && fullUrl.equals(u))
            return entry;
          if (u == null) {
            Element resource = entry.getNamedChild("resource");
            String et = resource.getType();
            String eid = resource.getNamedChildValue("id");
            if (t.equals(et) && i.equals(eid))
              return entry;
          }
        }
      }
      return null;
    }
  }

  private ElementDefinition resolveNameReference(StructureDefinitionSnapshotComponent snapshot, String contentReference) {
    for (ElementDefinition ed : snapshot.getElement())
      if (contentReference.equals("#"+ed.getId()))
        return ed;
    return null;
  }

  private StructureDefinition resolveProfile(StructureDefinition profile, String pr)  {
    if (pr.startsWith("#")) {
      for (Resource r : profile.getContained()) {
        if (r.getId().equals(pr.substring(1)) && r instanceof StructureDefinition)
          return (StructureDefinition) r;
      }
      return null;
    } else {
      long t = System.nanoTime();
      StructureDefinition fr = context.fetchResource(StructureDefinition.class, pr);
      sdTime = sdTime + (System.nanoTime() - t);
      return fr;
    }
  }

  private ElementDefinition resolveType(String type)  {
    if (logical != null)
      for (BundleEntryComponent be : logical.getEntry()) {
        if (be.hasResource() && be.getResource() instanceof StructureDefinition) {
          StructureDefinition sd = (StructureDefinition) be.getResource();
          if (sd.getId().equals(type))
            return sd.getSnapshot().getElement().get(0);
        }
      }
    String url = "http://hl7.org/fhir/StructureDefinition/" + type;
    long t = System.nanoTime();
    StructureDefinition sd = context.fetchResource(StructureDefinition.class, url);
    sdTime = sdTime + (System.nanoTime() - t);
    if (sd == null || !sd.hasSnapshot())
      return null;
    else
      return sd.getSnapshot().getElement().get(0);
  }

  public void setAnyExtensionsAllowed(boolean anyExtensionsAllowed) {
    this.anyExtensionsAllowed = anyExtensionsAllowed;
  }

  public IResourceValidator setBestPracticeWarningLevel(BestPracticeWarningLevel value) {
    bpWarnings = value;
    return this;
  }

  @Override
  public void setCheckDisplay(CheckDisplayOption checkDisplay) {
    this.checkDisplay = checkDisplay;
  }

  public void setSuppressLoincSnomedMessages(boolean suppressLoincSnomedMessages) {
    this.suppressLoincSnomedMessages = suppressLoincSnomedMessages;
  }

  public IdStatus getResourceIdRule() {
    return resourceIdRule;
  }

  public void setResourceIdRule(IdStatus resourceIdRule) {
    this.resourceIdRule = resourceIdRule;
  }


  public boolean isAllowXsiLocation() {
    return allowXsiLocation;
  }

  public void setAllowXsiLocation(boolean allowXsiLocation) {
    this.allowXsiLocation = allowXsiLocation;
  }

  /**
   * 
   * @param element
   *          - the candidate that might be in the slice
   * @param path
   *          - for reporting any errors. the XPath for the element
   * @param slice
   *          - the definition of how slicing is determined
   * @param ed
   *          - the slice for which to test membership
   * @param errors 
   * @param stack 
   * @return
   * @throws DefinitionException 
   * @throws DefinitionException 
   * @throws IOException 
   * @throws FHIRException
   */
  private boolean sliceMatches(Object appContext, Element element, String path, ElementDefinition slicer, ElementDefinition ed, StructureDefinition profile, List<ValidationMessage> errors, NodeStack stack) throws DefinitionException, FHIRException, IOException {
    if (!slicer.getSlicing().hasDiscriminator())
      return false; // cannot validate in this case

    ExpressionNode n = (ExpressionNode) ed.getUserData("slice.expression.cache");
    if (n == null) {
      long t = System.nanoTime();
      // GG: this approach is flawed because it treats discriminators individually rather than collectively 
      String expression = "true";
      for (ElementDefinitionSlicingDiscriminatorComponent s : slicer.getSlicing().getDiscriminator()) {
        String discriminator = s.getPath();
        if (s.getType() == DiscriminatorType.PROFILE)
          throw new FHIRException("Validating against slices with discriminators based on profiles is not yet supported by the FHIRPath engine: " + discriminator);
        // Todo: Fix this once FHIRPath (and this engine) supports a boolean function that test profile conformance
        
        ElementDefinition criteriaElement = getCriteriaForDiscriminator(path, ed, discriminator, profile);
        if (s.getType() == DiscriminatorType.TYPE) {
          String type = null;
          if (!criteriaElement.getPath().contains("[") && discriminator.contains("[")) {
            discriminator = discriminator.substring(0, discriminator.indexOf('['));
            String lastNode = tail(discriminator);
            type = tail(criteriaElement.getPath()).substring(lastNode.length());
            type = type.substring(0,1).toLowerCase() + type.substring(1);
          } else if (!criteriaElement.hasType() || criteriaElement.getType().size()==1) {
            if (discriminator.contains("["))
              discriminator = discriminator.substring(0, discriminator.indexOf('['));
            type = criteriaElement.getType().get(0).getCode();
          }
          if (type==null)
			// Slicer won't ever have a slice name, so this error needs to be reworked
            throw new DefinitionException("Discriminator (" + discriminator + ") is based on type, but slice " + slicer.getSliceName() + " does not declare a type");
          if (discriminator.isEmpty())
            expression = expression + " and this is " + type;
          else
            expression = expression + " and " + discriminator + " is " + type;
        } else if (criteriaElement.hasFixed()) {
          expression = expression + " and " + discriminator + " = ";
          Type fixed = criteriaElement.getFixed();
          if (fixed instanceof StringType) {
            Gson gson = new Gson();
            String json = gson.toJson((StringType)fixed);
            String escapedString = json.substring(json.indexOf(":")+2);
            escapedString = escapedString.substring(0, escapedString.indexOf(",\"myStringValue")-1);
            expression = expression + "'" + escapedString + "'";
          } else if (fixed instanceof UriType) {
              expression = expression + "'" + ((UriType)fixed).asStringValue() + "'";
          } else if (fixed instanceof IntegerType) {
            expression = expression + ((IntegerType)fixed).asStringValue();
          } else if (fixed instanceof DecimalType) {
            expression = expression + ((IntegerType)fixed).asStringValue();
          } else if (fixed instanceof BooleanType) {
            expression = expression + ((BooleanType)fixed).asStringValue();
          } else
            throw new DefinitionException("Unsupported fixed value type for discriminator(" + discriminator + ") for slice " + slicer.getSliceName() + ": " + fixed.getClass().getName());
        } else if (criteriaElement.hasBinding() && criteriaElement.getBinding().hasStrength() && criteriaElement.getBinding().getStrength().equals(BindingStrength.REQUIRED) && criteriaElement.getBinding().getValueSetReference()!=null) {
          expression = expression + " and " + discriminator + " in '" + criteriaElement.getBinding().getValueSetReference().getReference() + "'";
        } else if (criteriaElement.hasMin() && criteriaElement.getMin()>0) {
          expression = expression + " and " + discriminator + ".exists()";
        } else if (criteriaElement.hasMax() && criteriaElement.getMax().equals("0")) {
          expression = expression + " and " + discriminator + ".exists().not()";
        } else {
          throw new DefinitionException("Could not match discriminator (" + discriminator + ") for slice " + slicer.getSliceName() + " - does not have fixed value, binding or existence assertions");
        }
      }

      try {
        n = fpe.parse(expression);
      } catch (FHIRLexerException e) {
        throw new FHIRException("Problem processing expression "+expression +" in profile " + profile.getUrl() + " path " + path + ": " + e.getMessage());
      }
      fpeTime = fpeTime + (System.nanoTime() - t);
      ed.setUserData("slice.expression.cache", n);
    }

    String msg;
    boolean ok;
    try {
      long t = System.nanoTime();
      ok = fpe.evaluateToBoolean(null, element, n);
      fpeTime = fpeTime + (System.nanoTime() - t);
      msg = fpe.forLog();
    } catch (Exception ex) {
      throw new FHIRException("Problem evaluating slicing expression for element in profile " + profile.getUrl() + " path " + path + ": " + ex.getMessage());
    }
    return ok;
  }

  // we assume that the following things are true:
  // the instance at root is valid against the schema and schematron
  // the instance validator had no issues against the base resource profile
  private void start(Object appContext, List<ValidationMessage> errors, Element resource, Element element, StructureDefinition defn, NodeStack stack) throws FHIRException, FHIRException, IOException {
    // profile is valid, and matches the resource name
    ResourceProfiles resourceProfiles = getResourceProfiles(element, stack);
    if (!resourceProfiles.isProcessed())
      checkDeclaredProfiles(resourceProfiles, errors, resource, element, stack);
    
    if (!resourceProfiles.isProcessed()) {
      resourceProfiles.setProcessed();
      if (!resourceProfiles.hasProfiles() &&
          (rule(errors, IssueType.STRUCTURE, element.line(), element.col(), stack.getLiteralPath(), defn.hasSnapshot(),
              "StructureDefinition has no snapshot - validation is against the snapshot, so it must be provided"))) {
        // Don't need to validate against the resource if there's a profile because the profile snapshot will include the relevant parts of the resources
        validateElement(appContext, errors, defn, defn.getSnapshot().getElement().get(0), null, null, resource, element, element.getName(), stack, false);
      }

      // specific known special validations
      if (element.getType().equals("Bundle"))
        validateBundle(errors, element, stack);
      if (element.getType().equals("Observation"))
        validateObservation(errors, element, stack);
      if (element.getType().equals("QuestionnaireResponse"))
        validateQuestionannaireResponse(errors, element, stack);
    }
    for (ProfileUsage profileUsage : resourceProfiles.uncheckedProfiles()) {
      profileUsage.setChecked();
      validateElement(appContext, errors, profileUsage.getProfile(), profileUsage.getProfile().getSnapshot().getElement().get(0), null, null, resource, element, element.getName(), stack, false);
    }
  }

  private void validateQuestionannaireResponse(List<ValidationMessage> errors, Element element, NodeStack stack) {
    Element q = element.getNamedChild("questionnaire");
    if (hint(errors, IssueType.REQUIRED, element.line(), element.col(), stack.getLiteralPath(), q != null && isNotBlank(q.getNamedChildValue("reference")), "No questionnaire is identified, so no validation can be performed against the base questionnaire")) {
      long t = System.nanoTime();
      Questionnaire qsrc = context.fetchResource(Questionnaire.class, q.getNamedChildValue("reference"));
      sdTime = sdTime + (System.nanoTime() - t);
      if (warning(errors, IssueType.REQUIRED, q.line(), q.col(), stack.getLiteralPath(), qsrc != null, "The questionnaire could not be resolved, so no validation can be performed against the base questionnaire")) {
        boolean inProgress = "in-progress".equals(element.getNamedChildValue("status"));
        validateQuestionannaireResponseItems(qsrc, qsrc.getItem(), errors, element, stack, inProgress);        
      }
    }
  }

  private void validateQuestionannaireResponseItem(Questionnaire qsrc, QuestionnaireItemComponent qItem, List<ValidationMessage> errors, Element element, NodeStack stack, boolean inProgress) {
    String text = element.getNamedChildValue("text");
    rule(errors, IssueType.INVALID, element.line(), element.col(), stack.getLiteralPath(), Utilities.noString(text) || text.equals(qItem.getText()), "If text exists, it must match the questionnaire definition for linkId "+qItem.getLinkId());

    List<Element> answers = new ArrayList<Element>();
    element.getNamedChildren("answer", answers);
    if (inProgress)
      warning(errors, IssueType.REQUIRED, element.line(), element.col(), stack.getLiteralPath(), (answers.size() > 0) || !qItem.getRequired(), "No response answer found for required item "+qItem.getLinkId());
    else
      rule(errors, IssueType.REQUIRED, element.line(), element.col(), stack.getLiteralPath(), (answers.size() > 0) || !qItem.getRequired(), "No response answer found for required item "+qItem.getLinkId());
    if (answers.size() > 1)
      rule(errors, IssueType.INVALID, answers.get(1).line(), answers.get(1).col(), stack.getLiteralPath(), qItem.getRepeats(), "Only one response answer item with this linkId allowed");

    for (Element answer : answers) {
      NodeStack ns = stack.push(answer, -1, null, null);
      switch (qItem.getType()) {
      case GROUP: 
        rule(errors, IssueType.STRUCTURE, answer.line(), answer.col(), stack.getLiteralPath(), false, "Items of type group should not have answers"); 
        break;
      case DISPLAY:  // nothing
        break;
      case BOOLEAN:       
        validateQuestionnaireResponseItemType(errors, answer, ns, "boolean");
        break;
      case DECIMAL:       
        validateQuestionnaireResponseItemType(errors, answer, ns, "decimal");
        break;
      case INTEGER:       
        validateQuestionnaireResponseItemType(errors, answer, ns, "integer");
        break;
      case DATE:          
        validateQuestionnaireResponseItemType(errors, answer, ns, "date");
        break;
      case DATETIME:      
        validateQuestionnaireResponseItemType(errors, answer, ns, "dateTime");
        break;
      case TIME:          
        validateQuestionnaireResponseItemType(errors, answer, ns, "time");
        break;
      case STRING:        
        validateQuestionnaireResponseItemType(errors, answer, ns, "string");
        break;
      case TEXT:          
        validateQuestionnaireResponseItemType(errors, answer, ns, "text");
        break;
      case URL:           
        validateQuestionnaireResponseItemType(errors, answer, ns, "uri");
        break;
      case ATTACHMENT:    
        validateQuestionnaireResponseItemType(errors, answer, ns, "Attachment");
        break;
      case REFERENCE:     
        validateQuestionnaireResponseItemType(errors, answer, ns, "Reference");
        break;
      case QUANTITY:   
        if (validateQuestionnaireResponseItemType(errors, answer, ns, "Quantity").equals("Quantity"))
          if (qItem.hasExtension("???"))
            validateQuestionnaireResponseItemQuantity(errors, answer, ns);
        break;
      case CHOICE:     
        String itemType=validateQuestionnaireResponseItemType(errors, answer, ns, "Coding", "date", "time", "integer", "string");
        if (itemType.equals("Coding")) validateAnswerCode(errors, answer, ns, qsrc, qItem, false);
        else if (itemType.equals("date")) checkOption(errors, answer, ns, qsrc, qItem, "date");
        else if (itemType.equals("time")) checkOption(errors, answer, ns, qsrc, qItem, "time");
        else if (itemType.equals("integer")) checkOption(errors, answer, ns, qsrc, qItem, "integer");
        else if (itemType.equals("string")) checkOption(errors, answer, ns, qsrc, qItem, "string");
        break;
      case OPENCHOICE: 
        itemType=validateQuestionnaireResponseItemType(errors, answer, ns, "Coding", "date", "time", "integer", "string");
        if (itemType.equals("Coding")) validateAnswerCode(errors, answer, ns, qsrc, qItem, true);
        else if (itemType.equals("date")) checkOption(errors, answer, ns, qsrc, qItem, "date");
        else if (itemType.equals("time")) checkOption(errors, answer, ns, qsrc, qItem, "time");
        else if (itemType.equals("integer")) checkOption(errors, answer, ns, qsrc, qItem, "integer");
        else if (itemType.equals("string")) checkOption(errors, answer, ns, qsrc, qItem, "string", true);
        break;
      }
      validateQuestionannaireResponseItems(qsrc, qItem.getItem(), errors, answer, stack, inProgress);
    }
    if (qItem.getType() == null) {
      fail(errors, IssueType.REQUIRED, element.line(), element.col(), stack.getLiteralPath(), false, "Definition for item "+qItem.getLinkId() + " does not contain a type");
    } else if (qItem.getType() == QuestionnaireItemType.GROUP) {
      validateQuestionannaireResponseItems(qsrc, qItem.getItem(), errors, element, stack, inProgress);
    } else {
      List<Element> items = new ArrayList<Element>();
      element.getNamedChildren("item", items);
      for (Element item : items) {
        NodeStack ns = stack.push(item, -1, null, null);
        rule(errors, IssueType.STRUCTURE, answers.get(0).line(), answers.get(0).col(), stack.getLiteralPath(), false, "Items not of type group should not have items - Item with linkId {0} of type {1} has {2} item(s)", qItem.getLinkId(), qItem.getType(), items.size());
      }
    }
  }

  private void validateQuestionannaireResponseItem(Questionnaire qsrc, QuestionnaireItemComponent qItem, List<ValidationMessage> errors, List<Element> elements, NodeStack stack, boolean inProgress) {
    if (elements.size() > 1)
      rule(errors, IssueType.INVALID, elements.get(1).line(), elements.get(1).col(), stack.getLiteralPath(), qItem.getRepeats(), "Only one response item with this linkId allowed");
    for (Element element : elements) {
      NodeStack ns = stack.push(element, -1, null, null);
      validateQuestionannaireResponseItem(qsrc, qItem, errors, element, ns, inProgress);
    }
  }

  private int getLinkIdIndex(List<QuestionnaireItemComponent> qItems, String linkId) {
    for (int i = 0; i < qItems.size(); i++) {
      if (linkId.equals(qItems.get(i).getLinkId()))
        return i;
    }
    return -1;
  }

  private void validateQuestionannaireResponseItems(Questionnaire qsrc, List<QuestionnaireItemComponent> qItems, List<ValidationMessage> errors, Element element, NodeStack stack, boolean inProgress) {
    List<Element> items = new ArrayList<Element>();
    element.getNamedChildren("item", items);
    // now, sort into stacks
    Map<String, List<Element>> map = new HashMap<String, List<Element>>();
    int lastIndex = -1;
    for (Element item : items) {
      String linkId = item.getNamedChildValue("linkId");
      if (rule(errors, IssueType.REQUIRED, item.line(), item.col(), stack.getLiteralPath(), !Utilities.noString(linkId), "No LinkId, so can't be validated")) {
        int index = getLinkIdIndex(qItems, linkId);
        if (index == -1) {
          QuestionnaireItemComponent qItem = findQuestionnaireItem(qsrc, linkId);
          if (qItem != null) {
            rule(errors, IssueType.STRUCTURE, item.line(), item.col(), stack.getLiteralPath(), index > -1, "Structural Error: item is in the wrong place");
            NodeStack ns = stack.push(item, -1, null, null);
            validateQuestionannaireResponseItem(qsrc, qItem, errors, element, ns, inProgress);
          }
          else
            rule(errors, IssueType.NOTFOUND, item.line(), item.col(), stack.getLiteralPath(), index > -1, "LinkId \""+linkId+"\" not found in questionnaire");
        }
        else
        {
          rule(errors, IssueType.STRUCTURE, item.line(), item.col(), stack.getLiteralPath(), index >= lastIndex, "Structural Error: items are out of order");
          lastIndex = index;
          List<Element> mapItem = map.get(linkId);
          if (mapItem == null) {
            mapItem = new ArrayList<Element>();
            map.put(linkId, mapItem);
          }
          mapItem.add(item);
        }
      }
    }

    // ok, now we have a list of known items, grouped by linkId. We"ve made an error for anything out of order
    for (QuestionnaireItemComponent qItem : qItems) {
      List<Element> mapItem = map.get(qItem.getLinkId());
      if (mapItem != null)
        validateQuestionannaireResponseItem(qsrc, qItem, errors, mapItem, stack, inProgress);
      else
        rule(errors, IssueType.REQUIRED, element.line(), element.col(), stack.getLiteralPath(), !qItem.getRequired(), "No response found for required item "+qItem.getLinkId());
    }
  }

  private void validateQuestionnaireResponseItemQuantity( List<ValidationMessage> errors, Element answer, NodeStack stack)	{

  }

  private String validateQuestionnaireResponseItemType(List<ValidationMessage> errors, Element element, NodeStack stack, String... types) {
    List<Element> values = new ArrayList<Element>();
    element.getNamedChildrenWithWildcard("value[x]", values);
    if (values.size() > 0) {
      NodeStack ns = stack.push(values.get(0), -1, null, null);
      CommaSeparatedStringBuilder l = new CommaSeparatedStringBuilder();
      for (String s : types)  {
        l.append(s);
        if (values.get(0).getName().equals("value"+Utilities.capitalize(s))) 
          return(s);
      }
      if (types.length == 1)
        rule(errors, IssueType.STRUCTURE, values.get(0).line(), values.get(0).col(), ns.getLiteralPath(), false, "Answer value must be of type "+types[0]);
      else
        rule(errors, IssueType.STRUCTURE, values.get(0).line(), values.get(0).col(), ns.getLiteralPath(), false, "Answer value must be one of the types "+l.toString());
    }
    return null;
  }

  private QuestionnaireItemComponent findQuestionnaireItem(Questionnaire qSrc, String linkId) {
    return findItem(qSrc.getItem(), linkId);
  }

  private QuestionnaireItemComponent findItem(List<QuestionnaireItemComponent> list, String linkId) {
    for (QuestionnaireItemComponent item : list) {
      if (linkId.equals(item.getLinkId()))
        return item;
      QuestionnaireItemComponent result = findItem(item.getItem(), linkId);
      if (result != null)
        return result;
    }
    return null;
  }

  /*	private void validateAnswerCode(List<ValidationMessage> errors, Element value, NodeStack stack, List<Coding> optionList) {
	  String system = value.getNamedChildValue("system");
	  String code = value.getNamedChildValue("code");
	  boolean found = false;
	  for (Coding c : optionList) {
      if (ObjectUtil.equals(c.getSystem(), system) && ObjectUtil.equals(c.getCode(), code)) {
	      found = true;
	      break;
	    }
	  }
	  rule(errors, IssueType.STRUCTURE, value.line(), value.col(), stack.getLiteralPath(), found, "The code "+system+"::"+code+" is not a valid option");
	}*/

  private void validateAnswerCode(List<ValidationMessage> errors, Element value, NodeStack stack, Questionnaire qSrc, Reference ref, boolean theOpenChoice) {
    ValueSet vs = resolveBindingReference(qSrc, ref, qSrc.getUrl());
    if (warning(errors, IssueType.CODEINVALID, value.line(), value.col(), stack.getLiteralPath(), vs != null, "ValueSet " + describeReference(ref) + " not found"))  {
      try {
        Coding c = readAsCoding(value);
        if (isBlank(c.getCode()) && isBlank(c.getSystem()) && isNotBlank(c.getDisplay())) {
          if (theOpenChoice) {
            return;
          }
        }

        long t = System.nanoTime();
        ValidationResult res = context.validateCode(c, vs);
        txTime = txTime + (System.nanoTime() - t);
        if (!res.isOk())
          rule(errors, IssueType.CODEINVALID, value.line(), value.col(), stack.getLiteralPath(), false, "The value provided ("+c.getSystem()+"::"+c.getCode()+") is not in the options value set in the questionnaire");
      } catch (Exception e) {
        warning(errors, IssueType.CODEINVALID, value.line(), value.col(), stack.getLiteralPath(), false, "Error " + e.getMessage() + " validating Coding against Questionnaire Options");
      }
    }
  }

  private void validateAnswerCode( List<ValidationMessage> errors, Element answer, NodeStack stack, Questionnaire qSrc, QuestionnaireItemComponent qItem, boolean theOpenChoice) {
    Element v = answer.getNamedChild("valueCoding");
    NodeStack ns = stack.push(v, -1, null, null);
    if (qItem.getOption().size() > 0)
      checkCodingOption(errors, answer, stack, qSrc, qItem, theOpenChoice);
    //	    validateAnswerCode(errors, v, stack, qItem.getOption());
    else if (qItem.hasOptions())
      validateAnswerCode(errors, v, stack, qSrc, qItem.getOptions(), theOpenChoice);
    else
      hint(errors, IssueType.STRUCTURE, v.line(), v.col(), stack.getLiteralPath(), false, "Cannot validate options because no option or options are provided");
  }

  private void checkOption( List<ValidationMessage> errors, Element answer, NodeStack stack, Questionnaire qSrc, QuestionnaireItemComponent qItem, String type) {
    checkOption(errors, answer, stack, qSrc,  qItem, type, false);
  }

  private void checkOption( List<ValidationMessage> errors, Element answer, NodeStack stack, Questionnaire qSrc, QuestionnaireItemComponent qItem, String type, boolean openChoice) {
    if (type.equals("integer"))     checkIntegerOption(errors, answer, stack, qSrc, qItem, openChoice);
    else if (type.equals("date"))   checkDateOption(errors, answer, stack, qSrc, qItem, openChoice);
    else if (type.equals("time"))   checkTimeOption(errors, answer, stack, qSrc, qItem, openChoice);
    else if (type.equals("string")) checkStringOption(errors, answer, stack, qSrc, qItem, openChoice);
    else if (type.equals("Coding")) checkCodingOption(errors, answer, stack, qSrc, qItem, openChoice);
  }

  private void checkIntegerOption( List<ValidationMessage> errors, Element answer, NodeStack stack, Questionnaire qSrc, QuestionnaireItemComponent qItem, boolean openChoice) {
    Element v = answer.getNamedChild("valueInteger");
    NodeStack ns = stack.push(v, -1, null, null);
    if (qItem.getOption().size() > 0) {
      List<IntegerType> list = new ArrayList<IntegerType>();
      for (QuestionnaireItemOptionComponent components : qItem.getOption())  {
        try {
          list.add(components.getValueIntegerType());
        } catch (FHIRException e) {
          // If it's the wrong type, just keep going
        }
      }
      if (list.isEmpty() && !openChoice) {
        rule(errors, IssueType.STRUCTURE, v.line(), v.col(), stack.getLiteralPath(), false, "Option list has no option values of type integer");
      } else {
        boolean found = false;
        for (IntegerType item : list) {
          if (item.getValue() == Integer.parseInt(v.primitiveValue())) {
            found = true;
            break;
          }
        }
        if (!found) {
          rule(errors, IssueType.STRUCTURE, v.line(), v.col(), stack.getLiteralPath(), found, "The integer "+v.primitiveValue()+" is not a valid option");
        }
      }
    } else
      hint(errors, IssueType.STRUCTURE, v.line(), v.col(), stack.getLiteralPath(), false, "Cannot validate integer answer option because no option list is provided");
  }

  private void checkDateOption( List<ValidationMessage> errors, Element answer, NodeStack stack, Questionnaire qSrc, QuestionnaireItemComponent qItem, boolean openChoice) {
    Element v = answer.getNamedChild("valueDate");
    NodeStack ns = stack.push(v, -1, null, null);
    if (qItem.getOption().size() > 0) {
      List<DateType> list = new ArrayList<DateType>();
      for (QuestionnaireItemOptionComponent components : qItem.getOption())  {
        try {
          list.add(components.getValueDateType());
        } catch (FHIRException e) {
          // If it's the wrong type, just keep going
        }
      }
      if (list.isEmpty() && !openChoice) {
        rule(errors, IssueType.STRUCTURE, v.line(), v.col(), stack.getLiteralPath(), false, "Option list has no option values of type date");
      } else {
        boolean found = false;
        for (DateType item : list) {
          if (item.getValue().equals(v.primitiveValue())) {
            found = true;
            break;
          }
        }
        if (!found) {
          rule(errors, IssueType.STRUCTURE, v.line(), v.col(), stack.getLiteralPath(), found, "The date "+v.primitiveValue()+" is not a valid option");
        }
      }
    } else
      hint(errors, IssueType.STRUCTURE, v.line(), v.col(), stack.getLiteralPath(), false, "Cannot validate date answer option because no option list is provided");
  }

  private void checkTimeOption( List<ValidationMessage> errors, Element answer, NodeStack stack, Questionnaire qSrc, QuestionnaireItemComponent qItem, boolean openChoice) {
    Element v = answer.getNamedChild("valueTime");
    NodeStack ns = stack.push(v, -1, null, null);
    if (qItem.getOption().size() > 0) {
      List<TimeType> list = new ArrayList<TimeType>();
      for (QuestionnaireItemOptionComponent components : qItem.getOption())  {
        try {
          list.add(components.getValueTimeType());
        } catch (FHIRException e) {
          // If it's the wrong type, just keep going
        }
      }
      if (list.isEmpty() && !openChoice) {
        rule(errors, IssueType.STRUCTURE, v.line(), v.col(), stack.getLiteralPath(), false, "Option list has no option values of type time");
      } else {
        boolean found = false;
        for (TimeType item : list) {
          if (item.getValue().equals(v.primitiveValue())) {
            found = true;
            break;
          }
        }
        if (!found) {
          rule(errors, IssueType.STRUCTURE, v.line(), v.col(), stack.getLiteralPath(), found, "The time "+v.primitiveValue()+" is not a valid option");
        }
      }
    } else
      hint(errors, IssueType.STRUCTURE, v.line(), v.col(), stack.getLiteralPath(), false, "Cannot validate time answer option because no option list is provided");
  }

  private void checkStringOption( List<ValidationMessage> errors, Element answer, NodeStack stack, Questionnaire qSrc, QuestionnaireItemComponent qItem, boolean openChoice) {
    Element v = answer.getNamedChild("valueString");
    NodeStack ns = stack.push(v, -1, null, null);
    if (qItem.getOption().size() > 0) {
      List<StringType> list = new ArrayList<StringType>();
      for (QuestionnaireItemOptionComponent components : qItem.getOption())  {
        try {
          if (components.getValue() != null) {
            list.add(components.getValueStringType());
          }
        } catch (FHIRException e) {
          // If it's the wrong type, just keep going
        }
      }
      if (list.isEmpty() && !openChoice) {
        rule(errors, IssueType.STRUCTURE, v.line(), v.col(), stack.getLiteralPath(), false, "Option list has no option values of type string");
      } else {
        boolean found = false;
        for (StringType item : list) {
          if (item.getValue().equals((v.primitiveValue()))) {
            found = true;
            break;
          }
        }
        if (!found) {
          rule(errors, IssueType.STRUCTURE, v.line(), v.col(), stack.getLiteralPath(), found, "The string "+v.primitiveValue()+" is not a valid option");
        }
      }
    } else {
      hint(errors, IssueType.STRUCTURE, v.line(), v.col(), stack.getLiteralPath(), false, "Cannot validate string answer option because no option list is provided");
    }
  }

  private void checkCodingOption( List<ValidationMessage> errors, Element answer, NodeStack stack, Questionnaire qSrc, QuestionnaireItemComponent qItem, boolean openChoice) {
    Element v = answer.getNamedChild("valueCoding");
    String system = v.getNamedChildValue("system");
    String code = v.getNamedChildValue("code");
    NodeStack ns = stack.push(v, -1, null, null);
    if (qItem.getOption().size() > 0) {
      List<Coding> list = new ArrayList<Coding>();
      for (QuestionnaireItemOptionComponent components : qItem.getOption())  {
        try {
          if (components.getValue() != null) {
            list.add(components.getValueCoding());
          }
        } catch (FHIRException e) {
          // If it's the wrong type, just keep going
        }
      }
      if (list.isEmpty() && !openChoice) {
        rule(errors, IssueType.STRUCTURE, v.line(), v.col(), stack.getLiteralPath(), false, "Option list has no option values of type coding");
      } else {
        boolean found = false;
        for (Coding item : list) {
          if (ObjectUtil.equals(item.getSystem(), system) && ObjectUtil.equals(item.getCode(), code)) {
            found = true;
            break;
          }
        }
        if (!found) {
          rule(errors, IssueType.STRUCTURE, v.line(), v.col(), stack.getLiteralPath(), found, "The code "+system+"::"+code+" is not a valid option");
        }
      }
    } else
      hint(errors, IssueType.STRUCTURE, v.line(), v.col(), stack.getLiteralPath(), false, "Cannot validate Coding option because no option list is provided");
  }

  private String tail(String path) {
    return path.substring(path.lastIndexOf(".") + 1);
  }

  private String tryParse(String ref)  {
    String[] parts = ref.split("\\/");
    switch (parts.length) {
    case 1:
      return null;
    case 2:
      return checkResourceType(parts[0]);
    default:
      if (parts[parts.length - 2].equals("_history"))
        return checkResourceType(parts[parts.length - 4]);
      else
        return checkResourceType(parts[parts.length - 2]);
    }
  }

  private boolean typesAreAllReference(List<TypeRefComponent> theType) {
    for (TypeRefComponent typeRefComponent : theType) {
      if (typeRefComponent.getCode().equals("Reference") == false) {
        return false;
      }
    }
    return true;
  }

  private void validateBundle(List<ValidationMessage> errors, Element bundle, NodeStack stack) {
    List<Element> entries = new ArrayList<Element>();
    bundle.getNamedChildren("entry", entries);
    String type = bundle.getNamedChildValue("type");
    if (entries.size() == 0) {
      rule(errors, IssueType.INVALID, stack.getLiteralPath(), !(type.equals("document") || type.equals("message")), "Documents or Messages must contain at least one entry");
    } else {
      Element firstEntry = entries.get(0);
      NodeStack firstStack = stack.push(firstEntry, 0, null, null);
      String fullUrl = firstEntry.getNamedChildValue("fullUrl");

      if (type.equals("document")) {
        Element resource = firstEntry.getNamedChild("resource");
        NodeStack localStack = firstStack.push(resource, -1, null, null);
        String id = resource.getNamedChildValue("id");
        if (rule(errors, IssueType.INVALID, firstEntry.line(), firstEntry.col(), stack.addToLiteralPath("entry", ":0"), resource != null, "No resource on first entry")) {
          validateDocument(errors, entries, resource, localStack.push(resource, -1, null, null), fullUrl, id);
        }
        checkAllInterlinked(errors, entries, stack, bundle);
      }
      if (type.equals("message")) {
        Element resource = firstEntry.getNamedChild("resource");
        NodeStack localStack = firstStack.push(resource, -1, null, null);
        String id = resource.getNamedChildValue("id");
        if (rule(errors, IssueType.INVALID, firstEntry.line(), firstEntry.col(), stack.addToLiteralPath("entry", ":0"), resource != null, "No resource on first entry")) {
          validateMessage(errors, entries, resource, localStack.push(resource, -1, null, null), fullUrl, id);
        }
        checkAllInterlinked(errors, entries, stack, bundle);
      }
    }
  }

  private void checkAllInterlinked(List<ValidationMessage> errors, List<Element> entries, NodeStack stack, Element bundle) {
    List<Element> visitedResources = new ArrayList<Element>();
    HashMap<Element,Element> candidateEntries = new HashMap<Element,Element>();
    List<Element> candidateResources = new ArrayList<Element>();
    for (Element entry: entries) {
      candidateEntries.put(entry.getNamedChild("resource"), entry);
      candidateResources.add(entry.getNamedChild("resource"));
    }
    followResourceLinks(entries.get(0), visitedResources, candidateEntries, candidateResources, true, errors, stack);
    List<Element> unusedResources = new ArrayList<Element>();
    unusedResources.addAll(candidateResources);
    unusedResources.removeAll(visitedResources);
    int i = 0;
    for (Element entry : entries) {
      warning(errors, IssueType.INFORMATIONAL, entry.line(), entry.col(), stack.addToLiteralPath("entry", Integer.toString(i)), !unusedResources.contains(entry.getNamedChild("resource")), "Entry isn't reachable by traversing from first Bundle entry");
      i++;
    }
    // Todo - check if the remaining resources point *to* the elements in the referenced set.  Any that are still left over are errors
  }

  private void followResourceLinks(Element entry, List<Element> visitedResources, HashMap<Element, Element> candidateEntries, List<Element> candidateResources, boolean referenced, List<ValidationMessage> errors, NodeStack stack) {
    Element resource = entry.getNamedChild("resource");
    if (visitedResources.contains(resource))
      return;

    if (referenced)
      visitedResources.add(resource);

    List<String> references = findReferences(resource);
    for (String reference: references) {
      Element r = getFromBundle(stack.getElement(), reference, entry.getChildValue("fullUrl"), errors, stack.addToLiteralPath("entry:" + candidateResources.indexOf(resource)));
      if (r!=null && !visitedResources.contains(r)) {
        followResourceLinks(candidateEntries.get(r), visitedResources, candidateEntries, candidateResources, referenced, errors, stack);
      }
    }
  }

  private List<String> findReferences(Element start) {
    List<String> references = new ArrayList<String>();
    findReferences(start, references);
    return references;
  }

  private void findReferences(Element start, List<String> references) {
    for (Element child : start.getChildren()) {
      if (child.getType().equals("Reference")) {
        String ref = child.getChildValue("reference");
        if (ref!=null && !ref.startsWith("#"))
          references.add(ref);
      }
      findReferences(child, references);
    }
  }

  private void validateBundleReference(List<ValidationMessage> errors, List<Element> entries, Element ref, String name, NodeStack stack, String fullUrl, String type, String id) {
    String reference = null;
    try {
      reference = ref.getNamedChildValue("reference");
    } catch (Error e) {
      
    }
        
    if (ref != null && !Utilities.noString(reference)) {
      Element target = resolveInBundle(entries, reference, fullUrl, type, id);
      rule(errors, IssueType.INVALID, ref.line(), ref.col(), stack.addToLiteralPath("reference"), target != null, "Unable to resolve the target of the reference in the bundle (" + name + ")");
    }
  }

  private void validateContains(Object appContext, List<ValidationMessage> errors, String path, ElementDefinition child, ElementDefinition context, Element resource, Element element, NodeStack stack, IdStatus idstatus) throws FHIRException, FHIRException, IOException {
    String resourceName = element.getType();
    long t = System.nanoTime();
    StructureDefinition profile = this.context.fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/" + resourceName);
    sdTime = sdTime + (System.nanoTime() - t);
    // special case: resource wrapper is reset if we're crossing a bundle boundary, but not otherwise
    if (element.getSpecial() == SpecialElement.BUNDLE_ENTRY || element.getSpecial() == SpecialElement.BUNDLE_OUTCOME || element.getSpecial() == SpecialElement.PARAMETER ) 
      resource = element;
    if (rule(errors, IssueType.INVALID, element.line(), element.col(), stack.getLiteralPath(), profile != null, "No profile found for contained resource of type '" + resourceName + "'"))
      validateResource(appContext, errors, resource, element, profile, null, idstatus, stack);
  }

  private void validateDocument(List<ValidationMessage> errors, List<Element> entries, Element composition, NodeStack stack, String fullUrl, String id) {
    // first entry must be a composition
    if (rule(errors, IssueType.INVALID, composition.line(), composition.col(), stack.getLiteralPath(), composition.getType().equals("Composition"),
        "The first entry in a document must be a composition")) {
      // the composition subject and section references must resolve in the bundle
      Element elem = composition.getNamedChild("subject");
      if (rule(errors, IssueType.INVALID, composition.line(), composition.col(), stack.getLiteralPath(), elem != null, "A document composition must have a subject"))
        validateBundleReference(errors, entries, elem, "Composition Subject", stack.push(elem, -1, null, null), fullUrl, "Composition", id);
      validateSections(errors, entries, composition, stack, fullUrl, id);
    }
  }
  // rule(errors, IssueType.INVALID, bundle.line(), bundle.col(), "Bundle", !"urn:guid:".equals(base), "The base 'urn:guid:' is not valid (use urn:uuid:)");
  // rule(errors, IssueType.INVALID, entry.line(), entry.col(), localStack.getLiteralPath(), !"urn:guid:".equals(ebase), "The base 'urn:guid:' is not valid");
  // rule(errors, IssueType.INVALID, entry.line(), entry.col(), localStack.getLiteralPath(), !Utilities.noString(base) || !Utilities.noString(ebase), "entry
  // does not have a base");
  // String firstBase = null;
  // firstBase = ebase == null ? base : ebase;

  private void validateElement(Object appContext, List<ValidationMessage> errors, StructureDefinition profile, ElementDefinition definition, StructureDefinition cprofile, ElementDefinition context,
      Element resource, Element element, String actualType, NodeStack stack, boolean inCodeableConcept) throws FHIRException, FHIRException, IOException {
    element.markValidation(profile, definition);

    //		System.out.println("  "+stack.getLiteralPath()+" "+Long.toString((System.nanoTime() - time) / 1000000));
    //		time = System.nanoTime();
    if (resource.getName().equals("contained")) {
      NodeStack ancestor = stack;
      while (!ancestor.element.isResource() || ancestor.element.getName().equals("contained"))
        ancestor = ancestor.parent;
      checkInvariants(errors, stack.getLiteralPath(), profile, definition, null, null, ancestor.element, element);
    } else
      checkInvariants(errors, stack.getLiteralPath(), profile, definition, null, null, resource, element);
    if (definition.getFixed()!=null)
      checkFixedValue(errors, stack.getLiteralPath(), element, definition.getFixed(), definition.getSliceName(), null);


    // get the list of direct defined children, including slices
    List<ElementDefinition> childDefinitions = ProfileUtilities.getChildMap(profile, definition);
    if (childDefinitions.isEmpty()) {
      if (actualType == null)
        return; // there'll be an error elsewhere in this case, and we're going to stop.

      StructureDefinition dt = this.context.fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/" + actualType);
      if (dt == null)
        throw new DefinitionException("Unable to resolve actual type " + actualType);

      childDefinitions = ProfileUtilities.getChildMap(dt, dt.getSnapshot().getElement().get(0));
    }

    // 1. List the children, and remember their exact path (convenience)
    List<ElementInfo> children = new ArrayList<InstanceValidator.ElementInfo>();
    ChildIterator iter = new ChildIterator(stack.getLiteralPath(), element);
    while (iter.next())
      children.add(new ElementInfo(iter.name(), iter.element(), iter.path(), iter.count()));

    // 2. assign children to a definition
    // for each definition, for each child, check whether it belongs in the slice
    ElementDefinition slicer = null;
    boolean unsupportedSlicing = false;
    List<String> problematicPaths = new ArrayList<String>();
    String slicingPath = null;
    int sliceOffset = 0;
    for (int i = 0; i < childDefinitions.size(); i++) {
      ElementDefinition ed = childDefinitions.get(i);
      boolean childUnsupportedSlicing = false;
      boolean process = true;
      if (ed.hasSlicing() && !ed.getSlicing().getOrdered())
        slicingPath = ed.getPath();
      else if (slicingPath!=null && ed.getPath().equals(slicingPath))
        ; // nothing
      else if (slicingPath != null && !ed.getPath().startsWith(slicingPath))
        slicingPath = null;
      // where are we with slicing
      if (ed.hasSlicing()) {
        if (slicer != null && slicer.getPath().equals(ed.getPath()))
          throw new DefinitionException("Slice encountered midway through path on " + slicer.getPath());
        slicer = ed;
        process = false;
        sliceOffset = i;
      } else if (slicer != null && !slicer.getPath().equals(ed.getPath()))
        slicer = null;

//      if (process) {
        for (ElementInfo ei : children) {
          boolean match = false;
          if (slicer == null || slicer == ed) {
            match = nameMatches(ei.name, tail(ed.getPath()));
          } else {
//            ei.slice = slice;
            if (nameMatches(ei.name, tail(ed.getPath())))
              try {
                match = sliceMatches(appContext, ei.element, ei.path, slicer, ed, profile, errors, stack);
                if (match)
                  ei.slice = slicer;
              } catch (FHIRException e) {
                warning(errors, IssueType.PROCESSING, ei.line(), ei.col(), ei.path, false, e.getMessage());
                unsupportedSlicing = true;
                childUnsupportedSlicing = true;
              }
          }
          if (match) {
            if (rule(errors, IssueType.INVALID, ei.line(), ei.col(), ei.path, ei.definition == null || ei.definition == slicer, "Profile " + profile.getUrl() + ", Element matches more than one slice")) {
              ei.definition = ed;
              if (ei.slice == null) {
                ei.index = i;
              } else {
                ei.index = sliceOffset;
                ei.sliceindex = i - (sliceOffset + 1);
              }
            }
          } else if (childUnsupportedSlicing) {
            problematicPaths.add(ed.getPath());
          }
        }
//      }
    }
    int last = -1;
    int lastSlice = -1;
    for (ElementInfo ei : children) {
      String sliceInfo = "";
      if (slicer != null)
        sliceInfo = " (slice: " + slicer.getPath()+")";
      if (ei.path.endsWith(".extension"))
        rule(errors, IssueType.INVALID, ei.line(), ei.col(), ei.path, ei.definition != null, "Element is unknown or does not match any slice (url=\"" + ei.element.getNamedChildValue("url") + "\")" + (profile==null ? "" : " for profile " + profile.getUrl()));
      else if (!unsupportedSlicing)
        if (ei.slice!=null && (ei.slice.getSlicing().getRules().equals(ElementDefinition.SlicingRules.OPEN) || ei.slice.getSlicing().getRules().equals(ElementDefinition.SlicingRules.OPEN)))
          hint(errors, IssueType.INFORMATIONAL, ei.line(), ei.col(), ei.path, (ei.definition != null),
              "Element " + ei.element.getName() + " is unknown or does not match any slice " + sliceInfo + (profile==null ? "" : " for profile " + profile.getUrl()));
        else
          if (ei.slice!=null && (ei.slice.getSlicing().getRules().equals(ElementDefinition.SlicingRules.OPEN) || ei.slice.getSlicing().getRules().equals(ElementDefinition.SlicingRules.OPEN)))
            rule(errors, IssueType.INVALID, ei.line(), ei.col(), ei.path, (ei.definition != null),
                "Element " + ei.element.getName() + " is unknown or does not match any slice " + sliceInfo + (profile==null ? "" : " for profile " + profile.getUrl()));
          else
            hint(errors, IssueType.NOTSUPPORTED, ei.line(), ei.col(), ei.path, (ei.definition != null),
                "Could not verify slice for profile " + profile.getUrl());
      // TODO: Should get the order of elements correct when parsing elements that are XML attributes vs. elements
      boolean isXmlAttr = false;
      if (ei.definition!=null)
        for (Enumeration<PropertyRepresentation> r : ei.definition.getRepresentation()) {
          if (r.getValue() == PropertyRepresentation.XMLATTR) {
            isXmlAttr = true;
            break;
          }
        }

      rule(errors, IssueType.INVALID, ei.line(), ei.col(), ei.path, (ei.definition == null) || (ei.index >= last) || isXmlAttr, "As specified by profile " + profile.getUrl() + ", Element '"+ei.name+"' is out of order");
      if (ei.slice != null && ei.index == last && ei.slice.getSlicing().getOrdered())
        rule(errors, IssueType.INVALID, ei.line(), ei.col(), ei.path, (ei.definition == null) || (ei.sliceindex >= lastSlice) || isXmlAttr, "As specified by profile " + profile.getUrl() + ", Element  '"+ei.name+"' is out of order in ordered slice");
      if (ei.definition == null || !isXmlAttr)
        last = ei.index;
      if (ei.slice != null)
        lastSlice = ei.sliceindex;
      else
        lastSlice = -1;
    }

    // 3. report any definitions that have a cardinality problem
    for (ElementDefinition ed : childDefinitions) {
      if (ed.getRepresentation().isEmpty()) { // ignore xml attributes
        int count = 0;
        List<ElementDefinition> slices = null;
        if (ed.hasSlicing())
          slices = ProfileUtilities.getSliceList(profile, ed);
        for (ElementInfo ei : children)
          if (ei.definition == ed)
            count++;
          else if (slices!=null) {
            for (ElementDefinition sed : slices) {
              if (ei.definition == sed) {
                count++;
                break;
              }
            }
          }
        String location = "Profile " + profile.getUrl() + ", Element '" + stack.getLiteralPath() + "." + tail(ed.getPath()) + (ed.hasSliceName()? "[" + ed.getSliceName() + "]": "");
        if (ed.getMin() > 0) {
          if (problematicPaths.contains(ed.getPath()))
            hint(errors, IssueType.NOTSUPPORTED, element.line(), element.col(), stack.getLiteralPath(), count >= ed.getMin(),
            location + "': Unable to check minimum required (" + Integer.toString(ed.getMin()) + ") due to lack of slicing validation");
          else
            rule(errors, IssueType.STRUCTURE, element.line(), element.col(), stack.getLiteralPath(), count >= ed.getMin(),
            location + "': minimum required = " + Integer.toString(ed.getMin()) + ", but only found " + Integer.toString(count));
        }
        if (ed.hasMax() && !ed.getMax().equals("*")) {
          if (problematicPaths.contains(ed.getPath()))
            hint(errors, IssueType.NOTSUPPORTED, element.line(), element.col(), stack.getLiteralPath(), count <= Integer.parseInt(ed.getMax()),
            location + ": Unable to check max allowed (" + ed.getMax() + ") due to lack of slicing validation");
          else
            rule(errors, IssueType.STRUCTURE, element.line(), element.col(), stack.getLiteralPath(), count <= Integer.parseInt(ed.getMax()),
            location + ": max allowed = " + ed.getMax() + ", but found " + Integer.toString(count));
        }
      }
    }
    // 4. check order if any slices are ordered. (todo)

    // 5. inspect each child for validity
    for (ElementInfo ei : children) {
      List<String> profiles = new ArrayList<String>();
      if (ei.definition != null) {
        String type = null;
        ElementDefinition typeDefn = null;
        if (ei.definition.getType().size() == 1 && !ei.definition.getType().get(0).getCode().equals("*") && !ei.definition.getType().get(0).getCode().equals("Element")
            && !ei.definition.getType().get(0).getCode().equals("BackboneElement")) {
          type = ei.definition.getType().get(0).getCode();
          // Excluding reference is a kludge to get around versioning issues
          if (ei.definition.getType().get(0).hasProfile() && !type.equals("Reference"))
            profiles.add(ei.definition.getType().get(0).getProfile());
          
        } else if (ei.definition.getType().size() == 1 && ei.definition.getType().get(0).getCode().equals("*")) {
          String prefix = tail(ei.definition.getPath());
          assert prefix.endsWith("[x]");
          type = ei.name.substring(prefix.length() - 3);
          if (isPrimitiveType(type))
            type = Utilities.uncapitalize(type);
          // Excluding reference is a kludge to get around versioning issues
          if (ei.definition.getType().get(0).hasProfile() && !type.equals("Reference"))
            profiles.add(ei.definition.getType().get(0).getProfile());
        } else if (ei.definition.getType().size() > 1) {

          String prefix = tail(ei.definition.getPath());
          assert typesAreAllReference(ei.definition.getType()) || prefix.endsWith("[x]") : prefix;

          prefix = prefix.substring(0, prefix.length() - 3);
          for (TypeRefComponent t : ei.definition.getType())
            if ((prefix + Utilities.capitalize(t.getCode())).equals(ei.name)) {
              type = t.getCode();
              // Excluding reference is a kludge to get around versioning issues
              if (t.hasProfile() && !type.equals("Reference"))
                profiles.add(t.getProfile());
            }
          if (type == null) {
            TypeRefComponent trc = ei.definition.getType().get(0);
            if (trc.getCode().equals("Reference"))
              type = "Reference";
            else 
              rule(errors, IssueType.STRUCTURE, ei.line(), ei.col(), stack.getLiteralPath(), false,
                  "The element " + ei.name + " is illegal. Valid types at this point are " + describeTypes(ei.definition.getType()));
          }
        } else if (ei.definition.getContentReference() != null) {
          typeDefn = resolveNameReference(profile.getSnapshot(), ei.definition.getContentReference());
        }

        if (type != null) {
          if (type.startsWith("@")) {
            ei.definition = findElement(profile, type.substring(1));
            type = null;
          }
        }
        NodeStack localStack = stack.push(ei.element, ei.count, ei.definition, type == null ? typeDefn : resolveType(type));
        String localStackLiterapPath = localStack.getLiteralPath();
        String eiPath = ei.path;
        assert(eiPath.equals(localStackLiterapPath)) : "ei.path: " + ei.path + "  -  localStack.getLiteralPath: " + localStackLiterapPath;
        boolean thisIsCodeableConcept = false;

        if (type != null) {
          if (isPrimitiveType(type)) {
            checkPrimitive(appContext, errors, ei.path, type, ei.definition, ei.element, profile);
          } else {
            if (type.equals("Identifier"))
              checkIdentifier(errors, ei.path, ei.element, ei.definition);
            else if (type.equals("Coding"))
              checkCoding(errors, ei.path, ei.element, profile, ei.definition, inCodeableConcept);
            else if (type.equals("CodeableConcept")) {
              checkCodeableConcept(errors, ei.path, ei.element, profile, ei.definition);
              thisIsCodeableConcept = true;
            } else if (type.equals("Reference"))
              checkReference(appContext, errors, ei.path, ei.element, profile, ei.definition, actualType, localStack);

            // We only check extensions if we're not in a complex extension or if the element we're dealing with is not defined as part of that complex extension
            if (type.equals("Extension") && ei.element.getChildValue("url").contains("/"))
              checkExtension(appContext, errors, ei.path, resource, ei.element, ei.definition, profile, localStack);
            else if (type.equals("Resource"))
              validateContains(appContext, errors, ei.path, ei.definition, definition, resource, ei.element, localStack, idStatusForEntry(element, ei)); // if
            // (str.matches(".*([.,/])work\\1$"))
            else {
              StructureDefinition p = null;
              boolean elementValidated = false;
              if (profiles.isEmpty()) {
                p = getProfileForType(type);
                rule(errors, IssueType.STRUCTURE, ei.line(), ei.col(), ei.path, p != null, "Unknown type " + type);
              } else if (profiles.size()==1) {
                p = this.context.fetchResource(StructureDefinition.class, profiles.get(0));
                rule(errors, IssueType.STRUCTURE, ei.line(), ei.col(), ei.path, p != null, "Unknown profile " + profiles.get(0));
              } else {
                elementValidated = true;
                HashMap<String, List<ValidationMessage>> goodProfiles = new HashMap<String, List<ValidationMessage>>();
                List<List<ValidationMessage>> badProfiles = new ArrayList<List<ValidationMessage>>();
                for (String typeProfile : profiles) {
                  p = this.context.fetchResource(StructureDefinition.class, typeProfile);
                  if (rule(errors, IssueType.STRUCTURE, ei.line(), ei.col(), ei.path, p != null, "Unknown profile " + typeProfile)) {
                    List<ValidationMessage> profileErrors = new ArrayList<ValidationMessage>();
                    validateElement(appContext, profileErrors, p, p.getSnapshot().getElement().get(0), profile, ei.definition, resource, ei.element, type, localStack, thisIsCodeableConcept);
                    boolean hasError = false;
                    for (ValidationMessage msg : profileErrors) {
                      if (msg.getLevel()==ValidationMessage.IssueSeverity.ERROR || msg.getLevel()==ValidationMessage.IssueSeverity.FATAL) {
                        hasError = true;
                        break;
                      }
                    }
                    if (hasError)
                      badProfiles.add(profileErrors);
                    else
                      goodProfiles.put(typeProfile, profileErrors);
                  }
                  if (goodProfiles.size()==1) {
                    errors.addAll(goodProfiles.get(0));
                  } else if (goodProfiles.size()==0) {
                    rule(errors, IssueType.STRUCTURE, ei.line(), ei.col(), ei.path, false, "Unable to find matching profile among choices: " + StringUtils.join("; ", profiles));
                    for (List<ValidationMessage> messages : badProfiles) {
                      errors.addAll(messages);
                    }
                  } else {
                    warning(errors, IssueType.STRUCTURE, ei.line(), ei.col(), ei.path, false, "Found multiple matching profiles among choices: " + StringUtils.join("; ", goodProfiles.keySet()));
                    for (List<ValidationMessage> messages : goodProfiles.values()) {
                      errors.addAll(messages);
                    }                    
                  }
                }
              }
              if (p!=null) {
                if (!elementValidated)
                  validateElement(appContext, errors, p, p.getSnapshot().getElement().get(0), profile, ei.definition, resource, ei.element, type, localStack, thisIsCodeableConcept);
                int index = profile.getSnapshot().getElement().indexOf(ei.definition);
                if (index < profile.getSnapshot().getElement().size() - 1) {
                  String nextPath = profile.getSnapshot().getElement().get(index+1).getPath();
                  if (!nextPath.equals(ei.definition.getPath()) && nextPath.startsWith(ei.definition.getPath()))
                    validateElement(appContext, errors, profile, ei.definition, null, null, resource, ei.element, type, localStack, thisIsCodeableConcept);
                }
              }
            }
          }
        } else {
          if (rule(errors, IssueType.STRUCTURE, ei.line(), ei.col(), stack.getLiteralPath(), ei.definition != null, "Unrecognised Content " + ei.name))
            validateElement(appContext, errors, profile, ei.definition, null, null, resource, ei.element, type, localStack, false);
        }
      }
    }
  }

  private IdStatus idStatusForEntry(Element ep, ElementInfo ei) {
    if (isBundleEntry(ei.path)) {
      Element req = ep.getNamedChild("request");
      Element resp = ep.getNamedChild("response");
      Element fullUrl = ep.getNamedChild("fullUrl");
      Element method = null;
      Element url = null;
      if (req != null) {
        method = req.getNamedChild("method");
        url = req.getNamedChild("url");
      }
      if (resp != null) {
        return IdStatus.OPTIONAL;
      } if (method == null) {
        if (fullUrl == null)
          return IdStatus.REQUIRED;
        else if (fullUrl.primitiveValue().startsWith("urn:uuid:") || fullUrl.primitiveValue().startsWith("urn:oid:"))
          return IdStatus.OPTIONAL;
        else
          return IdStatus.REQUIRED;
      } else {
        String s = method.primitiveValue();
        if (s.equals("PUT")) {
          if (url == null)
            return IdStatus.REQUIRED;
          else
            return IdStatus.OPTIONAL; // or maybe prohibited? not clear
        } else if (s.equals("POST"))
          return IdStatus.OPTIONAL; // this should be prohibited, but see task 9102
        else // actually, we should never get to here; a bundle entry with method get/delete should not have a resource
          return IdStatus.OPTIONAL;					
      }
    } else if (isParametersEntry(ei.path) || isBundleOutcome(ei.path))
      return IdStatus.OPTIONAL; 
    else
      return IdStatus.REQUIRED; 
  }

  private void checkInvariants(List<ValidationMessage> errors, String path, StructureDefinition profile, ElementDefinition ed, String typename, String typeProfile, Element resource, Element element) throws FHIRException, FHIRException {
    if (noInvariantChecks)
      return;

    for (ElementDefinitionConstraintComponent inv : ed.getConstraint()) {
      if (inv.hasExpression()) {
        ExpressionNode n = (ExpressionNode) inv.getUserData("validator.expression.cache");
        if (n == null) {
          long t = System.nanoTime();
          try {
            n = fpe.parse(inv.getExpression());
          } catch (FHIRLexerException e) {
            throw new FHIRException("Problem processing expression "+inv.getExpression() +" in profile " + profile.getUrl() + " path " + path + ": " + e.getMessage());
          }
          fpeTime = fpeTime + (System.nanoTime() - t);
          inv.setUserData("validator.expression.cache", n);
        }

        String msg;
        boolean ok;
        try {
          long t = System.nanoTime();
          ok = fpe.evaluateToBoolean(resource, element, n);
          fpeTime = fpeTime + (System.nanoTime() - t);
          msg = fpe.forLog();
        } catch (Exception ex) {
          ok = false;
          msg = ex.getMessage(); 
        }
        if (!ok) {
          try {
            ok = fpe.evaluateToBoolean(resource, element, n);
          } catch (PathEngineException e) {
            throw new FHIRException("Problem processing expression "+inv.getExpression() +" in profile " + profile.getUrl() + " path " + path + ": " + e.getMessage());
          }
          if (!Utilities.noString(msg))
            msg = " ("+msg+")";
          if (inv.getSeverity() == ConstraintSeverity.ERROR)
            rule(errors, IssueType.INVARIANT, element.line(), element.col(), path, ok, inv.getHuman()+msg+" ["+inv.getExpression()+"]");
          else if (inv.getSeverity() == ConstraintSeverity.WARNING)
            warning(errors, IssueType.INVARIANT, element.line(), element.line(), path, ok, inv.getHuman()+msg+" ["+inv.getExpression()+"]");
        }
      }
    }
  }

  private void validateMessage(List<ValidationMessage> errors, List<Element> entries, Element messageHeader, NodeStack stack, String fullUrl, String id) {
    // first entry must be a messageheader
    if (rule(errors, IssueType.INVALID, messageHeader.line(), messageHeader.col(), stack.getLiteralPath(), messageHeader.getType().equals("MessageHeader"),
        "The first entry in a message must be a MessageHeader")) {
      // the composition subject and section references must resolve in the bundle
      List<Element> elements = messageHeader.getChildren("data");
      for (Element elem: elements)
        validateBundleReference(errors, entries, elem, "MessageHeader Data", stack.push(elem, -1, null, null), fullUrl, "MessageHeader", id);
    }
  }

  private void validateObservation(List<ValidationMessage> errors, Element element, NodeStack stack) {
    // all observations should have a subject, a performer, and a time

    bpCheck(errors, IssueType.INVALID, element.line(), element.col(), stack.getLiteralPath(), element.getNamedChild("subject") != null, "All observations should have a subject");
    bpCheck(errors, IssueType.INVALID, element.line(), element.col(), stack.getLiteralPath(), element.getNamedChild("performer") != null, "All observations should have a performer");
    bpCheck(errors, IssueType.INVALID, element.line(), element.col(), stack.getLiteralPath(), element.getNamedChild("effectiveDateTime") != null || element.getNamedChild("effectivePeriod") != null,
        "All observations should have an effectiveDateTime or an effectivePeriod");
  }

  /*
   * The actual base entry point
   */
  /*  private void validateResource(List<ValidationMessage> errors, Element resource, Element element, StructureDefinition defn, ValidationProfileSet profiles, IdStatus idstatus, NodeStack stack) throws FHIRException, FHIRException {
    List<StructureDefinition> declProfiles = new ArrayList<StructureDefinition>();
    List<Element> meta = element.getChildrenByName("meta");
    if (!meta.isEmpty()) {
      for (Element profileName : meta.get(0).getChildrenByName("profile")) {
        StructureDefinition sd = context.fetchResource(StructureDefinition.class, profileName.getValue());
        if (sd != null)
          declProfiles.add(sd);
      }
    }

    if (!declProfiles.isEmpty()) {
      // Validate against profiles rather than the resource itself as they'll be more constrained and will cover the resource elements anyhow
      for (StructureDefinition sd : declProfiles)
        validateResource2(errors, resource, element, sd, profiles, idstatus, stack);
    } else
      validateResource2(errors, resource, element, defn, profiles, idstatus, stack);
  }*/

  private void validateResource(Object appContext, List<ValidationMessage> errors, Element resource, Element element, StructureDefinition defn, ValidationProfileSet profiles, IdStatus idstatus, NodeStack stack) throws FHIRException, FHIRException, IOException {
    assert stack != null;
    assert resource != null;

    boolean ok = true;

    String resourceName = element.getType(); // todo: consider namespace...?
    if (defn == null) {
      long t = System.nanoTime();
      defn = element.getProperty().getStructure();
      if (defn == null)
        defn = context.fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/" + resourceName);
      if (profiles!=null)
        getResourceProfiles(resource, stack).addProfiles(errors, profiles, stack.getLiteralPath(), element);
      sdTime = sdTime + (System.nanoTime() - t);
      ok = rule(errors, IssueType.INVALID, element.line(), element.col(), stack.addToLiteralPath(resourceName), defn != null, "No definition found for resource type '" + resourceName + "'");
    }

    String type = defn.getKind() == StructureDefinitionKind.LOGICAL ? defn.getId() : defn.getType();
    // special case: we have a bundle, and the profile is not for a bundle. We'll try the first entry instead 
    if (!type.equals(resourceName) && resourceName.equals("Bundle")) {
      Element first = getFirstEntry(element);
      if (first != null && first.getType().equals(type)) {
        element = first;
        resourceName = element.getType();
        idstatus = IdStatus.OPTIONAL; // why?
      }
    }
    ok = rule(errors, IssueType.INVALID, -1, -1, stack.getLiteralPath(), type.equals(resourceName), "Specified profile type was '" + type + "', but found type '" + resourceName + "'");

    if (ok) {
      if (idstatus == IdStatus.REQUIRED && (element.getNamedChild("id") == null))
        rule(errors, IssueType.INVALID, element.line(), element.col(), stack.getLiteralPath(), false, "Resource requires an id, but none is present");
      else if (idstatus == IdStatus.PROHIBITED && (element.getNamedChild("id") != null))
        rule(errors, IssueType.INVALID, element.line(), element.col(), stack.getLiteralPath(), false, "Resource has an id, but none is allowed");
      start(appContext, errors, resource, element, defn, stack); // root is both definition and type
    }
  }

  private void loadProfiles(ValidationProfileSet profiles) throws DefinitionException {
    if (profiles != null) { 
      for (String profile : profiles.getCanonicalUrls()) {
        StructureDefinition p = context.fetchResource(StructureDefinition.class, profile);
        if (p == null)
          throw new DefinitionException("StructureDefinition '" + profile + "' not found");
        profiles.getDefinitions().add(p);
      }
    }
  }

  private Element getFirstEntry(Element bundle) {
    List<Element> list = new ArrayList<Element>();
    bundle.getNamedChildren("entry", list);
    if (list.isEmpty())
      return null;
    Element resource = list.get(0).getNamedChild("resource");
    if (resource == null)
      return null;
    else
      return resource;
  }

  private void validateSections(List<ValidationMessage> errors, List<Element> entries, Element focus, NodeStack stack, String fullUrl, String id) {
    List<Element> sections = new ArrayList<Element>();
    focus.getNamedChildren("entry", sections);
    int i = 0;
    for (Element section : sections) {
      NodeStack localStack = stack.push(section, 1, null, null);
      validateBundleReference(errors, entries, section.getNamedChild("content"), "Section Content", localStack, fullUrl, "Composition", id);
      validateSections(errors, entries, section, localStack, fullUrl, id);
      i++;
    }
  }

  private boolean valueMatchesCriteria(Element value, ElementDefinition criteria) throws FHIRException {
    if (criteria.hasFixed()) {
      List<ValidationMessage> msgs = new ArrayList<ValidationMessage>();
      checkFixedValue(msgs, "{virtual}", value, criteria.getFixed(), "value", null);
      return msgs.size() == 0;
    } else if (criteria.hasBinding() && criteria.getBinding().getStrength() == BindingStrength.REQUIRED && criteria.getBinding().hasValueSet()) {
      throw new FHIRException("Unable to resolve slice matching - slice matching by value set not done");      
    } else {
      throw new FHIRException("Unable to resolve slice matching - no fixed value or required value set");
    }
  }

  private boolean yearIsValid(String v) {
    if (v == null) {
      return false;
    }
    try {
      int i = Integer.parseInt(v.substring(0, Math.min(4, v.length())));
      return i >= 1800 && i <= 2100;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  public class ChildIterator {
    private String basePath;
    private Element parent;
    private int cursor;
    private int lastCount;

    public ChildIterator(String path, Element element) {
      parent = element;
      basePath = path;
      cursor = -1;
    }

    public int count() {
      String nb = cursor == 0 ? "--" : parent.getChildren().get(cursor-1).getName();
      String na = cursor >= parent.getChildren().size() - 1 ? "--" : parent.getChildren().get(cursor+1).getName();
      if (name().equals(nb) || name().equals(na) ) {
        return lastCount + 1;
      } else
        return -1;
    }

    public Element element() {
      return parent.getChildren().get(cursor);
    }

    public String name() {
      return element().getName();
    }

    public boolean next() {
      if (cursor == -1) {
        cursor++;
        lastCount = 0;
      } else {
        String lastName = name();
        cursor++;
        if (cursor < parent.getChildren().size() && name().equals(lastName))
          lastCount++;
        else
          lastCount = 0;
      }
      return cursor < parent.getChildren().size();
    }

    public String path() {
      int i = count();
      String sfx = "";
      if (i > -1) {
        sfx = "[" + Integer.toString(lastCount + 1) + "]";
      }
      return basePath + "." + name() + sfx;
    }
  }

  public class NodeStack {
    private ElementDefinition definition;
    private Element element;
    private ElementDefinition extension;
    private String literalPath; // xpath format
    private List<String> logicalPaths; // dotted format, various entry points
    private NodeStack parent;
    private ElementDefinition type;

    public NodeStack() {
    }	  

    public NodeStack(Element element) {
      this.element = element;
      literalPath = element.getName();
    }	  

    public String addToLiteralPath(String... path) {
      StringBuilder b = new StringBuilder();
      b.append(getLiteralPath());
      for (String p : path) {
        if (p.startsWith(":")) {
          b.append("[");
          b.append(p.substring(1));
          b.append("]");
        } else {
          b.append(".");
          b.append(p);
        }	  
      }	  
      return b.toString();
    }

    private ElementDefinition getDefinition() {
      return definition;
    }

    private Element getElement() {
      return element;
    }

    protected String getLiteralPath() {
      return literalPath == null ? "" : literalPath;
    }

    private List<String> getLogicalPaths() {
      return logicalPaths == null ? new ArrayList<String>() : logicalPaths;
    }

    private ElementDefinition getType() {
      return type;
    }

    private NodeStack push(Element element, int count, ElementDefinition definition, ElementDefinition type) {
      NodeStack res = new NodeStack();
      res.parent = this;
      res.element = element;
      res.definition = definition;
      res.literalPath = getLiteralPath() + "." + element.getName();
      if (count > -1)
        res.literalPath = res.literalPath + "[" + Integer.toString(count) + "]";
      res.logicalPaths = new ArrayList<String>();
      if (type != null) {
        // type will be bull if we on a stitching point of a contained resource, or if....
        res.type = type;
        String t = tail(definition.getPath());
        for (String lp : getLogicalPaths()) {
          res.logicalPaths.add(lp + "." + t);
          if (t.endsWith("[x]"))
            res.logicalPaths.add(lp + "." + t.substring(0, t.length() - 3) + type.getPath());
        }
        res.logicalPaths.add(type.getPath());
      } else if (definition != null) {
        for (String lp : getLogicalPaths())
          res.logicalPaths.add(lp + "." + element.getName());
      } else
        res.logicalPaths.addAll(getLogicalPaths());
      // CommaSeparatedStringBuilder b = new CommaSeparatedStringBuilder();
      // for (String lp : res.logicalPaths)
      // b.append(lp);
      // System.out.println(res.literalPath+" : "+b.toString());
      return res;
    }

    private void setType(ElementDefinition type) {
      this.type = type;
    }
  }

  private void checkForProcessingInstruction(List<ValidationMessage> errors, Document document) {
    Node node = document.getFirstChild();
    while (node != null) {
      rule(errors, IssueType.INVALID, -1, -1, "(document)", node.getNodeType() != Node.PROCESSING_INSTRUCTION_NODE, "No processing instructions allowed in resources");
      node = node.getNextSibling();
    }
  }

  public class ElementInfo {

    public int index; // order of definition in overall order. all slices get the index of the slicing definition
    public int sliceindex; // order of the definition in the slices (if slice != null)
    public int count;
    public ElementDefinition definition;
    public ElementDefinition slice;
    private Element element;
    private String name;
    private String path;

    public ElementInfo(String name, Element element, String path, int count) {
      this.name = name;
      this.element = element;
      this.path = path;
      this.count = count;
    }

    public int col() {
      return element.col();
    }

    public int line() {
      return element.line();
    }

  }

  public String reportTimes() {
    String s = String.format("Times: overall = %d, tx = %d, sd = %d, load = %d, fpe = %d", overall, txTime, sdTime, loadTime, fpeTime);
    overall = 0;
    txTime = 0;
    sdTime = 0;
    loadTime = 0;
    fpeTime = 0;
    return s;
  }

  public boolean isNoBindingMsgSuppressed() {
    return noBindingMsgSuppressed;
  }

  public IResourceValidator setNoBindingMsgSuppressed(boolean noBindingMsgSuppressed) {
    this.noBindingMsgSuppressed = noBindingMsgSuppressed;
    return this;
  }

  
  public boolean isNoTerminologyChecks() {
    return noTerminologyChecks;
  }

  public IResourceValidator setNoTerminologyChecks(boolean noTerminologyChecks) {
    this.noTerminologyChecks = noTerminologyChecks;
    return this;
  }

  public void checkAllInvariants(){
    for (StructureDefinition sd : context.allStructures()) {
      if (sd.getDerivation() == TypeDerivationRule.SPECIALIZATION) {
        for (ElementDefinition ed : sd.getSnapshot().getElement()) {
          for (ElementDefinitionConstraintComponent inv : ed.getConstraint()) {
            if (inv.hasExpression()) {
              try {
                ExpressionNode n = (ExpressionNode) inv.getUserData("validator.expression.cache");
                if (n == null) {
                  n = fpe.parse(inv.getExpression());
                  inv.setUserData("validator.expression.cache", n);
                }
                fpe.check(null, sd.getKind() == StructureDefinitionKind.RESOURCE ?  sd.getType() : "DomainResource", ed.getPath(), n);
              } catch (Exception e) {
                System.out.println("Error processing structure ["+sd.getId()+"] path "+ed.getPath()+":"+inv.getKey()+" (\""+inv.getExpression()+"\"): "+e.getMessage());
              }
            }
          }
        }
      }
    }
  }

}
