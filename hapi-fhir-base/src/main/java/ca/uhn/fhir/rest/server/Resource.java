package ca.uhn.fhir.rest.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.common.BaseMethodBinding;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class Resource {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(Resource.class);

	private String resourceName;
	private List<BaseMethodBinding> methods = new ArrayList<BaseMethodBinding>();
	private IResourceProvider resourceProvider;

	public Resource() {
	}

	public Resource(String resourceName, List<BaseMethodBinding> methods) {
		this.resourceName = resourceName;
		this.methods = methods;
	}

	public BaseMethodBinding getMethod(String theResourceName, IdDt theId, IdDt theVersionId, Set<String> theParameters) throws Exception {
		if (null == methods) {
			ourLog.warn("No methods exist for resource provider: {}", resourceProvider.getClass());
			return null;
		}

		ourLog.info("Looking for a handler for {} / {} / {} / {}", new Object[] {theResourceName,theId, theVersionId, theParameters});
		for (BaseMethodBinding rm : methods) {
			if (rm.matches(theResourceName, theId, theVersionId, theParameters)) {
				ourLog.info("Handler {} matches", rm);
				return rm;
			} else {
				ourLog.info("Handler {} does not match", rm);
			}
		}
		return null;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public List<BaseMethodBinding> getMethods() {
		return methods;
	}

	public void setMethods(List<BaseMethodBinding> methods) {
		this.methods = methods;
	}

	public void addMethod(BaseMethodBinding method) {
		this.methods.add(method);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Resource))
			return false;
		return resourceName.equals(((Resource) o).getResourceName());
	}

	@Override
	public int hashCode() {
		return 0;
	}

	public void setResourceProvider(IResourceProvider theProvider) {
		resourceProvider = theProvider;
	}

	public IResourceProvider getResourceProvider() {
		return resourceProvider;
	}

}
