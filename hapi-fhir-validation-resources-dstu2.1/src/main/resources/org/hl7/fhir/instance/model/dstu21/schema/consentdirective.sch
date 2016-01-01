<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
  <sch:ns prefix="f" uri="http://hl7.org/fhir"/>
  <sch:ns prefix="h" uri="http://www.w3.org/1999/xhtml"/>
  <sch:pattern>
    <sch:title>f:Contract</sch:title>
    <sch:rule context="f:Contract">
      <sch:assert test="count(f:valuedItem) &lt;= 0">valuedItem: maximum cardinality is 0</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract">
      <sch:assert test="count(f:binding[x]) &gt;= 1">binding[x]: minimum cardinality is 1</sch:assert>
    </sch:rule>
    </sch:pattern>
  <sch:pattern>
    <sch:title>f:Contract/f:term</sch:title>
    <sch:rule context="f:Contract/f:term">
      <sch:assert test="count(f:valuedItem) &lt;= 0">valuedItem: maximum cardinality is 0</sch:assert>
    </sch:rule>
    </sch:pattern>
</sch:schema>
