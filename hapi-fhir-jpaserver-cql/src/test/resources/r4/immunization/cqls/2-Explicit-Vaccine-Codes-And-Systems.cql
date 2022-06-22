library Retrieve
using FHIR version '4.0.1'
include FHIRHelpers version '4.0.1'

codesystem VaccineCVX: 'http://hl7.org/fhir/sid/cvx'
codesystem VaccineOID: 'urn:oid:1.2.36.1.2001.1005.17'

context Patient

define "MMR Vaccinated 03":
[Immunization: Code '03' from VaccineCVX] Immu where Immu.status in {'completed'}

define "MMR Vaccinated 94":
[Immunization: Code '94' from VaccineCVX] Immu where Immu.status in {'completed'}

define "MMR Vaccinated MMR":
[Immunization: Code 'MMR' from VaccineOID] Immu where Immu.status in {'completed'}

define "MMR Vaccinated MMRCSL":
[Immunization: Code 'MMRCSL' from VaccineOID] Immu where Immu.status in {'completed'}

define "Initial Population":
	true

define "Denominator":
	true

define "Numerator":
 	exists("MMR Vaccinated 03") or
exists("MMR Vaccinated 94") or
exists("MMR Vaccinated MMR") or
exists("MMR Vaccinated MMRCSL")
