<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
  <sch:ns prefix="f" uri="http://hl7.org/fhir"/>
  <sch:ns prefix="h" uri="http://www.w3.org/1999/xhtml"/>
  <!-- 
    This file contains just the constraints for the profile Questionnaire
    It includes the base constraints for the resource as well.
    Because of the way that schematrons and containment work, 
    you may need to use this schematron fragment to build a, 
    single schematron that validates contained resources (if you have any) 
  -->
  <sch:pattern>
    <sch:title>Questionnaire</sch:title>
    <sch:rule context="f:Questionnaire">
      <sch:assert test="not(parent::f:contained and f:contained)">If the resource is contained in another resource, it SHALL NOT contain nested Resources (inherited)</sch:assert>
      <sch:assert test="not(parent::f:contained and f:text)">If the resource is contained in another resource, it SHALL NOT contain any narrative (inherited)</sch:assert>
      <sch:assert test="not(exists(f:contained/*/f:meta/f:versionId)) and not(exists(f:contained/*/f:meta/f:lastUpdated))">If a resource is contained in another resource, it SHALL NOT have a meta.versionId or a meta.lastUpdated (inherited)</sch:assert>
      <sch:assert test="not(exists(for $contained in f:contained return $contained[not(parent::*/descendant::f:reference/@value=concat('#', $contained/*/id/@value) or descendant::f:reference[@value='#'])]))">If the resource is contained in another resource, it SHALL be referred to from elsewhere in the resource or SHALL refer to the containing resource (inherited)</sch:assert>
      <sch:assert test="count(descendant::f:linkId/@value)=count(distinct-values(descendant::f:linkId/@value))">The link ids for groups and questions must be unique within the questionnaire (inherited)</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>Questionnaire.extension</sch:title>
    <sch:rule context="f:Questionnaire/f:extension">
      <sch:assert test="@value|f:*|h:div">All FHIR elements must have a @value or children (inherited)</sch:assert>
      <sch:assert test="exists(f:extension)!=exists(f:*[starts-with(local-name(.), 'value')])">Must have either extensions or value[x], not both (inherited)</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>f:Questionnaire/f:item</sch:title>
    <sch:rule context="f:Questionnaire/f:item">
      <sch:assert test="count(f:extension[@url = 'http://hl7.org/fhir/StructureDefinition/cqif-condition']) &lt;= 1">extension with URL = 'http://hl7.org/fhir/StructureDefinition/cqif-condition': maximum cardinality of 'extension' is 1</sch:assert>
      <sch:assert test="count(f:extension[@url = 'http://hl7.org/fhir/StructureDefinition/cqif-initialValue']) &lt;= 1">extension with URL = 'http://hl7.org/fhir/StructureDefinition/cqif-initialValue': maximum cardinality of 'extension' is 1</sch:assert>
      <sch:assert test="count(f:extension[@url = 'http://hl7.org/fhir/StructureDefinition/cqif-calculatedValue']) &lt;= 1">extension with URL = 'http://hl7.org/fhir/StructureDefinition/cqif-calculatedValue': maximum cardinality of 'extension' is 1</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>Questionnaire.item</sch:title>
    <sch:rule context="f:Questionnaire/f:item">
      <sch:assert test="@value|f:*|h:div">All FHIR elements must have a @value or children (inherited)</sch:assert>
      <sch:assert test="not(f:type/@value=('group', 'display') and f:*[starts-with(local-name(.), 'initial')])">Read-only can't be specified for &quot;display&quot; items (inherited)</sch:assert>
      <sch:assert test="not(f:type/@value=('group', 'display') and f:*[starts-with(local-name(.), 'initial')])">Initial values can't be specified for groups or display items (inherited)</sch:assert>
      <sch:assert test="not(f:type/@value='display' and (f:required or f:repeats))">Required and repeat aren't permitted for display items (inherited)</sch:assert>
      <sch:assert test="f:type/@value=('choice','open-choice','decimal','integer','date','dateTime','time','string','quantity',') or not(f:option or f:options)">Only 'choice' items can have options (inherited)</sch:assert>
      <sch:assert test="not(f:options and f:option)">A question cannot have both option and options (inherited)</sch:assert>
      <sch:assert test="not(f:type/@value='display' and f:code)">Display items cannot have a &quot;code&quot; asserted (inherited)</sch:assert>
      <sch:assert test="f:type/@value=('boolean', 'decimal', 'integer', 'open-choice', 'string', 'text', 'url') or not(f:maxLength)">Maximum length can only be declared for simple question types (inherited)</sch:assert>
      <sch:assert test="not((f:type/@value='group' and not(f:item)) or (f:type/@value='display' and f:item))">Group items must have nested items, display items cannot have nested items (inherited)</sch:assert>
      <sch:assert test="not(f:option) or not(count(f:*[starts-with(local-name(.), 'initial')]))">If one or more option is present, initial[x] must be missing (inherited)</sch:assert>
      <sch:assert test="not(f:option) or not(count(f:*[starts-with(local-name(.), 'initial')]))">If there are more than one enableWhen, enableBehavior must be specified (inherited)</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>Questionnaire.item.extension</sch:title>
    <sch:rule context="f:Questionnaire/f:item/f:extension">
      <sch:assert test="@value|f:*|h:div">All FHIR elements must have a @value or children (inherited)</sch:assert>
      <sch:assert test="exists(f:extension)!=exists(f:*[starts-with(local-name(.), 'value')])">Must have either extensions or value[x], not both (inherited)</sch:assert>
      <sch:assert test="@value|f:*|h:div">All FHIR elements must have a @value or children (inherited)</sch:assert>
      <sch:assert test="exists(f:extension)!=exists(f:*[starts-with(local-name(.), 'value')])">Must have either extensions or value[x], not both (inherited)</sch:assert>
      <sch:assert test="@value|f:*|h:div">All FHIR elements must have a @value or children (inherited)</sch:assert>
      <sch:assert test="exists(f:extension)!=exists(f:*[starts-with(local-name(.), 'value')])">Must have either extensions or value[x], not both (inherited)</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>Questionnaire.item.enableWhen</sch:title>
    <sch:rule context="f:Questionnaire/f:item/f:enableWhen">
      <sch:assert test="@value|f:*|h:div">All FHIR elements must have a @value or children (inherited)</sch:assert>
      <sch:assert test="f:operator/@value != 'exists' or exists(f:answerBoolean)">If the operator is 'exists', the value must be a boolean (inherited)</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>Questionnaire.item.option</sch:title>
    <sch:rule context="f:Questionnaire/f:item/f:option">
      <sch:assert test="@value|f:*|h:div">All FHIR elements must have a @value or children (inherited)</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>Questionnaire.item.option.extension</sch:title>
    <sch:rule context="f:Questionnaire/f:item/f:option/f:extension">
      <sch:assert test="@value|f:*|h:div">All FHIR elements must have a @value or children (inherited)</sch:assert>
      <sch:assert test="exists(f:extension)!=exists(f:*[starts-with(local-name(.), 'value')])">Must have either extensions or value[x], not both (inherited)</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>Questionnaire.item.initial</sch:title>
    <sch:rule context="f:Questionnaire/f:item/f:initial">
      <sch:assert test="@value|f:*|h:div">All FHIR elements must have a @value or children (inherited)</sch:assert>
    </sch:rule>
  </sch:pattern>
</sch:schema>
