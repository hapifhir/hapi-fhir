package ca.uhn.fhir.jpa.testentity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import ca.uhn.fhir.jpa.entity.BaseResourceTable;
import ca.uhn.fhir.model.dstu.resource.Device;

@Entity
@DiscriminatorValue("DEVICE")
public class DeviceResourceTable extends BaseResourceTable<Device> {

	@Override
	public Class<Device> getResourceType() {
		return Device.class;
	}

}
