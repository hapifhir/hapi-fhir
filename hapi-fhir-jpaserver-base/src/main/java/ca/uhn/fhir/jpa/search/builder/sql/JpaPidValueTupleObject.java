package ca.uhn.fhir.jpa.search.builder.sql;

import ca.uhn.fhir.jpa.model.dao.JpaPid;
import com.healthmarketscience.common.util.AppendableExt;
import com.healthmarketscience.sqlbuilder.Expression;
import com.healthmarketscience.sqlbuilder.SqlContext;
import com.healthmarketscience.sqlbuilder.SqlObject;
import com.healthmarketscience.sqlbuilder.ValidationContext;
import com.healthmarketscience.sqlbuilder.dbspec.Table;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Outputs an SQL tuple for a collection of JpaPids, consisting of
 * ((resId,partitionId),(resId,partitionId),(resId,partitionId),...)
 */
public class JpaPidValueTupleObject extends Expression {

	private final Collection<String> myValues;

	public JpaPidValueTupleObject(Collection<String> theValues) {
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

	public static JpaPidValueTupleObject from(SearchQueryBuilder theSearchQueryBuilder, JpaPid[] thePids) {
		List<String> placeholders = new ArrayList<>(thePids.length * 2);
		for (JpaPid next : thePids) {
			placeholders.add(theSearchQueryBuilder.generatePlaceholder(next.getPartitionId()));
			placeholders.add(theSearchQueryBuilder.generatePlaceholder(next.getId()));
		}
		return new JpaPidValueTupleObject(placeholders);
	}
}
