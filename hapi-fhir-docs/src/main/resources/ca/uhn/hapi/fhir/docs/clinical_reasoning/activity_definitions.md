# ActivityDefinition

## Introduction

The FHIR Clinical Reasoning Module defines the [ActivityDefinition resource](https://www.hl7.org/fhir/activitydefinition.html) and several [associated operations](https://www.hl7.org/fhir/activitydefinition-operations.html). An ActivityDefinition is a shareable, consumable description of some activity to be performed. It may be used to specify actions to be taken as part of a workflow, order set, or protocol, or it may be used independently as part of a catalog of activities such as orderables.

In general, an activity definition is simply a conceptual description of some specific action that should be taken. An instance of an ActivityDefinition does not indicate that any action has been performed (as an event resource does), nor does it indicate the actual intent to carry out any particular action (as a request resource does). Instead, an activity definition provides a reusable template that can be used to construct specific request resources such as ServiceRequest and MedicationRequest.

Note that this is conceptually similar to the Task resource as well, with the distinction being that ActivityDefinition represents the description of a task in the abstract, while the Task resource is used to track a specific instance of a task as it moves through the steps of a workflow.

An ActivityDefinition resource provides a description, or template, of an action to perform. These actions can be purely text-based descriptions of the action to be performed, only interpretable by a human user, or they can be structured definitions with enough information to construct a resource to represent the request or activity directly. This process of converting the ActivityDefinition into a specific resource in a particular context is performed with the [$apply](/docs/clinical_reasoning/activity_definitions.html#apply) operation.

## Operations

HAPI implements the following operations for ActivityDefinitions

* [$apply](/docs/clinical_reasoning/activity_definitions.html#apply)

## Apply

The `ActivityDefinition/$apply` [operation](https://www.hl7.org/fhir/activitydefinition-operation-apply.html) creates a [Request Resource](https://www.hl7.org/fhir/workflow.html#request) for a given context. This implementation follows the [FHIR Specification](https://www.hl7.org/fhir/activitydefinition.html#12.22.4.3) and supports the [FHIR Clinical Guidelines IG](http://hl7.org/fhir/uv/cpg/index.html).

### Parameters

The following parameters are supported for the `ActivityDefinition/$apply` operation:

| Parameter           | Type                      | Description |
|---------------------|---------------------------|-------------|
| activityDefinition      | ActivityDefinition            | The activity definition to be applied. If the operation is invoked at the instance level, this parameter is not allowed; if the operation is invoked at the type level, this parameter is required, or a url (and optionally version) must be supplied. |
| canonical           | canonical(ActivityDefinition) | The canonical url of the activity definition to be applied. If the operation is invoked at the instance level, this parameter is not allowed; if the operation is invoked at the type level, this parameter (and optionally the version), or the activityDefinition parameter must be supplied. |
| url                 | uri                       | Canonical URL of the ActivityDefinition when invoked at the resource type level. This is exclusive with the activityDefinition and canonical parameters. |
| version             | string                    | Version of the ActivityDefinition when invoked at the resource type level. This is exclusive with the activityDefinition and canonical parameters. |
| subject             | string(reference)         | The subject(s) that is/are the target of the activity definition to be applied. |
| encounter           | string(reference)         | The encounter in context, if any. |
| practitioner        | string(reference)         | The practitioner applying the activity definition. |
| organization        | string(reference)         | The organization applying the activity definition. |
| userType            | CodeableConcept           | The type of user initiating the request, e.g. patient, healthcare provider, or specific type of healthcare provider (physician, nurse, etc.) |
| userLanguage        | CodeableConcept           | Preferred language of the person using the system |
| userTaskContext     | CodeableConcept           | The task the system user is performing, e.g. laboratory results review, medication list review, etc. This information can be used to tailor decision support outputs, such as recommended information resources. |
| setting             | CodeableConcept           | The current setting of the request (inpatient, outpatient, etc.). |
| settingContext      | CodeableConcept           | Additional detail about the setting of the request, if any |
| parameters          | Parameters                | Any input parameters defined in libraries referenced by the ActivityDefinition. |
| useServerData       | boolean                   | Whether to use data from the server performing the evaluation. If this parameter is true (the default), then the operation will use data first from any bundles provided as parameters (through the data and prefetch parameters), second data from the server performing the operation, and third, data from the dataEndpoint parameter (if provided). If this parameter is false, the operation will use data first from the bundles provided in the data or prefetch parameters, and second from the dataEndpoint parameter (if provided). |
| data                | Bundle                    | Data to be made available to the ActivityDefinition evaluation. |
| dataEndpoint        | Endpoint                  | An endpoint to use to access data referenced by retrieve operations in libraries referenced by the ActivityDefinition. |
| contentEndpoint     | Endpoint                  | An endpoint to use to access content (i.e. libraries) referenced by the ActivityDefinition. |
| terminologyEndpoint | Endpoint                  | An endpoint to use to access terminology (i.e. valuesets, codesystems, and membership testing) referenced by the ActivityDefinition. |

