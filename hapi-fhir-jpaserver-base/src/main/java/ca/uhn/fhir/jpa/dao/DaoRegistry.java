package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

public class DaoRegistry implements ApplicationContextAware {
	private ApplicationContext myAppCtx;

	@Autowired
	private FhirContext myCtx;
	private Map<String, IFhirResourceDao<?>> myResourceNameToResourceDao = new HashMap<>();

	@Override
	public void setApplicationContext(ApplicationContext theApplicationContext) throws BeansException {
		myAppCtx = theApplicationContext;
	}

	@PostConstruct
	public void start() {
		Map<String, IFhirResourceDao> resourceDaos = myAppCtx.getBeansOfType(IFhirResourceDao.class);
		for (IFhirResourceDao nextResourceDao : resourceDaos.values()) {
			RuntimeResourceDefinition nextResourceDef = myCtx.getResourceDefinition(nextResourceDao.getResourceType());
			myResourceNameToResourceDao.put(nextResourceDef.getName(), nextResourceDao);
		}
	}

	public IFhirResourceDao<?> getResourceDao(String theResourceName) {
		IFhirResourceDao<?> retVal = myResourceNameToResourceDao.get(theResourceName);
		Validate.notNull(retVal, "No DAO exists for resource type %s", theResourceName);
		return retVal;

	}

}
