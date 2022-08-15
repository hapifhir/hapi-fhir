package ca.uhn.fhir.mdm.util;

/*-
 * #%L
 * HAPI FHIR - Master Data Management
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
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.instance.model.api.IBase;

public final class IdentifierUtil {

	private IdentifierUtil() {
	}

	public static CanonicalIdentifier identifierDtFromIdentifier(IBase theIdentifier) {
		CanonicalIdentifier retval = new CanonicalIdentifier();

		// TODO add other fields like "use" etc
		if (theIdentifier instanceof org.hl7.fhir.dstu3.model.Identifier) {
			org.hl7.fhir.dstu3.model.Identifier ident = (org.hl7.fhir.dstu3.model.Identifier) theIdentifier;
			retval.setSystem(ident.getSystem()).setValue(ident.getValue());
		} else if (theIdentifier instanceof org.hl7.fhir.r4.model.Identifier) {
			org.hl7.fhir.r4.model.Identifier ident = (org.hl7.fhir.r4.model.Identifier) theIdentifier;
			retval.setSystem(ident.getSystem()).setValue(ident.getValue());
		} else if (theIdentifier instanceof org.hl7.fhir.r5.model.Identifier) {
			org.hl7.fhir.r5.model.Identifier ident = (org.hl7.fhir.r5.model.Identifier) theIdentifier;
			retval.setSystem(ident.getSystem()).setValue(ident.getValue());
		} else {
			throw new InternalErrorException(Msg.code(1486) + "Expected 'Identifier' type but was '" + theIdentifier.getClass().getName() + "'");
		}
		return retval;
	}



	/**
	 * Retrieves appropriate FHIR Identifier model instance based on the context version
	 *
	 * @param theFhirContext FHIR context to use for determining the identifier version
	 * @param eid EID to get equivalent FHIR Identifier from
	 * @param <T> Generic Identifier base interface
	 * @return Returns appropriate R4 or DSTU3 Identifier instance
	 */
	public static <T extends IBase> T toId(FhirContext theFhirContext, CanonicalEID eid) {
		switch (theFhirContext.getVersion().getVersion()) {
			case R4:
				return (T) eid.toR4();
			case DSTU3:
				return (T) eid.toDSTU3();
		}
		throw new IllegalStateException(Msg.code(1487) + "Unsupported FHIR version " + theFhirContext.getVersion().getVersion());
	}
}
