<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
  <sch:ns prefix="f" uri="http://hl7.org/fhir"/>
  <sch:ns prefix="h" uri="http://www.w3.org/1999/xhtml"/>
  <!-- 
    This file contains just the constraints for the resource TestScript
    It is provided for documentation purposes. When actually validating,
    always use fhir-invariants.sch (because of the way containment works)
    Alternatively you can use this file to build a smaller version of
    fhir-invariants.sch (the contents are identical; only include those 
    resources relevant to your implementation).
  -->
  <sch:pattern>
    <sch:title>Global</sch:title>
    <sch:rule context="//f:*">
      <sch:assert test="@value|f:*|h:div">global-1: All FHIR elements must have a @value or children</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>TestScript</sch:title>
    <sch:rule context="//f:TestScript">
      <sch:assert test="not(parent::f:contained and f:contained)">dom-2: If the resource is contained in another resource, it SHALL NOT contain nested Resources</sch:assert>
      <sch:assert test="not(parent::f:contained and f:text)">dom-1: If the resource is contained in another resource, it SHALL NOT contain any narrative</sch:assert>
      <sch:assert test="not(exists(f:contained/f:meta/f:versionId)) and not(exists(f:contained/f:meta/f:lastUpdated))">dom-4: If a resource is contained in another resource, it SHALL NOT have a meta.versionId or a meta.lastUpdated</sch:assert>
      <sch:assert test="not(exists(for $id in f:contained/*/@id return $id[not(ancestor::f:contained/parent::*/descendant::f:reference/@value=concat('#', $id))]))">dom-3: If the resource is contained in another resource, it SHALL be referred to from elsewhere in the resource</sch:assert>
      <sch:assert test="not(f:assert)">inv-16: Assertions SHALL be present in TestScript.setup.action and TestScript.test.action only.</sch:assert>
      <sch:assert test="not(f:operation)">inv-15: Operations SHALL be present in TestScript.setup.action, TestScript.test.action and TestScript.teardown.action only.</sch:assert>
    </sch:rule>
    <sch:rule context="//f:TestScript/f:text/f:div">
      <sch:assert test="not(descendant-or-self::*/@*[not(name(.)=('abbr', 'accesskey', 'align', 'alt', 'axis', 'bgcolor', 'border', 'cellhalign', 'cellpadding', 'cellspacing', 'cellvalign', 'char', 'charoff', 'charset', 'cite', 'class', 'colspan', 'compact', 'coords', 'dir', 'frame', 'headers', 'height', 'href', 'hreflang', 'hspace', 'id', 'lang', 'longdesc', 'name', 'nowrap', 'rel', 'rev', 'rowspan', 'rules', 'scope', 'shape', 'span', 'src', 'start', 'style', 'summary', 'tabindex', 'title', 'type', 'valign', 'value', 'vspace', 'width'))])">txt-3: The narrative SHALL contain only the basic html formatting attributes described in chapters 7-11 (except section 4 of chapter 9) and 15 of the HTML 4.0 standard, &lt;a&gt; elements (either name or href), images and internally contained style attributes</sch:assert>
      <sch:assert test="not(descendant-or-self::*[not(local-name(.)=('a', 'abbr', 'acronym', 'b', 'big', 'blockquote', 'br', 'caption', 'cite', 'code', 'colgroup', 'dd', 'dfn', 'div', 'dl', 'dt', 'em', 'h1', 'h2', 'h3', 'h4', 'h5', 'h6', 'hr', 'i', 'img', 'li', 'ol', 'p', 'pre', 'q', 'samp', 'small', 'span', 'strong', 'table', 'tbody', 'td', 'tfoot', 'th', 'thead', 'tr', 'tt', 'ul', 'var'))])">txt-1: The narrative SHALL contain only the basic html formatting elements described in chapters 7-11 (except section 4 of chapter 9) and 15 of the HTML 4.0 standard, &lt;a&gt; elements (either name or href), images and internally contained style attributes</sch:assert>
      <sch:assert test="descendant::text()[normalize-space(.)!=''] or descendant::h:img[@src]">txt-2: The narrative SHALL have some non-whitespace content</sch:assert>
    </sch:rule>
    <sch:rule context="//f:TestScript/f:identifier/f:period">
      <sch:assert test="not(exists(f:start)) or not(exists(f:end)) or (f:start/@value &lt;= f:end/@value)">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="//f:TestScript/f:identifier/f:assigner">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a local reference if the resource is provided inline</sch:assert>
    </sch:rule>
    <sch:rule context="//f:TestScript/f:contact/f:telecom">
      <sch:assert test="not(exists(f:value)) or exists(f:system)">cpt-2: A system is required if a value is provided.</sch:assert>
    </sch:rule>
    <sch:rule context="//f:TestScript/f:contact/f:telecom/f:period">
      <sch:assert test="not(exists(f:start)) or not(exists(f:end)) or (f:start/@value &lt;= f:end/@value)">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="//f:TestScript/f:metadata">
      <sch:assert test="f:capability/f:required or f:capability/f:validated or (f:capability/f:required and f:capability/f:validated)">inv-5: TestScript metadata capability SHALL contain required or validated or both.</sch:assert>
    </sch:rule>
    <sch:rule context="//f:TestScript/f:metadata/f:capability/f:conformance">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a local reference if the resource is provided inline</sch:assert>
    </sch:rule>
    <sch:rule context="//f:TestScript/f:fixture/f:resource">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a local reference if the resource is provided inline</sch:assert>
    </sch:rule>
    <sch:rule context="//f:TestScript/f:profile">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a local reference if the resource is provided inline</sch:assert>
    </sch:rule>
    <sch:rule context="//f:TestScript/f:variable">
      <sch:assert test="not(f:headerField and f:path)">inv-4: Variable cannot contain both headerField and path.</sch:assert>
    </sch:rule>
    <sch:rule context="//f:TestScript/f:setup/f:metadata">
      <sch:assert test="f:capability/f:required or f:capability/f:validated or (f:capability/f:required and f:capability/f:validated)">inv-6: Setup metadata capability SHALL contain required or validated or both.</sch:assert>
    </sch:rule>
    <sch:rule context="//f:TestScript/f:setup/f:action">
      <sch:assert test="(f:operation or f:assert) and not(f:operation and f:assert)">inv-1: Setup action SHALL contain either an operation or assert but not both.</sch:assert>
    </sch:rule>
    <sch:rule context="//f:TestScript/f:setup/f:action/f:operation">
      <sch:assert test="f:sourceId or ((f:targetId or f:url or f:params) and (count(f:targetId) + count(f:url) + count(f:params) =1)) or (f:type/f:code/@value='conformance' or f:type/f:code/@value='search' or f:type/f:code/@value='transaction' or f:type/f:code/@value='history')">inv-10: Setup operation SHALL contain either sourceId or targetId or params or url.</sch:assert>
    </sch:rule>
    <sch:rule context="//f:TestScript/f:setup/f:action/f:assert">
      <sch:assert test="count(f:contentType) + count(f:headerField) + count(f:minimumId) + count(f:navigationLinks) + count(f:path) + count(f:resource) + count(f:responseCode) + count(f:response) + count(f:validateProfileId)  &lt;=1">inv-8: Only a single assertion SHALL be present within setup action assert element.</sch:assert>
      <sch:assert test="(f:compareToSourceId and f:compareToSourcePath) or not(f:compareToSourceId or f:compareToSourcePath)">inv-13: Setup action assert shall contain both compareToSourceId and compareToSourcePath or neither.</sch:assert>
    </sch:rule>
    <sch:rule context="//f:TestScript/f:test/f:metadata">
      <sch:assert test="f:capability/f:required or f:capability/f:validated or (f:capability/f:required and f:capability/f:validated)">inv-7: Test metadata capability SHALL contain required or validated or both.</sch:assert>
    </sch:rule>
    <sch:rule context="//f:TestScript/f:test/f:action">
      <sch:assert test="(f:operation or f:assert) and not(f:operation and f:assert)">inv-2: Test action SHALL contain either an operation or assert but not both.</sch:assert>
    </sch:rule>
    <sch:rule context="//f:TestScript/f:test/f:action/f:operation">
      <sch:assert test="f:sourceId or (f:targetId or f:url or f:params) and (count(f:targetId) + count(f:url) + count(f:params) =1) or (f:type/f:code/@value='conformance' or f:type/f:code/@value='search' or f:type/f:code/@value='transaction' or f:type/f:code/@value='history')">inv-11: Test operation SHALL contain either sourceId or targetId or params or url.</sch:assert>
    </sch:rule>
    <sch:rule context="//f:TestScript/f:test/f:action/f:assert">
      <sch:assert test="count(f:contentType) + count(f:headerField) + count(f:minimumId) + count(f:navigationLinks) + count(f:path) + count(f:resource) + count(f:responseCode) + count(f:response) + count(f:validateProfileId)  &lt;=1">inv-9: Only a single assertion SHALL be present within test action assert element.</sch:assert>
      <sch:assert test="(f:compareToSourceId and f:compareToSourcePath) or not(f:compareToSourceId or f:compareToSourcePath)">inv-14: Test action assert shall contain both compareToSourceId and compareToSourcePath or neither.</sch:assert>
    </sch:rule>
    <sch:rule context="//f:TestScript/f:teardown/f:action">
      <sch:assert test="f:operation">inv-3: Teardown action SHALL contain an operation.</sch:assert>
    </sch:rule>
    <sch:rule context="//f:TestScript/f:teardown/f:action/f:operation">
      <sch:assert test="f:sourceId or (f:targetId or f:url or (f:params and f:resource)) and (count(f:targetId) + count(f:url) + count(f:params) =1) or (f:type/f:code/@value='conformance' or f:type/f:code/@value='search' or f:type/f:code/@value='transaction' or f:type/f:code/@value='history')">inv-12: Teardown operation SHALL contain either sourceId or targetId or params or url.</sch:assert>
    </sch:rule>
  </sch:pattern>
</sch:schema>
