package org.hl7.fhir.instance.utils;

import java.util.List;

import org.hl7.fhir.instance.formats.IParser;
import org.hl7.fhir.instance.formats.ParserType;
import org.hl7.fhir.instance.model.ConceptMap;
import org.hl7.fhir.instance.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.instance.model.Resource;
import org.hl7.fhir.instance.model.ValueSet;
import org.hl7.fhir.instance.model.ValueSet.ConceptDefinitionComponent;
import org.hl7.fhir.instance.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.instance.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.instance.terminologies.ValueSetExpander.ValueSetExpansionOutcome;
import org.hl7.fhir.instance.utils.IWorkerContext.ValidationResult;
import org.hl7.fhir.instance.validation.IResourceValidator;


/**
 * Standard interface for work context across reference implementations
 * 
 * Provides access to common services that code using FHIR resources needs
 * 
 * @author Grahame
 */
public interface IWorkerContext {

  // -- Parsers (read and write instances) ----------------------------------------

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

    public boolean isOk() {
      return definition != null;
    }

    public String getDisplay() {
      return definition == null ? "??" : definition.getDisplay();
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
   * Get a parser to read/write instances. Use the defined type (will be extended 
   * as further types are added, though the only anticipated types is RDF)
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

  // -- access to fixed content ---------------------------------------------------

  /**
   * Fetch a fixed resource that's pre-known in advance, and loaded as part of the
   * context. The most common use of this is to access the the standard conformance
   * resources that are part of the standard - profiles, extension definitions, and 
   * value sets (etc).
   * 
   * The context loader may choose to make additional resources available (i.e. 
   * implementation specific conformance statements, profiles, extension definitions)
   * 
   * Schemas and other similar non resource content can be accessed as Binary resources
   * using their filename in validation.zip as the id (http:/hl7/.org/fhir/Binary/[name]
   * 
   * @param resource
   * @param Reference
   * @return
   * @throws Exception
   */
  public <T extends Resource> T fetchResource(Class<T> class_, String uri) throws EOperationOutcome, Exception;

  public <T extends Resource> boolean hasResource(Class<T> class_, String uri);

  // -- Ancilliary services ------------------------------------------------------

	/**
   * Get a generator that can generate narrative for the instance
   * 
   * @return a prepared generator
   */
  public INarrativeGenerator getNarrativeGenerator(String prefix, String basePath);

  /**
   * Get a validator that can check whether a resource is valid 
   * 
   * @return a prepared generator
   * @throws Exception 
   */
  public IResourceValidator newValidator() throws Exception;


//  boolean isResource(String name);

  // knowledge services
  public List<ConceptMap> allMaps();
  
  public ValueSet fetchCodeSystem(String system);
  public boolean supportsSystem(String system);

  // expand whole vs, or just a piece
  public ValueSetExpansionOutcome expandVS(ValueSet source);
  public ValueSetExpansionComponent expandVS(ConceptSetComponent inc);
  public ValidationResult validateCode(String system, String code, String display);
  public ValidationResult validateCode(String system, String code, String display, ValueSet vs);
  public ValidationResult validateCode(String system, String code, String display, ConceptSetComponent vsi);  
  
}
