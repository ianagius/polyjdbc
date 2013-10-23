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
package org.polyjdbc.core.query;

import java.util.List;
import java.util.Set;
import org.polyjdbc.core.query.mapper.ObjectMapper;

/**
 * Runs queries and processes the results inside single transaction. QueryRunner
 * should live only as long as transaction is open. Calls to multiple methods
 * are allowed inside single transaction. QueryRunner should be always closed, if
 * not resource leaks are possible! On any exception transaction is rolled back.
 *
 * <pre>
 * QueryRunner runner = (...)
 * try {
 *      runner.queryList(...);
 * }
 * finally {
 *      runner.close();
 * }
 * </pre>
 *
 * To get rid of try-finally see {@link SimpleQueryRunner} or {@link TransactionRunner}.
 *
 * @author Adam Dubiel
 */
public interface QueryRunner {

    /**
     * Return single object retrieved using query and mapped to Java using mapper.
     * Should throw {@link org.polyjdbc.core.exception.NonUniqueException}:
     * <ul>
     * <li>with NO_ITEM_FOUND code when no item was found</li>
     * <li>with NON_UNIQE_ITEM code when more than one item returned</li>
     * </ul>
     * To suppress exception see {@link #queryUnique(org.polyjdbc.core.query.SelectQuery, org.polyjdbc.core.query.mapper.ObjectMapper, boolean) }.
     */
    <T> T queryUnique(SelectQuery query, ObjectMapper<T> mapper);

    /**
     * Return single object retrieved using query and mapped to Java using mapper,
     * will suppress any {@link org.polyjdbc.core.exception.NonUniqueException} exceptions
     * if failOnNotUniqueOrNotFound set to false.
     * If exceptions are suppressed method will return null if nothing found and
     * first item from the list if more than one found. Will fetch at most
     * 2 items from database - never more.
     */
    <T> T queryUnique(SelectQuery query, ObjectMapper<T> mapper, boolean failOnNotUniqueOrNotFound);

    /**
     * Return list of items or empty list if none found.
     */
    <T> List<T> queryList(SelectQuery query, ObjectMapper<T> mapper);

    /**
     * Return set of items or empty set if none found.
     */
    <T> Set<T> querySet(SelectQuery query, ObjectMapper<T> mapper);

    /**
     * Returns true if at least one item was returned by query.
     */
    boolean queryExistence(SelectQuery query);

    /**
     * Run insert query and return newly created object id (generated by
     * sequence or autoincrement).
     */
    long insert(InsertQuery insertQuery);

    /**
     * Run insert query without trying to generate key - entity should have
     * one defined already.
     */
    void insertWithoutKey(InsertQuery insertQuery);

    /**
     * Run delete query, returns number of records deleted.
     */
    int delete(DeleteQuery deleteQuery);

    /**
     * Commit transaction at this point.
     */
    void commit();

    /**
     * Rollback any actions since last commit.
     */
    void rollback();

    /**
     * Rollback actions and close transaction, freeing all resources.
     */
    void rollbackAndClose();

    /**
     * Commit and close transaction.
     */
    void close();
}
