package ca.uhn.fhir.jpa.cosmos;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import com.helger.commons.lang.StackTraceHelper;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Interceptor
public class CosmosPartitionInterceptor {
	private static final Logger ourLog = LoggerFactory.getLogger(CosmosPartitionInterceptor.class);

	private final List<RequestPartitionId> myReadRequestPartitionIds = new ArrayList<>();
	private final List<RequestPartitionId> myCreateRequestPartitionIds = new ArrayList<>();

	public void addReadPartition(RequestPartitionId theRequestPartitionId) {
		myReadRequestPartitionIds.add(theRequestPartitionId);
		ourLog.info("Adding partition {} for read (not have {})", theRequestPartitionId, myReadRequestPartitionIds.size());
	}

	@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_READ)
	public RequestPartitionId partitionIdentifyRead(ServletRequestDetails theRequestDetails) {

		// Just to be nice, figure out the first line in the stack that isn't a part of the
		// partitioning or interceptor infrastructure, just so it's obvious who is asking
		// for a partition ID
		String stack;
		try {
			throw new Exception();
		} catch (Exception e) {
			stack = StackTraceHelper.getStackAsString(e);
			stack = Arrays.stream(stack.split("\\n"))
				.filter(t -> t.contains("ca.uhn.fhir"))
				.filter(t -> !t.toLowerCase().contains("interceptor"))
				.filter(t -> !t.toLowerCase().contains("partitionhelper"))
				.findFirst()
				.orElse("UNKNOWN");
		}

		RequestPartitionId retVal = myReadRequestPartitionIds.remove(0);
		ourLog.info("Returning partition {} for read at: {}", retVal, stack);
		return retVal;
	}

	public void addCreatePartition(RequestPartitionId theRequestPartitionId) {
		myCreateRequestPartitionIds.add(theRequestPartitionId);
	}

	@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_CREATE)
	public RequestPartitionId PartitionIdentifyCreate(IBaseResource theResource, ServletRequestDetails theRequestDetails) {
		assertNotNull(theResource);
		assertTrue(!myCreateRequestPartitionIds.isEmpty(), "No create partitions left in interceptor");
		RequestPartitionId retVal = myCreateRequestPartitionIds.remove(0);
		ourLog.debug("Returning partition [{}] for create of resource {} with date {}", retVal, theResource, retVal.getPartitionDate());
		return retVal;
	}

	public void assertNoRemainingIds() {
		assertEquals(0, myReadRequestPartitionIds.size(), () -> "Found " + myReadRequestPartitionIds.size() + " READ partitions remaining in interceptor");
		assertEquals(0, myCreateRequestPartitionIds.size(), () -> "Still have " + myCreateRequestPartitionIds.size() + " CREATE partitions remaining in interceptor");
	}


}
