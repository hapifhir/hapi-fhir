# PlanDefinition

## Introduction

The FHIR Clinical Reasoning Module defines the [PlanDefinition resource](https://www.hl7.org/fhir/plandefinition.html) and several [associated operations](https://www.hl7.org/fhir/plandefinition-operations.html). A plan definition is a pre-defined group of actions to be taken in particular circumstances, often including conditional elements, options, and other decision points. The resource is flexible enough to be used to represent a variety of workflows, as well as clinical decision support and quality improvement assets, including order sets, protocols, and decision support rules.

PlanDefinitions can contain hierarchical groups of action definitions, where each action definition describes an activity to be performed (often in terms of an ActivityDefinition resource), and each group defines additional behavior, relationships, and applicable conditions between the actions in the overall definition.

In addition to describing what should take place, each action in a plan definition can specify when and whether the action should take place. For when the action should be taken, the trigger element specifies the action should be taken in response to some trigger occurring (such as a particular point in a workflow being reached, or as the result of a prescription being ordered). For whether the action should be taken, the condition element can be used to provide an expression that evaluates to true or false to indicate the applicability of the action to the specific context.

The process of applying a PlanDefinition to a particular context typically produces request resources representing the actions that should be performed, grouped within a RequestOrchestration to capture relationships between the resulting request resources.

Each ActivityDefinition is used to construct a specific resource, based on the definition of the activity and combined with contextual information for the particular patient that the plan definition is being applied to.


## Operations

HAPI implements the following operations for PlanDefinitions:

* [$apply](/docs/clinical_reasoning/plan_definitions.html#apply)
* [$package](/docs/clinical_reasoning/plan_definitions.html#package)

## Apply

The `PlanDefinition/$apply` [operation](https://www.hl7.org/fhir/plandefinition-operation-apply.html) applies a PlanDefinition to a given context. This implementation follows the [FHIR Specification](https://www.hl7.org/fhir/plandefinition.html#12.23.4.3) and supports the [FHIR Clinical Guidelines IG](http://hl7.org/fhir/uv/cpg/index.html). In addition, an R5 version of apply is made available for R4 instances.  This will cause $apply to return a Bundle of resources instead of a CarePlan.  This can be invoked with `$r5.apply`.

Some example PlanDefinition workflows are available in the [opioid-cds-r4](https://github.com/cqframework/opioid-cds-r4) IG. Full Bundles with all the required supporting resources are available [here](https://github.com/cqframework/opioid-cds-r4/tree/1e543f781138f3d85404b7f65a92ff713519ef2c/bundles). You can download a Bundle and load it on your server as a transaction:

```bash
POST http://your-server-base/fhir opioidcds-10-patient-view-bundle.json
```

These Bundles do not include example Patient clinical data. Applying a PlanDefinition can be invoked with:

```bash
GET http://your-server-base/fhir/PlanDefinition/opioidcds-10-patient-view/$apply?subject=Patient/patientId&encounter=Encounter/encounterId&practitioner=Practitioner/practitionerId
```

### Parameters

The following parameters are supported for the `PlanDefinition/$apply` and `PlanDefinition/$r5.apply` operation:

| Parameter           | Type                      | Description |
|---------------------|---------------------------|-------------|
| planDefinition      | PlanDefinition            | The plan definition to be applied. If the operation is invoked at the instance level, this parameter is not allowed; if the operation is invoked at the type level, this parameter is required, or a url (and optionally version) must be supplied. |
| canonical           | canonical(PlanDefinition) | The canonical url of the plan definition to be applied. If the operation is invoked at the instance level, this parameter is not allowed; if the operation is invoked at the type level, this parameter (and optionally the version), or the planDefinition parameter must be supplied. |
| url                 | uri                       | Canonical URL of the PlanDefinition when invoked at the resource type level. This is exclusive with the planDefinition and canonical parameters. |
| version             | string                    | Version of the PlanDefinition when invoked at the resource type level. This is exclusive with the planDefinition and canonical parameters. |
| subject             | string(reference)         | The subject(s) that is/are the target of the plan definition to be applied. |
| encounter           | string(reference)         | The encounter in context, if any. |
| practitioner        | string(reference)         | The practitioner applying the plan definition. |
| organization        | string(reference)         | The organization applying the plan definition. |
| userType            | CodeableConcept           | The type of user initiating the request, e.g. patient, healthcare provider, or specific type of healthcare provider (physician, nurse, etc.) |
| userLanguage        | CodeableConcept           | Preferred language of the person using the system |
| userTaskContext     | CodeableConcept           | The task the system user is performing, e.g. laboratory results review, medication list review, etc. This information can be used to tailor decision support outputs, such as recommended information resources. |
| setting             | CodeableConcept           | The current setting of the request (inpatient, outpatient, etc.). |
| settingContext      | CodeableConcept           | Additional detail about the setting of the request, if any |
| parameters          | Parameters                | Any input parameters defined in libraries referenced by the PlanDefinition. |
| useServerData       | boolean                   | Whether to use data from the server performing the evaluation. If this parameter is true (the default), then the operation will use data first from any bundles provided as parameters (through the data and prefetch parameters), second data from the server performing the operation, and third, data from the dataEndpoint parameter (if provided). If this parameter is false, the operation will use data first from the bundles provided in the data or prefetch parameters, and second from the dataEndpoint parameter (if provided). |
| data                | Bundle                    | Data to be made available to the PlanDefinition evaluation. |
| dataEndpoint        | Endpoint                  | An endpoint to use to access data referenced by retrieve operations in libraries referenced by the PlanDefinition. |
| contentEndpoint     | Endpoint                  | An endpoint to use to access content (i.e. libraries) referenced by the PlanDefinition. |
| terminologyEndpoint | Endpoint                  | An endpoint to use to access terminology (i.e. valuesets, codesystems, and membership testing) referenced by the PlanDefinition. |


## Package

The `PlanDefinition/$package` [operation](https://build.fhir.org/ig/HL7/crmi-ig/OperationDefinition-crmi-package.html) for PlanDefinition will generate a Bundle of resources that includes the PlanDefinition as well as any related resources which can then be shared. This implementation follows the [CRMI IG](https://build.fhir.org/ig/HL7/crmi-ig/branches/master/index.html) guidance for [packaging artifacts](https://build.fhir.org/ig/HL7/crmi-ig/branches/master/packaging.html).

### Parameters

The following parameters are supported for the `PlanDefinition/$package` operation:

| Parameter | Type      | Description |
|-----------|-----------|-------------|
| id        | string    | The logical id of an existing Resource to package on the server. |
| canonical | canonical | A canonical url (optionally version specific) of a Resource to package on the server. |
| url       | uri       | A canonical or artifact reference to a Resource to package on the server. This is exclusive with the canonical parameter. |
| version   | string    | The version of the Resource. This is exclusive with the canonical parameter. | 
| usePut    | boolean   | Determines the type of method returned in the Bundle Entries: POST if False (the default), PUT if True. | 


## Example PlanDefinition

```json
{
   "resourceType": "PlanDefinition",
   "id": "opioidcds-04",
   "url": "http://hl7.org/fhir/ig/opioid-cds/PlanDefinition/opioidcds-04",
   "identifier": [
      {
         "system": "urn:ietf:rfc:3986",
         "value": "urn:oid:2.16.840.1.113883.4.642.11.4"
      },
      {
         "use": "official",
         "value": "cdc-opioid-guidance"
      }
   ],
   "version": "0.1.0",
   "name": "Cdcopioid04",
   "title": "CDC Opioid Prescribing Guideline Recommendation #4",
   "type": {
      "coding": [
         {
            "system": "http://terminology.hl7.org/CodeSystem/plan-definition-type",
            "code": "eca-rule",
            "display": "ECA Rule"
         }
      ]
   },
   "status": "draft",
   "date": "2018-03-19",
   "publisher": "Centers for Disease Control and Prevention (CDC)",
   "description": "When starting opioid therapy for chronic pain, clinicians should prescribe immediate-release opioids instead of extended-release/long-acting (ER/LA) opioids.",
   "useContext": [
      {
         "code": {
            "system": "http://terminology.hl7.org/CodeSystem/usage-context-type",
            "code": "focus",
            "display": "Clinical Focus"
         },
         "valueCodeableConcept": {
            "coding": [
               {
                  "system": "http://snomed.info/sct",
                  "code": "182888003",
                  "display": "Medication requested (situation)"
               }
            ]
         }
      },
      {
         "code": {
            "system": "http://terminology.hl7.org/CodeSystem/usage-context-type",
            "code": "focus",
            "display": "Clinical Focus"
         },
         "valueCodeableConcept": {
            "coding": [
               {
                  "system": "http://snomed.info/sct",
                  "code": "82423001",
                  "display": "Chronic pain (finding)"
               }
            ]
         }
      }
   ],
   "jurisdiction": [
      {
         "coding": [
            {
               "system": "urn:iso:std:iso:3166",
               "code": "US",
               "display": "United States of America"
            }
         ]
      }
   ],
   "purpose": "CDC’s Guideline for Prescribing Opioids for Chronic Pain is intended to improve communication between providers and patients about the risks and benefits of opioid therapy for chronic pain, improve the safety and effectiveness of pain treatment, and reduce the risks associated with long-term opioid therapy, including opioid use disorder and overdose. The Guideline is not intended for patients who are in active cancer treatment, palliative care, or end-of-life care.",
   "usage": "Providers should use caution when prescribing extended-release/long-acting (ER/LA) opioids as they carry a higher risk and negligible benefit compared to immediate-release opioids.",
   "copyright": "© CDC 2016+.",
   "topic": [
      {
         "text": "Opioid Prescribing"
      }
   ],
   "author": [
      {
         "name": "Kensaku Kawamoto, MD, PhD, MHS"
      },
      {
         "name": "Bryn Rhodes"
      },
      {
         "name": "Floyd Eisenberg, MD, MPH"
      },
      {
         "name": "Robert McClure, MD, MPH"
      }
   ],
   "relatedArtifact": [
      {
         "type": "documentation",
         "display": "CDC guideline for prescribing opioids for chronic pain",
         "document": {
            "url": "https://guidelines.gov/summaries/summary/50153/cdc-guideline-for-prescribing-opioids-for-chronic-pain---united-states-2016#420"
         }
      },
      {
         "type": "documentation",
         "display": "MME Conversion Tables",
         "document": {
            "url": "https://www.cdc.gov/drugoverdose/pdf/calculating_total_daily_dose-a.pdf"
         }
      }
   ],
   "library": [
      "http://example.org/fhir/Library/opioidcds-recommendation-04"
   ],
   "action": [
      {
         "title": "Extended-release opioid prescription triggered.",
         "description": "Checking if the trigger prescription meets the inclusion criteria for recommendation #4 workflow.",
         "documentation": [
            {
               "type": "documentation",
               "document": {
                  "extension": [
                     {
                        "url": "http://hl7.org/fhir/StructureDefinition/cqf-strengthOfRecommendation",
                        "valueCodeableConcept": {
                           "coding": [
                              {
                                 "system": "http://terminology.hl7.org/CodeSystem/recommendation-strength",
                                 "code": "strong",
                                 "display": "Strong"
                              }
                           ]
                        }
                     },
                     {
                        "url": "http://hl7.org/fhir/StructureDefinition/cqf-qualityOfEvidence",
                        "valueCodeableConcept": {
                           "coding": [
                              {
                                 "system": "http://terminology.hl7.org/CodeSystem/evidence-quality",
                                 "code": "low",
                                 "display": "Low quality"
                              }
                           ]
                        }
                     }
                  ]
               }
            }
         ],
         "trigger": [
            {
               "type": "named-event",
               "name": "medication-prescribe"
            }
         ],
         "condition": [
            {
               "kind": "applicability",
               "expression": {
                  "description": "Check whether the opioid prescription for the existing patient is extended-release without any opioids-with-abuse-potential prescribed in the past 90 days.",
                  "language": "text/cql-identifier",
                  "expression": "Inclusion Criteria"
               }
            }
         ],
         "groupingBehavior": "visual-group",
         "selectionBehavior": "exactly-one",
         "dynamicValue": [
            {
               "path": "action.title",
               "expression": {
                  "language": "text/cql-identifier",
                  "expression": "Get Summary"
               }
            },
            {
               "path": "action.description",
               "expression": {
                  "language": "text/cql-identifier",
                  "expression": "Get Detail"
               }
            },
            {
               "path": "activity.extension",
               "expression": {
                  "language": "text/cql-identifier",
                  "expression": "Get Indicator"
               }
            }
         ],
         "action": [
            {
               "description": "Will prescribe immediate release"
            },
            {
               "description": "Risk of overdose carefully considered and outweighed by benefit; snooze 3 mo"
            },
            {
               "description": "N/A - see comment; snooze 3 mo"
            }
         ]
      }
   ]
}
```
