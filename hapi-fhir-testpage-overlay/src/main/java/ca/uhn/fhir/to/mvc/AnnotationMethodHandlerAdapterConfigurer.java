package ca.uhn.fhir.to.mvc;

import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

public class AnnotationMethodHandlerAdapterConfigurer {
	private final RequestMappingHandlerAdapter myAdapter;

	public AnnotationMethodHandlerAdapterConfigurer(RequestMappingHandlerAdapter theAdapter) {
		myAdapter = theAdapter;

		myAdapter.setWebBindingInitializer(new ToBindingInitializer());
	}
}
