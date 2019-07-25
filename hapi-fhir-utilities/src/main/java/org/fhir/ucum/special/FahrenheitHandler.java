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

package org.fhir.ucum.special;

import org.fhir.ucum.Decimal;

public class FahrenheitHandler extends SpecialUnitHandler {

	@Override
	public String getCode() {
		return "[degF]";
	}

	@Override
	public String getUnits() {
		return "K";
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ohf.ucum.special.SpecialUnitHandler#getValue()
	 */
	@Override
	public Decimal getValue() {		
		try {
	    return new Decimal(5).divide(new Decimal(9));
    } catch (Exception e) {
	    // won't happen
    	return null;
    }
	}

}
