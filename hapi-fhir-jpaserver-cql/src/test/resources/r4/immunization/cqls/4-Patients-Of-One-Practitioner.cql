library Retrieve
using FHIR version '4.0.1'
include FHIRHelpers version '4.0.1'

valueset "MMRVaccinated": 'http://hl7.org/fhir/ValueSet/mmr-vaccine-codes'

context Patient

define "Initial Population":
	[Patient]

define "Denominator":
	[Patient] myPatient where (myPatient.generalPractitioner[0].reference=('Practitioner/dreric'))

define "Numerator":
 	"QualifyingImmunizations"

define "QualifyingImmunizations":
  [Immunization: "MMRVaccinated"] myImmunization
    where myImmunization.status  = 'completed'
