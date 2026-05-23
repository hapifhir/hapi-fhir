package ca.uhn.fhir.jpa.batch2.mockjob.sendtostep;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobFileJson;
import ca.uhn.fhir.model.api.IModelJson;
import jakarta.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseMockSendToStepWorker {

	private List<MockSendToStepJobModelJson> myReceivedMessages = new ArrayList<>();

	public List<MockSendToStepJobModelJson> getReceivedMessages() {
		return myReceivedMessages;
	}

	void captureMessage(StepExecutionDetails<MockSendToStepJobParameters, MockSendToStepJobModelJson> theStepExecutionDetails) {
		MockSendToStepJobModelJson data = theStepExecutionDetails.getData();
		myReceivedMessages.add(data);
	}

	void sendDataToSubsequentSteps(@Nonnull StepExecutionDetails<MockSendToStepJobParameters, ?> theStepExecutionDetails, @Nonnull IJobDataSink<MockSendToStepJobModelJson> theDataSink) {
		MockSendToStepJobParameters parameters = theStepExecutionDetails.getParameters();
		String currentStepId = theStepExecutionDetails.getCurrentStepId();
		String nextStepId = theStepExecutionDetails.getNextStepId();

		List<MockSendToStepJobParameters.MessageJson> messages = parameters.getMessages().getOrDefault(currentStepId, List.of());

		for (MockSendToStepJobParameters.MessageJson message : messages) {
			if (message.isSendInvalidType()) {
				BulkImportJobFileJson invalidModel = new BulkImportJobFileJson();
				invalidModel.setContents("hello");
				theDataSink.acceptForFutureStep(message.getTargetStepId(), invalidModel);
				continue;
			}

			MockSendToStepJobModelJson data = new MockSendToStepJobModelJson();
			data.setSourceStepId(currentStepId);
			data.setMessage(message.getMessage());

			if (message.getTargetStepId().equals(nextStepId)) {
				theDataSink.accept(data);
			} else {
				theDataSink.acceptForFutureStep(message.getTargetStepId(), data);
			}
		}
	}
}
