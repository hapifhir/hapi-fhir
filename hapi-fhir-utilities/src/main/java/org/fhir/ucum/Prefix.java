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


public class Prefix extends Concept {
	
	/**
	 * value for the prefix.  
	 */
	private Decimal value; // 1^-24 through to 1^24

	/**
	 * @param code
	 * @param codeUC
	 */
	public Prefix(String code, String codeUC) {
		super(ConceptKind.PREFIX, code, codeUC);
	}

	/**
	 * @return the index
	 */
	public Decimal getValue() {
		return value;
	}

	/**
	 * @param index the index to set
	 */
	public void setValue(Decimal index) {
		this.value = index;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ohf.ucum.model.Concept#getDescription()
	 */
	@Override
	public String getDescription() {
		return super.getDescription()+" = "+value.toString();
	}
	
	
	

}
