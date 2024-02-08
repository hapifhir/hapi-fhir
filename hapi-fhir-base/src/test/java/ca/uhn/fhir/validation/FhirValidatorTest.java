package ca.uhn.fhir.validation;

import ca.uhn.fhir.context.FhirContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class FhirValidatorTest {
	private static final String PREFIX = "Brakebills";
	public static final String MESSAGE = "Fillory";
	@Mock
	FhirContext myFhirContext;

	@Test
	public void testBuildMessagesThreePartLocation() {
		// setup
		List<FhirValidator.ConcurrentValidationTask> tasks = buildTasks("patient.name.first");

		// execute
		List<SingleValidationMessage> resultMessages = FhirValidator.buildValidationMessages(tasks);

		// validate
		assertThat(resultMessages).hasSize(1);
		assertThat(resultMessages.get(0).getMessage()).isEqualTo(MESSAGE);
		assertThat(resultMessages.get(0).getLocationString()).isEqualTo(PREFIX + ".name.first");
	}

	@Test
	public void testBuildMessagesTwoPartLocation() {
		// setup
		List<FhirValidator.ConcurrentValidationTask> tasks = buildTasks("patient.name");

		// execute
		List<SingleValidationMessage> resultMessages = FhirValidator.buildValidationMessages(tasks);

		// validate
		assertThat(resultMessages).hasSize(1);
		assertThat(resultMessages.get(0).getMessage()).isEqualTo(MESSAGE);
		assertThat(resultMessages.get(0).getLocationString()).isEqualTo(PREFIX + ".name");
	}

	@Test
	public void testBuildMessagesNullLocation() {
		// setup
		List<FhirValidator.ConcurrentValidationTask> tasks = buildTasks(null);

		// execute
		List<SingleValidationMessage> resultMessages = FhirValidator.buildValidationMessages(tasks);

		// validate
		assertThat(resultMessages).hasSize(1);
		assertThat(resultMessages.get(0).getMessage()).isEqualTo(MESSAGE);
		assertThat(resultMessages.get(0).getLocationString()).isEqualTo(PREFIX);
	}

	@Test
	public void testBuildMessagesOnePartLocation() {
		// setup
		List<FhirValidator.ConcurrentValidationTask> tasks = buildTasks("patient");

		// execute
		List<SingleValidationMessage> resultMessages = FhirValidator.buildValidationMessages(tasks);

		// validate
		assertThat(resultMessages).hasSize(1);
		assertThat(resultMessages.get(0).getMessage()).isEqualTo(MESSAGE);
		assertThat(resultMessages.get(0).getLocationString()).isEqualTo(PREFIX + ".patient");
	}

	private List<FhirValidator.ConcurrentValidationTask> buildTasks(String theLocation) {
		SingleValidationMessage message = new SingleValidationMessage();
		message.setMessage(MESSAGE);
		message.setLocationString(theLocation);
		ValidationResult result = new ValidationResult(myFhirContext, Collections.singletonList(message));
		CompletableFuture<ValidationResult> future = new CompletableFuture<>();
		future.complete(result);
		List<FhirValidator.ConcurrentValidationTask> tasks = new ArrayList<>();
		FhirValidator.ConcurrentValidationTask task = new FhirValidator.ConcurrentValidationTask(PREFIX, future);
		tasks.add(task);
		return tasks;
	}


}
