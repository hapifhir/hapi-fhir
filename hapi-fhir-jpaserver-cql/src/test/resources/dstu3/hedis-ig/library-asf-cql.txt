library ASF_FHIR version '1.0.0'

/*
Unhealthy Alcohol Use Screening and Follow-up (ASF)
*/

using FHIR version '3.0.0'

include FHIRHelpers version '3.0.0' called FHIRHelpers

/*
Measure Description
The percentage of members 18 years of age and older who were screened for
unhealthy alcohol use using a standardized tool and, if screened
positive, received appropriate follow-up care. Two rates are reported.
1. Unhealthy Alcohol Use Screening. The percentage of members who had a
systematic screening for unhealthy alcohol use.
2. Counseling or Other Follow-up. The percentage of members who screened
positive for unhealthy alcohol use and received brief counseling or
other follow-up care within 2 months of a positive screening.
*/

codesystem "LOINC": 'http://loinc.org'
codesystem "CQFramework": 'http://cqframework.info/codesystem/placeholder'

// Update

valueset "Alcohol Counseling and Treatment": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113883.3.464.1004.1437'
valueset "Alcohol Screening": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113883.3.464.1004.1337'
valueset "Alcohol use disorder": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113883.3.464.1004.1339'
valueset "Dementia": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113883.3.464.1004.1074'

code "Alcoholic drinks per drinking day - Reported": '11287-0' from "LOINC"
code "AUDIT Total Score (in points)": '75624-7' from "LOINC"
code "AUDIT-C Total Score (in points)": '75626-2' from "LOINC"

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

/*
This library evaluates with respect to exactly 1 candidate patient at a time,
that patient being given by the special context parameter Patient.
*/

context Patient

define "Initial Population":
	AgeInYearsAt(start of "Measurement Period") >= 18

/*
Exclusions
*/

define "Denominator Exclusion":
  exists (
    [Condition: "Alcohol use disorder"] AlcoholUse
      where AlcoholUse.clinicalStatus in { 'active', 'recurrence' }
        and AlcoholUse.assertedDate during day of Interval[start of "Measurement Period" - 1 year, end of "Measurement Period"]
	)
    or exists (
      [Condition: "Dementia"] D
        where D.clinicalStatus in { 'active', 'recurrence' }
          and D.assertedDate during day of Interval[start of "Measurement Period", end of "Measurement Period" - 60 days]
    )

/*
Denominators and Numerators
*/

// Unhealthy Alcohol Use Screening

define "Denominator 1":
  // "Initial Population"
  true

// Unhealthy Alcohol Use Screening

define "Numerator 1":
  // "Initial Population"
  exists ( "AUDIT-C Assessment" )
    or exists ( "AUDIT Assessment" )
    or (
      "Patient is Male"
        and exists ( "Five or more drinks per day Assessment" )
    )
    or (
      "Patient is Female"
        and (
          exists ( "Four or more drinks per day Assessment" )
            or exists ( "Five or more drinks per day Assessment" )
        )
    )
    or (
      "Patient is 65 or Over"
        and (
          exists ( "Four or more drinks per day Assessment" )
            or exists ( "Five or more drinks per day Assessment" )
        )
    )
   // Note: The spec doesn't include the over 65 test here but does in dependent N/D 2.

define "AUDIT-C Assessment":
  [Observation: "AUDIT-C Total Score (in points)"] A
    where A.status in { 'final', 'amended', 'corrected' }
      and A.effective in day of "Measurement Period"
      and A.value is not null

define "AUDIT Assessment":
  [Observation: "AUDIT Total Score (in points)"] A
    where A.status in { 'final', 'amended', 'corrected' }
      and A.effective in day of "Measurement Period"
      and A.value is not null

define "Patient is Male":
  Patient.gender = 'male'

define "Five or more drinks per day Assessment":
  [Observation: "Alcoholic drinks per drinking day - Reported"] A
    where A.status in { 'final', 'amended', 'corrected' }
      and A.effective in day of "Measurement Period"
      and A.value >= 5 '{drinks}/d'

define "Patient is Female":
  Patient.gender = 'female'

define "Patient is 65 or Over":
  AgeInYearsAt(start of "Measurement Period")>= 65

define "Four or more drinks per day Assessment":
  [Observation: "Alcoholic drinks per drinking day - Reported"] A
    where A.status in { 'final', 'amended', 'corrected' }
      and A.effective in day of "Measurement Period"
      and A.value >= 4 '{drinks}/d'

// Counseling or Other Follow-Up on Positive Screen

/*
Initial Population
Product lines -- Commercial, Medicaid, Medicare (report each product line separately).
*/

define "Denominator 2":
  // "Initial Population"
  exists "Positive Assessment for Unhealthy Alcohol Use"

// Counseling or Other Follow-Up on Positive Screen

define "Numerator 2":
  // "Initial Population"
  exists (
    "Initial Positive Assessment for Unhealthy Alcohol Use" A
      with "Followup After Positive Screen" F
        such that
          if F is Observation then F.effective 2 months or less on or after day of A.effective
          else F.performed."end" 2 months or less on or after day of A.effective
  )

define "Positive Assessment for Unhealthy Alcohol Use":
  (
    "AUDIT Assessment" A
      where A.value >= 8
  )
    union (
      "AUDIT-C Assessment" A
        where ("Patient is Male" and A.value >= 4)
          or ("Patient is Female" and A.value >= 3)
    )
    union (
      "Five or more drinks per day Assessment" A
        where "Patient is Male"
          and A.value >= 1
    )
    union (
      "Four or more drinks per day Assessment" A
        where ("Patient is Female" or "Patient is 65 or Over")
          and A.value >= 1
    )

define "Followup After Positive Screen":
  (
    [Procedure: "Alcohol Counseling and Treatment"] Proc
      where Proc.status = 'completed'
	)
    union (
      [Observation: "Alcohol Counseling and Treatment"] Obs
        where Obs.status in { 'final', 'amended', 'corrected' }
    )
    union (
      [Procedure: "Alcohol Screening"] Proc
        where Proc.status = 'completed'
    )
    union (
      [Observation: "Alcohol Screening"] Obs
        where Obs.status in { 'final', 'amended', 'corrected' }
    )

define "Initial Positive Assessment for Unhealthy Alcohol Use":
  {
    First(
      "Positive Assessment for Unhealthy Alcohol Use" A
        sort by effective.value
    )
  }

/*
Stratifiers
*/

define "Stratifier 1":
  AgeInYearsAt(start of "Measurement Period")in Interval[18, 44]

define "Stratifier 2":
  AgeInYearsAt(start of "Measurement Period")in Interval[45, 64]

define "Stratifier 3":
  AgeInYearsAt(start of "Measurement Period")>= 65
