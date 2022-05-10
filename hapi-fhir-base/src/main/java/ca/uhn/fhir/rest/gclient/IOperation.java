package ca.uhn.fhir.rest.gclient;

import org.hl7.fhir.instance.model.api.IIdType;

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
public interface IOperation extends IBaseOn<IOperationUnnamed> {

	/**
	 * This operation operates on a specific version of a resource
	 */
	IOperationUnnamed onInstanceVersion(IIdType theId);

	/**
	 * This operation is called <b><a href="https://www.hl7.org/fhir/messaging.html">$process-message</a></b> as defined by the FHIR
	 * specification.<br><br>
	 * Usage :<br>
	 * <code>
	 * <pre>
	 * Bundle response = client
	 * .operation()
	 * .processMessage()
	 * .synchronous(Bundle.class)
	 * .setResponseUrlParam("http://myserver/fhir")
	 * .setMessageBundle(msgBundle)
	 * .execute();
	 *
	 * //if you want to send an async message
	 *
	 * OperationOutcome response = client
	 * .operation()
	 * .processMessage()
	 * .asynchronous(OperationOutcome.class)
	 * .setResponseUrlParam("http://myserver/fhir")
	 * .setMessageBundle(msgBundle)
	 * .execute();
	 *
	 * </pre>
	 * </code>
	 *
	 * @return An interface that defines the operation related to sending
	 * Messages to a Messaging Server
	 * @see <a href="https://www.hl7.org/fhir/messaging.html">2.4 Messaging
	 * using FHIR Resources</a>
	 */
	IOperationProcessMsg processMessage();

}
