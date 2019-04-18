package ca.uhn.fhir.jpa.dao.expunge;

import ca.uhn.fhir.jpa.config.TestDstu3Config;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.model.interceptor.api.HookParams;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestDstu3Config.class})
public class PartitionRunnerTest {
	private static final Logger ourLog = LoggerFactory.getLogger(PartitionRunnerTest.class);

	@Autowired
	private PartitionRunner myPartitionRunner;

	@Autowired
	private DaoConfig myDaoConfig;
	private PointcutLatch myLatch = new PointcutLatch("partition call");

	@After
	public void before() {
		myDaoConfig.setExpungeThreadCount(new DaoConfig().getExpungeThreadCount());
		myDaoConfig.setExpungeBatchSize(new DaoConfig().getExpungeBatchSize());
	}

	@Test
	public void emptyList() throws InterruptedException {
		Slice<Long> resourceIds = buildSlice(0);
		Consumer<List<Long>> partitionConsumer = buildPartitionConsumer(myLatch);
		myLatch.setExpectedCount(0);
		myPartitionRunner.runInPartitionedTransactionThreads(resourceIds, partitionConsumer);
		myLatch.clear();
	}

	private Slice<Long> buildSlice(int size) {
		List<Long> list = new ArrayList<>();
		for (long i = 0; i < size; ++i) {
			list.add(i + 1);
		}
		return (Slice<Long>) new SliceImpl(list);
	}

	@Test
	public void oneItem() throws InterruptedException {
		Slice<Long> resourceIds = buildSlice(1);

		Consumer<List<Long>> partitionConsumer = buildPartitionConsumer(myLatch);
		myLatch.setExpectedCount(1);
		myPartitionRunner.runInPartitionedTransactionThreads(resourceIds, partitionConsumer);
		PartitionCall partitionCall = (PartitionCall) PointcutLatch.getLatchInvocationParameter(myLatch.awaitExpected());
		assertEquals("expunge-1", partitionCall.threadName);
		assertEquals(1, partitionCall.size);
	}


	@Test
	public void twoItems() throws InterruptedException {
		Slice<Long> resourceIds = buildSlice(2);

		Consumer<List<Long>> partitionConsumer = buildPartitionConsumer(myLatch);
		myLatch.setExpectedCount(1);
		myPartitionRunner.runInPartitionedTransactionThreads(resourceIds, partitionConsumer);
		PartitionCall partitionCall = (PartitionCall) PointcutLatch.getLatchInvocationParameter(myLatch.awaitExpected());
		assertEquals("expunge-1", partitionCall.threadName);
		assertEquals(2, partitionCall.size);
	}

	@Test
	public void tenItemsBatch5() throws InterruptedException {
		Slice<Long> resourceIds = buildSlice(10);
		myDaoConfig.setExpungeBatchSize(5);

		Consumer<List<Long>> partitionConsumer = buildPartitionConsumer(myLatch);
		myLatch.setExpectedCount(2);
		myPartitionRunner.runInPartitionedTransactionThreads(resourceIds, partitionConsumer);
		List<HookParams> calls = myLatch.awaitExpected();

		PartitionCall partitionCall = (PartitionCall)PointcutLatch.getLatchInvocationParameter(calls, 0);
		assertEquals("expunge-1", partitionCall.threadName);
		assertEquals(5, partitionCall.size);
		partitionCall = (PartitionCall)PointcutLatch.getLatchInvocationParameter(calls, 1);
		assertEquals("expunge-2", partitionCall.threadName);
		assertEquals(5, partitionCall.size);
	}

	@Test
	public void nineItemsBatch5() throws InterruptedException {
		Slice<Long> resourceIds = buildSlice(9);
		myDaoConfig.setExpungeBatchSize(5);

		Consumer<List<Long>> partitionConsumer = buildPartitionConsumer(myLatch);
		myLatch.setExpectedCount(2);
		myPartitionRunner.runInPartitionedTransactionThreads(resourceIds, partitionConsumer);
		List<HookParams> calls = myLatch.awaitExpected();

		PartitionCall partitionCall = (PartitionCall)PointcutLatch.getLatchInvocationParameter(calls, 0);
		assertEquals("expunge-1", partitionCall.threadName);
		assertEquals(5, partitionCall.size);
		partitionCall = (PartitionCall)PointcutLatch.getLatchInvocationParameter(calls, 1);
		assertEquals("expunge-2", partitionCall.threadName);
		assertEquals(4, partitionCall.size);
	}

	@Test
	public void tenItemsOneThread() throws InterruptedException {
		Slice<Long> resourceIds = buildSlice(10);
		myDaoConfig.setExpungeBatchSize(5);
		myDaoConfig.setExpungeThreadCount(1);

		Consumer<List<Long>> partitionConsumer = buildPartitionConsumer(myLatch);
		myLatch.setExpectedCount(2);
		myPartitionRunner.runInPartitionedTransactionThreads(resourceIds, partitionConsumer);
		List<HookParams> calls = myLatch.awaitExpected();

		PartitionCall partitionCall = (PartitionCall)PointcutLatch.getLatchInvocationParameter(calls, 0);
		assertEquals("expunge-1", partitionCall.threadName);
		assertEquals(5, partitionCall.size);
		partitionCall = (PartitionCall)PointcutLatch.getLatchInvocationParameter(calls, 1);
		assertEquals("expunge-1", partitionCall.threadName);
		assertEquals(5, partitionCall.size);
	}

	Consumer<List<Long>> buildPartitionConsumer(PointcutLatch latch) {
		return list -> latch.call(new PartitionCall(Thread.currentThread().getName(), list.size()));
	}

	static class PartitionCall {
		private final String threadName;
		private final int size;

		public PartitionCall(String theThreadName, int theSize) {
			threadName = theThreadName;
			size = theSize;
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this)
				.append("myThreadName", threadName)
				.append("mySize", size)
				.toString();
		}
	}
}
