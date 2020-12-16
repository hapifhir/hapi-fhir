library EXM349 version '2.10.000'

using FHIR version '4.0.1'

include FHIRHelpers version '4.0.1' called FHIRHelpers
include MATGlobalCommonFunctions version '5.0.000' called Global
include SupplementalDataElements version '2.0.0' called SDE

codesystem "LOINC": 'http://loinc.org'

valueset "Conditions Due To Human Immunodeficiency Virus (HIV)": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113762.1.4.1056.54'
valueset "Ethnicity": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.114222.4.11.837'
valueset "Human Immunodeficiency Virus (HIV) Laboratory Test Codes (Ab and Ag)": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113762.1.4.1056.50'
valueset "Office Visit": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113883.3.464.1003.101.12.1001'
valueset "ONC Administrative Sex": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113762.1.4.1'
valueset "Payer": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.114222.4.11.3591'
valueset "Preventive Care Services - Established Office Visit, 18 and Up": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113883.3.464.1003.101.12.1025'
valueset "Preventive Care Services, Initial Office Visit, 0 to 17": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113883.3.464.1003.101.12.1022'
valueset "Preventive Care Services-Initial Office Visit, 18 and Up": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113883.3.464.1003.101.12.1023'
valueset "Preventive Care, Established Office Visit, 0 to 17": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113883.3.464.1003.101.12.1024'
valueset "Race": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.114222.4.11.836'

code "HIV 1 and 2 tests - Meaningful Use set": '75622-1' from "LOINC" display 'HIV 1 and 2 tests - Meaningful Use set'

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

define "Qualifying Encounters":
	( [Encounter: "Preventive Care Services, Initial Office Visit, 0 to 17"]
		union [Encounter: "Preventive Care Services-Initial Office Visit, 18 and Up"]
		union [Encounter: "Preventive Care, Established Office Visit, 0 to 17"]
		union [Encounter: "Preventive Care Services - Established Office Visit, 18 and Up"]
		union [Encounter: "Office Visit"] ) ValidEncounter
		where ValidEncounter.status = 'finished'
		and ValidEncounter.period during "Measurement Period"

define "Denominator":
	"Initial Population"

define "Denominator Exclusions":
	exists ( [Condition: code in "Conditions Due To Human Immunodeficiency Virus (HIV)"] HIVConditions
			where FHIRHelpers.ToConcept(HIVConditions.clinicalStatus) ~ Global."active"
			and FHIRHelpers.ToConcept(HIVConditions.verificationStatus) ~ Global."confirmed"
			and Global."Normalize Interval"(HIVConditions.onset) starts before start of "Measurement Period" )

define "Numerator":
	exists "HIV Test Performed"

define "HIV Test Performed":
	( [Observation: "Human Immunodeficiency Virus (HIV) Laboratory Test Codes (Ab and Ag)"]
		union [Observation: "HIV 1 and 2 tests - Meaningful Use set"] ) HIVTest
		where Global.CalendarAgeInYearsAt(FHIRHelpers.ToDate(Patient.birthDate), start of Global."Normalize Interval"(HIVTest.effective)) between 15 and 65
			and HIVTest.status in {'final', 'amended', 'corrected'}
			and Global."Normalize Interval"(HIVTest.effective) starts before end of "Measurement Period"

define "Initial Population":
	Global.CalendarAgeInYearsAt(FHIRHelpers.ToDate(Patient.birthDate), start of "Measurement Period") between 15 and 65
		and exists "Qualifying Encounters"
