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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.term.UploadStatistics;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.term.custom.CodeSystemToCustomCsvConverter;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.AttachmentUtil;
import ca.uhn.fhir.util.ParametersUtil;
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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class TerminologyUploaderProvider extends BaseJpaProvider {

	public static final String PARAM_FILE = "file";
	public static final String PARAM_CODESYSTEM = "codeSystem";
	public static final String PARAM_SYSTEM = "system";
	private static final String RESP_PARAM_CONCEPT_COUNT = "conceptCount";
	private static final String RESP_PARAM_TARGET = "target";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TerminologyUploaderProvider.class);
	private static final String RESP_PARAM_SUCCESS = "success";

	private CodeSystemToCustomCsvConverter myCodeSystemToCustomCsvConverter;

	@Autowired
	private ITermLoaderSvc myTerminologyLoaderSvc;

	/**
	 * Constructor
	 */
	public TerminologyUploaderProvider() {
		this(null, null);
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
	 * Constructor
	 */
	public TerminologyUploaderProvider(FhirContext theContext, ITermLoaderSvc theTerminologyLoaderSvc) {
		setContext(theContext);
		myTerminologyLoaderSvc = theTerminologyLoaderSvc;
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
				//		@OperationParam(name = "conceptCount", type = IntegerType.class, min = 1)
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
						case ITermLoaderSvc.LOINC_URI -> myTerminologyLoaderSvc.loadLoinc(
								localFiles, theRequestDetails);
						case ITermLoaderSvc.SCT_URI -> myTerminologyLoaderSvc.loadSnomedCt(
								localFiles, theRequestDetails);
						default -> myTerminologyLoaderSvc.loadCustom(codeSystemUrl, localFiles, theRequestDetails);
					};

			IBaseParameters retVal = ParametersUtil.newInstance(getContext());
			ParametersUtil.addParameterToParametersBoolean(getContext(), retVal, RESP_PARAM_SUCCESS, true);
			ParametersUtil.addParameterToParametersInteger(
					getContext(), retVal, RESP_PARAM_CONCEPT_COUNT, stats.getUpdatedConceptCount());
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
				getContext(), retVal, RESP_PARAM_CONCEPT_COUNT, theOutcome.getUpdatedConceptCount());
		ParametersUtil.addParameterToParametersReference(
				getContext(), retVal, RESP_PARAM_TARGET, theOutcome.getTarget().getValue());
		return retVal;
	}

	public void setTerminologyLoaderSvc(ITermLoaderSvc theTermLoaderSvc) {
		myTerminologyLoaderSvc = theTermLoaderSvc;
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
