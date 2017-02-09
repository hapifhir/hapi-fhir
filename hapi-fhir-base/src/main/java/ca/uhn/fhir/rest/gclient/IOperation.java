package ca.uhn.fhir.rest.gclient;

import org.hl7.fhir.instance.model.api.IBaseResource;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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
public interface IOperation extends IBaseOn<IOperationUnnamed> {

    /**
     * This operation is called <b><a href="https://www.hl7.org/fhir/messaging.html">$process-message</a></b> as defined by FHIR
     * DSTU2.<br><br>
     * Usage :<br>
     * <code>
     * <pre>
     * Bundle response = client
     * .operation()
     * .onServer()
     * .processMessage()
     * .setResponseUrlParam("http://myserver/fhir")
     * .setMessageBundle(msgBundle)
     * .synchronous(Bundle.class)
     * .execute();
     * 
     * //if you want to send an async message
     * 
     * OperationOutcome response = client
     * .operation()
     * .onServer()
     * .processMessage()
     * .setResponseUrlParam("http://myserver/fhir")
     * .setMessageBundle(msgBundle)
     * .asynchronous(OperationOutcome.class)
     * .execute();
     *
     * </pre>
     * </code>
     *
     * @see <a href="https://www.hl7.org/fhir/messaging.html">2.4 Messaging
     * using FHIR Resources</a>
     *
     * @return An interface that defines the operation related to sending
     * Messages to a Messaging Server
     */
    IOperationProcessMsg processMessage();
}
