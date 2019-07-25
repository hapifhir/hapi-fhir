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

public class Value {

	private String unit;
	
	private String unitUC;
	
	private Decimal value;
	
	private String text;

	/**
	 * @param unit
	 * @param unitUC
	 * @param value
	 */
	public Value(String unit, String unitUC, Decimal value) {
		super();
		this.unit = unit;
		this.unitUC = unitUC;
		this.value = value;
	}

	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * @return the unitUC
	 */
	public String getUnitUC() {
		return unitUC;
	}

	/**
	 * @param unitUC the unitUC to set
	 */
	public void setUnitUC(String unitUC) {
		this.unitUC = unitUC;
	}

	/**
	 * @return the value
	 */
	public Decimal getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Decimal value) {
		this.value = value;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	public String getDescription() {
		if (value == null)
			return unit;
		return value.toString()+unit;
	}
	
	
}
