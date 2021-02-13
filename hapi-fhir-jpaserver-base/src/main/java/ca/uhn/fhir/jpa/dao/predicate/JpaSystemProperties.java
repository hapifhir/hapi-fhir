package ca.uhn.fhir.jpa.dao.predicate;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 University Health Network
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

public class JpaSystemProperties {

	private static final Logger ourLog = LoggerFactory.getLogger(JpaSystemProperties.class);
	public static final String HFJ_FORCE_IN_CLAUSES_HF_50 = "hfj_force_in_clauses_hf50";
	private static boolean ourOptmizeSingleElementInExpression;

	static {
		updateSettingsBasedOnCurrentSystemProperties();
	}

	/**
	 * Non instantiable
	 */
	private JpaSystemProperties() {
	}

	public static void updateSettingsBasedOnCurrentSystemProperties() {
		ourOptmizeSingleElementInExpression = !("true".equals(System.getProperty(HFJ_FORCE_IN_CLAUSES_HF_50)));
		if (!ourOptmizeSingleElementInExpression) {
			ourLog.warn("Forcing IN(..) clauses for single element SQL predicates");
		}
	}

	public static boolean isOptmizeSingleElementInExpression() {
		return ourOptmizeSingleElementInExpression;
	}
}
