











package ca.uhn.fhir.model.datatype;

import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.datatype.*;

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
public class NarrativeDt extends BaseCompositeDatatype {

	@Child(name="status", type=CodeDt.class, order=0, min=1, max=1)	
	private CodeDt myStatus;
	
	@Child(name="div", type=XhtmlDt.class, order=1, min=1, max=1)	
	private XhtmlDt myDiv;
	
	/**
	 * Gets the value(s) for status (generated | extensions | additional)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the narrative - whether it's entirely generated (from just the defined data or the extensions too), or whether a human authored it and it may contain additional data
     * </p> 
	 */
	public CodeDt getStatus() {
		return myStatus;
	}

	/**
	 * Sets the value(s) for status (generated | extensions | additional)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the narrative - whether it's entirely generated (from just the defined data or the extensions too), or whether a human authored it and it may contain additional data
     * </p> 
	 */
	public void setStatus(CodeDt theValue) {
		myStatus = theValue;
	}
	
	/**
	 * Gets the value(s) for div (Limited xhtml content)
	 *
     * <p>
     * <b>Definition:</b>
     * The actual narrative content, a stripped down version of XHTML
     * </p> 
	 */
	public XhtmlDt getDiv() {
		return myDiv;
	}

	/**
	 * Sets the value(s) for div (Limited xhtml content)
	 *
     * <p>
     * <b>Definition:</b>
     * The actual narrative content, a stripped down version of XHTML
     * </p> 
	 */
	public void setDiv(XhtmlDt theValue) {
		myDiv = theValue;
	}
	


}