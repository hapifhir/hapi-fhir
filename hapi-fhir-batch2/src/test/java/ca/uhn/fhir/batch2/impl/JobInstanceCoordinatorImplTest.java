package ca.uhn.fhir.batch2.impl;

import ca.uhn.fhir.batch2.impl.JobInstanceCoordinatorImpl;
import ca.uhn.fhir.batch2.model.JobDefinitionParameter;
import ca.uhn.fhir.batch2.model.JobInstanceParameter;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = JobInstanceCoordinatorImplTest.MyConfig.class)
public class JobInstanceCoordinatorImplTest {

	@Test
	public void testValidateParameters_RequiredParamNotPresent() {
		List<JobDefinitionParameter> definitions = Lists.newArrayList(
			new JobDefinitionParameter("foo", "Foo Parameter", JobDefinitionParameter.TypeEnum.STRING, true, false),
			new JobDefinitionParameter("bar", "Bar Parameter", JobDefinitionParameter.TypeEnum.STRING, true, false)
		);

		List<JobInstanceParameter> instances = Lists.newArrayList(
			new JobInstanceParameter().setName("foo").setValue("foo value")
		);

		try {
			JobInstanceCoordinatorImpl.validateParameters(definitions, instances);
			Assertions.fail();
		} catch (InvalidRequestException e) {
			Assertions.assertEquals("Missing required parameter: bar", e.getMessage());
		}
	}

	@Test
	public void testValidateParameters_RequiredParamMissingValue() {
		List<JobDefinitionParameter> definitions = Lists.newArrayList(
			new JobDefinitionParameter("foo", "Foo Parameter", JobDefinitionParameter.TypeEnum.STRING, true, false),
			new JobDefinitionParameter("bar", "Bar Parameter", JobDefinitionParameter.TypeEnum.STRING, true, false)
		);

		List<JobInstanceParameter> instances = Lists.newArrayList(
			new JobInstanceParameter().setName("foo").setValue("foo value"),
			new JobInstanceParameter().setName("bar")
		);

		try {
			JobInstanceCoordinatorImpl.validateParameters(definitions, instances);
			Assertions.fail();
		} catch (InvalidRequestException e) {
			Assertions.assertEquals("Missing required parameter: bar", e.getMessage());
		}
	}

	@Test
	public void testValidateParameters_InvalidRepeatingParameter() {
		List<JobDefinitionParameter> definitions = Lists.newArrayList(
			new JobDefinitionParameter("foo", "Foo Parameter", JobDefinitionParameter.TypeEnum.STRING, true, true)
		);

		List<JobInstanceParameter> instances = Lists.newArrayList(
			new JobInstanceParameter().setName("foo").setValue("foo value"),
			new JobInstanceParameter().setName("foo").setValue("foo value 2")
		);

		try {
			JobInstanceCoordinatorImpl.validateParameters(definitions, instances);
			Assertions.fail();
		} catch (InvalidRequestException e) {
			Assertions.assertEquals("Illegal repeating parameter: foo", e.getMessage());
		}
	}

	@Test
	public void testValidateParameters_OptionalParameterMissing() {
		List<JobDefinitionParameter> definitions = Lists.newArrayList(
			new JobDefinitionParameter("foo", "Foo Parameter", JobDefinitionParameter.TypeEnum.STRING, false, true),
			new JobDefinitionParameter("bar", "Bar Parameter", JobDefinitionParameter.TypeEnum.STRING, false, false)
		);

		List<JobInstanceParameter> instances = Lists.newArrayList();

		Assertions.assertDoesNotThrow(() -> JobInstanceCoordinatorImpl.validateParameters(definitions, instances));
	}

	@Test
	public void testValidateParameters_UnexpectedParameter() {
		List<JobDefinitionParameter> definitions = Lists.newArrayList();

		List<JobInstanceParameter> instances = Lists.newArrayList(
			new JobInstanceParameter().setName("foo").setValue("foo value")
		);

		try {
			JobInstanceCoordinatorImpl.validateParameters(definitions, instances);
			Assertions.fail();
		} catch (InvalidRequestException e) {
			Assertions.assertEquals("Unexpected parameter: foo", e.getMessage());
		}
	}

	//	public static class MyConfig extends BaseBatch2AppCtx {
//
//
//
//		@Override
//		public IChannelProducer batchProcessingChannelProducer() {
//			return new LinkedBlockingChannel("batch2", Executors.newSingleThreadExecutor(), new BlockingLin);
//		}
//
//	}
}
