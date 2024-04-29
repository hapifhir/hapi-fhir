# Care Gaps

## Overview
A gap in care refers to a discrepancy or gap in a patient's care that has been identified through analysis of their medical records, history, and current health status.
These gaps can include missing or incomplete information, unmet health needs, and opportunities for preventative care or intervention. Identifying and addressing care gaps can help improve the quality of care provided to patients, reduce healthcare costs, and ultimately lead to better health outcomes.

Example: This woman was supposed to have a breast cancer screening this year but did not. Let’s reach out to her and get that scheduled.

A Gaps in Care Report is designed to communicate actual or perceived gaps in care between systems, such as the payer’s system and provider’s EMR. The report provides opportunities for providers to provide missing care and/or to communicate care provision data to payers. The report may also provide information for upcoming care opportunities, prospective gaps.

The gaps in care flow is between a provider and a measurement organization’s system performing analytics.


<a href="/hapi-fhir/docs/images/caregapsflow.png"><img src="/hapi-fhir/docs/images/caregapsflow.png" alt="Care Gaps Flow" style="margin-left: 15px; margin-bottom: 15px;" /></a><sub><sup>Sourced from [Implementation Guide](http://hl7.org/fhir/us/davinci-deqm/2023Jan/gaps-in-care-reporting.html)</sup></sub>

The Gaps in Care Reporting uses the [DEQM Individual MeasureReport Profile](http://hl7.org/fhir/us/davinci-deqm/2023Jan/StructureDefinition-indv-measurereport-deqm.html). This allows the Gaps in Care Reporting to use the same machinery as the Individual Reporting to calculate measures and represent the results of individual calculation.

The following resources are used in the Gaps in Care Reporting Scenario:

| Report Type   |              Profile Name               | Link to Profile                                                                                                                   |
|---------------|:---------------------------------------:|-----------------------------------------------------------------------------------------------------------------------------------|
| Bundle        |    DEQM Gaps In Care Bundle Profile     | [DEQM Gaps In Care Bundle Profile](http://hl7.org/fhir/us/davinci-deqm/2023Jan/StructureDefinition-gaps-bundle-deqm.html)           |
| Composition   |  DEQM Gaps In Care Composition Profile  | [DEQM Gaps In Care Composition Profile](http://hl7.org/fhir/us/davinci-deqm/2023Jan/StructureDefinition-gaps-composition-deqm.html) |
| DetectedIssue | DEQM Gaps In Care DetectedIssue Profile | [DEQM Gaps In Care Detected Profile](http://hl7.org/fhir/us/davinci-deqm/2023Jan/StructureDefinition-gaps-detectedissue-deqm.html)  |
| Group         |     DEQM Gaps In Care Group Profile     | [DEQM Gaps In Care Group Profile](http://hl7.org/fhir/us/davinci-deqm/2023Jan/StructureDefinition-gaps-group-deqm.html)            |
| MeasureReport | DEQM Gaps In Care MeasureReport Profile | [DEQM Gaps In Care MeasureReport Profile](http://hl7.org/fhir/us/davinci-deqm/2023Jan/StructureDefinition-indv-measurereport-deqm.html)      |

## Gaps in Care Reporting
[Gaps through period](http://hl7.org/fhir/us/davinci-deqm/2023Jan/index.html#glossary) is the time period defined by a Client for running the Gaps in Care Report. 
* When the [gaps through period](http://hl7.org/fhir/us/davinci-deqm/2023Jan/index.html#glossary) ends on a date that is in the future, the Gaps in Care Reporting is said to look for care gaps prospectively. In this scenario, it provides providers with opportunities to assess anticipated [open gaps](http://build.fhir.org/ig/HL7/davinci-deqm/index.html#glossary) and take proper actions to close the gaps.
* When the [gaps through period](http://hl7.org/fhir/us/davinci-deqm/2023Jan/index.html#glossary) ends on a date that is in the past, the Gaps in Care Reporting is said to look for care gaps retrospectively. In the retrospective scenario, identified [open gaps](http://build.fhir.org/ig/HL7/davinci-deqm/index.html#glossary) can no longer be acted upon to meet the quality measure.

| Use Case      |              care-gaps Operation               | Gaps Through Period Start Date                                                                                                        | Gaps Through Period End Date | Report Calculated Date | Colorectal Cancer Screening - Colonoscopy Date | Gaps in Care Report                                                                                                                                                                                                                                                                                           |
|---------------|:---------------------------------------:|---------------------------------------------------------------------------------------------------------------------------------------|------------------------------|------------------------|------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Prospective Use Case | $care-gaps?periodStart=2021-01-01&periodEnd=2021-06-30&subject=Patient/123&measureId=EXM130-7.3.000&status=open-gap | 2021-01-01  | 2021-06-30 | 2021-04-01 | Example: patient had colonoscopy on 2011-05-03 | Returns gaps through 2021-06-30. The Gaps in Care Report indicates the patient has an [open gaps](http://build.fhir.org/ig/HL7/davinci-deqm/index.html#glossary) for the colorectal cancer screening measure. By 2021-06-30, the colonoscopy would be over 10 years.                                          |
| Retrospective Use Case | $care-gaps?periodStart=2020-01-01&periodEnd=2020-12-31&subject=Patient/123&measureId=EXM130-7.3.000&status=open-gap | 2020-01-01| 2020-12-31 | 2021-04-01 | Example: patient had colonoscopy on 2011-05-03 | Returns gaps through 2020-12-31. The Gaps in Care Report indicates the patient has a [closed gaps](http://build.fhir.org/ig/HL7/davinci-deqm/index.html#glossary) for the colorectal cancer screening measure. Since on 2020-12-31, the procedure would have occurred within the specified 10-year timeframe. |

## Operations
Hapi FHIR implements the [$care-gaps](http://hl7.org/fhir/us/davinci-deqm/2023Jan/OperationDefinition-care-gaps.html) operation.

## Care Gaps
The `$care-gaps` operation is used to run a Gaps in Care Report.

### Testing care gaps on Hapi FHIR
Hapi FHIR is integrated with `$care-gaps` operations and following are the steps to identify open gap on sample data following the remediation step to generate a report for closed gap.

All the sample files used below are available on [hapi-fhir](https://github.com/hapifhir/hapi-fhir/tree/master/hapi-fhir-storage-cr/src/test/resources) code base under resources folder.

1. Submit payer content
```bash
POST http://localhost/fhir/ CaregapsColorectalCancerScreeningsFHIR-bundle.json
```
2. Submit payer org data
```bash
POST http://localhost/fhir/ CaregapsAuthorAndReporter.json
```
3. Submit provider data
```bash 
POST http://localhost/fhir/Measure/ColorectalCancerScreeningsFHIR/$submit-data CaregapsPatientData.json
```
4. Provider runs care-gaps operation to identify open gap.
```bash
GET http://localhost/fhir/Measure/$care-gaps?periodStart=2020-01-01&periodEnd=2020-12-31&status=open-gap&status=closed-gap&subject=Patient/end-to-end-EXM130&measureId=ColorectalCancerScreeningsFHIR
```
5. Provider fixes gaps
```bash
POST http://localhost/fhir/Measure/ColorectalCancerScreeningsFHIR/$submit-data CaregapsSubmitDataCloseGap.json
```
6. Provider runs care-gaps operation to identify the gap is closed.
```bash
GET http://localhost/fhir/Measure/$care-gaps?periodStart=2020-01-01&periodEnd=2020-12-31&status=open-gap&status=closed-gap&subject=Patient/end-to-end-EXM130&measureId=ColorectalCancerScreeningsFHIR
```

