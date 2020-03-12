package ca.uhn.fhir.jpa.model.cross;

import java.util.Date;

public class ResourceLookup implements IResourceLookup {
    private final String myResourceType;
    private final Long myResourcePid;
    private final Date myDeletedAt;

	public ResourceLookup(String theResourceType, Long theResourcePid, Date theDeletedAt) {
        myResourceType = theResourceType;
        myResourcePid = theResourcePid;
        myDeletedAt = theDeletedAt;
    }

    @Override
    public String getResourceType() {
        return myResourceType;
    }

    @Override
    public Long getResourceId() {
        return myResourcePid;
    }

    @Override
    public Date getDeleted() {
        return myDeletedAt;
    }
}
