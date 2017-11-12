package ca.uhn.fhir.jpa.cqf.ruler.omtk;

import org.opencds.cqf.cql.data.DataProvider;
import org.opencds.cqf.cql.runtime.Code;
import org.opencds.cqf.cql.runtime.Interval;

import java.math.BigDecimal;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Bryn on 4/24/2017.
 */
public class OmtkDataProvider implements DataProvider {

    public static final String RXNORM = "http://www.nlm.nih.gov/research/umls/rxnorm";

    public OmtkDataProvider(String connectionString) {
        if (connectionString == null) {
            throw new IllegalArgumentException("connectionString is null");
        }

        this.connectionString = connectionString;
    }

    private String connectionString;

    private java.sql.Connection connection;
    private java.sql.Connection getConnection() {
        if (connection == null) {
            connection = getNewConnection();
        }

        try {
            if (!connection.isValid(0)) {
                connection = getNewConnection();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        return connection;
    }

    private java.sql.Connection getNewConnection() {
        try {
            return DriverManager.getConnection(connectionString);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Iterable<Object> retrieve(String context, Object contextValue, String dataType, String templateId,
                                     String codePath, Iterable<Code> codes, String valueSet, String datePath,
                                     String dateLowPath, String dateHighPath, Interval dateRange) {

        java.sql.Statement statement = null;
        try {
            statement = getConnection().createStatement();

            // TODO: Construct the SELECT statement based on the code path
            // TODO: Throw an error if an attempt is made to limit based on date range
            StringBuilder select = new StringBuilder();
            select.append(String.format("SELECT * FROM %s", dataType));
            if (codePath != null) {
                StringBuilder codeList = new StringBuilder();
                boolean plural = false;
                for (Code code : codes) {
                    if (codeList.length() > 0) {
                        codeList.append(", ");
                        plural = true;
                    }
                    codeList.append(code.getCode()); // TODO: Need to handle the case when code is a string type...
                }

                if (plural) {
                    select.append(String.format(" WHERE %s IN ( %s )", codePath, codeList.toString()));
                }
                else {
                    select.append(String.format(" WHERE %S = %s", codePath, codeList.toString()));
                }
            }

            if (datePath != null) {
                throw new UnsupportedOperationException("OmtkDataProvider does not support filtering by date range.");
            }

            java.sql.ResultSet rs = statement.executeQuery(select.toString());
            return new OmtkDataWrapper(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public String getPackageName() {
        return "ca.uhn.fhir.jpaca.uhn.fhir.jpa.cqf.ruler.omtk";
    }

    @Override
    public void setPackageName(String s) {

    }

    @Override
    public Object resolvePath(Object target, String path) {
        if (target == null) {
            return null;
        }

        if (target instanceof OmtkRow) {
            OmtkRow row = (OmtkRow)target;
            return mapType(row.getValue(path), path);
        }

        throw new UnsupportedOperationException(String.format("Could not retrieve value of property %s from object of type %s.",
                path, target.getClass().getName()));
    }

    @Override
    public Class resolveType(String typeName) {
        throw new UnsupportedOperationException("OmtkProvider does not support write.");
    }

    @Override
    public Class resolveType(Object o) {
        throw new UnsupportedOperationException("OmtkProvider does not support write.");
    }

    @Override
    public Object createInstance(String s) {
        throw new UnsupportedOperationException("OmtkProvider does not support write.");
    }

    @Override
    public void setValue(Object target, String path, Object value) {
        throw new UnsupportedOperationException("OmtkProvider does not support write.");
    }

    private Object mapType(Object type, String path) {
        // not all integers are codes
        if (path.equals("STRENGTH_VALUE")) {
            return new BigDecimal(type.toString());
        }

        if (type instanceof Double) {
            return new BigDecimal((Double) type);
        }

        else if (type instanceof Integer) {
            return new Code().withCode(type.toString()).withSystem("http://www.nlm.nih.gov/research/umls/rxnorm");
        }

        return type;
    }
}
