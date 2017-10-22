package org.utplsql.api;

import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Database utility functions.
 */
public final class DBHelper {

    public static final String UTPLSQL_VERSION = "3.0.3";

    private DBHelper() {}

    /**
     * Return a new sys_guid from database.
     * @param conn the connection
     * @return the new id string
     * @throws SQLException any database error
     */
    public static String newSysGuid(Connection conn) throws SQLException {
        CallableStatement callableStatement = null;
        try {
            callableStatement = conn.prepareCall("BEGIN ? := sys_guid(); END;");
            callableStatement.registerOutParameter(1, OracleTypes.RAW);
            callableStatement.executeUpdate();
            return callableStatement.getString(1);
        } finally {
            if (callableStatement != null)
                callableStatement.close();
        }
    }

    /**
     * Return the current schema name.
     * @param conn the connection
     * @return the schema name
     * @throws SQLException any database error
     */
    public static String getCurrentSchema(Connection conn) throws SQLException {
        CallableStatement callableStatement = null;
        try {
            callableStatement = conn.prepareCall("BEGIN ? := sys_context('userenv', 'current_schema'); END;");
            callableStatement.registerOutParameter(1, Types.VARCHAR);
            callableStatement.executeUpdate();
            return callableStatement.getString(1);
        } finally {
            if (callableStatement != null)
                callableStatement.close();
        }
    }

    /**
     * Check the utPLSQL version compatibility.
     * @param conn the connection
     * @return true if the requested utPLSQL version is compatible with the one installed on database
     * @throws SQLException any database error
     */
    public static boolean versionCompatibilityCheck(Connection conn, String requested, String current)
            throws SQLException {
        CallableStatement callableStatement = null;
        try {
            callableStatement = conn.prepareCall("BEGIN ? := ut_runner.version_compatibility_check(?, ?); END;");
            callableStatement.registerOutParameter(1, Types.SMALLINT);
            callableStatement.setString(2, requested);

            if (current == null)
                callableStatement.setNull(3, Types.VARCHAR);
            else
                callableStatement.setString(3, current);

            callableStatement.executeUpdate();
            return callableStatement.getInt(1) == 1;
        } catch (SQLException e) {
            if (e.getErrorCode() == 6550)
                return false;
            else
                throw e;
        } finally {
            if (callableStatement != null)
                callableStatement.close();
        }
    }

    public static boolean versionCompatibilityCheck(Connection conn, String requested)
            throws SQLException {
        return versionCompatibilityCheck(conn, requested, null);
    }

    public static boolean versionCompatibilityCheck(Connection conn)
            throws SQLException {
        return versionCompatibilityCheck(conn, UTPLSQL_VERSION);
    }

    /**
     * Enables the dbms_output buffer.
     * @param conn the connection
     * @param bufferLen the buffer length
     */
    public static void enableDBMSOutput(Connection conn, int bufferLen) {
        try (CallableStatement call = conn.prepareCall("BEGIN dbms_output.enable(?); END;")) {
            call.setInt(1, bufferLen);
            call.execute();
        } catch (SQLException e) {
            System.out.println("Failed to enable dbms_output.");
        }
    }

    /**
     * Enables the dbms_output buffer.
     * @param conn the connection
     */
    public static void enableDBMSOutput(Connection conn) {
        enableDBMSOutput(conn, Integer.MAX_VALUE);
    }

    /**
     * Disables the dbms_output buffer.
     * @param conn the connection
     */
    public static void disableDBMSOutput(Connection conn) {
        try (CallableStatement call = conn.prepareCall("BEGIN dbms_output.disable(); END;")) {
            call.execute();
        } catch (SQLException e) {
            System.out.println("Failed to disable dbms_output.");
        }
    }

}
