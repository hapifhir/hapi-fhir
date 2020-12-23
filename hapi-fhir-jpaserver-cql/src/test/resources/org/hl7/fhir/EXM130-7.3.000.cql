library EXM130 version '7.3.000'

/*
Based on CMS130v7 - Colorectal Cancer Screening
*/

/*
This example is a work in progress and should not be considered a final specification
or recommendation for guidance. This example will help guide and direct the process
of finding conventions and usage patterns that meet the needs of the various stakeholders
in the measure development community.
*/

using FHIR version '4.0.1'

include FHIRHelpers version '4.0.1' called FHIRHelpers
include Hospice version '2.0.000' called Hospice
include AdultOutpatientEncounters version '2.0.000' called AdultOutpatientEncounters
include MATGlobalCommonFunctions version '5.0.000' called Global
include SupplementalDataElements version '2.0.0' called SDE

valueset "ONC Administrative Sex": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113762.1.4.1'
valueset "Race": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.114222.4.11.836'
valueset "Ethnicity": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.114222.4.11.837'
valueset "Payer": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.114222.4.11.3591'
valueset "Colonoscopy": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113883.3.464.1003.108.12.1020'
valueset "Fecal Occult Blood Test (FOBT)": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113883.3.464.1003.198.12.1011'
valueset "Flexible Sigmoidoscopy": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113883.3.464.1003.198.12.1010'
valueset "Malignant Neoplasm of Colon": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113883.3.464.1003.108.12.1001'
valueset "Total Colectomy": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113883.3.464.1003.198.12.1019'
valueset "FIT DNA": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113883.3.464.1003.108.12.1039'
valueset "CT Colonography": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113883.3.464.1003.108.12.1038'

parameter "Measurement Period" Interval<DateTime>
  default Interval[@2019-01-01T00:00:00.0, @2020-01-01T00:00:00.0)

context Patient

define "SDE Ethnicity":
  SDE."SDE Ethnicity"

define "SDE Payer":
  SDE."SDE Payer"

define "SDE Race":
  SDE."SDE Race"

define "SDE Sex":
  SDE."SDE Sex"

define "Denominator":
	true

define "Flexible Sigmoidoscopy Performed":
	[Procedure: "Flexible Sigmoidoscopy"] FlexibleSigmoidoscopy
		where FlexibleSigmoidoscopy.status = 'completed'
			and Global."Normalize Interval"(FlexibleSigmoidoscopy.performed) ends 5 years or less on or before end of "Measurement Period"

define "CT Colonography Performed":
	[Procedure: "CT Colonography"] Colonography
		where Colonography.status = 'completed'
			and Global."Normalize Interval"(Colonography.performed) ends 5 years or less on or before end of "Measurement Period"

define "Total Colectomy Performed":
	[Procedure: "Total Colectomy"] Colectomy
		where Colectomy.status = 'completed'
			and Global."Normalize Interval"(Colectomy.performed) starts on or before end of "Measurement Period"

define "Malignant Neoplasm Ever":
	[Condition: "Malignant Neoplasm of Colon"] ColorectalCancer
		where Global."Normalize Interval"(ColorectalCancer.onset) starts on or before end of "Measurement Period"

define "Fecal Occult Blood Test Performed":
	[Observation: "Fecal Occult Blood Test (FOBT)"] FecalOccultResult
		where FecalOccultResult.status in {'final', 'amended', 'corrected', 'preliminary'}
			and FecalOccultResult.value is not null
			and Global."Normalize Interval"(FecalOccultResult.effective) during day of "Measurement Period"

define "Fecal Immunochemical Test DNA":
	[Observation: "FIT DNA"] FitDNA
		where FitDNA.status in {'final', 'amended', 'corrected', 'preliminary'}
			and FitDNA.value is not null
			and Global."Normalize Interval"(FitDNA.effective) occurs 3 years or less on or before day of end of "Measurement Period"

define "Colonoscopy Performed":
	[Procedure: "Colonoscopy"] Colonoscopy
		where Colonoscopy.status = 'completed'
		  and Global."Normalize Interval"(Colonoscopy.performed) ends 10 years or less on or before end of "Measurement Period"

define "Numerator":
	exists "Colonoscopy Performed"
		or exists "Fecal Occult Blood Test Performed"
		or exists "Flexible Sigmoidoscopy Performed"
		or exists "Fecal Immunochemical Test DNA"
		or exists "CT Colonography Performed"

define "Denominator Exclusion":
	Hospice."Has Hospice"
		or exists "Malignant Neoplasm Ever"
		or exists "Total Colectomy Performed"

define "Initial Population":
	Global."CalendarAgeInYearsAt"(FHIRHelpers.ToDate(Patient.birthDate), start of "Measurement Period") in Interval[50, 75]
			and exists AdultOutpatientEncounters."Qualifying Encounters"
