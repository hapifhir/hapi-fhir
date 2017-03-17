/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.uhn.fhir.rest.gclient;

import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 *
 * @author HGS
 */
public interface IOperationProcessMsg{

    /**
     * Set the Message Bundle to POST to the messaging server.<br>
     * After this call you must choose either the method synchronous or asynchronous to set the processing mode.
     * 
     * @param <R>
     * @param theMsgBundle A Bundle of type message
     * @return 
     */
    <R extends IBaseResource> IOperationProcessMsgMode<R> setMessageBundle(IBaseBundle theMsgBundle);

    /**
     * An optional query parameter indicating that responses from the receiving server should be sent to this url
     * 
     * @param respondToUri The receiving endpoint to witch server response messages should be sent.
     * @return 
     */
    IOperationProcessMsg setResponseUrlParam(String respondToUri);
}
