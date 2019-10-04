package ca.uhn.fhir.rest.server.resources

import ca.uhn.fhir.model.api.annotation.Child
import ca.uhn.fhir.model.api.annotation.Extension
import ca.uhn.fhir.model.api.annotation.ResourceDef
import org.hl7.fhir.dstu3.model.CodeableConcept
import org.hl7.fhir.dstu3.model.Patient
import org.hl7.fhir.instance.model.api.IBaseResource

import java.util.Collections.emptyList

@ResourceDef(name = "Patient")
class ExtendedPatient : Patient() {

   @Child(name = "status")
   @Extension(
      url = "http://some.url",
      definedLocally = false,
      isModifier = false
   )
   var status: List<CodeableConcept> = emptyList()
}
