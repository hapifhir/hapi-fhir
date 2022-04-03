package ca.uhn.fhir.jpa.config;

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

import ca.uhn.fhir.i18n.HapiLocalizer;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.model.entity.ForcedId;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboStringUnique;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import org.hibernate.HibernateException;
import org.hibernate.PessimisticLockException;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
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

		if (theException instanceof ConstraintViolationException) {
			String constraintName = ((ConstraintViolationException) theException).getConstraintName();

			/*
			 * Note: Compare the constraint name in a case-insensitive way. Most DBs preserve the case, but Postgresql
			 * will return it as lowercase even though the definition is in caps.
			 */
			if (isNotBlank(constraintName)) {
				constraintName = constraintName.toUpperCase();
				if (constraintName.contains(ResourceHistoryTable.IDX_RESVER_ID_VER)) {
					throw new ResourceVersionConflictException(Msg.code(823) + messageToPrepend + myLocalizer.getMessage(HapiFhirHibernateJpaDialect.class, "resourceVersionConstraintFailure"));
				}
				if (constraintName.contains(ResourceIndexedComboStringUnique.IDX_IDXCMPSTRUNIQ_STRING)) {
					throw new ResourceVersionConflictException(Msg.code(824) + messageToPrepend + myLocalizer.getMessage(HapiFhirHibernateJpaDialect.class, "resourceIndexedCompositeStringUniqueConstraintFailure"));
				}
				if (constraintName.contains(ForcedId.IDX_FORCEDID_TYPE_FID)) {
					throw new ResourceVersionConflictException(Msg.code(825) + messageToPrepend + myLocalizer.getMessage(HapiFhirHibernateJpaDialect.class, "forcedIdConstraintFailure"));
				}
			}
		}

		/*
		 * It would be nice if we could be more precise here, since technically any optimistic lock
		 * failure could result in a StaleStateException, but with the error message we're returning
		 * we're basically assuming it's an optimistic lock failure on HFJ_RESOURCE.
		 *
		 * That said, I think this is an OK trade-off. There is a high probability that if this happens
		 * it is a failure on HFJ_RESOURCE (there aren't many other tables in our schema that
		 * use @Version at all) and this error message is infinitely more comprehensible
		 * than the one we'd otherwise return.
		 *
		 * The actual StaleStateException is thrown in hibernate's Expectations
		 * class in a method called "checkBatched" currently. This can all be tested using the
		 * StressTestR4Test method testMultiThreadedUpdateSameResourceInTransaction()
		 */
		if (theException instanceof org.hibernate.StaleStateException) {
			String msg = messageToPrepend + myLocalizer.getMessage(HapiFhirHibernateJpaDialect.class, "resourceVersionConstraintFailure");
			throw new ResourceVersionConflictException(Msg.code(826) + msg);
		}
		if (theException instanceof org.hibernate.PessimisticLockException) {
			PessimisticLockException ex = (PessimisticLockException) theException;
			String sql = defaultString(ex.getSQL()).toUpperCase();
			if (sql.contains(ResourceHistoryTable.HFJ_RES_VER)) {
				String msg = messageToPrepend + myLocalizer.getMessage(HapiFhirHibernateJpaDialect.class, "resourceVersionConstraintFailure");
				throw new ResourceVersionConflictException(Msg.code(827) + msg);
			}
		}

		DataAccessException retVal = super.convertHibernateAccessException(theException);
		return retVal;
	}

}
