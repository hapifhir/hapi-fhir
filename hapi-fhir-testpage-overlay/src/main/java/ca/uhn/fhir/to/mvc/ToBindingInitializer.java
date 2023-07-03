package ca.uhn.fhir.to.mvc;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

public class ToBindingInitializer implements WebBindingInitializer {

	@Override
	public void initBinder(WebDataBinder theBinder) {
		theBinder.setFieldMarkerPrefix("__");
	}

	@Override
	public void initBinder(WebDataBinder theBinder, WebRequest theRequest) {
		// nothing
	}

}
