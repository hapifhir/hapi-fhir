# CQL Measure

## Introduction

The FHIR Clinical Reasoning Module defines the [Measure resource](https://www.hl7.org/fhir/measure.html) and several [associated operations](https://www.hl7.org/fhir/measure-operations.html). The Measure Resource represents a structured, computable definition of a health-related measure such as a clinical quality measure, public health indicator, or population analytics measure. These Measures can then be used for reporting, analytics, and data-exchange purposes.

Electronic Clinical Quality Measures (eCQMs) in FHIR are represented as a FHIR Measure resource containing metadata and terminology, a population criteria section, and at least one FHIR Library resource containing a data criteria section as well as the logic used to define the population criteria. The population criteria section typically contains initial population criteria, denominator criteria, and numerator criteria sub-components, among others. This is elaborated upon in greater detail in the [CQF Measures IG](http://hl7.org/fhir/us/cqfmeasures). An example of an eCQM as defined in FHIR looks like:

```json
{
  "resourceType" : "Measure",
  "library" : [
    "http://hl7.org/fhir/us/cqfmeasures/Library/EXMLogic"
  ],
  "group" : [
    {
      "population" : [
        {
          "code" : {
            "coding" : [
              {
                "code" : "initial-population"
              }
            ]
          },
          "criteria" : {
            "language" : "text/cql.identifier",
            "expression" : "Initial Population"
          }
        },
        {
          "code" : {
            "coding" : [
              {
                "code" : "numerator"
              }
            ]
          },
          "criteria" : {
            "language" : "text/cql.identifier",
            "expression" : "Numerator"
          }
        },
        {
          "code" : {
            "coding" : [
              {
                "code" : "denominator"
              }
            ]
          },
          "criteria" : {
            "language" : "text/cql.identifier",
            "expression" : "Denominator"
          }
        }
      ]
    }
  ]
}

```

Measures are then scored according the whether a subjects (or subjects) are members of the various populations.

For example, a Measure for Breast Cancer screening might define an Initial Population (via CQL expressions) of "all women", a Denominator of "women over 35", and a Numerator of "women over 35 who have had breast cancer screenings in the past year". If the Measure is evaluated against a population of 100 women, 50 are over 35, and of those 25 have had breast cancer screenings in the past year, the final score would be 50%<sup>1</sup> (total number in numerator / total number in the denominator).

1. There are several methods for scoring Measures, this is meant only as an example.

## Operations

HAPI implements the [$evaluate-measure](https://www.hl7.org/fhir/operation-measure-evaluate-measure.html) operation. Support for additional operations is planned.

## Evaluate Measure

The `$evaluate-measure` operation is used to execute a Measure as specified by the relevant FHIR Resources against a subject or set of subjects. This implementation currently focuses primarily on supporting the narrower evaluation requirements defined by the [CQF Measures IG](http://hl7.org/fhir/us/cqfmeasures). Some support for extensions defined by other IGs is included as well, and the implementation aims to support a wider range of functionality in the future.

### Example Measure

Several example Measures are available in the [ecqm-content-r4](https://github.com/cqframework/ecqm-content-r4) IG. Full Bundles with all the required supporting resources are available [here](https://github.com/cqframework/ecqm-content-r4/tree/master/bundles/measure). You can download a Bundle and load it on your server as a transaction:

```bash
POST http://your-server-base/fhir BreastCancerScreeningFHIR-bundle.json
```

These Bundles also include example Patient clinical data so once posted Measure evaluation can be invoked with:

```bash
GET http://your-server-base/fhir/Measure/BreastCancerScreeningFHIR/$evaluate-measure?periodStart=2019-01-01&periodEnd=2019-12-31&subject=numerator&reportType=subject
```

### Measure Features

The FHIR Measure specification defines several different types of Measures and various parameters for controlling the Measure evaluation. This section describes the features supported by HAPI.

#### Reporting Period

The `periodStart` and `periodEnd` parameters are used to control the Reporting Period for which a report is generated. This corresponds to `Measurement Period` defined in the CQL logic, as defined by the conformance requirements in the CQF Measures IG. Both `periodStart` and `periodEnd` must be used or neither must be used.

If neither are used the default reporting period specified in the CQL logic is used, as shown here

```cql
parameter "Measurement Period" Interval<DateTime>
  default Interval[@2019-01-01T00:00:00.0, @2020-01-01T00:00:00.0)
```

If neither are used and there is no default reporting period in the CQL logic an error is thrown.

A request using `periodStart` and `periodEnd` looks like:

```bash
GET fhir/Measure/<MeasureId>/$evaluate-measure?periodStart=2019-01-01&periodEnd=2019-12-31
```

`periodStart` and `periodEnd` support Dates (YYYY, YYYY-MM, or YYYY-MM-DD) and DateTimes (YYYY-MM-DDThh:mm:ss+zz:zz)

#### Report Types

Measure report types determine what data is returned from the evaluation. This is controlled with the `reportType` parameter on the $evaluate-measure Operation

| Report Type  |     Supported      | Description                                                                                                    |
| ------------ | :----------------: | -------------------------------------------------------------------------------------------------------------- |
| subject      | :white_check_mark: | Measure report for a single subject (e.g. one patient). Includes additional detail, such as evaluatedResources |
| subject-list | :white_check_mark: | Measure report including the list of subjects in each population (e.g. all the patients in the "numerator")    |
| population   | :white_check_mark: | Summary measure report for a population                                                                        |

NOTE: There's an open issue on the FHIR specification to align these names to the MeasureReportType value set.

A request using `reportType` looks like:

```bash
GET fhir/Measure/<MeasureId>/$evaluate-measure?reportType=subject-list
```

#### Subject Types

The subject of a measure evaluation is controlled with the `subject` (R4+) and `patient` (DSTU3) operation parameters. Currently the only subject type supported by HAPI is Patient. This means that all Measure evaluation and reporting happens with respect to a Patient or set of Patient resources.

| Subject Type      |      Supported       | Description       |
| ----------------- | :------------------: | ----------------- |
| Patient           |  :white_check_mark:  | A Patient         |
| Practitioner      | :white_large_square: | A Practitioner    |
| Organization      | :white_large_square: | An Organization   |
| Location          | :white_large_square: | A Location        |
| Device            | :white_large_square: | A Device          |
| Group<sup>1</sup> | :white_large_square: | A set of subjects |

1. See next section

A request using `subject` looks like:

```bash
GET fhir/Measure/<MeasureId>/$evaluate-measure?subject=Patient/123
```

##### Selecting a set of Patients

The set of Patients used for Measure evaluation is controlled with the `subject` (R4+) or `patient` (DSTU3), and `practitioner` parameters. The two parameters are mutually exclusive.

| Parameter                                             |      Supported       | Description                                                             |
| ----------------------------------------------------- | :------------------: | ----------------------------------------------------------------------- |
| Not specified                                         |  :white_check_mark:  | All Patients on the server                                              |
| `subject=XXX` or `subject=Patient/XXX`                |  :white_check_mark:  | A single Patient                                                        |
| `practitioner=XXX` or `practitioner=Practitioner/XXX` |  :white_check_mark:  | All Patients whose `generalPractitioner` is the referenced Practitioner |
| `subject=Group/XXX`<sup>1</sup>                       | :white_large_square: | A Group containing subjects                                             |
| `subject=XXX` AND `practitioner=XXX`                  |         :x:          | Not a valid combination                                                 |

1. Referencing a Group of Patients as the subject is defined in the ATR IG and is on the roadmap. This will allow much more control over which Patients are included in the evaluated set.

A request using `practitioner` looks like:

```bash
GET fhir/Measure/<MeasureId>/$evaluate-measure?practitioner=Practitioner/XYZ
```

#### ReportType, Subject, Practitioner Matrix

The following table shows the combinations of the `subject` (or `patient`), `practitioner` and `reportType` parameters that are valid

|                  |      subject reportType       |           subject-list reportType            |            population reportType             |
| ---------------- | :----------------: | :-------------------------------: | :-------------------------------: |
| subject parameter      | :white_check_mark: | :white_check_mark: <sup>1,2</sup> | :white_check_mark: <sup>1,2</sup> |
| practitioner parameter |  :x:<sup>3</sup>   |        :white_check_mark:         |        :white_check_mark:         |

1. Including the subject parameter restricts the Measure evaluation to a single Patient. Omit the `subject` (or `patient`) parameter to get report for multiple Patients. The subject-list and population report types have less detail than a subject report.
2. A Group `subject` with a subject-list or population `reportType` will be a valid combination once Group support is implemented.
3. A practitioner have may zero, one, or many patients so a practitioner report always assumes a set.

#### Scoring Methods

The Measure scoring method determines how a Measure score is calculated. It is set with the [scoring](https://www.hl7.org/fhir/measure-definitions.html#Measure.scoring) element on the Measure resource.

The HAPI implementation conforms to the requirements defined by the CQF Measures IG. A more detailed description of each scoring method is linked in the table below.

| Scoring Method      |      Supported       | Description                                                                                                            |
| ------------------- | :------------------: | ---------------------------------------------------------------------------------------------------------------------- |
| proportion          |  :white_check_mark:  | [Proportion Measures](https://build.fhir.org/ig/HL7/cqf-measures/measure-conformance.html#proportion-measures)         |
| ratio               |  :white_check_mark:  | [Ratio Measures](https://build.fhir.org/ig/HL7/cqf-measures/measure-conformance.html#ratio-measures)                   |
| continuous-variable |  :white_check_mark:  | [Continuous Variable](https://build.fhir.org/ig/HL7/cqf-measures/measure-conformance.html#continuous-variable-measure) |
| cohort              | :white_check_mark:*  | [Cohort](https://build.fhir.org/ig/HL7/cqf-measures/measure-conformance.html#cohort-definitions)                       |
| composite           | :white_large_square: | See below                                                                                                              |

* The cohort Measure scoring support is partial. The HAPI implementation does not yet return the required Measure observations

An example Measure resource with `scoring` defined looks like:

```json
{
  "resourceType": "Measure",
  "scoring": {
    "coding": [ {
      "system": "http://terminology.hl7.org/CodeSystem/measure-scoring",
      "code": "proportion",
      "display": "Proportion"
    } ]
  }
}
```

##### Composite Scoring

A composite Measure is scored by combining and/or aggregating the results of other Measures. The [compositeScoring](https://www.hl7.org/fhir/measure-definitions.html#Measure.compositeScoring) element is used to control how composite Measures are scored. HAPI does not currently support any composite scoring method.

| Composite Scoring Method |      Supported       | Description                                                                                    |
| ------------------------ | :------------------: | ---------------------------------------------------------------------------------------------- |
| opportunity              | :white_large_square: | Combines Numerators and Denominators for each component Measure                                |
| all-or-nothing           | :white_large_square: | Includes individuals that are in the numerator for all component Measures                      |
| linear                   | :white_large_square: | Gives an individual score based on the number of numerators in which they appear               |
| weighted                 | :white_large_square: | Gives an individual a cored based on a weighted factor for each numerator in which they appear |

#### Populations

The HAPI implementation uses the populations defined by the CQF Measures IG for each scoring type. A matrix of the supported populations is shown in the [Criteria Names](https://build.fhir.org/ig/HL7/cqf-measures/measure-conformance.html#criteria-names) section of the CQF Measures IG.

#### Population Criteria

The logical criteria used for determining each Measure population is defined by the [Measure.group.population.criteria](https://hl7.org/fhir/R4/measure-definitions.html#Measure.group.population.criteria) element. The Measure specification allows population criteria to be defined using FHIR Path, CQL, or other languages as appropriate. The HAPI implementation currently only supports using CQL. The relationship between a Measure Population and CQL is illustrated in the [Population Criteria](https://build.fhir.org/ig/HL7/cqf-measures/measure-conformance.html#population-criteria) section of the CQF Measures IG.

An example Measure resource with a population criteria referencing a CQL identifier looks like:

```json
{
  "resourceType": "Measure",
  "group": [ {
    "population": [ {
      "code": {
        "coding": [ {
          "system": "http://terminology.hl7.org/CodeSystem/measure-population",
          "code": "initial-population",
          "display": "Initial Population"
        } ]
      },
      "criteria": {
        "language": "text/cql.identifier",
        "expression": "Initial Population"
      }
    }]
  }]
}
```

##### Criteria Expression Type

| Expression Type |      Supported       |
| --------------- | :------------------: |
| CQL             |  :white_check_mark:  |
| FHIR Path       | :white_large_square: |

#### Supplemental Data Elements

Supplemental Data Elements are used to report additional information about the subjects that may not be included in the in the Population criteria definitions. For example, it may be of interest to report the gender of all subjects for informational purposes. Supplemental data elements are defined by the [Measure.supplementalData](http://www.hl7.org/fhir/measure-definitions.html#Measure.supplementalData) element, and are reported as Observations in the evaluatedResources of the MeasureReport.

Supplemental Data Elements can be specified as either CQL definitions or FHIR Path expressions.

| Expression Type |      Supported       |
| --------------- | :------------------: |
| CQL             |  :white_check_mark:  |
| FHIR Path       | :white_large_square: |

An example Measure resource with some supplemental data elements set looks like:

```json
{
"resourceType": "Measure",
   "supplementalData": [ {
      "code": {
         "text": "sde-ethnicity"
      },
      "criteria": {
         "language": "text/cql.identifier",
         "expression": "SDE Ethnicity"
      }
   }]
}
```

#### Stratifiers

Stratifiers are used divide Measure populations into segments of interest. For  example, it may be of interest to compare the Measure score between different age groups or genders. Each stratum within a stratification is scored the same way as the overall population. Stratifiers are defined using the [Measure.group.stratifier](http://hl7.org/fhir/R4/measure-definitions.html#Measure.group.stratifier) element.

HAPI does not implement stratifier support but it's on the roadmap.

An example Measure resource with a stratifier set looks like:

```json
{
  "resourceType": "Measure",
  "group": [ {
      "stratifier": [ {
         "code": {
            "text": "Stratum 1"
         },
         "criteria": {
            "language": "text/cql.identifier",
            "expression": "Stratification 1"
         }
      }]
   }]
}
```

##### Stratifier Expression Support

As with Populations and Supplemental Data Elements the criteria used for Stratification may be defined with CQL or FHIR Path.

| Expression Type |      Supported       |
| --------------- | :------------------: |
| CQL             | :white_large_square: |
| FHIR Path       | :white_large_square: |

##### Stratifier Component Support

The Measure specification also supports multi-dimensional stratification, for cases where more than one data element is needed.

| Stratifier Type  |      Supported       |
| ---------------- | :------------------: |
| Single Component | :white_large_square: |
| Multi Component  | :white_large_square: |

#### Evaluated Resources

A FHIR MeasureReport permits referencing the Resources used when evaluating in the [MeasureReport.evaluatedResource](https://www.hl7.org/fhir/measurereport-definitions.html#MeasureReport.evaluatedResource) element. HAPI includes these resources when generating `subject` reports for a single Patient. Evaluated resources for `population` or `subject-list` reports are not included. For large populations this could quickly become an extremely large number of resources.

The evaluated resources will not include every resource on the HAPI server for a given subject. Rather, it includes only the resources that were retrieved from the server by the CQL logic that was evaluated. This corresponds to the data-requirements for a given Measure. As an example, consider the following CQL:

```cql
valueset "Example Value Set" : 'http://fhir.org/example-value-set'

define "Example Observations":
   [Observation : "Example Value Set"]
```

That CQL will only select Observation Resources that have a code in the "Example Value Set". Those Observations will be reported in the Evaluated Resources while any others will not.

#### Last Received On

The `lastReceivedOn` parameter is the date the Measure was evaluated and reported. It is used to limit the number of resources reported in the Measure report for individual reports. It is currently not supported by HAPI.

#### Extensions

A number of extensions to Measure evaluation defined by various IGs are supported. They are described briefly in the table below.

| Extension | Description |
| --------- | ----------- |
| http://hl7.org/fhir/us/cqframework/cqfmeasures/StructureDefinition/cqfm-productLine | Used to evaluate different product lines (e.g. Medicare, Private, etc.) |
| http://hl7.org/fhir/StructureDefinition/cqf-measureInfo | Used to demark a Measure Observation |
| http://hl7.org/fhir/us/davinci-deqm/StructureDefinition/extension-populationReference | Used to specify the population that triggered a particular `evaluatedResource`|

There's not currently a way to configure which extensions are enabled. All supported extensions are always enabled.

## Architecture

Below are a few diagrams that show the overall architecture of Measure evaluation and how it fits into the HAPI FHIR Server.

### Component Diagram

This is a simplified component diagram of the Measure evaluation architecture

![Measure Evaluation Architecture](/hapi-fhir/docs/images/ref_measure_architecture_drawio.svg)

### Sequence Chart

This sequence chart approximates the Measure evaluation logic implemented by HAPI.

![Measure Evaluation Sequence Chart](/hapi-fhir/docs/images/measure_evaluation_sequence.png)

## FAQs

Q: I get an error saying HAPI can't locate my library, and I've verified it's on the server.

A: HAPI follows the [Library conformance requirements](https://build.fhir.org/ig/HL7/cqf-measures/measure-conformance.html#conformance-requirement-3-1) defined by the CQF Measures IG, meaning the Library must have a `logic-library` type, the name and versions of the FHIR Library and CQL Library must match, and the url of the Library must end in the name of the Library.

FHIR Libraries generated from CQL via the IG Publisher follow these requirements automatically.

Q: Does HAPI support partitions for evaluation?

A: Yes, though the Measure and associated Resources must be in the same partition as the clinical data being used.

## Roadmap

* Complete cohort implementation
* Support for stratifiers
* Support for Group subjects
* Support for FHIRPath expressions in Stratifiers, Supplemental Data Elements, and Population Criteria
* `$data-requirements`, `$collect-data`, `$submit-data`, and `$care-gaps` operations
* Support for more extensions defined in the CQF Measures, CPG, and ATR IGs
