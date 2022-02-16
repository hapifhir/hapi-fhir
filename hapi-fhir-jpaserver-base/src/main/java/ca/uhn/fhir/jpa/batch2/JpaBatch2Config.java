package ca.uhn.fhir.jpa.batch2;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.config.BaseBatch2Config;
import ca.uhn.fhir.batch2.impl.SynchronizedJobPersistenceWrapper;
import ca.uhn.fhir.jpa.dao.data.IBatch2JobInstanceRepository;
import ca.uhn.fhir.jpa.dao.data.IBatch2WorkChunkRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JpaBatch2Config extends BaseBatch2Config {

	@Bean
	public IJobPersistence batch2JobInstancePersister(IBatch2JobInstanceRepository theJobInstanceRepository, IBatch2WorkChunkRepository theWorkChunkRepository) {
		return new JpaJobPersistenceImpl(theJobInstanceRepository, theWorkChunkRepository);
	}

	@Primary
	@Bean
	public IJobPersistence batch2JobInstancePersisterWrapper(IBatch2JobInstanceRepository theJobInstanceRepository, IBatch2WorkChunkRepository theWorkChunkRepository) {
		IJobPersistence retVal = batch2JobInstancePersister(theJobInstanceRepository, theWorkChunkRepository);
		// Avoid H2 synchronization issues caused by
		// https://github.com/h2database/h2database/issues/1808
		if ("true".equals(System.getProperty("unit_test_mode"))) {
			retVal = new SynchronizedJobPersistenceWrapper(retVal);
		}
		return retVal;
	}


}
