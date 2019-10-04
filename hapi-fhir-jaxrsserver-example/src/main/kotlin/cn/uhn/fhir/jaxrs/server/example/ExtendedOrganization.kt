package cn.uhn.fhir.jaxrs.server.example

import ca.uhn.fhir.model.api.annotation.ResourceDef
import org.hl7.fhir.dstu3.model.BaseResource
import org.hl7.fhir.dstu3.model.Organization
import org.hl7.fhir.instance.model.api.IIdType

@ResourceDef(name = "Organization")
open class ExtendedOrganization : Organization() {

   override fun setId(value: IIdType?): BaseResource? {
      return this
   }
}
