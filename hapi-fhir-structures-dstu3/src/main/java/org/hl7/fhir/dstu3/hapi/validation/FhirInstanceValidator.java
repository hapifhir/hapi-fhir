package org.hl7.fhir.dstu3.hapi.validation;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.Validate;
import org.hl7.fhir.dstu3.model.Base;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.dstu3.model.TypeDetails;
import org.hl7.fhir.dstu3.utils.FHIRPathEngine.IEvaluationContext;
import org.hl7.fhir.dstu3.utils.IResourceValidator.BestPracticeWarningLevel;
import org.hl7.fhir.dstu3.utils.IResourceValidator.IdStatus;
import org.hl7.fhir.dstu3.validation.InstanceValidator;
import org.hl7.fhir.exceptions.PathEngineException;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueSeverity;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.validation.IValidationContext;
import ca.uhn.fhir.validation.IValidatorModule;

public class FhirInstanceValidator extends BaseValidatorBridge implements IValidatorModule {

  private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirInstanceValidator.class);

  private boolean myAnyExtensionsAllowed = true;
  private BestPracticeWarningLevel myBestPracticeWarningLevel;
  private DocumentBuilderFactory myDocBuilderFactory;
  private StructureDefinition myStructureDefintion;
  private IValidationSupport myValidationSupport;

  /**
   * Constructor
   * 
   * Uses {@link DefaultProfileValidationSupport} for {@link IValidationSupport validation support}
   */
  public FhirInstanceValidator() {
    this(new DefaultProfileValidationSupport());
  }

  /**
   * Constructor which uses the given validation support
   * 
   * @param theValidationSupport
   *          The validation support
   */
  public FhirInstanceValidator(IValidationSupport theValidationSupport) {
    myDocBuilderFactory = DocumentBuilderFactory.newInstance();
    myDocBuilderFactory.setNamespaceAware(true);
    myValidationSupport = theValidationSupport;
  }

  private String determineResourceName(Document theDocument) {
    Element root = null;

    NodeList list = theDocument.getChildNodes();
    for (int i = 0; i < list.getLength(); i++) {
      if (list.item(i) instanceof Element) {
        root = (Element) list.item(i);
        break;
      }
    }
    root = theDocument.getDocumentElement();
    return root.getLocalName();
  }

  private StructureDefinition findStructureDefinitionForResourceName(final FhirContext theCtx, String resourceName) {
    String sdName = "http://hl7.org/fhir/StructureDefinition/" + resourceName;
    StructureDefinition profile = myStructureDefintion != null ? myStructureDefintion : myValidationSupport.fetchStructureDefinition(theCtx, sdName);
    return profile;
  }

  /**
   * Returns the "best practice" warning level (default is {@link BestPracticeWarningLevel#Hint}).
   * <p>
   * The FHIR Instance Validator has a number of checks for best practices in terms of FHIR usage. If this setting is
   * set to {@link BestPracticeWarningLevel#Error}, any resource data which does not meet these best practices will be
   * reported at the ERROR level. If this setting is set to {@link BestPracticeWarningLevel#Ignore}, best practice
   * guielines will be ignored.
   * </p>
   * 
   * @see {@link #setBestPracticeWarningLevel(BestPracticeWarningLevel)}
   */
  public BestPracticeWarningLevel getBestPracticeWarningLevel() {
    return myBestPracticeWarningLevel;
  }

  /**
   * Returns the {@link IValidationSupport validation support} in use by this validator. Default is an instance of
   * {@link DefaultProfileValidationSupport} if the no-arguments constructor for this object was used.
   */
  public IValidationSupport getValidationSupport() {
    return myValidationSupport;
  }

  /**
   * If set to {@literal true} (default is true) extensions which are not known to the 
   * validator (e.g. because they have not been explicitly declared in a profile) will
   * be validated but will not cause an error.
   */
  public boolean isAnyExtensionsAllowed() {
    return myAnyExtensionsAllowed;
  }

  /**
   * If set to {@literal true} (default is true) extensions which are not known to the 
   * validator (e.g. because they have not been explicitly declared in a profile) will
   * be validated but will not cause an error.
   */
  public void setAnyExtensionsAllowed(boolean theAnyExtensionsAllowed) {
    myAnyExtensionsAllowed = theAnyExtensionsAllowed;
  }

  /**
   * Sets the "best practice warning level". When validating, any deviations from best practices will be reported at
   * this level.
   * <p>
   * The FHIR Instance Validator has a number of checks for best practices in terms of FHIR usage. If this setting is
   * set to {@link BestPracticeWarningLevel#Error}, any resource data which does not meet these best practices will be
   * reported at the ERROR level. If this setting is set to {@link BestPracticeWarningLevel#Ignore}, best practice
   * guielines will be ignored.
   * </p>
   * 
   * @param theBestPracticeWarningLevel
   *          The level, must not be <code>null</code>
   */
  public void setBestPracticeWarningLevel(BestPracticeWarningLevel theBestPracticeWarningLevel) {
    Validate.notNull(theBestPracticeWarningLevel);
    myBestPracticeWarningLevel = theBestPracticeWarningLevel;
  }

  public void setStructureDefintion(StructureDefinition theStructureDefintion) {
    myStructureDefintion = theStructureDefintion;
  }

  /**
   * Sets the {@link IValidationSupport validation support} in use by this validator. Default is an instance of
   * {@link DefaultProfileValidationSupport} if the no-arguments constructor for this object was used.
   */
  public void setValidationSupport(IValidationSupport theValidationSupport) {
    myValidationSupport = theValidationSupport;
  }

  protected List<ValidationMessage> validate(final FhirContext theCtx, String theInput, EncodingEnum theEncoding) {
    HapiWorkerContext workerContext = new HapiWorkerContext(theCtx, myValidationSupport);

    InstanceValidator v;
    IEvaluationContext evaluationCtx = new NullEvaluationContext();
    try {
      v = new InstanceValidator(workerContext, evaluationCtx);
    } catch (Exception e) {
      throw new ConfigurationException(e);
    }

    v.setBestPracticeWarningLevel(getBestPracticeWarningLevel());
    v.setAnyExtensionsAllowed(isAnyExtensionsAllowed());
    v.setResourceIdRule(IdStatus.OPTIONAL);

    List<ValidationMessage> messages = new ArrayList<ValidationMessage>();

    if (theEncoding == EncodingEnum.XML) {
      Document document;
      try {
        DocumentBuilder builder = myDocBuilderFactory.newDocumentBuilder();
        InputSource src = new InputSource(new StringReader(theInput));
        document = builder.parse(src);
      } catch (Exception e2) {
        ourLog.error("Failure to parse XML input", e2);
        ValidationMessage m = new ValidationMessage();
        m.setLevel(IssueSeverity.FATAL);
        m.setMessage("Failed to parse input, it does not appear to be valid XML:" + e2.getMessage());
        return Collections.singletonList(m);
      }

      String resourceName = determineResourceName(document);
      StructureDefinition profile = findStructureDefinitionForResourceName(theCtx, resourceName);
      if (profile != null) {
        try {
          v.validate(null, messages, document, profile);
        } catch (Exception e) {
          throw new InternalErrorException("Unexpected failure while validating resource", e);
        }
      }
    } else if (theEncoding == EncodingEnum.JSON) {
      Gson gson = new GsonBuilder().create();
      JsonObject json = gson.fromJson(theInput, JsonObject.class);

      String resourceName = json.get("resourceType").getAsString();
      StructureDefinition profile = findStructureDefinitionForResourceName(theCtx, resourceName);
      if (profile != null) {
        try {
          v.validate(null, messages, json, profile);
        } catch (Exception e) {
          throw new InternalErrorException("Unexpected failure while validating resource", e);
        }
      }
    } else {
      throw new IllegalArgumentException("Unknown encoding: " + theEncoding);
    }

    for (int i = 0; i < messages.size(); i++) {
      ValidationMessage next = messages.get(i);
      if ("Binding has no source, so can't be checked".equals(next.getMessage())) {
        messages.remove(i);
        i--;
      }
    }
    return messages;
  }

  @Override
  protected List<ValidationMessage> validate(IValidationContext<?> theCtx) {
    return validate(theCtx.getFhirContext(), theCtx.getResourceAsString(), theCtx.getResourceAsStringEncoding());
  }

  public class NullEvaluationContext implements IEvaluationContext {

    @Override
    public TypeDetails checkFunction(Object theAppContext, String theFunctionName, List<TypeDetails> theParameters) throws PathEngineException {
      return null;
    }

    @Override
    public List<Base> executeFunction(Object theAppContext, String theFunctionName, List<List<Base>> theParameters) {
      return null;
    }

    @Override
    public boolean log(String theArgument, List<Base> theFocus) {
      return false;
    }

    @Override
    public Base resolveConstant(Object theAppContext, String theName) throws PathEngineException {
      return null;
    }

    @Override
    public TypeDetails resolveConstantType(Object theAppContext, String theName) throws PathEngineException {
      return null;
    }

    @Override
    public FunctionDetails resolveFunction(String theFunctionName) {
      return null;
    }

    @Override
    public Base resolveReference(Object theAppContext, String theUrl) {
      return null;
    }

  }

}
