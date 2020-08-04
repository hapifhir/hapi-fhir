package ca.uhn.fhir.jpa.partition;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PartitionManagementProviderTest.MyConfig.class)
public class PartitionManagementProviderTest {

	private static final Logger ourLog = LoggerFactory.getLogger(PartitionManagementProviderTest.class);
	private static FhirContext ourCtx = FhirContext.forCached(FhirVersionEnum.R4);
	@RegisterExtension
	public static RestfulServerExtension ourServerRule = new RestfulServerExtension(ourCtx);
	@MockBean
	private IPartitionLookupSvc myPartitionConfigSvc;
	@Autowired
	private PartitionManagementProvider myPartitionManagementProvider;
	private IGenericClient myClient;

	@BeforeEach
	public void before() {
		ourServerRule.getRestfulServer().registerProvider(myPartitionManagementProvider);
		myClient = ourServerRule.getFhirClient();
		myClient.registerInterceptor(new LoggingInterceptor(false));
	}

	@AfterEach
	public void after() {
		ourServerRule.getRestfulServer().unregisterProvider(myPartitionManagementProvider);
	}

	@Test
	public void testCreatePartition() {
		when(myPartitionConfigSvc.createPartition(any())).thenAnswer(createAnswer());

		Parameters input = createInputPartition();
		ourLog.info("Input:\n{}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input));

		Parameters response = myClient
			.operation()
			.onServer()
			.named(ProviderConstants.PARTITION_MANAGEMENT_CREATE_PARTITION)
			.withParameters(input)
			.encodedXml()
			.execute();

		ourLog.info("Response:\n{}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(response));
		verify(myPartitionConfigSvc, times(1)).createPartition(any());
		verifyNoMoreInteractions(myPartitionConfigSvc);

		assertEquals(123, ((IntegerType) response.getParameter(ProviderConstants.PARTITION_MANAGEMENT_PARTITION_ID)).getValue().intValue());
		assertEquals("PARTITION-123", ((StringType) response.getParameter(ProviderConstants.PARTITION_MANAGEMENT_PARTITION_NAME)).getValue());
		assertEquals("a description", ((StringType) response.getParameter(ProviderConstants.PARTITION_MANAGEMENT_PARTITION_DESC)).getValue());
	}

	@NotNull
	private Parameters createInputPartition() {
		Parameters input = new Parameters();
		input.addParameter(ProviderConstants.PARTITION_MANAGEMENT_PARTITION_ID, new IntegerType(123));
		input.addParameter(ProviderConstants.PARTITION_MANAGEMENT_PARTITION_NAME, new CodeType("PARTITION-123"));
		input.addParameter(ProviderConstants.PARTITION_MANAGEMENT_PARTITION_DESC, new StringType("a description"));
		return input;
	}

	@Test
	public void testCreatePartition_InvalidInput() {
		try {
			myClient
				.operation()
				.onServer()
				.named(ProviderConstants.PARTITION_MANAGEMENT_CREATE_PARTITION)
				.withNoParameters(Parameters.class)
				.encodedXml()
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: No Partition ID supplied", e.getMessage());
		}
		verify(myPartitionConfigSvc, times(0)).createPartition(any());
	}

	@Test
	public void testReadPartition() {
		PartitionEntity partition = new PartitionEntity();
		partition.setId(123);
		partition.setName("PARTITION-123");
		partition.setDescription("a description");
		when(myPartitionConfigSvc.getPartitionById(eq(123))).thenReturn(partition);

		Parameters response = myClient
			.operation()
			.onServer()
			.named(ProviderConstants.PARTITION_MANAGEMENT_READ_PARTITION)
			.withParameter(Parameters.class, ProviderConstants.PARTITION_MANAGEMENT_PARTITION_ID, new IntegerType(123))
			.useHttpGet()
			.encodedXml()
			.execute();

		ourLog.info("Response:\n{}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(response));
		verify(myPartitionConfigSvc, times(1)).getPartitionById(any());
		verifyNoMoreInteractions(myPartitionConfigSvc);

		assertEquals(123, ((IntegerType) response.getParameter(ProviderConstants.PARTITION_MANAGEMENT_PARTITION_ID)).getValue().intValue());
		assertEquals("PARTITION-123", ((StringType) response.getParameter(ProviderConstants.PARTITION_MANAGEMENT_PARTITION_NAME)).getValue());
		assertEquals("a description", ((StringType) response.getParameter(ProviderConstants.PARTITION_MANAGEMENT_PARTITION_DESC)).getValue());
	}

	@Test
	public void testReadPartition_InvalidInput() {
		try {
			myClient
				.operation()
				.onServer()
				.named(ProviderConstants.PARTITION_MANAGEMENT_READ_PARTITION)
				.withNoParameters(Parameters.class)
				.encodedXml()
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: No Partition ID supplied", e.getMessage());
		}
		verify(myPartitionConfigSvc, times(0)).getPartitionById(any());
	}

	@Test
	public void testUpdatePartition() {
		when(myPartitionConfigSvc.updatePartition(any())).thenAnswer(createAnswer());

		Parameters input = createInputPartition();
		ourLog.info("Input:\n{}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input));

		Parameters response = myClient
			.operation()
			.onServer()
			.named(ProviderConstants.PARTITION_MANAGEMENT_UPDATE_PARTITION)
			.withParameters(input)
			.encodedXml()
			.execute();

		ourLog.info("Response:\n{}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(response));
		verify(myPartitionConfigSvc, times(1)).updatePartition(any());
		verifyNoMoreInteractions(myPartitionConfigSvc);

		assertEquals(123, ((IntegerType) response.getParameter(ProviderConstants.PARTITION_MANAGEMENT_PARTITION_ID)).getValue().intValue());
		assertEquals("PARTITION-123", ((StringType) response.getParameter(ProviderConstants.PARTITION_MANAGEMENT_PARTITION_NAME)).getValue());
		assertEquals("a description", ((StringType) response.getParameter(ProviderConstants.PARTITION_MANAGEMENT_PARTITION_DESC)).getValue());
	}

	@Test
	public void testUpdatePartition_InvalidInput() {
		try {
			myClient
				.operation()
				.onServer()
				.named(ProviderConstants.PARTITION_MANAGEMENT_UPDATE_PARTITION)
				.withNoParameters(Parameters.class)
				.encodedXml()
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: No Partition ID supplied", e.getMessage());
		}
		verify(myPartitionConfigSvc, times(0)).createPartition(any());
	}

	@Test
	public void testDeletePartition() {
		Parameters input = new Parameters();
		input.addParameter(ProviderConstants.PARTITION_MANAGEMENT_PARTITION_ID, new IntegerType(123));
		ourLog.info("Input:\n{}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input));

		Parameters response = myClient
			.operation()
			.onServer()
			.named(ProviderConstants.PARTITION_MANAGEMENT_DELETE_PARTITION)
			.withParameters(input)
			.encodedXml()
			.execute();

		ourLog.info("Response:\n{}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(response));
		verify(myPartitionConfigSvc, times(1)).deletePartition(eq(123));
		verifyNoMoreInteractions(myPartitionConfigSvc);
	}

	@Test
	public void testDeletePartition_InvalidInput() {
		try {
			myClient
				.operation()
				.onServer()
				.named(ProviderConstants.PARTITION_MANAGEMENT_CREATE_PARTITION)
				.withNoParameters(Parameters.class)
				.encodedXml()
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: No Partition ID supplied", e.getMessage());
		}
		verify(myPartitionConfigSvc, times(0)).createPartition(any());
	}

	@Configuration
	public static class MyConfig {

		@Bean
		public PartitionManagementProvider partitionManagementProvider() {
			return new PartitionManagementProvider();
		}

		@Bean
		public FhirContext fhirContext() {
			return ourCtx;
		}

	}

	@NotNull
	private static Answer<Object> createAnswer() {
		return t -> t.getArgument(0, PartitionEntity.class);
	}

}
