/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.uhn.fhir.rest.gclient;

import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 *
 * @author HGS
 */
public interface IOperationProcessMsg<T extends IBaseResource> extends IClientExecutable<IOperationProcessMsg<T>, T> {

    IOperationProcessMsg<T> setMessageBundle(IBaseBundle theMsgBundle);

    IOperationProcessMsg<T> setAsyncProcessingMode();

    IOperationProcessMsg<T> setResponseUrlParam(String respondToUri);

}
