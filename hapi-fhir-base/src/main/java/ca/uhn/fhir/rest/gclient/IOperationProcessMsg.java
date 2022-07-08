/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.uhn.fhir.rest.gclient;

/*
 * #%L
 * HAPI FHIR - Core Library
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
