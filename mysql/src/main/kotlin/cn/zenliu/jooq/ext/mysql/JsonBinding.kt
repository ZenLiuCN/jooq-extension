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
 * Json to Jackson [JsonNode] mapper
 * @property mapper ObjectMapper
 * @constructor
 */
//@Allow(SQLDialect.MYSQL)
class JsonBind : Binding<Any, JsonNode> {
    override fun register(ctx: BindingRegisterContext<JsonNode>) {
        ctx.statement().registerOutParameter(ctx.index(), Types.VARCHAR)
    }

    override fun sql(ctx: BindingSQLContext<JsonNode>) {
        ctx.render().visit(DSL.`val`(ctx.convert(converter()).value()))
    }

    override fun converter(): Converter<Any, JsonNode> = Converter
            .of(
                    Any::class.java,
                    JsonNode::class.java,
                    {
                        if (JsonDSL.mapper == null) throw JsonDSL.JsonMapperErrorException()
                        JsonDSL.mapper?.readTree(
                                JsonDSL.mapper?.writeValueAsBytes(it)
                        )
                    },
                    {
                        //                        if (JsonDSL.mapper == null) throw JsonDSL.JsonMapperErrorException()
                           it?.toString() ?: NullNode.instance.toString()

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
