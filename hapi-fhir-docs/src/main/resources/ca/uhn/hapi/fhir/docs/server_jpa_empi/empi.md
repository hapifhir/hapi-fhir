# EMPI Getting Started

## Introduction

An Enterprise Master Patient Index (EMPI) allows for links to be created and maintained between different Patient and/or Practitioner resources. These links are used to indicate the fact that different Patient/Practitioner resources are known or believed to refer to the same actual (real world) person.

These links are created and updated using different combinations of automatic linking and manual linking.

Note: This documentation describes EMPI for Patient resources. The same information applies for Practitioner resources.  You can substitute "Practitioner" for "Patient" anywhere it appears in this documentation.

## Working Example

A complete working example of HAPI EMPI can be found in the [JPA Server Starter](/hapi-fhir/docs/server_jpa/get_started.html) project. You may wish to browse its source to see how it is set up.

## Overview

To get up and running with HAPI EMPI, either enable it using the `hapi.properties` file in the JPA Server Starter, or follow the instructions below to (enable it in HAPI FHIR directly)[#empi-settings].  

Once EMPI is enabled, the next thing you will want to do is configure your [EMPI Rules](/hapi-fhir/docs/server_jpa_empi/empi_rules.html)

HAPI EMPI watches for incoming Patient resources and automatically links them to Person resources based on these rules.  For example, if the rules indicate that any two patients with the same ssn, birthdate and first and last name are the same person, then two different Patient resources with matching values for these attributes will automatically be linked to the same Person resource.  If no existing resources match the incoming Patient, then a new Person resource will be created and linked to the incoming Patient.

Based on how well two patients match, the EMPI Rules may link the Patient to the Person as a MATCH or a POSSIBLE_MATCH.  In the case of a POSSIBLE_MATCH, a user will need to later use [EMPI Operations](/hapi-fhir/docs/server_jpa_empi/empi_operations.html) to either confirm the link as a MATCH, or mark the link as a NO_MATCH in which case HAPI EMPI will create a new Person for them.

Another thing that can happen in the linking process is HAPI EMPI can determine that two Person resources may be duplicates.  In this case, it marks them as POSSIBLE_DUPLICATE and the user can use [EMPI Operations](/hapi-fhir/docs/server_jpa_empi/empi_operations.html) to either merge the two Persons or mark them as NO_MATCH in which case HAPI EMPI will know not to mark them as possible duplicates in the future.

HAPI EMPI keeps track of which links were automatically established vs manually verified.  Manual links always take precedence over automatic links.  Once a link for a patient has been manually verified, HAPI EMPI won't modify or remove it.

## EMPI Settings

Follow these steps to enable EMPI on the server:

The [EmpiSettings](/hapi-fhir/apidocs/hapi-fhir-server-empi/ca/uhn/fhir/empi/rules/config/EmpiSettings.html) bean contains configuration settings related to EMPI within the server. To enable EMPI, the [setEnabled(boolean)](/hapi-fhir/apidocs/hapi-fhir-server-empi/ca/uhn/fhir/empi/rules/config/EmpiSettings.html#setEnabled(boolean)) property should be enabled.

See [EMPI EID Settings](/hapi-fhir/docs/server_jpa_empi/empi_eid.html#empi-eid-settings) for a description of the EID-related settings.
