package ca.uhn.fhir.jpa.fql.executor;

import java.util.List;

public interface IFqlResult {

	List<String> getColumnNames();

	boolean hasNext();

	List<String> getNextRowAsStrings();

	boolean isClosed();

	void close();
}
