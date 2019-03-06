package ca.uhn.fhir.jpa.config;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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
import ca.uhn.fhir.jpa.model.entity.ForcedId;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedCompositeStringUnique;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import org.hibernate.HibernateException;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;

import javax.persistence.PersistenceException;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class HapiFhirHibernateJpaDialect extends HibernateJpaDialect {

	private static final Logger ourLog = LoggerFactory.getLogger(HapiFhirHibernateJpaDialect.class);
	private HapiLocalizer myLocalizer;

	/**
	 * Constructor
	 */
	public HapiFhirHibernateJpaDialect(HapiLocalizer theLocalizer) {
		myLocalizer = theLocalizer;
	}


	public RuntimeException translate(PersistenceException theException, String theMessageToPrepend) {
		if (theException.getCause() instanceof HibernateException) {
			return new PersistenceException(convertHibernateAccessException((HibernateException) theException.getCause(), theMessageToPrepend));
		}
		return theException;
	}

	@Override
	protected DataAccessException convertHibernateAccessException(HibernateException theException) {
		return convertHibernateAccessException(theException, null);
	}

	private DataAccessException convertHibernateAccessException(HibernateException theException, String theMessageToPrepend) {
		String messageToPrepend = "";
		if (isNotBlank(theMessageToPrepend)) {
			messageToPrepend = theMessageToPrepend + " - ";
		}
		if (theException.toString().contains("Batch update")) {
			theException.toString();
		}
		// <editor-fold desc="I HATE YOU">
		if (theException instanceof ConstraintViolationException) {
			String constraintName = ((ConstraintViolationException) theException).getConstraintName();
			switch (defaultString(constraintName)) {
				case ResourceHistoryTable.IDX_RESVER_ID_VER:
					throw new ResourceVersionConflictException(messageToPrepend + myLocalizer.getMessage(HapiFhirHibernateJpaDialect.class, "resourceVersionConstraintFailure"));
				case ResourceIndexedCompositeStringUnique.IDX_IDXCMPSTRUNIQ_STRING:
					throw new ResourceVersionConflictException(messageToPrepend + myLocalizer.getMessage(HapiFhirHibernateJpaDialect.class, "resourceIndexedCompositeStringUniqueConstraintFailure"));
				case ForcedId.IDX_FORCEDID_TYPE_FID:
					throw new ResourceVersionConflictException(messageToPrepend + myLocalizer.getMessage(HapiFhirHibernateJpaDialect.class, "forcedIdConstraintFailure"));
			}
		}
		// </editor-fold>

		return super.convertHibernateAccessException(theException);
	}

}
