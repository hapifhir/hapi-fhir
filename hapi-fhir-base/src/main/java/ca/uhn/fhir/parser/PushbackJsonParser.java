package ca.uhn.fhir.parser;

import java.math.BigDecimal;

import javax.json.stream.JsonLocation;
import javax.json.stream.JsonParser;

public class PushbackJsonParser implements JsonParser {

	private JsonParser myWrap;

	public PushbackJsonParser(JsonParser theWrap) {
		myWrap=theWrap;
	}
	
	@Override
	public boolean hasNext() {
		return myWrap.hasNext();
	}

	@Override
	public Event next() {
		return myWrap.next();
	}

	@Override
	public String getString() {
		return myWrap.getString();
	}

	@Override
	public boolean isIntegralNumber() {
		return myWrap.isIntegralNumber();
	}

	@Override
	public int getInt() {
		return myWrap.getInt();
	}

	@Override
	public long getLong() {
		return myWrap.getLong();
	}

	@Override
	public BigDecimal getBigDecimal() {
		 return myWrap.getBigDecimal();
	}

	@Override
	public JsonLocation getLocation() {
		return myWrap.getLocation();
	}

	@Override
	public void close() {
			myWrap.close();
	}

}
