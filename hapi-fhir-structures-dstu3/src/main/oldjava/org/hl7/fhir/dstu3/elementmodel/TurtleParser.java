package org.hl7.fhir.dstu3.elementmodel;

import java.io.InputStream;
import java.io.OutputStream;

import org.hl7.fhir.dstu3.formats.IParser.OutputStyle;
import org.hl7.fhir.dstu3.context.IWorkerContext;

public class TurtleParser extends ParserBase {

	public TurtleParser(IWorkerContext theContext) {
		super(theContext);
	}

	@Override
	public Element parse(InputStream theStream) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void compose(Element theE, OutputStream theDestination, OutputStyle theStyle, String theBase) throws Exception {
		throw new UnsupportedOperationException();
	}

}
