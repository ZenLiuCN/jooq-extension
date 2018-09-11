/*
 * Copyright (c) 2018.
 * Authored By Zen.Liu
 */

package cn.zenliu.jooq.ext.mysql

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.jooq.*
import org.jooq.impl.DSL
import java.sql.SQLFeatureNotSupportedException
import java.sql.Types
import java.util.*

/**
 * Jooq binding for jsonToJacksonNode
 * @property jackson2ObjectMapper ObjectMapper
 * @constructor
 */
//class JsonJsonNodeBinding(val jackson2ObjectMapper: ObjectMapper) : Binding<Any, JsonNode> {
class JsonJsonNodeBinding(val jackson2ObjectMapper: ObjectMapper=ObjectMapper()) : Binding<String, JsonNode> {
    override fun register(ctx: BindingRegisterContext<JsonNode>) {
        ctx.statement().registerOutParameter(ctx.index(), Types.VARCHAR)
    }

    override fun sql(ctx: BindingSQLContext<JsonNode>) {
        ctx.render().sql("cast(").visit(DSL.`val`(
            ctx.convert(converter()))
        ).sql("as json)")
    }

    override fun converter(): Converter<String, JsonNode> = Converter
        .of(
            String::class.java,
            JsonNode::class.java,
            {
                jackson2ObjectMapper.readTree(jackson2ObjectMapper.writeValueAsBytes(it))
            },
            {
                jackson2ObjectMapper.readValue(it.toString(), String::class.java)
            }
        )

    override fun get(ctx: BindingGetResultSetContext<JsonNode>) {
        ctx.convert(converter()).value(ctx.resultSet().getString(ctx.index()))
    }

    override fun get(ctx: BindingGetStatementContext<JsonNode>) {
        ctx.convert(converter()).value(ctx.statement().getString(ctx.index()))
    }

    override fun get(ctx: BindingGetSQLInputContext<JsonNode>?) {
        throw SQLFeatureNotSupportedException()
    }

    override fun set(ctx: BindingSetStatementContext<JsonNode>) {
        ctx.statement().setString(ctx.index(), Objects.toString(ctx.convert(converter()).value(), null))
    }

    override fun set(ctx: BindingSetSQLOutputContext<JsonNode>) {
        throw  SQLFeatureNotSupportedException()
    }

}
