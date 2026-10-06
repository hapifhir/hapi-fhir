package ca.uhn.fhir.mdm.model;

import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.rest.server.TransactionLogMessages;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

// Created by claude-opus-5-5
class MdmTransactionContextTest {

	@Test
	void restoreCheckpoint_discardsLinksAndMessagesAddedSinceTheCheckpoint() {
		MdmTransactionContext context = new MdmTransactionContext(
			TransactionLogMessages.createNew(), MdmTransactionContext.OperationType.CREATE_RESOURCE);
		IMdmLink before = mock(IMdmLink.class);
		context.addMdmLink(before);
		context.addTransactionLogMessage("before");

		MdmTransactionContext.Checkpoint checkpoint = context.createCheckpoint();
		context.addMdmLink(mock(IMdmLink.class));
		context.addTransactionLogMessage("during");
		context.setIsBlocked(true);
		context.restoreCheckpoint(checkpoint);

		assertThat(context.getMdmLinks()).containsExactly(before);
		assertThat(context.getTransactionLogMessages().getValues()).containsExactly("before");
		assertThat(context.getIsBlocked()).isFalse();
	}

	@Test
	void restoreCheckpoint_withoutLogMessages_isHandled() {
		MdmTransactionContext context = new MdmTransactionContext(MdmTransactionContext.OperationType.CREATE_RESOURCE);
		MdmTransactionContext.Checkpoint checkpoint = context.createCheckpoint();
		context.addMdmLink(mock(IMdmLink.class));

		context.restoreCheckpoint(checkpoint);

		assertThat(context.getMdmLinks()).isEmpty();
		assertThat(context.getTransactionLogMessages()).isNull();
	}

	@Test
	void restoreCheckpoint_whenMessagesStartedAfterTheCheckpoint_discardsThem() {
		MdmTransactionContext context = new MdmTransactionContext(
			TransactionLogMessages.createNew(), MdmTransactionContext.OperationType.CREATE_RESOURCE);
		MdmTransactionContext.Checkpoint checkpoint = context.createCheckpoint();
		context.addTransactionLogMessage("during");

		context.restoreCheckpoint(checkpoint);

		assertThat(context.getTransactionLogMessages().getValues()).isEmpty();
	}
}
