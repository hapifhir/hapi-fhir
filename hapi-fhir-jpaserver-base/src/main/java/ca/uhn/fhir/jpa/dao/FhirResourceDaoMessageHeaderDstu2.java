package ca.uhn.fhir.jpa.dao;

/*
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoMessageHeader;
import ca.uhn.fhir.model.dstu2.resource.MessageHeader;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import org.hl7.fhir.instance.model.api.IBaseBundle;

public class FhirResourceDaoMessageHeaderDstu2 extends BaseHapiFhirResourceDao<MessageHeader> implements IFhirResourceDaoMessageHeader<MessageHeader> {

	public static IBaseBundle throwProcessMessageNotImplemented() {
		throw new NotImplementedOperationException(Msg.code(945) + "This operation is not yet implemented on this server");
	}
}
