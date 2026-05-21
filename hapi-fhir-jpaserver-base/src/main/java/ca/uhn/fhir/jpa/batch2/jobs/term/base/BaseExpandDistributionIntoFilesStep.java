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
import ca.uhn.fhir.jpa.term.LoadedFileDescriptors;
import ca.uhn.fhir.jpa.util.CsvUtil;
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
import java.util.List;

import static org.apache.commons.lang3.ObjectUtils.getIfNull;

public abstract class BaseExpandDistributionIntoFilesStep<PT extends BaseTerminologyImportParameters, OT extends TerminologyFileSetJson> implements IJobStepWorker<PT, VoidModel, OT> {

	private static final Logger ourLog = LoggerFactory.getLogger(BaseExpandDistributionIntoFilesStep.class);

	@Autowired
	private IJobPersistence myJobPersistence;

	private Integer myChunkLineSizeForUnitTests = null;

	@VisibleForTesting
	public void setChunkLineSizeForUnitTest(int theChunkLineSize) {
		myChunkLineSizeForUnitTests = theChunkLineSize;
	}

	@SuppressWarnings("SwitchStatementWithTooFewBranches")
	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<PT, VoidModel> theStepExecutionDetails, @Nonnull IJobDataSink<OT> theDataSink) throws JobExecutionFailedException {

		String instanceId = theStepExecutionDetails.getInstance().getInstanceId();
		PT jobParameters = theStepExecutionDetails.getParameters();
		AttachmentDetails loincFileAttachment = myJobPersistence.fetchAttachmentByFilename(instanceId, TerminologyConstants.FILENAME_LOINC_DISTRIBUTION_FILE);

		ourLog.info("Import {}[{}] - Expanding file {}", getTerminologyName(), instanceId, loincFileAttachment.getFilename());

		OT fileSet = newTerminologyFileSetJson();

		try (InputStream inputStream = loincFileAttachment.getInputStream()) {
			try (BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
				ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(bufferedInputStream);
				ZipArchiveEntry entry;
				while ((entry = zipInputStream.getNextEntry()) != null) {
					try (BOMInputStream fis = new LoadedFileDescriptors.NonClosableBOMInputStream(zipInputStream)) {
						String nextFileName = entry.getName();
						byte[] bytes = IOUtils.toByteArray(fis);

						// Asynchronous processing (prepare work chunks based on input file)
						List<StepIdAndFileHandlingInstructions> processors = getStepIdAndFileHandlingInstructionsForFileName(theStepExecutionDetails, jobParameters, theStepExecutionDetails.getJobDefinition(), nextFileName);

						ListMultimap<ITerminologyImportFileHandlerStep.FileHandlingType, String> fileHandlingTypeToAttachmentIds = MultimapBuilder.hashKeys().arrayListValues().build();

						for (StepIdAndFileHandlingInstructions processor : processors) {
							ITerminologyImportFileHandlerStep.FileHandlingType fileHandlingType = processor.fileHandlingInstructions().fileHandlingType();

							if (fileHandlingTypeToAttachmentIds.containsKey(fileHandlingType)) {
								List<String> attachmentIds = fileHandlingTypeToAttachmentIds.get(fileHandlingType);
								for (String attachmentId : attachmentIds) {
									fileSet.addChunk(processor.stepId(), nextFileName, attachmentId);
								}
								continue;
							}

							List<String> attachmentIds = switch (fileHandlingType) {
								case CSV_SPLIT_WITH_REPEAT_HEADER_1000_LINE_CHUNKS -> {
									int chunkSize = getIfNull(myChunkLineSizeForUnitTests, 1000);
									yield csvSplitWithRepeatHeader(instanceId, bytes, fileSet, loincFileAttachment.getFilename(), nextFileName, processor.stepId(), chunkSize);
								}
								case CSV_SPLIT_WITH_REPEAT_HEADER_50000_LINE_CHUNKS -> {
									int chunkSize = getIfNull(myChunkLineSizeForUnitTests, 50000);
									yield csvSplitWithRepeatHeader(instanceId, bytes, fileSet, loincFileAttachment.getFilename(), nextFileName, processor.stepId(), chunkSize);
								}
							};
							fileHandlingTypeToAttachmentIds.putAll(fileHandlingType, attachmentIds);
						}

						// Synchronous processing (anything that is small enough to just handle it here)
						handleSynchronous(theStepExecutionDetails, nextFileName, bytes, jobParameters, fileSet);

					}
				}
			}
		} catch (IOException e) {
			// FIXME: add code
			throw new JobExecutionFailedException(Msg.code(1) + "Files to expand " + getTerminologyName() + " zip file: " + e.getMessage(), e);
		}

		submitChunksForNextStep(theStepExecutionDetails, theDataSink, fileSet);

		return RunOutcome.SUCCESS;
	}

	/**
	 * Subclasses can override this method to handle files that are small enough to just handle here.
	 */
	protected void handleSynchronous(StepExecutionDetails<PT, VoidModel> theStepExecutionDetails, String theFileName, byte[] theBytes, PT theJobParameters, OT theFileSet) {
		// nothing
	}

	/**
	 * A simple factory method for a new empty file set.
	 */
	protected abstract OT newTerminologyFileSetJson();

	/**
	 * @return A list of attachment IDs for the work chunks that were created.
	 */
	private List<String> csvSplitWithRepeatHeader(String theJobInstanceId, byte[] theBytes, TerminologyFileSetJson theFileSet, String theZipOuterFilename, String theZipInnerFilename, String theStepId, int theChunkLines) throws JobExecutionFailedException {
		List<String> attachmentIds = new ArrayList<>();

		String filename = theZipInnerFilename;
		int lastSlash = Math.max(filename.lastIndexOf('/'), filename.lastIndexOf('\\'));
		if (lastSlash != -1) {
			filename = filename.substring(lastSlash + 1);
		}

		ByteArrayInputStream bis = new ByteArrayInputStream(theBytes);
		InputStreamReader reader = new InputStreamReader(bis, StandardCharsets.UTF_8);
		try (CSVParser parser = newLoincCsvParser(reader)) {

			List<String> headers = parser.getHeaderNames();
			Iterator<CSVRecord> recordIterator = parser.getRecords().iterator();
			List<CSVRecord> recordsBuffer = new ArrayList<>();
			int startRow = 0;
			int endRow = 0;
			while (recordIterator.hasNext()) {

				CSVRecord nextRecord = recordIterator.next();
				recordsBuffer.add(nextRecord);

				if (recordsBuffer.size() == theChunkLines || !recordIterator.hasNext()) {
					String attachmentId = writeChunk(theJobInstanceId, headers, recordsBuffer, filename, startRow, endRow);
					theFileSet.addChunk(theStepId, theZipInnerFilename, attachmentId);
					recordsBuffer.clear();
					attachmentIds.add(attachmentId);

					startRow = endRow + 1;
					endRow = startRow;
				} else {
					endRow++;
				}
			}

		} catch (IOException e) {
			// FIXME: add code
			throw new JobExecutionFailedException(Msg.code(1) + "Failed to parse " + theZipInnerFilename + " in zip file: " + theZipOuterFilename + ": " + e, e);
		}

		return attachmentIds;
	}

	private String writeChunk(String theJobInstanceId, List<String> theHeaders, List<CSVRecord> theRecordsBuffer, String theFilename, int theStartRow, int theEndRow) {
		CsvUtil.ICsvProducer producer = thePrinter -> {
			for (CSVRecord record : theRecordsBuffer) {
				thePrinter.printRecord(record);
			}
		};
		byte[] bytes = CsvUtil.writeCsvToByteArray(theHeaders.toArray(new String[0]), producer);
		AttachmentDetails attachmentDetails = AttachmentDetails
			.build()
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

	private List<StepIdAndFileHandlingInstructions> getStepIdAndFileHandlingInstructionsForFileName(StepExecutionDetails<PT, VoidModel> theStepExecutionDetails, PT theJobParameters, JobDefinition<PT> theJobDefinition, String theStepId) {
		List<StepIdAndFileHandlingInstructions> stepProcessingInstructions = new ArrayList<>();

		for (JobDefinitionStep<PT, ?, ?> step : theJobDefinition.getSteps()) {
			if (step.getJobStepWorker() instanceof ITerminologyImportFileHandlerStep<PT, ?, ?> fileHandler) {
				fileHandler
					.canHandleFile(theStepExecutionDetails, theJobParameters, theStepId)
					.ifPresent(instructions -> stepProcessingInstructions.add(new StepIdAndFileHandlingInstructions(step.getStepId(), instructions)));
			}
		}

		return stepProcessingInstructions;
	}

	public static <OT extends TerminologyFileSetJson> void submitChunksForNextStep(@Nonnull StepExecutionDetails<?, ?> theStepExecutionDetails, @Nonnull IJobDataSink<OT> theDataSink, OT theFileSet) {
		String nextStepId = theStepExecutionDetails.getNextStepId();

		/*
		 * The chunk data holds the attachment IDs for all processing steps that have not yet been completed,
		 * including the next step, but also including subsequent steps. We want to send one chunk to the next
		 * step that includes all the data for subsequent steps so that it can be relayed onward, but then
		 * all other work chunks should only hold a single attachment ID for actual processing.
		 */
		List<TerminologyFileSetJson.Chunk> attachmentIdsForNextStep = theFileSet.getAndRemoveFutureChunksForStepId(nextStepId);
		theFileSet.setChunkForCurrentStep(null);
		if (!theFileSet.isEmpty()) {
			theDataSink.accept(theFileSet);
		}

		// Subsequent steps
		for (TerminologyFileSetJson.Chunk chunk : attachmentIdsForNextStep) {
			OT fileSetToSend = theFileSet.cloneWithOnlyCopyForwardData();
			fileSetToSend.setChunkForCurrentStep(chunk);
			theDataSink.accept(fileSetToSend);
		}
	}

	@Nonnull
	public static CSVParser newLoincCsvParser(Reader theReader) throws IOException {
		return new CSVParser(theReader, CSVFormat.DEFAULT.builder()
			.setDelimiter(',')
			.setEscape(null)
			.setIgnoreEmptyLines(true)
			.setQuote('"')
			.setRecordSeparator('\n')
			.setNullString("")
			.setQuoteMode(QuoteMode.NON_NUMERIC)
			.setHeader()
			.setSkipHeaderRecord(true)
			.build());
	}

	private record StepIdAndFileHandlingInstructions(String stepId,
	                                                 ITerminologyImportFileHandlerStep.FileHandlingInstructions fileHandlingInstructions) {
	}

}
