/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.test.utilities;

import org.apache.commons.lang3.Validate;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class RandomDataHelper {
	public static void fillFieldsRandomly(Object theTarget) {
		new RandomDataHelper().fillFields(theTarget);
	}

	public void fillFields(Object theTarget) {
		ReflectionUtils.doWithFields(theTarget.getClass(), field->{
			Class<?> fieldType = field.getType();
			if (!Modifier.isFinal(field.getModifiers())) {
				ReflectionUtils.makeAccessible(field);
				Object value = generateRandomValue(fieldType);
				field.set(theTarget, value);
			}
		});
	}


	public Object generateRandomValue(Class<?> fieldType) {
		Random random = new Random();
		Object result = null;
		if (fieldType.equals(String.class)) {
			result = UUID.randomUUID().toString();
		} else if (fieldType.equals(UUID.class)) {
			result = UUID.randomUUID();
		} else if (Date.class.isAssignableFrom(fieldType)) {
			result = new Date(System.currentTimeMillis() - random.nextInt(100000000));
		} else if (fieldType.equals(Integer.TYPE)) {
			result = random.nextInt();
		} else if (fieldType.equals(Long.TYPE)) {
			result = random.nextInt();
		} else if (fieldType.equals(Long.class)) {
			result = random.nextLong();
		} else if (fieldType.equals(Double.class) || fieldType.equals(Double.TYPE)) {
			result = random.nextDouble();
		} else if (Number.class.isAssignableFrom(fieldType)) {
			result = random.nextInt(Byte.MAX_VALUE) + 1;
		} else if (Enum.class.isAssignableFrom(fieldType)) {
			Object[] enumValues = fieldType.getEnumConstants();
			result = enumValues[random.nextInt(enumValues.length)];
		} else if (fieldType.equals(Boolean.TYPE) || fieldType.equals(Boolean.class)) {
			result = random.nextBoolean();
		}
		Validate.notNull(result, "Does not support type %s", fieldType);
		return result;
	}
}
