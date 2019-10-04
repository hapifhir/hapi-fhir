package ca.uhn.fhir.rest.server

import ca.uhn.fhir.rest.server.resources.ExtendedPatient
import ca.uhn.fhir.jaxrs.server.AbstractJaxRsResourceProvider
import org.hl7.fhir.instance.model.api.IAnyResource

class Server : BaseResource<ExtendedPatient>() {
   override fun getResourceType(): Class<ExtendedPatient>? = ExtendedPatient::class.java
}

abstract class BaseResource<T: IAnyResource>: AbstractJaxRsResourceProvider<T>()

fun main() {

}
