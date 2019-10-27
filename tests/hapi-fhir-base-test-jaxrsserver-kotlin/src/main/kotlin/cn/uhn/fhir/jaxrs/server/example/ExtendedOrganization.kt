package cn.uhn.fhir.jaxrs.server.example

import ca.uhn.fhir.model.api.annotation.Child
import ca.uhn.fhir.model.api.annotation.Extension
import ca.uhn.fhir.model.api.annotation.ResourceDef
import org.hl7.fhir.dstu3.model.CodeableConcept
import org.hl7.fhir.dstu3.model.Organization

@ResourceDef(name = "Organization")
class ExtendedOrganization : Organization() {
   @Child(name = "someEnumerationInAList")
   @Extension(
      url = "http://test.url",
      definedLocally = false,
      isModifier = false
   )
   var legalStatus: List<CodeableConcept> = emptyList()
}
