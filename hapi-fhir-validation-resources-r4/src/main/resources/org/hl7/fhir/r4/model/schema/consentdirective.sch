<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
  <sch:ns prefix="f" uri="http://hl7.org/fhir"/>
  <sch:ns prefix="h" uri="http://www.w3.org/1999/xhtml"/>
  <!-- 
    This file contains just the constraints for the profile Contract
    It includes the base constraints for the resource as well.
    Because of the way that schematrons and containment work, 
    you may need to use this schematron fragment to build a, 
    single schematron that validates contained resources (if you have any) 
  -->
  <sch:pattern>
    <sch:title>f:Contract</sch:title>
    <sch:rule context="f:Contract">
      <sch:assert test="count(f:identifier) &lt;= 1">identifier: maximum cardinality of 'identifier' is 1</sch:assert>
      <sch:assert test="count(f:valuedItem) &lt;= 0">valuedItem: maximum cardinality of 'valuedItem' is 0</sch:assert>
      <sch:assert test="count(f:binding[x]) &gt;= 1">binding[x]: minimum cardinality of 'binding[x]' is 1</sch:assert>
      <sch:assert test="count(f:binding[x]) &lt;= 1">binding[x]: maximum cardinality of 'binding[x]' is 1</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>f:Contract/f:actor</sch:title>
    <sch:rule context="f:Contract/f:actor">
      <sch:assert test="count(f:id) &lt;= 1">id: maximum cardinality of 'id' is 1</sch:assert>
      <sch:assert test="count(f:entity) &gt;= 1">entity: minimum cardinality of 'entity' is 1</sch:assert>
      <sch:assert test="count(f:entity) &lt;= 1">entity: maximum cardinality of 'entity' is 1</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>f:Contract/f:valuedItem</sch:title>
    <sch:rule context="f:Contract/f:valuedItem">
      <sch:assert test="count(f:id) &lt;= 1">id: maximum cardinality of 'id' is 1</sch:assert>
      <sch:assert test="count(f:entity[x]) &lt;= 1">entity[x]: maximum cardinality of 'entity[x]' is 1</sch:assert>
      <sch:assert test="count(f:identifier) &lt;= 1">identifier: maximum cardinality of 'identifier' is 1</sch:assert>
      <sch:assert test="count(f:effectiveTime) &lt;= 1">effectiveTime: maximum cardinality of 'effectiveTime' is 1</sch:assert>
      <sch:assert test="count(f:quantity) &lt;= 1">quantity: maximum cardinality of 'quantity' is 1</sch:assert>
      <sch:assert test="count(f:unitPrice) &lt;= 1">unitPrice: maximum cardinality of 'unitPrice' is 1</sch:assert>
      <sch:assert test="count(f:factor) &lt;= 1">factor: maximum cardinality of 'factor' is 1</sch:assert>
      <sch:assert test="count(f:points) &lt;= 1">points: maximum cardinality of 'points' is 1</sch:assert>
      <sch:assert test="count(f:net) &lt;= 1">net: maximum cardinality of 'net' is 1</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>Contract.valuedItem.quantity</sch:title>
    <sch:rule context="f:Contract/f:valuedItem/f:quantity">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">If a code for the unit is present, the system SHALL also be present</sch:assert>
      <sch:assert test="not(exists(f:comparator))">The comparator is not used on a SimpleQuantity</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>Contract.valuedItem.unitPrice</sch:title>
    <sch:rule context="f:Contract/f:valuedItem/f:unitPrice">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">If a code for the unit is present, the system SHALL also be present</sch:assert>
      <sch:assert test="(f:code or not(f:value)) and (not(exists(f:system)) or f:system/@value='urn:iso:std:iso:4217')">There SHALL be a code if there is a value and it SHALL be an expression of currency.  If system is present, it SHALL be ISO 4217 (system = &quot;urn:iso:std:iso:4217&quot; - currency).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>Contract.valuedItem.net</sch:title>
    <sch:rule context="f:Contract/f:valuedItem/f:net">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">If a code for the unit is present, the system SHALL also be present</sch:assert>
      <sch:assert test="(f:code or not(f:value)) and (not(exists(f:system)) or f:system/@value='urn:iso:std:iso:4217')">There SHALL be a code if there is a value and it SHALL be an expression of currency.  If system is present, it SHALL be ISO 4217 (system = &quot;urn:iso:std:iso:4217&quot; - currency).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>f:Contract/f:signer</sch:title>
    <sch:rule context="f:Contract/f:signer">
      <sch:assert test="count(f:signature) &lt;= 1">signature: maximum cardinality of 'signature' is 1</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>f:Contract/f:term</sch:title>
    <sch:rule context="f:Contract/f:term">
      <sch:assert test="count(f:topic) &lt;= 1">topic: maximum cardinality of 'topic' is 1</sch:assert>
      <sch:assert test="count(f:text) &lt;= 1">text: maximum cardinality of 'text' is 1</sch:assert>
      <sch:assert test="count(f:valuedItem) &lt;= 0">valuedItem: maximum cardinality of 'valuedItem' is 0</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>f:Contract/f:term/f:actor</sch:title>
    <sch:rule context="f:Contract/f:term/f:actor">
      <sch:assert test="count(f:id) &lt;= 1">id: maximum cardinality of 'id' is 1</sch:assert>
      <sch:assert test="count(f:entity) &gt;= 1">entity: minimum cardinality of 'entity' is 1</sch:assert>
      <sch:assert test="count(f:entity) &lt;= 1">entity: maximum cardinality of 'entity' is 1</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>f:Contract/f:term/f:valuedItem</sch:title>
    <sch:rule context="f:Contract/f:term/f:valuedItem">
      <sch:assert test="count(f:id) &lt;= 1">id: maximum cardinality of 'id' is 1</sch:assert>
      <sch:assert test="count(f:entity[x]) &lt;= 1">entity[x]: maximum cardinality of 'entity[x]' is 1</sch:assert>
      <sch:assert test="count(f:identifier) &lt;= 1">identifier: maximum cardinality of 'identifier' is 1</sch:assert>
      <sch:assert test="count(f:effectiveTime) &lt;= 1">effectiveTime: maximum cardinality of 'effectiveTime' is 1</sch:assert>
      <sch:assert test="count(f:quantity) &lt;= 1">quantity: maximum cardinality of 'quantity' is 1</sch:assert>
      <sch:assert test="count(f:unitPrice) &lt;= 1">unitPrice: maximum cardinality of 'unitPrice' is 1</sch:assert>
      <sch:assert test="count(f:factor) &lt;= 1">factor: maximum cardinality of 'factor' is 1</sch:assert>
      <sch:assert test="count(f:points) &lt;= 1">points: maximum cardinality of 'points' is 1</sch:assert>
      <sch:assert test="count(f:net) &lt;= 1">net: maximum cardinality of 'net' is 1</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>Contract.term.valuedItem.quantity</sch:title>
    <sch:rule context="f:Contract/f:term/f:valuedItem/f:quantity">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">If a code for the unit is present, the system SHALL also be present</sch:assert>
      <sch:assert test="not(exists(f:comparator))">The comparator is not used on a SimpleQuantity</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>Contract.term.valuedItem.unitPrice</sch:title>
    <sch:rule context="f:Contract/f:term/f:valuedItem/f:unitPrice">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">If a code for the unit is present, the system SHALL also be present</sch:assert>
      <sch:assert test="(f:code or not(f:value)) and (not(exists(f:system)) or f:system/@value='urn:iso:std:iso:4217')">There SHALL be a code if there is a value and it SHALL be an expression of currency.  If system is present, it SHALL be ISO 4217 (system = &quot;urn:iso:std:iso:4217&quot; - currency).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>Contract.term.valuedItem.net</sch:title>
    <sch:rule context="f:Contract/f:term/f:valuedItem/f:net">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">If a code for the unit is present, the system SHALL also be present</sch:assert>
      <sch:assert test="(f:code or not(f:value)) and (not(exists(f:system)) or f:system/@value='urn:iso:std:iso:4217')">There SHALL be a code if there is a value and it SHALL be an expression of currency.  If system is present, it SHALL be ISO 4217 (system = &quot;urn:iso:std:iso:4217&quot; - currency).</sch:assert>
    </sch:rule>
  </sch:pattern>
</sch:schema>
