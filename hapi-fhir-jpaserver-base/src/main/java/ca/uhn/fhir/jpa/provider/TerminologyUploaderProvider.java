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
package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.batch2.api.AttachmentContentTypeEnum;
import ca.uhn.fhir.batch2.api.AttachmentDetails;
import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.util.AsyncRequestUtil;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyJobParameters;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyResultJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants;
import ca.uhn.fhir.jpa.batch2.jobs.term.custom.ImportCustomTerminologyJobAppCtx;
import ca.uhn.fhir.jpa.batch2.jobs.term.icd.ImportIcdJobAppCtx;
import ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincJobAppCtx;
import ca.uhn.fhir.jpa.batch2.jobs.term.snomedct.ImportSnomedCtJobAppCtx;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.term.UploadStatistics;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.term.custom.CodeSystemToCustomCsvConverter;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.ServletRequestUtil;
import ca.uhn.fhir.util.AttachmentUtil;
import ca.uhn.fhir.util.DatatypeUtil;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.fhir.util.ParametersUtil;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.util.ValidateUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.ICompositeType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

import static ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants.CUSTOM_CONCEPTS_FILE;
import static ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants.CUSTOM_HIERARCHY_FILE;
import static ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants.CUSTOM_PROPERTIES_FILE;
import static ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants.FILENAME_ICD10CM_DISTRIBUTION_FILE;
import static ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants.FILENAME_ICD10_DISTRIBUTION_FILE;
import static ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants.FILENAME_LOINC_DISTRIBUTION_FILE;
import static ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants.FILENAME_LOINC_UPLOAD_PROPERTIES_FILE;
import static ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants.FILENAME_SNOMED_CT_DISTRIBUTION_FILE;
import static ca.uhn.fhir.jpa.batch2.jobs.term.icd.icd10.ImportIcd10Step2HandleConcepts.ICD10_XML_FILENAME;
import static ca.uhn.fhir.jpa.batch2.jobs.term.icd.icd10.ImportIcd10Step2HandleConcepts.ICD10_XML_FILE_PATTERN;
import static ca.uhn.fhir.jpa.batch2.jobs.term.icd.icd10cm.ImportIcd10CmStep2HandleConcepts.ICD10CM_FILENAME;
import static ca.uhn.fhir.jpa.batch2.jobs.term.icd.icd10cm.ImportIcd10CmStep2HandleConcepts.ICD10CM_FILE_PATTERN;
import static ca.uhn.fhir.jpa.model.util.JpaConstants.OPERATION_UPLOAD_TERMINOLOGY_START_JOB;
import static ca.uhn.fhir.rest.server.RestfulServerUtils.createFullyQualifiedUrlFromRelativeUrl;
import static ca.uhn.fhir.util.DatatypeUtil.toStringValue;
import static ca.uhn.fhir.util.DatatypeUtil.toStringValueOrEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class TerminologyUploaderProvider extends BaseJpaProvider {

	public static final String PARAM_FILE = "file";
	public static final String PARAM_CODESYSTEM = "codeSystem";
	public static final String PARAM_SYSTEM = "system";
	public static final String PARAM_VERSION = "version";
	public static final String PARAM_FILENAME = "filename";
	public static final String PARAM_JOB_INSTANCE_ID = "jobInstanceId";
	public static final String PARAM_MAKE_CURRENT = "makeCurrent";
	public static final String PARAM_JOB_ATTACHMENT_ID = "jobAttachmentId";
	public static final String RESP_PARAM_OUTCOME = "outcome";
	public static final Pattern LOINC_XML_FILENAME_PATTERN =
			Pattern.compile("loinc[0-9._-]*\\.zip", Pattern.CASE_INSENSITIVE);
	public static final Pattern SNOMED_CT_XML_FILENAME_PATTERN =
			Pattern.compile("snomed[a-zA-Z0-9._-]*\\.zip", Pattern.CASE_INSENSITIVE);
	public static final Pattern ICD10_FILENAME_PATTERN = Pattern.compile("icd10.*\\.zip", Pattern.CASE_INSENSITIVE);

	public static final Pattern ICD10CM_FILENAME_PATTERN = Pattern.compile("icd10cm.*\\.zip", Pattern.CASE_INSENSITIVE);
	public static final Pattern CUSTOM_TERMINOLOGY_PATTERN = Pattern.compile(".*\\.zip", Pattern.CASE_INSENSITIVE);
	private static final Logger ourLog = LoggerFactory.getLogger(TerminologyUploaderProvider.class);
	private static final String RESP_PARAM_CONCEPT_COUNT = "conceptCount";
	private static final String RESP_PARAM_TARGET = "target";
	private static final String RESP_PARAM_SUCCESS = "success";
	private final Map<String, JobType> myCanonicalUrlToJobType = new HashMap<>();
	private final Map<String, JobType> myJobDefinitionIdToJobType = new HashMap<>();
	private final JobType myCustomJobType;
	private CodeSystemToCustomCsvConverter myCodeSystemToCustomCsvConverter;

	@Autowired
	private ITermLoaderSvc myTerminologyLoaderSvc;

	@Autowired
	private IJobCoordinator myJobCoordinator;

	@Autowired
	private IJobPersistence myJobPersistence;

	/**
	 * Constructor
	 */
	public TerminologyUploaderProvider() {
		this(null, null, null, null);
	}

	/**
	 * Constructor
	 */
	public TerminologyUploaderProvider(
			FhirContext theContext,
			ITermLoaderSvc theTerminologyLoaderSvc,
			IJobCoordinator theJobCoordinator,
			IJobPersistence theJobPersistence) {
		setContext(theContext);
		myTerminologyLoaderSvc = theTerminologyLoaderSvc;
		myJobCoordinator = theJobCoordinator;
		myJobPersistence = theJobPersistence;

		// LOINC
		List<DistributionFilenamePattern> loincDistributionFiles = List.of(
				new DistributionFilenamePattern(
						LOINC_XML_FILENAME_PATTERN, FILENAME_LOINC_DISTRIBUTION_FILE, AttachmentContentTypeEnum.ZIP),
				new DistributionFilenamePattern(
						filenameToPattern(FILENAME_LOINC_UPLOAD_PROPERTIES_FILE),
						FILENAME_LOINC_UPLOAD_PROPERTIES_FILE,
						AttachmentContentTypeEnum.PROPERTIES));
		JobType loincJobType = new JobType(
				ImportLoincJobAppCtx.JOB_ID_IMPORT_TERM_LOINC,
				"LOINC",
				FILENAME_LOINC_UPLOAD_PROPERTIES_FILE,
				loincDistributionFiles);
		myCanonicalUrlToJobType.put(TerminologyConstants.LOINC_URI, loincJobType);
		myJobDefinitionIdToJobType.put(loincJobType.jobDefinitionId(), loincJobType);

		// SNOMED CT
		JobType sctJobType = new JobType(
				ImportSnomedCtJobAppCtx.JOB_ID_IMPORT_TERM_SNOMED_CT,
				"SNOMED CT",
				null,
				SNOMED_CT_XML_FILENAME_PATTERN,
				FILENAME_SNOMED_CT_DISTRIBUTION_FILE);
		myCanonicalUrlToJobType.put(TerminologyConstants.SCT_URI, sctJobType);
		myJobDefinitionIdToJobType.put(sctJobType.jobDefinitionId(), sctJobType);

		// ICD-10
		List<DistributionFilenamePattern> icd10DistributionFiles = List.of(
				new DistributionFilenamePattern(
						ICD10_FILENAME_PATTERN, FILENAME_ICD10_DISTRIBUTION_FILE, AttachmentContentTypeEnum.ZIP),
				new DistributionFilenamePattern(
						ICD10_XML_FILE_PATTERN, ICD10_XML_FILENAME, AttachmentContentTypeEnum.XML));
		JobType icd10JobType =
				new JobType(ImportIcdJobAppCtx.JOB_ID_IMPORT_ICD_10, "ICD-10", null, icd10DistributionFiles);
		myCanonicalUrlToJobType.put(TerminologyConstants.ICD10_URI, icd10JobType);
		myJobDefinitionIdToJobType.put(icd10JobType.jobDefinitionId(), icd10JobType);

		// ICD-10-CM
		List<DistributionFilenamePattern> icd10CpDistributionFiles = List.of(
				new DistributionFilenamePattern(
						ICD10CM_FILENAME_PATTERN, FILENAME_ICD10CM_DISTRIBUTION_FILE, AttachmentContentTypeEnum.ZIP),
				new DistributionFilenamePattern(ICD10CM_FILE_PATTERN, ICD10CM_FILENAME, AttachmentContentTypeEnum.XML));
		JobType icd10cmJobType =
				new JobType(ImportIcdJobAppCtx.JOB_ID_IMPORT_ICD_10_CM, "ICD-10-CM", null, icd10CpDistributionFiles);
		myCanonicalUrlToJobType.put(TerminologyConstants.ICD10CM_URI, icd10cmJobType);
		myJobDefinitionIdToJobType.put(icd10cmJobType.jobDefinitionId(), icd10cmJobType);

		// Custom
		List<DistributionFilenamePattern> customDistributionFiles = List.of(
				new DistributionFilenamePattern(
						CUSTOM_TERMINOLOGY_PATTERN,
						TerminologyConstants.FILENAME_CUSTOM_DISTRIBUTION_FILE,
						AttachmentContentTypeEnum.ZIP),
				new DistributionFilenamePattern(
						Pattern.compile("concepts.*\\.csv", Pattern.CASE_INSENSITIVE),
						CUSTOM_CONCEPTS_FILE,
						AttachmentContentTypeEnum.CSV),
				new DistributionFilenamePattern(
						Pattern.compile("properties.*\\.csv", Pattern.CASE_INSENSITIVE),
						CUSTOM_PROPERTIES_FILE,
						AttachmentContentTypeEnum.CSV),
				new DistributionFilenamePattern(
						Pattern.compile("hierarchy.*\\.csv", Pattern.CASE_INSENSITIVE),
						CUSTOM_HIERARCHY_FILE,
						AttachmentContentTypeEnum.CSV));
		myCustomJobType = new JobType(
				ImportCustomTerminologyJobAppCtx.JOB_ID_IMPORT_CUSTOM_TERMINOLOGY,
				"Custom Terminology",
				null,
				customDistributionFiles);
		myJobDefinitionIdToJobType.put(myCustomJobType.jobDefinitionId(), myCustomJobType);
	}

	@PostConstruct
	public void startIfNecessary() {
		if (myCodeSystemToCustomCsvConverter == null && getContext() != null) {
			myCodeSystemToCustomCsvConverter = new CodeSystemToCustomCsvConverter(getContext());
		}
	}

	@Override
	public void setContext(FhirContext theContext) {
		super.setContext(theContext);
		startIfNecessary();
	}

	/**
	 * <code>$hapi.fhir.upload-terminology.create-job</code>
	 * <p></p>
	 * This method is intended to replace the legacy "uploadSnapshot" method
	 */
	@Operation(
			typeName = "CodeSystem",
			name = JpaConstants.OPERATION_UPLOAD_TERMINOLOGY_CREATE_JOB,
			idempotent = false,
			returnParameters = {
				@OperationParam(name = RESP_PARAM_OUTCOME, typeName = "string", min = 1),
				@OperationParam(name = PARAM_JOB_INSTANCE_ID, typeName = "code", min = 1)
			})
	public IBaseParameters uploadTerminologyCreateJob(
			@OperationParam(name = PARAM_SYSTEM, min = 1, typeName = "uri") IPrimitiveType<String> theCodeSystemUrl,
			@OperationParam(name = PARAM_VERSION, min = 0, typeName = "code")
					IPrimitiveType<String> theCodeSystemVersion,
			@OperationParam(name = PARAM_MAKE_CURRENT, typeName = "boolean", min = 0)
					IPrimitiveType<Boolean> theMakeCurrent,
			ServletRequestDetails theRequestDetails) {

		String url = toStringValue(theCodeSystemUrl);
		if (isBlank(url)) {
			throw new InvalidRequestException(Msg.code(2943) + "Missing required parameter: " + PARAM_SYSTEM);
		}

		UrlUtil.CanonicalUrlParts canonicalUrl = UrlUtil.parseCanonicalUrl(url, toStringValue(theCodeSystemVersion));

		JobType jobType = myCanonicalUrlToJobType.get(canonicalUrl.url());
		if (jobType == null) {
			jobType = myCustomJobType;
		}

		return startImportTerminologyJob(theMakeCurrent, theRequestDetails, canonicalUrl, jobType);
	}

	@Nonnull
	private IBaseParameters startImportTerminologyJob(
			IPrimitiveType<Boolean> theMakeCurrent,
			ServletRequestDetails theRequestDetails,
			UrlUtil.CanonicalUrlParts canonicalUrl,
			JobType theJobType) {
		String terminologyName = theJobType.terminologyName();
		String jobDefinitionId = theJobType.jobDefinitionId();

		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(jobDefinitionId);
		ImportTerminologyJobParameters parameters = new ImportTerminologyJobParameters();
		parameters.setUrl(canonicalUrl.url());
		parameters.setVersionId(canonicalUrl.versionId().orElse(null));

		Boolean makeCurrent = DatatypeUtil.toBooleanValue(theMakeCurrent);
		if (makeCurrent != null && !makeCurrent) {
			parameters.setDontMakeCurrent(true);
		}

		startRequest.setParameters(parameters);
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(theRequestDetails, startRequest);
		String instanceId = startResponse.getInstanceId();

		StringBuilder description = new StringBuilder();
		description.append("Upload ");
		description.append(terminologyName);
		description.append(" Job has been created and is in BUILDING state with ID[");
		description.append(instanceId);
		description.append("]. You can now upload the distribution file");
		if (theJobType.distributionFilenamePatterns().size() > 1) {
			description.append("(s)");
		}
		description.append(" (");
		for (Iterator<DistributionFilenamePattern> iterator =
						theJobType.distributionFilenamePatterns().iterator();
				iterator.hasNext(); ) {
			description.append(iterator.next().jobFilename());
			if (iterator.hasNext()) {
				description.append(", ");
			}
		}
		description.append(")");
		description.append(" to the job using the ");
		description.append(createFullyQualifiedUrlFromRelativeUrl(
				theRequestDetails, "CodeSystem/" + JpaConstants.OPERATION_UPLOAD_TERMINOLOGY_ATTACH_FILE));
		description.append(" operation, and then start the job using the ");
		description.append(createFullyQualifiedUrlFromRelativeUrl(
				theRequestDetails, "CodeSystem/" + OPERATION_UPLOAD_TERMINOLOGY_START_JOB));
		description.append(" operation.");

		IBaseParameters response = ParametersUtil.newInstance(getContext());
		ParametersUtil.addParameterToParametersString(
				getContext(), response, RESP_PARAM_OUTCOME, description.toString());
		ParametersUtil.addParameterToParametersCode(getContext(), response, PARAM_JOB_INSTANCE_ID, instanceId);
		return response;
	}

	/**
	 * <code>$hapi.fhir.upload-terminology.attach-file</code>
	 */
	@Operation(
			typeName = "CodeSystem",
			name = JpaConstants.OPERATION_UPLOAD_TERMINOLOGY_ATTACH_FILE,
			idempotent = false,
			manualRequest = true,
			returnParameters = {
				@OperationParam(name = RESP_PARAM_OUTCOME, typeName = "string", min = 1),
				@OperationParam(name = PARAM_JOB_ATTACHMENT_ID, typeName = "code", min = 1)
			})
	public IBaseParameters uploadTerminologyAttachFile(
			@OperationParam(name = PARAM_JOB_INSTANCE_ID, min = 1, typeName = "code")
					IPrimitiveType<String> theJobInstanceId,
			@OperationParam(name = PARAM_FILENAME, min = 0, typeName = "code") IPrimitiveType<String> theFilename,
			HttpServletRequest theServletRequest,
			ServletRequestDetails theRequestDetails) {

		try (InputStream inputStream = theServletRequest.getInputStream()) {
			JobInstance jobInstance = myJobCoordinator.getInstance(toStringValue(theJobInstanceId));
			validateJobIsInBuildingStatus(jobInstance);

			JobType jobType = myJobDefinitionIdToJobType.get(jobInstance.getJobDefinitionId());
			if (jobType != null) {
				AttachmentDetails attachmentDetails = null;
				String filename = toStringValueOrEmpty(theFilename);

				for (DistributionFilenamePattern pattern : jobType.distributionFilenamePatterns()) {
					if (pattern.pattern().matcher(filename).find()) {
						attachmentDetails =
								new AttachmentDetails(inputStream, pattern.contentType(), pattern.jobFilename());
						break;
					}
				}

				if (attachmentDetails == null) {
					throw new InvalidRequestException(Msg.code(2953) + "File named \"" + toStringValue(theFilename)
							+ "\" is not valid for import " + jobType.terminologyName() + " job");
				}

				String instanceId = jobInstance.getInstanceId();

				String attachmentId = myJobPersistence.storeNewAttachment(instanceId, attachmentDetails);

				StringBuilder description = new StringBuilder();
				description.append("Attachment with ID[");
				description.append(attachmentId);
				description.append("] has been stored for job with ID[");
				description.append(instanceId);
				description.append("].");

				IBaseParameters response = ParametersUtil.newInstance(getContext());
				ParametersUtil.addParameterToParametersString(
						getContext(), response, RESP_PARAM_OUTCOME, description.toString());
				ParametersUtil.addParameterToParametersCode(
						getContext(), response, PARAM_JOB_ATTACHMENT_ID, attachmentId);
				return response;
			}
		} catch (IOException e) {
			ourLog.warn(
					"Failed to stream job attachment for job instance[{}]: {}", theJobInstanceId, e.getMessage(), e);
			throw new InvalidRequestException(
					Msg.code(2945) + "IO failure while streaming job attachment: " + e.getMessage(), e);
		}

		throw new InvalidRequestException(Msg.code(2946) + "Can't attach files to this job");
	}

	/**
	 * <code>$hapi.fhir.upload-terminology.start-job</code>
	 */
	@Operation(
			typeName = "CodeSystem",
			name = OPERATION_UPLOAD_TERMINOLOGY_START_JOB,
			manualResponse = true,
			idempotent = false)
	public void uploadTerminologyStartJob(
			@OperationParam(name = PARAM_JOB_INSTANCE_ID, min = 1, typeName = "code")
					IPrimitiveType<String> theJobInstanceId,
			ServletRequestDetails theRequestDetails)
			throws IOException {

		ServletRequestUtil.validatePreferAsyncHeader(theRequestDetails, OPERATION_UPLOAD_TERMINOLOGY_START_JOB);

		JobInstance jobInstance = myJobCoordinator.getInstance(toStringValue(theJobInstanceId));
		validateJobIsInBuildingStatus(jobInstance);

		JobType jobType = myJobDefinitionIdToJobType.get(jobInstance.getJobDefinitionId());
		if (jobType != null) {
			ourLog.info(
					"Starting Upload Terminology job[{}] of type: {}",
					jobInstance.getInstanceId(),
					jobInstance.getJobDefinitionId());
			myJobCoordinator.enqueueBuildingJobForExecution(jobInstance.getInstanceId());
		} else {
			throw new InvalidRequestException(Msg.code(2947) + "Can't start job of this type");
		}

		String pollUrl = "CodeSystem/" + JpaConstants.OPERATION_UPLOAD_TERMINOLOGY_POLL_FOR_STATUS + "?"
				+ PARAM_JOB_INSTANCE_ID + "=" + jobInstance.getInstanceId();
		AsyncRequestUtil.handleAsynchronousOperationStartRequest(
				theRequestDetails, pollUrl, OPERATION_UPLOAD_TERMINOLOGY_START_JOB, null);
	}

	/**
	 * <code>$hapi.fhir.upload-terminology.poll-for-status</code>
	 */
	@Operation(
			typeName = "CodeSystem",
			name = JpaConstants.OPERATION_UPLOAD_TERMINOLOGY_POLL_FOR_STATUS,
			manualResponse = true,
			idempotent = true)
	public void uploadTerminologyPollForStatus(
			@OperationParam(name = PARAM_JOB_INSTANCE_ID, min = 1, typeName = "code")
					IPrimitiveType<String> theJobInstanceId,
			ServletRequestDetails theRequestDetails)
			throws IOException {

		JobInstance jobInstance = myJobCoordinator.getInstance(toStringValue(theJobInstanceId));

		JobType jobType = myJobDefinitionIdToJobType.get(jobInstance.getJobDefinitionId());
		if (jobType != null) {
			Function<JobInstance, AsyncRequestUtil.CompletedJobPollResponse> completedDetailsProvider = instance -> {
				ImportTerminologyResultJson resultJson =
						JsonUtil.deserialize(jobInstance.getReport(), ImportTerminologyResultJson.class);
				String report = resultJson.getReport();
				return new AsyncRequestUtil.CompletedJobPollResponse(null, List.of(report));
			};
			AsyncRequestUtil.handleAsyncJobPollForStatusResponse(
					theRequestDetails, jobInstance, OPERATION_UPLOAD_TERMINOLOGY_START_JOB, completedDetailsProvider);

		} else {
			throw new InvalidRequestException(Msg.code(2948) + "Can't use this operation to poll status of this job");
		}
	}

	/**
	 * <code>
	 * $apply-codesystem-delta-add
	 * </code>
	 */
	@Operation(
			typeName = "CodeSystem",
			name = JpaConstants.OPERATION_APPLY_CODESYSTEM_DELTA_ADD,
			idempotent = false,
			returnParameters = {})
	public IBaseParameters uploadDeltaAdd(
			HttpServletRequest theServletRequest,
			@OperationParam(name = PARAM_SYSTEM, min = 1, max = 1, typeName = "uri") IPrimitiveType<String> theSystem,
			@OperationParam(name = PARAM_FILE, min = 0, max = OperationParam.MAX_UNLIMITED, typeName = "attachment")
					List<ICompositeType> theFiles,
			@OperationParam(
							name = PARAM_CODESYSTEM,
							min = 0,
							max = OperationParam.MAX_UNLIMITED,
							typeName = "CodeSystem")
					List<IBaseResource> theCodeSystems,
			RequestDetails theRequestDetails) {

		startRequest(theServletRequest);
		try {
			validateHaveSystem(theSystem);
			validateHaveFiles(theFiles, theCodeSystems);

			List<ITermLoaderSvc.FileDescriptor> files = convertAttachmentsToFileDescriptors(theFiles);
			files.addAll(myCodeSystemToCustomCsvConverter.convertCodeSystemsToFileDescriptors(theCodeSystems));
			UploadStatistics outcome =
					myTerminologyLoaderSvc.loadDeltaAdd(theSystem.getValue(), files, theRequestDetails);
			return toDeltaResponse(outcome);
		} finally {
			endRequest(theServletRequest);
		}
	}

	/**
	 * <code>
	 * $apply-codesystem-delta-remove
	 * </code>
	 */
	@Operation(
			typeName = "CodeSystem",
			name = JpaConstants.OPERATION_APPLY_CODESYSTEM_DELTA_REMOVE,
			idempotent = false,
			returnParameters = {})
	public IBaseParameters uploadDeltaRemove(
			HttpServletRequest theServletRequest,
			@OperationParam(name = PARAM_SYSTEM, min = 1, max = 1, typeName = "uri") IPrimitiveType<String> theSystem,
			@OperationParam(name = PARAM_FILE, min = 0, max = OperationParam.MAX_UNLIMITED, typeName = "attachment")
					List<ICompositeType> theFiles,
			@OperationParam(
							name = PARAM_CODESYSTEM,
							min = 0,
							max = OperationParam.MAX_UNLIMITED,
							typeName = "CodeSystem")
					List<IBaseResource> theCodeSystems,
			RequestDetails theRequestDetails) {

		startRequest(theServletRequest);
		try {
			validateHaveSystem(theSystem);
			validateHaveFiles(theFiles, theCodeSystems);

			List<ITermLoaderSvc.FileDescriptor> files = convertAttachmentsToFileDescriptors(theFiles);
			files.addAll(myCodeSystemToCustomCsvConverter.convertCodeSystemsToFileDescriptors(theCodeSystems));
			UploadStatistics outcome =
					myTerminologyLoaderSvc.loadDeltaRemove(theSystem.getValue(), files, theRequestDetails);
			return toDeltaResponse(outcome);
		} finally {
			endRequest(theServletRequest);
		}
	}

	private void validateHaveSystem(IPrimitiveType<String> theSystem) {
		if (theSystem == null || isBlank(theSystem.getValueAsString())) {
			throw new InvalidRequestException(Msg.code(1139) + "Missing mandatory parameter: " + PARAM_SYSTEM);
		}
	}

	private void validateHaveFiles(List<ICompositeType> theFiles, List<IBaseResource> theCodeSystems) {
		if (theFiles != null) {
			for (ICompositeType nextFile : theFiles) {
				if (!nextFile.isEmpty()) {
					return;
				}
			}
		}
		if (theCodeSystems != null) {
			for (IBaseResource next : theCodeSystems) {
				if (!next.isEmpty()) {
					return;
				}
			}
		}
		throw new InvalidRequestException(Msg.code(1140) + "Missing mandatory parameter: " + PARAM_FILE);
	}

	@Nonnull
	private List<ITermLoaderSvc.FileDescriptor> convertAttachmentsToFileDescriptors(
			@OperationParam(name = PARAM_FILE, min = 0, max = OperationParam.MAX_UNLIMITED, typeName = "attachment")
					List<ICompositeType> theFiles) {
		List<ITermLoaderSvc.FileDescriptor> files = new ArrayList<>();
		if (theFiles != null) {
			for (ICompositeType next : theFiles) {

				String nextUrl =
						AttachmentUtil.getOrCreateUrl(getContext(), next).getValue();
				ValidateUtil.isNotBlankOrThrowUnprocessableEntity(nextUrl, "Missing Attachment.url value");

				byte[] nextData;
				if (nextUrl.startsWith("localfile:")) {
					String nextLocalFile = nextUrl.substring("localfile:".length());

					if (isNotBlank(nextLocalFile)) {
						ourLog.info("Reading in local file: {}", nextLocalFile);
						File nextFile = new File(nextLocalFile);
						if (!nextFile.exists() || !nextFile.isFile()) {
							throw new InvalidRequestException(Msg.code(1141) + "Unknown file: " + nextFile.getName());
						}
						files.add(new FileBackedFileDescriptor(nextFile));
					}

				} else {
					nextData =
							AttachmentUtil.getOrCreateData(getContext(), next).getValue();
					ValidateUtil.isTrueOrThrowInvalidRequest(
							nextData != null && nextData.length > 0, "Missing Attachment.data value");
					files.add(new ITermLoaderSvc.ByteArrayFileDescriptor(nextUrl, nextData));
				}
			}
		}
		return files;
	}

	private IBaseParameters toDeltaResponse(UploadStatistics theOutcome) {
		IBaseParameters retVal = ParametersUtil.newInstance(getContext());
		ParametersUtil.addParameterToParametersInteger(
				getContext(),
				retVal,
				RESP_PARAM_CONCEPT_COUNT,
				theOutcome.getAddedConceptCount() + theOutcome.getUpdatedConceptCount());
		ParametersUtil.addParameterToParametersReference(
				getContext(), retVal, RESP_PARAM_TARGET, theOutcome.getTarget().getValue());
		return retVal;
	}

	public void setTerminologyLoaderSvc(ITermLoaderSvc theTermLoaderSvc) {
		myTerminologyLoaderSvc = theTermLoaderSvc;
	}

	@Nonnull
	private static Pattern filenameToPattern(String theFilename) {
		return Pattern.compile(theFilename, Pattern.CASE_INSENSITIVE | Pattern.LITERAL);
	}

	private static void validateJobIsInBuildingStatus(JobInstance jobInstance) {
		if (jobInstance.getStatus() != StatusEnum.BUILDING) {
			throw new InvalidRequestException(
					Msg.code(2949) + "Job is not in BUILDING status: " + jobInstance.getStatus());
		}
	}

	private record JobType(
			String jobDefinitionId,
			String terminologyName,
			String propertyFileName,
			List<DistributionFilenamePattern> distributionFilenamePatterns) {

		/**
		 * Distribution which accepts a single ZIP file
		 */
		JobType(
				String jobDefinitionId,
				String terminologyName,
				String propertyFileName,
				Pattern distributionFilenamePattern,
				String distributionFileName) {
			this(
					jobDefinitionId,
					terminologyName,
					propertyFileName,
					List.of(new DistributionFilenamePattern(
							distributionFilenamePattern, distributionFileName, AttachmentContentTypeEnum.ZIP)));
		}
	}

	private record DistributionFilenamePattern(
			Pattern pattern, String jobFilename, AttachmentContentTypeEnum contentType) {}

	public static class FileBackedFileDescriptor implements ITermLoaderSvc.FileDescriptor {
		private final File myNextFile;

		public FileBackedFileDescriptor(File theNextFile) {
			myNextFile = theNextFile;
		}

		@Override
		public String getFilename() {
			return myNextFile.getAbsolutePath();
		}

		@Override
		public InputStream getInputStream() {
			try {
				return new FileInputStream(myNextFile);
			} catch (FileNotFoundException theE) {
				throw new InternalErrorException(Msg.code(1142) + theE);
			}
		}
	}
}
