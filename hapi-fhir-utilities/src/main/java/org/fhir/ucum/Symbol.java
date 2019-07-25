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


public class Symbol extends Component {

	private Unit unit; // may be Base Unit or DefinedUnit
	private Prefix prefix;  // only if unit is metric 
	private int exponent;
	
	
	/**
	 * 
	 */
	public Symbol() {
		super();
	}


	/**
	 * @param unit
	 * @param prefix
	 * @param exponent
	 */
	public Symbol(Unit unit, Prefix prefix, int exponent) {
		super();
		this.unit = unit;
		this.prefix = prefix;
		this.exponent = exponent;
	}


	/**
	 * @return the unit
	 */
	public Unit getUnit() {
		return unit;
	}


	/**
	 * @param unit the unit to set
	 */
	public void setUnit(Unit unit) {
		this.unit = unit;
	}


	/**
	 * @return the prefix
	 */
	public Prefix getPrefix() {
		return prefix;
	}


	/**
	 * @param prefix the prefix to set
	 */
	public void setPrefix(Prefix prefix) {
		this.prefix = prefix;
	}


	/**
	 * @return the exponent
	 */
	public int getExponent() {
		return exponent;
	}


	/**
	 * @param exponent the exponent to set
	 */
	public void setExponent(int exponent) {
		this.exponent = exponent;
	}
	
	public boolean hasPrefix() {
		return prefix != null;
	}


	public void invertExponent() {
		exponent = -exponent;
		
	}
	
}
