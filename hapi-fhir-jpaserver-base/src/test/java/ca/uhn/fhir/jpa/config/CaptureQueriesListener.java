package ca.uhn.fhir.jpa.config;

import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.proxy.ParameterSetOperation;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.hibernate.engine.jdbc.internal.BasicFormatterImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CaptureQueriesListener implements ProxyDataSourceBuilder.SingleQueryExecution {

	private static final LinkedList<Query> LAST_N_QUERIES = new LinkedList<>();

	@Override
	public void execute(ExecutionInfo execInfo, List<QueryInfo> queryInfoList) {
		synchronized (LAST_N_QUERIES) {
			for (QueryInfo next : queryInfoList) {
				String sql = next.getQuery();
				List<String> params;
				if (next.getParametersList().size() > 0 && next.getParametersList().get(0).size() > 0) {
					List<ParameterSetOperation> values = next
						.getParametersList()
						.get(0);
					params = values.stream()
						.map(t -> t.getArgs()[1])
						.map(t -> t != null ? t.toString() : "NULL")
						.collect(Collectors.toList());
				} else {
					params = new ArrayList<>();
				}
				LAST_N_QUERIES.add(0, new Query(sql, params));
			}
			while (LAST_N_QUERIES.size() > 100) {
				LAST_N_QUERIES.removeLast();
			}
		}
	}

	public static class Query {
		private final String myThreadName = Thread.currentThread().getName();
		private final String mySql;
		private final List<String> myParams;

		Query(String theSql, List<String> theParams) {
			mySql = theSql;
			myParams = Collections.unmodifiableList(theParams);
		}

		public String getThreadName() {
			return myThreadName;
		}

		public String getSql(boolean theInlineParams, boolean theFormat) {
			String retVal = mySql;
			if (theFormat) {
				retVal = new BasicFormatterImpl().format(retVal);
			}

			if (theInlineParams) {
				List<String> nextParams = new ArrayList<>(myParams);
				while (retVal.contains("?") && nextParams.size() > 0) {
					int idx = retVal.indexOf("?");
					retVal = retVal.substring(0, idx) + nextParams.remove(0) + retVal.substring(idx + 1);
				}
			}

			return retVal;

		}

	}

	public static void clear() {
		synchronized (LAST_N_QUERIES) {
			LAST_N_QUERIES.clear();
		}
	}

	/**
	 * Index 0 is newest!
	 */
	public static ArrayList<Query> getLastNQueries() {
		synchronized (LAST_N_QUERIES) {
			return new ArrayList<>(LAST_N_QUERIES);
		}
	}
}
