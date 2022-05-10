package ca.uhn.fhir.validation.schematron;

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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.util.CoverageIgnore;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.IValidatorModule;

import java.lang.reflect.Constructor;

public class SchematronProvider {


	private static final String I18N_KEY_NO_PH_WARNING = FhirValidator.class.getName() + ".noPhWarningOnStartup";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirValidator.class);
	
	@CoverageIgnore
	public static boolean isSchematronAvailable(FhirContext theFhirContext) {
		try {
			Class.forName("com.helger.schematron.ISchematronResource");
			return true;
		} catch (ClassNotFoundException e) {
			ourLog.info(theFhirContext.getLocalizer().getMessage(I18N_KEY_NO_PH_WARNING));
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	@CoverageIgnore
	public static Class<? extends IValidatorModule> getSchematronValidatorClass() {
		try {
			return (Class<? extends IValidatorModule>) Class.forName("ca.uhn.fhir.validation.schematron.SchematronBaseValidator");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(Msg.code(1973) + "Cannot resolve schematron validator ", e);
		}
	}
	
	@CoverageIgnore
	public static IValidatorModule getSchematronValidatorInstance(FhirContext myContext) {
		try {
			Class<? extends IValidatorModule> cls = getSchematronValidatorClass();
			Constructor<? extends IValidatorModule> constructor = cls.getConstructor(FhirContext.class);
			return constructor.newInstance(myContext);
		} catch (Exception e) {
			throw new IllegalStateException(Msg.code(1974) + "Cannot construct schematron validator ", e);
		}
	}
}
