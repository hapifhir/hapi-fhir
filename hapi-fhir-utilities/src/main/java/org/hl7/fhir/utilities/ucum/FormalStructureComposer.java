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

public class FormalStructureComposer {

	public String compose(Term term) {
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
//			bldr.append('(');
			composeTerm(bldr, term.getTerm());		
//			bldr.append('}');
		}
	}

	private void composeComp(StringBuilder bldr, Component comp) {
		if (comp instanceof Factor)
			composeFactor(bldr, (Factor)comp);
		else if (comp instanceof Symbol)
			composeSymbol(bldr, (Symbol)comp);
		else if (comp instanceof Term)
			composeTerm(bldr, (Term)comp);
		else
			bldr.append('?');
	}

	private void composeSymbol(StringBuilder bldr, Symbol symbol) {
		bldr.append('(');
		if (symbol.getPrefix() != null) { 
			bldr.append(symbol.getPrefix().getNames().get(0));
		}
		bldr.append(symbol.getUnit().getNames().get(0));
		if (symbol.getExponent() != 1) { 
			bldr.append(" ^ ");
			bldr.append(symbol.getExponent());
		}
		bldr.append(')');
	}

	private void composeFactor(StringBuilder bldr, Factor comp) {
	   bldr.append(comp.getValue());		
	}

	private void composeOp(StringBuilder bldr, Operator op) {
		if (op == Operator.DIVISION)
			bldr.append(" / ");
		else
			bldr.append(" * ");
	}

}
