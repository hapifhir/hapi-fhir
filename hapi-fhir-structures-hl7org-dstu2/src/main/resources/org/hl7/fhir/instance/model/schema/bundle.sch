<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
  <sch:ns prefix="f" uri="http://hl7.org/fhir"/>
  <sch:ns prefix="h" uri="http://www.w3.org/1999/xhtml"/>
  <!-- 
    This file contains just the constraints for the resource Bundle
    It is provided for documentation purposes. When actually validating,
    always use fhir-invariants.sch (because of the way containment works)
    Alternatively you can use this file to build a smaller version of
    fhir-invariants.sch (the contents are identical; only include those 
    resources relevant to your implementation).
  -->
    <sch:pattern>
      <sch:title>Global</sch:title>
      <sch:rule context="f:*">
        <sch:assert test="@value|f:*|h:div">global-1: All FHIR elements must have a @value or children</sch:assert>
      </sch:rule>
    </sch:pattern>
  <sch:pattern>
    <sch:title>Bundle</sch:title>
    <sch:rule context="f:Bundle">
      <sch:assert test="not(f:entry/f:transaction) or (f:type/@value = 'transaction') or (f:type/@value = 'history')">bdl-3: entry.transaction when (and only when) a transaction</sch:assert>
      <sch:assert test="not(f:entry/f:transactionResponse) or (f:type/@value = 'transaction-response')">bdl-4: entry.transactionResponse when (and only when) a transaction-response</sch:assert>
      <sch:assert test="not(f:total) or (f:type/@value = 'searchset') or (f:type/@value = 'history')">bdl-1: total only when a search or history</sch:assert>
      <sch:assert test="not(f:entry/f:search) or (f:type/@value = 'searchset')">bdl-2: entry.search only when a search</sch:assert>
    </sch:rule>
    <sch:rule context="f:Bundle/f:entry">
      <sch:assert test="f:resource or f:transaction or f:transactionResponse">bdl-5: must be a resource unless there's a transaction or transaction response</sch:assert>
    </sch:rule>
    </sch:pattern>
</sch:schema>
