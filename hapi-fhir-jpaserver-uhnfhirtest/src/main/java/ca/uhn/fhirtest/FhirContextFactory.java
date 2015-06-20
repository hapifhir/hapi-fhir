package ca.uhn.fhirtest;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import ca.uhn.fhir.context.FhirContext;

public class FhirContextFactory implements FactoryBean<FhirContext>, InitializingBean {

	private int myConnectionRequestTimeout = 5000;
	private int mySocketTimeout = 10000;
	private int myConnectTimeout = 4000;

	public int getConnectionRequestTimeout() {
		return myConnectionRequestTimeout;
	}

	public void setConnectionRequestTimeout(int theConnectionRequestTimeout) {
		myConnectionRequestTimeout = theConnectionRequestTimeout;
	}

	public int getSocketTimeout() {
		return mySocketTimeout;
	}

	public void setSocketTimeout(int theSocketTimeout) {
		mySocketTimeout = theSocketTimeout;
	}

	public int getConnectTimeout() {
		return myConnectTimeout;
	}

	public void setConnectTimeout(int theConnectTimeout) {
		myConnectTimeout = theConnectTimeout;
	}

	private FhirContext myCtx;

	public FhirContextFactory() {
	}

	@Override
	public FhirContext getObject() throws Exception {
		return myCtx;
	}

	@Override
	public Class<FhirContext> getObjectType() {
		return FhirContext.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		myCtx = new FhirContext();
		myCtx.getRestfulClientFactory().setConnectTimeout(myConnectTimeout);
		myCtx.getRestfulClientFactory().setSocketTimeout(mySocketTimeout);
		myCtx.getRestfulClientFactory().setConnectionRequestTimeout(myConnectionRequestTimeout);
	}

}
