package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.rest.api.MethodOutcome;

public class DaoMethodOutcome extends MethodOutcome {

    private ResourceTable myEntity;
    private IResource myResource;

    public ResourceTable getEntity() {
        return myEntity;
    }

    public IResource getResource() {
        return myResource;
    }

    @Override
    public DaoMethodOutcome setCreated(Boolean theCreated) {
        super.setCreated(theCreated);
        return this;
    }

    public DaoMethodOutcome setEntity(ResourceTable theEntity) {
        myEntity = theEntity;
        return this;
    }

    public DaoMethodOutcome setResource(IResource theResource) {
        myResource = theResource;
        return this;
    }

}
