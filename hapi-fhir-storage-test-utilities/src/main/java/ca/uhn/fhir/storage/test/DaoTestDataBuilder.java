package ca.uhn.fhir.storage.test;

/*-
 * #%L
 * hapi-fhir-storage-test-utilities
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
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

public class DaoTestDataBuilder implements ITestDataBuilder, AfterEachCallback {
	private static final Logger ourLog = LoggerFactory.getLogger(DaoTestDataBuilder.class);

	final FhirContext myFhirCtx;
	final DaoRegistry myDaoRegistry;
	SystemRequestDetails mySrd;
	final SetMultimap<String, IIdType> myIds = HashMultimap.create();

	public DaoTestDataBuilder(FhirContext theFhirCtx, DaoRegistry theDaoRegistry, SystemRequestDetails theSrd) {
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
		return dao.update(theResource, mySrd).getId().toUnqualifiedVersionless();
	}

	@Override
	public FhirContext getFhirContext() {
		return myFhirCtx;
	}

	/**
	 * Delete anything created
	 */
	public void cleanup() {
		ourLog.info("cleanup {}", myIds);

		myIds.keySet().forEach(nextType->{
			IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(nextType);
			myIds.get(nextType).forEach(dao::delete);
		});
		myIds.clear();
	}

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
