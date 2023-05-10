# PlanDefinition

## Introduction

The FHIR Clinical Reasoning Module defines the [PlanDefinition resource](https://www.hl7.org/fhir/plandefinition.html) and several [associated operations](https://www.hl7.org/fhir/plandefinition-operations.html). A plan definition is a pre-defined group of actions to be taken in particular circumstances, often including conditional elements, options, and other decision points. The resource is flexible enough to be used to represent a variety of workflows, as well as clinical decision support and quality improvement assets, including order sets, protocols, and decision support rules.

PlanDefinitions can contain hierarchical groups of action definitions, where each action definition describes an activity to be performed (often in terms of an ActivityDefinition resource), and each group defines additional behavior, relationships, and applicable conditions between the actions in the overall definition.

In addition to describing what should take place, each action in a plan definition can specify when and whether the action should take place. For when the action should be taken, the trigger element specifies the action should be taken in response to some trigger occurring (such as a particular point in a workflow being reached, or as the result of a prescription being ordered). For whether the action should be taken, the condition element can be used to provide an expression that evaluates to true or false to indicate the applicability of the action to the specific context.

The process of applying a PlanDefinition to a particular context typically produces request resources representing the actions that should be performed, grouped within a RequestOrchestration to capture relationships between the resulting request resources.

Each ActivityDefinition is used to construct a specific resource, based on the definition of the activity and combined with contextual information for the particular patient that the plan definition is being applied to.


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

## Operations

HAPI implements the [$apply](http://hl7.org/fhir/uv/cpg/OperationDefinition-cpg-plandefinition-apply.html) operation. Support for additional operations is planned.

## Apply

The `$apply` operation applies a PlanDefinition to a given context. This implementation follows the [FHIR Specification](https://www.hl7.org/fhir/plandefinition.html#12.23.4.3) and supports the [FHIR Clinical Guidelines IG](http://hl7.org/fhir/uv/cpg/index.html)

### Example PlanDefinition

Some example PlanDefinition workflows are available in the [opioid-cds-r4](https://github.com/cqframework/opioid-cds-r4) IG. Full Bundles with all the required supporting resources are available [here](https://github.com/cqframework/opioid-cds-r4/tree/master/bundles). You can download a Bundle and load it on your server as a transaction:

```bash
POST http://your-server-base/fhir opioidcds-10-patient-view-bundle.json
```

These Bundles do not include example Patient clinical data. Applying a PlanDefinition can be invoked with:

```bash
GET http://your-server-base/fhir/PlanDefinition/opioidcds-10-patient-view/$apply?subject=Patient/patientId&encounter=Encounter/encounterId&practitioner=Practitioner/practitionerId
```

