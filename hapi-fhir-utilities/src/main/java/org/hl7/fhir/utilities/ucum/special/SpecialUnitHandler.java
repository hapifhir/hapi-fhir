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

public abstract class SpecialUnitHandler {

	/**
	 * Used to connect this handler with the case sensitive unit
	 * @return
	 */
	public abstract String getCode();

	/** 
	 * the alternate units to convert to
	 * 
	 * @return
	 */
	public abstract String getUnits();

	/**
	 * get the conversion value
	 * 
	 * @return
	 */
	public abstract Decimal getValue();
	
}
