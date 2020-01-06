package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.springframework.beans.factory.annotation.Autowired;

@Interceptor
public class ParserConfigInterceptor {

	@Autowired
	private FhirContext myContext;

	@Hook(Pointcut.INTERCEPTOR_REGISTERED)
	public void start() {
		myContext.getParserOptions().setStripVersionsFromReferences(false);

		IGenericClient client = FhirContext.forR4().newRestfulGenericClient("aa");


		client.read().resource("Patient").withIdAndVersion("100", "25").execute();

	}

}
