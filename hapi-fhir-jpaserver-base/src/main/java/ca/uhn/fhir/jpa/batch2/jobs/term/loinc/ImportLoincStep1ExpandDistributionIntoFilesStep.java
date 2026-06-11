/*-
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
package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseExpandDistributionIntoFilesStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyJobParameters;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyMetadataAttachmentJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.annotation.Nonnull;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ValueSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Supplier;

import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincJobAppCtx.STEP_ID_FINALIZE_IMPORT;
import static org.hl7.fhir.common.hapi.validation.support.ValidationConstants.LOINC_ALL_VALUESET_ID;
import static org.hl7.fhir.common.hapi.validation.support.ValidationConstants.LOINC_GENERIC_VALUESET_URL;

/**
 * @see ImportLoincJobAppCtx#importLoincStep1ExpandDistributionIntoFiles()
 */
public class ImportLoincStep1ExpandDistributionIntoFilesStep
		extends BaseExpandDistributionIntoFilesStep<
				ImportTerminologyJobParameters, ImportLoincStep1ExpandDistributionIntoFilesStep.MyContext> {
	private static final Logger ourLog = LoggerFactory.getLogger(ImportLoincStep1ExpandDistributionIntoFilesStep.class);

	@Override
	protected MyContext newContextObject() {
		return new MyContext();
	}

	@Nonnull
	@Override
	protected String getDistributionFileName() {
		return TerminologyConstants.FILENAME_LOINC_DISTRIBUTION_FILE;
	}

	@Override
	protected void handleSynchronous(
			StepExecutionDetails<ImportTerminologyJobParameters, VoidModel> theStepExecutionDetails,
			IJobDataSink<TerminologyFileSetJson> theDataSink,
			MyContext theContext,
			String theSingleFileName,
			Supplier<InputStream> theInputStreamSupplier,
			ImportTerminologyJobParameters theJobParameters,
			ImportTerminologyMetadataAttachmentJson theJobMetadataAttachment)
			throws IOException {
		super.handleSynchronous(
				theStepExecutionDetails,
				theDataSink,
				theContext,
				theSingleFileName,
				theInputStreamSupplier,
				theJobParameters,
				theJobMetadataAttachment);

		if (theSingleFileName.endsWith("loinc.xml")) {
			theContext.incrementLoincXmlCount();
			handleLoincXml(theInputStreamSupplier, theJobMetadataAttachment);
		}
	}

	private void handleLoincXml(
			Supplier<InputStream> theInputStream, ImportTerminologyMetadataAttachmentJson theJobMetadataAttachment)
			throws IOException {
		ourLog.info("Processing 'loinc.xml' file");

		String loincCodeSystemXml = IOUtils.toString(theInputStream.get(), StandardCharsets.UTF_8);
		theJobMetadataAttachment.setCodeSystemXml(loincCodeSystemXml);

		CodeSystem codeSystem = theJobMetadataAttachment.getCodeSystem();
		if (!"http://loinc.org".equals(codeSystem.getUrl())) {
			throw new JobExecutionFailedException(Msg.code(876)
					+ "'loinc.xml' file must have URL of 'http://loinc.org'. Found: " + codeSystem.getUrl());
		}
	}

	@Override
	protected void afterCompletionOfFileProcessing(
			MyContext theContext, IJobDataSink<TerminologyFileSetJson> theDataSink) {
		super.afterCompletionOfFileProcessing(theContext, theDataSink);

		if (theContext.getLoincXmlCount() == 0) {
			throw new JobExecutionFailedException(Msg.code(2950) + "No 'loinc.xml' file found in ZIP");
		}
		if (theContext.getLoincXmlCount() > 1) {
			throw new JobExecutionFailedException(Msg.code(2951) + "Multiple 'loinc.xml' file found in ZIP");
		}
	}

	@Override
	protected void massageCodeSystem(
			CodeSystem theCodeSystem,
			MyContext theContext,
			StepExecutionDetails<ImportTerminologyJobParameters, VoidModel> theStepExecutionDetails) {
		super.massageCodeSystem(theCodeSystem, theContext, theStepExecutionDetails);

		// TODO: DM 2019-09-13 - Manually add EXTERNAL_COPYRIGHT_NOTICE property until Regenstrief adds this to
		// loinc.xml
		if (theCodeSystem.getProperty().stream().noneMatch(t -> "EXTERNAL_COPYRIGHT_NOTICE".equals(t.getCode()))) {
			theCodeSystem.addProperty().setCode("EXTERNAL_COPYRIGHT_NOTICE").setType(CodeSystem.PropertyType.STRING);
		}
	}

	@Nonnull
	@Override
	protected String getCodeSystemIdRoot() {
		return "loinc";
	}

	@Override
	protected void startStaging(
			StepExecutionDetails<ImportTerminologyJobParameters, VoidModel> theStepExecutionDetails,
			IJobDataSink<TerminologyFileSetJson> theDataSink,
			ImportTerminologyJobParameters theJobParameters,
			ImportTerminologyMetadataAttachmentJson theJobMetadataAttachment,
			MyContext theContext) {
		super.startStaging(
				theStepExecutionDetails, theDataSink, theJobParameters, theJobMetadataAttachment, theContext);

		CodeSystem cs = theJobMetadataAttachment.getCodeSystem();

		// Create the "all loinc" ValueSet
		ValueSet valueSet = getValueSetLoincAll(cs.getVersion(), cs.getCopyright());
		IFhirResourceDao valueSetDao = myDaoRegistry.getResourceDao("ValueSet");
		RequestDetails srd = theStepExecutionDetails.newSystemRequestDetails();
		valueSetDao.update(myVersionCanonicalizer.valueSetFromCanonical(valueSet), srd);

		TerminologyFileSetJson fileSet = new TerminologyFileSetJson();
		fileSet.addResourceToActivate("ValueSet/" + valueSet.getIdElement().getIdPart());
		theDataSink.acceptForFutureStep(STEP_ID_FINALIZE_IMPORT, fileSet);
	}

	private ValueSet getValueSetLoincAll(String theLoincVersion, String theCopyrightStatement) {
		ValueSet retVal = new ValueSet();

		retVal.setId(LOINC_ALL_VALUESET_ID + "-" + theLoincVersion);
		retVal.setUrl(LOINC_GENERIC_VALUESET_URL);
		retVal.setVersion(theLoincVersion);
		retVal.setName("All LOINC codes");
		retVal.setStatus(Enumerations.PublicationStatus.DRAFT);
		retVal.setDate(new Date());
		retVal.setPublisher("Regenstrief Institute, Inc.");
		retVal.setDescription("A value set that includes all LOINC codes");
		retVal.setCopyright(theCopyrightStatement);
		retVal.getCompose()
				.addInclude()
				.setSystem(TerminologyConstants.LOINC_URI)
				.setVersion(theLoincVersion);

		return retVal;
	}

	public static class MyContext {

		private int myLoincXmlCount;

		public void incrementLoincXmlCount() {
			myLoincXmlCount++;
		}

		public int getLoincXmlCount() {
			return myLoincXmlCount;
		}
	}
}
