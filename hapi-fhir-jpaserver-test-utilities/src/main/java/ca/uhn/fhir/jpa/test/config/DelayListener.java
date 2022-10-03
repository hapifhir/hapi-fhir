package ca.uhn.fhir.jpa.test.config;

import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DelayListener implements ProxyDataSourceBuilder.SingleQueryExecution {

	private boolean enabled = false;
	private AtomicInteger deleteCount= new AtomicInteger(0);

	public void enable() {
		enabled = true;
	}

	public void reset() {
		enabled = false;
		deleteCount = new AtomicInteger(0);
	}

	@Override
	public void execute(ExecutionInfo execInfo, List<QueryInfo> queryInfoList) {
		if (enabled && queryInfoList.get(0).getQuery().contains("from HFJ_RES_LINK")) {
			if (deleteCount.getAndIncrement() == 0) {
				try {
					Thread.sleep(500L);
				} catch (InterruptedException theE) {
					theE.printStackTrace();
				}
			}
		}
	}

}
