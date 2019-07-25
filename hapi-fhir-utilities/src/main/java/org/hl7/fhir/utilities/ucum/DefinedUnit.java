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


public class DefinedUnit extends Unit{

	/**
	 * whether this is a metric unit or not
	 */
	private boolean metric;
	
	/**
	 * special means?
	 */
	private boolean isSpecial;
	
	/**
	 * The class of this unit
	 */
	private String class_;
	
	/**
	 * Value details
	 */
	private Value value;
	
	


	/**
	 * @param code
	 * @param codeUC
	 */
	public DefinedUnit(String code, String codeUC) {
		super(ConceptKind.UNIT, code, codeUC);
	}


	/**
	 * @return the metric
	 */
	public boolean isMetric() {
		return metric;
	}


	/**
	 * @param metric the metric to set
	 */
	public void setMetric(boolean metric) {
		this.metric = metric;
	}


	/**
	 * @return the isSpecial
	 */
	public boolean isSpecial() {
		return isSpecial;
	}


	/**
	 * @param isSpecial the isSpecial to set
	 */
	public void setSpecial(boolean isSpecial) {
		this.isSpecial = isSpecial;
	}


	/**
	 * @return the class_
	 */
	public String getClass_() {
		return class_;
	}


	/**
	 * @param class_ the class_ to set
	 */
	public void setClass_(String class_) {
		this.class_ = class_;
	}


	/**
	 * @return the value
	 */
	public Value getValue() {
		return value;
	}


	/**
	 * @param value the value to set
	 */
	public void setValue(Value value) {
		this.value = value;
	}


	/* (non-Javadoc)
	 * @see org.eclipse.ohf.ucum.model.BaseUnit#getDescription()
	 */
	@Override
	public String getDescription() {
		return super.getDescription()+" = "+value.getDescription();
	}


		
	
}
