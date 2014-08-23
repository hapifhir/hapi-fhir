<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
  <sch:ns prefix="f" uri="http://hl7.org/fhir"/>
  <sch:ns prefix="a" uri="http://www.w3.org/2005/Atom"/>
  <sch:ns prefix="h" uri="http://www.w3.org/1999/xhtml"/>
  <sch:pattern>
    <sch:title>Profile</sch:title>
    <sch:rule context="/f:Profile">
      <sch:assert test="not(exists(for $structure in f:structure return $structure/preceding-sibling::f:structure[f:type/@value=$structure/f:type/@value and f:name/@value = $structure/f:name/@value]))">Inv-17: There can't be multiple structures with the same type and name</sch:assert>
      <sch:assert test="count(f:structure[not(f:name)]) = count(distinct-values(f:structure[not(f:name)]/f:type/@value))">Inv-15: Where multiple structures exist with the same type, they SHALL have names</sch:assert>
      <sch:assert test="exists(f:structure) or exists(f:extensionDefn)">Inv-8: SHALL define at least one structure constraint or extension definition</sch:assert>
      <sch:assert test="count(f:extensionDefn) = count(distinct-values(f:extensionDefn/f:code/@value))">Inv-16: Extension definition codes must be unique</sch:assert>
      <sch:assert test="count(distinct-values(f:structure/f:name/@value)) =count(f:structure/f:name)">Inv-27: Structure name must be unique</sch:assert>
      <sch:assert test="count(distinct-values(f:query/f:name/@value)) =count(f:query/f:name)">Inv-28: Query name must be unique</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Profile/f:telecom">
      <sch:assert test="not(exists(f:value)) or exists(f:system)">Inv-2: A system is required if a value is provided.</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Profile/f:telecom/f:period">
      <sch:assert test="not(exists(f:start)) or not(exists(f:end)) or (f:start/@value &lt;= f:end/@value)">Inv-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Profile/f:code">
      <sch:assert test="not (exists(f:valueSet) and exists(f:code)) or exists(f:system)">Inv-1: If a valueSet is provided, a system URI Is required</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Profile/f:code/f:valueSet">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::a:content/f:*/f:contained/f:*[@id=substring-after(current()/f:reference/@value, '#')]|/f:*/f:contained/f:*[@id=substring-after(current()/f:reference/@value, '#')])">Inv-1: SHALL have a local reference if the resource is provided inline</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Profile/f:mapping">
      <sch:assert test="exists(f:uri) or exists(f:name)">Inv-26: Must have at a name or a uri (or both)</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Profile/f:structure">
      <sch:assert test="count(f:element) &gt;= count(distinct-values(f:element/f:path/@value))">Inv-18: Element paths must be unique - or not (LM)</sch:assert>
      <sch:assert test="count(distinct-values(f:searchParam/f:name/@value)) =count(f:searchParam/f:name)">Inv-29: Parameter names must be unique within structure</sch:assert>
      <sch:assert test="upper-case(substring(f:type,1,1))=substring(f:type,1,1)">Inv-12: Only complex types can be constrained, not primitive types such as string etc.</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Profile/f:structure/f:element">
      <sch:assert test="not(f:slicing) or (not(starts-with(preceding-sibling::f:element[1]/f:path/@value, current()/f:path/@value)) and following-sibling::f:element[1]/f:path/@value=current()/f:path/@value)">Inv-21: An element that's a slicing descriptor must not be preceded by an element that starts with the same path and must be followed by an element with exactly the same path.</sch:assert>
      <sch:assert test="exists(f:slicing)!=exists(f:definition)">Inv-20: An element must either be a definition or a slicing descriptor, never both.</sch:assert>
      <sch:assert test="exists(f:slicing) != exists(f:definition)">Inv-11: Must have either a slice or a definition, but not both</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Profile/f:structure/f:element/f:definition">
      <sch:assert test="not(exists(f:nameReference) and exists(f:*[starts-with(local-name(.), 'value')]))">Inv-2: Either a namereference or a fixed value (but not both) is permitted</sch:assert>
      <sch:assert test="not(exists(f:*[starts-with(local-name(.), 'value')])) or (count(f:type)=1 and f:type/f:code[substring(@value,1,1)=lower-case(substring(@value,1,1))])">Inv-10: Value may only be specified if the type consists of a single repetition that has a type corresponding to one of the primitive data types.</sch:assert>
      <sch:assert test="not(exists(f:binding)) or f:type/f:code/@value=('code','Coding','CodeableConcept','Quantity')">Inv-7: Binding can only be present for coded elements</sch:assert>
      <sch:assert test="count(f:element[f:name]) = count(distinct-values(f:element/f:name/@value))">Inv-19: Element names must be unique</sch:assert>
      <sch:assert test="count(f:type[not(f:profile)]) = count(distinct-values(f:type[not(f:profile)]/f:code/@value))">Inv-22: If a definition has multiple types with the same code, each must specify a profile</sch:assert>
      <sch:assert test="not(exists(for $type in f:type return $type/preceding-sibling::f:type[f:code/@value=$type/f:code/@value and f:profile/@value = $type/f:profile/@value]))">Inv-23: Types must be unique by the combination of code and profile</sch:assert>
      <sch:assert test="count(f:constraint) = count(distinct-values(f:constraint/f:key/@value))">Inv-24: Constraints must be unique by key</sch:assert>
      <sch:assert test="count(f:constraint[f:name]) = count(distinct-values(f:constraint/f:name/@value))">Inv-25: Constraint names must be unique.</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Profile/f:structure/f:element/f:definition/f:max">
      <sch:assert test="@value='*' or (normalize-space(@value)!='' and normalize-space(translate(@value, '0123456789',''))='')">Inv-6: Max SHALL be a number or &quot;*&quot;</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Profile/f:structure/f:element/f:definition/f:type">
      <sch:assert test="not(exists(f:aggregation)) or exists(f:code[starts-with(@value, 'Resource(')])">Inv-9: Aggregation may only be specified if one of the allowed types for the element is a resource</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Profile/f:structure/f:element/f:definition/f:binding">
      <sch:assert test="(exists(f:referenceUri) or exists(f:referenceResource)) or exists(f:description)">Inv-3: provide either a reference or a description (or both)</sch:assert>
      <sch:assert test="not(f:Conformance/value='example' and f:isExtensible.value='false')">Inv-14: Example value sets are always extensible</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Profile/f:structure/f:element/f:definition/f:binding/f:referenceUri">
      <sch:assert test="starts-with(@value, 'http:') or starts-with(@value, 'https:')">Inv-13: uri SHALL start with http:// or https://</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Profile/f:structure/f:element/f:definition/f:binding/f:referenceResource">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::a:content/f:*/f:contained/f:*[@id=substring-after(current()/f:reference/@value, '#')]|/f:*/f:contained/f:*[@id=substring-after(current()/f:reference/@value, '#')])">Inv-1: SHALL have a local reference if the resource is provided inline</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Profile/f:extensionDefn/f:code">
      <sch:assert test="count(ancestor::f:Profile/f:extensionDefn/f:code[@value=current()/@value])=1">Inv-5: Codes SHALL be unique in the context of a profile</sch:assert>
    </sch:rule>
  </sch:pattern>
</sch:schema>
