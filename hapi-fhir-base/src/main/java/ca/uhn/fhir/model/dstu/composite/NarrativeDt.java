















package ca.uhn.fhir.model.dstu.composite;

import java.util.List;

import ca.uhn.fhir.model.api.BaseElement;
import ca.uhn.fhir.model.api.ICompositeDatatype;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.dstu.valueset.NarrativeStatusEnum;
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
public class NarrativeDt extends BaseElement implements ICompositeDatatype {

	@Child(name="status", type=CodeDt.class, order=0, min=1, max=1)	
	private BoundCodeDt<NarrativeStatusEnum> myStatus;
	
	@Child(name="div", type=XhtmlDt.class, order=1, min=1, max=1)	
	private XhtmlDt myDiv;
	
	@Override
	public boolean isEmpty() {
		return ca.uhn.fhir.util.ElementUtil.isEmpty(  myStatus, myDiv );
	}

	@Override
	public List<IElement> getAllPopulatedChildElements() {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(  myStatus, myDiv );
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

  


}