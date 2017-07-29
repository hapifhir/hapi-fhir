<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
  <sch:ns prefix="f" uri="http://hl7.org/fhir"/>
  <sch:ns prefix="h" uri="http://www.w3.org/1999/xhtml"/>
  <!-- 
    This file contains just the constraints for the profile Observation
    It includes the base constraints for the resource as well.
    Because of the way that schematrons and containment work, 
    you may need to use this schematron fragment to build a, 
    single schematron that validates contained resources (if you have any) 
  -->
  <sch:pattern>
    <sch:title>f:Observation</sch:title>
    <sch:rule context="f:Observation">
      <sch:assert test="count(f:extension[@url = 'http://hl7.org/fhir/StructureDefinition/observation-geneticsTranscriptReferenceSequenceId']) &lt;= 1">extension with URL = 'http://hl7.org/fhir/StructureDefinition/observation-geneticsTranscriptReferenceSequenceId': maximum cardinality of 'extension' is 1</sch:assert>
      <sch:assert test="count(f:extension[@url = 'http://hl7.org/fhir/StructureDefinition/observation-geneticsProteinReferenceSequenceId']) &lt;= 1">extension with URL = 'http://hl7.org/fhir/StructureDefinition/observation-geneticsProteinReferenceSequenceId': maximum cardinality of 'extension' is 1</sch:assert>
      <sch:assert test="count(f:extension[@url = 'http://hl7.org/fhir/StructureDefinition/observation-geneticsDNASequenceVariation']) &lt;= 1">extension with URL = 'http://hl7.org/fhir/StructureDefinition/observation-geneticsDNASequenceVariation': maximum cardinality of 'extension' is 1</sch:assert>
      <sch:assert test="count(f:extension[@url = 'http://hl7.org/fhir/StructureDefinition/observation-geneticsDNAVariationId']) &lt;= 1">extension with URL = 'http://hl7.org/fhir/StructureDefinition/observation-geneticsDNAVariationId': maximum cardinality of 'extension' is 1</sch:assert>
      <sch:assert test="count(f:extension[@url = 'http://hl7.org/fhir/StructureDefinition/observation-geneticsDNASequenceVariationType']) &lt;= 1">extension with URL = 'http://hl7.org/fhir/StructureDefinition/observation-geneticsDNASequenceVariationType': maximum cardinality of 'extension' is 1</sch:assert>
      <sch:assert test="count(f:extension[@url = 'http://hl7.org/fhir/StructureDefinition/observation-geneticsAminoAcidChange']) &lt;= 1">extension with URL = 'http://hl7.org/fhir/StructureDefinition/observation-geneticsAminoAcidChange': maximum cardinality of 'extension' is 1</sch:assert>
      <sch:assert test="count(f:extension[@url = 'http://hl7.org/fhir/StructureDefinition/observation-geneticsAminoAcidChangeType']) &lt;= 1">extension with URL = 'http://hl7.org/fhir/StructureDefinition/observation-geneticsAminoAcidChangeType': maximum cardinality of 'extension' is 1</sch:assert>
      <sch:assert test="count(f:extension[@url = 'http://hl7.org/fhir/StructureDefinition/observation-geneticsGene']) &lt;= 1">extension with URL = 'http://hl7.org/fhir/StructureDefinition/observation-geneticsGene': maximum cardinality of 'extension' is 1</sch:assert>
      <sch:assert test="count(f:extension[@url = 'http://hl7.org/fhir/StructureDefinition/observation-geneticsDNARegionName']) &lt;= 1">extension with URL = 'http://hl7.org/fhir/StructureDefinition/observation-geneticsDNARegionName': maximum cardinality of 'extension' is 1</sch:assert>
      <sch:assert test="count(f:extension[@url = 'http://hl7.org/fhir/StructureDefinition/observation-geneticsAlleleName']) &lt;= 1">extension with URL = 'http://hl7.org/fhir/StructureDefinition/observation-geneticsAlleleName': maximum cardinality of 'extension' is 1</sch:assert>
      <sch:assert test="count(f:extension[@url = 'http://hl7.org/fhir/StructureDefinition/observation-geneticsGenomicSourceClass']) &lt;= 1">extension with URL = 'http://hl7.org/fhir/StructureDefinition/observation-geneticsGenomicSourceClass': maximum cardinality of 'extension' is 1</sch:assert>
      <sch:assert test="count(f:extension[@url = 'http://hl7.org/fhir/StructureDefinition/observation-geneticsSequence']) &lt;= 1">extension with URL = 'http://hl7.org/fhir/StructureDefinition/observation-geneticsSequence': maximum cardinality of 'extension' is 1</sch:assert>
      <sch:assert test="count(f:extension[@url = 'http://hl7.org/fhir/StructureDefinition/observation-geneticsInterpretation']) &lt;= 1">extension with URL = 'http://hl7.org/fhir/StructureDefinition/observation-geneticsInterpretation': maximum cardinality of 'extension' is 1</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>Observation</sch:title>
    <sch:rule context="f:Observation">
      <sch:assert test="not(exists(f:dataAbsentReason)) or (not(exists(*[starts-with(local-name(.), 'value')])))">dataAbsentReason SHALL only be present if Observation.value[x] is not present (inherited)</sch:assert>
      <sch:assert test="not(exists(f:component/f:code)) or count(for $coding in f:code/f:coding return parent::*/f:component/f:code/f:coding[f:code/@value=$coding/f:code/@value and f:system/@value=$coding/f:system/@value])=0">Component code SHALL not be same as observation code (inherited)</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>Observation.referenceRange</sch:title>
    <sch:rule context="f:Observation/f:referenceRange">
      <sch:assert test="(exists(f:low) or exists(f:high)or exists(f:text))">Must have at least a low or a high or text (inherited)</sch:assert>
    </sch:rule>
  </sch:pattern>
</sch:schema>
