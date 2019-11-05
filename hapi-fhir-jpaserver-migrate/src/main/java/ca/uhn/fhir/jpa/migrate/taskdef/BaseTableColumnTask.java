package ca.uhn.fhir.jpa.migrate.taskdef;

/*-
 * #%L
 * HAPI FHIR JPA Server - Migration
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import org.apache.commons.lang3.Validate;
import org.thymeleaf.util.StringUtils;

import java.util.Locale;

public abstract class BaseTableColumnTask<T extends BaseTableTask> extends BaseTableTask<T> {

	private String myColumnName;

	@SuppressWarnings("unchecked")
	public T setColumnName(String theColumnName) {
		myColumnName = StringUtils.toUpperCase(theColumnName, Locale.US);
		return (T) this;
	}


	public String getColumnName() {
		return myColumnName;
	}

	@Override
	public void validate() {
		super.validate();
		Validate.notBlank(myColumnName, "Column name not specified");
	}


}
