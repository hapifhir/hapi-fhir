/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;

public class JpaHapiTransactionService extends HapiTransactionService {

	private volatile Boolean myCustomIsolationSupported;

	public JpaHapiTransactionService() {}

	@Override
	public boolean isCustomIsolationSupported() {
		if (myCustomIsolationSupported == null) {
			if (myTransactionManager instanceof JpaTransactionManager) {
				JpaDialect jpaDialect = ((JpaTransactionManager) myTransactionManager).getJpaDialect();
				myCustomIsolationSupported = (jpaDialect instanceof HibernateJpaDialect);
			} else {
				myCustomIsolationSupported = false;
			}
		}
		return myCustomIsolationSupported;
	}
}
