library EXM104_FHIR3 version '8.1.000'

/*
Based on CMS104v8 - Discharged on Antithrombotic Therapy
Author: TJC
STK 2
*/

using FHIR version '3.0.0'

include FHIRHelpers version '3.0.0'
include MATGlobalCommonFunctions_FHIR3 version '4.0.000' called Global
include TJCOverall_FHIR3 version '3.6.000' called TJC
include SupplementalDataElements_FHIR3 version '1.0.0' called SDE

codesystem "LOINC": 'http://loinc.org'
codesystem "SNOMEDCT": 'http://snomed.info/sct/731000124108'
codesystem "Diagnosis Role": 'http://hl7.org/fhir/diagnosis-role'
codesystem "RequestIntent": 'http://hl7.org/fhir/request-intent'
// NOTE: This is an R4 code system
codesystem "MedicationRequestCategory": 'http://terminology.hl7.org/CodeSystem/medicationrequest-category'

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

// NOTE: This is an R4 code
code "Discharge": 'discharge' from "MedicationRequestCategory" display 'Discharge'

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

  define "Encounter with Principal Diagnosis and Age":
  	"All Stroke Encounter" AllStrokeEncounter
  		with ["Patient"] BirthDate
  			such that AgeInYearsAt(AllStrokeEncounter.period.start) >= 18

        define "All Stroke Encounter":
        	"Non Elective Inpatient Encounter" NonElectiveEncounter
        		where Global.PrincipalDiagnosis(NonElectiveEncounter).code in "Hemorrhagic Stroke"
        		or Global.PrincipalDiagnosis(NonElectiveEncounter).code in "Ischemic Stroke"

            define "Non Elective Inpatient Encounter":
            	["Encounter": "Non-Elective Inpatient Encounter"] NonElectiveEncounter
            		where Global."LengthInDays"(NonElectiveEncounter.period)<= 120
            			and NonElectiveEncounter.period ends during "Measurement Period"

define "Denominator":
	TJC."Ischemic Stroke Encounter"

  define "Ischemic Stroke Encounter":
  	"Encounter with Principal Diagnosis and Age" EncounterWithAge
  		where Global.PrincipalDiagnosis(EncounterWithAge).code in "Ischemic Stroke"

define "Denominator Exclusion":
	TJC."Ischemic Stroke Encounters with Discharge Disposition"
		union TJC."Comfort Measures during Hospitalization"

    define "Ischemic Stroke Encounters with Discharge Disposition":
    	( ( "Ischemic Stroke Encounter" IschemicStrokeEncounter
    				where IschemicStrokeEncounter.hospitalization.dischargeDisposition in "Discharge To Acute Care Facility"
    					or IschemicStrokeEncounter.hospitalization.dischargeDisposition in "Left Against Medical Advice"
    					or IschemicStrokeEncounter.hospitalization.dischargeDisposition in "Patient Expired"
    					or IschemicStrokeEncounter.hospitalization.dischargeDisposition in "Discharged to Home for Hospice Care"
    					or IschemicStrokeEncounter.hospitalization.dischargeDisposition in "Discharged to Health Care Facility for Hospice Care"
    		)
    	)

    define "Comfort Measures during Hospitalization":
    	"Ischemic Stroke Encounter" IschemicStrokeEncounter
    		with "Intervention Comfort Measures" ComfortMeasure
    			such that Coalesce(start of ComfortMeasure.performed, ComfortMeasure.authoredOn)during Global."HospitalizationWithObservation"(IschemicStrokeEncounter)

          define "Intervention Comfort Measures":
          	(["ProcedureRequest": "Comfort Measures"] P
          		where P.intent = 'order')
          		union
          		(["Procedure": "Comfort Measures"] IntervetionPerformed
          		where IntervetionPerformed.status = 'completed')

define "Numerator":
	TJC."Ischemic Stroke Encounter" IschemicStrokeEncounter
		with "Antithrombotic Therapy at Discharge" DischargeAntithrombotic
			such that DischargeAntithrombotic.authoredOn during IschemicStrokeEncounter.period

      define "Antithrombotic Therapy at Discharge":
      	//The following should work, but it's pending a fix in the CQL Engine, ref https://github.com/DBCG/cql_engine/issues/271.
        //["MedicationRequest": "Antithrombotic Therapy"] Antithrombotic
        //where Antithrombotic.intent = 'order'
        
        //Temporarily using the following until the fix is available.
        ["MedicationRequest"] Antithrombotic
            where (Antithrombotic.medication as CodeableConcept) in "Antithrombotic Therapy"
      	    and Antithrombotic.intent = 'order'

define "Denominator Exceptions":
	"Encounter With No Antithrombotic At Discharge"
		union "Encounter With Ticagrelor at Discharge"

    define "Encounter With No Antithrombotic At Discharge":
    	TJC."Ischemic Stroke Encounter" IschemicStrokeEncounter
    		with "Antithrombotic Not Given at Discharge" NoDischargeAntithrombotic
    			such that NoDischargeAntithrombotic.authoredOn during IschemicStrokeEncounter.period

          define "Antithrombotic Not Given at Discharge":
          	["MedicationRequest": "Antithrombotic Therapy"] NoAntithromboticDischarge
          	  // STU3
          		where exists (NoAntithromboticDischarge.extension E where E.url = 'http://hl7.org/fhir/us/davinci-deqm/STU3/StructureDefinition/extension-doNotPerform' and E.value is true)
          		// R4
          		//where NoAntithromboticDischarge.doNotPerform is true
          			and (singleton from NoAntithromboticDischarge.reasonCode in "Medical Reason"
          				or singleton from NoAntithromboticDischarge.reasonCode in "Patient Refusal")

          // NOTE: On the assumption that status of not-taken is the closest to what the measure is looking for, this is the expression:
          // TODO: Request discussion w/ Pharmacy regarding how medications not prescribed at discharged would be documented
          //define "Antithrombotic Not Given at Discharge R4":
          //  ["MedicationStatement": "Antithrombotic Therapy"] AntithromboticTherapy
          //	  where AntithromboticTherapy.status = 'not-taken'
          //		  and (AntithromboticTherapy.statusReason in "Medical Reason"
          //				or AntithrombtoicTherapy.statusReason in "Patient Refusal")

          // If it's documented as a checkbox, it may be represented as an observation
          //define "Antithrombotic Not Given at Discharge (Observation)":
          //  ["Observation": "Medication Not Given At Discharge"] O
          //	  where O.value in "Antithrombotic Therapy"

      /*
      define "Encounter With Ticagrelor During Hospitalization":
      	TJC."Ischemic Stroke Encounter" IschemicStrokeEncounter
      		with ["MedicationAdministration": medication in TJC."Ticagrelor Therapy"] Ticagrelor
      			such that Ticagrelor.effective starts during Global."HospitalizationWithObservation"(IschemicStrokeEncounter)
      */
      //Looking for Ticagrelor as discharge medication, include a corrected defintion in 2021 CQL based measure
      define "Encounter With Ticagrelor at Discharge":
        TJC."Ischemic Stroke Encounter" IschemicStrokeEncounter
          with "Ticagrelor Therapy at Discharge" DischargeTicagrelor
            such that DischargeTicagrelor.authoredOn during IschemicStrokeEncounter.period

            define "Ticagrelor Therapy at Discharge":
              ["MedicationRequest": medication in TJC."Ticagrelor Therapy"] Ticagrelor
                where (Ticagrelor.medication as CodeableConcept) in TJC."Ticagrelor Therapy"
                  and Ticagrelor.intent = 'order'
