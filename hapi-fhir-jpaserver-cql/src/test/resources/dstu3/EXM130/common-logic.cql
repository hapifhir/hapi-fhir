library NCQACommon_FHIR version '3.10.000'

using FHIR version '3.0.0'

include FHIRHelpers version '3.0.0' called FHIRHelpers

codesystem "LOINC": 'urn:oid:2.16.840.1.113883.6.1'
codesystem "SNOMEDCT": 'urn:oid:2.16.840.1.113883.6.96'
codesystem "SNOMEDCT:2015-09": 'urn:oid:2.16.840.1.113883.6.96' version 'urn:hl7:version:2015-09'
codesystem "SNOMEDCT:2016-03": 'urn:oid:2.16.840.1.113883.6.96' version 'urn:hl7:version:2016-03'

valueset "Encounter Inpatient": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113883.3.666.5.307'
valueset "Hospice care ambulatory": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113762.1.4.1108.15'
valueset "Care Services in Long-Term Residential Facility": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113883.3.464.1003.101.12.1014'
valueset "NCQA Payer": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113762.1.4.1165.42'
valueset "X Institutional SNP Value Set": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113762.1.4.1165.43'
valueset "Medicare": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113762.1.4.1165.44'
valueset "Medicaid": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113762.1.4.1165.45'
valueset "Commercial": 'http://ncqa.org/hedis/ValueSet/2.16.840.1.113762.1.4.1165.46'


code "Birthdate": '21112-8' from "LOINC" display 'Birthdate'
code "Dead": '419099009' from "SNOMEDCT" display 'Dead'
code "Discharge to healthcare facility for hospice care (procedure)": '428371000124100' from "SNOMEDCT:2015-09" display 'Discharge to healthcare facility for hospice care (procedure)'
code "Discharge to home for hospice care (procedure)": '428361000124107' from "SNOMEDCT:2015-09" display 'Discharge to home for hospice care (procedure)'
code "Long term care hospital (environment)": '32074000' from "SNOMEDCT:2016-03" display 'Long term care hospital (environment)'
code "Patient transfer, to another health care facility (procedure)": '19712007' from "SNOMEDCT:2016-03" display 'Patient transfer, to another health care facility (procedure)'
code "Referral to long term care service (procedure)": '417708006' from "SNOMEDCT:2016-03" display 'Referral to long term care service (procedure)'

parameter "Measurement Period" Interval<DateTime>

context Patient

define "Is Enrolled in Institutional SNP":
  exists ( ["Encounter": "X Institutional SNP Value Set"] SNP
      where SNP.status in { 'in-progress', 'finished' }
        and SNP.period overlaps "Measurement Period"
  )

define "Is Living Long Term in Institution":
  exists ( ["Encounter": "Encounter Inpatient"] DischargeLongTerm
      where DischargeLongTerm.status in { 'in-progress', 'finished' }
        and ( FHIRHelpers.ToConcept(DischargeLongTerm.hospitalization.dischargeDisposition) ~ "Patient transfer, to another health care facility (procedure)"
          or FHIRHelpers.ToConcept(DischargeLongTerm.hospitalization.dischargeDisposition) ~ "Referral to long term care service (procedure)"
      )
        and DischargeLongTerm.period ends during "Measurement Period"
  )
    // TODO: Consider whether this should be Procedure or Task? (See http://build.fhir.org/ig/cqframework/qi-core/Intervention.html#Intervention)
    or exists ( ["Task": "Care Services in Long-Term Residential Facility"] LongTermOrder
        where LongTermOrder.intent = 'order'
          and (LongTermOrder.authoredOn during "Measurement Period"
            or LongTermOrder.executionPeriod overlaps "Measurement Period"
          )
    )
    // TODO: For the mapping to task, both Order and Performed map to a Task with intent of order, the fact that
    // the task is performed seems to be best captured by the presence of an executionPeriod... This would be
    // worth some discussion with the Workflow Group on the intended representation here
    //or exists ( ["Task": "Care Services in Long-Term Residential Facility"] LongTermPerformed
    //    where LongTermPerformed.
    //    LongTermPerformed.relevantPeriod overlaps "Measurement Period"
    //)

/*This is the standard NCQA Hospice exclusion logic from 3.6.2018*/
define "Has Hospice":
  exists ( ["Encounter": "Encounter Inpatient"] DischargeHospice
      where DischargeHospice.status in { 'in-progress', 'finished' }
        and ( FHIRHelpers.ToConcept(DischargeHospice.hospitalization.dischargeDisposition) ~ "Discharge to home for hospice care (procedure)"
          or FHIRHelpers.ToConcept(DischargeHospice.hospitalization.dischargeDisposition) ~ "Discharge to healthcare facility for hospice care (procedure)"
      )
        and DischargeHospice.period ends during "Measurement Period"
  )
    or exists ( ["Task": "Hospice care ambulatory"] HospiceTask
        where HospiceTask.intent = 'order'
          and (HospiceTask.authoredOn during "Measurement Period"
            or HospiceTask.executionPeriod overlaps "Measurement Period"
          )
    )
    // TODO: See discussion in "Is Living Long Term in Institution"
    //or exists ( ["Intervention, Order": "Hospice care ambulatory"] HospiceOrder
    //    where HospiceOrder.authorDatetime during "Measurement Period"
    //)
    //or exists ( ["Intervention, Performed": "Hospice care ambulatory"] HospicePerformed
    //    where HospicePerformed.relevantPeriod overlaps "Measurement Period"
    //)

/*ToDate takes a given DateTime value and returns a DateTime with the time components "zeroed", and the timezone of the input value, for example */
define function "ToDate"(Value DateTime ):
  DateTime(year from Value, month from Value, day from Value, 0, 0, 0, 0, timezone from Value)

/*CalendarAgeInDaysAt calculates the calendar age (meaning the age without considering time components) in days*/
define function "CalendarAgeInDaysAt"(BirthDateTime DateTime, AsOf DateTime ):
  days between ToDate(BirthDateTime)and ToDate(AsOf)

/*CalendarAgeInDays calculates the calendar age (meaning the age without considering time components) in days as of today*/
define function "CalendarAgeInDays"(BirthDateTime DateTime ):
  CalendarAgeInDaysAt(BirthDateTime, Today())

/*CalendarAgeInMonthsAt calculates the calendar age (meaning the age without considering time components) in months*/
define function "CalendarAgeInMonthsAt"(BirthDateTime DateTime, AsOf DateTime ):
  months between ToDate(BirthDateTime)and ToDate(AsOf)

define function "CalendarAgeInMonths"(BirthDateTime DateTime ):
  CalendarAgeInMonthsAt(BirthDateTime, Today())

/*calculates the difference in calendar days between the start and end of the given interval*/
define function "LengthInDays"(Value Interval<DateTime> ):
  difference in days between start of Value and end of Value

/*CalendarAgeInYearsAt calculates the calendar age (meaning the age without considering time components) in years*/
define function "CalendarAgeInYearsAt"(BirthDateTime DateTime, AsOf DateTime ):
  years between ToDate(BirthDateTime)and ToDate(AsOf)

/*CalendarAgeInYears calculates the calendar age (meaning the age without considering time components) in years as of today */
define function "CalendarAgeInYears"(BirthDateTime DateTime ):
  CalendarAgeInYearsAt(BirthDateTime, Today())
