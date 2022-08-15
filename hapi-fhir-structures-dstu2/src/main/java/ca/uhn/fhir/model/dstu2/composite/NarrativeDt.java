package ca.uhn.fhir.model.dstu2.composite;

/*
 * #%L
 * HAPI FHIR Structures - DSTU2 (FHIR v1.0.0)
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
import ca.uhn.fhir.model.dstu2.valueset.NarrativeStatusEnum;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
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

	@Child(name="status", type=CodeDt.class, order=0, min=1, max=1)	
	private BoundCodeDt<NarrativeStatusEnum> myStatus;
	
	@Child(name="div", type=XhtmlDt.class, order=1, min=1, max=1)	
	private XhtmlDt myDiv;
	
	public NarrativeDt() {
		// nothing
	}
	
	public NarrativeDt(XhtmlDt theDiv, NarrativeStatusEnum theStatus) {
		setDiv(theDiv);
		setStatus(theStatus);
	}

	@Override
	public boolean isEmpty() {
		return ca.uhn.fhir.util.ElementUtil.isEmpty(  myStatus, myDiv );
	}

	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements( theType, myStatus, myDiv );
	}

	/**
	 * Gets the value(s) for <b>status</b> (generated | extensions | additional).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the narrative - whether it's entirely generated (from just the defined data or the extensions too), or whether a human authored it and it may contain additional data
     * </p> 
	 */
	public BoundCodeDt<NarrativeStatusEnum> getStatusElement() {
		return getStatus();
	}
	
	/**
	 * Gets the value(s) for <b>status</b> (generated | extensions | additional).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the narrative - whether it's entirely generated (from just the defined data or the extensions too), or whether a human authored it and it may contain additional data
     * </p> 
	 */
	@Override
	public BoundCodeDt<NarrativeStatusEnum> getStatus() {
		if (myStatus == null) {
			myStatus = new BoundCodeDt<NarrativeStatusEnum>(NarrativeStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	/**
	 * Sets the value(s) for <b>status</b> (generated | extensions | additional)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the narrative - whether it's entirely generated (from just the defined data or the extensions too), or whether a human authored it and it may contain additional data
     * </p> 
	 */
	public void setStatus(BoundCodeDt<NarrativeStatusEnum> theValue) {
		myStatus = theValue;
	}

	/**
	 * Sets the value(s) for <b>status</b> (generated | extensions | additional)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the narrative - whether it's entirely generated (from just the defined data or the extensions too), or whether a human authored it and it may contain additional data
     * </p> 
	 */
	public void setStatus(NarrativeStatusEnum theValue) {
		getStatus().setValueAsEnum(theValue);
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




}
