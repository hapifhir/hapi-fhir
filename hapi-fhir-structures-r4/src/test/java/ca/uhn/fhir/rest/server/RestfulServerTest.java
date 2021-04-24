package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.method.BaseMethodBinding;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Test;

import javax.servlet.ServletException;
import java.util.Collection;

import static com.google.common.collect.MoreCollectors.onlyElement;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class RestfulServerTest {


	@Test
	public void testDefaultMethods() throws ServletException {
		RestfulServer restfulServer = new RestfulServer(FhirContext.forR4());
		restfulServer.registerProvider(new SomeResourceProvider());
		restfulServer.init();
		Collection<IResourceProvider> resourceProviders = restfulServer.getResourceProviders();
		assertThat(resourceProviders.size(), is(1));
		Collection<ResourceBinding> resourceBindings = restfulServer.getResourceBindings();

		// 3, as the resource bindings include StructureDefinition, OperationDefinition and the registered Patient resource provider
		assertThat(resourceBindings.size(), is(3));
		ResourceBinding resourceBinding = resourceBindings.stream().filter(rb -> "Patient".equalsIgnoreCase(rb.getResourceName())).collect(onlyElement());

		assertThat(resourceBinding.getMethodBindings().size(), is(1));
		BaseMethodBinding<?> methodBinding = resourceBinding.getMethodBindings().stream().filter(mb -> "meta".equalsIgnoreCase(mb.getMethod().getName())).collect(onlyElement());

		assertThat(methodBinding.getMethod().getReturnType(), is(Parameters.class));
		assertThat(methodBinding.getMethod().getParameterTypes(), is(new Class[]{RequestDetails.class}));
	}

	class SomeResourceProvider implements IResourceProvider, SomeInterface {
		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}
	}


	interface SomeInterface {
		@Operation(
			name = "$meta",
			idempotent = true,
			returnParameters = {@OperationParam(
				name = "return",
				type = Meta.class
			)}
		)
		default Parameters meta(RequestDetails theRequestDetails) {
			return null;
		}
	}
}

