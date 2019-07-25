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


public abstract class Unit extends Concept {

	/**
	 * kind of thing this base unit represents
	 */
	private String property;

	public Unit(ConceptKind kind, String code, String codeUC) {
		super(kind, code, codeUC);
	}

	/**
	 * @return the property
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * @param property the property to set
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ohf.ucum.model.Concept#getDescription()
	 */
	@Override
	public String getDescription() {
		return super.getDescription()+" ("+property+")";
	}
	
	
}
