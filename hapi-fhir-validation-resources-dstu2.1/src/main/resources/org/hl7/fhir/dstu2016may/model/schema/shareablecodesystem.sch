<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
  <sch:ns prefix="f" uri="http://hl7.org/fhir"/>
  <sch:ns prefix="h" uri="http://www.w3.org/1999/xhtml"/>
  <!-- 
    This file contains just the constraints for the profile CodeSystem
    It includes the base constraints for the resource as well.
    Because of the way that schematrons and containment work, 
    you may need to use this schematron fragment to build a, 
    single schematron that validates contained resources (if you have any) 
  -->
  <sch:pattern>
    <sch:title>f:CodeSystem</sch:title>
    <sch:rule context="f:CodeSystem">
      <sch:assert test="count(f:url) &gt;= 1">url: minimum cardinality of 'url' is 1</sch:assert>
      <sch:assert test="count(f:version) &gt;= 1">version: minimum cardinality of 'version' is 1</sch:assert>
      <sch:assert test="count(f:name) &gt;= 1">name: minimum cardinality of 'name' is 1</sch:assert>
      <sch:assert test="count(f:experimental) &gt;= 1">experimental: minimum cardinality of 'experimental' is 1</sch:assert>
      <sch:assert test="count(f:publisher) &gt;= 1">publisher: minimum cardinality of 'publisher' is 1</sch:assert>
      <sch:assert test="count(f:description) &gt;= 1">description: minimum cardinality of 'description' is 1</sch:assert>
      <sch:assert test="count(f:caseSensitive) &gt;= 1">caseSensitive: minimum cardinality of 'caseSensitive' is 1</sch:assert>
      <sch:assert test="count(f:concept) &gt;= 1">concept: minimum cardinality of 'concept' is 1</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>CodeSystem</sch:title>
    <sch:rule context="f:CodeSystem">
      <sch:assert test="count(distinct-values(descendant::f:concept/f:code/@value))=count(descendant::f:concept)">Within a code system definition, all the codes SHALL be unique (inherited)</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>f:CodeSystem/f:concept</sch:title>
    <sch:rule context="f:CodeSystem/f:concept">
      <sch:assert test="count(f:display) &gt;= 1">display: minimum cardinality of 'display' is 1</sch:assert>
      <sch:assert test="count(f:definition) &gt;= 1">definition: minimum cardinality of 'definition' is 1</sch:assert>
    </sch:rule>
  </sch:pattern>
</sch:schema>
