library Retrieve
using FHIR version '4.0.1'
include FHIRHelpers version '4.0.1'

valueset "MMR Vaccinated": 'http://hl7.org/fhir/ValueSet/mmr-vaccine-codes'

context Patient

define "Initial Population":
	[Patient]

define "Denominator":
	[Patient]

define "Numerator":
 	"Qualifying Immunizations"

define "Qualifying Immunizations":
  [Immunization: "MMR Vaccinated"] ValidImmunization
    where ValidImmunization.status  = 'completed'