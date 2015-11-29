<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
  <sch:ns prefix="f" uri="http://hl7.org/fhir"/>
  <sch:ns prefix="h" uri="http://www.w3.org/1999/xhtml"/>
  <sch:pattern>
    <sch:title>f:ValueSet</sch:title>
    <sch:rule context="f:ValueSet">
      <sch:assert test="count(f:url) &gt;= 1">url: minimum cardinality is 1</sch:assert>
    </sch:rule>
    <sch:rule context="f:ValueSet">
      <sch:assert test="count(f:version) &gt;= 1">version: minimum cardinality is 1</sch:assert>
    </sch:rule>
    <sch:rule context="f:ValueSet">
      <sch:assert test="count(f:name) &gt;= 1">name: minimum cardinality is 1</sch:assert>
    </sch:rule>
    <sch:rule context="f:ValueSet">
      <sch:assert test="count(f:experimental) &gt;= 1">experimental: minimum cardinality is 1</sch:assert>
    </sch:rule>
    <sch:rule context="f:ValueSet">
      <sch:assert test="count(f:publisher) &gt;= 1">publisher: minimum cardinality is 1</sch:assert>
    </sch:rule>
    <sch:rule context="f:ValueSet">
      <sch:assert test="count(f:description) &gt;= 1">description: minimum cardinality is 1</sch:assert>
    </sch:rule>
    </sch:pattern>
  <sch:pattern>
    <sch:title>f:ValueSet/f:codeSystem</sch:title>
    <sch:rule context="f:ValueSet/f:codeSystem">
      <sch:assert test="count(f:caseSensitive) &gt;= 1">caseSensitive: minimum cardinality is 1</sch:assert>
    </sch:rule>
    </sch:pattern>
  <sch:pattern>
    <sch:title>f:ValueSet/f:codeSystem/f:concept</sch:title>
    <sch:rule context="f:ValueSet/f:codeSystem/f:concept">
      <sch:assert test="count(f:display) &gt;= 1">display: minimum cardinality is 1</sch:assert>
    </sch:rule>
    <sch:rule context="f:ValueSet/f:codeSystem/f:concept">
      <sch:assert test="count(f:definition) &gt;= 1">definition: minimum cardinality is 1</sch:assert>
    </sch:rule>
    </sch:pattern>
</sch:schema>
