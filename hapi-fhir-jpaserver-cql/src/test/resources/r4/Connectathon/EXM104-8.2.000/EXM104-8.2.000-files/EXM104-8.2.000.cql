library EXM104 version '8.2.000'
// for 2020 Reporting Year
//STK 2
/*
Based on CMS104 - Discharged on Antithrombotic Therapy
Author: TJC
*/

using FHIR version '4.0.1'

include FHIRHelpers version '4.0.1'
include MATGlobalCommonFunctions version '5.0.000' called Global
include SupplementalDataElements version '2.0.0' called SDE
include TJCOverall version '5.0.000' called TJC

codesystem "LOINC": 'http://loinc.org'
codesystem "SNOMEDCT": 'http://snomed.info/sct/731000124108'
// NOTE: R4 code system URLs changed to be based on terminology.hl7.org...
codesystem "Diagnosis Role": 'http://terminology.hl7.org/CodeSystem/diagnosis-role'
//codesystem "Diagnosis Role": 'http://hl7.org/fhir/diagnosis-role'
codesystem "RequestIntent": 'http://terminology.hl7.org/CodeSystem/request-intent'
//codesystem "RequestIntent": 'http://hl7.org/fhir/request-intent'


valueset "Antithrombotic Therapy": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113883.3.117.1.7.1.201'
valueset "Comfort Measures": 'http://cts.nlm.nih.gov/fhir/ValueSet/1.3.6.1.4.1.33895.1.3.0.45'
valueset "Discharge To Acute Care Facility": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113883.3.117.1.7.1.87'
valueset "Discharged to Health Care Facility for Hospice Care": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113883.3.117.1.7.1.207'
valueset "Discharged to Home for Hospice Care": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113883.3.117.1.7.1.209'
valueset "Emergency Department Visit": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113883.3.117.1.7.1.292'
valueset "Ethnicity": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.114222.4.11.837'
valueset "Hemorrhagic Stroke": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113883.3.117.1.7.1.212'
valueset "Ischemic Stroke": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113883.3.117.1.7.1.247'
valueset "Left Against Medical Advice": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113883.3.117.1.7.1.308'
valueset "Medical Reason": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113883.3.117.1.7.1.473'
valueset "Non-Elective Inpatient Encounter": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113883.3.117.1.7.1.424'
valueset "Observation Services": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113762.1.4.1111.143'
valueset "ONC Administrative Sex": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113762.1.4.1'
valueset "Patient Expired": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113883.3.117.1.7.1.309'
valueset "Patient Refusal": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113883.3.117.1.7.1.93'
valueset "Payer": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.114222.4.11.3591'
valueset "Race": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.114222.4.11.836'
valueset "Ticagrelor Therapy": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113762.1.4.1110.39'

code "Birth date": '21112-8' from "LOINC" display 'Birth date'
code "Order": 'order' from "RequestIntent" display 'Order'

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

define "Initial Population":
	TJC."Encounter with Principal Diagnosis and Age"

define "Denominator":
	TJC."Ischemic Stroke Encounter"

define "Denominator Exclusion":
	TJC."Ischemic Stroke Encounters with Discharge Disposition"
		union TJC."Comfort Measures during Hospitalization"

define "Numerator":
	TJC."Ischemic Stroke Encounter" IschemicStrokeEncounter
		with "Antithrombotic Therapy at Discharge" DischargeAntithrombotic
			such that DischargeAntithrombotic.authoredOn during Global."Normalize Interval"(IschemicStrokeEncounter.period)

define "Antithrombotic Therapy at Discharge":
	["MedicationRequest": medication in "Antithrombotic Therapy"] Antithrombotic
  //Note: expressed as an or with equivalence semantics pending resolution of potential CQL issue.
	  where exists (Antithrombotic.category C where FHIRHelpers.ToConcept(C) ~ Global."Community" or FHIRHelpers.ToConcept(C) ~ Global."Discharge")
      and Antithrombotic.status in { 'active', 'completed' }
	    and Antithrombotic.intent.value = 'order'

define "Denominator Exception":
	"Encounter With No Antithrombotic At Discharge"
		union "Encounter With Ticagrelor at Discharge"

define "Encounter With No Antithrombotic At Discharge":
	TJC."Ischemic Stroke Encounter" IschemicStrokeEncounter
		with "Antithrombotic Not Given at Discharge" NoDischargeAntithrombotic
			such that NoDischargeAntithrombotic.authoredOn during IschemicStrokeEncounter.period

define "Antithrombotic Not Given at Discharge":
	["MedicationRequest": medication in "Antithrombotic Therapy"] NoAntithromboticDischarge
    where NoAntithromboticDischarge.doNotPerform is true
			and (NoAntithromboticDischarge.reasonCode in "Medical Reason"
				or NoAntithromboticDischarge.reasonCode in "Patient Refusal")
      //Note: expressed as an or with equivalence semantics pending resolution of potential CQL issue.
      and exists (NoAntithromboticDischarge.category C where FHIRHelpers.ToConcept(C) ~ Global."Community" or FHIRHelpers.ToConcept(C) ~ Global."Discharge")
      and NoAntithromboticDischarge.status = 'completed'
      and NoAntithromboticDischarge.intent = 'order'

define "Encounter With Ticagrelor at Discharge":
  TJC."Ischemic Stroke Encounter" IschemicStrokeEncounter
    with "Ticagrelor Therapy at Discharge" DischargeTicagrelor
      such that DischargeTicagrelor.authoredOn during IschemicStrokeEncounter.period

define "Ticagrelor Therapy at Discharge":
  ["MedicationRequest": medication in TJC."Ticagrelor Therapy"] Ticagrelor
    //Note: expressed as an or with equivalence semantics pending resolution of potential CQL issue.
    where exists (Ticagrelor.category C where FHIRHelpers.ToConcept(C) ~ Global."Community" or FHIRHelpers.ToConcept(C) ~ Global."Discharge")
      and Ticagrelor.status in { 'active', 'completed' }
      and Ticagrelor.intent = 'order'
