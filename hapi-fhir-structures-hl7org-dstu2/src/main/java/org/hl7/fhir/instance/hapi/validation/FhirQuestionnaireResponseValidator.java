package org.hl7.fhir.instance.hapi.validation;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hl7.fhir.instance.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.instance.model.Questionnaire;
import org.hl7.fhir.instance.model.QuestionnaireResponse;
import org.hl7.fhir.instance.model.ValueSet;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.utils.WorkerContext;
import org.hl7.fhir.instance.validation.QuestionnaireResponseValidator;
import org.hl7.fhir.instance.validation.ValidationMessage;
import org.hl7.fhir.instance.validation.ValidationMessage.Source;

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.ResourceReferenceInfo;
import ca.uhn.fhir.validation.IResourceLoader;
import ca.uhn.fhir.validation.IValidationContext;
import ca.uhn.fhir.validation.IValidatorModule;

public class FhirQuestionnaireResponseValidator extends BaseValidatorBridge implements IValidatorModule {

  private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory
      .getLogger(FhirQuestionnaireResponseValidator.class);
  private IResourceLoader myResourceLoader;

  /**
   * Set the class which will be used to load linked resources from the
   * <code>QuestionnaireResponse</code>. Specifically, if the
   * <code>QuestionnaireResponse</code> refers to an external (non-contained)
   * <code>Questionnaire</code>, or to any external (non-contained)
   * <code>ValueSet</code>, the resource loader will be used to fetch those
   * resources during the validation.
   * 
   * @param theResourceLoader
   *          The resourceloader to use. May be <code>null</code> if no resource
   *          loader should be used (in which case any
   *          <code>QuestionaireResponse</code> with external references will
   *          fail to validate.)
   */
  public void setResourceLoader(IResourceLoader theResourceLoader) {
    myResourceLoader = theResourceLoader;
  }

  @Override
  protected List<ValidationMessage> validate(IValidationContext<?> theCtx) {
    Object resource = theCtx.getResource();
    if (!(theCtx.getResource() instanceof IBaseResource)) {
      ourLog.debug("Not validating object of type {}", theCtx.getResource().getClass());
      return Collections.emptyList();
    }

    if (resource instanceof QuestionnaireResponse) {
      return doValidate(theCtx, (QuestionnaireResponse) resource);
    }

    RuntimeResourceDefinition def = theCtx.getFhirContext().getResourceDefinition((IBaseResource) resource);
    if ("QuestionnaireResponse".equals(def.getName()) == false) {
      return Collections.emptyList();
    }

    /*
     * If we have a non-RI structure, convert it
     */

    IParser p = theCtx.getFhirContext().newJsonParser();
    String string = p.encodeResourceToString((IBaseResource) resource);
    QuestionnaireResponse qa = p.parseResource(QuestionnaireResponse.class, string);

    return doValidate(theCtx, qa);
  }

  private List<ValidationMessage> doValidate(IValidationContext<?> theValCtx, QuestionnaireResponse theResource) {

    WorkerContext workerCtx = new WorkerContext();
    ArrayList<ValidationMessage> retVal = new ArrayList<ValidationMessage>();

    if (!loadReferences(theResource, workerCtx, theValCtx, retVal)) {
      return retVal;
    }

    QuestionnaireResponseValidator val = new QuestionnaireResponseValidator(workerCtx);

    val.validate(retVal, theResource);
    return retVal;
  }

  private boolean loadReferences(IBaseResource theResource, WorkerContext theWorkerCtx, IValidationContext<?> theValCtx,
      ArrayList<ValidationMessage> theMessages) {
    List<ResourceReferenceInfo> refs = theValCtx.getFhirContext().newTerser().getAllResourceReferences(theResource);

    List<IBaseResource> newResources = new ArrayList<IBaseResource>();

    for (ResourceReferenceInfo nextRefInfo : refs) {
      IIdType nextRef = nextRefInfo.getResourceReference().getReferenceElement();
      String resourceType = nextRef.getResourceType();
      if (nextRef.isLocal()) {
        IBaseResource resource = nextRefInfo.getResourceReference().getResource();
        if (resource instanceof ValueSet) {
          theWorkerCtx.getValueSets().put(nextRef.getValue(), (ValueSet) resource);
          newResources.add(resource);
        } else if (resource instanceof Questionnaire) {
          theWorkerCtx.getQuestionnaires().put(nextRef.getValue(), (Questionnaire) resource);
          newResources.add(resource);
        } else if (resource == null) {
          theMessages.add(new ValidationMessage(Source.QuestionnaireResponseValidator,
              org.hl7.fhir.instance.model.OperationOutcome.IssueType.INVALID,
              "Invalid reference '" + nextRef.getValue() + "' - No contained resource with this ID found", IssueSeverity.FATAL));
        }
      } else if (isBlank(resourceType)) {
        theMessages.add(new ValidationMessage(Source.QuestionnaireResponseValidator,
            org.hl7.fhir.instance.model.OperationOutcome.IssueType.INVALID,
            "Invalid reference '" + nextRef.getValue() + "' - Does not identify resource type", IssueSeverity.FATAL));
      } else if ("ValueSet".equals(resourceType)) {
        if (!theWorkerCtx.getValueSets().containsKey(nextRef.getValue())) {
          ValueSet resource = tryToLoad(ValueSet.class, nextRef, theMessages);
          if (resource == null) {
            return false;
          }
          theWorkerCtx.getValueSets().put(nextRef.getValue(), resource);
          newResources.add(resource);
        }
      } else if ("Questionnaire".equals(resourceType)) {
        if (!theWorkerCtx.getQuestionnaires().containsKey(nextRef.getValue())) {
          Questionnaire resource = tryToLoad(Questionnaire.class, nextRef, theMessages);
          if (resource == null) {
            return false;
          }
          theWorkerCtx.getQuestionnaires().put(nextRef.getValue(), resource);
          newResources.add(resource);
        }
      }
    }

    for (IBaseResource nextAddedResource : newResources) {
      boolean outcome = loadReferences(nextAddedResource, theWorkerCtx, theValCtx, theMessages);
      if (!outcome) {
        return false;
      }
    }

    return true;
  }

  private <T extends IBaseResource> T tryToLoad(Class<T> theType, IIdType theReference,
      List<ValidationMessage> theMessages) {
    if (myResourceLoader == null) {
      theMessages.add(new ValidationMessage().setLevel(IssueSeverity.FATAL)
          .setMessage("No resource loader present, could not load " + theReference));
      return null;
    }

    try {
      T retVal = myResourceLoader.load(theType, theReference);
      if (retVal == null) {
        throw new IllegalStateException(
            "ResourceLoader returned null. This is a bug with the resourceloader. Reference was: " + theReference);
      }
      return retVal;
    } catch (ResourceNotFoundException e) {
      theMessages.add(new ValidationMessage().setLevel(IssueSeverity.FATAL)
          .setMessage("Reference could not be found: " + theReference));
      return null;
    }
  }

}
