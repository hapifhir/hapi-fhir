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
import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseTerminologyImportParameters;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyResultJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincJobAppCtx;
import ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincJobParameters;
import ca.uhn.fhir.jpa.batch2.jobs.term.snomedct.ImportSnomedCtJobAppCtx;
import ca.uhn.fhir.jpa.batch2.jobs.term.snomedct.ImportSnomedCtJobParameters;
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
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import static ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants.FILENAME_LOINC_DISTRIBUTION_FILE;
import static ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants.FILENAME_LOINC_UPLOAD_PROPERTIES_FILE;
import static ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants.FILENAME_SNOMED_CT_DISTRIBUTION_FILE;
import static ca.uhn.fhir.rest.server.RestfulServerUtils.createFullyQualifiedUrlFromRelativeUrl;
import static ca.uhn.fhir.util.DatatypeUtil.toStringValue;
import static ca.uhn.fhir.util.DatatypeUtil.toStringValueOrEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

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
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TerminologyUploaderProvider.class);
	private static final String RESP_PARAM_CONCEPT_COUNT = "conceptCount";
	private static final String RESP_PARAM_TARGET = "target";
	private static final String RESP_PARAM_SUCCESS = "success";
	private final Map<String, JobType> myCanonicalUrlToJobType = new HashMap<>();
	private final Map<String, JobType> myJobDefinitionIdToJobType = new HashMap<>();
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

		String loincJobDefinitionId = ImportLoincJobAppCtx.JOB_ID_IMPORT_TERM_LOINC;
		Supplier<BaseTerminologyImportParameters> paramsFactory = ImportLoincJobParameters::new;
		String loincTerminologyName = "LOINC";
		JobType loincJobType = new JobType(LOINC_XML_FILENAME_PATTERN, FILENAME_LOINC_UPLOAD_PROPERTIES_FILE, loincJobDefinitionId, paramsFactory, loincTerminologyName, FILENAME_LOINC_DISTRIBUTION_FILE, FILENAME_LOINC_UPLOAD_PROPERTIES_FILE);
		myCanonicalUrlToJobType.put(ITermLoaderSvc.LOINC_URI, loincJobType);
		myJobDefinitionIdToJobType.put(loincJobDefinitionId, loincJobType);

		String sctJobDefinitionId = ImportSnomedCtJobAppCtx.JOB_ID_IMPORT_TERM_SNOMED_CT;
		Supplier<BaseTerminologyImportParameters> sctParamsFactory = ImportSnomedCtJobParameters::new;
		String sctTerminologyName = "SNOMED CT";
		JobType sctJobType = new JobType(SNOMED_CT_XML_FILENAME_PATTERN, null, sctJobDefinitionId, sctParamsFactory, sctTerminologyName, FILENAME_SNOMED_CT_DISTRIBUTION_FILE, null);
		myCanonicalUrlToJobType.put(ITermLoaderSvc.SCT_URI, sctJobType);
		myJobDefinitionIdToJobType.put(sctJobDefinitionId, sctJobType);
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
	 * This method is intended to replace the legacy {@link #uploadSnapshot(HttpServletRequest, IPrimitiveType, List, RequestDetails)}
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
		if (jobType != null) {
			return startImportTerminologyJob(theMakeCurrent, theRequestDetails, canonicalUrl, jobType);
		}

		throw new InvalidRequestException(Msg.code(2944) + "Unsupported code system: " + canonicalUrl.url());
	}

	@Nonnull
	private IBaseParameters startImportTerminologyJob(IPrimitiveType<Boolean> theMakeCurrent, ServletRequestDetails theRequestDetails, UrlUtil.CanonicalUrlParts canonicalUrl, JobType theJobType) {
		String terminologyName = theJobType.terminologyName();
		String distributionFileName = theJobType.distributionFileName();
		String propertyFileName = theJobType.propertyFileName();
		String jobDefinitionId = theJobType.jobDefinitionId();
		Supplier<BaseTerminologyImportParameters> paramsFactory = theJobType.paramsFactory();

		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(jobDefinitionId);
		BaseTerminologyImportParameters parameters = paramsFactory.get();
		parameters.setVersionId(canonicalUrl.versionId().orElse(null));

		Boolean makeCurrent = DatatypeUtil.toBooleanValue(theMakeCurrent);
		if (makeCurrent != null && !makeCurrent) {
			parameters.setDontMakeCurrent(true);
		}

		startRequest.setParameters(parameters);
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(theRequestDetails, startRequest);
		String instanceId = startResponse.getInstanceId();

		StringBuilder description = new StringBuilder();
		description.append("Upload " + terminologyName + " Job has been created and is in BUILDING state with ID[");
		description.append(instanceId);
		description.append("]. You can now upload the distribution file (");
		description.append(distributionFileName);
		description.append(")");
		if (propertyFileName != null) {
			description.append(" and optionally upload a property file (");
			description.append(propertyFileName);
			description.append(")");
		}
		description.append(" to the job using the ");
		description.append(createFullyQualifiedUrlFromRelativeUrl(
			theRequestDetails, "CodeSystem/" + JpaConstants.OPERATION_UPLOAD_TERMINOLOGY_ATTACH_FILE));
		description.append(" operation, and then start the job using the ");
		description.append(createFullyQualifiedUrlFromRelativeUrl(
			theRequestDetails, "CodeSystem/" + JpaConstants.OPERATION_UPLOAD_TERMINOLOGY_START_JOB));
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
				AttachmentDetails attachmentDetails;
				String filename = toStringValueOrEmpty(theFilename);
				if (jobType.distributionFilenamePattern().matcher(filename).find()) {
					attachmentDetails = new AttachmentDetails(
						inputStream, AttachmentContentTypeEnum.ZIP, jobType.distributionFileName());
				} else if (filename.endsWith(jobType.propertyFileName())) {
					attachmentDetails = new AttachmentDetails(
						inputStream, AttachmentContentTypeEnum.PROPERTIES, jobType.propertyFileName());
				} else {
					throw new InvalidRequestException(
						Msg.code(2944) + "Don't know how to handle file: " + toStringValue(theFilename));
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
		name = JpaConstants.OPERATION_UPLOAD_TERMINOLOGY_START_JOB,
		manualResponse = true,
		idempotent = false)
	public void uploadTerminologyStartJob(
		@OperationParam(name = PARAM_JOB_INSTANCE_ID, min = 1, typeName = "code")
		IPrimitiveType<String> theJobInstanceId,
		ServletRequestDetails theRequestDetails)
		throws IOException {

		ServletRequestUtil.validatePreferAsyncHeader(
			theRequestDetails, JpaConstants.OPERATION_UPLOAD_TERMINOLOGY_START_JOB);

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
			theRequestDetails, pollUrl, JpaConstants.OPERATION_UPLOAD_TERMINOLOGY_START_JOB, null);
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
				theRequestDetails,
				jobInstance,
				JpaConstants.OPERATION_UPLOAD_TERMINOLOGY_START_JOB,
				completedDetailsProvider);

		} else {
			throw new InvalidRequestException(Msg.code(2948) + "Can't use this operation to poll status of this job");
		}
	}

	/**
	 * <code>
	 * $upload-external-codesystem
	 * </code>
	 */
	@Operation(
		typeName = "CodeSystem",
		name = JpaConstants.OPERATION_UPLOAD_EXTERNAL_CODE_SYSTEM,
		idempotent = false,
		returnParameters = {
			@OperationParam(name = RESP_PARAM_SUCCESS, typeName = "boolean", min = 1),
			@OperationParam(name = RESP_PARAM_CONCEPT_COUNT, typeName = "integer", min = 1),
			@OperationParam(name = RESP_PARAM_TARGET, typeName = "Reference", min = 1)
		})
	public IBaseParameters uploadSnapshot(
		HttpServletRequest theServletRequest,
		@OperationParam(name = PARAM_SYSTEM, min = 1, typeName = "uri") IPrimitiveType<String> theCodeSystemUrl,
		@OperationParam(name = PARAM_FILE, min = 0, max = OperationParam.MAX_UNLIMITED, typeName = "attachment")
		List<ICompositeType> theFiles,
		RequestDetails theRequestDetails) {

		startRequest(theServletRequest);

		if (theCodeSystemUrl == null || isBlank(theCodeSystemUrl.getValueAsString())) {
			throw new InvalidRequestException(Msg.code(1137) + "Missing mandatory parameter: " + PARAM_SYSTEM);
		}

		if (theFiles == null || theFiles.isEmpty()) {
			throw new InvalidRequestException(
				Msg.code(1138) + "No '" + PARAM_FILE + "' parameter, or package had no data");
		}
		for (ICompositeType next : theFiles) {
			ValidateUtil.isTrueOrThrowInvalidRequest(
				getContext().getElementDefinition(next.getClass()).getName().equals("Attachment"),
				"Package must be of type Attachment");
		}

		try {
			List<ITermLoaderSvc.FileDescriptor> localFiles = convertAttachmentsToFileDescriptors(theFiles);

			String codeSystemUrl = theCodeSystemUrl.getValue();
			codeSystemUrl = trim(codeSystemUrl);

			UploadStatistics stats =
				switch (codeSystemUrl) {
					case ITermLoaderSvc.ICD10_URI -> myTerminologyLoaderSvc.loadIcd10(
						localFiles, theRequestDetails);
					case ITermLoaderSvc.ICD10CM_URI -> myTerminologyLoaderSvc.loadIcd10cm(
						localFiles, theRequestDetails);
					case ITermLoaderSvc.IMGTHLA_URI -> myTerminologyLoaderSvc.loadImgthla(
						localFiles, theRequestDetails);
					case ITermLoaderSvc.SCT_URI -> myTerminologyLoaderSvc.loadSnomedCt(
						localFiles, theRequestDetails);
					default -> myTerminologyLoaderSvc.loadCustom(codeSystemUrl, localFiles, theRequestDetails);
				};

			IBaseParameters retVal = ParametersUtil.newInstance(getContext());
			ParametersUtil.addParameterToParametersBoolean(getContext(), retVal, RESP_PARAM_SUCCESS, true);
			ParametersUtil.addParameterToParametersInteger(
				getContext(),
				retVal,
				RESP_PARAM_CONCEPT_COUNT,
				stats.getAddedConceptCount() + stats.getUpdatedConceptCount());
			ParametersUtil.addParameterToParametersReference(
				getContext(), retVal, RESP_PARAM_TARGET, stats.getTarget().getValue());

			return retVal;
		} finally {
			endRequest(theServletRequest);
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

	private static void validateJobIsInBuildingStatus(JobInstance jobInstance) {
		if (jobInstance.getStatus() != StatusEnum.BUILDING) {
			throw new InvalidRequestException(
				Msg.code(2949) + "Job is not in BUILDING status: " + jobInstance.getStatus());
		}
	}

	private record JobType(Pattern distributionFilenamePattern, String thePropertyFileName, String jobDefinitionId, Supplier<BaseTerminologyImportParameters> paramsFactory,
	                       String terminologyName, String distributionFileName, String propertyFileName) {
	}

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
