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

package org.fhir.ucum;

public class BaseUnit extends Unit {


	/**
	 * abbrevation for property
	 */
	private char dim;
	
	/**
	 * @param code
	 * @param codeUC
	 */
	public BaseUnit(String code, String codeUC) {
		super(ConceptKind.BASEUNIT, code, codeUC);
	}

	/**
	 * @return the dim
	 */
	public char getDim() {
		return dim;
	}

	/**
	 * @param dim the dim to set
	 */
	public void setDim(char dim) {
		this.dim = dim;
	}


}
