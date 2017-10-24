//
// Translated by CS2J (http://www.cs2j.com): 8/18/2017 3:07:36 PM
//

package org.hl7.fhir.r4.utils.transform.deserializer;




import java.util.List;

/**
* Classes that intend to use the fhir mapper should define a class that implements this itnerface.
* The appropriate methods will be called when the correspondiong elements of the map file are parsed.
*/
public interface IFhirMapExecutor   
{
  /**
    * Called when map .... comand found.
    * 
    *  @param structureMap 
    *  @param name
    */
    void map(UrlData structureMap, String name) throws Exception ;

    /**
    * Called when uses .... comand found.
    *
    *  @param structureDefinition
    *  @param name
    */
    void uses(UrlData structureDefinition, FhirMapUseNames name) throws Exception ;

    /**
    * Called when import.... comand found.
    *
    *  @param structureMap
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
    *  @param type type of input)
    *  @param mode input mode
    */
    void groupInput(String name, String type, FhirMapInputModes mode) throws Exception ;

    /**
    * Called when a rule starts being defined.
    *
    *  @param ruleName
    */
    void ruleStart(List<String> ruleName) throws Exception ;

    /**
    * Called for each rule source.
    *
    *  @param context source context
    *  @param type optional type name and cardinality. Null if unset
    *  @param defaultValue optional default value.
    *  @param listOptions Optional list opeions. FhirMappingListOpeions.NotSet if unset
    *  @param variable Optional assigment variable. Null if unset
    *  @param wherePath Optional where fhir path. . Null if unset
    *  @param checkPath Optional check fhir path. . Null if unset
    */
    void ruleSource(List<String> context,  FhirMapRuleType type,  String defaultValue,  FhirMapListOptions listOptions,  String variable,  String wherePath,  String checkPath) throws Exception ;

    /**
    * Execute transform similar to following.
    * 'do.requester as prr'
    *
    *  @param context
    *  @param targetVariable
    */
    void transformAs(List<String> context, String targetVariable) throws Exception ;

    /**
    * Execute append transform.
    *
    *  @param context
    *  @param appendVariables
    *  @param targetVariable may be null
    */
    void transformAppend(List<String> context, List<String> appendVariables, String targetVariable) throws Exception ;

    /**
    * Execute cast transform
    *
    *  @param context
    *  @param sourceVariable
    *  @param typeName may be null
    */
    void transformCast(List<String> context, String sourceVariable, String typeName, String targetVariable) throws Exception ;

  /**
   *
   */
  void groupCall(String id, List<String> params);

  /**
    * Execute Coding transform.
    *
    *  @param context
    *  @param system
    *  @param code
    *  @param display may be null
    *  @param targetVariable may be null
    */
    void transformCoding(List<String> context, UrlData system, String code, String display, String targetVariable) throws Exception ;

    /**
    * Execute CodeableConcept transform.
    *
    *  @param context
    *  @param system
    *  @param code
    *  @param display may be null
    *  @param targetVariable may be null
    */
    void transformCodeableConcept(List<String> context, UrlData system, String code, String display, String targetVariable) throws Exception ;

    /**
    * Execute CodeableConcept transform.
    *
    *  @param context
    *  @param text
    *  @param targetVariable may be null
    */
    void transformCodeableConcept(List<String> context, String text, String targetVariable) throws Exception ;

    /// <summary>
    /// Execute copy transform & copy shorthand similar to following.
    /// 'cdr.subject = v'
    /// </summary>
    /// <param name="context"></param>
    /// <param name="assignVariable"></param>
    /// <param name="targetVariable">may be null</param>
    void transformCopy(List<String> context, String copyVariable, String targetVariable) throws Exception ;

    /**
    * Execute cp transform
    *
    *  @param context
    *  @param system May be null
    *  @param cpVariable
    *  @param targetVariable may be null
    */
    void transformCp(List<String> context, UrlData system, String cpVariable, String targetVariable) throws Exception ;

    /**
    * Execute create transform
    *
    *  @param context
    *  @param createVariable
    *  @param targetVariable may be null
    */
    void transformCreate(List<String> context, String createVariable, String targetVariable) throws Exception ;

    /**
    * Execute dateOp transform
    *
    *  @param context
    *  @param variable
    *  @param operation
    *  @param variable2 May be null
    *  @param targetVariable may be null
    */
    void transformDateOp(List<String> context, String variable, String operation, String variable2, String targetVariable) throws Exception ;

    /**
    * Execute escape transform
    *
    *  @param context
    *  @param variable
    *  @param string1
    *  @param string2 May be null
    *  @param targetVariable may be null
    */
    void transformEscape(List<String> context, String variable, String string1, String string2, String targetVariable) throws Exception ;

    void transformExtension(List<String> context, String targetVariable) throws Exception;

    /**
    * Execute evaluate transform
    *
    *  @param context
    *  @param obj
    *  @param objElement
    *  @param targetVariable may be null
    */
    void transformEvaluate(List<String> context, String obj, String objElement, String targetVariable) throws Exception ;

    /**
    * Execute id transform
    *
    *  @param context
    *  @param system
    *  @param value
    *  @param type
    *  @param targetVariable may be null
    */
    void transformId(List<String> context, UrlData system, String value, String type, String targetVariable) throws Exception ;

    /**
    * Execute pointer transform
    *
    *  @param context
    *  @param resource
    *  @param targetVariable may be null
    */
    void transformPointer(List<String> context, String resource, String targetVariable) throws Exception ;

    /**
    * Execute qty transform
    *
    *  @param context
    *  @param targetVariable may be null
    */
    void transformQty(List<String> context, String text, String targetVariable) throws Exception ;

    /**
    * Execute qty transform
    *
    *  @param context
    *  @param value
    *  @param unitString
    *  @param system
    *  @param targetVariable
    */
    void transformQty(List<String> context, String value, String unitString, UrlData system, String targetVariable) throws Exception ;

    /**
    * Execute qty transform
    *
    *  @param context
    *  @param value
    *  @param unitString
    *  @param type
    *  @param targetVariable
    */
    void transformQty(List<String> context, String value, String unitString, String type, String targetVariable) throws Exception ;

    /**
    * Execute reference transform.
    *
    *  @param context
    *  @param targetVariable may be null
    */
    void transformReference(List<String> context, String text, String targetVariable) throws Exception ;

    /**
    * Execute create transform
    *
    *  @param context
    *  @param variable
    *  @param mapUri
    *  @param outputType
    *  @param targetVariable may be null
    */
    void transformTranslate(List<String> context, String variable, UrlData mapUri, FhirMapTranslateOutputTypes outputType, String targetVariable) throws Exception ;

    /**
    * Execute truncate transform
    * 
    *  @param context 
    *  @param variable 
    *  @param length 
    *  @param targetVariable may be null
    */
    void transformTruncate(List<String> context, String variable, int length, String targetVariable) throws Exception ;

    /**
    * Execute uuid transform
    * 
    *  @param context 
    *  @param targetVariable may be null
    */
    void transformUuid(List<String> context, String targetVariable) throws Exception ;

    /**
    * Called when a rule's definition is complete.
    */
    void ruleComplete() throws Exception ;

}


