package ca.uhn.fhir.rest.server;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.rest.method.BaseMethodBinding;
import ca.uhn.fhir.rest.method.Request;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class ResourceBinding {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceBinding.class);

	private String resourceName;
	private List<BaseMethodBinding> methods = new ArrayList<BaseMethodBinding>();
	private IResourceProvider resourceProvider;

	public ResourceBinding() {
	}

	public ResourceBinding(String resourceName, List<BaseMethodBinding> methods) {
		this.resourceName = resourceName;
		this.methods = methods;
	}

	public BaseMethodBinding getMethod(Request theRequest) throws Exception {
		if (null == methods) {
			ourLog.warn("No methods exist for resource provider: {}", resourceProvider.getClass());
			return null;
		}

		ourLog.debug("Looking for a handler for {}", theRequest);
		for (BaseMethodBinding rm : methods) {
			if (rm.matches(theRequest)) {
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

	public List<BaseMethodBinding> getMethodBindings() {
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
		if (!(o instanceof ResourceBinding))
			return false;
		return resourceName.equals(((ResourceBinding) o).getResourceName());
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
