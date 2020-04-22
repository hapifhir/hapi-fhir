package ca.uhn.fhir.jpa.partition;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.model.util.ProviderConstants;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.test.utilities.server.RestfulServerRule;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PartitionManagementProviderTest.MyConfig.class)
public class PartitionManagementProviderTest {

	private static final Logger ourLog = LoggerFactory.getLogger(PartitionManagementProviderTest.class);
	private static FhirContext ourCtx = FhirContext.forR4();
	@ClassRule
	public static RestfulServerRule ourServerRule = new RestfulServerRule(ourCtx);
	@MockBean
	private IPartitionLookupSvc myPartitionConfigSvc;
	@Autowired
	private PartitionManagementProvider myPartitionManagementProvider;
	private IGenericClient myClient;

	@Before
	public void before() {
		ourServerRule.getRestfulServer().registerProvider(myPartitionManagementProvider);
		myClient = ourServerRule.getFhirClient();
		myClient.registerInterceptor(new LoggingInterceptor(false));
	}

	@After
	public void after() {
		ourServerRule.getRestfulServer().unregisterProvider(myPartitionManagementProvider);
	}

	@Test
	public void testAddPartition() {
		when(myPartitionConfigSvc.createPartition(any())).thenAnswer(createAnswer());

		Parameters input = new Parameters();
		input.addParameter(ProviderConstants.PARTITION_MANAGEMENT_PARTITION_ID, new IntegerType(123));
		input.addParameter(ProviderConstants.PARTITION_MANAGEMENT_PARTITION_NAME, new CodeType("PARTITION-123"));
		input.addParameter(ProviderConstants.PARTITION_MANAGEMENT_PARTITION_DESC, new StringType("a description"));
		ourLog.info("Input:\n{}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input));

		Parameters response = myClient
			.operation()
			.onServer()
			.named(ProviderConstants.PARTITION_MANAGEMENT_ADD_PARTITION)
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

	@Test
	public void testUpdatePartition() {
		when(myPartitionConfigSvc.updatePartition(any())).thenAnswer(createAnswer());

		Parameters input = new Parameters();
		input.addParameter(ProviderConstants.PARTITION_MANAGEMENT_PARTITION_ID, new IntegerType(123));
		input.addParameter(ProviderConstants.PARTITION_MANAGEMENT_PARTITION_NAME, new CodeType("PARTITION-123"));
		input.addParameter(ProviderConstants.PARTITION_MANAGEMENT_PARTITION_DESC, new StringType("a description"));
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
		return t -> {
			return t.getArgument(0, PartitionEntity.class);
		};
	}

}
