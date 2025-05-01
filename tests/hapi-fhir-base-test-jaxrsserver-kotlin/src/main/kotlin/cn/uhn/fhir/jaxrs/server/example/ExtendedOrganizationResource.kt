package cn.uhn.fhir.jaxrs.server.example

import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.jaxrs.server.AbstractJaxRsResourceProvider
import ca.uhn.fhir.model.api.Include
import ca.uhn.fhir.rest.annotation.IncludeParam
import ca.uhn.fhir.rest.annotation.OptionalParam
import ca.uhn.fhir.rest.annotation.Search
import ca.uhn.fhir.rest.api.Constants
import ca.uhn.fhir.rest.param.StringParam
import jakarta.ejb.Stateless
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

@Path("Organization")
@Stateless
@Produces(MediaType.APPLICATION_JSON, Constants.CT_FHIR_JSON, Constants.CT_FHIR_XML)
class ExtendedOrganizationResource : AbstractJaxRsResourceProvider<ExtendedOrganization>(FhirContext.forDstu3()) {
   override fun getResourceType(): Class<ExtendedOrganization>? = ExtendedOrganization::class.java

   @Search
   fun find(
      @OptionalParam(name = "_id") theId: StringParam?,
      @IncludeParam(allow = ["Patient:general-practitioner"]) includes: Collection<Include>?
   ): List<ExtendedOrganization> {
      val organization = ExtendedOrganization().also {
         it.id = "id"
      }
      return listOf(organization)
   }
}
