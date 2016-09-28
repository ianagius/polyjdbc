/*
 * Copyright 2013 Adam Dubiel, Przemek Hertel.
 *
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
 */

package org.polyjdbc.core.schema;

import org.polyjdbc.core.integration.DatabaseTest;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.*;

/**
 *
 * @author Adam Dubiel
 */
@Test(groups = "integration")
public class SchemaInspectorImplTest extends DatabaseTest {

    @Test
    public void shouldReturnTrueWhenRelationExists() {

        // given
        SchemaInspector inspector = new SchemaInspectorImpl(transaction());

        // when
        boolean exists = inspector.relationExists("test");
        inspector.close();

        // then
        assertThat(exists).isTrue();
    }

    @Test
    public void shouldReturnFalseWhenAskingForUnexistentRelation() {
        // given
        SchemaInspector inspector = new SchemaInspectorImpl(transaction());

        // when
        boolean exists = inspector.relationExists("unknown");
        inspector.close();

        // then
        assertThat(exists).isFalse();
    }
    
    @Test
    public void shouldReturnTrueWhenIndexExists() {
        // given
        SchemaInspector inspector = new SchemaInspectorImpl(transaction());

        // when
        boolean exists = inspector.indexExists("test", "idx_test_pseudo");
        inspector.close();
//        h2DatabaseInterface();

        // then
        assertThat(exists).isTrue();
    }

    @Test
    public void shouldReturnFalseWhenAskingForUnexistentIndex() {
        // given
        SchemaInspector inspector = new SchemaInspectorImpl(transaction());

        // when
        boolean exists = inspector.indexExists("test", "unknown");
        inspector.close();

        // then
        assertThat(exists).isFalse();
    }
}