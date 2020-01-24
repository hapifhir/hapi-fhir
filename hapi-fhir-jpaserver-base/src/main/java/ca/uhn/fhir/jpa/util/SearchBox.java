package ca.uhn.fhir.jpa.util;

import org.hibernate.search.spatial.impl.Point;

public class SearchBox {
	private final Point mySouthWest;
	private final Point myNorthEast;

	public SearchBox(Point theSouthWest, Point theNorthEast) {
		mySouthWest = theSouthWest;
		myNorthEast = theNorthEast;
	}

	public Point getSouthWest() {
		return mySouthWest;
	}

	public Point getNorthEast() {
		return myNorthEast;
	}
}
