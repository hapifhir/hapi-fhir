package org.hl7.fhir.utilities;

import java.util.ArrayList;
import java.util.List;

public class Table<T> {

	private int rowCount;
	private int colCount;
	private List<List<T>> rows = new ArrayList<List<T>>();
	
	public Table(int rowCount, int colCount) {
		this.rowCount = rowCount;
		this.colCount = colCount;
		for (int r = 0; r < rowCount; r++) {
			rows.add(new ArrayList<T>());
			for (int c = 0; c < colCount; c++) {
				rows.get(r).add(null);
			}
		}
	}

	public void setValue(int r, int c, T value) {
	  rows.get(r).set(c, value);
	}

	public T get(int r, int c) {
  	return rows.get(r).get(c);
	}

	public int colCount() {
		return colCount;
	}

}
