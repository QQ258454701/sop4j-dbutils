/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.dbutils2;

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


public class BatchInsertExecutorTest {

    private BatchInsertExecutor executor;
    
    @Mock private ResultSetHandler<Object> handler;
    @Mock private Connection conn;
    @Mock private PreparedStatement stmt;
    @Mock private ResultSet resultSet;

    @Before
    public void setup() throws SQLException {
        MockitoAnnotations.initMocks(this);
        
        when(conn.prepareStatement(any(String.class))).thenReturn(stmt);
        when(stmt.getGeneratedKeys()).thenReturn(resultSet);
        when(handler.handle(any(ResultSet.class))).thenReturn(new Object());
    }
    
    protected void createExecutor(String sql) throws Exception {
        executor = new BatchInsertExecutor(conn, sql, true);
    }
    
    @Test
    public void testGoodSQL() throws Exception {
        createExecutor("insert into blah");
        
        executor.addBatch();
        Object ret = executor.execute(handler);
        
        assertNotNull(ret);
        verify(handler, times(1)).handle(resultSet);
        verify(conn, times(1)).close();
        verify(stmt, times(1)).close();
    }
    
}
