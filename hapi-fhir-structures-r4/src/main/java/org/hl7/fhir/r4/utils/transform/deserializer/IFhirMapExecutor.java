//
// Translated by CS2J (http://www.cs2j.com): 8/18/2017 3:07:36 PM
//

package org.hl7.fhir.r4.utils.transform.deserializer;




import java.util.List;

/**
 * Classes that intend to use the fhir mapper should define a class that implements this interface.
 * The appropriate methods will be called when the corresponding elements of the map file are parsed.
 */
public interface IFhirMapExecutor
{
  /**
   * Called when map .... command found.
   * @param structureMap prospected Url value for structure map
   * @param name prospected name for structure map
   */
  void map(UrlData structureMap, String name) throws Exception ;

  /**
   * Called when uses .... command found.
   *
   *  @param structureDefinition Url of the Structure Definition
   *  @param name name of the Structure Definition which will determine its mode. The name determined in this statement will dictate the behavior of the structure.
   */
  void uses(UrlData structureDefinition, FhirMapUseNames name) throws Exception ;

  /**
   * Called when import.... command found.
   *
   *  @param structureMap the import value
   */
  void imports(UrlData structureMap) throws Exception ;

  /**
   * Called at start of group definitions.
   *
   *  @param groupName name of group
   *  @param groupType Group type. In grammar this is optional in which case this will be set to GroupTypesUnset)
   */
  void groupStart(String groupName, FhirMapGroupTypes groupType, String groupExtendName) throws Exception ;

  /**
   * Called at end of group definitions.
   */
  void groupEnd() throws Exception ;

  /**
   * Called at each group input definition.
   *
   *  @param name name of input
   *  @param type type of input
   *  @param mode input mode
   */
  void groupInput(String name, String type, FhirMapInputModes mode) throws Exception ;

  /**
   * Called when a rule starts being defined.
   *
   * @param ruleName Name of rule
   */
  void ruleStart(List<String> ruleName) throws Exception ;

  /**
   * Called for each rule source.
   *
   *  @param context source context
   *  @param type optional type name and cardinality. Null if unset
   *  @param defaultValue optional default value.
   *  @param listOptions Optional list options. FhirMappingListOpeions.NotSet if unset
   *  @param variable Optional assignment variable. Null if unset
   *  @param wherePath Optional where fhir path. Null if unset
   *  @param checkPath Optional check fhir path. Null if unset
   */
  void ruleSource(List<String> context,  FhirMapRuleType type,  String defaultValue,  FhirMapListOptions listOptions,  String variable,  String wherePath,  String checkPath) throws Exception ;

  /**
   * Execute transform similar to following.
   * 'do.requester as prr'
   *
   * @param context Target Context
   * @param targetVariable variable being assigned
   */
  void transformAs(List<String> context, String targetVariable) throws Exception ;

  /**
   * Execute append transform.
   *
   * @param context Target context
   * @param appendVariables value appended in transform
   * @param targetVariable target variable assigned, can be null
   */
  void transformAppend(List<String> context, List<String> appendVariables, String targetVariable) throws Exception ;

  /**
   * Execute cast transform
   *
   * @param context Target context
   * @param sourceVariable Source variable
   * @param typeName Type of value to cast to, may be null
   * @param targetVariable variable to assign value to
   */
  void transformCast(List<String> context, String sourceVariable, String typeName, String targetVariable) throws Exception ;

  /**
   *
   */
  void groupCall(String id, List<String> params);

  /**
   * Execute Coding transform.
   *
   * @param context Target context.
   * @param system URL dictating the Code system
   * @param code the code value
   * @param display display value for the Coding object
   * @param targetVariable variable to be assigned the Coding object, may be left null
   */
  void transformCoding(List<String> context, UrlData system, String code, String display, String targetVariable) throws Exception ;

  /**
   * Execute CodeableConcept transform.
   *
   * @param context Target context
   * @param system System used in the Codeable Concept
   * @param code Code in the Codeable Concept
   * @param display Display value for the Codeable Concept, may be left null
   * @param targetVariable Variable to hold the Codeable Concept object
   */
  void transformCodeableConcept(List<String> context, UrlData system, String code, String display, String targetVariable) throws Exception ;

  /**
   * Execute CodeableConcept transform.
   *
   * @param context Target context
   * @param text text for Codeable Concept
   * @param targetVariable Variable to hold the Codeable Concept object
   */
  void transformCodeableConcept(List<String> context, String text, String targetVariable) throws Exception ;

  /**
   * Populates a target for a Copy transform, adds it to the top rule on the stack
   * @param context Target context
   * @param copyVariable Varaible or value copied
   * @param targetVariable Variable that will hold the result of the transform
   * @throws Exception if it fails to populate correctly
   */
  void transformCopy(List<String> context, String copyVariable, String targetVariable) throws Exception ;

  /**
   * Execute cp transform
   *
   * @param context Target context
   * @param system System value for the transform
   * @param cpVariable Cp variable to be added
   * @param targetVariable Variable that will hold the result of the transform, may be left null
   */
  void transformCp(List<String> context, UrlData system, String cpVariable, String targetVariable) throws Exception ;

  /**
   * Execute create transform
   *
   * @param context Target Context
   * @param createVariable type of object to be created
   * @param targetVariable Variable that will hold the result of the transform, may be left null
   */
  void transformCreate(List<String> context, String createVariable, String targetVariable) throws Exception ;

  /**
   * Execute dateOp transform
   *
   * @param context Target Context
   * @param variable variable to be fed into the transform
   * @param operation type of operation performed
   * @param variable2 optional variable to be fed into the transform
   * @param targetVariable Variable that will hold the result of the transform, may be left null
   */
  void transformDateOp(List<String> context, String variable, String operation, String variable2, String targetVariable) throws Exception ;

  /**
   * Execute escape transform
   *
   * @param context Target Context
   * @param variable Variable fed into the transform
   * @param string1 String fed into the transform
   * @param string2 Second string fed into the transform, may be left null
   * @param targetVariable Variable that will hold the result of the transform, may be left null
   */
  void transformEscape(List<String> context, String variable, String string1, String string2, String targetVariable) throws Exception ;

  /**
   * Populates a target for an Extension transform, adds it to the top rule on the stack
   * @param context Target Context
   * @param targetVariable Variable that will hold the result of the transform, may be left null
   */
  void transformExtension(List<String> context, String targetVariable) throws Exception;

  /**
   * Execute evaluate transform
   *
   * @param context Target Context
   * @param obj object type
   * @param objElement element within object
   * @param targetVariable Variable that will hold the result of the transform, may be left null
   */
  void transformEvaluate(List<String> context, String obj, String objElement, String targetVariable) throws Exception ;

  /**
   * Execute id transform
   *
   * @param context Target Context
   * @param system Url dictating the system used for the Id
   * @param value The Id Value
   * @param type type value
   * @param targetVariable Variable that will hold the result of the transform, may be left null
   */
  void transformId(List<String> context, UrlData system, String value, String type, String targetVariable) throws Exception ;

  /**
   * Execute pointer transform
   *
   * @param context Target Context
   * @param resource Resource to be pointed to
   * @param targetVariable Variable that will hold the result of the transform, may be left null
   */
  void transformPointer(List<String> context, String resource, String targetVariable) throws Exception ;

  /**
   * Execute qty transform
   *
   * @param context Target Context
   * @param text Text of quantity
   * @param targetVariable Variable that will hold the result of the transform, may be left null
   */
  void transformQty(List<String> context, String text, String targetVariable) throws Exception ;

  /**
   * Execute qty transform
   *
   * @param context Target Context
   * @param value value of the quantity
   * @param unitString Unit of measure
   * @param system system of the quantity
   * @param targetVariable Variable that will hold the result of the transform, may be left null
   */
  void transformQty(List<String> context, String value, String unitString, UrlData system, String targetVariable) throws Exception ;

  /**
   * Execute qty transform
   *
   * @param context Target Context
   * @param value value of the quantity
   * @param unitString unit of measure
   * @param type type
   * @param targetVariable Variable that will hold the result of the transform, may be left null
   */
  void transformQty(List<String> context, String value, String unitString, String type, String targetVariable) throws Exception ;

  /**
   * Execute reference transform.
   *
   * @param context Target context
   * @param text reference value
   * @param targetVariable Variable that will hold the result of the transform, may be left null
   */
  void transformReference(List<String> context, String text, String targetVariable) throws Exception ;

  /**
   * Execute create transform
   *
   * @param context Target context
   * @param variable variable of the translate
   * @param mapUri the Map URL of the translation
   * @param outputType the type of output expected
   * @param targetVariable Variable that will hold the result of the transform, may be left null
   */
  void transformTranslate(List<String> context, String variable, UrlData mapUri, FhirMapTranslateOutputTypes outputType, String targetVariable) throws Exception ;

  /**
   * Execute truncate transform
   *
   * @param context Target context
   * @param variable variable to be truncated
   * @param length length of string to truncate to
   * @param targetVariable Variable that will hold the result of the transform, may be left null
   */
  void transformTruncate(List<String> context, String variable, int length, String targetVariable) throws Exception ;

  /**
   * Execute uuid transform
   *
   * @param context Target context
   * @param targetVariable Variable that will hold the result of the transform, may be left null
   */
  void transformUuid(List<String> context, String targetVariable) throws Exception ;

  /**
   * Called when a rule's definition is complete.
   */
  void ruleComplete() throws Exception ;

}


