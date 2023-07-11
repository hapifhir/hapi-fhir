/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.test.util;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.beans.BeanInfo;
import java.beans.FeatureDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasItems;

public class HasGetterOrSetterForAllJsonFields extends TypeSafeMatcher<Class<? extends IModelJson>> {
	private static final Logger ourLog = LoggerFactory.getLogger(HasGetterOrSetterForAllJsonFields.class);

	@Override
	public void describeTo(Description description) {
		description.appendText("All @JsonProperty annotated fields have getters and setters.");
	}

	@Override
	protected boolean matchesSafely(Class<? extends IModelJson> item) {
		List<String> jsonPropertyFields = getJsonPropertyFields(item);
		Matcher<Iterable<Object>> matcher = hasItems(jsonPropertyFields.toArray());
		List<String> properties = getProperties(item);
		ourLog.info("{}: testing {} @JsonProperty fields", item.getSimpleName(), jsonPropertyFields.size());
		return matcher.matches(properties);
	}

	@Nonnull
	private List<String> getJsonPropertyFields(Class<? extends IModelJson> item) {
		List<Field> fields = new ArrayList<>();

		populateFields(fields, item);

		return fields.stream()
			.filter(this::isJsonProperty)
			.filter(this::isNotCollection)
			.filter(this::isNotMap)
			.map(Field::getName)
			.map(this::stripPrefix)
			.map(this::stripUnderscoreSuffix)
			.sorted()
			.collect(Collectors.toList());
	}

	private boolean isNotCollection(Field theField) {
		return !Collection.class.isAssignableFrom(theField.getType());
	}

	private boolean isNotMap(Field theField) {
		return !Map.class.isAssignableFrom(theField.getType());
	}

	private boolean isJsonProperty(Field theField) {
		if (!theField.isAnnotationPresent(JsonProperty.class)) {
			return false;
		}
		Schema apiModelProperty = theField.getAnnotation(Schema.class);
		if (apiModelProperty != null && apiModelProperty.accessMode() == Schema.AccessMode.READ_ONLY) {
			return false;
		}
		return apiModelProperty == null || !apiModelProperty.hidden();
	}

	private String stripPrefix(String theFieldName) {
		if (theFieldName.startsWith("my")) {
			return theFieldName.substring(2, 3).toLowerCase() + theFieldName.substring(3);
		}
		return theFieldName;
	}

	private String stripUnderscoreSuffix(String theFieldName) {
		if (theFieldName.endsWith("_")) {
			return theFieldName.substring(0, theFieldName.length() - 1);
		}
		return theFieldName;
	}

	@Override
	protected void describeMismatchSafely(Class<? extends IModelJson> item, Description mismatchDescription) {
		mismatchDescription.appendText(" for class ").appendText(item.getName()).appendText(", ");
		List<String> jsonFields = getJsonPropertyFields(item);
		Matcher<Iterable<Object>> matcher = hasItems(jsonFields.toArray());
		List<String> properties = getProperties(item);
		matcher.describeMismatch(properties, mismatchDescription);
		mismatchDescription.appendText("\n  All non-collection @JsonProperty fields: " + String.join(", ", jsonFields));
		mismatchDescription.appendText("\n  Have get/set methods for: " + String.join(", ", properties));
	}

	private List<String> getProperties(Class<? extends IModelJson> item) {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(item);
			return Arrays.stream(beanInfo.getPropertyDescriptors())
				.map(FeatureDescriptor::getName)
				.filter(name -> !"class".equals(name))
				.map(this::lowerCaseFirstLetter)
				.sorted()
				.collect(Collectors.toList());
		} catch (IntrospectionException e) {
			throw new AssertionError("Unable to introspect " + item.getName(), e);
		}
	}

	private String lowerCaseFirstLetter(String thePropertyName) {
		return thePropertyName.substring(0, 1).toLowerCase() + thePropertyName.substring(1);
	}

	private static void populateFields(List<Field> theFields, Class<?> theItem) {
		theFields.addAll(Arrays.asList(theItem.getDeclaredFields()));

		if (theItem.getSuperclass() != null) {
			populateFields(theFields, theItem.getSuperclass());
		}
	}

	public static HasGetterOrSetterForAllJsonFields hasGetterOrSetterForAllJsonFields() {
		return new HasGetterOrSetterForAllJsonFields();
	}
}
