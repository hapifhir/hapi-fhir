package ca.uhn.fhir.jpa.util;

import java.util.ArrayList;
import java.util.Queue;

public class SqlQueryList extends ArrayList<SqlQuery> {
	public SqlQueryList(Queue<SqlQuery> theList) {
		super(theList);
	}
}
