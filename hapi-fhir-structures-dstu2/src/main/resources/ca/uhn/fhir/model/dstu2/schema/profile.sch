<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
  <sch:ns prefix="f" uri="http://hl7.org/fhir"/>
  <sch:ns prefix="a" uri="http://www.w3.org/2005/Atom"/>
  <sch:ns prefix="h" uri="http://www.w3.org/1999/xhtml"/>
  <sch:pattern>
    <sch:title>Profile</sch:title>
    <sch:rule context="/f:DomainResource">
      <sch:assert test="exists(for $id in f:contained/*/@id return $id[not(ancestor::f:contained/parent::*/descendant::f:reference/@value=concat('#', $id))])">Inv-3: If the resource is contained in another resource, it SHALL be referred to from elsewhere in the resource</sch:assert>
      <sch:assert test="not(parent::f:contained and f:contained)">Inv-2: If the resource is contained in another resource, it SHALL not contain nested Resources</sch:assert>
      <sch:assert test="not(parent::f:contained and f:text)">Inv-1: If the resource is contained in another resource, it SHALL not contain any narrative</sch:assert>
    </sch:rule>
    <sch:rule context="/f:DomainResource/f:text/f:div">
      <sch:assert test="not(descendant-or-self::*/@*[not(name(.)=('abbr', 'accesskey', 'align', 'alt', 'axis', 'bgcolor', 'border', 'cellhalign', 'cellpadding', 'cellspacing', 'cellvalign', 'char', 'charoff', 'charset', 'cite', 'class', 'colspan', 'compact', 'coords', 'dir', 'frame', 'headers', 'height', 'href', 'hreflang', 'hspace', 'id', 'lang', 'longdesc', 'name', 'nowrap', 'rel', 'rev', 'rowspan', 'rules', 'scope', 'shape', 'span', 'src', 'start', 'style', 'summary', 'tabindex', 'title', 'type', 'valign', 'value', 'vspace', 'width'))])">Inv-3: The narrative SHALL contain only the basic html formatting attributes described in chapters 7-11 (except section 4 of chapter 9) and 15 of the HTML 4.0 standard, &lt;a&gt; elements (either name or href), images and internally contained style attributes</sch:assert>
      <sch:assert test="descendant::text()[normalize-space(.)!=''] or descendant::h:img[@src]">Inv-2: The narrative SHALL have some non-whitespace content</sch:assert>
      <sch:assert test="not(descendant-or-self::*[not(local-name(.)=('a', 'abbr', 'acronym', 'b', 'big', 'blockquote', 'br', 'caption', 'cite', 'code', 'colgroup', 'dd', 'dfn', 'div', 'dl', 'dt', 'em', 'h1', 'h2', 'h3', 'h4', 'h5', 'h6', 'hr', 'i', 'img', 'li', 'ol', 'p', 'pre', 'q', 'samp', 'small', 'span', 'strong', 'table', 'tbody', 'td', 'tfoot', 'th', 'thead', 'tr', 'tt', 'ul', 'var'))])">Inv-1: The narrative SHALL contain only the basic html formatting elements described in chapters 7-11 (except section 4 of chapter 9) and 15 of the HTML 4.0 standard, &lt;a&gt; elements (either name or href), images and internally contained style attributes</sch:assert>
    </sch:rule>
    <sch:rule context="/f:DomainResource/f:contained/f:meta/f:security">
      <sch:assert test="not (exists(f:valueSet) and exists(f:code)) or exists(f:system)">Inv-1: If a valueSet is provided, a system URI Is required</sch:assert>
    </sch:rule>
    <sch:rule context="/f:DomainResource/f:contained/f:meta/f:security/f:valueSet">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::a:content/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">Inv-1: SHALL have a local reference if the resource is provided inline</sch:assert>
    </sch:rule>
    <sch:rule context="/f:DomainResource/f:contained/f:meta/f:tag">
      <sch:assert test="not (exists(f:valueSet) and exists(f:code)) or exists(f:system)">Inv-1: If a valueSet is provided, a system URI Is required</sch:assert>
    </sch:rule>
    <sch:rule context="/f:DomainResource/f:contained/f:meta/f:tag/f:valueSet">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::a:content/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">Inv-1: SHALL have a local reference if the resource is provided inline</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Profile">
      <sch:assert test="count(distinct-values(f:searchParam/f:name/@value)) =count(f:searchParam/f:name)">Inv-3: Parameter names must be unique within structure</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Profile/f:identifier/f:period">
      <sch:assert test="not(exists(f:start)) or not(exists(f:end)) or (f:start/@value &lt;= f:end/@value)">Inv-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Profile/f:identifier/f:assigner">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::a:content/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">Inv-1: SHALL have a local reference if the resource is provided inline</sch:assert>
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
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::a:content/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">Inv-1: SHALL have a local reference if the resource is provided inline</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Profile/f:mapping">
      <sch:assert test="exists(f:uri) or exists(f:name)">Inv-2: Must have at a name or a uri (or both)</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Profile/f:snapshot">
      <sch:assert test="count(f:element) &gt;= count(distinct-values(f:element/f:path/@value))">Inv-1: Element paths must be unique - or not (LM)</sch:assert>
      <sch:assert test="exists(f:base) or (count(f:element) = count(f:element[exists(f:definition/f:formal) and exists(f:definition/f:min) and exists(f:definition/f:max) and exists(f:definition/f:isModifier)]))">Inv-4: If a structure is a snapshot, then each element definition must have a formal definition, cardinalities, and a isModifier flag</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Profile/f:snapshot/f:element">
      <sch:assert test="(f:max/@value = '*') or (f:max/@value &gt;= f:min/@value)">Inv-2: Min &lt;= Max</sch:assert>
      <sch:assert test="not(exists(f:*[starts-with(local-name(.), 'pattern')])) or (count(f:type)=1 )">Inv-7: Pattern may only be specified if there is one type</sch:assert>
      <sch:assert test="not(exists(f:*[starts-with(local-name(.), 'fixed')])) or (count(f:type)=1 )">Inv-6: Fixed value may only be specified if there is one type</sch:assert>
      <sch:assert test="not(exists(f:nameReference) and exists(f:*[starts-with(local-name(.), 'value')]))">Inv-5: Either a namereference or a fixed value (but not both) is permitted</sch:assert>
      <sch:assert test="count(f:constraint[f:name]) = count(distinct-values(f:constraint/f:name/@value))">Inv-15: Constraint names must be unique.</sch:assert>
      <sch:assert test="not(exists(f:*[starts-with(local-name(.), 'pattern')])) or not(exists(f:*[starts-with(local-name(.), 'value')]))">Inv-8: Pattern and value are mutually exclusive</sch:assert>
      <sch:assert test="not(exists(f:*[starts-with(local-name(.), 'fixed')])) or not(exists(f:meaningWhenMissing))">Inv-16: default value and meaningWhenMissing are mutually exclusive</sch:assert>
      <sch:assert test="not(exists(for $type in f:type return $type/preceding-sibling::f:type[f:code/@value=$type/f:code/@value and f:profile/@value = $type/f:profile/@value]))">Inv-13: Types must be unique by the combination of code and profile</sch:assert>
      <sch:assert test="count(f:constraint) = count(distinct-values(f:constraint/f:key/@value))">Inv-14: Constraints must be unique by key</sch:assert>
      <sch:assert test="not(exists(f:binding)) or (count(f:type/f:code) = 0) or  f:type/f:code/@value=('code','Coding','CodeableConcept','Quantity','Extension')">Inv-11: Binding can only be present for coded elements</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Profile/f:snapshot/f:element/f:slicing">
      <sch:assert test="(f:discriminator) or (f:description)">Inv-1: If there is no discriminators, there must be a description</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Profile/f:snapshot/f:element/f:max">
      <sch:assert test="@value='*' or (normalize-space(@value)!='' and normalize-space(translate(@value, '0123456789',''))='')">Inv-3: Max SHALL be a number or &quot;*&quot;</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Profile/f:snapshot/f:element/f:type">
      <sch:assert test="not(exists(f:aggregation)) or exists(f:code[@value = 'Reference'])">Inv-4: Aggregation may only be specified if one of the allowed types for the element is a resource</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Profile/f:snapshot/f:element/f:binding">
      <sch:assert test="(exists(f:referenceUri) or exists(f:referenceResource)) or exists(f:description)">Inv-10: provide either a reference or a description (or both)</sch:assert>
      <sch:assert test="not(f:conformance/@value='example' and f:isExtensible/@value='false')">Inv-9: Example value sets are always extensible</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Profile/f:snapshot/f:element/f:binding/f:referenceUri">
      <sch:assert test="starts-with(@value, 'http:') or starts-with(@value, 'https:')">Inv-12: uri SHALL start with http:// or https://</sch:assert>
    </sch:rule>
    <sch:rule context="/f:Profile/f:snapshot/f:element/f:binding/f:referenceReference">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::a:content/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">Inv-1: SHALL have a local reference if the resource is provided inline</sch:assert>
    </sch:rule>
  </sch:pattern>
</sch:schema>
