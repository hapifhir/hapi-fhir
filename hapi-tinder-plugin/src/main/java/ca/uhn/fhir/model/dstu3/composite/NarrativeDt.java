















package ca.uhn.fhir.model.dstu3.composite;

/*
 * #%L
 * HAPI FHIR Structures - DSTU2 (FHIR v1.0.0)
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.List;

import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.base.composite.BaseNarrativeDt;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.XhtmlDt;

/**
 * HAPI/FHIR <b>Narrative</b> Datatype
 * (A human-readable formatted text, including images)
 *
 * <p>
 * <b>Definition:</b>
 * A human-readable formatted text, including images
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 */
@DatatypeDef(name="Narrative") 
public class NarrativeDt extends BaseNarrativeDt {

	@Child(name="div", type=XhtmlDt.class, order=1, min=1, max=1)	
	private XhtmlDt myDiv;
	
	public NarrativeDt() {
		// nothing
	}
	
	@Override
	public boolean isEmpty() {
		return ca.uhn.fhir.util.ElementUtil.isEmpty(  myDiv );
	}

	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements( theType, myDiv );
	}

	/**
	 * Gets the value(s) for <b>div</b> (Limited xhtml content).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The actual narrative content, a stripped down version of XHTML
     * </p> 
	 */
	public XhtmlDt getDivElement() {
		return getDiv();
	}
	
	/**
	 * Gets the value(s) for <b>div</b> (Limited xhtml content).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The actual narrative content, a stripped down version of XHTML
     * </p> 
	 */
	@Override
	public XhtmlDt getDiv() {
		if (myDiv == null) {
			myDiv = new XhtmlDt();
		}
		return myDiv;
	}

	/**
	 * Sets the value(s) for <b>div</b> (Limited xhtml content)
	 *
     * <p>
     * <b>Definition:</b>
     * The actual narrative content, a stripped down version of XHTML
     * </p> 
	 */
	public void setDiv(XhtmlDt theValue) {
		myDiv = theValue;
	}

	/**
	 * Sets the value using a textual DIV (or simple text block which will be
	 * converted to XHTML)
	 */
	public void setDiv(String theTextDiv) {
		myDiv = new XhtmlDt(theTextDiv);
	}

	@Override
	public BoundCodeDt getStatus() {
		return null;
	}




}
