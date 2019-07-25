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

package org.hl7.fhir.utilities.ucum.special;

import org.hl7.fhir.utilities.ucum.Decimal;

/**
 * If you want to actually use one of these units, then you'll
 * have to figure out how to implement them
 * 
 * @author Grahame Grieve
 *
 */
public class HoldingHandler extends SpecialUnitHandler {

	private String code;
	private String units;
	private Decimal value = Decimal.one();
	
	
	/**
	 * @param code
	 * @param units
	 */
	public HoldingHandler(String code, String units) {
		super();
		this.code = code;
		this.units = units;
	}

	public HoldingHandler(String code, String units, Decimal value) {
		super();
		this.code = code;
		this.units = units;
		this.value = value;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getUnits() {
		return units;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ohf.ucum.special.SpecialUnitHandler#getValue()
	 */
	@Override
	public Decimal getValue() {		
		return value;
	}

}
