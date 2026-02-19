package ca.uhn.fhir.test.utilities;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.insert.Insert;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class SqlParsingUtil {

	/**
	 * Non instantiable
	 */
	private SqlParsingUtil() {}


	public static String parseInsertStatementTableName(String theInsertSql) throws JSQLParserException {
		Insert parsedStatement = (Insert) CCJSqlParserUtil.parse(theInsertSql);
		return parsedStatement.getTable().getName();
	}

	public static Map<String, String> parseInsertStatementParams(String theInsertSql) throws JSQLParserException {
		Insert parsedStatement = (Insert) CCJSqlParserUtil.parse(theInsertSql);

		Map<String, String> retVal = new HashMap<>();

		for (int i = 0; i < parsedStatement.getColumns().size(); i++) {
			String columnName = parsedStatement.getColumns().get(i).getColumnName();
			String columnValue = parsedStatement.getValues().getExpressions().get(i).toString();
			assertFalse(retVal.containsKey(columnName), ()->"Duplicate column in insert statement: " + columnName);
			retVal.put(columnName, columnValue);
		}

		return retVal;
	}


}
