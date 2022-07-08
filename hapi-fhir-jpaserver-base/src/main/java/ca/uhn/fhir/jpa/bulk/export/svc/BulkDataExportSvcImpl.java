package ca.uhn.fhir.jpa.bulk.export.svc;

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
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkDataExportSvc;
import ca.uhn.fhir.jpa.bulk.export.model.BulkExportJobStatusEnum;
import ca.uhn.fhir.jpa.dao.data.IBulkExportCollectionDao;
import ca.uhn.fhir.jpa.dao.data.IBulkExportJobDao;
import ca.uhn.fhir.jpa.entity.BulkExportCollectionEntity;
import ca.uhn.fhir.jpa.entity.BulkExportCollectionFileEntity;
import ca.uhn.fhir.jpa.entity.BulkExportJobEntity;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.util.SearchParameterUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.InstantType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions.ExportStyle.GROUP;
import static ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions.ExportStyle.PATIENT;
import static ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions.ExportStyle.SYSTEM;
import static ca.uhn.fhir.util.UrlUtil.escapeUrlParam;
import static ca.uhn.fhir.util.UrlUtil.escapeUrlParams;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class BulkDataExportSvcImpl implements IBulkDataExportSvc {

	private static final Logger ourLog = LoggerFactory.getLogger(BulkDataExportSvcImpl.class);
	private final int myReuseBulkExportForMillis = (int) (60 * DateUtils.MILLIS_PER_MINUTE);

	@Autowired
	private IBulkExportJobDao myBulkExportJobDao;
	@Autowired
	private IBulkExportCollectionDao myBulkExportCollectionDao;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private FhirContext myContext;
	@Autowired
	private DaoConfig myDaoConfig;

	private Set<String> myCompartmentResources;


	@Transactional
	@Override
	@Deprecated
	public JobInfo submitJob(BulkDataExportOptions theBulkDataExportOptions) {
		return submitJob(theBulkDataExportOptions, true, null);
	}

	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	@Transactional
	@Override
	public JobInfo submitJob(BulkDataExportOptions theBulkDataExportOptions, Boolean useCache, RequestDetails theRequestDetails) {
		String outputFormat = Constants.CT_FHIR_NDJSON;
		if (isNotBlank(theBulkDataExportOptions.getOutputFormat())) {
			outputFormat = theBulkDataExportOptions.getOutputFormat();
		}
		if (!Constants.CTS_NDJSON.contains(outputFormat)) {
			throw new InvalidRequestException(Msg.code(786) + "Invalid output format: " + theBulkDataExportOptions.getOutputFormat());
		}

		// Interceptor call: STORAGE_INITIATE_BULK_EXPORT
		HookParams params = new HookParams()
			.add(BulkDataExportOptions.class, theBulkDataExportOptions)
			.add(RequestDetails.class, theRequestDetails)
			.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
		CompositeInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequestDetails, Pointcut.STORAGE_INITIATE_BULK_EXPORT, params);

		// TODO GGG KS can we encode BulkDataExportOptions as a JSON string as opposed to this request string.  Feels like it would be a more extensible encoding...
		//Probably yes, but this will all need to be rebuilt when we remove this bridge entity
		StringBuilder requestBuilder = new StringBuilder();
		requestBuilder.append("/");

		//Prefix the export url with Group/[id]/ or /Patient/ depending on what type of request it is.
		if (theBulkDataExportOptions.getExportStyle().equals(GROUP)) {
			requestBuilder.append(theBulkDataExportOptions.getGroupId().toVersionless()).append("/");
		} else if (theBulkDataExportOptions.getExportStyle().equals(PATIENT)) {
			requestBuilder.append("Patient/");
		}

		requestBuilder.append(JpaConstants.OPERATION_EXPORT);
		requestBuilder.append("?").append(JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT).append("=").append(escapeUrlParam(outputFormat));
		Set<String> resourceTypes = theBulkDataExportOptions.getResourceTypes();
		if (resourceTypes != null) {
			requestBuilder.append("&").append(JpaConstants.PARAM_EXPORT_TYPE).append("=").append(String.join(",", escapeUrlParams(resourceTypes)));
		}
		Date since = theBulkDataExportOptions.getSince();
		if (since != null) {
			requestBuilder.append("&").append(JpaConstants.PARAM_EXPORT_SINCE).append("=").append(new InstantType(since).setTimeZoneZulu(true).getValueAsString());
		}
		if (theBulkDataExportOptions.getFilters() != null && theBulkDataExportOptions.getFilters().size() > 0) {
			theBulkDataExportOptions.getFilters().stream()
				.forEach(filter -> requestBuilder.append("&").append(JpaConstants.PARAM_EXPORT_TYPE_FILTER).append("=").append(escapeUrlParam(filter)));
		}

		if (theBulkDataExportOptions.getExportStyle().equals(GROUP)) {
			requestBuilder.append("&").append(JpaConstants.PARAM_EXPORT_GROUP_ID).append("=").append(theBulkDataExportOptions.getGroupId().getValue());
			requestBuilder.append("&").append(JpaConstants.PARAM_EXPORT_MDM).append("=").append(theBulkDataExportOptions.isExpandMdm());
		}

		String request = requestBuilder.toString();

		//If we are using the cache, then attempt to retrieve a matching job based on the Request String, otherwise just make a new one.
		if (useCache) {
			Date cutoff = DateUtils.addMilliseconds(new Date(), -myReuseBulkExportForMillis);
			Pageable page = PageRequest.of(0, 10);
			Slice<BulkExportJobEntity> existing = myBulkExportJobDao.findExistingJob(page, request, cutoff, BulkExportJobStatusEnum.ERROR);
			if (!existing.isEmpty()) {
				return toSubmittedJobInfo(existing.iterator().next());
			}
		}

		if (resourceTypes != null && resourceTypes.contains("Binary")) {
			String msg = myContext.getLocalizer().getMessage(BulkDataExportSvcImpl.class, "onlyBinarySelected");
			throw new InvalidRequestException(Msg.code(787) + msg);
		}

		if (resourceTypes == null || resourceTypes.isEmpty()) {
			// This is probably not a useful default, but having the default be "download the whole
			// server" seems like a risky default too. We'll deal with that by having the default involve
			// only returning a small time span
			resourceTypes = getAllowedResourceTypesForBulkExportStyle(theBulkDataExportOptions.getExportStyle());
			if (since == null) {
				since = DateUtils.addDays(new Date(), -1);
			}
		}

		resourceTypes =
			resourceTypes
				.stream()
				.filter(t -> !"Binary".equals(t))
				.collect(Collectors.toSet());

		BulkExportJobEntity jobEntity = new BulkExportJobEntity();
		jobEntity.setJobId(UUID.randomUUID().toString());
		jobEntity.setStatus(BulkExportJobStatusEnum.SUBMITTED);
		jobEntity.setSince(since);
		jobEntity.setCreated(new Date());
		jobEntity.setRequest(request);

		// Validate types
		validateTypes(resourceTypes);
		validateTypeFilters(theBulkDataExportOptions.getFilters(), resourceTypes);

		updateExpiry(jobEntity);
		myBulkExportJobDao.save(jobEntity);

		for (String nextType : resourceTypes) {

			BulkExportCollectionEntity collection = new BulkExportCollectionEntity();
			collection.setJob(jobEntity);
			collection.setResourceType(nextType);
			jobEntity.getCollections().add(collection);
			myBulkExportCollectionDao.save(collection);
		}

		ourLog.info("Bulk export job submitted: {}", jobEntity.toString());

		return toSubmittedJobInfo(jobEntity);
	}

	public void validateTypes(Set<String> theResourceTypes) {
		for (String nextType : theResourceTypes) {
			if (!myDaoRegistry.isResourceTypeSupported(nextType)) {
				String msg = myContext.getLocalizer().getMessage(BulkDataExportSvcImpl.class, "unknownResourceType", nextType);
				throw new InvalidRequestException(Msg.code(788) + msg);
			}
		}
	}

	public void validateTypeFilters(Set<String> theTheFilters, Set<String> theResourceTypes) {
		if (theTheFilters != null) {
			for (String next : theTheFilters) {
				if (!next.contains("?")) {
					throw new InvalidRequestException(Msg.code(789) + "Invalid " + JpaConstants.PARAM_EXPORT_TYPE_FILTER + " value \"" + next + "\". Must be in the form [ResourceType]?[params]");
				}
				String resourceType = next.substring(0, next.indexOf("?"));
				if (!theResourceTypes.contains(resourceType)) {
					throw new InvalidRequestException(Msg.code(790) + "Invalid " + JpaConstants.PARAM_EXPORT_TYPE_FILTER + " value \"" + next + "\". Resource type does not appear in " + JpaConstants.PARAM_EXPORT_TYPE+ " list");
				}
			}
		}
	}

	private JobInfo toSubmittedJobInfo(BulkExportJobEntity theJob) {
		return new JobInfo()
			.setJobId(theJob.getJobId())
			.setStatus(theJob.getStatus());
	}

	/**
	 * If the retention period is set to <= 0, set it to null, which prevents it from getting expired, otherwise, set
	 * the retention period.
	 *
	 * @param theJob the job to update the expiry for.
	 */
	private void updateExpiry(BulkExportJobEntity theJob) {
		if (myDaoConfig.getBulkExportFileRetentionPeriodHours() > 0) {
			theJob.setExpiry(DateUtils.addHours(new Date(), myDaoConfig.getBulkExportFileRetentionPeriodHours()));
		} else {
			theJob.setExpiry(null);
		}
	}

	@Transactional
	@Override
	public JobInfo getJobInfoOrThrowResourceNotFound(String theJobId) {
		BulkExportJobEntity job = myBulkExportJobDao
			.findByJobId(theJobId)
			.orElseThrow(() -> new ResourceNotFoundException(theJobId));

		JobInfo retVal = new JobInfo();
		retVal.setJobId(theJobId);
		retVal.setStatus(job.getStatus());
		retVal.setStatus(job.getStatus());
		retVal.setStatusTime(job.getStatusTime());
		retVal.setStatusMessage(job.getStatusMessage());
		retVal.setRequest(job.getRequest());

		if (job.getStatus() == BulkExportJobStatusEnum.COMPLETE) {
			for (BulkExportCollectionEntity nextCollection : job.getCollections()) {
				for (BulkExportCollectionFileEntity nextFile : nextCollection.getFiles()) {
					retVal.addFile()
						.setResourceType(nextCollection.getResourceType())
						.setResourceId(toQualifiedBinaryId(nextFile.getResourceId()));
				}
			}
		}

		return retVal;
	}

	@Override
	public Set<String> getPatientCompartmentResources() {
		if (myCompartmentResources == null) {
			myCompartmentResources = myContext.getResourceTypes().stream()
				.filter(resType -> SearchParameterUtil.isResourceTypeInPatientCompartment(myContext, resType))
				.collect(Collectors.toSet());
		}
		return myCompartmentResources;
	}

	public Set<String> getAllowedResourceTypesForBulkExportStyle(BulkDataExportOptions.ExportStyle theExportStyle) {
		if (theExportStyle.equals(SYSTEM)) {
			return myContext.getResourceTypes();
		} else if (theExportStyle.equals(GROUP) || theExportStyle.equals(PATIENT)) {
			return getPatientCompartmentResources();
		} else {
			throw new IllegalArgumentException(Msg.code(791) + String.format("HAPI FHIR does not recognize a Bulk Export request of type: %s", theExportStyle));
		}
	}

	private IIdType toQualifiedBinaryId(String theIdPart) {
		IIdType retVal = myContext.getVersion().newIdType();
		retVal.setParts(null, "Binary", theIdPart, null);

		return retVal;
	}
}
