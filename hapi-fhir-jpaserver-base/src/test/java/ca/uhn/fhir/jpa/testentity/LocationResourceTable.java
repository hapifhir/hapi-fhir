package ca.uhn.fhir.jpa.testentity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import ca.uhn.fhir.jpa.entity.BaseResourceTable;
import ca.uhn.fhir.model.dstu.resource.Location;

@Entity
@DiscriminatorValue("LOCATION")
public class LocationResourceTable extends BaseResourceTable<Location> {

	@Override
	public Class<Location> getResourceType() {
		return Location.class;
	}

}
