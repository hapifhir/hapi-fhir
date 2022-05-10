package ca.uhn.fhir.validation;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
