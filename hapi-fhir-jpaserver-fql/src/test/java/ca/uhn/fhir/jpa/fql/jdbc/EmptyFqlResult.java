package ca.uhn.fhir.jpa.fql.jdbc;

import ca.uhn.fhir.jpa.fql.executor.IFqlResult;
import ca.uhn.fhir.jpa.fql.parser.FqlStatement;

import java.util.List;

public class EmptyFqlResult implements IFqlResult {
	private final String mySearchId;

	public EmptyFqlResult(String theSearchId) {
		mySearchId = theSearchId;
	}

	@Override
	public List<String> getColumnNames() {
		return null;
	}

	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public Row getNextRow() {
		return null;
	}

	@Override
	public boolean isClosed() {
		return false;
	}

	@Override
	public void close() {

	}

	@Override
	public String getSearchId() {
		return mySearchId;
	}

	@Override
	public int getLimit() {
		return 0;
	}

	@Override
	public FqlStatement getStatement() {
		return null;
	}
}
