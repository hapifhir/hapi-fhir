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
package ca.uhn.fhir.jpa.test.config;

import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DelayListener implements ProxyDataSourceBuilder.SingleQueryExecution {
	private static final Logger ourLog = LoggerFactory.getLogger(DelayListener.class);

	private boolean enabled = false;
	private AtomicInteger deleteCount= new AtomicInteger(0);

	public void enable() {
		enabled = true;
	}

	public void reset() {
		enabled = false;
		deleteCount = new AtomicInteger(0);
	}

	@Override
	public void execute(ExecutionInfo execInfo, List<QueryInfo> queryInfoList) {
		if (enabled && queryInfoList.get(0).getQuery().contains("from HFJ_RES_LINK")) {
			if (deleteCount.getAndIncrement() == 0) {
				try {
					Thread.sleep(500L);
				} catch (InterruptedException theE) {
					ourLog.error(theE.getMessage(), theE);
				}
			}
		}
	}

}
