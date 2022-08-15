package ca.uhn.fhir.jpa.partition;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
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

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
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
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
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

	@Nonnull
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
			assertEquals("HTTP 400 Bad Request: " + Msg.code(1314) + "No Partition ID supplied", e.getMessage());
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
			assertEquals("HTTP 400 Bad Request: " + Msg.code(1314) + "No Partition ID supplied", e.getMessage());
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
			assertEquals("HTTP 400 Bad Request: " + Msg.code(1314) + "No Partition ID supplied", e.getMessage());
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
			assertEquals("HTTP 400 Bad Request: " + Msg.code(1314) + "No Partition ID supplied", e.getMessage());
		}
		verify(myPartitionConfigSvc, times(0)).createPartition(any());
	}

	@Test
	public void testListPartitions() {
		PartitionEntity partition1 = new PartitionEntity();
		partition1.setId(1);
		partition1.setName("PARTITION-1");
		partition1.setDescription("a description1");

		PartitionEntity partition2 = new PartitionEntity();
		partition2.setId(2);
		partition2.setName("PARTITION-2");
		partition2.setDescription("a description2");

		List<PartitionEntity> partitionList = new ArrayList<PartitionEntity>();
		partitionList.add(partition1);
		partitionList.add(partition2);
		when(myPartitionConfigSvc.listPartitions()).thenReturn(partitionList);

		Parameters response = myClient
			.operation()
			.onServer()
			.named(ProviderConstants.PARTITION_MANAGEMENT_LIST_PARTITIONS)
			.withNoParameters(Parameters.class)
			.useHttpGet()
			.encodedXml()
			.execute();

		ourLog.info("Response:\n{}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(response));
		verify(myPartitionConfigSvc, times(1)).listPartitions();
		verifyNoMoreInteractions(myPartitionConfigSvc);

		List<Parameters.ParametersParameterComponent> list = getParametersByName(response, "partition");
		assertThat(list, hasSize(2));
		List<Parameters.ParametersParameterComponent> part = list.get(0).getPart();
		assertThat(part.get(0).getName(), is(ProviderConstants.PARTITION_MANAGEMENT_PARTITION_ID));
		assertEquals(1, ((IntegerType) part.get(0).getValue()).getValue().intValue());
		assertThat(part.get(1).getName(), is(ProviderConstants.PARTITION_MANAGEMENT_PARTITION_NAME));
		assertEquals("PARTITION-1", part.get(1).getValue().toString());
		assertThat(part.get(2).getName(), is(ProviderConstants.PARTITION_MANAGEMENT_PARTITION_DESC));
		assertEquals("a description1", part.get(2).getValue().toString());

		part = list.get(1).getPart();
		assertThat(part.get(0).getName(), is(ProviderConstants.PARTITION_MANAGEMENT_PARTITION_ID));
		assertEquals(2, ((IntegerType) part.get(0).getValue()).getValue().intValue());
		assertThat(part.get(1).getName(), is(ProviderConstants.PARTITION_MANAGEMENT_PARTITION_NAME));
		assertEquals("PARTITION-2", part.get(1).getValue().toString());
		assertThat(part.get(2).getName(), is(ProviderConstants.PARTITION_MANAGEMENT_PARTITION_DESC));
		assertEquals("a description2", part.get(2).getValue().toString());
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

	@Nonnull
	private static Answer<Object> createAnswer() {
		return t -> t.getArgument(0, PartitionEntity.class);
	}

	private List<Parameters.ParametersParameterComponent> getParametersByName(Parameters theParams, String theName) {
		return theParams.getParameter().stream().filter(p -> p.getName().equals(theName)).collect(Collectors.toList());
	}
}
