package ca.uhn.fhir.jpa.provider;

/*
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.term.IHapiTerminologyLoaderSvc;
import ca.uhn.fhir.jpa.term.IHapiTerminologyLoaderSvc.UploadStatistics;
import ca.uhn.fhir.jpa.term.IHapiTerminologySvc;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.AttachmentUtil;
import ca.uhn.fhir.util.ParametersUtil;
import ca.uhn.fhir.util.ValidateUtil;
import org.hl7.fhir.convertors.VersionConvertor_30_40;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.ICompositeType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.apache.commons.lang3.StringUtils.*;

public class TerminologyUploaderProvider extends BaseJpaProvider {

	public static final String CONCEPT_COUNT = "conceptCount";
	public static final String TARGET = "target";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TerminologyUploaderProvider.class);
	public static final String PARENT_CODE = "parentCode";
	public static final String VALUE = "value";

	@Autowired
	private FhirContext myCtx;
	@Autowired
	private IHapiTerminologyLoaderSvc myTerminologyLoaderSvc;
	@Autowired
	private IHapiTerminologySvc myTerminologySvc;

	/**
	 * Constructor
	 */
	public TerminologyUploaderProvider() {
		this(null, null, null);
	}

	/**
	 * Constructor
	 */
	public TerminologyUploaderProvider(FhirContext theContext, IHapiTerminologyLoaderSvc theTerminologyLoaderSvc, IHapiTerminologySvc theTerminologySvc) {
		myCtx = theContext;
		myTerminologyLoaderSvc = theTerminologyLoaderSvc;
		myTerminologySvc = theTerminologySvc;
	}


	/**
	 * <code>
	 * $apply-codesystem-delta-add
	 * </code>
	 */
	@Operation(typeName="CodeSystem", name = JpaConstants.OPERATION_APPLY_CODESYSTEM_DELTA_ADD, idempotent = false, returnParameters = {
	})
	public IBaseParameters applyCodeSystemDeltaAdd(
		HttpServletRequest theServletRequest,
		@OperationParam(name = PARENT_CODE, min = 0, max = 1) IPrimitiveType<String> theParentCode,
		@OperationParam(name = VALUE, min = 1, max = 1) IBaseResource theValue,
		RequestDetails theRequestDetails
	) {

		startRequest(theServletRequest);
		try {

			CodeSystem value;
			if (theValue instanceof CodeSystem) {
				value = (CodeSystem) theValue;
			} else if (theValue instanceof org.hl7.fhir.dstu3.model.CodeSystem) {
				value = VersionConvertor_30_40.convertCodeSystem((org.hl7.fhir.dstu3.model.CodeSystem) theValue);
			} else {
				throw new InvalidRequestException("Value must be present and be a CodeSystem");
			}

			String system = value.getUrl();
			String parentCode = theParentCode != null ? theParentCode.getValue() : null;

			AtomicInteger counter = myTerminologySvc.applyDeltaCodesystemsAdd(system, parentCode, value);

			IBaseParameters retVal = ParametersUtil.newInstance(myCtx);
			ParametersUtil.addParameterToParametersBoolean(myCtx, retVal, "success", true);
			ParametersUtil.addParameterToParametersInteger(myCtx, retVal, "addedConcepts", counter.get());
			return retVal;

		} finally {
			endRequest(theServletRequest);
		}

	}


	/**
	 * <code>
	 * $apply-codesystem-delta-remove
	 * </code>
	 */
	@Operation(typeName="CodeSystem", name = JpaConstants.OPERATION_APPLY_CODESYSTEM_DELTA_REMOVE, idempotent = false, returnParameters = {
	})
	public IBaseParameters applyCodeSystemDeltaRemove(
		HttpServletRequest theServletRequest,
		@OperationParam(name = VALUE, min = 1, max = 1) IBaseResource theValue,
		RequestDetails theRequestDetails
	) {

		startRequest(theServletRequest);
		try {

			CodeSystem value;
			if (theValue instanceof CodeSystem) {
				value = (CodeSystem) theValue;
			} else if (theValue instanceof org.hl7.fhir.dstu3.model.CodeSystem) {
				value = VersionConvertor_30_40.convertCodeSystem((org.hl7.fhir.dstu3.model.CodeSystem) theValue);
			} else {
				throw new InvalidRequestException("Value must be present and be a CodeSystem");
			}

			String system = value.getUrl();

			AtomicInteger counter = myTerminologySvc.applyDeltaCodesystemsRemove(system, value);

			IBaseParameters retVal = ParametersUtil.newInstance(myCtx);
			ParametersUtil.addParameterToParametersBoolean(myCtx, retVal, "success", true);
			ParametersUtil.addParameterToParametersInteger(myCtx, retVal, "removedConcepts", counter.get());
			return retVal;

		} finally {
			endRequest(theServletRequest);
		}

	}


	/**
	 * <code>
	 * $upload-external-codesystem
	 * </code>
	 */
	@Operation(typeName="CodeSystem", name = JpaConstants.OPERATION_UPLOAD_EXTERNAL_CODE_SYSTEM, idempotent = false, returnParameters = {
//		@OperationParam(name = "conceptCount", type = IntegerType.class, min = 1)
	})
	public IBaseParameters uploadExternalCodeSystem(
		HttpServletRequest theServletRequest,
		@OperationParam(name = "url", min = 1, typeName = "uri") IPrimitiveType<String> theCodeSystemUrl,
		@OperationParam(name = "contentMode", min = 0, typeName = "code") IPrimitiveType<String> theContentMode,
		@OperationParam(name = "localfile", min = 1, max = OperationParam.MAX_UNLIMITED, typeName = "string") List<IPrimitiveType<String>> theLocalFile,
		@OperationParam(name = "package", min = 0, max = OperationParam.MAX_UNLIMITED, typeName = "attachment") List<ICompositeType> thePackage,
		RequestDetails theRequestDetails
	) {

		startRequest(theServletRequest);

		if (theLocalFile == null || theLocalFile.size() == 0) {
			if (thePackage == null || thePackage.size() == 0) {
				throw new InvalidRequestException("No 'localfile' or 'package' parameter, or package had no data");
			}
			for (ICompositeType next : thePackage) {
				ValidateUtil.isTrueOrThrowInvalidRequest(myCtx.getElementDefinition(next.getClass()).getName().equals("Attachment"), "Package must be of type Attachment");
			}
		}

		try {
			List<IHapiTerminologyLoaderSvc.FileDescriptor> localFiles = new ArrayList<>();
			if (theLocalFile != null && theLocalFile.size() > 0) {
				for (IPrimitiveType<String> nextLocalFile : theLocalFile) {
					if (isNotBlank(nextLocalFile.getValue())) {
						ourLog.info("Reading in local file: {}", nextLocalFile.getValue());
						File nextFile = new File(nextLocalFile.getValue());
						if (!nextFile.exists() || !nextFile.isFile()) {
							throw new InvalidRequestException("Unknown file: " + nextFile.getName());
						}
						localFiles.add(new IHapiTerminologyLoaderSvc.FileDescriptor() {
							@Override
							public String getFilename() {
								return nextFile.getAbsolutePath();
							}

							@Override
							public InputStream getInputStream() {
								try {
									return new FileInputStream(nextFile);
								} catch (FileNotFoundException theE) {
									throw new InternalErrorException(theE);
								}
							}
						});
					}
				}
			}

			if (thePackage != null) {
				for (ICompositeType nextPackage : thePackage) {
					final String url = AttachmentUtil.getOrCreateUrl(myCtx, nextPackage).getValueAsString();

					if (isBlank(url)) {
						throw new UnprocessableEntityException("Package is missing mandatory url element");
					}

					localFiles.add(new IHapiTerminologyLoaderSvc.FileDescriptor() {
						@Override
						public String getFilename() {
							return url;
						}

						@Override
						public InputStream getInputStream() {
							byte[] data = AttachmentUtil.getOrCreateData(myCtx, nextPackage).getValue();
							return new ByteArrayInputStream(data);
						}
					});
				}
			}

			String codeSystemUrl = theCodeSystemUrl != null ? theCodeSystemUrl.getValue() : null;
			codeSystemUrl = defaultString(codeSystemUrl);

			String contentMode = theContentMode != null ? theContentMode.getValue() : null;
			UploadStatistics stats;
			if ("custom".equals(contentMode)) {
				stats = myTerminologyLoaderSvc.loadCustom(codeSystemUrl, localFiles, theRequestDetails);
			} else {
				switch (codeSystemUrl) {
					case IHapiTerminologyLoaderSvc.SCT_URI:
						stats = myTerminologyLoaderSvc.loadSnomedCt(localFiles, theRequestDetails);
						break;
					case IHapiTerminologyLoaderSvc.LOINC_URI:
						stats = myTerminologyLoaderSvc.loadLoinc(localFiles, theRequestDetails);
						break;
					case IHapiTerminologyLoaderSvc.IMGTHLA_URI:
						stats = myTerminologyLoaderSvc.loadImgthla(localFiles, theRequestDetails);
						break;
					default:
						throw new InvalidRequestException("Unknown URL: " + codeSystemUrl);
				}
			}

			IBaseParameters retVal = ParametersUtil.newInstance(myCtx);
			ParametersUtil.addParameterToParametersBoolean(myCtx, retVal, "success", true);
			ParametersUtil.addParameterToParametersInteger(myCtx, retVal, CONCEPT_COUNT, stats.getConceptCount());
			ParametersUtil.addParameterToParametersReference(myCtx, retVal, TARGET, stats.getTarget().getValue());

			return retVal;
		} finally {
			endRequest(theServletRequest);
		}
	}


}
