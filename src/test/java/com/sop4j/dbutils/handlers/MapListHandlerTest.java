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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sop4j.dbutils.BaseTestCase;
import com.sop4j.dbutils.ResultSetHandler;

/**
 * MapListHandlerTest
 */
public class MapListHandlerTest extends BaseTestCase {

    public void testHandle() throws SQLException {
        ResultSetHandler<List<Map<String,Object>>> h = new MapListHandler();
        List<Map<String,Object>> results = h.handle(this.rs);

        assertNotNull(results);
        assertEquals(ROWS, results.size());

        Iterator<Map<String,Object>> iter = results.iterator();
        Map<String,Object> row = null;
        assertTrue(iter.hasNext());
        row = iter.next();
        assertEquals(COLS, row.keySet().size());
        assertEquals("1", row.get("one"));
        assertEquals("2", row.get("TWO"));
        assertEquals("3", row.get("Three"));

        assertTrue(iter.hasNext());
        row = iter.next();
        assertEquals(COLS, row.keySet().size());

        assertEquals("4", row.get("one"));
        assertEquals("5", row.get("TWO"));
        assertEquals("6", row.get("Three"));

        assertFalse(iter.hasNext());
    }

    public void testEmptyResultSetHandle() throws SQLException {
        ResultSetHandler<List<Map<String,Object>>> h = new MapListHandler();
        List<Map<String,Object>> results = h.handle(this.emptyResultSet);

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

}