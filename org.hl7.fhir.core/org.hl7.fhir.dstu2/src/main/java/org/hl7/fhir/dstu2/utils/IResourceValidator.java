package org.hl7.fhir.dstu2.utils;

/*-
 * #%L
 * org.hl7.fhir.dstu2
 * %%
 * Copyright (C) 2014 - 2019 Health Level 7
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import java.util.List;

import org.hl7.fhir.dstu2.model.StructureDefinition;
import org.hl7.fhir.utilities.validation.ValidationMessage;
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
