package ca.uhn.fhir.jpa.search.builder.sql;

import com.healthmarketscience.common.util.AppendableExt;
import com.healthmarketscience.sqlbuilder.SqlContext;
import com.healthmarketscience.sqlbuilder.SqlObject;
import com.healthmarketscience.sqlbuilder.ValidationContext;
import com.healthmarketscience.sqlbuilder.dbspec.Column;
import com.healthmarketscience.sqlbuilder.dbspec.Table;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ColumnTupleObject extends SqlObject {

	private final List<Column> myColumns;

	public ColumnTupleObject(Column... theColumns) {
		myColumns = List.of(theColumns);
	}

	@Override
	protected void collectSchemaObjects(ValidationContext vContext) {
		myColumns.forEach(vContext::addColumn);
	}

	@Override
	public void appendTo(AppendableExt app) throws IOException {
		app.append('(');

		for (Iterator<Column> iter = myColumns.iterator(); iter.hasNext(); ) {
			Column column = iter.next();
			appendTableAliasPrefix(app, column.getTable());
			app.append(column.getColumnNameSQL());

			if (iter.hasNext()) {
				app.append(',');
			}
		}

		app.append(')');
	}

	/**
	 * Outputs the table alias prefix <code>"[&lt;tableAlias&gt;.]"</code> for a
	 * column reference if the current SqlContext specifies table aliases should
	 * be used (and the table has an alias), otherwise does nothing.
	 */
	static void appendTableAliasPrefix(AppendableExt app, Table table) throws IOException {
		if (SqlContext.getContext(app).getUseTableAliases()) {
			String alias = table.getAlias();
			if (isNotBlank(alias)) {
				app.append(alias).append(".");
			}
		}
	}

	public static Object from(DbColumn[] theJoinColumns) {
		if (theJoinColumns.length == 1) {
			return theJoinColumns[0];
		} else {
			return new ColumnTupleObject(theJoinColumns);
		}
	}
}
