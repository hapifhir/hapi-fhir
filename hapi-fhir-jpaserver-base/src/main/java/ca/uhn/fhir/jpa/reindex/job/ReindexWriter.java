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

import ca.uhn.fhir.jpa.search.reindex.ResourceReindexer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Input: list of pids of resources to be deleted and expunged
 * Output: list of sql statements to be executed
 */

public class ReindexWriter implements ItemWriter<List<Long>> {
	@Autowired
	ResourceReindexer myResourceReindexer;

	@Override
	public void write(List<? extends List<Long>> thePidLists) throws Exception {
		for (List<Long> pidList : thePidLists) {
			pidList.forEach(pid -> myResourceReindexer.readAndReindexResourceByPid(pid));
		}
	}
}
