/*
 * Copyright (C) 2013 SOP4J
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
package com.sop4j.dbutils.handlers;

import java.sql.SQLException;
import java.util.Map;

import com.sop4j.dbutils.BaseTestCase;
import com.sop4j.dbutils.ResultSetHandler;

/**
 * MapHandlerTest
 */
public class MapHandlerTest extends BaseTestCase {

    public void testHandle() throws SQLException {
        ResultSetHandler<Map<String,Object>> h = new MapHandler();
        Map<String,Object> results = h.handle(this.rs);

        assertNotNull(results);
        assertEquals(COLS, results.keySet().size());
        assertEquals("1", results.get("ONE"));
        assertEquals("2", results.get("two"));
        assertEquals("3", results.get("Three"));
    }

    public void testEmptyResultSetHandle() throws SQLException {
        ResultSetHandler<Map<String,Object>> h = new MapHandler();
        Map<String,Object> results = h.handle(this.emptyResultSet);

        assertNull(results);
    }

}