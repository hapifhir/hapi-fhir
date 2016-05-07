package ca.uhn.fhir.util;

import java.util.StringTokenizer;

public class UrlPathTokenizer extends StringTokenizer {

	public UrlPathTokenizer(String theRequestPath) {
		super(theRequestPath, "/");
	}

	@Override
	public String nextToken() {
		return UrlUtil.unescape(super.nextToken());
	}

	@CoverageIgnore
	@Override
	public String nextToken(String theDelim) {
		throw new UnsupportedOperationException();
	}

	@CoverageIgnore
	@Override
	public Object nextElement() {
		return super.nextElement();
	}

}
