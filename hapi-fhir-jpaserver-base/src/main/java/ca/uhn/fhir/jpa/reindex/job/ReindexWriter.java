package ca.uhn.fhir.jpa.reindex.job;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

/**
 * Input: list of pids of resources to be deleted and expunged
 * Output: list of sql statements to be executed
 */
public class ReindexWriter implements ItemWriter<List<Long>> {
	private static final Logger ourLog = LoggerFactory.getLogger(ReindexWriter.class);

	@Override
	public void write(List<? extends List<Long>> thePids) throws Exception {
		// FIXME KHS
	}
}
