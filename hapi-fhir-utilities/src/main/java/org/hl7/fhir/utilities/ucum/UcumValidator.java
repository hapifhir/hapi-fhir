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

import org.hl7.fhir.utilities.ucum.special.Registry;


public class UcumValidator {

	private UcumModel model;
	private List<String> result;
	private Registry handlers;

	public UcumValidator(UcumModel model, Registry handlers) {
		super();
		this.model = model;
		this.handlers = handlers;
	}

	public List<String> validate() {
		result = new ArrayList<String>();
		checkCodes();
		checkUnits();
		return result;
	}

	private void checkCodes() {
		for (Unit unit : model.getBaseUnits()) {
			checkUnitCode(unit.getCode(), true);
		}
		for (Unit unit : model.getDefinedUnits()) {
			checkUnitCode(unit.getCode(), true);
		}		
	}

	private void checkUnits() {
		for (DefinedUnit unit : model.getDefinedUnits()) {
			if (!unit.isSpecial())
				checkUnitCode(unit.getValue().getUnit(), false);
			else if (!handlers.exists(unit.getCode()))
				result.add("No Handler for "+unit.getCode().toString());
		}
	}

	private void checkUnitCode(String code, boolean primary) {
		try {
			Term term = new ExpressionParser(model).parse(code);
			String c = new ExpressionComposer().compose(term);
			if (!c.equals(code))
				result.add("Round trip failed: "+code+" -> "+c);
			new Converter(model, handlers).convert(term);			
		} catch (Exception e) {
			result.add(code+": "+e.getMessage());
		}
		if (primary)
			try {
				// there can't be any codes that have digits in them that aren't inside []
				boolean inBrack = false;
				boolean nonDigits = false;
				for (int i = 0; i < code.length(); i++) {
					char ch = code.charAt(i);
					if (ch == '[')
						if (inBrack)
							throw new Exception("nested [");
						else 
							inBrack = true;
					if (ch == ']')
						if (!inBrack)
							throw new Exception("] without [");
						else 
							inBrack = false;
					nonDigits = nonDigits || !(ch >= '0' && ch <= '9');
					if (ch >= '0' && ch <= '9' && !inBrack && nonDigits) {
						throw new Exception("code "+code+" is ambiguous because  it has digits outside []");
					}
				}
			} catch (Exception e) {
				result.add(e.getMessage());
			}

	}

}
