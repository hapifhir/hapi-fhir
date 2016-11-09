package ca.uhn.fhir.parser;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Device;

@ResourceDef(name="Device", id="ExtendedDevice")
public class ExtendedDevice extends Device {

	private static final long serialVersionUID = 1L;
	
	@Child(name="someOrg")
	@Extension(url="http://extensionsBaseUrl/Device#someOrg", definedLocally=true, isModifier = false)
	private ResourceReferenceDt someOrg;
	
	@Child(name="someOtherOrg")
	@Extension(url="http://extensionsBaseUrl/Device#someOtherOrg", definedLocally=true, isModifier = false)
	private ResourceReferenceDt someOtherOrg;

	public ResourceReferenceDt getSomeOrg() {
		if (someOrg == null) {
			someOrg = new ResourceReferenceDt();
		}
		return someOrg;
	}

	public void setSomeOrg(ResourceReferenceDt someOrg) {
		this.someOrg = someOrg;
	}

	public ResourceReferenceDt getSomeOtherOrg() {
		if (someOtherOrg == null) {
			someOtherOrg = new ResourceReferenceDt();
		}
		return someOtherOrg;
	}

	public void setSomeOtherOrg(ResourceReferenceDt someOtherOrg) {
		this.someOtherOrg = someOtherOrg;
	}
	
}