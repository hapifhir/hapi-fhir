<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
  <sch:ns prefix="f" uri="http://hl7.org/fhir"/>
  <sch:ns prefix="h" uri="http://www.w3.org/1999/xhtml"/>
  <!-- 
    This file contains just the constraints for the resource Observation
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
    <sch:title>Observation</sch:title>
    <sch:rule context="f:Observation">
      <sch:assert test="not(parent::f:contained and f:contained)">dom-2: If the resource is contained in another resource, it SHALL NOT contain nested Resources</sch:assert>
      <sch:assert test="not(parent::f:contained and f:text)">dom-1: If the resource is contained in another resource, it SHALL NOT contain any narrative</sch:assert>
      <sch:assert test="not(exists(f:contained/*/f:meta/f:versionId)) and not(exists(f:contained/*/f:meta/f:lastUpdated))">dom-4: If a resource is contained in another resource, it SHALL NOT have a meta.versionId or a meta.lastUpdated</sch:assert>
      <sch:assert test="not(exists(for $contained in f:contained return $contained[not(parent::*/descendant::f:reference/@value=concat('#', $contained/*/id/@value) or descendant::f:reference[@value='#'])]))">dom-3: If the resource is contained in another resource, it SHALL be referred to from elsewhere in the resource or SHALL refer to the containing resource</sch:assert>
      <sch:assert test="not(f:*[starts-with(local-name(.), 'value')] and (for $coding in f:code/f:coding return f:component/f:code/f:coding[f:code/@value=$coding/f:code/@value] [f:system/@value=$coding/f:system/@value]))">obs-7: If Observation.code is the same as a Observation.component.code then the value element associated with the code SHALL NOT be present</sch:assert>
      <sch:assert test="not(exists(f:dataAbsentReason)) or (not(exists(*[starts-with(local-name(.), 'value')])))">obs-6: dataAbsentReason SHALL only be present if Observation.value[x] is not present</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:text/h:div">
      <sch:assert test="not(descendant-or-self::*[not(local-name(.)=('a', 'abbr', 'acronym', 'b', 'big', 'blockquote', 'br', 'caption', 'cite', 'code', 'col', 'colgroup', 'dd', 'dfn', 'div', 'dl', 'dt', 'em', 'h1', 'h2', 'h3', 'h4', 'h5', 'h6', 'hr', 'i', 'img', 'li', 'ol', 'p', 'pre', 'q', 'samp', 'small', 'span', 'strong', 'sub', 'sup', 'table', 'tbody', 'td', 'tfoot', 'th', 'thead', 'tr', 'tt', 'ul', 'var'))]) and not(descendant-or-self::*/@*[not(name(.)=('abbr', 'accesskey', 'align', 'alt', 'axis', 'bgcolor', 'border', 'cellhalign', 'cellpadding', 'cellspacing', 'cellvalign', 'char', 'charoff', 'charset', 'cite', 'class', 'colspan', 'compact', 'coords', 'dir', 'frame', 'headers', 'height', 'href', 'hreflang', 'hspace', 'id', 'lang', 'longdesc', 'name', 'nowrap', 'rel', 'rev', 'rowspan', 'rules', 'scope', 'shape', 'span', 'src', 'start', 'style', 'summary', 'tabindex', 'title', 'type', 'valign', 'value', 'vspace', 'width'))])">txt-1: The narrative SHALL contain only the basic html formatting elements and attributes described in chapters 7-11 (except section 4 of chapter 9) and 15 of the HTML 4.0 standard, &lt;a&gt; elements (either name or href), images and internally contained style attributes</sch:assert>
      <sch:assert test="descendant::text()[normalize-space(.)!=''] or descendant::h:img[@src]">txt-2: The narrative SHALL have some non-whitespace content</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:identifier/f:period">
      <sch:assert test="not(exists(f:start/@value)) or not(exists(f:end/@value)) or (xs:dateTime(f:start/@value) &lt;= xs:dateTime(f:end/@value))">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:identifier/f:assigner">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:basedOn">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:basedOn/f:identifier/f:period">
      <sch:assert test="not(exists(f:start/@value)) or not(exists(f:end/@value)) or (xs:dateTime(f:start/@value) &lt;= xs:dateTime(f:end/@value))">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:basedOn/f:identifier/f:assigner">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:partOf">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:partOf/f:identifier/f:period">
      <sch:assert test="not(exists(f:start/@value)) or not(exists(f:end/@value)) or (xs:dateTime(f:start/@value) &lt;= xs:dateTime(f:end/@value))">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:partOf/f:identifier/f:assigner">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:subject">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:subject/f:identifier/f:period">
      <sch:assert test="not(exists(f:start/@value)) or not(exists(f:end/@value)) or (xs:dateTime(f:start/@value) &lt;= xs:dateTime(f:end/@value))">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:subject/f:identifier/f:assigner">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:focus">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:focus/f:identifier/f:period">
      <sch:assert test="not(exists(f:start/@value)) or not(exists(f:end/@value)) or (xs:dateTime(f:start/@value) &lt;= xs:dateTime(f:end/@value))">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:focus/f:identifier/f:assigner">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:context">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:context/f:identifier/f:period">
      <sch:assert test="not(exists(f:start/@value)) or not(exists(f:end/@value)) or (xs:dateTime(f:start/@value) &lt;= xs:dateTime(f:end/@value))">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:context/f:identifier/f:assigner">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:effectivePeriod">
      <sch:assert test="not(exists(f:start/@value)) or not(exists(f:end/@value)) or (xs:dateTime(f:start/@value) &lt;= xs:dateTime(f:end/@value))">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:effectiveTiming/f:repeat">
      <sch:assert test="not(exists(f:offset)) or exists(f:when)">tim-9: If there's an offset, there must be a when (and not C, CM, CD, CV)</sch:assert>
      <sch:assert test="f:period/@value &gt;= 0 or not(f:period/@value)">tim-5: period SHALL be a non-negative value</sch:assert>
      <sch:assert test="not(exists(f:periodMax)) or exists(f:period)">tim-6: If there's a periodMax, there must be a period</sch:assert>
      <sch:assert test="not(exists(f:durationMax)) or exists(f:duration)">tim-7: If there's a durationMax, there must be a duration</sch:assert>
      <sch:assert test="not(exists(f:countMax)) or exists(f:count)">tim-8: If there's a countMax, there must be a count</sch:assert>
      <sch:assert test="not(exists(f:duration)) or exists(f:durationUnit)">tim-1: if there's a duration, there needs to be duration units</sch:assert>
      <sch:assert test="not(exists(f:timeOfDay)) or not(exists(f:when))">tim-10: If there's a timeOfDay, there cannot be be a when, or vice versa</sch:assert>
      <sch:assert test="not(exists(f:period)) or exists(f:periodUnit)">tim-2: if there's a period, there needs to be period units</sch:assert>
      <sch:assert test="f:duration/@value &gt;= 0 or not(f:duration/@value)">tim-4: duration SHALL be a non-negative value</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:effectiveTiming/f:repeat/f:boundsDuration">
      <sch:assert test="(f:code or not(f:value)) and (not(exists(f:system)) or f:system/@value='http://unitsofmeasure.org')">drt-1: There SHALL be a code if there is a value and it SHALL be an expression of time.  If system is present, it SHALL be UCUM.</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:effectiveTiming/f:repeat/f:boundsRange">
      <sch:assert test="not(exists(f:low/f:value/@value)) or not(exists(f:high/f:value/@value)) or (number(f:low/f:value/@value) &lt;= number(f:high/f:value/@value))">rng-2: If present, low SHALL have a lower value than high</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:effectiveTiming/f:repeat/f:boundsRange/f:low">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the unit is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:effectiveTiming/f:repeat/f:boundsRange/f:high">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the unit is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:effectiveTiming/f:repeat/f:boundsPeriod">
      <sch:assert test="not(exists(f:start/@value)) or not(exists(f:end/@value)) or (xs:dateTime(f:start/@value) &lt;= xs:dateTime(f:end/@value))">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:performer">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:performer/f:identifier/f:period">
      <sch:assert test="not(exists(f:start/@value)) or not(exists(f:end/@value)) or (xs:dateTime(f:start/@value) &lt;= xs:dateTime(f:end/@value))">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:performer/f:identifier/f:assigner">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:valueQuantity">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the unit is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:valueRange">
      <sch:assert test="not(exists(f:low/f:value/@value)) or not(exists(f:high/f:value/@value)) or (number(f:low/f:value/@value) &lt;= number(f:high/f:value/@value))">rng-2: If present, low SHALL have a lower value than high</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:valueRange/f:low">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the unit is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:valueRange/f:high">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the unit is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:valueRatio">
      <sch:assert test="(count(f:numerator) = count(f:denominator)) and ((count(f:numerator) &gt; 0) or (count(f:extension) &gt; 0))">rat-1: Numerator and denominator SHALL both be present, or both are absent. If both are absent, there SHALL be some extension present</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:valueRatio/f:numerator">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the unit is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:valueRatio/f:denominator">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the unit is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:valueSampledData/f:origin">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the unit is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:valuePeriod">
      <sch:assert test="not(exists(f:start/@value)) or not(exists(f:end/@value)) or (xs:dateTime(f:start/@value) &lt;= xs:dateTime(f:end/@value))">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:specimen">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:specimen/f:identifier/f:period">
      <sch:assert test="not(exists(f:start/@value)) or not(exists(f:end/@value)) or (xs:dateTime(f:start/@value) &lt;= xs:dateTime(f:end/@value))">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:specimen/f:identifier/f:assigner">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:device">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:device/f:identifier/f:period">
      <sch:assert test="not(exists(f:start/@value)) or not(exists(f:end/@value)) or (xs:dateTime(f:start/@value) &lt;= xs:dateTime(f:end/@value))">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:device/f:identifier/f:assigner">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:referenceRange">
      <sch:assert test="(exists(f:low) or exists(f:high)or exists(f:text))">obs-3: Must have at least a low or a high or text</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:referenceRange/f:low">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the unit is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:referenceRange/f:high">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the unit is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:referenceRange/f:age">
      <sch:assert test="not(exists(f:low/f:value/@value)) or not(exists(f:high/f:value/@value)) or (number(f:low/f:value/@value) &lt;= number(f:high/f:value/@value))">rng-2: If present, low SHALL have a lower value than high</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:referenceRange/f:age/f:low">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the unit is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:referenceRange/f:age/f:high">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the unit is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:hasMember">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:hasMember/f:identifier/f:period">
      <sch:assert test="not(exists(f:start/@value)) or not(exists(f:end/@value)) or (xs:dateTime(f:start/@value) &lt;= xs:dateTime(f:end/@value))">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:hasMember/f:identifier/f:assigner">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:derivedFrom">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:derivedFrom/f:identifier/f:period">
      <sch:assert test="not(exists(f:start/@value)) or not(exists(f:end/@value)) or (xs:dateTime(f:start/@value) &lt;= xs:dateTime(f:end/@value))">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:derivedFrom/f:identifier/f:assigner">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:component/f:valueQuantity">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the unit is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:component/f:valueRange">
      <sch:assert test="not(exists(f:low/f:value/@value)) or not(exists(f:high/f:value/@value)) or (number(f:low/f:value/@value) &lt;= number(f:high/f:value/@value))">rng-2: If present, low SHALL have a lower value than high</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:component/f:valueRange/f:low">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the unit is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:component/f:valueRange/f:high">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the unit is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:component/f:valueRatio">
      <sch:assert test="(count(f:numerator) = count(f:denominator)) and ((count(f:numerator) &gt; 0) or (count(f:extension) &gt; 0))">rat-1: Numerator and denominator SHALL both be present, or both are absent. If both are absent, there SHALL be some extension present</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:component/f:valueRatio/f:numerator">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the unit is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:component/f:valueRatio/f:denominator">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the unit is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:component/f:valueSampledData/f:origin">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the unit is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:Observation/f:component/f:valuePeriod">
      <sch:assert test="not(exists(f:start/@value)) or not(exists(f:end/@value)) or (xs:dateTime(f:start/@value) &lt;= xs:dateTime(f:end/@value))">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
  </sch:pattern>
</sch:schema>
