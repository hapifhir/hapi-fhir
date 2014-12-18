<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
  <sch:ns prefix="f" uri="http://hl7.org/fhir"/>
  <sch:ns prefix="a" uri="http://www.w3.org/2005/Atom"/>
  <sch:ns prefix="h" uri="http://www.w3.org/1999/xhtml"/>
  <sch:pattern>
    <sch:title>Conformance</sch:title>
    <sch:rule context="/f:Conformance">
      <sch:assert test="count(f:software | f:implementation | f:description) &gt; 0">Inv-2: A Conformance statement SHALL have at least one of description, software, or implementation</sch:assert>
      <sch:assert test="exists(f:rest) or exists(f:messaging) or exists(f:document)">Inv-1: A Conformance statement SHALL have at least one of rest, messaging or document</sch:assert>
      <sch:assert test="count(f:document[f:mode='producer'])=count(distinct-values(f:document[f:mode='producer']/f:profile/@value)) and count(f:document[f:mode='consumer'])=count(distinct-values(f:document[f:mode='consumer']/f:profile/@value))">Inv-7: The set of documents must be unique by the combination of profile &amp; mode</sch:assert>
      <sch:assert test="count(f:messaging/f:endpoint)=count(distinct-values(f:messaging/f:endpoint/@value))">Inv-5: The set of end points listed for messaging must be unique</sch:assert>
      <sch:assert test="count(f:messaging)&lt;=1 or not(f:messaging[not(f:endpoint)])">Inv-4: If there is more than one messaging element, endpoint must be specified for each one</sch:assert>
      <sch:assert test="count(f:rest)=count(distinct-values(f:rest/f:mode/@value))">Inv-8: There can only be one REST declaration per mode</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Conformance/f:telecom">
      <sch:assert test="not(exists(f:value)) or exists(f:system)">Inv-2: A system is required if a value is provided.</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Conformance/f:telecom/f:period">
      <sch:assert test="not(exists(f:start)) or not(exists(f:end)) or (f:start/@value &lt;= f:end/@value)">Inv-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Conformance/f:profile">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::a:content/f:*/f:contained/f:*[@id=substring-after(current()/f:reference/@value, '#')]|/f:*/f:contained/f:*[@id=substring-after(current()/f:reference/@value, '#')])">Inv-1: SHALL have a local reference if the resource is provided inline</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Conformance/f:rest">
      <sch:assert test="count(f:query)=count(distinct-values(f:query/f:name/@value))">Inv-10: A given query can only be described once per RESTful mode</sch:assert>
      <sch:assert test="count(f:resource)=count(distinct-values(f:resource/f:type/@value))">Inv-9: A given resource can only be described once per RESTful mode</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Conformance/f:rest/f:security/f:service">
      <sch:assert test="count(f:coding[f:primary/@value='true'])&lt;1">Inv-2: Only one coding in a set can be chosen directly by the user</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Conformance/f:rest/f:security/f:service/f:coding">
      <sch:assert test="not (exists(f:valueSet) and exists(f:code)) or exists(f:system)">Inv-1: If a valueSet is provided, a system URI Is required</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Conformance/f:rest/f:security/f:service/f:coding/f:valueSet">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::a:content/f:*/f:contained/f:*[@id=substring-after(current()/f:reference/@value, '#')]|/f:*/f:contained/f:*[@id=substring-after(current()/f:reference/@value, '#')])">Inv-1: SHALL have a local reference if the resource is provided inline</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Conformance/f:rest/f:resource">
      <sch:assert test="count(f:operation)=count(distinct-values(f:operation/f:code/@value))">Inv-11: Operation codes must be unique in the context of a resource</sch:assert>
      <sch:assert test="count(f:searchParam)=count(distinct-values(f:searchParam/f:name/@value))">Inv-12: Search parameter names must be unique in the context of a resource</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Conformance/f:rest/f:resource/f:profile">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::a:content/f:*/f:contained/f:*[@id=substring-after(current()/f:reference/@value, '#')]|/f:*/f:contained/f:*[@id=substring-after(current()/f:reference/@value, '#')])">Inv-1: SHALL have a local reference if the resource is provided inline</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Conformance/f:rest/f:query">
      <sch:assert test="count(f:parameter)=count(distinct-values(f:parameter/f:name/@value))">Inv-13: Search parameter names must be unique in the context of a query</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Conformance/f:messaging">
      <sch:assert test="exists(f:endpoint) = exists(parent::f:Conformance/f:implementation)">Inv-3: Messaging end point is required (and is only permitted) when statement is for an implementation</sch:assert>
      <sch:assert test="count(f:event[f:mode='sender'])=count(distinct-values(f:event[f:mode='sender']/f:code/@value)) and count(f:event[f:mode='receiver'])=count(distinct-values(f:event[f:mode='receiver']/f:code/@value))">Inv-6: The set of events per messaging endpoint must be unique by the combination of code &amp; mode</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Conformance/f:messaging/f:event/f:code">
      <sch:assert test="not (exists(f:valueSet) and exists(f:code)) or exists(f:system)">Inv-1: If a valueSet is provided, a system URI Is required</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Conformance/f:messaging/f:event/f:code/f:valueSet">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::a:content/f:*/f:contained/f:*[@id=substring-after(current()/f:reference/@value, '#')]|/f:*/f:contained/f:*[@id=substring-after(current()/f:reference/@value, '#')])">Inv-1: SHALL have a local reference if the resource is provided inline</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Conformance/f:messaging/f:event/f:protocol">
      <sch:assert test="not (exists(f:valueSet) and exists(f:code)) or exists(f:system)">Inv-1: If a valueSet is provided, a system URI Is required</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Conformance/f:messaging/f:event/f:protocol/f:valueSet">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::a:content/f:*/f:contained/f:*[@id=substring-after(current()/f:reference/@value, '#')]|/f:*/f:contained/f:*[@id=substring-after(current()/f:reference/@value, '#')])">Inv-1: SHALL have a local reference if the resource is provided inline</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Conformance/f:messaging/f:event/f:request">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::a:content/f:*/f:contained/f:*[@id=substring-after(current()/f:reference/@value, '#')]|/f:*/f:contained/f:*[@id=substring-after(current()/f:reference/@value, '#')])">Inv-1: SHALL have a local reference if the resource is provided inline</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Conformance/f:messaging/f:event/f:response">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::a:content/f:*/f:contained/f:*[@id=substring-after(current()/f:reference/@value, '#')]|/f:*/f:contained/f:*[@id=substring-after(current()/f:reference/@value, '#')])">Inv-1: SHALL have a local reference if the resource is provided inline</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Conformance/f:document/f:profile">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::a:content/f:*/f:contained/f:*[@id=substring-after(current()/f:reference/@value, '#')]|/f:*/f:contained/f:*[@id=substring-after(current()/f:reference/@value, '#')])">Inv-1: SHALL have a local reference if the resource is provided inline</sch:assert>
    </sch:rule>
  </sch:pattern>
</sch:schema>
