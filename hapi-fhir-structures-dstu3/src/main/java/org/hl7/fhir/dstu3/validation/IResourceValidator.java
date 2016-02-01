package org.hl7.fhir.dstu3.validation;

import java.util.List;

import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.gson.JsonObject;

public interface IResourceValidator {

  /**
   * whether the validator should enforce best practice guidelines
   * as defined by various HL7 committees 
   *  
   *  
   * @author Grahame Grieve
   *
   */
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

  /**
   * how much to check displays for coded elements 
   * @return
   */
  CheckDisplayOption getCheckDisplay();

  /**
   * how much to check displays for coded elements 
   * @return
   */
  void setCheckDisplay(CheckDisplayOption checkDisplay);

	enum IdStatus {
		OPTIONAL, REQUIRED, PROHIBITED
	}
	
  /**
   * whether the resource must have an id or not (depends on context)
   * 
   * @return
   */

	IdStatus getResourceIdRule();
	void setResourceIdRule(IdStatus resourceIdRule);
  
  BestPracticeWarningLevel getBasePracticeWarningLevel();
  void setBestPracticeWarningLevel(BestPracticeWarningLevel value);
  
  
  /**
   * Given a DOM element, return a list of errors in the resource
   * 
   * @param errors
   * @param elem
   * @- if the underlying infrastructure fails (not if the resource is invalid)
   */
  void validate(List<ValidationMessage> errors, Element element) throws Exception;

  /**
   * Given a JSON Object, return a list of errors in the resource
   * 
   * @param errors
   * @param elem
   * @- if the underlying infrastructure fails (not if the resource is invalid)
   */
  void validate(List<ValidationMessage> errors, JsonObject object) throws Exception;

  /**
   * Given a DOM element, return a list of errors in the resource
   * 
   * @param errors
   * @param elem
   * @- if the underlying infrastructure fails (not if the resource is invalid)
   */
  List<ValidationMessage> validate(Element element) throws Exception;

  /**
   * Given a DOM element, return a list of errors in the resource
   * 
   * @param errors
   * @param elem
   * @- if the underlying infrastructure fails (not if the resource is invalid)
   */
  List<ValidationMessage> validate(JsonObject object) throws Exception;

  /**
   * Given a DOM element, return a list of errors in the resource 
   * with regard to the specified profile (by logical identifier)
   *  
   * @param errors
   * @param element
   * @param profile
   * @- if the underlying infrastructure fails, or the profile can't be found (not if the resource is invalid)
   */
  void validate(List<ValidationMessage> errors, Element element, String profile) throws Exception;

  /**
   * Given a DOM element, return a list of errors in the resource 
   * with regard to the specified profile (by logical identifier)
   *  
   * @param errors
   * @param element
   * @param profile
   * @- if the underlying infrastructure fails, or the profile can't be found (not if the resource is invalid)
   */
	List<ValidationMessage> validate(Element element, String profile) throws Exception;

  /**
   * Given a DOM element, return a list of errors in the resource 
   * with regard to the specified profile (by logical identifier)
   *  
   * @param errors
   * @param element
   * @param profile
   * @- if the underlying infrastructure fails, or the profile can't be found (not if the resource is invalid)
   */
  List<ValidationMessage> validate(JsonObject object, StructureDefinition profile) throws Exception;

  /**
   * Given a DOM element, return a list of errors in the resource 
   * with regard to the specified profile (by logical identifier)
   *  
   * @param errors
   * @param element
   * @param profile
   * @- if the underlying infrastructure fails, or the profile can't be found (not if the resource is invalid)
   */
  List<ValidationMessage> validate(JsonObject object, String profile) throws Exception;

  /**
   * Given a DOM element, return a list of errors in the resource 
   * with regard to the specified profile 
   *  
   * @param errors
   * @param element
   * @param profile
   * @- if the underlying infrastructure fails (not if the resource is invalid)
   */
  void validate(List<ValidationMessage> errors, Element element, StructureDefinition profile) throws Exception;

  /**
   * Given a DOM element, return a list of errors in the resource 
   * with regard to the specified profile 
   *  
   * @param errors
   * @param element
   * @param profile
   * @- if the underlying infrastructure fails (not if the resource is invalid)
   */
  void validate(List<ValidationMessage> errors, JsonObject object, StructureDefinition profile) throws Exception;

  /**
   * Given a DOM element, return a list of errors in the resource 
   * with regard to the specified profile 
   *  
   * @param errors
   * @param element
   * @param profile
   * @- if the underlying infrastructure fails (not if the resource is invalid)
   */
  void validate(List<ValidationMessage> errors, JsonObject object, String profile) throws Exception;

  /**
   * Given a DOM element, return a list of errors in the resource 
   * with regard to the specified profile
   *  
   * @param errors
   * @param element
   * @param profile
   * @- if the underlying infrastructure fails (not if the resource is invalid)
   */
  List<ValidationMessage> validate(Element element, StructureDefinition profile) throws Exception;


  /**
   * Given a DOM document, return a list of errors in the resource
   * 
   * @param errors
   * @param elem
   * @- if the underlying infrastructure fails (not if the resource is invalid)
   */
  void validate(List<ValidationMessage> errors, Document document) throws Exception;

  /**
   * Given a DOM document, return a list of errors in the resource
   * 
   * @param errors
   * @param elem
   * @- if the underlying infrastructure fails (not if the resource is invalid)
   */
  List<ValidationMessage> validate(Document document) throws Exception;

  /**
   * Given a DOM document, return a list of errors in the resource 
   * with regard to the specified profile (by logical identifier)
   *  
   * @param errors
   * @param element
   * @param profile
   * @- if the underlying infrastructure fails, or the profile can't be found (not if the resource is invalid)
   */
  void validate(List<ValidationMessage> errors, Document document, String profile) throws Exception;

  /**
   * Given a DOM document, return a list of errors in the resource 
   * with regard to the specified profile (by logical identifier)
   *  
   * @param errors
   * @param element
   * @param profile
   * @- if the underlying infrastructure fails, or the profile can't be found (not if the resource is invalid)
   */
	List<ValidationMessage> validate(Document document, String profile) throws Exception;

  /**
   * Given a DOM document, return a list of errors in the resource 
   * with regard to the specified profile 
   *  
   * @param errors
   * @param element
   * @param profile
   * @- if the underlying infrastructure fails (not if the resource is invalid)
   */
  void validate(List<ValidationMessage> errors, Document document, StructureDefinition profile) throws Exception;

  /**
   * Given a DOM document, return a list of errors in the resource 
   * with regard to the specified profile
   *  
   * @param errors
   * @param element
   * @param profile
   * @- if the underlying infrastructure fails (not if the resource is invalid)
   */
  List<ValidationMessage> validate(Document document, StructureDefinition profile) throws Exception;

}