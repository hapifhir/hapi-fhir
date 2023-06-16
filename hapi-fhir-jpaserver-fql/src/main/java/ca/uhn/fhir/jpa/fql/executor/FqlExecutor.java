/*-
 * #%L
 * HAPI FHIR JPA Server - Firely Query Language
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
package ca.uhn.fhir.jpa.fql.executor;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimePrimitiveDatatypeDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.fql.parser.FqlParser;
import ca.uhn.fhir.jpa.fql.parser.FqlStatement;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.util.JpaParamUtil;
import ca.uhn.fhir.model.api.IQueryParameterAnd;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.DateOrListParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.QualifierDetails;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class FqlExecutor implements IFqlExecutor {
	public static final int BATCH_SIZE = 1000;
	public static final String[] EMPTY_STRING_ARRAY = new String[0];

	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private IPagingProvider myPagingProvider;
	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;

	/**
	 * Constructor
	 */
	public FqlExecutor() {
		super();
	}

	@Override
	public LocalSearchFqlExecutionResult executeInitialSearch(String theStatement, Integer theLimit, RequestDetails theRequestDetails) {
		FqlParser parser = new FqlParser(myFhirContext, theStatement);
		FqlStatement statement = parser.parse();
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(statement.getFromResourceName());
		if (dao == null) {
			throw new DataFormatException("Unknown or unsupported FROM type: " + statement.getFromResourceName());
		}

		massageSelectColumnNames(statement);

		SearchParameterMap map = new SearchParameterMap();
		IFhirPath fhirPath = myFhirContext.newFhirPath();

		List<FqlStatement.WhereClause> searchClauses = statement.getSearchClauses();
		for (FqlStatement.WhereClause nextSearchClause : searchClauses) {
			if (nextSearchClause.getLeft().equals(Constants.PARAM_ID)) {
				map.add(Constants.PARAM_ID, new TokenOrListParam(null, nextSearchClause.getRightAsStrings().toArray(EMPTY_STRING_ARRAY)));
			} else if (nextSearchClause.getLeft().equals(Constants.PARAM_LASTUPDATED)) {
				DateOrListParam param = new DateOrListParam();
				for (String nextValue : nextSearchClause.getRightAsStrings()) {
					param.addOr(new DateParam(nextValue));
				}
				map.add(Constants.PARAM_LASTUPDATED, param);
			} else if (nextSearchClause.getLeft().startsWith("_")) {
				throw newInvalidRequestExceptionUnknownSearchParameter(nextSearchClause);
			} else {

				String paramName = nextSearchClause.getLeft();
				QualifierDetails qualifiedParamName = QualifierDetails.extractQualifiersFromParameterName(paramName);

				RuntimeSearchParam searchParam = mySearchParamRegistry.getActiveSearchParam(statement.getFromResourceName(), qualifiedParamName.getParamName());
				if (searchParam == null) {
					throw newInvalidRequestExceptionUnknownSearchParameter(nextSearchClause);
				}

				QualifiedParamList values = new QualifiedParamList();
				values.setQualifier(qualifiedParamName.getWholeQualifier());
				values.addAll(nextSearchClause.getRightAsStrings());
				IQueryParameterAnd<?> andParam = JpaParamUtil.parseQueryParams(myFhirContext, searchParam.getParamType(), paramName, List.of(values));
				map.add(qualifiedParamName.getParamName(), andParam);

			}
		}

		IBundleProvider outcome = dao.search(map, theRequestDetails);
		return new LocalSearchFqlExecutionResult(statement, outcome, fhirPath, theLimit, 0);
	}

	private void massageSelectColumnNames(FqlStatement theFqlStatement) {

		List<FqlStatement.SelectClause> selectClauses = theFqlStatement.getSelectClauses();
		for (int i = 0; i < selectClauses.size(); i++) {
			if ("*".equals(selectClauses.get(i).getClause())) {
				resolveAndReplaceStarInSelectClauseAtIndex(theFqlStatement, selectClauses, i);
			}
		}

	}

	private void resolveAndReplaceStarInSelectClauseAtIndex(FqlStatement theFqlStatement, List<FqlStatement.SelectClause> theSelectClauses, int theIndex) {
		String resourceName = theFqlStatement.getFromResourceName();
		TreeSet<String> allLeafPaths = findLeafPaths(resourceName);

		theSelectClauses.remove(theIndex);
		List<String> reversedLeafPaths = new ArrayList<>(allLeafPaths);
		reversedLeafPaths = Lists.reverse(reversedLeafPaths);
		reversedLeafPaths.forEach(t -> theSelectClauses.add(theIndex, new FqlStatement.SelectClause(t)));
	}

	@Nonnull
	private TreeSet<String> findLeafPaths(String theResourceName) {
		TreeSet<String> allLeafPaths = new TreeSet<>();
		RuntimeResourceDefinition def = myFhirContext.getResourceDefinition(theResourceName);
		findLeafPaths(def, allLeafPaths, new ArrayList<>());
		return allLeafPaths;
	}

	private void findLeafPaths(BaseRuntimeElementCompositeDefinition<?> theCompositeDefinition, TreeSet<String> theAllLeafPaths, List<String> theCurrentPath) {
		for (BaseRuntimeChildDefinition nextChild : theCompositeDefinition.getChildren()) {
			for (String nextChildName : nextChild.getValidChildNames()) {
				if (theCurrentPath.contains(nextChildName)) {
					continue;
				}
				if (nextChildName.equals("extension") || nextChildName.equals("modifierExtension")) {
					continue;
				}
				if (nextChildName.equals("id") && theCurrentPath.size() > 0) {
					continue;
				}

				theCurrentPath.add(nextChildName);

				BaseRuntimeElementDefinition<?> childDef = nextChild.getChildByName(nextChildName);
				if (childDef instanceof BaseRuntimeElementCompositeDefinition) {
					if (theCurrentPath.size() < 2) {
						findLeafPaths((BaseRuntimeElementCompositeDefinition<?>) childDef, theAllLeafPaths, theCurrentPath);
					}
				} else if (childDef instanceof RuntimePrimitiveDatatypeDefinition) {
					theAllLeafPaths.add(StringUtils.join(theCurrentPath, "."));
				}

				theCurrentPath.remove(theCurrentPath.size() - 1);
			}
		}
	}

	@Override
	public IFqlExecutionResult executeContinuation(FqlStatement theStatement, String theSearchId, int theStartingOffset, Integer theLimit, RequestDetails theRequestDetails) {
		IBundleProvider resultList = myPagingProvider.retrieveResultList(theRequestDetails, theSearchId);
		IFhirPath fhirPath = myFhirContext.newFhirPath();
		return new LocalSearchFqlExecutionResult(theStatement, resultList, fhirPath, theLimit, theStartingOffset);
	}

	/**
	 * Columns to return, per {@link java.sql.DatabaseMetaData#getTables(String, String, String, String[])}
	 * <OL>
	 * <LI><B>TABLE_CAT</B> String {@code =>} table catalog (may be {@code null})
	 * <LI><B>TABLE_SCHEM</B> String {@code =>} table schema (may be {@code null})
	 * <LI><B>TABLE_NAME</B> String {@code =>} table name
	 * <LI><B>TABLE_TYPE</B> String {@code =>} table type.  Typical types are "TABLE",
	 * "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY",
	 * "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
	 * <LI><B>REMARKS</B> String {@code =>} explanatory comment on the table (may be {@code null})
	 * <LI><B>TYPE_CAT</B> String {@code =>} the types catalog (may be {@code null})
	 * <LI><B>TYPE_SCHEM</B> String {@code =>} the types schema (may be {@code null})
	 * <LI><B>TYPE_NAME</B> String {@code =>} type name (may be {@code null})
	 * <LI><B>SELF_REFERENCING_COL_NAME</B> String {@code =>} name of the designated
	 * "identifier" column of a typed table (may be {@code null})
	 * <LI><B>REF_GENERATION</B> String {@code =>} specifies how values in
	 * SELF_REFERENCING_COL_NAME are created. Values are
	 * "SYSTEM", "USER", "DERIVED". (may be {@code null})
	 * </OL>
	 */
	@Override
	public IFqlExecutionResult introspectTables() {
		List<String> columns = List.of(
			"TABLE_CAT",
			"TABLE_SCHEM",
			"TABLE_NAME",
			"TABLE_TYPE",
			"REMARKS",
			"TYPE_CAT",
			"TYPE_SCHEM",
			"TYPE_NAME",
			"SELF_REFERENCING_COL_NAME",
			"REF_GENERATION"
		);
		List<IFqlExecutionResult.DataTypeEnum> dataTypes = List.of(
			IFqlExecutionResult.DataTypeEnum.STRING,
			IFqlExecutionResult.DataTypeEnum.STRING,
			IFqlExecutionResult.DataTypeEnum.STRING,
			IFqlExecutionResult.DataTypeEnum.STRING,
			IFqlExecutionResult.DataTypeEnum.STRING,
			IFqlExecutionResult.DataTypeEnum.STRING,
			IFqlExecutionResult.DataTypeEnum.STRING,
			IFqlExecutionResult.DataTypeEnum.STRING,
			IFqlExecutionResult.DataTypeEnum.STRING,
			IFqlExecutionResult.DataTypeEnum.STRING
		);
		List<List<Object>> rows = new ArrayList<>();

		TreeSet<String> resourceTypes = new TreeSet<>(myFhirContext.getResourceTypes());
		for (String next : resourceTypes) {
			rows.add(Lists.newArrayList(
				null,
				null,
				next,
				"TABLE",
				null,
				null,
				null,
				null,
				null,
				null
			));
		}

		return new StaticFqlExecutionResult(null, columns, dataTypes, rows);
	}

	/**
	 * Columns from {@link java.sql.DatabaseMetaData#getColumns(String, String, String, String)}
	 * 
	 *  <OL>
	 *  <LI><B>TABLE_CAT</B> String {@code =>} table catalog (may be {@code null})
	 *  <LI><B>TABLE_SCHEM</B> String {@code =>} table schema (may be {@code null})
	 *  <LI><B>TABLE_NAME</B> String {@code =>} table name
	 *  <LI><B>COLUMN_NAME</B> String {@code =>} column name
	 *  <LI><B>DATA_TYPE</B> int {@code =>} SQL type from java.sql.Types
	 *  <LI><B>TYPE_NAME</B> String {@code =>} Data source dependent type name,
	 *  for a UDT the type name is fully qualified
	 *  <LI><B>COLUMN_SIZE</B> int {@code =>} column size.
	 *  <LI><B>BUFFER_LENGTH</B> is not used.
	 *  <LI><B>DECIMAL_DIGITS</B> int {@code =>} the number of fractional digits. Null is returned for data types where
	 * DECIMAL_DIGITS is not applicable.
	 *  <LI><B>NUM_PREC_RADIX</B> int {@code =>} Radix (typically either 10 or 2)
	 *  <LI><B>NULLABLE</B> int {@code =>} is NULL allowed.
	 *      <UL>
	 *      <LI> columnNoNulls - might not allow {@code NULL} values
	 *      <LI> columnNullable - definitely allows {@code NULL} values
	 *      <LI> columnNullableUnknown - nullability unknown
	 *      </UL>
	 *  <LI><B>REMARKS</B> String {@code =>} comment describing column (may be {@code null})
	 *  <LI><B>COLUMN_DEF</B> String {@code =>} default value for the column, which should be interpreted as a string when the value is enclosed in single quotes (may be {@code null})
	 *  <LI><B>SQL_DATA_TYPE</B> int {@code =>} unused
	 *  <LI><B>SQL_DATETIME_SUB</B> int {@code =>} unused
	 *  <LI><B>CHAR_OCTET_LENGTH</B> int {@code =>} for char types the
	 *       maximum number of bytes in the column
	 *  <LI><B>ORDINAL_POSITION</B> int {@code =>} index of column in table
	 *      (starting at 1)
	 *  <LI><B>IS_NULLABLE</B> String  {@code =>} ISO rules are used to determine the nullability for a column.
	 *       <UL>
	 *       <LI> YES           --- if the column can include NULLs
	 *       <LI> NO            --- if the column cannot include NULLs
	 *       <LI> empty string  --- if the nullability for the
	 * column is unknown
	 *       </UL>
	 *  <LI><B>SCOPE_CATALOG</B> String {@code =>} catalog of table that is the scope
	 *      of a reference attribute ({@code null} if DATA_TYPE isn't REF)
	 *  <LI><B>SCOPE_SCHEMA</B> String {@code =>} schema of table that is the scope
	 *      of a reference attribute ({@code null} if the DATA_TYPE isn't REF)
	 *  <LI><B>SCOPE_TABLE</B> String {@code =>} table name that this the scope
	 *      of a reference attribute ({@code null} if the DATA_TYPE isn't REF)
	 *  <LI><B>SOURCE_DATA_TYPE</B> short {@code =>} source type of a distinct type or user-generated
	 *      Ref type, SQL type from java.sql.Types ({@code null} if DATA_TYPE
	 *      isn't DISTINCT or user-generated REF)
	 *   <LI><B>IS_AUTOINCREMENT</B> String  {@code =>} Indicates whether this column is auto incremented
	 *       <UL>
	 *       <LI> YES           --- if the column is auto incremented
	 *       <LI> NO            --- if the column is not auto incremented
	 *       <LI> empty string  --- if it cannot be determined whether the column is auto incremented
	 *       </UL>
	 *   <LI><B>IS_GENERATEDCOLUMN</B> String  {@code =>} Indicates whether this is a generated column
	 *       <UL>
	 *       <LI> YES           --- if this a generated column
	 *       <LI> NO            --- if this not a generated column
	 *       <LI> empty string  --- if it cannot be determined whether this is a generated column
	 *       </UL>
	 *  </OL>
	 *
	 * @param theTableName The table name or null
	 * @param theColumnName The column name or null
	 * @return
	 */
	@Override
	public IFqlExecutionResult introspectColumns(@Nullable String theTableName, @Nullable String theColumnName) {
		List<String> columns = List.of(
			"TABLE_CAT",
			"TABLE_SCHEM",
			"TABLE_NAME",
			"COLUMN_NAME",
			"DATA_TYPE",
			"TYPE_NAME",
			"COLUMN_SIZE",
			"BUFFER_LENGTH",
			"DECIMAL_DIGITS",
			"NUM_PREC_RADIX",
			"NULLABLE",
			"REMARKS",
			"COLUMN_DEF",
			"SQL_DATA_TYPE",
			"SQL_DATETIME_SUB",
			"CHAR_OCTET_LENGTH",
			"ORDINAL_POSITION",
			"IS_NULLABLE",
			"SCOPE_CATALOG",
			"SCOPE_SCHEMA",
			"SCOPE_TABLE",
			"SOURCE_DATA_TYPE",
			"IS_AUTOINCREMENT",
			"IS_GENERATEDCOLUMN"
		);
		List<IFqlExecutionResult.DataTypeEnum> dataTypes = List.of(
			IFqlExecutionResult.DataTypeEnum.STRING, // TABLE_CAT
			IFqlExecutionResult.DataTypeEnum.STRING, // TABLE_SCHEM
			IFqlExecutionResult.DataTypeEnum.STRING, // TABLE_NAME
			IFqlExecutionResult.DataTypeEnum.STRING, // COLUMN_NAME
			IFqlExecutionResult.DataTypeEnum.INTEGER, // DATA_TYPE
			IFqlExecutionResult.DataTypeEnum.STRING,  // TYPE_NAME
			IFqlExecutionResult.DataTypeEnum.INTEGER, // COLUMN_SIZE
			IFqlExecutionResult.DataTypeEnum.STRING, // BUFFER_LENGTH
			IFqlExecutionResult.DataTypeEnum.INTEGER, // DECIMAL_DIGITS
			IFqlExecutionResult.DataTypeEnum.INTEGER, // NUM_PREC_RADIX
			IFqlExecutionResult.DataTypeEnum.INTEGER, // NULLABLE
			IFqlExecutionResult.DataTypeEnum.STRING, // REMARKS
			IFqlExecutionResult.DataTypeEnum.STRING, // COLUMN_DEF
			IFqlExecutionResult.DataTypeEnum.INTEGER, // SQL_DATA_TYPE
			IFqlExecutionResult.DataTypeEnum.INTEGER, // SQL_DATETIME_SUB
			IFqlExecutionResult.DataTypeEnum.INTEGER, // CHAR_OCTET_LENGTH
			IFqlExecutionResult.DataTypeEnum.INTEGER, // ORDINAL_POSITION
			IFqlExecutionResult.DataTypeEnum.STRING, // IS_NULLABLE
			IFqlExecutionResult.DataTypeEnum.STRING, // SCOPE_CATALOG
			IFqlExecutionResult.DataTypeEnum.STRING, // SCOPE_SCHEMA
			IFqlExecutionResult.DataTypeEnum.STRING, // SCOPE_TABLE
			IFqlExecutionResult.DataTypeEnum.STRING, // SOURCE_DATA_TYPE
			IFqlExecutionResult.DataTypeEnum.STRING, // IS_AUTOINCREMENT
			IFqlExecutionResult.DataTypeEnum.STRING  // IS_GENERATEDCOLUMN
		);

		List<List<Object>> rows = new ArrayList<>();
		for (String nextResourceType : new TreeSet<>(myFhirContext.getResourceTypes())) {
			if (isBlank(theTableName) || theTableName.equals(nextResourceType)) {
				TreeSet<String> leafPaths = findLeafPaths(nextResourceType);
				int position = 1;
				for (String nextLeafPath : leafPaths) {
					if (isBlank(theColumnName) || theColumnName.equals(nextLeafPath)) {
						rows.add(
							Lists.newArrayList(
								null,
								null,
								nextResourceType,
								nextLeafPath,
								Types.VARCHAR,
								"string",
								-1,
								null,
								null,
								null,
								1, // nullable
								null,
								null,
								null,
								null,
								null,
								position++,
								"YES",
								null,
								null,
								null,
								null,
								"NO",
								"NO"
							)
						);
					}
				}
			}
		}

		return new StaticFqlExecutionResult(null, columns, dataTypes, rows);
	}

	@Nonnull
	private static InvalidRequestException newInvalidRequestExceptionUnknownSearchParameter(FqlStatement.WhereClause nextSearchClause) {
		return new InvalidRequestException("Unknown/unsupported search parameter: " + UrlUtil.sanitizeUrlPart(nextSearchClause.getLeft()));
	}


}
