/*******************************************************************************
 * Crown Copyright (c) 2006 - 2014, Copyright (c) 2006 - 2014 Kestral Computing P/L.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Kestral Computing P/L - initial implementation
 *******************************************************************************/

package org.hl7.fhir.utilities.ucum;

import java.util.ArrayList;
import java.util.List;

public class Concept {

	private ConceptKind kind;
	/**
	 * case sensitive code for this concept
	 */
	private String code;
	
	/**
	 * case insensitive code for this concept
	 */
	private String codeUC;
	
	/**
	 * print symbol for this code 
	 */
	private String printSymbol;
	
	/**
	 * names for the concept
	 */
	private List<String> names = new ArrayList<String>();
	
	
	/**
	 * @param code
	 * @param codeUC
	 */
	public Concept(ConceptKind kind, String code, String codeUC) {
		super();
		this.kind = kind;
		this.code = code;
		this.codeUC = codeUC;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the codeUC
	 */
	public String getCodeUC() {
		return codeUC;
	}

	/**
	 * @param codeUC the codeUC to set
	 */
	public void setCodeUC(String codeUC) {
		this.codeUC = codeUC;
	}

	/**
	 * @return the printSymbol
	 */
	public String getPrintSymbol() {
		return printSymbol;
	}

	/**
	 * @param printSymbol the printSymbol to set
	 */
	public void setPrintSymbol(String printSymbol) {
		this.printSymbol = printSymbol;
	}

	/**
	 * @return the name
	 */
	public List<String> getNames() {
		return names;
	}

	/**
	 * @return the kind
	 */
	public ConceptKind getKind() {
		return kind;
	}

	public String getDescription() {
		return  kind.toString().toLowerCase()+" "+code+" ('"+names.get(0)+"')";
	}
	
	@Override
	public String toString() {
		return this.getCode() + " = " + getDescription();
	}
}
