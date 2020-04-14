package ca.uhn.fhir.test.utilities;

import org.apache.commons.lang3.Validate;
import org.springframework.aop.framework.AopProxyUtils;

public class ProxyUtil {

	@SuppressWarnings("unchecked")
	public static <T> T getSingletonTarget(Object theSource, Class<T> theSubscriptionTriggeringSvcClass) {
		Validate.notNull(theSource);
		if (theSubscriptionTriggeringSvcClass.isAssignableFrom(theSource.getClass())) {
			return (T) theSource;
		}
		return (T) AopProxyUtils.getSingletonTarget(theSource);
	}

}
