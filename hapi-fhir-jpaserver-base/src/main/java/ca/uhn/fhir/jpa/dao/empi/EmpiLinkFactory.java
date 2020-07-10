package ca.uhn.fhir.jpa.dao.empi;

import ca.uhn.fhir.empi.api.IEmpiSettings;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpiLinkFactory {
	@Autowired
	IEmpiSettings myEmpiSettings;

	public EmpiLink newEmpiLink() {
		return new EmpiLink(myEmpiSettings.getRuleVersion());
	}
}
