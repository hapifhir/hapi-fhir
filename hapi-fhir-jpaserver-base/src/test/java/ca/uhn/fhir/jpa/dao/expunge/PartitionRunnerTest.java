package ca.uhn.fhir.jpa.dao.expunge;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.jpa.config.TestDstu3Config;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.test.concurrency.PointcutLatch;
import com.google.common.collect.Sets;
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
import java.util.Set;
import java.util.function.Consumer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isOneOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestDstu3Config.class})
public class PartitionRunnerTest {
	private static final Logger ourLog = LoggerFactory.getLogger(PartitionRunnerTest.class);
	private static final String EXPUNGE_THREADNAME_1 = "expunge-1";
	private static final String EXPUNGE_THREADNAME_2 = "expunge-2";

	@Autowired
	private PartitionRunner myPartitionRunner;

	@Autowired
	private DaoConfig myDaoConfig;
	private PointcutLatch myLatch = new PointcutLatch("partition call");

	@After
	public void before() {
		myDaoConfig.setExpungeThreadCount(new DaoConfig().getExpungeThreadCount());
		myDaoConfig.setExpungeBatchSize(new DaoConfig().getExpungeBatchSize());
		myLatch.clear();
	}

	@Test
	public void emptyList() {
		Slice<Long> resourceIds = buildSlice(0);
		Consumer<List<Long>> partitionConsumer = buildPartitionConsumer(myLatch);
		myLatch.setExpectedCount(0);
		myPartitionRunner.runInPartitionedThreads(resourceIds, partitionConsumer);
		myLatch.clear();
	}

	private Slice<Long> buildSlice(int size) {
		List<Long> list = new ArrayList<>();
		for (long i = 0; i < size; ++i) {
			list.add(i + 1);
		}
		return new SliceImpl(list);
	}

	@Test
	public void oneItem() throws InterruptedException {
		Slice<Long> resourceIds = buildSlice(1);

		Consumer<List<Long>> partitionConsumer = buildPartitionConsumer(myLatch);
		myLatch.setExpectedCount(1);
		myPartitionRunner.runInPartitionedThreads(resourceIds, partitionConsumer);
		PartitionCall partitionCall = (PartitionCall) PointcutLatch.getLatchInvocationParameter(myLatch.awaitExpected());
		assertEquals("main", partitionCall.threadName);
		assertEquals(1, partitionCall.size);
	}


	@Test
	public void twoItems() throws InterruptedException {
		Slice<Long> resourceIds = buildSlice(2);

		Consumer<List<Long>> partitionConsumer = buildPartitionConsumer(myLatch);
		myLatch.setExpectedCount(1);
		myPartitionRunner.runInPartitionedThreads(resourceIds, partitionConsumer);
		PartitionCall partitionCall = (PartitionCall) PointcutLatch.getLatchInvocationParameter(myLatch.awaitExpected());
		assertEquals("main", partitionCall.threadName);
		assertEquals(2, partitionCall.size);
	}

	@Test
	public void tenItemsBatch5() throws InterruptedException {
		Slice<Long> resourceIds = buildSlice(10);
		myDaoConfig.setExpungeBatchSize(5);

		Consumer<List<Long>> partitionConsumer = buildPartitionConsumer(myLatch);
		myLatch.setExpectedCount(2);
		myPartitionRunner.runInPartitionedThreads(resourceIds, partitionConsumer);
		List<HookParams> calls = myLatch.awaitExpected();
		PartitionCall partitionCall1 = (PartitionCall) PointcutLatch.getLatchInvocationParameter(calls, 0);
		assertThat(partitionCall1.threadName, isOneOf(EXPUNGE_THREADNAME_1, EXPUNGE_THREADNAME_2));
		assertEquals(5, partitionCall1.size);
		PartitionCall partitionCall2 = (PartitionCall) PointcutLatch.getLatchInvocationParameter(calls, 1);
		assertThat(partitionCall2.threadName, isOneOf(EXPUNGE_THREADNAME_1, EXPUNGE_THREADNAME_2));
		assertEquals(5, partitionCall2.size);
		assertNotEquals(partitionCall1.threadName, partitionCall2.threadName);
	}

	@Test
	public void nineItemsBatch5() throws InterruptedException {
		Slice<Long> resourceIds = buildSlice(9);
		myDaoConfig.setExpungeBatchSize(5);

		// We don't care in which order, but one partition size should be
		// 5 and one should be 4
		Set<Integer> nums = Sets.newHashSet(5, 4);

		Consumer<List<Long>> partitionConsumer = buildPartitionConsumer(myLatch);
		myLatch.setExpectedCount(2);
		myPartitionRunner.runInPartitionedThreads(resourceIds, partitionConsumer);
		List<HookParams> calls = myLatch.awaitExpected();
		PartitionCall partitionCall1 = (PartitionCall) PointcutLatch.getLatchInvocationParameter(calls, 0);
		assertThat(partitionCall1.threadName, isOneOf(EXPUNGE_THREADNAME_1, EXPUNGE_THREADNAME_2));
		assertEquals(true, nums.remove(partitionCall1.size));
		PartitionCall partitionCall2 = (PartitionCall) PointcutLatch.getLatchInvocationParameter(calls, 1);
		assertThat(partitionCall2.threadName, isOneOf(EXPUNGE_THREADNAME_1, EXPUNGE_THREADNAME_2));
		assertEquals(true, nums.remove(partitionCall2.size));
		assertNotEquals(partitionCall1.threadName, partitionCall2.threadName);
	}

	@Test
	public void tenItemsOneThread() throws InterruptedException {
		Slice<Long> resourceIds = buildSlice(10);
		myDaoConfig.setExpungeBatchSize(5);
		myDaoConfig.setExpungeThreadCount(1);

		Consumer<List<Long>> partitionConsumer = buildPartitionConsumer(myLatch);
		myLatch.setExpectedCount(2);
		myPartitionRunner.runInPartitionedThreads(resourceIds, partitionConsumer);
		List<HookParams> calls = myLatch.awaitExpected();
		{
			PartitionCall partitionCall = (PartitionCall) PointcutLatch.getLatchInvocationParameter(calls, 0);
			assertEquals(EXPUNGE_THREADNAME_1, partitionCall.threadName);
			assertEquals(5, partitionCall.size);
		}
		{
			PartitionCall partitionCall = (PartitionCall) PointcutLatch.getLatchInvocationParameter(calls, 1);
			assertEquals(EXPUNGE_THREADNAME_1, partitionCall.threadName);
			assertEquals(5, partitionCall.size);
		}
	}

	private Consumer<List<Long>> buildPartitionConsumer(PointcutLatch latch) {
		return list -> latch.call(new PartitionCall(Thread.currentThread().getName(), list.size()));
	}

	static class PartitionCall {
		private final String threadName;
		private final int size;

		PartitionCall(String theThreadName, int theSize) {
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
