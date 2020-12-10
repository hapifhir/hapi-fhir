# MDM Getting Started 

<div class="helpInfoCalloutBox">
MDM module replaces the EMPI module. EMPI is now deprecated and can not be used. Please refer to the [Migration Instructions](#migration-instructions) section for more details.
</div>

## Introduction 
 
A Master Data Management (MDM) module allows for links to be created and maintained among FHIR resources. These links indicate the fact that different FHIR resources are known or believed to refer to the same actual (real world) resource. The links are created and updated using different combinations of automatic and manual linking. 
 
The real-world resource is referred to as the Golden Resource in this context. The resource believed to be a duplicate is said to be a source resource.

## Working Example

A complete working example of HAPI MDM can be found in the [JPA Server Starter](/hapi-fhir/docs/server_jpa/get_started.html) project. You may wish to browse its source to see how it is set up.

## Overview

To get up and running with HAPI MDM, either enable it using the `hapi.properties` file in the JPA Server Starter, or follow the instructions below to (enable it in HAPI FHIR directly)[#mdm-settings].  

Once MDM is enabled, the next thing you will want to do is configure your [MDM Rules](/hapi-fhir/docs/server_jpa_mdm/mdm_rules.html)

HAPI MDM watches for incoming source resources and automatically links them to the appropriate Golden Resources based on these rules.  For example, if the rules indicate that any two patients with the same SSN, birthdate and first and last name are the same patient, then two different Patient resources with matching values for these attributes will automatically be linked to the same Golden Patient resource. If no existing resources match the incoming Patient, then a new Golden Patient resource will be created and linked to the incoming Patient.

Based on how well two patients match, the MDM Rules may link the Patient to the Golden Patient as a MATCH or a POSSIBLE_MATCH. In the case of a POSSIBLE_MATCH, a user will need to later use [MDM Operations](/hapi-fhir/docs/server_jpa_mdm/mdm_operations.html) to either confirm the link as a MATCH, or mark the link as a NO_MATCH in which case HAPI MDM will create a new Golden Resource Patient record for them.

Another thing that can happen in the linking process is HAPI MDM can determine that two Patients resources may be duplicates. In this case, it marks them as POSSIBLE_DUPLICATE and the user can use [MDM Operations](/hapi-fhir/docs/server_jpa_mdm/mdm_operations.html) to either merge the two Patients or mark them as NO_MATCH in which case HAPI MDM will know not to mark them as possible duplicates in the future.

HAPI MDM keeps track of which links were automatically established vs manually verified.  Manual links always take precedence over automatic links. Once a link for a patient has been manually verified, HAPI MDM won't modify or remove it.

## MDM Settings

Follow these steps to enable MDM on the server:

The [MdmSettings](/hapi-fhir/apidocs/hapi-fhir-server-mdm/ca/uhn/fhir/mdm/rules/config/MdmSettings.html) bean contains configuration settings related to MDM within the server. To enable MDM, the [setEnabled(boolean)](/hapi-fhir/apidocs/hapi-fhir-server-mdm/ca/uhn/fhir/mdm/rules/config/MdmSettings.html#setEnabled(boolean)) property should be enabled.

See [MDM EID Settings](/hapi-fhir/docs/server_jpa_mdm/mdm_eid.html#mdm-eid-settings) for a description of the EID-related settings.

## Migration Instructions

Please note that EMPI is now deprecated and cannot be used. To switch from EMPI to MDM, please copy over EMPI settings to MDM module settings. Also note that MDM now requires "mdmTypes" in the JSON configuration. This entry should include all FHIR resource types that are supported by MDM. For more details on supported MDM types refer to [MDM Rules](/hapi-fhir/docs/server_jpa_mdm/mdm_rules.html)
