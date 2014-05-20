package ca.uhn.fhir.jpa.testentity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import ca.uhn.fhir.jpa.entity.BaseResourceTable;
import ca.uhn.fhir.model.dstu.resource.Observation;

@Entity
@DiscriminatorValue("OBSERVATION")
public class ObservationResourceTable extends BaseResourceTable<Observation> {

	@Override
	public Class<Observation> getResourceType() {
		return Observation.class;
	}

}
