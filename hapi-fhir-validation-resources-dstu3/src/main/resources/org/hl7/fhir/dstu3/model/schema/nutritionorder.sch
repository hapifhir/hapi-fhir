<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
  <sch:ns prefix="f" uri="http://hl7.org/fhir"/>
  <sch:ns prefix="h" uri="http://www.w3.org/1999/xhtml"/>
  <!-- 
    This file contains just the constraints for the resource NutritionOrder
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
    <sch:title>NutritionOrder</sch:title>
    <sch:rule context="f:NutritionOrder">
      <sch:assert test="not(parent::f:contained and f:contained)">dom-2: If the resource is contained in another resource, it SHALL NOT contain nested Resources</sch:assert>
      <sch:assert test="not(parent::f:contained and f:text)">dom-1: If the resource is contained in another resource, it SHALL NOT contain any narrative</sch:assert>
      <sch:assert test="not(exists(f:contained/*/f:meta/f:versionId)) and not(exists(f:contained/*/f:meta/f:lastUpdated))">dom-4: If a resource is contained in another resource, it SHALL NOT have a meta.versionId or a meta.lastUpdated</sch:assert>
      <sch:assert test="not(exists(for $id in f:contained/*/@id return $id[not(ancestor::f:contained/parent::*/descendant::f:reference/@value=concat('#', $id))]))">dom-3: If the resource is contained in another resource, it SHALL be referred to from elsewhere in the resource</sch:assert>
      <sch:assert test="exists(f:oralDiet) or exists(f:supplement) or exists(f:enteralFormula)">nor-1: Nutrition Order SHALL contain either Oral Diet , Supplement, or Enteral Formula class</sch:assert>
    </sch:rule>
    <sch:rule context="f:NutritionOrder/f:text/h:div">
      <sch:assert test="not(descendant-or-self::*[not(local-name(.)=('a', 'abbr', 'acronym', 'b', 'big', 'blockquote', 'br', 'caption', 'cite', 'code', 'col', 'colgroup', 'dd', 'dfn', 'div', 'dl', 'dt', 'em', 'h1', 'h2', 'h3', 'h4', 'h5', 'h6', 'hr', 'i', 'img', 'li', 'ol', 'p', 'pre', 'q', 'samp', 'small', 'span', 'strong', 'sub', 'sup', 'table', 'tbody', 'td', 'tfoot', 'th', 'thead', 'tr', 'tt', 'ul', 'var'))]) and not(descendant-or-self::*/@*[not(name(.)=('abbr', 'accesskey', 'align', 'alt', 'axis', 'bgcolor', 'border', 'cellhalign', 'cellpadding', 'cellspacing', 'cellvalign', 'char', 'charoff', 'charset', 'cite', 'class', 'colspan', 'compact', 'coords', 'dir', 'frame', 'headers', 'height', 'href', 'hreflang', 'hspace', 'id', 'lang', 'longdesc', 'name', 'nowrap', 'rel', 'rev', 'rowspan', 'rules', 'scope', 'shape', 'span', 'src', 'start', 'style', 'summary', 'tabindex', 'title', 'type', 'valign', 'value', 'vspace', 'width'))])">txt-1: The narrative SHALL contain only the basic html formatting elements and attributes described in chapters 7-11 (except section 4 of chapter 9) and 15 of the HTML 4.0 standard, &lt;a&gt; elements (either name or href), images and internally contained style attributes</sch:assert>
      <sch:assert test="descendant::text()[normalize-space(.)!=''] or descendant::h:img[@src]">txt-2: The narrative SHALL have some non-whitespace content</sch:assert>
    </sch:rule>
    <sch:rule context="f:NutritionOrder/f:identifier/f:period">
      <sch:assert test="not(exists(f:start)) or not(exists(f:end)) or (f:start/@value &lt;= f:end/@value)">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:NutritionOrder/f:identifier/f:assigner">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:NutritionOrder/f:patient">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:NutritionOrder/f:patient/f:identifier/f:period">
      <sch:assert test="not(exists(f:start)) or not(exists(f:end)) or (f:start/@value &lt;= f:end/@value)">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:NutritionOrder/f:patient/f:identifier/f:assigner">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:NutritionOrder/f:encounter">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:NutritionOrder/f:encounter/f:identifier/f:period">
      <sch:assert test="not(exists(f:start)) or not(exists(f:end)) or (f:start/@value &lt;= f:end/@value)">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:NutritionOrder/f:encounter/f:identifier/f:assigner">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:NutritionOrder/f:orderer">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:NutritionOrder/f:orderer/f:identifier/f:period">
      <sch:assert test="not(exists(f:start)) or not(exists(f:end)) or (f:start/@value &lt;= f:end/@value)">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:NutritionOrder/f:orderer/f:identifier/f:assigner">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:NutritionOrder/f:allergyIntolerance">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:NutritionOrder/f:allergyIntolerance/f:identifier/f:period">
      <sch:assert test="not(exists(f:start)) or not(exists(f:end)) or (f:start/@value &lt;= f:end/@value)">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:NutritionOrder/f:allergyIntolerance/f:identifier/f:assigner">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:NutritionOrder/f:oralDiet/f:schedule/f:repeat">
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
    <sch:rule context="f:NutritionOrder/f:oralDiet/f:schedule/f:repeat/f:boundsDuration">
      <sch:assert test="(f:code or not(f:value)) and (not(exists(f:system)) or f:system/@value='http://unitsofmeasure.org')">drt-1: There SHALL be a code if there is a value and it SHALL be an expression of time.  If system is present, it SHALL be UCUM.</sch:assert>
    </sch:rule>
    <sch:rule context="f:NutritionOrder/f:oralDiet/f:schedule/f:repeat/f:boundsRange">
      <sch:assert test="not(exists(f:low/f:value/@value)) or not(exists(f:high/f:value/@value)) or (number(f:low/f:value/@value) &lt;= number(f:high/f:value/@value))">rng-2: If present, low SHALL have a lower value than high</sch:assert>
    </sch:rule>
    <sch:rule context="f:NutritionOrder/f:oralDiet/f:schedule/f:repeat/f:boundsRange/f:low">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the unit is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:NutritionOrder/f:oralDiet/f:schedule/f:repeat/f:boundsRange/f:high">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the unit is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:NutritionOrder/f:oralDiet/f:schedule/f:repeat/f:boundsPeriod">
      <sch:assert test="not(exists(f:start)) or not(exists(f:end)) or (f:start/@value &lt;= f:end/@value)">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:NutritionOrder/f:oralDiet/f:nutrient/f:amount">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the unit is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:NutritionOrder/f:supplement/f:schedule/f:repeat">
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
    <sch:rule context="f:NutritionOrder/f:supplement/f:schedule/f:repeat/f:boundsDuration">
      <sch:assert test="(f:code or not(f:value)) and (not(exists(f:system)) or f:system/@value='http://unitsofmeasure.org')">drt-1: There SHALL be a code if there is a value and it SHALL be an expression of time.  If system is present, it SHALL be UCUM.</sch:assert>
    </sch:rule>
    <sch:rule context="f:NutritionOrder/f:supplement/f:schedule/f:repeat/f:boundsRange">
      <sch:assert test="not(exists(f:low/f:value/@value)) or not(exists(f:high/f:value/@value)) or (number(f:low/f:value/@value) &lt;= number(f:high/f:value/@value))">rng-2: If present, low SHALL have a lower value than high</sch:assert>
    </sch:rule>
    <sch:rule context="f:NutritionOrder/f:supplement/f:schedule/f:repeat/f:boundsRange/f:low">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the unit is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:NutritionOrder/f:supplement/f:schedule/f:repeat/f:boundsRange/f:high">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the unit is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:NutritionOrder/f:supplement/f:schedule/f:repeat/f:boundsPeriod">
      <sch:assert test="not(exists(f:start)) or not(exists(f:end)) or (f:start/@value &lt;= f:end/@value)">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:NutritionOrder/f:supplement/f:quantity">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the unit is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:NutritionOrder/f:enteralFormula/f:caloricDensity">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the unit is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:NutritionOrder/f:enteralFormula/f:administration/f:schedule/f:repeat">
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
    <sch:rule context="f:NutritionOrder/f:enteralFormula/f:administration/f:schedule/f:repeat/f:boundsDuration">
      <sch:assert test="(f:code or not(f:value)) and (not(exists(f:system)) or f:system/@value='http://unitsofmeasure.org')">drt-1: There SHALL be a code if there is a value and it SHALL be an expression of time.  If system is present, it SHALL be UCUM.</sch:assert>
    </sch:rule>
    <sch:rule context="f:NutritionOrder/f:enteralFormula/f:administration/f:schedule/f:repeat/f:boundsRange">
      <sch:assert test="not(exists(f:low/f:value/@value)) or not(exists(f:high/f:value/@value)) or (number(f:low/f:value/@value) &lt;= number(f:high/f:value/@value))">rng-2: If present, low SHALL have a lower value than high</sch:assert>
    </sch:rule>
    <sch:rule context="f:NutritionOrder/f:enteralFormula/f:administration/f:schedule/f:repeat/f:boundsRange/f:low">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the unit is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:NutritionOrder/f:enteralFormula/f:administration/f:schedule/f:repeat/f:boundsRange/f:high">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the unit is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:NutritionOrder/f:enteralFormula/f:administration/f:schedule/f:repeat/f:boundsPeriod">
      <sch:assert test="not(exists(f:start)) or not(exists(f:end)) or (f:start/@value &lt;= f:end/@value)">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:NutritionOrder/f:enteralFormula/f:administration/f:quantity">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the unit is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:NutritionOrder/f:enteralFormula/f:administration/f:rateQuantity">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the unit is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:NutritionOrder/f:enteralFormula/f:administration/f:rateRatio">
      <sch:assert test="(count(f:numerator) = count(f:denominator)) and ((count(f:numerator) &gt; 0) or (count(f:extension) &gt; 0))">rat-1: Numerator and denominator SHALL both be present, or both are absent. If both are absent, there SHALL be some extension present</sch:assert>
    </sch:rule>
    <sch:rule context="f:NutritionOrder/f:enteralFormula/f:administration/f:rateRatio/f:numerator">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the unit is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:NutritionOrder/f:enteralFormula/f:administration/f:rateRatio/f:denominator">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the unit is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:NutritionOrder/f:enteralFormula/f:maxVolumeToDeliver">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the unit is present, the system SHALL also be present</sch:assert>
    </sch:rule>
  </sch:pattern>
</sch:schema>
