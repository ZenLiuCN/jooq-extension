/*
 * Copyright (c) 2018.
 * Authored By Zen.Liu
 */

package cn.zenliu.jooq.ext.postgres

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.jooq.Field
import org.jooq.impl.DSL.field



object JsonbDSL {
    private var mapper: ObjectMapper? = null
    /**
     * Before use should set Jackson ObjectMapper
     * @param m [ObjectMapper]
     */
    fun setMapper(m: ObjectMapper) {
        this.mapper = m
    }

    fun jsonb(node: Field<JsonNode>, path:String, vararg more:Any)=field(
        more.joinToString ("->",prefix = "{?} -> {?}"){ "{?}" },
        JsonNode::class,
        node,
        *more.toMutableList().apply { add(0,path) }.toTypedArray()
    )

    class JsonMapperErrorException(message: String?="mapper not set!" , throwable:Throwable?=null ) : Exception(message,throwable)
}
