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
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

public class XMLWriterState {

	private String name;
	private String namespace;
	private boolean children;
	private boolean inComment;
	private boolean pretty;
	
	private XMLNamespace[] namespaceDefns = null;
	
	public void addNamespaceDefn(String namespace, String abbrev) throws IOException {
		XMLNamespace ns;
		ns = getDefnByAbbreviation(abbrev);
		if (ns != null)
			throw new IOException("duplicate namespace declaration on \""+abbrev+"\"");
		ns = new XMLNamespace(namespace, abbrev);
		if (namespaceDefns == null)
			namespaceDefns = new XMLNamespace[] {ns};
		else {
			XMLNamespace[] newns = new XMLNamespace[namespaceDefns.length + 1];
			for (int i = 0; i < namespaceDefns.length; i++) {
				newns[i] = namespaceDefns[i];
			}
			namespaceDefns = newns;
			namespaceDefns[namespaceDefns.length-1] = ns;			
		}
	}

	public XMLNamespace getDefnByNamespace(String namespace) {
		if (namespaceDefns == null)
			return null;
		for (int  i = 0; i < namespaceDefns.length; i++) {
			XMLNamespace element = namespaceDefns[i];
			if (element.getNamespace().equals(namespace))
				return element;
		}
		return null;
	}

	public XMLNamespace getDefnByAbbreviation(String abbreviation) {
		if (namespaceDefns == null)
			return null;
		for (int  i = 0; i < namespaceDefns.length; i++) {
			XMLNamespace element = namespaceDefns[i];
			if (element.getAbbreviation() != null && element.getAbbreviation().equals(abbreviation))
				return element;
		}
		return null;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the namespace
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * @param namespace the namespace to set
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	/**
	 * @return the children
	 */
	public boolean hasChildren() {
		return children;
	}

	public void seeChild() {
		this.children = true;
	}

	public XMLNamespace getDefaultNamespace() {
		if (namespaceDefns == null)
			return null;
		for (int  i = 0; i < namespaceDefns.length; i++) {
			XMLNamespace element = namespaceDefns[i];
			if (element.getAbbreviation() == null)
				return element;
		}
		return null;
	}

	/**
	 * @return the inComment
	 */
	public boolean isInComment() {
		return inComment;
	}

	/**
	 * @param inComment the inComment to set
	 */
	public void setInComment(boolean inComment) {
		this.inComment = inComment;
	}

	/**
	 * @return the pretty
	 */
	public boolean isPretty() {
		return pretty;
	}

	/**
	 * @param pretty the pretty to set
	 */
	public void setPretty(boolean pretty) {
		this.pretty = pretty;
	}

	
}
