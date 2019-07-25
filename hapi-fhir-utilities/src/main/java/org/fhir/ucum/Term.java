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

public class Term extends Component {

	// op-term where op = /
	// component
	// component-op-term
	private Component comp;
	private Operator op;
	private Term term;
	/**
	 * 
	 */
	public Term() {
		super();
	}
	/**
	 * @return the comp
	 */
	public Component getComp() {
		return comp;
	}
	/**
	 * @param comp the comp to set
	 */
	public void setComp(Component comp) {
		this.comp = comp;
	}
	/**
	 * @return the op
	 */
	public Operator getOp() {
		return op;
	}
	/**
	 * @param op the op to set
	 */
	public void setOp(Operator op) {
		this.op = op;
	}
	/**
	 * @return the term
	 */
	public Term getTerm() {
		return term;
	}
	/**
	 * @param term the term to set
	 */
	public void setTerm(Term term) {
		this.term = term;
	}
	
	public boolean hasComp() {
		return comp != null;
	}

	public boolean hasOp() {
		return op != null;
	}
	
	public boolean hasTerm() {
		return term != null;
	}
	public void setTermCheckOp(Term term) {
		setTerm(term);
		if (term == null)
			setOp(null);
	}

	
}
