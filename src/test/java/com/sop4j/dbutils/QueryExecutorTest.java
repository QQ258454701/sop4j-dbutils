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

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class QueryExecutorTest {

    private QueryExecutor executor;

    @Mock private ResultSetHandler<Object> handler;
    @Mock private Connection conn;
    @Mock private PreparedStatement stmt;
    @Mock private ResultSet resultSet;

    @Before
    public void setup() throws SQLException {
        MockitoAnnotations.initMocks(this);

        when(conn.prepareStatement(any(String.class))).thenReturn(stmt);
        when(conn.prepareStatement(any(String.class), any(Integer.class))).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(resultSet);
        when(handler.handle(any(ResultSet.class))).thenReturn(new Object());
    }

    protected void createExecutor(String sql) throws Exception {
        executor = new QueryExecutor(conn, sql, true);
    }

    @Test
    public void testGoodSQL() throws Exception {
        createExecutor("insert into blah");

        Object ret = executor.execute(handler);

        assertNotNull(ret);
        verify(handler, times(1)).handle(resultSet);
        verify(conn, times(1)).close();
        verify(stmt, times(1)).close();
    }

    @Test(expected=SQLException.class)
    public void testUnmappedParams() throws Exception {
        createExecutor("insert into blah (:something)");

        Object ret = executor.execute(handler);

        assertNotNull(ret);
        verify(handler, times(1)).handle(resultSet);
        verify(conn, times(1)).close();
        verify(stmt, times(1)).close();
    }

    @Test(expected=SQLException.class)
    public void testNullHandler() throws Exception {
        createExecutor("insert into blah");

        Object ret = executor.execute(null);

        assertNotNull(ret);
        verify(handler, times(1)).handle(resultSet);
        verify(conn, times(1)).close();
        verify(stmt, times(1)).close();
    }
}
