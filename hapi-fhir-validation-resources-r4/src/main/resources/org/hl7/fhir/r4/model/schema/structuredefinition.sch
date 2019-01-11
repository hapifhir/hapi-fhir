<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
  <sch:ns prefix="f" uri="http://hl7.org/fhir"/>
  <sch:ns prefix="h" uri="http://www.w3.org/1999/xhtml"/>
  <!-- 
    This file contains just the constraints for the resource StructureDefinition
    It is provided for documentation purposes. When actually validating,
    always use fhir-invariants.sch (because of the way containment works)
    Alternatively you can use this file to build a smaller version of
    fhir-invariants.sch (the contents are identical; only include those 
    resources relevant to your implementation).
  -->
  <sch:pattern>
    <sch:title>Global</sch:title>
    <sch:rule context="f:extension">
      <sch:assert test="exists(f:extension)!=exists(f:*[starts-with(local-name(.), 'value')])">ext-1: Must have either extensions or value[x], not both</sch:assert>
    </sch:rule>
    <sch:rule context="f:modifierExtension">
      <sch:assert test="exists(f:extension)!=exists(f:*[starts-with(local-name(.), 'value')])">ext-1: Must have either extensions or value[x], not both</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>Global 1</sch:title>
    <sch:rule context="f:*">
      <sch:assert test="@value|f:*|h:div">global-1: All FHIR elements must have a @value or children</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>StructureDefinition</sch:title>
    <sch:rule context="f:StructureDefinition">
      <sch:assert test="not(parent::f:contained and f:contained)">dom-2: If the resource is contained in another resource, it SHALL NOT contain nested Resources</sch:assert>
      <sch:assert test="not(exists(f:contained/*/f:meta/f:versionId)) and not(exists(f:contained/*/f:meta/f:lastUpdated))">dom-4: If a resource is contained in another resource, it SHALL NOT have a meta.versionId or a meta.lastUpdated</sch:assert>
      <sch:assert test="not(exists(for $contained in f:contained return $contained[not(parent::*/descendant::f:reference/@value=concat('#', $contained/*/id/@value) or descendant::f:reference[@value='#'])]))">dom-3: If the resource is contained in another resource, it SHALL be referred to from elsewhere in the resource or SHALL refer to the containing resource</sch:assert>
      <sch:assert test="not(exists(f:contained/*/f:meta/f:security))">dom-5: If a resource is contained in another resource, it SHALL NOT have a security label</sch:assert>
      <sch:assert test="not(exists(f:snapshot/f:element[not(contains(f:path/@value, '.')) and (f:label or f:code or f:requirements)])) and not(exists(f:differential/f:element[not(contains(f:path/@value, '.')) and (f:label or f:code or f:requirements)]))">sdf-9: In any snapshot or differential, no label, code or requirements on an element without a &quot;.&quot; in the path (e.g. the first element)</sch:assert>
      <sch:assert test="f:kind/@value='logical' or not(f:differential/f:element[1][not(contains(f:path/@value, '.'))]/f:type)">sdf-15a: If the first element in a differential has no &quot;.&quot; in the path and it's not a logical model, it has no type</sch:assert>
      <sch:assert test="not(starts-with(f:url/@value, 'http://hl7.org/fhir/StructureDefinition')) or count(f:differential/f:element/f:type/f:code[@value and not(matches(string(@value), '^[a-zA-Z0-9]+$'))]|f:snapshot/f:element/f:type/f:code[@value and not(matches(string(@value), '^[a-zA-Z0-9]+$'))]) =0">sdf-19: FHIR Specification models only use FHIR defined types</sch:assert>
      <sch:assert test="count(f:snapshot/f:element)=count(f:snapshot/f:element/@id) and (count(f:snapshot/f:element)=count(distinct-values(f:snapshot/f:element/@id)))">sdf-16: All element definitions must have unique ids (snapshot)</sch:assert>
      <sch:assert test="f:kind/@value='logical' or not(f:snapshot/f:element[1]/f:type)">sdf-15: The first element in a snapshot has no type unless model is a logical model.</sch:assert>
      <sch:assert test="not(exists(f:contextInvariant)) or (f:type/@value = 'Extension')">sdf-18: Context Invariants can only be used for extensions</sch:assert>
      <sch:assert test="count(f:differential/f:element)=count(f:differential/f:element/@id) and (count(f:differential/f:element)=count(distinct-values(f:differential/f:element/@id)))">sdf-17: All element definitions must have unique ids (diff)</sch:assert>
      <sch:assert test="count(*[self::snapshot or self::differential]/f:element[not(contains(f:path/@value, '.')) and f:sliceName])=0">sdf-23: No slice name on root</sch:assert>
      <sch:assert test="(f:kind/@value = 'logical') or not(exists(f:snapshot)) or (f:type/@value = f:snapshot/f:element[1]/f:path/@value)">sdf-11: If there's a type, its content must match the path name in the first element of a snapshot</sch:assert>
      <sch:assert test="not(starts-with(f:url/@value, 'http://hl7.org/fhir/StructureDefinition')) or (not(exists(f:snapshot/f:element/*[starts-with(local-name(), 'defaultValue')])) and not(exists(f:differential/f:element/*[starts-with(local-name(), 'defaultValue')])))">sdf-22: FHIR Specification models never have default values</sch:assert>
      <sch:assert test="count(*/f:element)=count(*/f:element/@id)">sdf-14: All element definitions must have an id</sch:assert>
      <sch:assert test="(f:derivation/@value = 'constraint') or (count(f:snapshot/f:element) = count(distinct-values(f:snapshot/f:element/f:path/@value)))">sdf-1: Element paths must be unique unless the structure is a constraint</sch:assert>
      <sch:assert test="not(exists(f:differential/f:element/*[starts-with(local-name(), 'defaultValue')])) or (f:derivation/@value = 'specialization')">sdf-21: Default values can only be specified on specializations</sch:assert>
      <sch:assert test="exists(f:snapshot) or exists(f:differential)">sdf-6: A structure must have either a differential, or a snapshot (or both)</sch:assert>
      <sch:assert test="not(f:type/@value = 'extension') or (f:derivation/@value = 'specialization') or (exists(f:context))">sdf-5: If the structure defines an extension then the structure must have context information</sch:assert>
      <sch:assert test="(f:abstract/@value=true()) or exists(f:baseDefinition)">sdf-4: If the structure is not abstract, then there SHALL be a baseDefinition</sch:assert>
    </sch:rule>
    <sch:rule context="f:StructureDefinition/f:text/h:div">
      <sch:assert test="not(descendant-or-self::*[not(local-name(.)=('a', 'abbr', 'acronym', 'b', 'big', 'blockquote', 'br', 'caption', 'cite', 'code', 'col', 'colgroup', 'dd', 'dfn', 'div', 'dl', 'dt', 'em', 'h1', 'h2', 'h3', 'h4', 'h5', 'h6', 'hr', 'i', 'img', 'li', 'ol', 'p', 'pre', 'q', 'samp', 'small', 'span', 'strong', 'sub', 'sup', 'table', 'tbody', 'td', 'tfoot', 'th', 'thead', 'tr', 'tt', 'ul', 'var'))]) and not(descendant-or-self::*/@*[not(name(.)=('abbr', 'accesskey', 'align', 'alt', 'axis', 'bgcolor', 'border', 'cellhalign', 'cellpadding', 'cellspacing', 'cellvalign', 'char', 'charoff', 'charset', 'cite', 'class', 'colspan', 'compact', 'coords', 'dir', 'frame', 'headers', 'height', 'href', 'hreflang', 'hspace', 'id', 'lang', 'longdesc', 'name', 'nowrap', 'rel', 'rev', 'rowspan', 'rules', 'scope', 'shape', 'span', 'src', 'start', 'style', 'summary', 'tabindex', 'title', 'type', 'valign', 'value', 'vspace', 'width'))])">txt-1: The narrative SHALL contain only the basic html formatting elements and attributes described in chapters 7-11 (except section 4 of chapter 9) and 15 of the HTML 4.0 standard, &lt;a&gt; elements (either name or href), images and internally contained style attributes</sch:assert>
      <sch:assert test="descendant::text()[normalize-space(.)!=''] or descendant::h:img[@src]">txt-2: The narrative SHALL have some non-whitespace content</sch:assert>
    </sch:rule>
    <sch:rule context="f:StructureDefinition/f:identifier/f:period">
      <sch:assert test="not(exists(f:start/@value)) or not(exists(f:end/@value)) or (xs:dateTime(f:start/@value) &lt;= xs:dateTime(f:end/@value))">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:StructureDefinition/f:identifier/f:assigner">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:StructureDefinition/f:contact/f:telecom">
      <sch:assert test="not(exists(f:value)) or exists(f:system)">cpt-2: A system is required if a value is provided.</sch:assert>
    </sch:rule>
    <sch:rule context="f:StructureDefinition/f:contact/f:telecom/f:period">
      <sch:assert test="not(exists(f:start/@value)) or not(exists(f:end/@value)) or (xs:dateTime(f:start/@value) &lt;= xs:dateTime(f:end/@value))">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:StructureDefinition/f:useContext/f:valueQuantity">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the unit is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:StructureDefinition/f:useContext/f:valueRange">
      <sch:assert test="not(exists(f:low/f:value/@value)) or not(exists(f:high/f:value/@value)) or (number(f:low/f:value/@value) &lt;= number(f:high/f:value/@value))">rng-2: If present, low SHALL have a lower value than high</sch:assert>
    </sch:rule>
    <sch:rule context="f:StructureDefinition/f:useContext/f:valueRange/f:low">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the unit is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:StructureDefinition/f:useContext/f:valueRange/f:high">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the unit is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:StructureDefinition/f:useContext/f:valueReference">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:StructureDefinition/f:useContext/f:valueReference/f:identifier/f:period">
      <sch:assert test="not(exists(f:start/@value)) or not(exists(f:end/@value)) or (xs:dateTime(f:start/@value) &lt;= xs:dateTime(f:end/@value))">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:StructureDefinition/f:useContext/f:valueReference/f:identifier/f:assigner">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:StructureDefinition/f:mapping">
      <sch:assert test="exists(f:uri) or exists(f:name)">sdf-2: Must have at least a name or a uri (or both)</sch:assert>
    </sch:rule>
    <sch:rule context="f:StructureDefinition/f:snapshot">
      <sch:assert test="f:element[1]/f:path/@value=parent::f:StructureDefinition/f:type/@value and count(f:element[position()!=1])=count(f:element[position()!=1][starts-with(f:path/@value, concat(ancestor::f:StructureDefinition/f:type/@value, '.'))])">sdf-8: All snapshot elements must start with the StructureDefinition's specified type for non-logical models, or with the same type name for logical models</sch:assert>
      <sch:assert test="count(f:element) = count(f:element[exists(f:definition) and exists(f:min) and exists(f:max)])">sdf-3: Each element definition in a snapshot must have a formal definition and cardinalities</sch:assert>
      <sch:assert test="count(f:element) = count(f:element/f:base)">sdf-8b: All snapshot elements must have a base definition</sch:assert>
    </sch:rule>
    <sch:rule context="f:StructureDefinition/f:snapshot/f:element">
      <sch:assert test="not(exists(f:binding)) or exists(f:binding/f:valueSet) or exists(f:binding/f:description)">sdf-10: provide either a binding reference or a description (or both)</sch:assert>
      <sch:assert test="matches(path/@value, '[^\s\.,:;\'&amp;quot;\/|?!@#$%&amp;amp;*()\[\]{}]{1,64}(\.[^\s\.,:;\'&amp;quot;\/|?!@#$%&amp;amp;*()\[\]{}]{1,64}(\[x\])?(\:[^\s\.]+)?)*')">eld-19: Element names cannot include some special characters</sch:assert>
      <sch:assert test="not(exists(f:min)) or not(exists(f:max)) or (not(f:max/@value) and not(f:min/@value)) or (f:max/@value = '*') or (number(f:max/@value) &gt;= f:min/@value)">eld-2: Min &lt;= Max</sch:assert>
      <sch:assert test="not(exists(f:contentReference) and (exists(f:type) or exists(f:*[starts-with(local-name(.), 'value')]) or exists(f:*[starts-with(local-name(.), 'defaultValue')])  or exists(f:*[starts-with(local-name(.), 'fixed')]) or exists(f:*[starts-with(local-name(.), 'pattern')]) or exists(f:*[starts-with(local-name(.), 'example')]) or exists(f:*[starts-with(local-name(.), 'f:minValue')]) or exists(f:*[starts-with(local-name(.), 'f:maxValue')]) or exists(f:maxLength) or exists(f:binding)))">eld-5: if the element definition has a contentReference, it cannot have type, defaultValue, fixed, pattern, example, minValue, maxValue, maxLength, or binding</sch:assert>
      <sch:assert test="not(exists(f:*[starts-with(local-name(.), 'pattern')])) or (count(f:type)&lt;=1)">eld-7: Pattern may only be specified if there is one type</sch:assert>
      <sch:assert test="not(exists(f:*[starts-with(local-name(.), 'fixed')])) or (count(f:type)&lt;=1)">eld-6: Fixed value may only be specified if there is one type</sch:assert>
      <sch:assert test="not(exists(f:binding)) or (count(f:type/f:code) = 0) or  f:type/f:code/@value=('code','Coding','CodeableConcept','Quantity','string', 'uri')">eld-11: Binding can only be present for coded elements, string, and uri</sch:assert>
      <sch:assert test="exists(f:sliceName) or not(exists(f:sliceIsConstraining))">eld-22: sliceIsConstraining can only appear if slicename is present</sch:assert>
      <sch:assert test="not(exists(f:*[starts-with(local-name(.), 'pattern')])) or not(exists(f:*[starts-with(local-name(.), 'fixed')]))">eld-8: Pattern and value are mutually exclusive</sch:assert>
      <sch:assert test="count(f:constraint) = count(distinct-values(f:constraint/f:key/@value))">eld-14: Constraints must be unique by key</sch:assert>
      <sch:assert test="not(exists(for $type in f:type return $type/preceding-sibling::f:type[f:code/@value=$type/f:code/@value]))">eld-13: Types must be unique by code</sch:assert>
      <sch:assert test="not(exists(f:sliceName/@value)) or matches(f:sliceName/@value, '^[a-zA-Z0-9\/\-_\[\]\@]+$')">eld-16: sliceName must be composed of proper tokens separated by &quot;/&quot;</sch:assert>
      <sch:assert test="not(exists(f:*[starts-with(local-name(.), 'fixed')])) or not(exists(f:meaningWhenMissing))">eld-15: default value and meaningWhenMissing are mutually exclusive</sch:assert>
      <sch:assert test="not(f:isModifier/@value = 'true') or exists(f:isModifierReason)">eld-18: Must have a modifier reason if isModifier = true</sch:assert>
    </sch:rule>
    <sch:rule context="f:StructureDefinition/f:snapshot/f:element/f:slicing">
      <sch:assert test="(f:discriminator) or (f:description)">eld-1: If there are no discriminators, there must be a definition</sch:assert>
    </sch:rule>
    <sch:rule context="f:StructureDefinition/f:snapshot/f:element/f:max">
      <sch:assert test="@value='*' or (normalize-space(@value)!='' and normalize-space(translate(@value, '0123456789',''))='')">eld-3: Max SHALL be a number or &quot;*&quot;</sch:assert>
    </sch:rule>
    <sch:rule context="f:StructureDefinition/f:snapshot/f:element/f:type">
      <sch:assert test="not(exists(f:aggregation)) or exists(f:code[@value = 'Reference'])">eld-4: Aggregation may only be specified if one of the allowed types for the element is a reference</sch:assert>
      <sch:assert test="not(exists(f:targetProfile)) or (f:code/@value = 'Reference')">eld-17: targetProfile is only allowed if the type is Reference or canonical</sch:assert>
    </sch:rule>
    <sch:rule context="f:StructureDefinition/f:snapshot/f:element/f:minValueQuantity">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the unit is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:StructureDefinition/f:snapshot/f:element/f:maxValueQuantity">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the unit is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:StructureDefinition/f:snapshot/f:element/f:binding">
      <sch:assert test="(starts-with(string(f:valueSet/@value), 'http:') or starts-with(string(f:valueSet/@value), 'https:') or starts-with(string(f:valueSet/@value), 'urn:'))">eld-12: ValueSet SHALL start with http:// or https:// or urn:</sch:assert>
    </sch:rule>
    <sch:rule context="f:StructureDefinition/f:differential">
      <sch:assert test="not(f:element[1]/f:slicing)">sdf-20: No slicing on the root element</sch:assert>
      <sch:assert test="count(f:element)=count(f:element[f:path/@value=ancestor::f:StructureDefinition/f:type/@value or starts-with(f:path/@value, concat(ancestor::f:StructureDefinition/f:type/@value, '.'))])">sdf-8a: In any differential, all the elements must start with the StructureDefinition's specified type for non-logical models, or with the same type name for logical models</sch:assert>
    </sch:rule>
    <sch:rule context="f:StructureDefinition/f:differential/f:element">
      <sch:assert test="matches(path/@value, '[^\s\.,:;\'&amp;quot;\/|?!@#$%&amp;amp;*()\[\]{}]{1,64}(\.[^\s\.,:;\'&amp;quot;\/|?!@#$%&amp;amp;*()\[\]{}]{1,64}(\[x\])?(\:[^\s\.]+)?)*')">eld-19: Element names cannot include some special characters</sch:assert>
      <sch:assert test="not(exists(f:min)) or not(exists(f:max)) or (not(f:max/@value) and not(f:min/@value)) or (f:max/@value = '*') or (number(f:max/@value) &gt;= f:min/@value)">eld-2: Min &lt;= Max</sch:assert>
      <sch:assert test="not(exists(f:contentReference) and (exists(f:type) or exists(f:*[starts-with(local-name(.), 'value')]) or exists(f:*[starts-with(local-name(.), 'defaultValue')])  or exists(f:*[starts-with(local-name(.), 'fixed')]) or exists(f:*[starts-with(local-name(.), 'pattern')]) or exists(f:*[starts-with(local-name(.), 'example')]) or exists(f:*[starts-with(local-name(.), 'f:minValue')]) or exists(f:*[starts-with(local-name(.), 'f:maxValue')]) or exists(f:maxLength) or exists(f:binding)))">eld-5: if the element definition has a contentReference, it cannot have type, defaultValue, fixed, pattern, example, minValue, maxValue, maxLength, or binding</sch:assert>
      <sch:assert test="not(exists(f:*[starts-with(local-name(.), 'pattern')])) or (count(f:type)&lt;=1)">eld-7: Pattern may only be specified if there is one type</sch:assert>
      <sch:assert test="not(exists(f:*[starts-with(local-name(.), 'fixed')])) or (count(f:type)&lt;=1)">eld-6: Fixed value may only be specified if there is one type</sch:assert>
      <sch:assert test="not(exists(f:binding)) or (count(f:type/f:code) = 0) or  f:type/f:code/@value=('code','Coding','CodeableConcept','Quantity','string', 'uri')">eld-11: Binding can only be present for coded elements, string, and uri</sch:assert>
      <sch:assert test="exists(f:sliceName) or not(exists(f:sliceIsConstraining))">eld-22: sliceIsConstraining can only appear if slicename is present</sch:assert>
      <sch:assert test="not(exists(f:*[starts-with(local-name(.), 'pattern')])) or not(exists(f:*[starts-with(local-name(.), 'fixed')]))">eld-8: Pattern and value are mutually exclusive</sch:assert>
      <sch:assert test="count(f:constraint) = count(distinct-values(f:constraint/f:key/@value))">eld-14: Constraints must be unique by key</sch:assert>
      <sch:assert test="not(exists(for $type in f:type return $type/preceding-sibling::f:type[f:code/@value=$type/f:code/@value]))">eld-13: Types must be unique by code</sch:assert>
      <sch:assert test="not(exists(f:sliceName/@value)) or matches(f:sliceName/@value, '^[a-zA-Z0-9\/\-_\[\]\@]+$')">eld-16: sliceName must be composed of proper tokens separated by &quot;/&quot;</sch:assert>
      <sch:assert test="not(exists(f:*[starts-with(local-name(.), 'fixed')])) or not(exists(f:meaningWhenMissing))">eld-15: default value and meaningWhenMissing are mutually exclusive</sch:assert>
      <sch:assert test="not(f:isModifier/@value = 'true') or exists(f:isModifierReason)">eld-18: Must have a modifier reason if isModifier = true</sch:assert>
    </sch:rule>
    <sch:rule context="f:StructureDefinition/f:differential/f:element/f:slicing">
      <sch:assert test="(f:discriminator) or (f:description)">eld-1: If there are no discriminators, there must be a definition</sch:assert>
    </sch:rule>
    <sch:rule context="f:StructureDefinition/f:differential/f:element/f:max">
      <sch:assert test="@value='*' or (normalize-space(@value)!='' and normalize-space(translate(@value, '0123456789',''))='')">eld-3: Max SHALL be a number or &quot;*&quot;</sch:assert>
    </sch:rule>
    <sch:rule context="f:StructureDefinition/f:differential/f:element/f:type">
      <sch:assert test="not(exists(f:aggregation)) or exists(f:code[@value = 'Reference'])">eld-4: Aggregation may only be specified if one of the allowed types for the element is a reference</sch:assert>
      <sch:assert test="not(exists(f:targetProfile)) or (f:code/@value = 'Reference')">eld-17: targetProfile is only allowed if the type is Reference or canonical</sch:assert>
    </sch:rule>
    <sch:rule context="f:StructureDefinition/f:differential/f:element/f:minValueQuantity">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the unit is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:StructureDefinition/f:differential/f:element/f:maxValueQuantity">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the unit is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:StructureDefinition/f:differential/f:element/f:binding">
      <sch:assert test="(starts-with(string(f:valueSet/@value), 'http:') or starts-with(string(f:valueSet/@value), 'https:') or starts-with(string(f:valueSet/@value), 'urn:'))">eld-12: ValueSet SHALL start with http:// or https:// or urn:</sch:assert>
    </sch:rule>
  </sch:pattern>
</sch:schema>
