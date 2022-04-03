package ca.uhn.fhir.jpa.bulk.export.job;

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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.batch.config.BatchConstants;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.dao.data.IBulkExportJobDao;
import ca.uhn.fhir.jpa.entity.BulkExportJobEntity;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.util.SearchParameterUtil;
import ca.uhn.fhir.util.UrlUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

public abstract class BaseJpaBulkItemReader extends BaseBulkItemReader {
	@Value("#{jobExecutionContext['" + BatchConstants.JOB_UUID_PARAMETER + "']}")
	protected String myJobUUID;
	@Autowired
	protected DaoRegistry myDaoRegistry;
	@Autowired
	protected SearchBuilderFactory mySearchBuilderFactory;
	@Autowired
	private IBulkExportJobDao myBulkExportJobDao;

	private ISearchBuilder mySearchBuilder;
	private BulkExportJobEntity myJobEntity;

	private RuntimeSearchParam myPatientSearchParam;

	/**
	 * Get and cache an ISearchBuilder for the given resource type this partition is responsible for.
	 */
	protected ISearchBuilder getSearchBuilderForLocalResourceType() {
		if (mySearchBuilder == null) {
			IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(myResourceType);
			RuntimeResourceDefinition def = myContext.getResourceDefinition(myResourceType);
			Class<? extends IBaseResource> nextTypeClass = def.getImplementingClass();
			mySearchBuilder = mySearchBuilderFactory.newSearchBuilder(dao, myResourceType, nextTypeClass);
		}
		return mySearchBuilder;
	}

	@Override
	protected String[] getTypeFilterList() {
		BulkExportJobEntity jobEntity = getJobEntity();
		Map<String, String[]> requestUrl = UrlUtil.parseQueryStrings(jobEntity.getRequest());
		return requestUrl.get(JpaConstants.PARAM_EXPORT_TYPE_FILTER);
	}

	@Override
	protected Date getSinceDate() {
		return getJobEntity().getSince();
	}

	@Override
	protected String getLogInfoForRead() {
		return "Bulk export starting generation for batch export job: " + getJobEntity() + " with resourceType " + myResourceType + " and UUID " + myJobUUID;
	}

	protected BulkExportJobEntity getJobEntity() {
		if (myJobEntity == null) {
			Optional<BulkExportJobEntity> jobOpt = myBulkExportJobDao.findByJobId(myJobUUID);
			if (jobOpt.isPresent()) {
				myJobEntity = jobOpt.get();
			} else {
				String errorMessage = String.format("Job with UUID %s does not exist!", myJobUUID);
				throw new IllegalStateException(Msg.code(795) + errorMessage);
			}
		}
		return myJobEntity;
	}

	protected RuntimeSearchParam getPatientSearchParamForCurrentResourceType() {
		if (myPatientSearchParam == null) {
			Optional<RuntimeSearchParam> onlyPatientSearchParamForResourceType = SearchParameterUtil.getOnlyPatientSearchParamForResourceType(myContext, myResourceType);
			if (onlyPatientSearchParamForResourceType.isPresent()) {
				myPatientSearchParam = onlyPatientSearchParamForResourceType.get();
			}
		}
		return myPatientSearchParam;
	}
}
