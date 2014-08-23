package ca.uhn.fhir.validation;

import java.io.InputStream;
import java.io.Reader;

import org.w3c.dom.ls.LSInput;

class LSInputImpl implements LSInput {

	private Reader myCharacterStream;
	private InputStream myByteStream;
	private String myStringData;
	private String mySystemId;
	private String myPublicId;
	private String myBaseURI;
	private String myEncoding;
	private boolean myCertifiedText;

	@Override
	public Reader getCharacterStream() {
		return myCharacterStream;
	}

	@Override
	public void setCharacterStream(Reader theCharacterStream) {
		myCharacterStream=theCharacterStream;
	}

	@Override
	public InputStream getByteStream() {
		return myByteStream;
	}

	@Override
	public void setByteStream(InputStream theByteStream) {
		myByteStream=theByteStream;
	}

	@Override
	public String getStringData() {
		return myStringData;
	}

	@Override
	public void setStringData(String theStringData) {
		myStringData=theStringData;
	}

	@Override
	public String getSystemId() {
		return mySystemId;
	}

	@Override
	public void setSystemId(String theSystemId) {
		mySystemId=theSystemId;
	}

	@Override
	public String getPublicId() {
		return myPublicId;
	}

	@Override
	public void setPublicId(String thePublicId) {
		myPublicId=thePublicId;
	}

	@Override
	public String getBaseURI() {
		return myBaseURI;
	}

	@Override
	public void setBaseURI(String theBaseURI) {
		myBaseURI=theBaseURI;
	}

	@Override
	public String getEncoding() {
		return myEncoding;
	}

	@Override
	public void setEncoding(String theEncoding) {
		myEncoding=theEncoding;
	}

	@Override
	public boolean getCertifiedText() {
		return myCertifiedText;
	}

	@Override
	public void setCertifiedText(boolean theCertifiedText) {
		myCertifiedText=theCertifiedText;
	}

}
