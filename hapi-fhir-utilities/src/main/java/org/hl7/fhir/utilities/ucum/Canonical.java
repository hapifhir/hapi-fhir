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

import org.hl7.fhir.exceptions.UcumException;


public class Canonical {

	public static class CanonicalUnit {
    private BaseUnit base;
    private int exponent;
		protected CanonicalUnit(BaseUnit base, int exponent) {
	    super();
	    this.base = base;
	    this.exponent = exponent;
    }
		public BaseUnit getBase() {
			return base;
		}
		public int getExponent() {
			return exponent;
		}
		public void setExponent(int exponent) {
			this.exponent = exponent;
		}
    
    
  }

	private Decimal value;
	private List<CanonicalUnit> units = new ArrayList<CanonicalUnit>();
	
	/**
	 * @param value
	 * @param unit
	 */
	public Canonical(Decimal value) {
		super();
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public Decimal getValue() {
		return value;
	}

	/**
	 * @return the unit
	 */
	public List<CanonicalUnit> getUnits() {
		return units;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Decimal value) {
		this.value = value;
	}

	public void multiplyValue(Decimal multiplicand) {
		value = value.multiply(multiplicand);		
	}

	public void multiplyValue(int multiplicand) {
		value = value.multiply(new Decimal(multiplicand));		
	}

	
	public void divideValue(Decimal divisor) throws UcumException  {
		value = value.divide(divisor);		
	}
	
	public void divideValue(int divisor) throws UcumException  {
		value = value.divide(new Decimal(divisor));		
	}

	
}
