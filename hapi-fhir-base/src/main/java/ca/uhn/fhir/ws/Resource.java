package ca.uhn.fhir.ws;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ca.uhn.fhir.model.api.IResource;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class Resource {

    private String resourceName;
    private List<ResourceMethod> methods = new ArrayList<ResourceMethod>();
	private IResourceProvider<? extends IResource> resourceProvider;

    public Resource() {}

    public Resource(String resourceName, List<ResourceMethod> methods) {
        this.resourceName = resourceName;
        this.methods = methods;
    }

    public ResourceMethod getMethod(Set<String> parameters) throws Exception {
        if (null == methods) return null;

        for (ResourceMethod rm : methods) {
            if (rm.matches(parameters)) return rm;
        }
        return null;
    }


    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public List<ResourceMethod> getMethods() {
        return methods;
    }

    public void setMethods(List<ResourceMethod> methods) {
        this.methods = methods;
    }

    public void addMethod(ResourceMethod method) {
        this.methods.add(method);
        method.setResource(this);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Resource)) return false;
        return resourceName.equals(((Resource)o).getResourceName());
    }

    @Override
    public int hashCode() {
        return 0;
    }

	public void setResourceProvider(IResourceProvider<? extends IResource> theProvider) {
		resourceProvider = theProvider;
	}

	public IResourceProvider<? extends IResource> getResourceProvider() {
		return resourceProvider;
	}

}
