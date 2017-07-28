package ca.uhn.fhir.jpa.util;

import java.util.Iterator;

public abstract class BaseIterator<T> implements Iterator<T> {

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
