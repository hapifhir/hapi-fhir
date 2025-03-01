/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.search.builder.sql;

import ca.uhn.fhir.jpa.model.dao.JpaPid;
import com.healthmarketscience.common.util.AppendableExt;
import com.healthmarketscience.sqlbuilder.Expression;
import com.healthmarketscience.sqlbuilder.ValidationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Outputs an SQL tuple for a collection of JpaPids, consisting of
 * ((resId,partitionId),(resId,partitionId),(resId,partitionId),...)
 */
public class JpaPidValueTuples extends Expression {

	private final Collection<String> myValues;

	public JpaPidValueTuples(Collection<String> theValues) {
		myValues = theValues;
	}

	@Override
	protected void collectSchemaObjects(ValidationContext vContext) {
		// nothing
	}

	@Override
	public void appendTo(AppendableExt app) throws IOException {
		app.append('(');

		String value;
		for (Iterator<String> iter = myValues.iterator(); iter.hasNext(); ) {
			if (hasParens()) {
				app.append("('");
			}
			value = iter.next();
			app.append(value);
			app.append("','");
			value = iter.next();
			app.append(value);
			app.append("')");
			if (iter.hasNext()) {
				app.append(',');
			}
		}
		if (hasParens()) {
			app.append(')');
		}
	}

	public static JpaPidValueTuples from(SearchQueryBuilder theSearchQueryBuilder, JpaPid[] thePids) {
		return from(theSearchQueryBuilder, Arrays.asList(thePids));
	}

	public static JpaPidValueTuples from(SearchQueryBuilder theSearchQueryBuilder, Collection<JpaPid> thePids) {
		List<String> placeholders = new ArrayList<>(thePids.size() * 2);
		for (JpaPid next : thePids) {
			placeholders.add(theSearchQueryBuilder.generatePlaceholder(next.getPartitionId()));
			placeholders.add(theSearchQueryBuilder.generatePlaceholder(next.getId()));
		}
		return new JpaPidValueTuples(placeholders);
	}
}
