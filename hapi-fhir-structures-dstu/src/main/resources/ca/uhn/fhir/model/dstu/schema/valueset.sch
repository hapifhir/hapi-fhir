<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
  <sch:ns prefix="f" uri="http://hl7.org/fhir"/>
  <sch:ns prefix="a" uri="http://www.w3.org/2005/Atom"/>
  <sch:ns prefix="h" uri="http://www.w3.org/1999/xhtml"/>
  <sch:pattern>
    <sch:title>ValueSet</sch:title>
    <sch:rule context="/f:ValueSet">
      <sch:assert test="not(exists(f:compose)) or (count(f:compose/f:import)!=1 or exists(f:compose/f:include) or exists(f:compose/f:exclude) or exists(f:define))">Inv-2: A value set with only one import SHALL also have an include and/or an exclude unless the value set defines its own codes</sch:assert>
      <sch:assert test="not(exists(f:define)) or (f:define/f:system/@value != f:identifier/@value)">Inv-7: A defined code system (if present) SHALL have a different identifier to the value set itself</sch:assert>
      <sch:assert test="exists(f:define) or exists(f:compose) or exists(f:expansion)">Inv-5: Value set SHALL contain either a define, a compose, or an expansion element</sch:assert>
    </sch:rule>
    <sch:rule context="/f:ValueSet/f:telecom">
      <sch:assert test="not(exists(f:value)) or exists(f:system)">Inv-2: A system is required if a value is provided.</sch:assert>
    </sch:rule>
    <sch:rule context="/f:ValueSet/f:telecom/f:period">
      <sch:assert test="not(exists(f:start)) or not(exists(f:end)) or (f:start/@value &lt;= f:end/@value)">Inv-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="/f:ValueSet/f:define">
      <sch:assert test="count(distinct-values(descendant::f:concept/f:code/@value))=count(descendant::f:concept)">Inv-3: Within a code system definition, all the codes SHALL be unique</sch:assert>
      <sch:assert test="count(descendant::f:concept)=count(distinct-values(descendant::f:concept/f:code/@value))">Inv-8: Codes must be unique</sch:assert>
    </sch:rule>
    <sch:rule context="/f:ValueSet/f:compose">
      <sch:assert test="exists(f:include) or exists(f:import)">Inv-1: A value set composition SHALL have an include or an import</sch:assert>
    </sch:rule>
    <sch:rule context="/f:ValueSet/f:expansion/f:identifier/f:period">
      <sch:assert test="not(exists(f:start)) or not(exists(f:end)) or (f:start/@value &lt;= f:end/@value)">Inv-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="/f:ValueSet/f:expansion/f:identifier/f:assigner">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::a:content/f:*/f:contained/f:*[@id=substring-after(current()/f:reference/@value, '#')]|/f:*/f:contained/f:*[@id=substring-after(current()/f:reference/@value, '#')])">Inv-1: SHALL have a local reference if the resource is provided inline</sch:assert>
    </sch:rule>
    <sch:rule context="/f:ValueSet/f:expansion/f:contains">
      <sch:assert test="exists(f:code) or exists(f:display)">Inv-6: SHALL have a code or a display</sch:assert>
    </sch:rule>
  </sch:pattern>
</sch:schema>
