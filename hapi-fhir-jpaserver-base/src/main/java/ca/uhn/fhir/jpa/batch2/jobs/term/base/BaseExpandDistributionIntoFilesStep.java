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
package ca.uhn.fhir.jpa.batch2.jobs.term.base;

import ca.uhn.fhir.batch2.api.AttachmentContentTypeEnum;
import ca.uhn.fhir.batch2.api.AttachmentDetails;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobDefinitionStep;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.term.LoadedFileDescriptors;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.util.CsvUtil;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import jakarta.annotation.Nonnull;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Enumerations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincJobAppCtx.STEP_ID_CHUNK_CONCEPTS_FOR_CLOSURE_GENERATION;
import static ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc.MAKE_LOADING_VERSION_CURRENT;
import static org.apache.commons.lang3.ObjectUtils.getIfNull;

public abstract class BaseExpandDistributionIntoFilesStep<PT extends BaseTerminologyImportParameters, CT>
		implements IJobStepWorker<PT, VoidModel, TerminologyFileSetJson> {

	private static final Logger ourLog = LoggerFactory.getLogger(BaseExpandDistributionIntoFilesStep.class);

	@Autowired
	protected DaoRegistry myDaoRegistry;

	@Autowired
	private ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;

	@Autowired
	protected IJobPersistence myJobPersistence;

	@Autowired
	protected VersionCanonicalizer myVersionCanonicalizer;

	private Integer myChunkLineSizeForUnitTests = null;

	@VisibleForTesting
	public void setChunkLineSizeForUnitTest(int theChunkLineSize) {
		myChunkLineSizeForUnitTests = theChunkLineSize;
	}

	protected abstract CT newContextObject();

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<PT, VoidModel> theStepExecutionDetails,
			@Nonnull IJobDataSink<TerminologyFileSetJson> theDataSink)
			throws JobExecutionFailedException {

		String instanceId = theStepExecutionDetails.getInstance().getInstanceId();
		PT jobParameters = theStepExecutionDetails.getParameters();
		AttachmentDetails loincFileAttachment =
				myJobPersistence.fetchAttachmentByFilename(instanceId, getDistributionFileName());
		CT context = newContextObject();
		ImportTerminologyMetadataAttachmentJson jobMetadataAttachment = new ImportTerminologyMetadataAttachmentJson();

		ourLog.info(
				"Import {}[{}] - Expanding file {}",
				getTerminologyName(),
				instanceId,
				loincFileAttachment.getFilename());

		TerminologyFileSetJson fileSet = newTerminologyFileSetJson();

		Set<String> stepsWhichHaveNotFoundFileAndNeedTo = theStepExecutionDetails.getJobDefinition().getSteps().stream()
				.filter(t -> t.getJobStepWorker() instanceof ITerminologyImportFileHandlerStep<?, ?, ?>)
				.filter(t -> ((ITerminologyImportFileHandlerStep<PT, ?, ?>) t.getJobStepWorker()).mustFindFile())
				.map(JobDefinitionStep::getStepId)
				.collect(Collectors.toCollection(LinkedHashSet::new));

		try (InputStream inputStream = loincFileAttachment.getInputStream()) {
			try (BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
				ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(bufferedInputStream);
				ZipArchiveEntry entry;
				while ((entry = zipInputStream.getNextEntry()) != null) {
					try (BOMInputStream fis = new LoadedFileDescriptors.NonClosableBOMInputStream(zipInputStream)) {
						String nextFileName = entry.getName();

						ourLog.info(
								"Import {}[{}] - Processing file: {}", getTerminologyName(), instanceId, nextFileName);

						byte[] bytes = IOUtils.toByteArray(fis);

						// Asynchronous processing (prepare work chunks based on input file)
						List<StepIdAndFileHandlingInstructions> processors =
								getStepIdAndFileHandlingInstructionsForFileName(
										theStepExecutionDetails,
										jobParameters,
										theStepExecutionDetails.getJobDefinition(),
										nextFileName);

						for (StepIdAndFileHandlingInstructions processor : processors) {
							stepsWhichHaveNotFoundFileAndNeedTo.remove(processor.stepId());
						}

						ListMultimap<ITerminologyImportFileHandlerStep.FileHandlingType, String>
								fileHandlingTypeToAttachmentIds = MultimapBuilder.hashKeys()
										.arrayListValues()
										.build();

						for (StepIdAndFileHandlingInstructions processor : processors) {
							ITerminologyImportFileHandlerStep.FileHandlingType fileHandlingType =
									processor.fileHandlingInstructions().fileHandlingType();

							if (fileHandlingTypeToAttachmentIds.containsKey(fileHandlingType)) {
								List<String> attachmentIds = fileHandlingTypeToAttachmentIds.get(fileHandlingType);
								for (String attachmentId : attachmentIds) {
									TerminologyFileSetJson data = newTerminologyFileSetJson();
									data.setSourceFilename(nextFileName);
									data.setAttachmentId(attachmentId);
									theDataSink.acceptForFutureStep(processor.stepId(), data);
								}
								continue;
							}

							List<String> attachmentIds =
									switch (fileHandlingType) {
										case CSV_SPLIT_WITH_REPEAT_HEADER_1000_LINE_CHUNKS -> {
											int chunkSize = getIfNull(myChunkLineSizeForUnitTests, 1000);
											yield csvSplitWithRepeatHeader(
													',',
													instanceId,
													bytes,
													loincFileAttachment.getFilename(),
													nextFileName,
													processor.stepId(),
													chunkSize,
													theDataSink);
										}
										case CSV_SPLIT_WITH_REPEAT_HEADER_50000_LINE_CHUNKS -> {
											int chunkSize = getIfNull(myChunkLineSizeForUnitTests, 50000);
											yield csvSplitWithRepeatHeader(
													',',
													instanceId,
													bytes,
													loincFileAttachment.getFilename(),
													nextFileName,
													processor.stepId(),
													chunkSize,
													theDataSink);
										}
										case TSV_SPLIT_WITH_REPEAT_HEADER_5000_LINE_CHUNKS -> {
											int chunkSize = getIfNull(myChunkLineSizeForUnitTests, 5000);
											yield csvSplitWithRepeatHeader(
													'\t',
													instanceId,
													bytes,
													loincFileAttachment.getFilename(),
													nextFileName,
													processor.stepId(),
													chunkSize,
													theDataSink);
										}
									};
							fileHandlingTypeToAttachmentIds.putAll(fileHandlingType, attachmentIds);
						}

						// Synchronous processing (anything that is small enough to just handle it here)
						handleSynchronous(
								theStepExecutionDetails,
								theDataSink,
								context,
								nextFileName,
								bytes,
								jobParameters,
								fileSet,
								jobMetadataAttachment);
					}
				}
			}
		} catch (IOException e) {
			throw new JobExecutionFailedException(
					Msg.code(2938) + "Files to expand " + getTerminologyName() + " zip file: " + e.getMessage(), e);
		}

		if (!stepsWhichHaveNotFoundFileAndNeedTo.isEmpty()) {
			throw new JobExecutionFailedException(
					"No files in the distribution were matched by step(s): " + stepsWhichHaveNotFoundFileAndNeedTo);
		}

		afterCompletionOfFileProcessing(context, theDataSink);

		startStaging(theStepExecutionDetails, theDataSink, jobParameters, jobMetadataAttachment);

		AttachmentDetails attachmentRequest = new AttachmentDetails(
				new ByteArrayInputStream(
						JsonUtil.serialize(jobMetadataAttachment).getBytes(StandardCharsets.UTF_8)),
				AttachmentContentTypeEnum.JSON,
				ImportTerminologyMetadataAttachmentJson.ATTACHMENT_FILENAME);
		myJobPersistence.storeNewAttachment(instanceId, attachmentRequest);

		return RunOutcome.SUCCESS;
	}

	@Nonnull
	protected abstract String getDistributionFileName();

	protected void afterCompletionOfFileProcessing(CT theContext, IJobDataSink<TerminologyFileSetJson> theDataSink) {
		// subclasses can override this method to do any cleanup
	}

	protected void startStaging(
			StepExecutionDetails<PT, VoidModel> theStepExecutionDetails,
			IJobDataSink<TerminologyFileSetJson> theDataSink,
			PT theJobParameters,
			ImportTerminologyMetadataAttachmentJson jobMetadataAttachment) {
		CodeSystem cs = jobMetadataAttachment.getCodeSystem();
		if (cs == null) {
			cs = new CodeSystem();
		}

		String codeSystemVersionId = theJobParameters.getVersionId();
		assert codeSystemVersionId != null;

		cs.setId(getCodeSystemIdRoot() + "-" + codeSystemVersionId);
		cs.setVersion(codeSystemVersionId);
		cs.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);

		massageCodeSystem(cs);

		jobMetadataAttachment.setCodeSystem(cs);

		// Create the CodeSystem resource
		SystemRequestDetails srd = theStepExecutionDetails.newSystemRequestDetails();
		srd.getUserData().put(MAKE_LOADING_VERSION_CURRENT, Boolean.FALSE);
		IFhirResourceDao codeSystemDao = myDaoRegistry.getResourceDao("CodeSystem");
		codeSystemDao.update(myVersionCanonicalizer.codeSystemFromCanonical(cs), srd);

		ITermCodeSystemStorageSvc.StartStagingCodeSystemVersionResponse response =
				myTermCodeSystemStorageSvc.startStagingCodeSystemVersion(cs.getUrl(), cs.getVersion());
		jobMetadataAttachment.setCodeSystemStagingVersionId(response.stagingVersionId());

		// Send a single chunk to trigger the first closure generation step
		TerminologyFileSetJson fileSet = new TerminologyFileSetJson();
		theDataSink.acceptForFutureStep(STEP_ID_CHUNK_CONCEPTS_FOR_CLOSURE_GENERATION, fileSet);
	}

	@Nonnull
	protected abstract String getCodeSystemIdRoot();

	protected void massageCodeSystem(CodeSystem theCodeSystem) {
		// subclasses can override this method to massage the CodeSystem
	}

	/**
	 * Subclasses can override this method to handle files that are small enough to just handle here.
	 */
	protected void handleSynchronous(
			StepExecutionDetails<PT, VoidModel> theStepExecutionDetails,
			IJobDataSink<TerminologyFileSetJson> theDataSink,
			CT theContext,
			String theFileName,
			byte[] theBytes,
			PT theJobParameters,
			TerminologyFileSetJson theFileSet,
			ImportTerminologyMetadataAttachmentJson theJobMetadataAttachment) {
		// nothing
	}

	private TerminologyFileSetJson newTerminologyFileSetJson() {
		return new TerminologyFileSetJson();
	}

	/**
	 * @return A list of attachment IDs for the work chunks that were created.
	 */
	private List<String> csvSplitWithRepeatHeader(
			char theDelimiter,
			String theJobInstanceId,
			byte[] theBytes,
			String theZipOuterFilename,
			String theZipInnerFilename,
			String theStepId,
			int theChunkLines,
			IJobDataSink<TerminologyFileSetJson> theDataSink)
			throws JobExecutionFailedException {
		List<String> attachmentIds = new ArrayList<>();

		String filename = theZipInnerFilename;
		int lastSlash = Math.max(filename.lastIndexOf('/'), filename.lastIndexOf('\\'));
		if (lastSlash != -1) {
			filename = filename.substring(lastSlash + 1);
		}

		ByteArrayInputStream bis = new ByteArrayInputStream(theBytes);
		InputStreamReader reader = new InputStreamReader(bis, StandardCharsets.UTF_8);
		try (CSVParser parser = newCsvParser(theDelimiter, reader)) {

			List<String> headers = parser.getHeaderNames();
			Iterator<CSVRecord> recordIterator = parser.getRecords().iterator();
			List<CSVRecord> recordsBuffer = new ArrayList<>();
			int startRow = 0;
			int endRow = 0;
			while (recordIterator.hasNext()) {

				CSVRecord nextRecord = recordIterator.next();
				recordsBuffer.add(nextRecord);

				if (recordsBuffer.size() == theChunkLines || !recordIterator.hasNext()) {
					String attachmentId =
							writeChunk(theJobInstanceId, headers, recordsBuffer, filename, startRow, endRow);

					TerminologyFileSetJson data = newTerminologyFileSetJson();
					data.setSourceFilename(theZipInnerFilename);
					data.setAttachmentId(attachmentId);
					theDataSink.acceptForFutureStep(theStepId, data);

					recordsBuffer.clear();
					attachmentIds.add(attachmentId);

					startRow = endRow + 1;
					endRow = startRow;
				} else {
					endRow++;
				}
			}

		} catch (IOException e) {
			throw new JobExecutionFailedException(
					Msg.code(2939) + "Failed to parse " + theZipInnerFilename + " in zip file: " + theZipOuterFilename
							+ ": " + e,
					e);
		}

		return attachmentIds;
	}

	private String writeChunk(
			String theJobInstanceId,
			List<String> theHeaders,
			List<CSVRecord> theRecordsBuffer,
			String theFilename,
			int theStartRow,
			int theEndRow) {
		CsvUtil.ICsvProducer producer = thePrinter -> {
			for (CSVRecord record : theRecordsBuffer) {
				thePrinter.printRecord(record);
			}
		};
		byte[] bytes = CsvUtil.writeCsvToByteArray(theHeaders.toArray(new String[0]), producer);
		AttachmentDetails attachmentDetails = AttachmentDetails.build()
				.withContentType(AttachmentContentTypeEnum.CSV)
				.withBytes(bytes)
				.withFilename(theFilename + "_" + theStartRow + "-" + theEndRow)
				.build();
		return myJobPersistence.storeNewAttachment(theJobInstanceId, attachmentDetails);
	}

	@Nonnull
	private String getTerminologyName() {
		return "LOINC";
	}

	private List<StepIdAndFileHandlingInstructions> getStepIdAndFileHandlingInstructionsForFileName(
			StepExecutionDetails<PT, VoidModel> theStepExecutionDetails,
			PT theJobParameters,
			JobDefinition<PT> theJobDefinition,
			String theStepId) {
		List<StepIdAndFileHandlingInstructions> stepProcessingInstructions = new ArrayList<>();

		for (JobDefinitionStep<PT, ?, ?> step : theJobDefinition.getSteps()) {
			if (step.getJobStepWorker() instanceof ITerminologyImportFileHandlerStep<PT, ?, ?> fileHandler) {
				fileHandler
						.canHandleFile(theStepExecutionDetails, theJobParameters, theStepId)
						.ifPresent(instructions -> stepProcessingInstructions.add(
								new StepIdAndFileHandlingInstructions(step.getStepId(), instructions)));
			}
		}

		return stepProcessingInstructions;
	}

	@Nonnull
	public static CSVParser newCsvParser(char theDelimiter, Reader theReader) throws IOException {
		Character quoteCharacter = '"';
		if (theDelimiter == '\t') {
			quoteCharacter = null;
		}

		return new CSVParser(
				theReader,
				CSVFormat.DEFAULT
						.builder()
						.setDelimiter(theDelimiter)
						.setEscape(null)
						.setIgnoreEmptyLines(true)
						.setQuote(quoteCharacter)
						.setRecordSeparator('\n')
						.setNullString("")
						.setQuoteMode(QuoteMode.NON_NUMERIC)
						.setHeader()
						.setSkipHeaderRecord(true)
						.setTrim(true)
						.get());
	}

	private record StepIdAndFileHandlingInstructions(
			String stepId, ITerminologyImportFileHandlerStep.FileHandlingInstructions fileHandlingInstructions) {}
}
