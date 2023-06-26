package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.jpa.search.builder.SearchBuilder;
import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.proxy.ParameterSetOperation;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * What's going on here:
 * <p>
 * Oracle chokes on queries that have more than 1000 parameters. We have stress tests that
 * do giant queries, so this is here to cause a failure if these result in lots of
 * bound parameters that would cause a failure in oracle. By default these don't cause issues
 * in Derby which is why we simulate the failure using this listener.
 * </p>
 */
public class BlockLargeNumbersOfParamsListener implements ProxyDataSourceBuilder.SingleQueryExecution {

	private static final Logger ourLog = LoggerFactory.getLogger(BlockLargeNumbersOfParamsListener.class);

	@Override
	public void execute(ExecutionInfo theExecInfo, List<QueryInfo> theQueryInfoList) {
		ourLog.trace("SqlQuery with {} queries", theQueryInfoList.size());
		for (QueryInfo next : theQueryInfoList) {
			ourLog.trace("Have {} param lists", next.getParametersList().size());
			for (List<ParameterSetOperation> nextParamsList : next.getParametersList()) {
				ourLog.trace("Have {} sub-param lists", nextParamsList.size());
				Validate.isTrue(nextParamsList.size() < 1000, "SqlQuery has %s parameters: %s", nextParamsList.size(), next.getQuery());
			}
		}
	}
}
