package org.hl7.fhir.dstu2016may.utils;

import org.hl7.fhir.dstu2016may.formats.IParser;
import org.hl7.fhir.dstu2016may.formats.ParserType;
import org.hl7.fhir.dstu2016may.model.*;
import org.hl7.fhir.dstu2016may.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.dstu2016may.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.dstu2016may.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.dstu2016may.terminologies.ValueSetExpander.ValueSetExpansionOutcome;
import org.hl7.fhir.dstu2016may.validation.IResourceValidator;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueSeverity;

import java.util.List;
import java.util.Set;


/**
 * This is the standard interface used for access to underlying FHIR
 * services through the tools and utilities provided by the reference
 * implementation. 
 * 
 * The functionality it provides is 
 *  - get access to parsers, validators, narrative builders etc
 *    (you can't create these directly because they need access 
 *    to the right context for their information)
 *    
 *  - find resources that the tools need to carry out their tasks
 *  
 *  - provide access to terminology services they need. 
 *    (typically, these terminology service requests are just
 *    passed through to the local implementation's terminology
 *    service)    
 *  
 * @author Grahame
 */
public interface IWorkerContext {

  // -- Parsers (read and write instances) ----------------------------------------

  /**
   * Get a parser to read/write instances. Use the defined type (will be extended 
   * as further types are added, though the only currently anticipate type is RDF)
   * 
   * XML/JSON - the standard renderers
   * XHTML - render the narrative only (generate it if necessary)
   * 
   * @param type
   * @return
   */
  public IParser getParser(ParserType type);

  /**
   * Get a parser to read/write instances. Determine the type 
   * from the stated type. Supported value for type:
   * - the recommended MIME types
   * - variants of application/xml and application/json
   * - _format values xml, json
   * 
   * @param type
   * @return
   */	
  public IParser getParser(String type);

  /**
   * Get a JSON parser
   * 
   * @return
   */
  public IParser newJsonParser();

  /**
   * Get an XML parser
   * 
   * @return
   */
  public IParser newXmlParser();

  /**
   * Get a validator that can check whether a resource is valid 
   * 
   * @return a prepared generator
   * @
   */
  public IResourceValidator newValidator();

  // -- resource fetchers ---------------------------------------------------

  /**
   * Find an identified resource. The most common use of this is to access the the 
   * standard conformance resources that are part of the standard - structure 
   * definitions, value sets, concept maps, etc.
   * 
   * Also, the narrative generator uses this, and may access any kind of resource
   * 
   * The URI is called speculatively for things that might exist, so not finding 
   * a matching resouce, return null, not an error
   * 
   * The URI can have one of 3 formats:
   *  - a full URL e.g. http://acme.org/fhir/ValueSet/[id]
   *  - a relative URL e.g. ValueSet/[id]
   *  - a logical id e.g. [id]
   *  
   * It's an error if the second form doesn't agree with class_. It's an 
   * error if class_ is null for the last form
   * 
   * @return
   * @throws Exception
   */
  public <T extends Resource> T fetchResource(Class<T> class_, String uri);

  /**
   * find whether a resource is available. 
   * 
   * Implementations of the interface can assume that if hasResource ruturns 
   * true, the resource will usually be fetched subsequently
   * 
   * @param class_
   * @param uri
   * @return
   */
  public <T extends Resource> boolean hasResource(Class<T> class_, String uri);

  // -- profile services ---------------------------------------------------------
  
  public List<String> getResourceNames();
  public List<StructureDefinition> allStructures();
  
  // -- Terminology services ------------------------------------------------------

  // these are the terminology services used internally by the tools
  /**
   * Find the code system definition for the nominated system uri. 
   * return null if there isn't one (then the tool might try 
   * supportsSystem)
   * 
   * @param system
   * @return
   */
  public CodeSystem fetchCodeSystem(String system);

  /**
   * True if the underlying terminology service provider will do 
   * expansion and code validation for the terminology. Corresponds
   * to the extension 
   * 
   * http://hl7.org/fhir/StructureDefinition/conformance-supported-system
   * 
   * in the Conformance resource
   * 
   * @param system
   * @return
   */
  public boolean supportsSystem(String system);

  /**
   * find concept maps for a source
   * @param url
   * @return
   */
  public List<ConceptMap> findMapsForSource(String url);  

  /**
   * ValueSet Expansion - see $expand
   *  
   * @param source
   * @return
   */
  public ValueSetExpansionOutcome expandVS(ValueSet source, boolean cacheOk);
  
  /**
   * Value set expanion inside the internal expansion engine - used 
   * for references to supported system (see "supportsSystem") for
   * which there is no value set. 
   * 
   * @param inc
   * @return
   */
  public ValueSetExpansionComponent expandVS(ConceptSetComponent inc);
  
  public class ValidationResult {
    private ConceptDefinitionComponent definition;
    private IssueSeverity severity;
    private String message;
    
    public ValidationResult(IssueSeverity severity, String message) {
      this.severity = severity;
      this.message = message;
    }
    
    public ValidationResult(ConceptDefinitionComponent definition) {
      this.definition = definition;
    }

    public ValidationResult(IssueSeverity severity, String message, ConceptDefinitionComponent definition) {
      this.severity = severity;
      this.message = message;
      this.definition = definition;
    }
    
    public boolean isOk() {
      return definition != null;
    }

    public String getDisplay() {
      //Don't want question-marks as that stops something more useful from being displayed, such as the code
      //return definition == null ? "??" : definition.getDisplay();
      return definition == null ? null : definition.getDisplay();
    }

    public ConceptDefinitionComponent asConceptDefinition() {
      return definition;
    }

    public IssueSeverity getSeverity() {
      return severity;
    }

    public String getMessage() {
      return message;
    }
  }

  /**
   * Validation of a code - consult the terminology service 
   * to see whether it is known. If known, return a description of it
   * 
   *  note: always return a result, with either an error or a code description
   *  
   * corresponds to 2 terminology service calls: $validate-code and $lookup
   * 
   * @param system
   * @param code
   * @param display
   * @return
   */
  public ValidationResult validateCode(String system, String code, String display);

  /**
   * Validation of a code - consult the terminology service 
   * to see whether it is known. If known, return a description of it
   * Also, check whether it's in the provided value set
   * 
   * note: always return a result, with either an error or a code description, or both (e.g. known code, but not in the value set)
   *  
   * corresponds to 2 terminology service calls: $validate-code and $lookup
   * 
   * @param system
   * @param code
   * @param display
   * @return
   */
  public ValidationResult validateCode(String system, String code, String display, ValueSet vs);
  public ValidationResult validateCode(Coding code, ValueSet vs);
  public ValidationResult validateCode(CodeableConcept code, ValueSet vs);
  
  /**
   * Validation of a code - consult the terminology service 
   * to see whether it is known. If known, return a description of it
   * Also, check whether it's in the provided value set fragment (for supported systems with no value set definition)
   * 
   * note: always return a result, with either an error or a code description, or both (e.g. known code, but not in the value set)
   *  
   * corresponds to 2 terminology service calls: $validate-code and $lookup
   * 
   * @param system
   * @param code
   * @param display
   * @return
   */
  public ValidationResult validateCode(String system, String code, String display, ConceptSetComponent vsi);

  /**
   * returns the recommended tla for the type 
   * 
   * @param name
   * @return
   */
  public String getAbbreviation(String name);

  // return a set of types that have tails
  public Set<String> typeTails();

	public String oid2Uri(String code);


}
