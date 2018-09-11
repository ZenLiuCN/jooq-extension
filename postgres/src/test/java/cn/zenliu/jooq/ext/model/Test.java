/*
 * Copyright (c) 2018.
 * Authored By Zen.Liu
 */

/*
 * This file is generated by jOOQ.
 */
package cn.zenliu.jooq.ext.model;


import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Test extends SchemaImpl {

    private static final long serialVersionUID = 1828681099;

    /**
     * The reference instance of <code>test</code>
     */
    public static final Test TEST = new Test();

    /**
     * The table <code>test.test</code>.
     */
    public final cn.zenliu.jooq.ext.model.tables.Test TEST_ = cn.zenliu.jooq.ext.model.tables.Test.TEST_;

    /**
     * No further instances allowed
     */
    private Test() {
        super("test", null);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        List result = new ArrayList();
        result.addAll(getTables0());
        return result;
    }

    private final List<Table<?>> getTables0() {
        return Arrays.<Table<?>>asList(
            cn.zenliu.jooq.ext.model.tables.Test.TEST_);
    }
}