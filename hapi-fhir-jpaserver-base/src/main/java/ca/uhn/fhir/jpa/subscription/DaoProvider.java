package ca.uhn.fhir.jpa.subscription;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;

@Component
public class DaoProvider {
	@Autowired
	private FhirContext myCtx;
	
	@Autowired
	private List<IFhirResourceDao<?>> myResourceDaos;
	
	private IFhirResourceDao<?> mySubscriptionDao;

	private Map<Class<? extends IBaseResource>, IFhirResourceDao<?>> myResourceTypeToDao;

	@SuppressWarnings("unchecked")
	public <R extends IBaseResource> IFhirResourceDao<R> getDao(Class<R> theType) {
		if (myResourceTypeToDao == null) {
			Map<Class<? extends IBaseResource>, IFhirResourceDao<?>> theResourceTypeToDao = new HashMap<>();
			for (IFhirResourceDao<?> next : myResourceDaos) {
				theResourceTypeToDao.put(next.getResourceType(), next);
			}

			if (this instanceof IFhirResourceDao<?>) {
				IFhirResourceDao<?> thiz = (IFhirResourceDao<?>) this;
				theResourceTypeToDao.put(thiz.getResourceType(), thiz);
			}

			myResourceTypeToDao = theResourceTypeToDao;
		}

		return (IFhirResourceDao<R>) myResourceTypeToDao.get(theType);
	}

	public IFhirResourceDao<?> getSubscriptionDao() {
		return mySubscriptionDao;
	}
	
	public void setResourceDaos(List<IFhirResourceDao<?>> theResourceDaos) {
		myResourceDaos = theResourceDaos;
	}
	
	@PostConstruct
	public void start() {
		for (IFhirResourceDao<?> next : myResourceDaos) {
			if (next.getResourceType() != null) {
				if (myCtx.getResourceDefinition(next.getResourceType()).getName().equals("Subscription")) {
					mySubscriptionDao = next;
				}
			}
		}
		Validate.notNull(mySubscriptionDao);
	}
}
