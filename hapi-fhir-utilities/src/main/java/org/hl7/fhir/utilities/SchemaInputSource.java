package org.hl7.fhir.utilities;
/*
Copyright (c) 2011+, HL7, Inc
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, 
are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this 
   list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, 
   this list of conditions and the following disclaimer in the documentation 
   and/or other materials provided with the distribution.
 * Neither the name of HL7 nor the names of its contributors may be used to 
   endorse or promote products derived from this software without specific 
   prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
POSSIBILITY OF SUCH DAMAGE.

*/

import java.io.InputStream;
import java.io.Reader;

import org.w3c.dom.ls.LSInput;

public class SchemaInputSource implements LSInput {

	private InputStream stream;
  private String publicId;
  private String systemId;
  private String namespaceURI;

	public SchemaInputSource(InputStream inputStream, String publicId, String systemId, String namespaceURI) {
		this.stream = inputStream;
		this.publicId = publicId;
		this.systemId = systemId;
		this.namespaceURI = namespaceURI;
	}

	@Override
	public String getBaseURI() {
	  return namespaceURI;
	}

	@Override
	public InputStream getByteStream() {
		return stream;
	}

	@Override
	public boolean getCertifiedText() {
    throw new Error("Not implemented yet");
	}

	@Override
	public Reader getCharacterStream() {
    return null;
	}

	@Override
	public String getEncoding() {
    return "UTF-8";
	}

	@Override
	public String getPublicId() {
    return publicId;
	}

	@Override
	public String getStringData() {
    return null;
	}

	@Override
	public String getSystemId() {
    return systemId;
	}

	@Override
	public void setBaseURI(String baseURI) {
    throw new Error("Not implemented yet");
	}

	@Override
	public void setByteStream(InputStream byteStream) {
    throw new Error("Not implemented yet");
	}

	@Override
	public void setCertifiedText(boolean certifiedText) {
    throw new Error("Not implemented yet");
	}

	@Override
	public void setCharacterStream(Reader characterStream) {
    throw new Error("Not implemented yet");
	}

	@Override
	public void setEncoding(String encoding) {
    throw new Error("Not implemented yet");
	}

	@Override
	public void setPublicId(String publicId) {
    throw new Error("Not implemented yet");
	}

	@Override
	public void setStringData(String stringData) {
    throw new Error("Not implemented yet");
	}

	@Override
	public void setSystemId(String systemId) {
    throw new Error("Not implemented yet");
	}
}