library Retrieve
using FHIR version '4.0.1'
include FHIRHelpers version '4.0.1'

context Patient

define "MMR Vaccinated":
[Immunization] Immu where
(Immu.vaccineCode.coding[0].code=('03')) or
(Immu.vaccineCode.coding[0].code=('94')) or
(Immu.vaccineCode.coding[0].code=('MMR')) or
(Immu.vaccineCode.coding[0].code=('MMRV')) or
(Immu.vaccineCode.coding[0].code=('MMRCSL'))

define "InitialPopulation":
	true

define "Denominator":
	true

define "Numerator":
 	exists("MMR Vaccinated")
