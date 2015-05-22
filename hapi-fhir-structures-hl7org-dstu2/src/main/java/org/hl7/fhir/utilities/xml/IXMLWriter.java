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
package org.hl7.fhir.utilities.xml;

/*
 * #%L
 * HAPI FHIR Structures - HL7.org DSTU2
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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


import java.io.IOException;

/**
 * Generalize 
 * @author dennisn
 */
public interface IXMLWriter {
	
	public abstract void start() throws IOException;

	public abstract void attribute(String namespace, String name, String value,
			boolean onlyIfNotEmpty) throws IOException;

	public abstract void attribute(String namespace, String name, String value)
			throws IOException;

	public abstract void attribute(String name, String value,
			boolean onlyIfNotEmpty) throws IOException;

	public abstract void attribute(String name, String value)
			throws IOException;

	public abstract void attributeNoLines(String name, String value)
			throws IOException;

//	public abstract XMLNamespace findByNamespace(String namespace);

	public abstract boolean namespaceDefined(String namespace);

//	public abstract XMLNamespace findByAbbreviation(String abbreviation);

	public abstract boolean abbreviationDefined(String abbreviation);

//	public abstract XMLNamespace findDefaultNamespace();

	public abstract String getDefaultNamespace();

	public abstract void namespace(String namespace) throws IOException;

	public abstract void setDefaultNamespace(String namespace) throws IOException;

	public abstract void namespace(String namespace, String abbreviation)
			throws IOException;

	public abstract void comment(String comment, boolean doPretty)
			throws IOException;

	public abstract void open(String namespace, String name) throws IOException;

	public abstract void open(String namespace, String name, String comment)
			throws IOException;

	public abstract void close(String name) throws IOException;

	public abstract void close(String namespace, String name)
			throws IOException;

	public abstract void closeToLevel(int count) throws IOException;

	public abstract void close() throws IOException;

	public abstract void open(String name) throws IOException;

	public abstract void element(String namespace, String name, String content,
			boolean onlyIfNotEmpty) throws IOException;

	public abstract void element(String namespace, String name, String content,
			String comment) throws IOException;

	public abstract void element(String namespace, String name, String content)
			throws IOException;

	public abstract void element(String name, String content,
			boolean onlyIfNotEmpty) throws IOException;

	public abstract void element(String name, String content)
			throws IOException;

	public abstract void text(String content) throws IOException;
	
	public abstract void text(String content, boolean dontEscape) throws IOException;

	public abstract void cData(String text) throws IOException;

	public abstract void writeBytes(byte[] bytes) throws IOException;

	public abstract boolean isPretty() throws IOException;

	public abstract void setPretty(boolean pretty) throws IOException;

	/**
	 * Start comment inserts a <!-- in the stream, but allows the user to 
	 * go on creating xml content as usual, with proper formatting applied etc.
	 * Any comments inserted inside a comment will be terminated with -- > instead of -->
	 * so the comment doesn't close prematurely.
	 * @throws IOException 
	 */
	public abstract void startCommentBlock() throws IOException;

	public abstract void endCommentBlock() throws IOException;

}
