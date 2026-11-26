/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.config.util;

import ca.uhn.fhir.jpa.model.entity.IdAndPartitionId;
import jakarta.persistence.EntityManager;
import jakarta.persistence.IdClass;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.data.util.DirectFieldAccessFallbackBeanWrapper;

/**
 * Entity information for entities whose key is {@link IdAndPartitionId}. Such an entity is new when its PID is null,
 * whether or not its partition ID has been set.
 *
 * @param <T> The entity type
 * @param <ID> The repository ID type
 */
// Created by claude-fable-5-2
public class PartitionedIdEntityInformation<T, ID> extends JpaMetamodelEntityInformation<T, ID> {

	/**
	 * The name of the PID field in {@link IdAndPartitionId}, which JPA requires the entity's field to share
	 */
	private static final String PID_ATTRIBUTE = "myId";

	private PartitionedIdEntityInformation(Class<T> theDomainClass, EntityManager theEntityManager) {
		super(
				theDomainClass,
				theEntityManager.getMetamodel(),
				theEntityManager.getEntityManagerFactory().getPersistenceUnitUtil());
	}

	@Override
	public boolean isNew(T theEntity) {
		return new DirectFieldAccessFallbackBeanWrapper(theEntity).getPropertyValue(PID_ATTRIBUTE) == null;
	}

	/**
	 * @param theEntityInformation The entity information Spring Data built for the entity
	 * @param theEntityManager The entity manager of the repository
	 * @return Entity information that decides newness from the PID alone if the entity's key is
	 * {@link IdAndPartitionId}, and the given entity information otherwise
	 */
	static <T> JpaEntityInformation<T, ?> forEntity(
			JpaEntityInformation<T, ?> theEntityInformation, EntityManager theEntityManager) {
		IdClass idClass = AnnotationUtils.findAnnotation(theEntityInformation.getJavaType(), IdClass.class);
		if (idClass == null || !IdAndPartitionId.class.equals(idClass.value())) {
			return theEntityInformation;
		}
		return new PartitionedIdEntityInformation<>(theEntityInformation.getJavaType(), theEntityManager);
	}
}
