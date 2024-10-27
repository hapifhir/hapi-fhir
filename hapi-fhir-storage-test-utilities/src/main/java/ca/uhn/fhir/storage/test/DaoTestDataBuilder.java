/*-
 * #%L
 * hapi-fhir-storage-test-utilities
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.storage.test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.fhir.util.BundleBuilder;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Implements ITestDataBuilder via a live DaoRegistry.
 * Note: this implements {@link AfterEachCallback} and will delete any resources created when registered
 * via {@link org.junit.jupiter.api.extension.RegisterExtension}.
 * Add the inner {@link Config} to your spring context to inject this.
 * For convenience, you can still implement ITestDataBuilder on your test class, and delegate the missing methods to this bean.
 */
public class DaoTestDataBuilder implements ITestDataBuilder.WithSupport, ITestDataBuilder.Support, AfterEachCallback {
	private static final Logger ourLog = LoggerFactory.getLogger(DaoTestDataBuilder.class);

	final FhirContext myFhirCtx;
	final DaoRegistry myDaoRegistry;
	RequestDetails mySrd;
	final SetMultimap<String, IIdType> myIds = HashMultimap.create();

	public DaoTestDataBuilder(FhirContext theFhirCtx, DaoRegistry theDaoRegistry, RequestDetails theSrd) {
		myFhirCtx = theFhirCtx;
		myDaoRegistry = theDaoRegistry;
		mySrd = theSrd;
	}

	@Override
	public IIdType doCreateResource(IBaseResource theResource) {
		if (ourLog.isDebugEnabled()) {
			ourLog.debug("create resource {}", myFhirCtx.newJsonParser().encodeResourceToString(theResource));
		}
		//noinspection rawtypes
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(theResource.getClass());
		//noinspection unchecked
		IIdType id = dao.create(theResource, mySrd).getId().toUnqualifiedVersionless();
		myIds.put(theResource.fhirType(), id);
		return id;
	}

	@Override
	public IIdType doUpdateResource(IBaseResource theResource) {
		//noinspection rawtypes
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(theResource.getClass());
		//noinspection unchecked
		IIdType id = dao.update(theResource, mySrd).getId().toUnqualifiedVersionless();
		myIds.put(theResource.fhirType(), id);
		return id;
	}

	@Override
	public Support getTestDataBuilderSupport() {
		return this;
	}

	@Override
	public void setRequestId(String theRequestId) {
		mySrd.setRequestId(theRequestId);
	}

	@Override
	public FhirContext getFhirContext() {
		return myFhirCtx;
	}

	/**
	 * Delete anything created by this builder since the last cleanup().
	 */
	public void cleanup() {
		ourLog.info("cleanup {}", myIds);

		var builder = new BundleBuilder(myFhirCtx);
		myIds.values()
			.forEach(builder::addTransactionDeleteEntry);
		var bundle = builder.getBundle();

		ourLog.trace("Deleting in bundle {}", myFhirCtx.newJsonParser().encodeToString(bundle));
		//noinspection unchecked
		myDaoRegistry.getSystemDao().transaction(mySrd, bundle);

		myIds.clear();
	}

	/**
	 * Tear down and cleanup any Resources created during execution.
	 */
	@Override
	public void afterEach(ExtensionContext context) throws Exception {
		cleanup();
	}

	@Configuration
	public static class Config {
		@Autowired FhirContext myFhirContext;
		@Autowired DaoRegistry myDaoRegistry;

		@Bean
		DaoTestDataBuilder testDataBuilder() {
			return new DaoTestDataBuilder(myFhirContext, myDaoRegistry, new SystemRequestDetails());
		}
	}
}
