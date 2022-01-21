package ca.uhn.fhir.jpa.interceptor;

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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.model.DeleteConflict;
import ca.uhn.fhir.jpa.api.model.DeleteConflictList;
import ca.uhn.fhir.model.primitive.IdDt;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * This JPA interceptor can be configured with a collection of FHIRPath expressions, and will disable
 * referential integrity for target resources at those paths.
 * <p>
 * For example, suppose this interceptor is configured with a path of <code>AuditEvent.entity.what</code>,
 * and an AuditEvent resource exists in the repository that has a reference in that path to resource
 * <code>Patient/123</code>. Normally this reference would prevent the Patient resource from being deleted unless
 * the AuditEvent was first deleted as well (or a <a href="/hapi-fhir/docs/server_jpa/configuration.html#cascading-deletes">cascading delete</a> was used).
 * With this interceptor in place, the Patient resource could be deleted, and the AuditEvent would remain intact.
 * </p>
 */
@Interceptor
public class OverridePathBasedReferentialIntegrityForDeletesInterceptor {

	private static final Logger ourLog = LoggerFactory.getLogger(OverridePathBasedReferentialIntegrityForDeletesInterceptor.class);
	private final Set<String> myPaths = new HashSet<>();

	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private DaoRegistry myDaoRegistry;

	/**
	 * Constructor
	 */
	public OverridePathBasedReferentialIntegrityForDeletesInterceptor() {
		super();
	}

	/**
	 * Adds a FHIRPath expression indicating a resource path that should be ignored when considering referential
	 * integrity for deletes.
	 *
	 * @param thePath The FHIRPath expression, e.g. <code>AuditEvent.agent.who</code>
	 */
	public void addPath(String thePath) {
		getPaths().add(thePath);
	}

	/**
	 * Remove all paths registered to this interceptor
	 */
	public void clearPaths() {
		getPaths().clear();
	}

	/**
	 * Returns the paths that will be considered by this interceptor
	 *
	 * @see #addPath(String)
	 */
	public Set<String> getPaths() {
		return myPaths;
	}

	/**
	 * Interceptor hook method. Do not invoke directly.
	 */
	@Hook(value = Pointcut.STORAGE_PRESTORAGE_DELETE_CONFLICTS, order = CascadingDeleteInterceptor.OVERRIDE_PATH_BASED_REF_INTEGRITY_INTERCEPTOR_ORDER)
	public void handleDeleteConflicts(DeleteConflictList theDeleteConflictList) {
		for (DeleteConflict nextConflict : theDeleteConflictList) {
			ourLog.info("Ignoring referential integrity deleting {} - Referred to from {} at path {}", nextConflict.getTargetId(), nextConflict.getSourceId(), nextConflict.getSourcePath());

			IdDt sourceId = nextConflict.getSourceId();
			IdDt targetId = nextConflict.getTargetId();
			String targetIdValue = targetId.toVersionless().getValue();

			IBaseResource sourceResource = myDaoRegistry.getResourceDao(sourceId.getResourceType()).read(sourceId);

			IFhirPath fhirPath = myFhirContext.newFhirPath();
			for (String nextPath : myPaths) {
				List<IBaseReference> selections = fhirPath.evaluate(sourceResource, nextPath, IBaseReference.class);
				for (IBaseReference nextSelection : selections) {
					String selectionTargetValue = nextSelection.getReferenceElement().toVersionless().getValue();
					if (Objects.equals(targetIdValue, selectionTargetValue)) {
						theDeleteConflictList.setResourceIdToIgnoreConflict(nextConflict.getTargetId());
						break;
					}
				}

			}

		}
	}
}
