package ca.uhn.fhir.jpa.provider;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.term.TermLoaderSvcImpl;
import ca.uhn.fhir.jpa.term.UploadStatistics;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.term.custom.ConceptHandler;
import ca.uhn.fhir.jpa.term.custom.HierarchyHandler;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.AttachmentUtil;
import ca.uhn.fhir.util.ParametersUtil;
import ca.uhn.fhir.util.ValidateUtil;
import com.google.common.base.Charsets;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.ICompositeType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;
import static org.hl7.fhir.convertors.conv30_40.CodeSystem30_40.convertCodeSystem;

public class TerminologyUploaderProvider extends BaseJpaProvider {

	public static final String PARAM_FILE = "file";
	public static final String PARAM_CODESYSTEM = "codeSystem";
	public static final String PARAM_SYSTEM = "system";
	private static final String RESP_PARAM_CONCEPT_COUNT = "conceptCount";
	private static final String RESP_PARAM_TARGET = "target";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TerminologyUploaderProvider.class);
	private static final String RESP_PARAM_SUCCESS = "success";

	@Autowired
	private ITermLoaderSvc myTerminologyLoaderSvc;

	/**
	 * Constructor
	 */
	public TerminologyUploaderProvider() {
		this(null, null);
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
	@Operation(typeName = "CodeSystem", name = JpaConstants.OPERATION_UPLOAD_EXTERNAL_CODE_SYSTEM, idempotent = false, returnParameters = {
//		@OperationParam(name = "conceptCount", type = IntegerType.class, min = 1)
	})
	public IBaseParameters uploadSnapshot(
		HttpServletRequest theServletRequest,
		@OperationParam(name = PARAM_SYSTEM, min = 1, typeName = "uri") IPrimitiveType<String> theCodeSystemUrl,
		@OperationParam(name = PARAM_FILE, min = 0, max = OperationParam.MAX_UNLIMITED, typeName = "attachment") List<ICompositeType> theFiles,
		RequestDetails theRequestDetails
	) {

		startRequest(theServletRequest);

		if (theCodeSystemUrl == null || isBlank(theCodeSystemUrl.getValueAsString())) {
			throw new InvalidRequestException("Missing mandatory parameter: " + PARAM_SYSTEM);
		}

		if (theFiles == null || theFiles.size() == 0) {
			throw new InvalidRequestException("No '" + PARAM_FILE + "' parameter, or package had no data");
		}
		for (ICompositeType next : theFiles) {
			ValidateUtil.isTrueOrThrowInvalidRequest(getContext().getElementDefinition(next.getClass()).getName().equals("Attachment"), "Package must be of type Attachment");
		}

		try {
			List<ITermLoaderSvc.FileDescriptor> localFiles = convertAttachmentsToFileDescriptors(theFiles);

			String codeSystemUrl = theCodeSystemUrl.getValue();
			codeSystemUrl = trim(codeSystemUrl);

			UploadStatistics stats;
			switch (codeSystemUrl) {
				case ITermLoaderSvc.SCT_URI:
					stats = myTerminologyLoaderSvc.loadSnomedCt(localFiles, theRequestDetails);
					break;
				case ITermLoaderSvc.LOINC_URI:
					stats = myTerminologyLoaderSvc.loadLoinc(localFiles, theRequestDetails);
					break;
				case ITermLoaderSvc.IMGTHLA_URI:
					stats = myTerminologyLoaderSvc.loadImgthla(localFiles, theRequestDetails);
					break;
				default:
					stats = myTerminologyLoaderSvc.loadCustom(codeSystemUrl, localFiles, theRequestDetails);
					break;
			}

			IBaseParameters retVal = ParametersUtil.newInstance(getContext());
			ParametersUtil.addParameterToParametersBoolean(getContext(), retVal, RESP_PARAM_SUCCESS, true);
			ParametersUtil.addParameterToParametersInteger(getContext(), retVal, RESP_PARAM_CONCEPT_COUNT, stats.getUpdatedConceptCount());
			ParametersUtil.addParameterToParametersReference(getContext(), retVal, RESP_PARAM_TARGET, stats.getTarget().getValue());

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
	@Operation(typeName = "CodeSystem", name = JpaConstants.OPERATION_APPLY_CODESYSTEM_DELTA_ADD, idempotent = false, returnParameters = {
	})
	public IBaseParameters uploadDeltaAdd(
		HttpServletRequest theServletRequest,
		@OperationParam(name = PARAM_SYSTEM, min = 1, max = 1, typeName = "uri") IPrimitiveType<String> theSystem,
		@OperationParam(name = PARAM_FILE, min = 0, max = OperationParam.MAX_UNLIMITED, typeName = "attachment") List<ICompositeType> theFiles,
		@OperationParam(name = PARAM_CODESYSTEM, min = 0, max = OperationParam.MAX_UNLIMITED, typeName = "CodeSystem") List<IBaseResource> theCodeSystems,
		RequestDetails theRequestDetails
	) {

		startRequest(theServletRequest);
		try {
			validateHaveSystem(theSystem);
			validateHaveFiles(theFiles, theCodeSystems);

			List<ITermLoaderSvc.FileDescriptor> files = convertAttachmentsToFileDescriptors(theFiles);
			convertCodeSystemsToFileDescriptors(files, theCodeSystems);
			UploadStatistics outcome = myTerminologyLoaderSvc.loadDeltaAdd(theSystem.getValue(), files, theRequestDetails);
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
	@Operation(typeName = "CodeSystem", name = JpaConstants.OPERATION_APPLY_CODESYSTEM_DELTA_REMOVE, idempotent = false, returnParameters = {
	})
	public IBaseParameters uploadDeltaRemove(
		HttpServletRequest theServletRequest,
		@OperationParam(name = PARAM_SYSTEM, min = 1, max = 1, typeName = "uri") IPrimitiveType<String> theSystem,
		@OperationParam(name = PARAM_FILE, min = 0, max = OperationParam.MAX_UNLIMITED, typeName = "attachment") List<ICompositeType> theFiles,
		@OperationParam(name = PARAM_CODESYSTEM, min = 0, max = OperationParam.MAX_UNLIMITED, typeName = "CodeSystem") List<IBaseResource> theCodeSystems,
		RequestDetails theRequestDetails
	) {

		startRequest(theServletRequest);
		try {
			validateHaveSystem(theSystem);
			validateHaveFiles(theFiles, theCodeSystems);

			List<ITermLoaderSvc.FileDescriptor> files = convertAttachmentsToFileDescriptors(theFiles);
			convertCodeSystemsToFileDescriptors(files, theCodeSystems);
			UploadStatistics outcome = myTerminologyLoaderSvc.loadDeltaRemove(theSystem.getValue(), files, theRequestDetails);
			return toDeltaResponse(outcome);
		} finally {
			endRequest(theServletRequest);
		}

	}

	private void convertCodeSystemsToFileDescriptors(List<ITermLoaderSvc.FileDescriptor> theFiles, List<IBaseResource> theCodeSystems) {
		Map<String, String> codes = new LinkedHashMap<>();
		Multimap<String, String> codeToParentCodes = ArrayListMultimap.create();

		if (theCodeSystems != null) {
			for (IBaseResource nextCodeSystemUncast : theCodeSystems) {
				CodeSystem nextCodeSystem = canonicalizeCodeSystem(nextCodeSystemUncast);
				convertCodeSystemCodesToCsv(nextCodeSystem.getConcept(), codes, null, codeToParentCodes);
			}
		}

		// Create concept file
		if (codes.size() > 0) {
			StringBuilder b = new StringBuilder();
			b.append(ConceptHandler.CODE);
			b.append(",");
			b.append(ConceptHandler.DISPLAY);
			b.append("\n");
			for (Map.Entry<String, String> nextEntry : codes.entrySet()) {
				b.append(csvEscape(nextEntry.getKey()));
				b.append(",");
				b.append(csvEscape(nextEntry.getValue()));
				b.append("\n");
			}
			byte[] bytes = b.toString().getBytes(Charsets.UTF_8);
			String fileName = TermLoaderSvcImpl.CUSTOM_CONCEPTS_FILE;
			ITermLoaderSvc.ByteArrayFileDescriptor fileDescriptor = new ITermLoaderSvc.ByteArrayFileDescriptor(fileName, bytes);
			theFiles.add(fileDescriptor);
		}

		// Create hierarchy file
		if (codeToParentCodes.size() > 0) {
			StringBuilder b = new StringBuilder();
			b.append(HierarchyHandler.CHILD);
			b.append(",");
			b.append(HierarchyHandler.PARENT);
			b.append("\n");
			for (Map.Entry<String, String> nextEntry : codeToParentCodes.entries()) {
				b.append(csvEscape(nextEntry.getKey()));
				b.append(",");
				b.append(csvEscape(nextEntry.getValue()));
				b.append("\n");
			}
			byte[] bytes = b.toString().getBytes(Charsets.UTF_8);
			String fileName = TermLoaderSvcImpl.CUSTOM_HIERARCHY_FILE;
			ITermLoaderSvc.ByteArrayFileDescriptor fileDescriptor = new ITermLoaderSvc.ByteArrayFileDescriptor(fileName, bytes);
			theFiles.add(fileDescriptor);
		}

	}

	@SuppressWarnings("EnumSwitchStatementWhichMissesCases")
	@Nonnull
	CodeSystem canonicalizeCodeSystem(@Nonnull IBaseResource theCodeSystem) {
		RuntimeResourceDefinition resourceDef = getContext().getResourceDefinition(theCodeSystem);
		ValidateUtil.isTrueOrThrowInvalidRequest(resourceDef.getName().equals("CodeSystem"), "Resource '%s' is not a CodeSystem", resourceDef.getName());

		CodeSystem nextCodeSystem;
		switch (getContext().getVersion().getVersion()) {
			case DSTU3:
				nextCodeSystem = convertCodeSystem((org.hl7.fhir.dstu3.model.CodeSystem) theCodeSystem);
				break;
			case R5:
				nextCodeSystem = org.hl7.fhir.convertors.conv40_50.CodeSystem40_50.convertCodeSystem((org.hl7.fhir.r5.model.CodeSystem) theCodeSystem);
				break;
			default:
				nextCodeSystem = (CodeSystem) theCodeSystem;
		}
		return nextCodeSystem;
	}

	private void convertCodeSystemCodesToCsv(List<CodeSystem.ConceptDefinitionComponent> theConcept, Map<String, String> theCodes, String theParentCode, Multimap<String, String> theCodeToParentCodes) {
		for (CodeSystem.ConceptDefinitionComponent nextConcept : theConcept) {
			if (isNotBlank(nextConcept.getCode())) {
				theCodes.put(nextConcept.getCode(), nextConcept.getDisplay());
				if (isNotBlank(theParentCode)) {
					theCodeToParentCodes.put(nextConcept.getCode(), theParentCode);
				}
				convertCodeSystemCodesToCsv(nextConcept.getConcept(), theCodes, nextConcept.getCode(), theCodeToParentCodes);
			}
		}
	}

	private void validateHaveSystem(IPrimitiveType<String> theSystem) {
		if (theSystem == null || isBlank(theSystem.getValueAsString())) {
			throw new InvalidRequestException("Missing mandatory parameter: " + PARAM_SYSTEM);
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
		throw new InvalidRequestException("Missing mandatory parameter: " + PARAM_FILE);
	}

	@Nonnull
	private List<ITermLoaderSvc.FileDescriptor> convertAttachmentsToFileDescriptors(@OperationParam(name = PARAM_FILE, min = 0, max = OperationParam.MAX_UNLIMITED, typeName = "attachment") List<ICompositeType> theFiles) {
		List<ITermLoaderSvc.FileDescriptor> files = new ArrayList<>();
		if (theFiles != null) {
			for (ICompositeType next : theFiles) {

				String nextUrl = AttachmentUtil.getOrCreateUrl(getContext(), next).getValue();
				ValidateUtil.isNotBlankOrThrowUnprocessableEntity(nextUrl, "Missing Attachment.url value");

				byte[] nextData;
				if (nextUrl.startsWith("localfile:")) {
					String nextLocalFile = nextUrl.substring("localfile:".length());


					if (isNotBlank(nextLocalFile)) {
						ourLog.info("Reading in local file: {}", nextLocalFile);
						File nextFile = new File(nextLocalFile);
						if (!nextFile.exists() || !nextFile.isFile()) {
							throw new InvalidRequestException("Unknown file: " + nextFile.getName());
						}
						files.add(new FileBackedFileDescriptor(nextFile));
					}

				} else {
					nextData = AttachmentUtil.getOrCreateData(getContext(), next).getValue();
					ValidateUtil.isTrueOrThrowInvalidRequest(nextData != null && nextData.length > 0, "Missing Attachment.data value");
					files.add(new ITermLoaderSvc.ByteArrayFileDescriptor(nextUrl, nextData));
				}
			}
		}
		return files;
	}

	private IBaseParameters toDeltaResponse(UploadStatistics theOutcome) {
		IBaseParameters retVal = ParametersUtil.newInstance(getContext());
		ParametersUtil.addParameterToParametersInteger(getContext(), retVal, RESP_PARAM_CONCEPT_COUNT, theOutcome.getUpdatedConceptCount());
		ParametersUtil.addParameterToParametersReference(getContext(), retVal, RESP_PARAM_TARGET, theOutcome.getTarget().getValue());
		return retVal;
	}

	private static class FileBackedFileDescriptor implements ITermLoaderSvc.FileDescriptor {
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
				throw new InternalErrorException(theE);
			}
		}
	}

	private static String csvEscape(String theValue) {
		return '"' +
			theValue
				.replace("\"", "\"\"")
				.replace("\n", "\\n")
				.replace("\r", "") +
			'"';
	}
}
