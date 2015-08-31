<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
  <sch:ns prefix="f" uri="http://hl7.org/fhir"/>
  <sch:ns prefix="h" uri="http://www.w3.org/1999/xhtml"/>
  <sch:pattern>
    <sch:title>Observation</sch:title>
    <sch:rule context="f:GeneticsMockup">
            <sch:assert test="count(f:subject) &gt;= 1">subject: minimum cardinality is 1</sch:assert>
    </sch:rule>
    <sch:rule context="f:GeneticsMockup">
            <sch:assert test="count(f:performer) &gt;= 1">performer: minimum cardinality is 1</sch:assert>
    </sch:rule>
    <sch:rule context="f:GeneticsMockup">
            <sch:assert test="count(f:performer) &lt;= 1">performer: maximum cardinality is 1</sch:assert>
    </sch:rule>
    <sch:rule context="f:GeneticsMockup">
            <sch:assert test="count(f:value[x]) &lt;= 0">value[x]: maximum cardinality is 0</sch:assert>
    </sch:rule>
    <sch:rule context="f:GeneticsMockup">
            <sch:assert test="count(f:specimen) &gt;= 1">specimen: minimum cardinality is 1</sch:assert>
    </sch:rule>
    <sch:rule context="f:GeneticsMockup">
            <sch:assert test="count(f:component) &lt;= 1">component: maximum cardinality is 1</sch:assert>
    </sch:rule>
    <sch:rule context="f:GeneticsMockup">
            <sch:assert test="count(f:component) &lt;= 1">component: maximum cardinality is 1</sch:assert>
    </sch:rule>
    <sch:rule context="f:GeneticsMockup">
            <sch:assert test="count(f:component) &lt;= 1">component: maximum cardinality is 1</sch:assert>
    </sch:rule>
    <sch:rule context="f:GeneticsMockup">
            <sch:assert test="count(f:component) &lt;= 1">component: maximum cardinality is 1</sch:assert>
    </sch:rule>
    <sch:rule context="f:GeneticsMockup">
            <sch:assert test="count(f:component) &lt;= 1">component: maximum cardinality is 1</sch:assert>
    </sch:rule>
    </sch:pattern>
  <sch:pattern>
    <sch:title>Observation.component</sch:title>
    <sch:rule context="f:GeneticsMockup/f:component">
            <sch:assert test="count(f:referenceRange) &lt;= 1">referenceRange: maximum cardinality is 1</sch:assert>
    </sch:rule>
    </sch:pattern>
  <sch:pattern>
    <sch:title>Observation.component</sch:title>
    <sch:rule context="f:GeneticsMockup/f:component">
            <sch:assert test="count(f:referenceRange) &lt;= 1">referenceRange: maximum cardinality is 1</sch:assert>
    </sch:rule>
    </sch:pattern>
  <sch:pattern>
    <sch:title>Observation.component</sch:title>
    <sch:rule context="f:GeneticsMockup/f:component">
            <sch:assert test="count(f:referenceRange) &lt;= 1">referenceRange: maximum cardinality is 1</sch:assert>
    </sch:rule>
    </sch:pattern>
  <sch:pattern>
    <sch:title>Observation.component</sch:title>
    <sch:rule context="f:GeneticsMockup/f:component">
            <sch:assert test="count(f:referenceRange) &lt;= 1">referenceRange: maximum cardinality is 1</sch:assert>
    </sch:rule>
    </sch:pattern>
  <sch:pattern>
    <sch:title>Observation.component</sch:title>
    <sch:rule context="f:GeneticsMockup/f:component">
            <sch:assert test="count(f:referenceRange) &lt;= 1">referenceRange: maximum cardinality is 1</sch:assert>
    </sch:rule>
    </sch:pattern>
  <sch:pattern>
    <sch:title>Observation.component</sch:title>
    <sch:rule context="f:GeneticsMockup/f:component">
            <sch:assert test="count(f:referenceRange) &lt;= 1">referenceRange: maximum cardinality is 1</sch:assert>
    </sch:rule>
    </sch:pattern>
</sch:schema>
