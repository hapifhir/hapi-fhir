package ca.uhn.fhir.batch2.jobs.export.svcs;

import ca.uhn.fhir.batch2.api.IJobStepExecutionServices;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.export.models.ResourceIdList;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkExportProcessor;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryResourceMatcher;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportResourceList;
import ca.uhn.fhir.rest.api.server.bulk.ConvertedFile;
import ca.uhn.fhir.rest.api.server.bulk.ConvertedFiles;
import ca.uhn.fhir.rest.api.server.bulk.IResourceConverter;
import ca.uhn.fhir.rest.server.interceptor.ResponseTerminologyTranslationSvc;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

// Created by Claude Opus 5
public class ExpandResourcesConsumerTest {

	private static final String CUSTOM_MIME_TYPE = "text/custom";

	private final FhirContext myFhirContext = FhirContext.forR4Cached();

	private JpaStorageSettings myStorageSettings;

	private InterceptorService myInterceptorService;

	private IBulkExportProcessor<?> myBulkExportProcessor;

	private InMemoryResourceMatcher myInMemoryResourceMatcher;

	private BinaryCreator myBinaryCreator;

	private ResponseTerminologyTranslationSvc myTerminologyTranslationSvc;

	private BulkExportJobParameters myParameters;

	@BeforeEach
	public void before() {
		myStorageSettings = new JpaStorageSettings();
		myInterceptorService = new InterceptorService();
		myBulkExportProcessor = mock(IBulkExportProcessor.class);
		myInMemoryResourceMatcher = mock(InMemoryResourceMatcher.class);
		myBinaryCreator = mock(BinaryCreator.class);
		myTerminologyTranslationSvc = null;

		myParameters = new BulkExportJobParameters();
		myParameters.setOutputFormat(Constants.CT_FHIR_NDJSON);
	}

	// ---------------------------------------------------------------------
	// Terminology normalization
	// ---------------------------------------------------------------------

	/**
	 * {@link ResponseTerminologyTranslationSvc} is injected with {@code required = false}, because
	 * the bean is only declared by the JPA server config, and this module can be wired into a
	 * context that does not provide it. When terminology normalization is switched on in such a
	 * context we must fail loudly rather than silently export un-normalized resources.
	 */
	@Test
	void accept_normalizeTerminologyEnabledButNoTranslationSvc_throws() {
		// setup
		myStorageSettings.setNormalizeTerminologyForBulkExportJobs(true);
		ExpandResourcesConsumer consumer = createConsumer();

		// execute + validate
		assertThatThrownBy(() -> consumer.accept(createResources("A")))
				.isInstanceOf(JobExecutionFailedException.class)
				.hasMessageContaining("HAPI-3053")
				.hasMessageContaining("ResponseTerminologyTranslationSvc");
	}

	/**
	 * The absent bean is only an error when the feature that needs it is enabled - the default
	 * configuration must keep working without it.
	 */
	@Test
	void accept_normalizeTerminologyDisabledAndNoTranslationSvc_doesNotThrow() {
		// setup
		myStorageSettings.setNormalizeTerminologyForBulkExportJobs(false);
		ExpandResourcesConsumer consumer = createConsumer();

		// execute
		consumer.accept(new ArrayList<>());

		// validate
		assertThat(consumer.getConsumedResourceCount()).isZero();
	}

	@Test
	void accept_normalizeTerminologyEnabledWithTranslationSvc_translatesResources() {
		// setup
		myStorageSettings.setNormalizeTerminologyForBulkExportJobs(true);
		myTerminologyTranslationSvc = mock(ResponseTerminologyTranslationSvc.class);
		registerConverter(new RecordingConverter(convertedFiles("Patient")));
		ExpandResourcesConsumer consumer = createConsumer();

		List<IBaseResource> resources = createResources("A");

		// execute
		consumer.accept(resources);

		// validate
		verify(myTerminologyTranslationSvc).processResourcesForTerminologyTranslation(resources);
	}

	// ---------------------------------------------------------------------
	// Converter selection
	// ---------------------------------------------------------------------

	@Test
	void accept_unsupportedOutputFormatAndNoConverterHook_throws() {
		// setup
		myParameters.setOutputFormat(CUSTOM_MIME_TYPE);
		ExpandResourcesConsumer consumer = createConsumer();

		// execute + validate
		assertThatThrownBy(() -> consumer.accept(createResources("A")))
				.isInstanceOf(JobExecutionFailedException.class)
				.hasMessageContaining("HAPI-3051")
				.hasMessageContaining(CUSTOM_MIME_TYPE);

		verifyNoInteractions(myBinaryCreator);
	}

	@Test
	void accept_noOutputFormatAndNoConverterHook_fallsBackToNdJson() {
		// setup
		myParameters.setOutputFormat(null);
		ExpandResourcesConsumer consumer = createConsumer();

		// execute
		consumer.accept(createResources("A"));

		// validate
		ConvertedFile file = captureSingleWrittenFile();
		assertThat(file.getResourceType()).isEqualTo("Patient");
		assertThat(file.getMimeType()).isEqualTo(Constants.CT_FHIR_NDJSON);
		assertThat(new String(file.getBytes(), StandardCharsets.UTF_8))
				.contains("\"resourceType\":\"Patient\"")
				.endsWith("\n");
	}

	/**
	 * A registered converter is consulted before the built-in NDJSON converter, so an
	 * implementer can replace the default output format as well as add new ones.
	 */
	@Test
	void accept_converterHookRegistered_takesPrecedenceOverBuiltInNdJsonConverter() {
		// setup
		myParameters.setOutputFormat(Constants.CT_FHIR_NDJSON);
		registerConverter(new RecordingConverter(convertedFiles("Patient")));
		ExpandResourcesConsumer consumer = createConsumer();

		// execute
		consumer.accept(createResources("A"));

		// validate
		ConvertedFile file = captureSingleWrittenFile();
		assertThat(file.getMimeType()).isEqualTo(CUSTOM_MIME_TYPE);
	}

	@Test
	void accept_converterReturnsMultipleFiles_writesOneBinaryPerFile() {
		// setup
		registerConverter(new RecordingConverter(convertedFiles("Patient", "Observation")));
		ExpandResourcesConsumer consumer = createConsumer();

		// execute
		consumer.accept(createResources("A"));

		// validate
		ArgumentCaptor<ConvertedFile> captor = ArgumentCaptor.forClass(ConvertedFile.class);
		verify(myBinaryCreator, times(2)).accept(captor.capture());
		assertThat(captor.getAllValues())
				.extracting(ConvertedFile::getResourceType)
				.containsExactly("Patient", "Observation");
	}

	// ---------------------------------------------------------------------
	// Converter output validation
	// ---------------------------------------------------------------------

	static Stream<Arguments> invalidConverterOutputs() {
		ConvertedFile blankResourceType = convertedFile(null, CUSTOM_MIME_TYPE, new byte[] {1});
		ConvertedFile blankMimeType = convertedFile("Patient", null, new byte[] {1});
		ConvertedFile noBytes = convertedFile("Patient", CUSTOM_MIME_TYPE, null);

		return Stream.of(
				Arguments.of("null output", null),
				Arguments.of("no files", new ConvertedFiles()),
				Arguments.of("blank resource type", new ConvertedFiles().addFile(blankResourceType)),
				Arguments.of("blank mime type", new ConvertedFiles().addFile(blankMimeType)),
				Arguments.of("no bytes", new ConvertedFiles().addFile(noBytes)));
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("invalidConverterOutputs")
	void accept_converterReturnsInvalidOutput_throws(String theName, ConvertedFiles theOutput) {
		// setup
		registerConverter(new RecordingConverter(theOutput));
		ExpandResourcesConsumer consumer = createConsumer();

		// execute + validate
		assertThatThrownBy(() -> consumer.accept(createResources("A")))
				.isInstanceOf(JobExecutionFailedException.class)
				.hasMessageContaining("HAPI-3052");

		verifyNoInteractions(myBinaryCreator);
	}

	// ---------------------------------------------------------------------
	// Resource inclusion hook
	// ---------------------------------------------------------------------

	@Test
	void accept_inclusionHookRemovesSomeResources_convertsOnlyTheRemainder() {
		// setup
		myInterceptorService.registerInterceptor(new InclusionInterceptor("Patient/A"));
		RecordingConverter converter = new RecordingConverter(convertedFiles("Patient"));
		registerConverter(converter);
		ExpandResourcesConsumer consumer = createConsumer();

		// execute
		consumer.accept(createResources("A", "B"));

		// validate
		assertThat(converter.getReceivedResources())
				.extracting(r -> r.getIdElement().getValue())
				.containsExactly("Patient/B");
		assertThat(consumer.getConsumedResourceCount()).isEqualTo(1);
	}

	@Test
	void accept_inclusionHookRemovesAllResources_doesNotConvertOrWriteBinary() {
		// setup
		myInterceptorService.registerInterceptor(new InclusionInterceptor("Patient/A", "Patient/B"));
		RecordingConverter converter = new RecordingConverter(convertedFiles("Patient"));
		registerConverter(converter);
		ExpandResourcesConsumer consumer = createConsumer();

		// execute
		consumer.accept(createResources("A", "B"));

		// validate
		assertThat(converter.getInvocationCount()).isZero();
		assertThat(consumer.getConsumedResourceCount()).isZero();
		verifyNoInteractions(myBinaryCreator);
	}

	// ---------------------------------------------------------------------
	// Post-fetch filtering
	// ---------------------------------------------------------------------

	@Test
	void accept_postFetchFilterUrlProvided_removesNonMatchingResources() {
		// setup
		myParameters.setPostFetchFilterUrls(List.of("Patient?active=true"));
		when(myInMemoryResourceMatcher.match(anyString(), any(), any(), any()))
				.thenReturn(InMemoryMatchResult.successfulMatch(), InMemoryMatchResult.noMatch());

		RecordingConverter converter = new RecordingConverter(convertedFiles("Patient"));
		registerConverter(converter);
		ExpandResourcesConsumer consumer = createConsumer();

		// execute
		consumer.accept(createResources("A", "B"));

		// validate
		assertThat(converter.getReceivedResources())
				.extracting(r -> r.getIdElement().getValue())
				.containsExactly("Patient/A");
		assertThat(consumer.getConsumedResourceCount()).isEqualTo(1);
	}

	@Test
	void accept_postFetchFilterUrlForOtherResourceType_isIgnored() {
		// setup
		myParameters.setPostFetchFilterUrls(List.of("Observation?status=final"));
		RecordingConverter converter = new RecordingConverter(convertedFiles("Patient"));
		registerConverter(converter);
		ExpandResourcesConsumer consumer = createConsumer();

		// execute
		consumer.accept(createResources("A"));

		// validate
		assertThat(converter.getReceivedResources()).hasSize(1);
		verifyNoInteractions(myInMemoryResourceMatcher);
	}

	// ---------------------------------------------------------------------
	// MDM expansion
	// ---------------------------------------------------------------------

	@Test
	void accept_expandMdmEnabled_expandsResources() {
		// setup
		registerConverter(new RecordingConverter(convertedFiles("Patient")));
		ExpandResourcesConsumer consumer = createConsumer();
		consumer.setDoExpandMDM(true);

		List<IBaseResource> resources = createResources("A");

		// execute
		consumer.accept(resources);

		// validate
		verify(myBulkExportProcessor).expandMdmResources(resources);
	}

	@Test
	void accept_expandMdmDisabled_doesNotExpandResources() {
		// setup
		registerConverter(new RecordingConverter(convertedFiles("Patient")));
		ExpandResourcesConsumer consumer = createConsumer();

		// execute
		consumer.accept(createResources("A"));

		// validate
		verify(myBulkExportProcessor, never()).expandMdmResources(any());
	}

	// ---------------------------------------------------------------------
	// Consumed resource accounting
	// ---------------------------------------------------------------------

	/**
	 * The count drives the step's RunOutcome. It accumulates across batches and deliberately
	 * reflects what was read from the repository rather than what the converter emitted.
	 */
	@Test
	void getConsumedResourceCount_multipleBatches_accumulatesAndIgnoresConverterOutput() {
		// setup
		registerConverter(new RecordingConverter(convertedFiles("Patient", "Patient", "Patient")));
		ExpandResourcesConsumer consumer = createConsumer();

		// execute
		consumer.accept(createResources("A", "B"));
		consumer.accept(createResources("C"));

		// validate
		assertThat(consumer.getConsumedResourceCount()).isEqualTo(3);
	}

	// ---------------------------------------------------------------------
	// Helpers
	// ---------------------------------------------------------------------

	private ExpandResourcesConsumer createConsumer() {
		return new ExpandResourcesConsumer(
				myFhirContext,
				myBulkExportProcessor,
				myInterceptorService,
				myStorageSettings,
				myInMemoryResourceMatcher,
				myTerminologyTranslationSvc,
				myBinaryCreator,
				createStepExecutionDetails());
	}

	private StepExecutionDetails<BulkExportJobParameters, ResourceIdList> createStepExecutionDetails() {
		ResourceIdList idList = new ResourceIdList();
		idList.setResourceType("Patient");
		idList.setIds(new ArrayList<>());

		JobInstance instance = new JobInstance();
		instance.setInstanceId("instance-id");

		return new StepExecutionDetails<>(
				myParameters, idList, instance, new WorkChunk().setId("chunk-id"), mock(IJobStepExecutionServices.class));
	}

	private void registerConverter(IResourceConverter theConverter) {
		myInterceptorService.registerInterceptor(new ConverterInterceptor(theConverter));
	}

	private ConvertedFile captureSingleWrittenFile() {
		ArgumentCaptor<ConvertedFile> captor = ArgumentCaptor.forClass(ConvertedFile.class);
		verify(myBinaryCreator).accept(captor.capture());
		return captor.getValue();
	}

	private List<IBaseResource> createResources(String... theIds) {
		List<IBaseResource> resources = new ArrayList<>();
		for (String id : theIds) {
			resources.add(new Patient().setId("Patient/" + id));
		}
		return resources;
	}

	private static ConvertedFiles convertedFiles(String... theResourceTypes) {
		ConvertedFiles files = new ConvertedFiles();
		for (String resourceType : theResourceTypes) {
			files.addFile(convertedFile(resourceType, CUSTOM_MIME_TYPE, "contents".getBytes(StandardCharsets.UTF_8)));
		}
		return files;
	}

	private static ConvertedFile convertedFile(String theResourceType, String theMimeType, byte[] theBytes) {
		ConvertedFile file = new ConvertedFile();
		file.setResourceType(theResourceType);
		file.setMimeType(theMimeType);
		file.setBytes(theBytes);
		return file;
	}

	/**
	 * Captures what the consumer handed to the converter, and returns a canned response.
	 */
	private static class RecordingConverter implements IResourceConverter {

		private final ConvertedFiles myOutput;

		private List<IBaseResource> myReceivedResources;

		private int myInvocationCount;

		RecordingConverter(ConvertedFiles theOutput) {
			myOutput = theOutput;
		}

		@Nonnull
		@Override
		public ConvertedFiles consume(@Nonnull
				BulkExportResourceList theResources, @Nonnull BulkExportJobParameters theJobParameters) {
			myInvocationCount++;
			myReceivedResources = new ArrayList<>(theResources.getResources());
			return myOutput;
		}

		List<IBaseResource> getReceivedResources() {
			return myReceivedResources;
		}

		int getInvocationCount() {
			return myInvocationCount;
		}
	}

	private static class ConverterInterceptor {

		private final IResourceConverter myConverter;

		ConverterInterceptor(IResourceConverter theConverter) {
			myConverter = theConverter;
		}

		@Hook(Pointcut.STORAGE_BULK_EXPORT_RESOURCE_CONVERT)
		public IResourceConverter selectConverter(BulkExportJobParameters theParameters) {
			return myConverter;
		}
	}

	private static class InclusionInterceptor {

		private final List<String> myExcludedIds;

		InclusionInterceptor(String... theExcludedIds) {
			myExcludedIds = List.of(theExcludedIds);
		}

		@Hook(Pointcut.STORAGE_BULK_EXPORT_RESOURCE_INCLUSION)
		public boolean include(BulkExportJobParameters theParameters, IBaseResource theResource) {
			return !myExcludedIds.contains(theResource.getIdElement().getValue());
		}
	}
}
