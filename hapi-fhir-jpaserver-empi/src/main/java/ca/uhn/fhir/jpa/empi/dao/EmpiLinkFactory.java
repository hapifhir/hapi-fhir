package ca.uhn.fhir.jpa.empi.dao;

import ca.uhn.fhir.empi.api.IEmpiSettings;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import org.springframework.beans.factory.annotation.Autowired;

public class EmpiLinkFactory {
	private final IEmpiSettings myEmpiSettings;

	@Autowired
	public EmpiLinkFactory(IEmpiSettings theEmpiSettings) {
		myEmpiSettings = theEmpiSettings;
	}

	public EmpiLink newEmpiLink() {
		return new EmpiLink(myEmpiSettings.getRuleVersion());
	}
}
