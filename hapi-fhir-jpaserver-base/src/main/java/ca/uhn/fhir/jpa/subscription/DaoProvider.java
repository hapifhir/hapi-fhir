package ca.uhn.fhir.jpa.subscription;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;

@Component
@Lazy
public class DaoProvider implements ApplicationContextAware {
	@Autowired
	private FhirContext myContext;
	
	@Autowired
	private List<IFhirResourceDao<?>> myResourceDaos;
	
	private IFhirResourceDao<?> mySubscriptionDao;

	private Map<Class<? extends IBaseResource>, IFhirResourceDao<?>> myResourceTypeToDao;

	private ApplicationContext myApplicationContext;

	@PostConstruct
	public void start() {
		for (IFhirResourceDao<?> next : myResourceDaos) {
			if (next.getResourceType() != null) {
				if (myContext.getResourceDefinition(next.getResourceType()).getName().equals("Subscription")) {
					mySubscriptionDao = next;
				}
			}
		}
		Validate.notNull(mySubscriptionDao);
	}

	@PostConstruct
	public void startClearCaches() {
		myResourceTypeToDao = null;
	}

	// TODO KHS This method looks redundant with getDao() but they do different things.  Can we consolidate?
	public Map<Class<? extends IBaseResource>, IFhirResourceDao<?>> getDaos() {
		if (myResourceTypeToDao == null) {
			Map<Class<? extends IBaseResource>, IFhirResourceDao<?>> resourceTypeToDao = new HashMap<>();

			Map<String, IFhirResourceDao> daos = myApplicationContext.getBeansOfType(IFhirResourceDao.class, false, false);

			String[] beanNames = myApplicationContext.getBeanNamesForType(IFhirResourceDao.class);

			for (IFhirResourceDao<?> next : daos.values()) {
				resourceTypeToDao.put(next.getResourceType(), next);
			}

			if (this instanceof IFhirResourceDao<?>) {
				IFhirResourceDao<?> thiz = (IFhirResourceDao<?>) this;
				resourceTypeToDao.put(thiz.getResourceType(), thiz);
			}

			myResourceTypeToDao = resourceTypeToDao;
		}

		return Collections.unmodifiableMap(myResourceTypeToDao);
	}



	public IFhirResourceDao<?> getSubscriptionDao() {
		return mySubscriptionDao;
	}

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

	public void setResourceDaos(List<IFhirResourceDao<?>> theResourceDaos) {
		myResourceDaos = theResourceDaos;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		myApplicationContext = applicationContext;
	}

	public IFhirResourceDao<?> getDaoOrThrowException(Class<? extends IBaseResource> theClass) {
		IFhirResourceDao<? extends IBaseResource> retVal = getDao(theClass);
		if (retVal == null) {
			List<String> supportedResourceTypes = getDaos()
				.keySet()
				.stream()
				.map(t -> myContext.getResourceDefinition(t).getName())
				.sorted()
				.collect(Collectors.toList());
			throw new InvalidRequestException("Unable to process request, this server does not know how to handle resources of type " + myContext.getResourceDefinition(theClass).getName() + " - Can handle: " + supportedResourceTypes);
		}
		return retVal;
	}

	public String getDaoNameList() {
		return myResourceTypeToDao.keySet().toString();
	}
}
