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

import org.hl7.fhir.utilities.ucum.Canonical.CanonicalUnit;

public class ExpressionComposer {


	public String compose(Term term) {
		if (term == null)
			return "1";
		StringBuilder bldr = new StringBuilder();
		composeTerm(bldr, term);
		return bldr.toString();
	}

	private void composeTerm(StringBuilder bldr, Term term) {
		if (term.getComp() != null)
			composeComp(bldr, term.getComp());
		if (term.getOp() != null)
			composeOp(bldr, term.getOp());
		if (term.getTerm() != null) { 
			composeTerm(bldr, term.getTerm());		
		}
	}

	private void composeComp(StringBuilder bldr, Component comp) {
		if (comp instanceof Factor)
			composeFactor(bldr, (Factor)comp);
		else if (comp instanceof Symbol)
			composeSymbol(bldr, (Symbol)comp);
		else if (comp instanceof Term) {
			bldr.append('(');
			composeTerm(bldr, (Term)comp);
			bldr.append(')');
		} else
			bldr.append('?');
	}

	private void composeSymbol(StringBuilder bldr, Symbol symbol) {
		if (symbol.getPrefix() != null) { 
			bldr.append(symbol.getPrefix().getCode());
		}
		bldr.append(symbol.getUnit().getCode());
		if (symbol.getExponent() != 1) { 
			bldr.append(symbol.getExponent());
		}
	}

	private void composeFactor(StringBuilder bldr, Factor comp) {
	   bldr.append(comp.getValue());		
	}

	private void composeOp(StringBuilder bldr, Operator op) {
		if (op == Operator.DIVISION)
			bldr.append("/");
		else
			bldr.append(".");
	}

	public String compose(Canonical can) {
		return compose(can, true);
	}
	
	public String compose(Canonical can, boolean value) {
	  StringBuilder b = new StringBuilder();
	  if (value)
	  	b.append(can.getValue().asDecimal());
	  boolean first = true;
	  for (CanonicalUnit c : can.getUnits()) {
	  	if (first) first = false; else b.append(".");
	  	b.append(c.getBase().getCode());
	  	if (c.getExponent() != 1)
	  		b.append(c.getExponent());
	  }
	  return b.toString();
  }

}
