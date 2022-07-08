package ca.uhn.fhir.jpa.term.job;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.batch.api.IBatchJobSubmitter;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.term.TermLoaderSvcImpl;
import ca.uhn.fhir.jpa.term.UploadStatistics;
import ca.uhn.fhir.jpa.term.ZipCollectionBuilder;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.test.utilities.BatchJobHelper;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Properties;

import static ca.uhn.fhir.jpa.batch.config.BatchConstants.JOB_PARAM_CODE_SYSTEM_ID;
import static ca.uhn.fhir.jpa.batch.config.BatchConstants.TERM_CODE_SYSTEM_DELETE_JOB_NAME;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_ANSWERLIST_DUPLICATE_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_ANSWERLIST_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_ANSWERLIST_LINK_DUPLICATE_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_ANSWERLIST_LINK_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_CODESYSTEM_MAKE_CURRENT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_CODESYSTEM_VERSION;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_DOCUMENT_ONTOLOGY_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_DUPLICATE_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_GROUP_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_GROUP_TERMS_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_HIERARCHY_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_IEEE_MEDICAL_DEVICE_CODE_MAPPING_TABLE_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_IMAGING_DOCUMENT_CODES_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_PARENT_GROUP_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_LINK_FILE_PRIMARY_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_LINK_FILE_SUPPLEMENTARY_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_RELATED_CODE_MAPPING_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_RSNA_PLAYBOOK_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_TOP2000_COMMON_LAB_RESULTS_SI_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_TOP2000_COMMON_LAB_RESULTS_US_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_UNIVERSAL_LAB_ORDER_VALUESET_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_UPLOAD_PROPERTIES_FILE;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_XML_FILE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


public class TermCodeSystemDeleteJobTest extends BaseJpaR4Test {

	private final ServletRequestDetails myRequestDetails = new ServletRequestDetails();
	private Properties uploadProperties;

	@Autowired private TermLoaderSvcImpl myTermLoaderSvc;
	@Autowired private IBatchJobSubmitter myJobSubmitter;
	@Autowired private BatchJobHelper myBatchJobHelper;

	@Autowired @Qualifier(TERM_CODE_SYSTEM_DELETE_JOB_NAME)
	private Job myTermCodeSystemDeleteJob;


	private void initMultipleVersionLoad() throws Exception {
		File file = ResourceUtils.getFile("classpath:loinc-ver/" + LOINC_UPLOAD_PROPERTIES_FILE.getCode());
		uploadProperties = new Properties();
		uploadProperties.load(new FileInputStream(file));

		IFhirResourceDao<ValueSet> valueSetIFhirResourceDao = myDaoRegistry.getResourceDao(ValueSet.class);
	}

	@Test
	public void runDeleteJobMultipleVersions() throws Exception {
		initMultipleVersionLoad();

		// loading a loinc CS with version loads two versions (second one with null version)
		String firstCurrentVer = "2.67";
		uploadLoincCodeSystem(firstCurrentVer, true);

		long[] termCodeSystemPidVect = new long[1];  //bypass final restriction
		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());

			TermCodeSystem termCodeSystem = myTermCodeSystemDao.findByCodeSystemUri("http://loinc.org");
			assertNotNull(termCodeSystem);
			termCodeSystemPidVect[0] = termCodeSystem.getPid();

			assertEquals(2, myTermCodeSystemVersionDao.count());
			assertEquals(162, myTermConceptDao.count());
		});

		JobParameters jobParameters = new JobParameters(
			Collections.singletonMap(
				JOB_PARAM_CODE_SYSTEM_ID, new JobParameter(termCodeSystemPidVect[0], true) ));


		JobExecution jobExecution = myJobSubmitter.runJob(myTermCodeSystemDeleteJob, jobParameters);


		myBatchJobHelper.awaitJobCompletion(jobExecution);
		assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());

		runInTransaction(() -> {
			assertEquals(0, myTermCodeSystemDao.count());
			assertNull(myTermCodeSystemDao.findByCodeSystemUri("http://loinc.org"));
			assertEquals(0, myTermCodeSystemVersionDao.count());
			assertEquals(0, myTermConceptDao.count());
		});
	}


	@Test
	public void runWithNoParameterFailsValidation() {
		JobParametersInvalidException thrown = Assertions.assertThrows(
			JobParametersInvalidException.class,
			() -> myJobSubmitter.runJob(myTermCodeSystemDeleteJob, new JobParameters())
		);
		assertEquals(Msg.code(923) + "This job needs Parameter: '" + JOB_PARAM_CODE_SYSTEM_ID + "'", thrown.getMessage());
	}


	@Test
	public void runWithNullParameterFailsValidation() {
		JobParameters jobParameters = new JobParameters(
			Collections.singletonMap(
				JOB_PARAM_CODE_SYSTEM_ID, new JobParameter((Long) null, true) ));

		JobParametersInvalidException thrown = Assertions.assertThrows(
			JobParametersInvalidException.class,
			() -> myJobSubmitter.runJob(myTermCodeSystemDeleteJob, jobParameters)
		);
		assertEquals(Msg.code(924) + "'" + JOB_PARAM_CODE_SYSTEM_ID + "' parameter is null", thrown.getMessage());
	}


	@Test
	public void runWithParameterZeroFailsValidation() {
		JobParameters jobParameters = new JobParameters(
			Collections.singletonMap(
				JOB_PARAM_CODE_SYSTEM_ID, new JobParameter(0L, true) ));

		JobParametersInvalidException thrown = Assertions.assertThrows(
			JobParametersInvalidException.class,
			() -> myJobSubmitter.runJob(myTermCodeSystemDeleteJob, jobParameters)
		);
		assertEquals(Msg.code(925) + "Invalid parameter '" + JOB_PARAM_CODE_SYSTEM_ID + "' value: 0", thrown.getMessage());
	}


	private IIdType uploadLoincCodeSystem(String theVersion, boolean theMakeItCurrent) throws Exception {
		ZipCollectionBuilder files = new ZipCollectionBuilder();

		myRequestDetails.getUserData().put(LOINC_CODESYSTEM_MAKE_CURRENT, theMakeItCurrent);
		uploadProperties.put(LOINC_CODESYSTEM_MAKE_CURRENT.getCode(), Boolean.toString(theMakeItCurrent));

		assertTrue(
			theVersion == null || theVersion.equals("2.67") || theVersion.equals("2.68") || theVersion.equals("2.69"),
			"Version supported are: 2.67, 2.68, 2.69 and null" );

		if (StringUtils.isBlank(theVersion)) {
			uploadProperties.remove(LOINC_CODESYSTEM_VERSION.getCode());
		} else {
			uploadProperties.put(LOINC_CODESYSTEM_VERSION.getCode(), theVersion);
		}

		addLoincMandatoryFilesToZip(files, theVersion);

		UploadStatistics stats = myTermLoaderSvc.loadLoinc(files.getFiles(), mySrd);
		myTerminologyDeferredStorageSvc.saveAllDeferred();

		return stats.getTarget();
	}


	public void addLoincMandatoryFilesToZip(ZipCollectionBuilder theFiles, String theVersion) throws IOException {
		String theClassPathPrefix = getClassPathPrefix(theVersion);
		addBaseLoincMandatoryFilesToZip(theFiles, true, theClassPathPrefix);
		theFiles.addPropertiesZip(uploadProperties, LOINC_UPLOAD_PROPERTIES_FILE.getCode());
		theFiles.addFileZip(theClassPathPrefix, LOINC_PART_LINK_FILE_PRIMARY_DEFAULT.getCode());
		theFiles.addFileZip(theClassPathPrefix, LOINC_PART_LINK_FILE_SUPPLEMENTARY_DEFAULT.getCode());
	}


	private static void addBaseLoincMandatoryFilesToZip(
		ZipCollectionBuilder theFiles, Boolean theIncludeTop2000, String theClassPathPrefix) throws IOException {
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
		if (theIncludeTop2000) {
			theFiles.addFileZip(theClassPathPrefix, LOINC_TOP2000_COMMON_LAB_RESULTS_SI_FILE_DEFAULT.getCode());
			theFiles.addFileZip(theClassPathPrefix, LOINC_TOP2000_COMMON_LAB_RESULTS_US_FILE_DEFAULT.getCode());
		}
	}


	private String getClassPathPrefix(String theVersion) {
		String theClassPathPrefix = "/loinc-ver/v-no-version/";

		if (StringUtils.isBlank(theVersion))   return theClassPathPrefix;

		switch(theVersion) {
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



}
