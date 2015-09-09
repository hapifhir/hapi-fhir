<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
  <sch:ns prefix="f" uri="http://hl7.org/fhir"/>
  <sch:ns prefix="h" uri="http://www.w3.org/1999/xhtml"/>
  <sch:pattern>
    <sch:title>f:Composition</sch:title>
    <sch:rule context="f:Composition">
      <sch:assert test="count(f:confidentiality) &gt;= 1">confidentiality: minimum cardinality is 1</sch:assert>
    </sch:rule>
    <sch:rule context="f:Composition">
      <sch:assert test="count(f:event) &lt;= 0">event: maximum cardinality is 0</sch:assert>
    </sch:rule>
    <sch:rule context="f:Composition">
      <sch:assert test="count(f:encounter) &lt;= 0">encounter: maximum cardinality is 0</sch:assert>
    </sch:rule>
    </sch:pattern>
  <sch:pattern>
    <sch:title>f:Composition/f:event</sch:title>
    <sch:rule context="f:Composition/f:event">
      <sch:assert test="count(f:code) &lt;= 0">code: maximum cardinality is 0</sch:assert>
    </sch:rule>
    <sch:rule context="f:Composition/f:event">
      <sch:assert test="count(f:period) &lt;= 0">period: maximum cardinality is 0</sch:assert>
    </sch:rule>
    <sch:rule context="f:Composition/f:event">
      <sch:assert test="count(f:detail) &lt;= 0">detail: maximum cardinality is 0</sch:assert>
    </sch:rule>
    </sch:pattern>
  <sch:pattern>
    <sch:title>f:Composition/f:section</sch:title>
    <sch:rule context="f:Composition/f:section">
      <sch:assert test="count(f:entry) &gt;= 1">entry: minimum cardinality is 1</sch:assert>
    </sch:rule>
    <sch:rule context="f:Composition/f:section">
      <sch:assert test="count(f:entry) &lt;= 1">entry: maximum cardinality is 1</sch:assert>
    </sch:rule>
    </sch:pattern>
</sch:schema>
