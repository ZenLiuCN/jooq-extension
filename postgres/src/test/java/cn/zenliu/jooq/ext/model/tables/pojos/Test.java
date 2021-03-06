/*
 * Copyright (c) 2018.
 * Authored By Zen.Liu
 */

/*
 * This file is generated by jOOQ.
 */
package cn.zenliu.jooq.ext.model.tables.pojos;


import com.fasterxml.jackson.databind.JsonNode;

import javax.annotation.Generated;
import java.io.Serializable;


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
public class Test implements Serializable {

    private static final long serialVersionUID = -1630230259;

    private final Integer  id;
    private final JsonNode json;

    public Test(Test value) {
        this.id = value.id;
        this.json = value.json;
    }

    public Test(
        Integer  id,
        JsonNode json
    ) {
        this.id = id;
        this.json = json;
    }

    public Integer getId() {
        return this.id;
    }

    public JsonNode getJson() {
        return this.json;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Test (");

        sb.append(id);
        sb.append(", ").append(json);

        sb.append(")");
        return sb.toString();
    }
}
