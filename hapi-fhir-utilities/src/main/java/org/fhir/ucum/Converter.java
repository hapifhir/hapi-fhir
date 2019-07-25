package org.fhir.ucum;

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

import java.util.Collections;
import java.util.Comparator;

import org.fhir.ucum.Canonical.CanonicalUnit;
import org.fhir.ucum.special.Registry;

public class Converter {

	private UcumModel model;
	private Registry handlers;
	
	/**
	 * @param model
	 */
	public Converter(UcumModel model, Registry handlers) {
		super();
		this.model = model;
		this.handlers = handlers;
	}

	
	public Canonical convert(Term term) throws UcumException  {
		return normalise("  ", term);
	}
	
	private Canonical normalise(String indent, Term term) throws UcumException  {
		Canonical result = new Canonical(new Decimal(1));
		
		debug(indent, "canonicalise", term);
    boolean div = false;
		Term t = term;
		while (t != null) {
    	if (t.getComp() instanceof Term) {
    		Canonical temp = normalise(indent+"  ", (Term) t.getComp());
    		if (div) {
    			result.divideValue(temp.getValue());
    			for (CanonicalUnit c : temp.getUnits()) 
    				c.setExponent(0-c.getExponent());
    		} else {
    			result.multiplyValue(temp.getValue());
    		}
    		result.getUnits().addAll(temp.getUnits());
    	} else if (t.getComp() instanceof Factor) {
    		if (div)
    			result.divideValue(((Factor) t.getComp()).getValue());
    		else
    			result.multiplyValue(((Factor) t.getComp()).getValue());
    	} else if (t.getComp() instanceof Symbol) {
    		Symbol o = (Symbol) t.getComp();
  			Canonical temp = normalise(indent, o);
    		if (div) {
    			result.divideValue(temp.getValue());
    			for (CanonicalUnit c : temp.getUnits()) 
    				c.setExponent(0-c.getExponent());
    		} else {
    			result.multiplyValue(temp.getValue());
    		}
    		result.getUnits().addAll(temp.getUnits());
    	}
			div = t.getOp() == Operator.DIVISION;
			t = t.getTerm();
		}

		debug(indent, "collate", result);

		for (int i = result.getUnits().size()-1; i >= 0; i--) {
			CanonicalUnit sf = result.getUnits().get(i);
			for (int j = i-1; j >=0; j--) {
				CanonicalUnit st = result.getUnits().get(j);
				if (st.getBase() == sf.getBase()) {
					st.setExponent(sf.getExponent()+st.getExponent());
					result.getUnits().remove(i);
					break;
				}
			}
		}
		for (int i = result.getUnits().size()-1; i >= 0; i--) {
			CanonicalUnit sf = result.getUnits().get(i);
			if (sf.getExponent() == 0)
				result.getUnits().remove(i);
		}
		
		debug(indent, "sort", result);
		Collections.sort(result.getUnits(), new Comparator<CanonicalUnit>(){
		   @Override
		   public int compare(final CanonicalUnit lhs, CanonicalUnit rhs) {
		  	 return lhs.getBase().getCode().compareTo(rhs.getBase().getCode());
		     }
		 });
		debug(indent, "done", result);
		return result;
	}

  private Canonical normalise(String indent, Symbol sym) throws UcumException  {
  	Canonical result = new Canonical(new Decimal(1));
  	
  	if (sym.getUnit() instanceof BaseUnit) {
  		result.getUnits().add(new CanonicalUnit((BaseUnit) sym.getUnit(), sym.getExponent()));
  	} else {
			Canonical can = expandDefinedUnit(indent, (DefinedUnit) sym.getUnit());
			for (CanonicalUnit c : can.getUnits()) {
				c.setExponent(c.getExponent() *  sym.getExponent());
			}
			result.getUnits().addAll(can.getUnits());
			if (sym.getExponent() > 0) 
				for (int i = 0; i < sym.getExponent(); i++)
					result.multiplyValue(can.getValue());
			else
				for (int i = 0; i > sym.getExponent(); i--)
					result.divideValue(can.getValue());
		} 
		if (sym.getPrefix() != null) {
			if (sym.getExponent() > 0) 
				for (int i = 0; i < sym.getExponent(); i++)
					result.multiplyValue(sym.getPrefix().getValue());
			else
				for (int i = 0; i > sym.getExponent(); i--)
					result.divideValue(sym.getPrefix().getValue());
		}
		return result;
  }

	private Canonical expandDefinedUnit(String indent, DefinedUnit unit) throws UcumException  {
		String u = unit.getValue().getUnit();
		if (unit.isSpecial()) {
			if (!handlers.exists(unit.getCode())) 
				throw new UcumException("Not handled yet (special unit)");
			else 
				u = handlers.get(unit.getCode()).getUnits();
		}
			
		Term t = new ExpressionParser(model).parse(u);
		debug(indent, "now handle", t);
		Canonical result = normalise(indent+"  ", t);
		result.multiplyValue(unit.getValue().getValue());
		return result;
  }


	private void debug(String indent, String state, Term unit) {
//		System.out.println(indent+state+": "+new ExpressionComposer().compose(unit));
	}


	private void debug(String indent, String state, Canonical can) {
//		 System.out.println(indent+state+": "+new ExpressionComposer().compose(can));
	}



}
