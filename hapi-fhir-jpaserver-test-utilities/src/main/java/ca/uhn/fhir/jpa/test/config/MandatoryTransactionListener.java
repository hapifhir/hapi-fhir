package ca.uhn.fhir.jpa.test.config;

/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
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

import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.Locale;

public class MandatoryTransactionListener implements ProxyDataSourceBuilder.SingleQueryExecution {
	@Override
	public void execute(ExecutionInfo execInfo, List<QueryInfo> queryInfoList) {
		if (!TransactionSynchronizationManager.isSynchronizationActive()) {
			for (QueryInfo nextQuery : queryInfoList) {
				String query = nextQuery.getQuery().toLowerCase(Locale.US);
				if (query.contains("hfj_") || query.contains("trm_")) {
					if (query.startsWith("select ") || query.startsWith("insert ") || query.startsWith("update ")) {
						throw new IllegalStateException("No transaction active executing query: " + nextQuery.getQuery());
					}
				}

			}
		}
	}
}
