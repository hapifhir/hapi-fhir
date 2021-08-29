package ca.uhn.fhir.jpa.batch.mdm;

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

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.data.IResourceLinkDao;
import ca.uhn.fhir.jpa.dao.expunge.ResourceTableFKProvider;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Input: list of pids of resources to be deleted and expunged
 * Output: list of sql statements to be executed
 */
public class MdmClearProcessor implements ItemProcessor<List<Long>, List<String>> {
	public static final String PROCESS_NAME = "Delete Expunging";
	public static final String THREAD_PREFIX = "delete-expunge";
	private static final Logger ourLog = LoggerFactory.getLogger(MdmClearProcessor.class);
	@Autowired
	ResourceTableFKProvider myResourceTableFKProvider;
	@Autowired
	DaoConfig myDaoConfig;
	@Autowired
	IdHelperService myIdHelper;
	@Autowired
	IResourceLinkDao myResourceLinkDao;

	@Override
	public List<String> process(List<Long> thePids) throws Exception {
		ourLog.info("CLEARING MDM " + thePids);
		// FIXME KHS
		return new ArrayList<>();
	}
}
