/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
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
package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.interceptor.ex.PartitionInterceptorReadAllPartitions;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.partition.IPartitionLookupSvc;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class SimplePartitionTestHelper implements BeforeEachCallback, AfterEachCallback {
	public static final int TEST_PARTITION_ID = 17;
	private static final String TEST_PARTITION_NAME = "test-partition-17";
	private final PartitionSettings myPartitionSettings;
	private final IPartitionLookupSvc myPartitionConfigSvc;
	private final IInterceptorService myInterceptorRegistry;
	private final PartitionInterceptorReadAllPartitions myInterceptor = new PartitionInterceptorReadAllPartitions();

	public SimplePartitionTestHelper(
			PartitionSettings thePartitionSettings,
			IPartitionLookupSvc thePartitionConfigSvc,
			IInterceptorService theInterceptorRegistry) {
		myPartitionSettings = thePartitionSettings;
		myPartitionConfigSvc = thePartitionConfigSvc;
		myInterceptorRegistry = theInterceptorRegistry;
	}

	@Override
	public void beforeEach(ExtensionContext context) throws Exception {
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionConfigSvc.createPartition(
				new PartitionEntity().setId(TEST_PARTITION_ID).setName(TEST_PARTITION_NAME), null);
		myInterceptorRegistry.registerInterceptor(myInterceptor);
	}

	@Override
	public void afterEach(ExtensionContext context) throws Exception {
		myInterceptorRegistry.unregisterInterceptor(myInterceptor);
		myPartitionConfigSvc.deletePartition(TEST_PARTITION_ID);
		myPartitionSettings.setPartitioningEnabled(false);
	}
}
