<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
  <sch:ns prefix="f" uri="http://hl7.org/fhir"/>
  <sch:ns prefix="h" uri="http://www.w3.org/1999/xhtml"/>
  <!-- 
    This file contains just the constraints for the profile ValueSet
    It includes the base constraints for the resource as well.
    Because of the way that schematrons and containment work, 
    you may need to use this schematron fragment to build a, 
    single schematron that validates contained resources (if you have any) 
  -->
  <sch:pattern>
    <sch:title>f:ValueSet</sch:title>
    <sch:rule context="f:ValueSet">
      <sch:assert test="count(f:url) &gt;= 1">url: minimum cardinality of 'url' is 1</sch:assert>
      <sch:assert test="count(f:version) &gt;= 1">version: minimum cardinality of 'version' is 1</sch:assert>
      <sch:assert test="count(f:name) &gt;= 1">name: minimum cardinality of 'name' is 1</sch:assert>
      <sch:assert test="count(f:experimental) &gt;= 1">experimental: minimum cardinality of 'experimental' is 1</sch:assert>
      <sch:assert test="count(f:publisher) &gt;= 1">publisher: minimum cardinality of 'publisher' is 1</sch:assert>
      <sch:assert test="count(f:description) &gt;= 1">description: minimum cardinality of 'description' is 1</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>ValueSet</sch:title>
    <sch:rule context="f:ValueSet">
      <sch:assert test="not(f:codeSystem/f:system/@value = f:url/@value)">A defined code system (if present) SHALL have a different url than the value set url (inherited)</sch:assert>
      <sch:assert test="not(exists(f:compose)) or (count(f:compose/f:import)!=1 or exists(f:compose/f:include) or exists(f:compose/f:exclude) or exists(f:codeSystem))">A value set with only one import SHALL also have an include and/or an exclude unless the value set includes and inline code system (inherited)</sch:assert>
      <sch:assert test="exists(f:codeSystem) or exists(f:compose) or exists(f:expansion)">Value set SHALL contain at least one of a codeSystem, a compose, or an expansion element (inherited)</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>f:ValueSet/f:codeSystem</sch:title>
    <sch:rule context="f:ValueSet/f:codeSystem">
      <sch:assert test="count(f:caseSensitive) &gt;= 1">caseSensitive: minimum cardinality of 'caseSensitive' is 1</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>ValueSet.codeSystem</sch:title>
    <sch:rule context="f:ValueSet/f:codeSystem">
      <sch:assert test="count(descendant::f:concept)=count(distinct-values(descendant::f:concept/f:code/@value))">Codes must be unique (inherited)</sch:assert>
      <sch:assert test="count(distinct-values(descendant::f:concept/f:code/@value))=count(descendant::f:concept)">Within a code system definition, all the codes SHALL be unique (inherited)</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>f:ValueSet/f:codeSystem/f:concept</sch:title>
    <sch:rule context="f:ValueSet/f:codeSystem/f:concept">
      <sch:assert test="count(f:display) &gt;= 1">display: minimum cardinality of 'display' is 1</sch:assert>
      <sch:assert test="count(f:definition) &gt;= 1">definition: minimum cardinality of 'definition' is 1</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>ValueSet.compose</sch:title>
    <sch:rule context="f:ValueSet/f:compose">
      <sch:assert test="exists(f:include) or exists(f:import)">A value set composition SHALL have an include or an import (inherited)</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>ValueSet.compose.include</sch:title>
    <sch:rule context="f:ValueSet/f:compose/f:include">
      <sch:assert test="not(exists(f:concept)) or not(exists(f:filter))">Cannot have both concept and filter (inherited)</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>ValueSet.expansion.contains</sch:title>
    <sch:rule context="f:ValueSet/f:expansion/f:contains">
      <sch:assert test="exists(f:code) or exists(f:display)">SHALL have a code or a display (inherited)</sch:assert>
      <sch:assert test="exists(f:code) or (f:abstract/@value = true())">Must have a code if not abstract (inherited)</sch:assert>
      <sch:assert test="exists(f:system) or not(exists(f:code))">Must have a system if a code is present (inherited)</sch:assert>
    </sch:rule>
  </sch:pattern>
</sch:schema>
