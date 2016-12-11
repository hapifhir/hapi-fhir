package org.hl7.fhir.dstu2016may.validation;

import java.io.InputStream;
import java.util.List;

import org.hl7.fhir.dstu2016may.metamodel.Manager.FhirFormat;
import org.hl7.fhir.dstu2016may.model.StructureDefinition;

import com.google.gson.JsonObject;

/**
 * Interface to the instance validator. This takes a resource, in one of many forms, and 
 * checks whether it is valid
 * 
 * @author Grahame Grieve
 *
 */
public interface IResourceValidator {

  public enum BestPracticeWarningLevel {
    Ignore,
    Hint,
    Warning,
    Error
  }

  public enum CheckDisplayOption {
    Ignore,
    Check,
    CheckCaseAndSpace,
    CheckCase,
    CheckSpace
  }

  enum IdStatus {
    OPTIONAL, REQUIRED, PROHIBITED
  }
  

  /**
   * how much to check displays for coded elements 
   * @return
   */
  CheckDisplayOption getCheckDisplay();
  void setCheckDisplay(CheckDisplayOption checkDisplay);

  /**
   * whether the resource must have an id or not (depends on context)
   * 
   * @return
   */

	IdStatus getResourceIdRule();
	void setResourceIdRule(IdStatus resourceIdRule);
  
  /**
   * whether the validator should enforce best practice guidelines
   * as defined by various HL7 committees 
   *
   */
  BestPracticeWarningLevel getBasePracticeWarningLevel();
  void setBestPracticeWarningLevel(BestPracticeWarningLevel value);
  
  /** 
   * Validate suite
   *
   * you can validate one of the following representations of resources:
   * 
   * stream - provide a format
   * a native resource
   * a metamodel resource 
   * a DOM element or Document
   * a Json Object 
   * 
   */
  void validate(List<ValidationMessage> errors, InputStream stream, FhirFormat format) throws Exception;
  void validate(List<ValidationMessage> errors, InputStream stream, FhirFormat format, String profile) throws Exception;
  void validate(List<ValidationMessage> errors, InputStream stream, FhirFormat format, StructureDefinition profile) throws Exception;
  void validate(List<ValidationMessage> errors, org.hl7.fhir.dstu2016may.model.Resource resource) throws Exception;
  void validate(List<ValidationMessage> errors, org.hl7.fhir.dstu2016may.model.Resource resource, String profile) throws Exception;
  void validate(List<ValidationMessage> errors, org.hl7.fhir.dstu2016may.model.Resource resource, StructureDefinition profile) throws Exception;
  void validate(List<ValidationMessage> errors, org.hl7.fhir.dstu2016may.metamodel.Element element) throws Exception;
  void validate(List<ValidationMessage> errors, org.hl7.fhir.dstu2016may.metamodel.Element element, String profile) throws Exception;
  void validate(List<ValidationMessage> errors, org.hl7.fhir.dstu2016may.metamodel.Element element, StructureDefinition profile) throws Exception;
  void validate(List<ValidationMessage> errors, org.w3c.dom.Element element) throws Exception;
  void validate(List<ValidationMessage> errors, org.w3c.dom.Element element, String profile) throws Exception;
  void validate(List<ValidationMessage> errors, org.w3c.dom.Element element, StructureDefinition profile) throws Exception;
  void validate(List<ValidationMessage> errors, org.w3c.dom.Document document) throws Exception;
  void validate(List<ValidationMessage> errors, org.w3c.dom.Document document, String profile) throws Exception;
  void validate(List<ValidationMessage> errors, org.w3c.dom.Document document, StructureDefinition profile) throws Exception;
  void validate(List<ValidationMessage> errors, JsonObject object) throws Exception;
  void validate(List<ValidationMessage> errors, JsonObject object, String profile) throws Exception;
  void validate(List<ValidationMessage> errors, JsonObject object, StructureDefinition profile) throws Exception; 
}