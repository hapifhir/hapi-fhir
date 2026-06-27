/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
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
package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.batch2.api.AttachmentContentTypeEnum;
import ca.uhn.fhir.batch2.api.AttachmentDetails;
import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.LookupCodeRequest;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyJobParameters;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyModeEnum;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyResultJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants;
import ca.uhn.fhir.jpa.batch2.jobs.term.custom.ImportCustomTerminologyJobAppCtx;
import ca.uhn.fhir.jpa.batch2.jobs.term.icd.ImportIcdJobAppCtx;
import ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincJobAppCtx;
import ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum;
import ca.uhn.fhir.jpa.batch2.jobs.term.snomedct.ImportSnomedCtJobAppCtx;
import ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand.PreExpandValueSetJobAppCtx;
import ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand.PreExpandValueSetParameters;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import static ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants.FILENAME_LOINC_DISTRIBUTION_FILE;
import static ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants.FILENAME_SNOMED_CT_DISTRIBUTION_FILE;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum.LOINC_ANSWERLIST_DUPLICATE_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum.LOINC_ANSWERLIST_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum.LOINC_ANSWERLIST_LINK_DUPLICATE_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum.LOINC_ANSWERLIST_LINK_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum.LOINC_CONSUMER_NAME_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum.LOINC_DOCUMENT_ONTOLOGY_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum.LOINC_DUPLICATE_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum.LOINC_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum.LOINC_GROUP_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum.LOINC_GROUP_TERMS_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum.LOINC_HIERARCHY_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum.LOINC_IEEE_MEDICAL_DEVICE_CODE_MAPPING_TABLE_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum.LOINC_IMAGING_DOCUMENT_CODES_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum.LOINC_PARENT_GROUP_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_LINK_FILE_PRIMARY_DEFAULT;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_LINK_FILE_SUPPLEMENTARY_DEFAULT;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_RELATED_CODE_MAPPING_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum.LOINC_RSNA_PLAYBOOK_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum.LOINC_UNIVERSAL_LAB_ORDER_VALUESET_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum.LOINC_XML_FILE;
import static ca.uhn.fhir.jpa.test.BaseJpaTest.newSrd;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class TerminologyTestHelper {

	private final IJobPersistence myJobPersistence;
	private final IJobCoordinator myJobCoordinator;
	private final Batch2JobHelper myBatch2JobHelper;
	private final MemoryCacheService myMemoryCacheService;
	private final IValidationSupport myValidationSupport;

	public TerminologyTestHelper(
			IJobPersistence theJobPersistence,
			IJobCoordinator theJobCoordinator,
			Batch2JobHelper theBatch2JobHelper,
			MemoryCacheService theMemoryCacheService,
			IValidationSupport theValidationSupport) {
		myJobPersistence = theJobPersistence;
		myJobCoordinator = theJobCoordinator;
		myBatch2JobHelper = theBatch2JobHelper;
		myMemoryCacheService = theMemoryCacheService;
		myValidationSupport = theValidationSupport;
	}

	public String startImportCustomJobAndWaitForCompletion(
			String theUrl, String theVersion, ZipCollectionBuilder theFiles, ImportTerminologyModeEnum theMode) {
		String jobDefinitionId = ImportCustomTerminologyJobAppCtx.JOB_ID_IMPORT_CUSTOM_TERMINOLOGY;
		String distributionFilename = TerminologyConstants.FILENAME_CUSTOM_DISTRIBUTION_FILE;
		return startImportTerminologyJob(
				theUrl, theVersion, theFiles, false, null, jobDefinitionId, distributionFilename, null, true, theMode);
	}

	public String startImportCustomJobAndWaitForCompletion(
			String theUrl, String theVersion, ZipCollectionBuilder theFiles) {
		String jobDefinitionId = ImportCustomTerminologyJobAppCtx.JOB_ID_IMPORT_CUSTOM_TERMINOLOGY;
		String distributionFilename = TerminologyConstants.FILENAME_CUSTOM_DISTRIBUTION_FILE;
		return startImportTerminologyJobAndWaitForCompletion(
				theUrl, theVersion, theFiles, false, null, jobDefinitionId, distributionFilename, null);
	}

	public String startImportCustomJobAndWaitForFailure(
			String theUrl, String theVersion, ZipCollectionBuilder theFiles) {
		String jobDefinitionId = ImportCustomTerminologyJobAppCtx.JOB_ID_IMPORT_CUSTOM_TERMINOLOGY;
		String distributionFilename = TerminologyConstants.FILENAME_CUSTOM_DISTRIBUTION_FILE;
		return startImportTerminologyJobAndWaitForFailure(
				theUrl, theVersion, theFiles, false, null, jobDefinitionId, distributionFilename, null);
	}

	public String startImportIcdJobAndWaitForCompletion(String theVersion, ZipCollectionBuilder theFiles) {
		String jobDefinitionId = ImportIcdJobAppCtx.JOB_ID_IMPORT_ICD_10;
		String distributionFilename = TerminologyConstants.FILENAME_ICD10_DISTRIBUTION_FILE;
		String propertiesFilename = null;
		return startImportTerminologyJobAndWaitForCompletion(
				TerminologyConstants.ICD10_URI,
				theVersion,
				theFiles,
				false,
				null,
				jobDefinitionId,
				distributionFilename,
				propertiesFilename);
	}

	public String startImportIcdJobAndWaitForFailure(String theVersion, ZipCollectionBuilder theFiles) {
		String jobDefinitionId = ImportIcdJobAppCtx.JOB_ID_IMPORT_ICD_10;
		String distributionFilename = TerminologyConstants.FILENAME_ICD10_DISTRIBUTION_FILE;
		String propertiesFilename = null;
		return startImportTerminologyJobAndWaitForFailure(
				TerminologyConstants.ICD10_URI,
				theVersion,
				theFiles,
				false,
				null,
				jobDefinitionId,
				distributionFilename,
				propertiesFilename);
	}

	public String startImportIcdCmJobAndWaitForCompletion(String theVersion, ZipCollectionBuilder theFiles) {
		String jobDefinitionId = ImportIcdJobAppCtx.JOB_ID_IMPORT_ICD_10_CM;
		String distributionFilename = TerminologyConstants.FILENAME_ICD10CM_DISTRIBUTION_FILE;
		String propertiesFilename = null;
		return startImportTerminologyJobAndWaitForCompletion(
				TerminologyConstants.ICD10CM_URI,
				theVersion,
				theFiles,
				false,
				null,
				jobDefinitionId,
				distributionFilename,
				propertiesFilename);
	}

	public String startImportLoincJobAndWaitForCompletion(String versionId, ZipCollectionBuilder theFiles) {
		return startImportLoincJobAndWaitForCompletion(versionId, theFiles, false);
	}

	public String startImportLoincJobAndWaitForCompletion(
			String versionId, ZipCollectionBuilder theFiles, boolean theDontMakeCurrent) {
		return startImportLoincJobAndWaitForCompletion(versionId, theFiles, theDontMakeCurrent, null);
	}

	public String startImportLoincJobAndWaitForCompletion(
			String versionId, ZipCollectionBuilder theFiles, boolean theDontMakeCurrent, Properties theJobProperties) {
		String jobDefinitionId = ImportLoincJobAppCtx.JOB_ID_IMPORT_TERM_LOINC;
		String distributionFilename = FILENAME_LOINC_DISTRIBUTION_FILE;
		String propertiesFilename = LoincUploadPropertiesEnum.LOINC_UPLOAD_PROPERTIES_FILE.getCode();
		return startImportTerminologyJobAndWaitForCompletion(
				TerminologyConstants.LOINC_URI,
				versionId,
				theFiles,
				theDontMakeCurrent,
				theJobProperties,
				jobDefinitionId,
				distributionFilename,
				propertiesFilename);
	}

	private String startImportTerminologyJobAndWaitForCompletion(
			String theUrl,
			String versionId,
			ZipCollectionBuilder theFiles,
			boolean theDontMakeCurrent,
			Properties theJobProperties,
			String jobDefinitionId,
			String distributionFilename,
			String propertiesFilename) {
		return startImportTerminologyJob(
				theUrl,
				versionId,
				theFiles,
				theDontMakeCurrent,
				theJobProperties,
				jobDefinitionId,
				distributionFilename,
				propertiesFilename,
				true,
				ImportTerminologyModeEnum.SNAPSHOT);
	}

	private String startImportTerminologyJobAndWaitForFailure(
			String theUrl,
			String versionId,
			ZipCollectionBuilder theFiles,
			boolean theDontMakeCurrent,
			Properties theJobProperties,
			String jobDefinitionId,
			String distributionFilename,
			String propertiesFilename) {
		return startImportTerminologyJob(
				theUrl,
				versionId,
				theFiles,
				theDontMakeCurrent,
				theJobProperties,
				jobDefinitionId,
				distributionFilename,
				propertiesFilename,
				false,
				ImportTerminologyModeEnum.SNAPSHOT);
	}

	private String startImportTerminologyJob(
			String theUrl,
			String versionId,
			ZipCollectionBuilder theFiles,
			boolean theDontMakeCurrent,
			Properties theJobProperties,
			String jobDefinitionId,
			String distributionFilename,
			String propertiesFilename,
			boolean expectSuccess,
			ImportTerminologyModeEnum theMode) {
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(jobDefinitionId);

		ImportTerminologyJobParameters parameters = new ImportTerminologyJobParameters();
		parameters.setUrl(theUrl);
		parameters.setVersionId(versionId);
		if (theDontMakeCurrent) {
			parameters.setDontMakeCurrent(true);
		}
		parameters.setMode(theMode);
		startRequest.setParameters(parameters);

		Batch2JobStartResponse instanceId = myJobCoordinator.startInstance(new SystemRequestDetails(), startRequest);

		if (theFiles.isSingleZip()) {
			AttachmentDetails attachmentDetails = AttachmentDetails.build()
					.withInputStream(new ByteArrayInputStream(theFiles.getZipBytes()))
					.withContentType(AttachmentContentTypeEnum.ZIP)
					.withFilename(distributionFilename)
					.build();
			myJobPersistence.storeNewAttachment(instanceId.getInstanceId(), attachmentDetails);
		} else {
			for (ZipCollectionBuilder.FileDescriptor descriptor : theFiles.getFiles()) {
				AttachmentDetails attachmentDetails = AttachmentDetails.build()
						.withInputStream(descriptor.inputStream())
						.withContentType(AttachmentContentTypeEnum.PLAIN_TEXT)
						.withFilename(descriptor.filename())
						.build();
				myJobPersistence.storeNewAttachment(instanceId.getInstanceId(), attachmentDetails);
			}
		}

		if (theJobProperties != null) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			try {
				theJobProperties.store(out, null);
			} catch (IOException e) {
				fail("Failed to store properties", e);
			}
			AttachmentDetails attachmentDetails =
					new AttachmentDetails(out.toByteArray(), AttachmentContentTypeEnum.PROPERTIES, propertiesFilename);
			myJobPersistence.storeNewAttachment(instanceId.getInstanceId(), attachmentDetails);
		}

		myJobCoordinator.enqueueBuildingJobForExecution(instanceId.getInstanceId());

		if (expectSuccess) {
			myBatch2JobHelper.awaitJobCompletion(instanceId.getInstanceId(), 99999999); // FIXME: restore
		} else {
			myBatch2JobHelper.awaitJobFailure(instanceId);
		}

		return instanceId.getInstanceId();
	}

	public String startImportSnomedCtJobAndWaitForCompletion(
			String versionId, ZipCollectionBuilder theFiles, boolean theDontMakeCurrent) {
		return startImportSnomedCtJobAndWaitForCompletion(versionId, theFiles, theDontMakeCurrent, false);
	}

	public String startImportSnomedCtJobAndWaitForFailure(
			String versionId, ZipCollectionBuilder theFiles, boolean theDontMakeCurrent) {
		return startImportSnomedCtJobAndWaitForCompletion(versionId, theFiles, theDontMakeCurrent, true);
	}

	public String startImportSnomedCtJobAndWaitForCompletion(
			String versionId, ZipCollectionBuilder theFiles, boolean theDontMakeCurrent, boolean theExpectFailure) {

		String distributionFilename = FILENAME_SNOMED_CT_DISTRIBUTION_FILE;
		if (theExpectFailure) {
			return startImportTerminologyJobAndWaitForFailure(
					TerminologyConstants.SCT_URI,
					versionId,
					theFiles,
					theDontMakeCurrent,
					null,
					ImportSnomedCtJobAppCtx.JOB_ID_IMPORT_TERM_SNOMED_CT,
					distributionFilename,
					null);
		} else {
			return startImportTerminologyJobAndWaitForCompletion(
					TerminologyConstants.SCT_URI,
					versionId,
					theFiles,
					theDontMakeCurrent,
					null,
					ImportSnomedCtJobAppCtx.JOB_ID_IMPORT_TERM_SNOMED_CT,
					distributionFilename,
					null);
		}
	}

	public void startImportLoincJobAndWaitForCompletion(String theVersion, boolean theMakeItCurrent) throws Exception {
		ZipCollectionBuilder files = new ZipCollectionBuilder(true);

		assertThat(theVersion == null
						|| theVersion.equals("2.67")
						|| theVersion.equals("2.68")
						|| theVersion.equals("2.69"))
				.as("Version supported are: 2.67, 2.68, 2.69 and null")
				.isTrue();

		addLoincMandatoryFilesToZip(files, theVersion);

		startImportLoincJobAndWaitForCompletion(theVersion, files, !theMakeItCurrent);
	}

	/**
	 * Uses the {@link IValidationSupport#lookupCode(ValidationSupportContext, LookupCodeRequest)} method to look up the code
	 * and fails if the display name doesn't match the expected value.
	 */
	public void assertConceptDisplay(String theSystem, String theCode, String theExpectedDisplay) {
		myMemoryCacheService.invalidateAllCaches();
		LookupCodeRequest request = new LookupCodeRequest(theSystem, theCode);
		IValidationSupport.LookupCodeResult result =
				myValidationSupport.lookupCode(new ValidationSupportContext(myValidationSupport), request);
		assertNotNull(result);
		assertTrue(result.isFound());
		assertEquals(theExpectedDisplay, result.getCodeDisplay());
	}

	/**
	 * Uses the {@link IValidationSupport#lookupCode(ValidationSupportContext, LookupCodeRequest)} method to look up the code
	 * and fails if the code is found.
	 */
	public void assertConceptNotFound(String theSystem, String theCode) {
		myMemoryCacheService.invalidateAllCaches();
		LookupCodeRequest lookupCodeRequest = new LookupCodeRequest(theSystem, theCode);
		IValidationSupport.LookupCodeResult result =
				myValidationSupport.lookupCode(new ValidationSupportContext(myValidationSupport), lookupCodeRequest);
		assertTrue(result == null || !result.isFound());
	}

	public void addLoincMandatoryFilesToZip(ZipCollectionBuilder theFiles, String theVersion) throws IOException {
		String theClassPathPrefix = getClassPathPrefix(theVersion);
		addBaseLoincMandatoryFilesToZip(theFiles, theClassPathPrefix);
		theFiles.addFileZip(theClassPathPrefix, LOINC_PART_LINK_FILE_PRIMARY_DEFAULT.getCode());
		theFiles.addFileZip(theClassPathPrefix, LOINC_PART_LINK_FILE_SUPPLEMENTARY_DEFAULT.getCode());
	}

	public void startImportLoincJobAndWaitForCompletion() throws Exception {
		startImportLoincJobAndWaitForCompletion("2.67", true);
	}

	private String getClassPathPrefix(String theVersion) {
		String theClassPathPrefix = "/loinc-ver/v-no-version/";

		if (StringUtils.isBlank(theVersion)) return theClassPathPrefix;

		switch (theVersion) {
			case "2.67":
				return "/loinc-ver/v267/";
			case "2.68":
				return "/loinc-ver/v268/";
			case "2.69":
				return "/loinc-ver/v269/";
		}

		fail("Setup failed. Unexpected version: " + theVersion);
		return null;
	}

	public String startImportLoincJobAndWaitForFailure(String theVersion, ZipCollectionBuilder theFiles) {
		String jobDefinitionId = ImportLoincJobAppCtx.JOB_ID_IMPORT_TERM_LOINC;
		String distributionFilename = FILENAME_LOINC_DISTRIBUTION_FILE;
		String propertiesFilename = LoincUploadPropertiesEnum.LOINC_UPLOAD_PROPERTIES_FILE.getCode();
		return startImportTerminologyJobAndWaitForFailure(
				TerminologyConstants.LOINC_URI,
				theVersion,
				theFiles,
				false,
				null,
				jobDefinitionId,
				distributionFilename,
				propertiesFilename);
	}

	public String getReport(String theJobInstanceId) {
		JobInstance instance = myJobCoordinator.getInstance(theJobInstanceId);
		return JsonUtil.deserialize(instance.getReport(), ImportTerminologyResultJson.class)
				.getReport();
	}

	public String startValueSetExpansionJobAndWaitForCompletion(String theUrl, String theVersion) {
		return startValueSetExpansionJob(theUrl, theVersion, true);
	}

	public String startValueSetExpansionJobAndWaitForFailure(String theUrl, String theVersion) {
		return startValueSetExpansionJob(theUrl, theVersion, false);
	}

	private String startValueSetExpansionJob(String theUrl, String theVersion, boolean expectSuccess) {
		PreExpandValueSetParameters parameters = new PreExpandValueSetParameters();
		parameters.setUrl(theUrl);
		parameters.setVersion(theVersion);

		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(PreExpandValueSetJobAppCtx.JOB_ID_PRE_EXPAND_VALUESET);
		startRequest.setParameters(parameters);

		String instanceId =
				myJobCoordinator.startInstance(newSrd(), startRequest).getInstanceId();
		if (expectSuccess) {
			myBatch2JobHelper.awaitJobCompletion(instanceId);
		} else {
			myBatch2JobHelper.awaitJobFailure(instanceId);
		}

		return instanceId;
	}

	private static void addBaseLoincMandatoryFilesToZip(ZipCollectionBuilder theFiles, String theClassPathPrefix)
			throws IOException {
		theFiles.addFileZip(theClassPathPrefix, LOINC_XML_FILE.getCode());
		theFiles.addFileZip(theClassPathPrefix, LOINC_GROUP_FILE_DEFAULT.getCode());
		theFiles.addFileZip(theClassPathPrefix, LOINC_GROUP_TERMS_FILE_DEFAULT.getCode());
		theFiles.addFileZip(theClassPathPrefix, LOINC_PARENT_GROUP_FILE_DEFAULT.getCode());
		theFiles.addFileZip(theClassPathPrefix, LOINC_FILE_DEFAULT.getCode());
		theFiles.addFileZip(theClassPathPrefix, LOINC_DUPLICATE_FILE_DEFAULT.getCode());
		theFiles.addFileZip(theClassPathPrefix, LOINC_HIERARCHY_FILE_DEFAULT.getCode());
		theFiles.addFileZip(theClassPathPrefix, LOINC_ANSWERLIST_FILE_DEFAULT.getCode());
		theFiles.addFileZip(theClassPathPrefix, LOINC_ANSWERLIST_DUPLICATE_FILE_DEFAULT.getCode());
		theFiles.addFileZip(theClassPathPrefix, LOINC_ANSWERLIST_LINK_FILE_DEFAULT.getCode());
		theFiles.addFileZip(theClassPathPrefix, LOINC_ANSWERLIST_LINK_DUPLICATE_FILE_DEFAULT.getCode());
		theFiles.addFileZip(theClassPathPrefix, LOINC_PART_FILE_DEFAULT.getCode());
		theFiles.addFileZip(theClassPathPrefix, LOINC_PART_RELATED_CODE_MAPPING_FILE_DEFAULT.getCode());
		theFiles.addFileZip(theClassPathPrefix, LOINC_DOCUMENT_ONTOLOGY_FILE_DEFAULT.getCode());
		theFiles.addFileZip(theClassPathPrefix, LOINC_RSNA_PLAYBOOK_FILE_DEFAULT.getCode());
		theFiles.addFileZip(theClassPathPrefix, LOINC_UNIVERSAL_LAB_ORDER_VALUESET_FILE_DEFAULT.getCode());
		theFiles.addFileZip(theClassPathPrefix, LOINC_IEEE_MEDICAL_DEVICE_CODE_MAPPING_TABLE_FILE_DEFAULT.getCode());
		theFiles.addFileZip(theClassPathPrefix, LOINC_IMAGING_DOCUMENT_CODES_FILE_DEFAULT.getCode());
		theFiles.addFileZip(theClassPathPrefix, LOINC_CONSUMER_NAME_FILE_DEFAULT.getCode());
	}
}
