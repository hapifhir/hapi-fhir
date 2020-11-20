library COL_FHIR version '1.0.1'

/*
Colorectal Cancer Screening (COL)
*/

using FHIR version '3.0.0'

include FHIRHelpers version '3.0.0' called FHIRHelpers
include NCQACommon_FHIR version '3.10.000' called Common
include Participation_FHIR version '1.0.0'

/*
Description
The percentage of members 50-75 years of age who had appropriate screening for colorectal cancer.
*/

/*
It is an outstanding question whether implementers would have an easier time dealing
with single code system value sets for retrieval of data, or if the grouping value sets
are easier to deal with. This mesaure is modified to take the approach of unioning
the results in CQL, where the BCS and CCS measures take the grouping value set
approach.
*/

valueset "Colonoscopy Value Set": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113883.3.464.1004.1064' // Grouper
//valueset "Colonoscopy Value Set (CPT)": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113883.3.464.1004.1064.29' // CPT
//valueset "Colonoscopy Value Set (HCPCS)": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113883.3.464.1004.1064.30' // HCPCS
//valueset "Colonoscopy Value Set (ICD9)": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113883.3.464.1004.1064.31' // ICD9CMProc

valueset "Colorectal Cancer Value Set": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113883.3.464.1004.1065' // Grouper
//valueset "Colorectal Cancer Value Set (ICD10)": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113883.3.464.1004.1065.32' // ICD10CM
//valueset "Colorectal Cancer Value Set (ICD9)": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113883.3.464.1004.1065.33' // ICD9CM

valueset "CT Colonography Value Set": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113883.3.464.1004.1421' // Grouper
//valueset "CT Colonography Value Set (CPT)": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113883.3.464.1004.1421.34' // CPT

valueset "FIT-DNA Value Set": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113883.3.464.1004.1420' // Grouper
//valueset "FIT-DNA Value Set (CPT)": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113883.3.464.1004.1420.35' // CPT
//valueset "FIT-DNA Value Set (HCPCS)": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113883.3.464.1004.1420.36' // HCPCS
//valueset "FIT-DNA Value Set (LOINC)": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113883.3.464.1004.1420.37' // LOINC

valueset "Flexible Sigmoidoscopy Value Set": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113883.3.464.1004.1102' // Grouper
//valueset "Flexible Sigmoidoscopy Value Set (CPT)": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113883.3.464.1004.1102.38' // CPT
//valueset "Flexible Sigmoidoscopy Value Set (HCPCS)": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113883.3.464.1004.1102.39' // HCPCS
//valueset "Flexible Sigmoidoscopy Value Set (ICD9)": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113883.3.464.1004.1102.40' // ICD9CMProc

valueset "FOBT Value Set": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113883.3.464.1004.1093' // Grouper
//valueset "FOBT Value Set (CPT)": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113883.3.464.1004.1093.41' // CPT
//valueset "FOBT Value Set (HCPCS)": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113883.3.464.1004.1093.42' // HCPCS
//valueset "FOBT Value Set (ICD9)": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113883.3.464.1004.1093.43' // ICD9CMProc

valueset "Total Colectomy Value Set": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113883.3.464.1004.1250' // Grouper
//valueset "Total Colectomy Value Set (CPT)": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113883.3.464.1004.1250.44' // CPT
//valueset "Total Colectomy Value Set (ICD10)": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113883.3.464.1004.1250.45' // ICD10PCS
//valueset "Total Colectomy Value Set (ICD9)": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113883.3.464.1004.1250.46' // ICD9CMProc

/*
This library has an explicit parameter which is the product line.
Recognized normal arguments are {'commercial', 'medicaid', 'medicare'}.
If one of these normal arguments is given, the patient will only be
considered to be in the Initial Population if they have an appropriate
continuous enrollment in that kind of medical plan.
If instead a null argument is given, their enrollment status will have no
effect on whether they are considered to be in the Initial Population.
If instead some other argument is given (an unrecognized plan type),
the patient will unconditionally NOT be in the Initial Population.
*/

parameter "Product Line" String

/*
This library has an explicit parameter which is the measurement year.
While the actual parameter's type accepts all intervals, this library
expects it will only be given arguments corresponding exactly to one whole
calendar year, and it will not behave properly otherwise; 2017 for example:
Interval[DateTime(2017,1,1,0,0,0,0), DateTime(2018,1,1,0,0,0,0))
*/

parameter "Measurement Period" Interval<DateTime>

define "Lookback Interval Two More Years":
  Interval[start of "Measurement Period" - 2 years, end of "Measurement Period")

define "Lookback Interval Four More Years":
  Interval[start of "Measurement Period" - 4 years, end of "Measurement Period")

define "Lookback Interval Nine More Years":
  Interval[start of "Measurement Period" - 9 years, end of "Measurement Period")

/*
This library evaluates with respect to exactly 1 candidate patient at a time,
that patient being given by the special context parameter Patient.
*/

context Patient

/*
Initial Population
Product lines -- Commercial, Medicaid, Medicare (report each product line separately).
*/

define "Initial Population":
  "Is Age 51 to 75 at End"

define "Is Age 51 to 75 at End":
  Common.CalendarAgeInYearsAt(FHIRHelpers.ToDate(Patient.birthDate), end of "Measurement Period") between 51 and 75

define "Denominator":
  true

define "Numerator":
  "Is Colorectal Cancer Screening"
    and not "Denominator Exclusion"

define "Is Colorectal Cancer Screening":
  "Is Fecal Occult Blood Test In Last Year"
    or "Is Flexible Sigmoidoscopy In Last Five Years"
    or "Is Colonoscopy In Last Ten Years"
    or "Is CT Colonography In Last Five Years"
    or "Is FIT-DNA Test In Last Three Years"

/*
define "FOBT Observations":
  [Observation: "FOBT Value Set (CPT)"]
    union [Observation: "FOBT Value Set (HCPCS)"]
    union [Observation: "FOBT Value Set (ICD9)"]
*/

define "FOBT Observations":
  [Observation: "FOBT Value Set"]

define "Is Fecal Occult Blood Test In Last Year":
  exists(
    "FOBT Observations" Obs
      where Obs.status in { 'final', 'amended' }
        and Obs.effective in day of "Measurement Period"
  )

/*
define "Flexible Sigmoidoscopy Reports":
  [DiagnosticReport: "Flexible Sigmoidoscopy Value Set (CPT)"]
    union [DiagnosticReport: "Flexible Sigmoidoscopy Value Set (HCPCS)"]
    union [DiagnosticReport: "Flexible Sigmoidoscopy Value Set (ICD9)"]
*/

define "Flexible Sigmoidoscopy Reports":
  [DiagnosticReport: "Flexible Sigmoidoscopy Value Set"]

define "Is Flexible Sigmoidoscopy In Last Five Years":
  exists(
    "Flexible Sigmoidoscopy Reports" DiagRep
      where DiagRep.status in { 'preliminary', 'final', 'amended', 'corrected', 'appended' }
        and DiagRep.effective overlaps day of "Lookback Interval Four More Years"
  )

/*
define "Colonoscopy Reports":
  [DiagnosticReport: "Colonoscopy Value Set (CPT)"]
    union [DiagnosticReport: "Colonoscopy Value Set (HCPCS)"]
    union [DiagnosticReport: "Colonoscopy Value Set (ICD9)"]
*/

define "Colonoscopy Reports":
  [DiagnosticReport: "Colonoscopy Value Set"]

define "Is Colonoscopy In Last Ten Years":
  exists(
    "Colonoscopy Reports" DiagRep
      where DiagRep.status in { 'preliminary', 'final', 'amended', 'corrected', 'appended' }
        and DiagRep.effective overlaps day of "Lookback Interval Nine More Years"
  )

define "Is CT Colonography In Last Five Years":
  exists(
    [DiagnosticReport: "CT Colonography Value Set"] DiagRep
      where DiagRep.status in { 'preliminary', 'final', 'amended', 'corrected', 'appended' }
        and DiagRep.effective overlaps day of "Lookback Interval Four More Years"
  )

/*
define "FIT-DNA Observations":
  [Observation: "FIT-DNA Value Set (CPT)"]
    union [Observation: "FIT-DNA Value Set (HCPCS)"]
    union [Observation: "FIT-DNA Value Set (LOINC)"]
*/

define "FIT-DNA Observations":
  [Observation: "FIT-DNA Value Set"]

define "Is FIT-DNA Test In Last Three Years":
  exists(
    "FIT-DNA Observations" Obs
      where Obs.status in { 'final', 'amended' }
        and Obs.effective in day of "Lookback Interval Two More Years"
  )

define "Denominator Exclusion":
  "Is Colorectal Cancer"
      or "Is Total Colectomy"

/*
define "Colorectal Cancer Conditions":
  [Condition: "Colorectal Cancer Value Set (ICD10)"]
    union [Condition: "Colorectal Cancer Value Set (ICD9)"]
*/

define "Colorectal Cancer Conditions":
  [Condition: "Colorectal Cancer Value Set"]

define "Is Colorectal Cancer":
  exists(
    "Colorectal Cancer Conditions" Cond
      where Cond.verificationStatus = 'confirmed'
        and Cond.assertedDate before day of end of "Measurement Period"
  )

/*
define "Total Colectomy Procedures":
  [Procedure: "Total Colectomy Value Set (CPT)"]
    union [Procedure: "Total Colectomy Value Set (ICD10)"]
    union [Procedure: "Total Colectomy Value Set (ICD9)"]
*/

define "Total Colectomy Procedures":
  [Procedure: "Total Colectomy Value Set"]

define "Is Total Colectomy":
  exists(
    "Total Colectomy Procedures" Proc
      where Proc.status = 'completed'
        and end of Proc.performed before day of end of "Measurement Period"
  )
