package ca.uhn.fhir.jpa.search.builder.sql;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class GeneratedSqlTest {

    @Test
    public void testBlockInlineNonBoundParameters() {
        assertThrows(
                AssertionError.class,
                () ->
                        new GeneratedSql(
                                false,
                                "SELECT * FROM FOO WHERE t = '123'",
                                Collections.emptyList()));
        assertThrows(
                AssertionError.class,
                () ->
                        new GeneratedSql(
                                false, "SELECT * FROM FOO WHERE t='123'", Collections.emptyList()));
        assertThrows(
                AssertionError.class,
                () ->
                        new GeneratedSql(
                                false,
                                "SELECT * FROM FOO WHERE t in ('123')",
                                Collections.emptyList()));
        assertThrows(
                AssertionError.class,
                () ->
                        new GeneratedSql(
                                false,
                                "SELECT * FROM FOO WHERE t IN ('123')",
                                Collections.emptyList()));
        assertThrows(
                AssertionError.class,
                () ->
                        new GeneratedSql(
                                false,
                                "SELECT * FROM FOO WHERE t IN('123')",
                                Collections.emptyList()));
    }
}
