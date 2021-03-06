/*
 * Copyright (c) 2018.
 * Authored By Zen.Liu
 */

/*
 * This file is generated by jOOQ.
 */
package cn.zenliu.jooq.ext.model;


import cn.zenliu.jooq.ext.model.tables.records.TestRecord;
import cn.zenliu.jooq.ext.model.tables.Test;
import org.jooq.Identity;
import org.jooq.UniqueKey;
import org.jooq.impl.Internal;

import javax.annotation.Generated;


/**
 * A class modelling foreign key relationships and constraints of tables of
 * the <code>test</code> schema.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // IDENTITY definitions
    // -------------------------------------------------------------------------

    public static final Identity<TestRecord, Integer> IDENTITY_TEST_ = Identities0.IDENTITY_TEST_;

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<TestRecord> KEY_TEST_PRIMARY = UniqueKeys0.KEY_TEST_PRIMARY;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------


    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Identities0 {
        public static Identity<TestRecord, Integer> IDENTITY_TEST_ = Internal.createIdentity(Test.TEST_, Test.TEST_.ID);
    }

    private static class UniqueKeys0 {
        public static final UniqueKey<TestRecord> KEY_TEST_PRIMARY = Internal.createUniqueKey(Test.TEST_, "KEY_test_PRIMARY", Test.TEST_.ID);
    }
}
