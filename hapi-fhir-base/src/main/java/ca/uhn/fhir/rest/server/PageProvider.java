package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.rest.annotation.GetPage;

public class PageProvider {
	
//	@GetPage(dstu1=true)
//	public Bundle getPageDstu1() {
//		return null;
//	}
	
	@GetPage()
	public IResource getPage() {
		return null;
	}
	
}
