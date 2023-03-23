package ca.uhn.fhir.jpa.util;

import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.CustomCondition;
import com.healthmarketscience.sqlbuilder.InCondition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSchema;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSpec;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class QueryParameterUtilsTest {

    private static final String VALUE_1 = "value1";
    private static final String VALUE_2 = "value2";
    private static final String SPEC_NAME = "some_spec";
    private static final String SCHEMA_NAME = "some_schema";
    private static final String TABLE_NAME = "some_table";
    private static final String COLUMN_NAME = "some_column";

    private DbSpec myDbSpec;
    private DbSchema myDbSchema;
    private DbTable myDbTable;
    private DbColumn myColumn;

    @BeforeEach
    public void setup(){
        myDbSpec = new DbSpec(SPEC_NAME);
        myDbSchema = new DbSchema(myDbSpec, SCHEMA_NAME);
        myDbTable = new DbTable(myDbSchema, TABLE_NAME);
        myColumn = new DbColumn(myDbTable, COLUMN_NAME, "VARCHAR", 10);
    }

    @Test
    public void toEqualToOrInPredicate_withNoValueParameters_returnsFalseCondition(){
        Condition result = QueryParameterUtils.toEqualToOrInPredicate(myColumn, Collections.EMPTY_LIST);
        assertEquals(CustomCondition.class, result.getClass());
        String expected = String.format("(%s)", QueryParameterUtils.FALSE_CONDITION);
        assertEquals(expected, result.toString());
    }

    @Test
    public void toEqualToOrInPredicate_withSingleParameter_returnBinaryEqualsCondition(){
        Condition result = QueryParameterUtils.toEqualToOrInPredicate(myColumn, List.of(VALUE_1));
        assertEquals(BinaryCondition.class, result.getClass());
        String expected = String.format("(%s0.%s = '%s')", SPEC_NAME, COLUMN_NAME, VALUE_1);
        assertEquals(expected, result.toString());
    }

    @Test
    public void toEqualToOrInPredicate_withMultipleParameters_returnsInCondition(){
        Condition result = QueryParameterUtils.toEqualToOrInPredicate(myColumn, List.of(VALUE_1, VALUE_2));
        assertEquals(InCondition.class, result.getClass());
        String expected = String.format("(%s0.%s IN ('%s','%s') )", SPEC_NAME, COLUMN_NAME, VALUE_1, VALUE_2);
        assertEquals(expected, result.toString());
    }

    @Test
    public void toNotEqualToOrNotInPredicate_withNoValueParameters_returnsTrueCondition(){
        Condition result = QueryParameterUtils.toNotEqualToOrNotInPredicate(myColumn, Collections.EMPTY_LIST);
        assertEquals(CustomCondition.class, result.getClass());
        String expected = String.format("(%s)", QueryParameterUtils.TRUE_CONDITION);
        assertEquals(expected, result.toString());
    }

    @Test
    public void toNotEqualToOrNotInPredicate_withSingleParameter_returnBinaryNotEqualsCondition(){
        Condition result = QueryParameterUtils.toNotEqualToOrNotInPredicate(myColumn, List.of(VALUE_1));
        assertEquals(BinaryCondition.class, result.getClass());
        String expected = String.format("(%s0.%s <> '%s')", SPEC_NAME, COLUMN_NAME, VALUE_1);
        assertEquals(expected, result.toString());
    }

    @Test
    public void toNotEqualToOrNotInPredicate_withMultipleParameters_returnsNotInCondition(){
        Condition result = QueryParameterUtils.toNotEqualToOrNotInPredicate(myColumn, List.of(VALUE_1, VALUE_2));
        assertEquals(InCondition.class, result.getClass());
        String expected = String.format("(%s0.%s NOT IN ('%s','%s') )", SPEC_NAME, COLUMN_NAME, VALUE_1, VALUE_2);
        assertEquals(expected, result.toString());
    }

}
