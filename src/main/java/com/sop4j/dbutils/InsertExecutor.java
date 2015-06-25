/*
 * Copyright (C) 2014 SOP4J
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sop4j.dbutils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Fluent class for executing inserts.
 *
 * @since 2.0
 */
public class InsertExecutor extends AbstractExecutor<InsertExecutor> {

    private final boolean closeConn;

    /**
     * Constructs an InsertExecutor given a connection and SQL statement.
     *
     * @param conn The connection to use during execution.
     * @param sql The SQL statement.
     * @param closeConnection If the connection should be closed or not.
     * @throws SQLException thrown if there is an error during execution.
     */
    InsertExecutor(final Connection conn, final String sql, final boolean closeConnection) throws SQLException {
        super(conn, sql, Statement.RETURN_GENERATED_KEYS);
        this.closeConn = closeConnection;
    }

    /**
     * Executes the given INSERT SQL statement.
     *
     * @param <T> the type returned by the ResultSetHandler.
     * @param handler The handler used to create the result object from
     * the <code>ResultSet</code> of auto-generated keys.
     *
     * @return An object generated by the handler.
     * @throws SQLException If there are database or parameter errors.
     */
    public <T> T execute(ResultSetHandler<T> handler) throws SQLException {
        // throw an exception if there are unmapped parameters
        this.throwIfUnmappedParams();

        // make sure our handler is not null
        if (handler == null) {
            if (closeConn) {
                close(getConnection());
            }
            throw new SQLException("Null ResultSetHandler");
        }

        try {
            // execute the update
            getStatement().executeUpdate();

            // get the result set
            final ResultSet resultSet = getStatement().getGeneratedKeys();

            // run the handler over the results and return them
            return handler.handle(resultSet);
        } catch (SQLException e) {
            this.rethrow(e);
        } finally {
            close(getStatement());
            if (closeConn) {
                close(getConnection());
            }
        }

        // we get here only if something is thrown
        return null;
    }

    /**
     * Executes the given INSERT SQL statement.
     *
     * @return the number of rows updated.
     * @throws SQLException If there are database or parameter errors.
     */
    public int execute() throws SQLException {
        // throw an exception if there are unmapped parameters
        this.throwIfUnmappedParams();

        try {
            // execute the insert
            int ret = getStatement().executeUpdate();

            // get any generated keys, and just close the ResultSet
            getStatement().getGeneratedKeys().close();

            return ret;
        } catch (SQLException e) {
            this.rethrow(e);
        } finally {
            close(getStatement());
            if (closeConn) {
                close(getConnection());
            }
        }

        return 0; // only get here on an error
    }

}
