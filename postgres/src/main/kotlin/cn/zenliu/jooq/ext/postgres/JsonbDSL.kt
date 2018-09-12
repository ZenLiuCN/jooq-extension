/*
 * Copyright (c) 2018.
 * Authored By Zen.Liu
 */

package cn.zenliu.jooq.ext.postgres

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.jooq.Allow
import org.jooq.Field
import org.jooq.SQLDialect
import org.jooq.impl.DSL

@Allow(SQLDialect.POSTGRES)
object JsonDSL : DSL() {
    var mapper: ObjectMapper? = null


    inline fun <reified N, K : Any, V : Any> entityOperatorFactory(clazz: Class<N>, oper: String, node: Field<JsonNode>, entity: Pair<K, V>, vararg more: Pair<K, V>) = field(
            more.joinToString(",", prefix = if (more.isEmpty()) "$oper( ? , ? , ?" else "$oper( ? , ? , ? ,", postfix = ")") { " ? , ? " },
            clazz,
            node,
            *more.toMutableList().apply { add(entity) }.flatMap { listOf(it.first, jsonbVal(it.second)) }.toTypedArray())

    inline fun <reified N> pathOperatorFactory(clazz: Class<N>, oper: String, node: Field<JsonNode>, entity: String, vararg more: String) = field(
            more.joinToString(",", prefix = if (more.isEmpty()) "$oper( ? , ? " else "$oper( ? , ? , ", postfix = ")") { " ? " },
            clazz,
            node,
            *more.toMutableList().apply { add(entity) }.map { it }.toTypedArray())

    @JvmStatic
    fun jsonb(node: Field<JsonNode>, vararg paths: Any) = field(
            paths.joinToString("->", prefix = " ? ") {
                when (it) {
                    is String -> " '?' "
                    is Int -> " ? "
                    else -> throw JsonbPathErrorException(it)
                }
            },
            JsonNode::class.java,
            node,
            * paths
    )

    @JvmStatic
    fun txt(node: Field<JsonNode>, vararg paths: Any) = field(
            paths.joinToString("->>", prefix = " ? ") {
                when (it) {
                    is String -> " '?' "
                    is Int -> " ? "
                    else -> throw JsonbPathErrorException(it)
                }
            },
            String::class.java,
            node,
            * paths
    )

    @JvmStatic
    fun extract(node: Field<JsonNode>, vararg paths: Any) = field(
            "? #> ?",
            JsonNode::class.java,
            node,
            toPaths(paths)
    )

    @JvmStatic
    fun extractTxt(node: Field<JsonNode>, vararg paths: Any) = field(
            "? #>> ?",
            JsonNode::class.java,
            node,
            toPaths(paths)
    )

    @JvmStatic
    fun contains(node: Field<JsonNode>, value: Any) = field(
            "? @> ?",
            Boolean::class.java,
            node,
            jsonbVal(value)
    )

    @JvmStatic
    fun containsBy(node: Field<JsonNode>, value: Any) = field(
            "? <@ ?",
            Boolean::class.java,
            node,
            jsonbVal(value)
    )

    @JvmStatic
    fun containsKey(node: Field<JsonNode>, value: Any) = field(
            "{?} ? '{?}'",
            Boolean::class.java,
            node,
            jsonbVal(value)
    )

    @JvmStatic
    fun containsAnyKey(node: Field<JsonNode>, vararg keys: Any) = field(
            "{?} ?| {?}",
            Boolean::class.java,
            node,
            toArray(keys)
    )

    @JvmStatic
    fun containsAllKeys(node: Field<JsonNode>, vararg keys: Any) = field(
            "{?} ?& {?}",
            Boolean::class.java,
            node,
            toArray(keys)
    )

    @JvmStatic
    fun join(node: Field<JsonNode>, node1: Field<JsonNode>) = field(
            "{?} || {?}",
            Boolean::class.java,
            node,
            node1
    )

    @JvmStatic
    fun join(node: Field<JsonNode>, value: Any) = field(
            "{?} || {?}",
            Boolean::class.java,
            node,
            jsonbVal(value)
    )

    @JvmStatic
    fun remove(node: Field<JsonNode>, key: Any) = field(
            "{?} - {?}",
            Boolean::class.java,
            node,
            toPath(key)
    )

    @JvmStatic
    fun toJsonb(node: Field<String>) = field(
            "? ::JSONB",
            JsonNode::class.java,
            node
    )

    @JvmStatic
    fun toJsonbOfStr(value: String) = "'$value'::jsonb"

    @JvmStatic
    fun jsonbVal(a: Any, tojson: Boolean = true, convert: Boolean = true): Any? = when (a) {
        is String -> DSL.escape(a, '\\')
        is Number -> a
        else -> when {
            !convert -> a
            tojson -> "'${DSL.escape(mapper?.writeValueAsString(a)
                    ?: throw JsonbMapperNotSetException(), '\\')}'::JSONB"
            else -> DSL.escape(mapper?.writeValueAsString(a) ?: throw JsonbMapperNotSetException(), '\\')
        }
    }

    @JvmStatic
    fun toPaths(vararg path: Any) = path.joinToString(",", prefix = "{", postfix = "}") {
        when (it) {
            is String -> " $it "
            is Number -> " $it "
            else -> throw JsonbPathErrorException(it)
        }
    }

    @JvmStatic
    fun toPathsPlaceHolder(vararg path: Any) = path.joinToString(",", prefix = "{", postfix = "}") {
        when (it) {
            is String -> " ? "
            is Number -> " ? "
            else -> throw JsonbPathErrorException(it)
        }
    }

    @JvmStatic
    fun toPath(path: Any) = when (path) {
        is String -> " '$path' "
        is Number -> " $path "
        else -> throw JsonbPathErrorException(path)
    }

    @JvmStatic
    fun toArray(vararg path: Any) = path.joinToString(",", prefix = "Array[", postfix = "]") {
        when (it) {
            is String -> " '$it' "
            is Number -> " $it "
            else -> throw JsonbPathErrorException(it)
        }
    }

    class JsonbMapperNotSetException(message: String? = "mapper not set!", throwable: Throwable? = null) : Exception(message, throwable)
    class JsonbPathErrorException(value: Any, message: String? = "Jsonb path value not vaild: $value of ${value::class.java.simpleName}", throwable: Throwable? = null) : Exception(message, throwable)

}


fun Field<JsonNode>.jsonb(vararg paths: String) = JsonDSL.jsonb(this, *paths)
fun Field<JsonNode>.txt(vararg paths: String) = JsonDSL.txt(this, *paths)
