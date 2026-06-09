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
import ca.uhn.fhir.batch2.api.AttachmentMetadata;
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
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Enumerations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

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
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyUtil.getJobProperties;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincJobAppCtx.STEP_ID_CHUNK_CONCEPTS_FOR_CLOSURE_GENERATION;
import static ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc.MAKE_LOADING_VERSION_CURRENT;
import static org.apache.commons.lang3.ObjectUtils.getIfNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseExpandDistributionIntoFilesStep<PT extends ImportTerminologyJobParameters, CT>
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
	public void setChunkLineSizeForUnitTest(Integer theChunkLineSize) {
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
		CT context = newContextObject();
		ImportTerminologyMetadataAttachmentJson jobMetadataAttachment = new ImportTerminologyMetadataAttachmentJson();

		Set<String> stepsWhichHaveNotFoundFileAndNeedTo = theStepExecutionDetails.getJobDefinition().getSteps().stream()
				.filter(t -> t.getJobStepWorker() instanceof ITerminologyImportFileHandlerStep<?, ?, ?>)
				.filter(t -> ((ITerminologyImportFileHandlerStep<PT, ?, ?>) t.getJobStepWorker()).mustFindFile())
				.map(JobDefinitionStep::getStepId)
				.collect(Collectors.toCollection(LinkedHashSet::new));

		AttachmentDetails distributionZipAttachment = null;
		try {
			distributionZipAttachment =
					myJobPersistence.fetchAttachmentByFilename(instanceId, getDistributionFileName());
		} catch (ResourceNotFoundException e) {
			if (!isIndividualFileAttachmentsSupported()) {
				throw new JobExecutionFailedException(
						Msg.code(1) + "No distribution file (" + getDistributionFileName() + ") was attached for "
								+ getTerminologyName(),
						e);
			}
		}
		if (distributionZipAttachment != null) {
			processDistributionZip(
					theStepExecutionDetails,
					theDataSink,
					instanceId,
					distributionZipAttachment,
					stepsWhichHaveNotFoundFileAndNeedTo,
					context,
					jobParameters,
					jobMetadataAttachment);
		} else if (isIndividualFileAttachmentsSupported()) {
			Pageable page = Pageable.ofSize(10);
			List<AttachmentMetadata> attachments;
			do {
				attachments = myJobPersistence.listAttachmentsForJobInstance(page, instanceId);
				for (AttachmentMetadata attachment : attachments) {
					String filename = attachment.filename();
					if (isNotBlank(filename)) {
						Supplier<InputStream> inputStream = () -> myJobPersistence
								.fetchAttachmentById(instanceId, attachment.attachmentId())
								.getInputStream();
						try {
							processSingleFile(
									theStepExecutionDetails,
									theDataSink,
									context,
									jobParameters,
									jobMetadataAttachment,
									filename,
									instanceId,
									filename,
									attachment.attachmentId(),
									inputStream,
									stepsWhichHaveNotFoundFileAndNeedTo);
						} catch (IOException e) {
							throw new JobExecutionFailedException(
									Msg.code(2968) + "Failed to expand " + getTerminologyName() + " file " + filename
											+ ": " + e.getMessage(),
									e);
						}
					}
				}
				page = page.next();
			} while (!attachments.isEmpty());
		}

		if (!stepsWhichHaveNotFoundFileAndNeedTo.isEmpty()) {
			throw new JobExecutionFailedException(Msg.code(2956)
					+ "No files in the distribution were matched by step(s): " + stepsWhichHaveNotFoundFileAndNeedTo);
		}

		afterCompletionOfFileProcessing(context, theDataSink);

		startStaging(theStepExecutionDetails, theDataSink, jobParameters, jobMetadataAttachment, context);

		AttachmentDetails attachmentRequest = new AttachmentDetails(
				new ByteArrayInputStream(
						JsonUtil.serialize(jobMetadataAttachment).getBytes(StandardCharsets.UTF_8)),
				AttachmentContentTypeEnum.JSON,
				ImportTerminologyMetadataAttachmentJson.ATTACHMENT_FILENAME);
		myJobPersistence.storeNewAttachment(instanceId, attachmentRequest);

		return RunOutcome.SUCCESS;
	}

	private void processDistributionZip(
			@Nonnull StepExecutionDetails<PT, VoidModel> theStepExecutionDetails,
			@Nonnull IJobDataSink<TerminologyFileSetJson> theDataSink,
			String instanceId,
			AttachmentDetails loincFileAttachment,
			Set<String> stepsWhichHaveNotFoundFileAndNeedTo,
			CT context,
			PT jobParameters,
			ImportTerminologyMetadataAttachmentJson jobMetadataAttachment) {
		String distributionZipFilename = loincFileAttachment.getFilename();
		ourLog.info("Import {}[{}] - Expanding file {}", getTerminologyName(), instanceId, distributionZipFilename);

		try (InputStream inputStream = loincFileAttachment.getInputStream()) {
			try (BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
				ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(bufferedInputStream);
				ZipArchiveEntry entry;
				while ((entry = zipInputStream.getNextEntry()) != null) {
					try (InputStream fis = new LoadedFileDescriptors.NonClosableBOMInputStream(zipInputStream)) {
						String nextFileName = entry.getName();

						ourLog.info(
								"Import {}[{}] - Processing file from distribution: {}",
								getTerminologyName(),
								instanceId,
								nextFileName);

						processSingleFile(
								theStepExecutionDetails,
								theDataSink,
								context,
								jobParameters,
								jobMetadataAttachment,
								distributionZipFilename,
								instanceId,
								nextFileName,
								null,
								() -> fis,
								stepsWhichHaveNotFoundFileAndNeedTo);

						// If no steps handled the bytes, consume them now so we
						// can proceed to the next entry in the ZIP
						IOUtils.consume(fis);
					}
				}
			}
		} catch (IOException e) {
			throw new JobExecutionFailedException(
					Msg.code(2938) + "Files to expand " + getTerminologyName() + " zip file: " + e.getMessage(), e);
		}
	}

	/**
	 * This method processes a single file (by filename) from a terminology distribution. So, for
	 * a ZIP file containing a bunch of different files, this method will be called once per file
	 * in the ZIP.
	 * <p>
	 * For each file, it checks with each step in the import job definition to see whether that step
	 * wants to handle this file and if so it forwards a work chunk to that step containing the
	 * contents of the file (or if the file is really big and the step indicates that it can handle it,
	 * a bunch of work chunks are created with subsections of the file).
	 * </p>
	 *
	 * @param theStepExecutionDetails The Batch2 step execution details.
	 * @param theDataSink The Batch2 data sink.
	 * @param theContext The context object instance for this step type.
	 * @param theJobInstanceId The job instance ID.
	 * @param theJobParameters The job instance parameters.
	 * @param theJobMetadataAttachment The job metadata attachment for this job instance.
	 * @param theContainerFileName The filename of the file which contained the file being processed. For example, if the
	 *                             file being processed came from a ZIP file, this is the name of that ZIP file. If the file being
	 *                             processed was a direct attachment to the job, this parameter should have the same
	 *                             value as {@literal theSingleFileName}.
	 * @param theSingleFileName The filename of the individual file being processed.
	 * @param theSingleFileAttachmentIdOrNull If the file came directly from an attachment, this is the ID of that attachment. If the file came from within a broader
	 *                                        attachment (e.g. it was one file within a larger ZIP file that was the attachment), this
	 *                                        should be <code>null</code>.
	 * @param theSingleFileInputStreamSupplier A supplier which will be invoked to get an InputStream for the file contents if the file is requested by any steps.
	 * @param theStepsWhichHaveNotFoundFileAndNeedTo This Set starts off with the IDs of all processing steps which indicate that they are required to find a file. If a step
	 *                                               accepts an individual file, it should remove its own ID from the Set so that only steps which did not
	 *                                               process any files remain in the set at the end.
	 */
	private void processSingleFile(
			@Nonnull StepExecutionDetails<PT, VoidModel> theStepExecutionDetails,
			@Nonnull IJobDataSink<TerminologyFileSetJson> theDataSink,
			CT theContext,
			PT theJobParameters,
			ImportTerminologyMetadataAttachmentJson theJobMetadataAttachment,
			String theContainerFileName,
			String theJobInstanceId,
			String theSingleFileName,
			@Nullable String theSingleFileAttachmentIdOrNull,
			Supplier<InputStream> theSingleFileInputStreamSupplier,
			Set<String> theStepsWhichHaveNotFoundFileAndNeedTo)
			throws IOException {
		// Asynchronous processing (prepare work chunks based on input file)
		List<StepIdAndFileHandlingInstructions> processors = getStepIdAndFileHandlingInstructionsForFileName(
				theStepExecutionDetails, theStepExecutionDetails.getJobDefinition(), theSingleFileName);

		ListMultimap<ITerminologyImportFileHandlerStep.FileHandlingType, String> fileHandlingTypeToAttachmentIds =
				MultimapBuilder.hashKeys().arrayListValues().build();

		for (StepIdAndFileHandlingInstructions processor : processors) {
			theStepsWhichHaveNotFoundFileAndNeedTo.remove(processor.stepId());
			ITerminologyImportFileHandlerStep.FileHandlingType fileHandlingType = processor.fileHandlingType();

			if (fileHandlingTypeToAttachmentIds.containsKey(fileHandlingType)) {
				List<String> attachmentIds = fileHandlingTypeToAttachmentIds.get(fileHandlingType);
				for (String attachmentId : attachmentIds) {
					TerminologyFileSetJson data = newTerminologyFileSetJson();
					data.setSourceFilename(theSingleFileName);
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
									fileHandlingType,
									',',
									theJobInstanceId,
									theSingleFileInputStreamSupplier.get(),
									theContainerFileName,
									theSingleFileName,
									processor.stepId(),
									chunkSize,
									theDataSink);
						}
						case CSV_SPLIT_WITH_REPEAT_HEADER_50000_LINE_CHUNKS -> {
							int chunkSize = getIfNull(myChunkLineSizeForUnitTests, 50000);
							yield csvSplitWithRepeatHeader(
									fileHandlingType,
									',',
									theJobInstanceId,
									theSingleFileInputStreamSupplier.get(),
									theContainerFileName,
									theSingleFileName,
									processor.stepId(),
									chunkSize,
									theDataSink);
						}
						case TSV_SPLIT_WITH_REPEAT_HEADER_5000_LINE_CHUNKS -> {
							int chunkSize = getIfNull(myChunkLineSizeForUnitTests, 5000);
							yield csvSplitWithRepeatHeader(
									fileHandlingType,
									'\t',
									theJobInstanceId,
									theSingleFileInputStreamSupplier.get(),
									theContainerFileName,
									theSingleFileName,
									processor.stepId(),
									chunkSize,
									theDataSink);
						}
						case XML -> {
							String attachmentId;
							if (theSingleFileAttachmentIdOrNull != null) {
								attachmentId = theSingleFileAttachmentIdOrNull;
							} else {
								AttachmentDetails attachmentRequest = new AttachmentDetails(
										theSingleFileInputStreamSupplier.get(),
										AttachmentContentTypeEnum.ZIP,
										theSingleFileName);
								attachmentId = myJobPersistence.storeNewAttachment(theJobInstanceId, attachmentRequest);
							}
							TerminologyFileSetJson data = newTerminologyFileSetJson();
							data.setSourceFilename(theSingleFileName);
							data.setAttachmentId(attachmentId);
							theDataSink.acceptForFutureStep(processor.stepId(), data);
							yield List.of(attachmentId);
						}
					};
			fileHandlingTypeToAttachmentIds.putAll(fileHandlingType, attachmentIds);
		}

		// Synchronous processing (anything that is small enough to just handle it here)
		handleSynchronous(
				theStepExecutionDetails,
				theDataSink,
				theContext,
				theSingleFileName,
				theSingleFileInputStreamSupplier,
				theJobParameters,
				theJobMetadataAttachment);
	}

	/**
	 * Subclasses may override if this type of job supports the attaching of
	 * individual files instead of just a single ZIP distribution file.
	 * CodeSystems like LOINC and SNOMED CT can't be imported without a complete
	 * collection of inter-related files, so we will always require the outer
	 * ZIP file to be attached (return = true). Other CodeSystems like ICD or "custom CSV" jobs
	 * make sense with only a single content file, so we allow either the
	 * distribution ZIP or the individual file(s) to be attached (return = false).
	 */
	protected boolean isIndividualFileAttachmentsSupported() {
		return false;
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
			ImportTerminologyMetadataAttachmentJson jobMetadataAttachment,
			CT theContext) {
		CodeSystem cs = jobMetadataAttachment.getCodeSystem();
		if (cs == null) {
			cs = new CodeSystem();
		}

		String codeSystemVersionId = theJobParameters.getVersionId();
		Validate.notBlank(codeSystemVersionId, "No version ID specified in job parameters");
		cs.setVersion(codeSystemVersionId);

		String url = theJobParameters.getUrl();
		Validate.notBlank(url, "No URL specified in job parameters");
		cs.setUrl(url);

		massageCodeSystem(cs, theContext, theStepExecutionDetails);

		if (getCodeSystemIdRoot() != null) {
			cs.setId(getCodeSystemIdRoot() + "-" + codeSystemVersionId);
		} else {
			Validate.notNull(cs.getId(), "CodeSystem was not assigned an ID");
		}

		cs.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);

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

	/**
	 * Subclasses may only return <code>null</code> if they intend to explicitly supply
	 * an ID for the CodeSystem in the {@link #massageCodeSystem(CodeSystem, Object, StepExecutionDetails)} method.
	 */
	@Nullable
	protected abstract String getCodeSystemIdRoot();

	/**
	 * Subclasses may override this method to make modifications to the CodeSystem
	 * resource that will be stored in the database to support this CodeSystem.
	 */
	protected void massageCodeSystem(
			CodeSystem theCodeSystem, CT theContext, StepExecutionDetails<PT, VoidModel> theStepExecutionDetails) {
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
			Supplier<InputStream> theInputStreamSupplier,
			PT theJobParameters,
			ImportTerminologyMetadataAttachmentJson theJobMetadataAttachment)
			throws IOException {
		// nothing
	}

	private TerminologyFileSetJson newTerminologyFileSetJson() {
		return new TerminologyFileSetJson();
	}

	/**
	 * @return A list of attachment IDs for the work chunks that were created.
	 */
	private List<String> csvSplitWithRepeatHeader(
			ITerminologyImportFileHandlerStep.FileHandlingType theFileHandlingType,
			char theDelimiter,
			String theJobInstanceId,
			InputStream theInputStream,
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

		/*
		 * Avoid filename collisions if two different steps want chunks from the same file,
		 * but they want them with different chunk sizes, e.g. if one step wants CSVs split
		 * into chunks 1000 and another wants chunks of 50000. Using the file handling type
		 * ordinal as a suffix ensures unique filenames for different chunk sizes.
		 *
		 * Note that these filenames aren't actually used for anything other than making
		 * the logs slightly more meaningful for troubleshooting, since the job steps
		 * use the attachment IDs to fetch these attachments and not the filenames. Also
		 * they only survive as long as the job does. So we don't need to worry about
		 * ordinal changes over time.
		 */
		filename += "_" + theFileHandlingType.ordinal();

		InputStreamReader reader = new InputStreamReader(theInputStream, StandardCharsets.UTF_8);
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
			JobDefinition<PT> theJobDefinition,
			String theStepId) {
		List<StepIdAndFileHandlingInstructions> stepProcessingInstructions = new ArrayList<>();

		for (JobDefinitionStep<PT, ?, ?> step : theJobDefinition.getSteps()) {
			if (step.getJobStepWorker() instanceof ITerminologyImportFileHandlerStep<PT, ?, ?> fileHandler) {
				canHandleFile(theStepExecutionDetails, fileHandler, theStepId)
						.ifPresent(instructions -> stepProcessingInstructions.add(
								new StepIdAndFileHandlingInstructions(step.getStepId(), instructions)));
			}
		}

		return stepProcessingInstructions;
	}

	@Nonnull
	private Optional<ITerminologyImportFileHandlerStep.FileHandlingType> canHandleFile(
			StepExecutionDetails<PT, VoidModel> theStepExecutionDetails,
			ITerminologyImportFileHandlerStep<PT, ?, ?> theFileHandler,
			String theFileName) {

		Properties jobProperties = getJobProperties(myJobPersistence, theStepExecutionDetails);

		for (BaseImportTerminologyFileCsvStep.LoincFileNameSpecification loincFileNameSpecification :
				theFileHandler.getFilesToProcess(theStepExecutionDetails)) {
			if (loincFileNameSpecification.matchFileName(jobProperties, theFileName)) {
				return Optional.of(loincFileNameSpecification.fileHandlingType());
			}
		}

		return Optional.empty();
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
			String stepId, ITerminologyImportFileHandlerStep.FileHandlingType fileHandlingType) {}
}
