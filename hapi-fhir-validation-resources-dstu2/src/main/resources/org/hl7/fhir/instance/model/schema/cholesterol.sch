<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
  <sch:ns prefix="f" uri="http://hl7.org/fhir"/>
  <sch:ns prefix="h" uri="http://www.w3.org/1999/xhtml"/>
  <sch:pattern>
    <sch:title>f:Observation</sch:title>
    <sch:rule context="f:Observation">
      <sch:assert test="count(f:referenceRange) &gt;= 1">referenceRange: minimum cardinality is 1</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation">
      <sch:assert test="count(f:referenceRange) &lt;= 1">referenceRange: maximum cardinality is 1</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation">
      <sch:assert test="count(f:related) &lt;= 0">related: maximum cardinality is 0</sch:assert>
    </sch:rule>
    </sch:pattern>
  <sch:pattern>
    <sch:title>f:Observation/f:valueQuantity</sch:title>
    <sch:rule context="f:Observation/f:valueQuantity">
      <sch:assert test="count(f:comparator) &lt;= 0">comparator: maximum cardinality is 0</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:valueQuantity">
      <sch:assert test="count(f:unit) &gt;= 1">unit: minimum cardinality is 1</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:valueQuantity">
      <sch:assert test="count(f:system) &gt;= 1">system: minimum cardinality is 1</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:valueQuantity">
      <sch:assert test="count(f:code) &gt;= 1">code: minimum cardinality is 1</sch:assert>
    </sch:rule>
    </sch:pattern>
  <sch:pattern>
    <sch:title>f:Observation/f:referenceRange</sch:title>
    <sch:rule context="f:Observation/f:referenceRange">
      <sch:assert test="count(f:low) &lt;= 0">low: maximum cardinality is 0</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:referenceRange">
      <sch:assert test="count(f:high) &gt;= 1">high: minimum cardinality is 1</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:referenceRange">
      <sch:assert test="count(f:meaning) &lt;= 0">meaning: maximum cardinality is 0</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:referenceRange">
      <sch:assert test="count(f:age) &lt;= 0">age: maximum cardinality is 0</sch:assert>
    </sch:rule>
    </sch:pattern>
</sch:schema>
