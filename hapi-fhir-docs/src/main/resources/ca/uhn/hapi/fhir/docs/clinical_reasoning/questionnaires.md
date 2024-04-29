# Questionnaires

## Introduction

The FHIR Clinical Reasoning Module defines the [Questionnaire resource](https://www.hl7.org/fhir/questionnaire.html). A Questionnaire is an organized collection of questions intended to solicit information from patients, providers or other individuals involved in the healthcare domain. They may be simple flat lists of questions or can be hierarchically organized in groups and sub-groups, each containing questions. The Questionnaire defines the questions to be asked, how they are ordered and grouped, any intervening instructional text and what the constraints are on the allowed answers. The results of a Questionnaire can be communicated using the QuestionnaireResponse resource.

Questionnaires cover the need to communicate data originating from forms used in medical history examinations, research questionnaires and sometimes full clinical specialty records. In many systems this data is collected using user-defined screens and forms. Questionnaires define specifics about data capture - exactly what questions were asked, in what order, what choices for answers were, etc. Each of these questions is part of the Questionnaire, and as such the Questionnaire is a separately identifiable Resource, whereas the individual questions are not. (Questionnaire questions can be linked to shared data elements using the Questionnaire.item.definition element.)

In addition to its use as a means for capturing data, Questionnaires can also be useful as a mechanism of defining a standardized 'presentation' of data that might already exist. For example, a peri-natal form or diabetes management form. In this use, the benefit is to expose a large volume of data in a predictable way that can be defined outside the user-interface design of the relevant system. The form might allow data to be edited or might be read-only. In some cases, the QuestionnaireResponse might not be intended to be persisted.

## Operations

HAPI implements the following operations for Questionnaires and QuestionnaireResponses:

* [$questionnaire](/docs/clinical_reasoning/questionnaires.html#questionnaire)
* [$populate](/docs/clinical_reasoning/questionnaires.html#populate)
* [$extract](/docs/clinical_reasoning/questionnaires.html#extract)
* [$package](/docs/clinical_reasoning/questionnaires.html#package)


## Questionnaire

The `StructureDefinition/$questionnaire` [operation]() generates a [Questionnaire](https://www.hl7.org/fhir/questionnaire.html) from a given [StructureDefinition](https://www.hl7.org/fhir/structuredefinition.html).  A question will be created for each core element or extension element found in the StructureDefinition.

### Parameters

The following parameters are supported for the `StructureDefinition/$questionnaire` operation:

| Parameter | Type | Description |
|-----------|------|-------------|
| profile          | StructureDefinition      | The StructureDefinition to base the Questionnaire on. Used when the operation is invoked at the 'type' level. |
| canonical | canonical | The canonical identifier for the StructureDefinition (optionally version-specific). |
| url | uri | Canonical URL of the StructureDefinition when invoked at the resource type level. This is exclusive with the profile and canonical parameters. |
| version | string | Version of the StructureDefinition when invoked at the resource type level. This is exclusive with the profile and canonical parameters. |
| supportedOnly | boolean | If true (default: false), the questionnaire will only include those elements marked as "mustSupport='true'" in the StructureDefinition. |
| requiredOnly | boolean | If true (default: false), the questionnaire will only include those elements marked as "min>0" in the StructureDefinition. |
| subject | string | The subject(s) that is/are the target of the Questionnaire. |
| parameters | Parameters | Any input parameters defined in libraries referenced by the StructureDefinition. |
| useServerData | boolean Whether to use data from the server performing the evaluation. |
| data | Bundle | Data to be made available during CQL evaluation. |
| dataEndpoint | Endpoint | An endpoint to use to access data referenced by retrieve operations in libraries referenced by the StructureDefinition. |
| contentEndpoint | Endpoint | An endpoint to use to access content (i.e. libraries) referenced by the StructureDefinition. |
| terminologyEndpoint | Endpoint | An endpoint to use to access terminology (i.e. valuesets, codesystems, and membership testing) referenced by the StructureDefinition. |

## Populate

The `Questionnaire/$populate` [operation](https://hl7.org/fhir/uv/sdc/OperationDefinition-Questionnaire-populate.html) generates a [QuestionnaireResponse](https://www.hl7.org/fhir/questionnaireresponse.html) based on a specific [Questionnaire](https://www.hl7.org/fhir/questionnaire.html), filling in answers to questions where possible based on information provided as part of the operation or already known by the server about the subject of the Questionnaire. 

### Parameters

The following parameters are supported for the `Questionnaire/$populate` operation:

| Parameter | Type | Description |
|-----------|------|-------------|
| questionnaire | Questionnaire | The Questionnaire to populate. Used when the operation is invoked at the 'type' level. |
| canonical | canonical | The canonical identifier for the Questionnaire (optionally version-specific). |
| url | uri | Canonical URL of the Questionnaire when invoked at the resource type level. This is exclusive with the questionnaire and canonical parameters. |
| version | string | Version of the Questionnaire when invoked at the resource type level. This is exclusive with the questionnaire and canonical parameters. |
| subject | string | The subject(s) that is/are the target of the Questionnaire. |
| parameters | Parameters | Any input parameters defined in libraries referenced by the Questionnaire. |
| useServerData | boolean | Whether to use data from the server performing the evaluation. |
| data | Bundle | Data to be made available during CQL evaluation. |
| dataEndpoint | Endpoint | An endpoint to use to access data referenced by retrieve operations in libraries referenced by the Questionnaire. |
| contentEndpoint | Endpoint | An endpoint to use to access content (i.e. libraries) referenced by the Questionnaire. |
| terminologyEndpoint | Endpoint | An endpoint to use to access terminology (i.e. valuesets, codesystems, and membership testing) referenced by the Questionnaire. |


## Extract

The `QuestionnaireResponse/$extract` [operation](http://hl7.org/fhir/uv/sdc/OperationDefinition-QuestionnaireResponse-extract.html) takes a completed [QuestionnaireResponse](https://www.hl7.org/fhir/questionnaireresponse.html) and converts it to a Bundle of resources by using metadata embedded in the [Questionnaire](https://www.hl7.org/fhir/questionnaire.html) the QuestionnaireResponse is based on. The extracted resources might include Observations, MedicationStatements and other standard FHIR resources which can then be shared and manipulated. When invoking the $extract operation, care should be taken that the submitted QuestionnaireResponse is itself valid. If not, the extract operation could fail (with appropriate OperationOutcomes) or, more problematic, might succeed but provide incorrect output.

This implementation allows for both [Observation based](https://hl7.org/fhir/uv/sdc/extraction.html#observation-based-extraction) and [Definition based](https://hl7.org/fhir/uv/sdc/extraction.html#definition-based-extraction) extraction.

### Parameters

The following parameters are supported for the `QuestionnaireResponse/$extract` operation:

| Parameter | Type | Description |
|-----------|------|-------------|
| questionnaire-response | QuestionnaireResponse | The QuestionnaireResponse to extract data from. Used when the operation is invoked at the 'type' level. |
| parameters | Parameters | Any input parameters defined in libraries referenced by the Questionnaire. |
| data | Bundle | Data to be made available during CQL evaluation. |


## Package

The `Questionnaire/$package` [operation](https://build.fhir.org/ig/HL7/crmi-ig/OperationDefinition-crmi-package.html) for [Questionnaire](https://www.hl7.org/fhir/questionnaire.html) will generate a Bundle of resources that includes the Questionnaire as well as any related Library or ValueSet resources which can then be shared. This implementation follows the [CRMI IG](https://build.fhir.org/ig/HL7/crmi-ig/branches/master/index.html) guidance for [packaging artifacts](https://build.fhir.org/ig/HL7/crmi-ig/branches/master/packaging.html).

### Parameters

The following parameters are supported for the `Questionnaire/$package` operation:

| Parameter | Type      | Description |
|-----------|-----------|-------------|
| id        | string    | The logical id of an existing Resource to package on the server. |
| canonical | canonical | A canonical url (optionally version specific) of a Resource to package on the server. |
| url       | uri       | A canonical or artifact reference to a Resource to package on the server. This is exclusive with the canonical parameter. |
| version   | string    | The version of the Resource. This is exclusive with the canonical parameter. | 
| usePut    | boolean   | Determines the type of method returned in the Bundle Entries: POST if False (the default), PUT if True. | 


## Example Questionnaire

```json
{
   "resourceType": "Questionnaire",
   "id": "ASLPA1",
   "meta": {
      "versionId": "1",
      "lastUpdated": "2023-05-09T19:02:10.538-06:00",
      "source": "#jucRbegv3NMJkZ8X"
   },
   "extension": [
      {
         "url": "http://hl7.org/fhir/uv/cpg/StructureDefinition/cpg-knowledgeCapability",
         "valueCode": "shareable"
      },
      {
         "url": "http://hl7.org/fhir/uv/cpg/StructureDefinition/cpg-knowledgeCapability",
         "valueCode": "computable"
      },
      {
         "url": "http://hl7.org/fhir/uv/cpg/StructureDefinition/cpg-knowledgeCapability",
         "valueCode": "publishable"
      },
      {
         "url": "http://hl7.org/fhir/uv/cpg/StructureDefinition/cpg-knowledgeRepresentationLevel",
         "valueCode": "structured"
      },
      {
         "url": "http://hl7.org/fhir/StructureDefinition/cqf-library",
         "valueCanonical": "http://example.org/sdh/dtr/aslp/Library/ASLPDataElements"
      }
   ],
   "url": "http://example.org/sdh/dtr/aslp/Questionnaire/ASLPA1",
   "name": "ASLPA1",
   "title": "ASLP.A1 Adult Sleep Studies",
   "status": "active",
   "experimental": false,
   "description": "Adult Sleep Studies Prior Authorization Form",
   "useContext": [
      {
         "code": {
            "system": "http://terminology.hl7.org/CodeSystem/usage-context-type",
            "code": "task",
            "display": "Workflow Task"
         },
         "valueCodeableConcept": {
            "coding": [
               {
                  "system": "http://fhir.org/guides/nachc/hiv-cds/CodeSystem/activity-codes",
                  "code": "ASLP.A1",
                  "display": "Adult Sleep Studies"
               }
            ]
         }
      }
   ],
   "item": [
      {
         "extension": [
            {
               "url": "http://hl7.org/fhir/uv/sdc/StructureDefinition/sdc-questionnaire-itemPopulationContext",
               "valueExpression": {
                  "language": "text/cql-identifier",
                  "expression": "Sleep Study"
               }
            }
         ],
         "linkId": "0",
         "definition": "http://example.org/sdh/dtr/aslp/StructureDefinition/aslp-sleep-study-order",
         "text": "A sleep study procedure being ordered",
         "type": "group",
         "repeats": true,
         "item": [
            {
               "linkId": "1",
               "definition": "http://example.org/sdh/dtr/aslp/StructureDefinition/aslp-sleep-study-order#ServiceRequest.code",
               "text": "A sleep study procedure being ordered",
               "type": "choice",
               "answerValueSet": "http://example.org/sdh/dtr/aslp/ValueSet/aslp-a1-de1-codes-grouper"
            },
            {
               "linkId": "2",
               "definition": "http://example.org/sdh/dtr/aslp/StructureDefinition/aslp-sleep-study-order#ServiceRequest.occurrence[x]",
               "text": "Date of the procedure",
               "type": "dateTime"
            }
         ]
      },
      {
         "extension": [
            {
               "url": "http://hl7.org/fhir/uv/sdc/StructureDefinition/sdc-questionnaire-initialExpression",
               "valueExpression": {
                  "language": "text/cql-identifier",
                  "expression": "Diagnosis of Obstructive Sleep Apnea"
               }
            }
         ],
         "linkId": "3",
         "definition": "http://example.org/sdh/dtr/aslp/StructureDefinition/aslp-diagnosis-of-obstructive-sleep-apnea#Condition.code",
         "text": "Diagnosis of Obstructive Sleep Apnea",
         "type": "choice",
         "answerValueSet": "http://example.org/sdh/dtr/aslp/ValueSet/aslp-a1-de17"
      },
      {
         "extension": [
            {
               "url": "http://hl7.org/fhir/uv/sdc/StructureDefinition/sdc-questionnaire-initialExpression",
               "valueExpression": {
                  "language": "text/cql-identifier",
                  "expression": "History of Hypertension"
               }
            }
         ],
         "linkId": "4",
         "definition": "http://example.org/sdh/dtr/aslp/StructureDefinition/aslp-history-of-hypertension#Observation.value[x]",
         "text": "History of Hypertension",
         "type": "boolean"
      },
      {
         "extension": [
            {
               "url": "http://hl7.org/fhir/uv/sdc/StructureDefinition/sdc-questionnaire-initialExpression",
               "valueExpression": {
                  "language": "text/cql-identifier",
                  "expression": "History of Diabetes"
               }
            }
         ],
         "linkId": "5",
         "definition": "http://example.org/sdh/dtr/aslp/StructureDefinition/aslp-history-of-diabetes#Observation.value[x]",
         "text": "History of Diabetes",
         "type": "boolean"
      },
      {
         "extension": [
            {
               "url": "http://hl7.org/fhir/uv/sdc/StructureDefinition/sdc-questionnaire-initialExpression",
               "valueExpression": {
                  "language": "text/cql-identifier",
                  "expression": "Neck Circumference"
               }
            }
         ],
         "linkId": "6",
         "definition": "http://example.org/sdh/dtr/aslp/StructureDefinition/aslp-height#Observation.value[x]",
         "text": "Neck circumference (in inches)",
         "type": "quantity"
      },
      {
         "extension": [
            {
               "url": "http://hl7.org/fhir/uv/sdc/StructureDefinition/sdc-questionnaire-initialExpression",
               "valueExpression": {
                  "language": "text/cql-identifier",
                  "expression": "Height"
               }
            }
         ],
         "linkId": "7",
         "definition": "http://example.org/sdh/dtr/aslp/StructureDefinition/aslp-height#Observation.value[x]",
         "text": "Height (in inches)",
         "type": "quantity"
      },
      {
         "extension": [
            {
               "url": "http://hl7.org/fhir/uv/sdc/StructureDefinition/sdc-questionnaire-initialExpression",
               "valueExpression": {
                  "language": "text/cql-identifier",
                  "expression": "Weight"
               }
            }
         ],
         "linkId": "8",
         "definition": "http://example.org/sdh/dtr/aslp/StructureDefinition/aslp-weight#Observation.value[x]",
         "text": "Weight (in pounds)",
         "type": "quantity"
      },
      {
         "extension": [
            {
               "url": "http://hl7.org/fhir/uv/sdc/StructureDefinition/sdc-questionnaire-initialExpression",
               "valueExpression": {
                  "language": "text/cql-identifier",
                  "expression": "BMI"
               }
            }
         ],
         "linkId": "9",
         "definition": "http://example.org/sdh/dtr/aslp/StructureDefinition/aslp-bmi#Observation.value[x]",
         "text": "Body mass index (BMI)",
         "type": "quantity"
      }
   ]
}
```

## Example QuestionnaireResponse

```json
{
   "resourceType": "QuestionnaireResponse",
   "id": "ASLPA1-positive-response",
   "extension": [
      {
         "url": "http://hl7.org/fhir/us/davinci-dtr/StructureDefinition/dtr-questionnaireresponse-questionnaire",
         "valueReference": {
            "reference": "#ASLPA1-positive"
         }
      }
   ],
   "questionnaire": "http://example.org/sdh/dtr/aslp/Questionnaire/ASLPA1",
   "status": "in-progress",
   "subject": {
      "reference": "Patient/positive"
   },
   "item": [
      {
         "linkId": "0",
         "definition": "http://example.org/sdh/dtr/aslp/StructureDefinition/aslp-sleep-study-order",
         "text": "A sleep study procedure being ordered",
         "item": [
            {
               "extension": [
                  {
                     "url": "http://hl7.org/fhir/StructureDefinition/questionnaireresponse-author",
                     "valueReference": {
                        "reference": "http://cqframework.org/fhir/Device/clinical-quality-language"
                     }
                  }
               ],
               "linkId": "1",
               "definition": "http://example.org/sdh/dtr/aslp/StructureDefinition/aslp-sleep-study-order#ServiceRequest.code",
               "text": "A sleep study procedure being ordered",
               "answer": [
                  {
                     "valueCoding": {
                        "system": "http://example.org/sdh/dtr/aslp/CodeSystem/aslp-codes",
                        "code": "ASLP.A1.DE2",
                        "display": "Home sleep apnea testing (HSAT)"
                     }
                  }
               ]
            },
            {
               "extension": [
                  {
                     "url": "http://hl7.org/fhir/StructureDefinition/questionnaireresponse-author",
                     "valueReference": {
                        "reference": "http://cqframework.org/fhir/Device/clinical-quality-language"
                     }
                  }
               ],
               "linkId": "2",
               "definition": "http://example.org/sdh/dtr/aslp/StructureDefinition/aslp-sleep-study-order#ServiceRequest.occurrence[x]",
               "text": "Date of the procedure",
               "answer": [
                  {
                     "valueDateTime": "2023-04-10T08:00:00.000Z"
                  }
               ]
            }
         ]
      },
      {
         "linkId": "0",
         "definition": "http://example.org/sdh/dtr/aslp/StructureDefinition/aslp-sleep-study-order",
         "text": "A sleep study procedure being ordered",
         "item": [
            {
               "extension": [
                  {
                     "url": "http://hl7.org/fhir/StructureDefinition/questionnaireresponse-author",
                     "valueReference": {
                        "reference": "http://cqframework.org/fhir/Device/clinical-quality-language"
                     }
                  }
               ],
               "linkId": "1",
               "definition": "http://example.org/sdh/dtr/aslp/StructureDefinition/aslp-sleep-study-order#ServiceRequest.code",
               "text": "A sleep study procedure being ordered",
               "answer": [
                  {
                     "valueCoding": {
                        "system": "http://example.org/sdh/dtr/aslp/CodeSystem/aslp-codes",
                        "code": "ASLP.A1.DE14",
                        "display": "Artificial intelligence (AI)"
                     }
                  }
               ]
            },
            {
               "extension": [
                  {
                     "url": "http://hl7.org/fhir/StructureDefinition/questionnaireresponse-author",
                     "valueReference": {
                        "reference": "http://cqframework.org/fhir/Device/clinical-quality-language"
                     }
                  }
               ],
               "linkId": "2",
               "definition": "http://example.org/sdh/dtr/aslp/StructureDefinition/aslp-sleep-study-order#ServiceRequest.occurrence[x]",
               "text": "Date of the procedure",
               "answer": [
                  {
                     "valueDateTime": "2023-04-15T08:00:00.000Z"
                  }
               ]
            }
         ]
      },
      {
         "extension": [
            {
               "url": "http://hl7.org/fhir/StructureDefinition/questionnaireresponse-author",
               "valueReference": {
                  "reference": "http://cqframework.org/fhir/Device/clinical-quality-language"
               }
            }
         ],
         "linkId": "3",
         "definition": "http://example.org/sdh/dtr/aslp/StructureDefinition/aslp-diagnosis-of-obstructive-sleep-apnea#Condition.code",
         "text": "Diagnosis of Obstructive Sleep Apnea",
         "answer": [
            {
               "valueCoding": {
                  "system": "http://example.org/sdh/dtr/aslp/CodeSystem/aslp-codes",
                  "code": "ASLP.A1.DE17",
                  "display": "Obstructive sleep apnea (OSA)"
               }
            }
         ]
      },
      {
         "extension": [
            {
               "url": "http://hl7.org/fhir/StructureDefinition/questionnaireresponse-author",
               "valueReference": {
                  "reference": "http://cqframework.org/fhir/Device/clinical-quality-language"
               }
            }
         ],
         "linkId": "4",
         "definition": "http://example.org/sdh/dtr/aslp/StructureDefinition/aslp-history-of-hypertension#Observation.value[x]",
         "text": "History of Hypertension",
         "answer": [
            {
               "valueBoolean": true
            }
         ]
      },
      {
         "extension": [
            {
               "url": "http://hl7.org/fhir/StructureDefinition/questionnaireresponse-author",
               "valueReference": {
                  "reference": "http://cqframework.org/fhir/Device/clinical-quality-language"
               }
            }
         ],
         "linkId": "5",
         "definition": "http://example.org/sdh/dtr/aslp/StructureDefinition/aslp-history-of-diabetes#Observation.value[x]",
         "text": "History of Diabetes",
         "answer": [
            {
               "valueBoolean": true
            }
         ]
      },
      {
         "extension": [
            {
               "url": "http://hl7.org/fhir/StructureDefinition/questionnaireresponse-author",
               "valueReference": {
                  "reference": "http://cqframework.org/fhir/Device/clinical-quality-language"
               }
            }
         ],
         "linkId": "6",
         "definition": "http://example.org/sdh/dtr/aslp/StructureDefinition/aslp-height#Observation.value[x]",
         "text": "Neck circumference (in inches)",
         "answer": [
            {
               "valueQuantity": {
                  "value": 16,
                  "unit": "[in_i]",
                  "system": "http://unitsofmeasure.org",
                  "code": "[in_i]"
               }
            }
         ]
      },
      {
         "extension": [
            {
               "url": "http://hl7.org/fhir/StructureDefinition/questionnaireresponse-author",
               "valueReference": {
                  "reference": "http://cqframework.org/fhir/Device/clinical-quality-language"
               }
            }
         ],
         "linkId": "7",
         "definition": "http://example.org/sdh/dtr/aslp/StructureDefinition/aslp-height#Observation.value[x]",
         "text": "Height (in inches)",
         "answer": [
            {
               "valueQuantity": {
                  "value": 69,
                  "unit": "[in_i]",
                  "system": "http://unitsofmeasure.org",
                  "code": "[in_i]"
               }
            }
         ]
      },
      {
         "extension": [
            {
               "url": "http://hl7.org/fhir/StructureDefinition/questionnaireresponse-author",
               "valueReference": {
                  "reference": "http://cqframework.org/fhir/Device/clinical-quality-language"
               }
            }
         ],
         "linkId": "8",
         "definition": "http://example.org/sdh/dtr/aslp/StructureDefinition/aslp-weight#Observation.value[x]",
         "text": "Weight (in pounds)",
         "answer": [
            {
               "valueQuantity": {
                  "value": 185,
                  "unit": "[lb_av]",
                  "system": "http://unitsofmeasure.org",
                  "code": "[lb_av]"
               }
            }
         ]
      },
      {
         "extension": [
            {
               "url": "http://hl7.org/fhir/StructureDefinition/questionnaireresponse-author",
               "valueReference": {
                  "reference": "http://cqframework.org/fhir/Device/clinical-quality-language"
               }
            }
         ],
         "linkId": "9",
         "definition": "http://example.org/sdh/dtr/aslp/StructureDefinition/aslp-bmi#Observation.value[x]",
         "text": "Body mass index (BMI)",
         "answer": [
            {
               "valueQuantity": {
                  "value": 16.2,
                  "unit": "kg/m2",
                  "system": "http://unitsofmeasure.org",
                  "code": "kg/m2"
               }
            }
         ]
      }
   ]
}
```
