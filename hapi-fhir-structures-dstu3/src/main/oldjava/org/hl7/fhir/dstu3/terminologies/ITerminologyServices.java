package org.hl7.fhir.dstu3.terminologies;

import org.hl7.fhir.dstu3.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.dstu3.terminologies.ValueSetExpander.ValueSetExpansionOutcome;


/**
 * The value set system has a collection of value sets 
 * that define code systems, and construct value sets from 
 * them
 * 
 * Large external terminologies - LOINC, Snomed, etc - are too big, and 
 * trying to represent their definition as a native value set is too 
 * large. (e.g. LOINC + properties ~ 500MB). So we don't try. Instead.
 * we assume that there's some external server that provides these 
 * services, using this interface
 * 
 * The FHIR build tool uses http://fhir.healthintersections.com.au for 
 * these services
 * 
 * @author Grahame
 *
 */
public interface ITerminologyServices {
  /**
   * return true if the service handles code or value set resolution on the system
   */
  public boolean supportsSystem(String system);
  
  /** 
   *  given a system|code, return a definition for it. Nil means not valid
   */
  public ConceptDefinitionComponent getCodeDefinition(String system, String code);
  
  public class ValidationResult {
    private IssueSeverity severity;
    private String message;
    public ValidationResult(IssueSeverity severity, String message) {
      super();
      this.severity = severity;
      this.message = message;
    }
    public IssueSeverity getSeverity() {
      return severity;
    }
    public String getMessage() {
      return message;
    }


  }

  /**
   * for this system|code and display, validate the triple against the rules of 
   * the underlying code system
   */
  public ValidationResult validateCode(String system, String code, String display);
  
  /**
   * Expand the value set fragment (system | codes | filters). Note that this 
   * might fail if the expansion is very large. If the expansion fails, then the 
   * checkVS will be called instead
   */
  public ValueSetExpansionComponent expandVS(ConceptSetComponent inc) throws Exception;
//  public ValueSet expandVS(ValueSet vs) throws Exception;
  public ValueSetExpansionOutcome expand(ValueSet vs);
  
  /**
   * Test the value set fragment (system | codes | filters). 
   */
  public boolean checkVS(ConceptSetComponent vsi, String system, String code);
  
  public boolean verifiesSystem(String system);



}