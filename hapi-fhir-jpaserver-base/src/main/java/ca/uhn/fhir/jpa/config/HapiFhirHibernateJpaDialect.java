package ca.uhn.fhir.jpa.config;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

import ca.uhn.fhir.i18n.HapiLocalizer;
import ca.uhn.fhir.jpa.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.entity.ResourceIndexedCompositeStringUnique;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import org.hibernate.HibernateException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mapping.PreferredConstructor;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;

import static org.apache.commons.lang3.StringUtils.defaultString;

public class HapiFhirHibernateJpaDialect extends HibernateJpaDialect {

	private HapiLocalizer myLocalizer;

	/**
	 * Constructor
	 */
	public HapiFhirHibernateJpaDialect(HapiLocalizer theLocalizer) {
		myLocalizer = theLocalizer;
	}

	@Override
	protected DataAccessException convertHibernateAccessException(HibernateException theException) {
		if (theException instanceof ConstraintViolationException) {
			String constraintName = ((ConstraintViolationException) theException).getConstraintName();
			switch (defaultString(constraintName)) {
				case ResourceHistoryTable.IDX_RESVER_ID_VER:
					throw new ResourceVersionConflictException(myLocalizer.getMessage(HapiFhirHibernateJpaDialect.class, "resourceVersionConstraintFailure"));
				case ResourceIndexedCompositeStringUnique.IDX_IDXCMPSTRUNIQ_STRING:
					throw new ResourceVersionConflictException(myLocalizer.getMessage(HapiFhirHibernateJpaDialect.class, "resourceIndexedCompositeStringUniqueConstraintFailure"));
			}
		}

		return super.convertHibernateAccessException(theException);
	}

}
