package ca.uhn.fhir.jpa.fql.executor;

import ca.uhn.fhir.jpa.fql.parser.FqlStatement;

import java.util.List;

public interface IFqlResult {

	List<String> getColumnNames();

	boolean hasNext();

	Row getNextRow();

	boolean isClosed();

	void close();

	String getSearchId();

	int getLimit();

	FqlStatement getStatement();

	class Row {

		private final List<String> myValues;
		private final int mySearchRowNumber;

		public Row(int theSearchRowNumber, List<String> theValues) {
			mySearchRowNumber = theSearchRowNumber;
			myValues = theValues;
		}

		public int searchRowNumber() {
			return mySearchRowNumber;
		}

		public List<String> values() {
			return myValues;
		}


	}


}
