package ca.uhn.fhir.jpa.testentity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import ca.uhn.fhir.jpa.entity.BaseResourceTable;
import ca.uhn.fhir.model.dstu.resource.Organization;

@Entity
@DiscriminatorValue("ORGANIZATION")
public class OrganizationResourceTable extends BaseResourceTable<Organization> {

	@Override
	public Class<Organization> getResourceType() {
		return Organization.class;
	}

}
