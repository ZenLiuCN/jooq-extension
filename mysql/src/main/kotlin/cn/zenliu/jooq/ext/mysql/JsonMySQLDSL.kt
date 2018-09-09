/*
 * Copyright (c) 2018.
 * Authored By Zen.Liu
 */

package cn.zenliu.jooq.ext.mysql

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.jooq.*
import org.jooq.impl.DSL
import org.jooq.impl.DSL.field


object JsonMySQLDSL {
    private var mapper: ObjectMapper? = null
    //<editor-fold desc="Inner function Factory">

    inline fun <reified N, K : Any, V : Any> entityOperatorFactory(clazz: Class<N>, oper: String, node: Field<JsonNode>, entity: Pair<K, V>, vararg more: Pair<K, V>) = field(
        more.joinToString(",", prefix = if (more.isEmpty()) "$oper( ? , ? , ?" else "$oper( ? , ? , ? ,", postfix = ")") { " ? , ? " },
        clazz,
        node,
        *more.toMutableList().apply { add(entity) }.flatMap { listOf(pathCheck(pathJoin(it.first)), json_val(it.second)) }.toTypedArray())

    inline fun <reified N, V : Any> pathOperatorFactory(clazz: Class<N>, oper: String, node: Field<JsonNode>, entity: V, vararg more: V) = field(
        more.joinToString(",", prefix = if (more.isEmpty()) "$oper( ? , ? " else "$oper( ? , ? , ", postfix = ")") { " ? " },
        clazz,
        node,
        *more.toMutableList().apply { add(entity) }.map { pathCheck(pathJoin(it)) }.toTypedArray())

    inline fun <reified N, V : Any> pathEmptyableOperatorFactory(clazz: Class<N>, oper: String, node: Field<JsonNode>, vararg more: V) = field(
        more.joinToString(",", prefix = if (more.isEmpty()) "$oper( ?  " else "$oper( ? ,", postfix = ")") { " ? " },
        clazz,
        node,
        *more.toMutableList().map { pathCheck(pathJoin(it)) }.toTypedArray())

    inline fun <reified N, V : Any> valueEmptyableOperatorFactory(tojson: Boolean, convert: Boolean, clazz: Class<N>, oper: String, node: Field<JsonNode>, vararg more: V) = field(
        more.joinToString(",", prefix = if (more.isEmpty()) "$oper( ?  " else "$oper( ? ,", postfix = ")") { " ? " },
        clazz,
        node,
        *more.toMutableList().map { json_val(it, tojson, convert) }.toTypedArray())

    inline fun <reified N, V : Any> valueOperatorFactory(tojson: Boolean, convert: Boolean, clazz: Class<N>, oper: String, node: Field<JsonNode>, entity: V, vararg more: V) = field(
        more.joinToString(",", prefix = if (more.isEmpty()) "$oper( ? , ? " else "$oper( ? , ? , ", postfix = ")") { " ? " },
        clazz,
        node,
        *more.toMutableList().apply { add(entity) }.map { json_val(it, tojson, convert) }.toTypedArray())

    //</editor-fold>
    //<editor-fold desc="Functions">
    @Support(SQLDialect.MYSQL_5_7, SQLDialect.MYSQL_8_0)
    @JvmStatic
    @Throws(JsonPathErrorException::class)
    fun json_remove(node: Field<JsonNode>, path: Any, vararg paths: Any) =
            pathOperatorFactory<JsonNode, Any>(JsonNode::class.java, "JSON_REMOVE", node, path, *paths)

    @Support(SQLDialect.MYSQL_5_7, SQLDialect.MYSQL_8_0)
    @JvmStatic
    @Throws(JsonPathErrorException::class)
    fun json_length(node: Field<JsonNode>, vararg paths: Any) =
            pathEmptyableOperatorFactory<JsonNode, Any>(JsonNode::class.java, "JSON_LENGTH", node, *paths)

    @Support(SQLDialect.MYSQL_5_7, SQLDialect.MYSQL_8_0)
    @JvmStatic
    @Throws(JsonPathErrorException::class)
    fun json_merge_patch(node: Field<JsonNode>, value: Any, vararg values: Any) =
            valueOperatorFactory<JsonNode, Any>(true, true, JsonNode::class.java, "JSON_MERGE_PATCH", node, value, *values)

    @Support(SQLDialect.MYSQL_5_7, SQLDialect.MYSQL_8_0)
    @JvmStatic
    @Throws(JsonPathErrorException::class)
    fun json_merge_preserve(node: Field<JsonNode>, value: Any, vararg values: Any) =
            valueOperatorFactory<JsonNode, Any>(true, true, JsonNode::class.java, "JSON_MERGE_PRESERVE", node, value, *values)

    @Support(SQLDialect.MYSQL_5_7, SQLDialect.MYSQL_8_0)
    @JvmStatic
    @Throws(JsonPathErrorException::class)
    fun json_merge_patch(node: Field<JsonNode>, value: Field<JsonNode>, vararg values: Field<JsonNode>) =
            valueOperatorFactory<JsonNode, Field<JsonNode>>(false, false, JsonNode::class.java, "JSON_MERGE_PATCH", node, value, *values)

    @Support(SQLDialect.MYSQL_5_7, SQLDialect.MYSQL_8_0)
    @JvmStatic
    @Throws(JsonPathErrorException::class)
    fun json_merge_preserve(node: Field<JsonNode>, value: Field<JsonNode>, vararg values: Field<JsonNode>) =
            valueOperatorFactory<JsonNode, Field<JsonNode>>(false, false, JsonNode::class.java, "JSON_MERGE_PRESERVE", node, value, *values)

    @Support(SQLDialect.MYSQL_5_7, SQLDialect.MYSQL_8_0)
    @JvmStatic
    @Throws(JsonPathErrorException::class)
    fun json_set(node: Field<JsonNode>, entity: Pair<String, Any>, vararg more: Pair<String, Any>) =
            entityOperatorFactory<JsonNode, String, Any>(JsonNode::class.java, "JSON_SET", node, entity, *more)

    @Support(SQLDialect.MYSQL_5_7, SQLDialect.MYSQL_8_0)
    @JvmStatic
    @Throws(JsonPathErrorException::class)
    fun json_replace(node: Field<JsonNode>, entity: Pair<String, Any>, vararg more: Pair<String, Any>) =
            entityOperatorFactory<JsonNode, String, Any>(JsonNode::class.java, "JSON_REPLACE", node, entity, *more)

    @Support(SQLDialect.MYSQL_5_7, SQLDialect.MYSQL_8_0)
    @JvmStatic
    @Throws(JsonPathErrorException::class)
    fun json_array_append(node: Field<JsonNode>, entity: Pair<String, Any>, vararg more: Pair<String, Any>) =
            entityOperatorFactory<JsonNode, String, Any>(JsonNode::class.java, "JSON_ARRAY_APPEND", node, entity, *more)

    @Support(SQLDialect.MYSQL_5_7, SQLDialect.MYSQL_8_0)
    @JvmStatic
    @Throws(JsonPathErrorException::class)
    fun json_insert(node: Field<JsonNode>, entity: Pair<String, Any>, vararg more: Pair<String, Any>) =
            entityOperatorFactory<JsonNode, String, Any>(JsonNode::class.java, "JSON_INSERT", node, entity, *more)


    @Support(SQLDialect.MYSQL_5_7, SQLDialect.MYSQL_8_0)
    @JvmStatic
    @Throws(JsonPathErrorException::class)
    fun json_extract(node: Field<JsonNode>, path: Any, vararg more: Any) = field(
        "? -> ?",
        JsonNode::class.java,
        node,
        pathCheck(pathJoin(*more.toMutableList().apply { add(path) }.toTypedArray())))


    @Support(SQLDialect.MYSQL_5_7, SQLDialect.MYSQL_8_0)
    @JvmStatic
    @Throws(JsonPathErrorException::class)
    fun json_extract_text(node: Field<JsonNode>, path: Any, vararg more: Any) = field(
        "? ->> ?",
        JsonNode::class.java, node,
        pathCheck(pathJoin(*more.toMutableList().apply { add(path) }.toTypedArray())))


    /**
     *
     * @param node [Field]<[JsonNode]>
     * @param path Any only [String] or [Int]
     * @param more Array<out Any> only [String] or [Int]
     * @return [Field]<[JsonNode]>
     * @throws JsonPathErrorException
     */


    @Support(SQLDialect.MYSQL_5_7, SQLDialect.MYSQL_8_0)
    @JvmStatic
    @Throws(JsonPathErrorException::class)
    fun json_type(node: Field<JsonNode>) = field(
        "JSON_TYPE( ? )",
        String::class.java,
        node
    )

    @Support(SQLDialect.MYSQL_5_7, SQLDialect.MYSQL_8_0)
    @JvmStatic
    @Throws(JsonPathErrorException::class)
    fun json_type(node: String) = field(
        "JSON_TYPE( ? )",
        String::class.java,
        node
    )

    @Support(SQLDialect.MYSQL_5_7, SQLDialect.MYSQL_8_0)
    @JvmStatic
    @Throws(JsonPathErrorException::class)
    fun json_valid(node: Field<JsonNode>) = field(
        "JSON_VALID( ? )",
        Boolean::class.java,
        node
    )

    @Support(SQLDialect.MYSQL_5_7, SQLDialect.MYSQL_8_0)
    @JvmStatic
    @Throws(JsonPathErrorException::class)
    fun json_valid(node: String) = field(
        "JSON_VALID( ? )",
        Boolean::class.java,
        node
    )


    @Support(SQLDialect.MYSQL_5_7, SQLDialect.MYSQL_8_0)
    @JvmStatic
    @Throws(JsonPathErrorException::class)
    fun json_contains(node: Field<JsonNode>, value: Any, path: String = "") = field(
        if (path.isNullOrBlank()) "JSON_CONTAINS( ? , ? )" else "JSON_CONTAINS( ? , ? , ?)",
        Boolean::class.java,
        node,
        * mutableListOf(json_val(value)).apply {
            if (path.isNotBlank()) {
                add(pathCheck(path))
            }
        }.toTypedArray()
    )

    @Support(SQLDialect.MYSQL_5_7, SQLDialect.MYSQL_8_0)
    @JvmStatic
    @Throws(JsonPathErrorException::class)
    fun json_keys(node: Field<JsonNode>, path: String = "") = field(
        if (path.isNullOrBlank()) "JSON_KEYS( ? )" else "JSON_KEYS( ? , ? )",
        JsonNode::class.java,
        node,
        *  mutableListOf<String>().apply {
            if (path.isNotBlank()) add(path)
        }.toTypedArray()
    )

    @Support(SQLDialect.MYSQL_5_7, SQLDialect.MYSQL_8_0)
    @JvmStatic
    @Throws(JsonPathErrorException::class)
    fun json_search(node: Field<JsonNode>, type: JSON_PATH_TYPE, value: String, escape_char: Char? = null, vararg path: String) = field(
        path.joinToString(",", prefix = if (escape_char == null) "JSON_SEARCH( ? , ? , ? ," else "JSON_SEARCH( ? , ? , ? , ?", postfix = ")") { " ? " },
        JsonNode::class.java,
        node,
        * mutableListOf<Any>(type.value, json_val(value)).apply {
            if (escape_char != null) {
                add(escape_char)
            }
            addAll(path)
        }.toTypedArray()
    )


    @Support(SQLDialect.MYSQL_5_7, SQLDialect.MYSQL_8_0)
    @JvmStatic
    @Throws(JsonPathErrorException::class)
    fun json_contains_path(node: Field<JsonNode>, type: JSON_PATH_TYPE, vararg path: String) = field(
        path.joinToString(",", prefix = "JSON_CONTAINS_PATH( ? , ? ", postfix = ")") { " ? , ? " },
        Boolean::class.java,
        node,
        type.value,
        * path.map { pathCheck(it) }.toTypedArray()
    )

    @Support(SQLDialect.MYSQL_5_7, SQLDialect.MYSQL_8_0)
    @JvmStatic
    @Throws(JsonPathErrorException::class)
    fun json_depth(node: Field<JsonNode>) = field(
        "JSON_DEPTH( ? )",
        Int::class.java,
        node
    )

    /**
     *
     * @param node [Field]<[JsonNode]>
     * @param value Array<out Pair<String, Any>> **Json path to Json Value**
     * @return [Field]<[JsonNode]>
     * @throws JsonPathErrorException
     */

    @Support(SQLDialect.MYSQL_5_7, SQLDialect.MYSQL_8_0)
    @JvmStatic
    @Throws(JsonPathErrorException::class)
    fun json_array_insert(node: Field<JsonNode>, vararg value: Pair<String, Any>) = field(
        value.joinToString(",", prefix = "JSON_ARRAY_INSERT( ?", postfix = ")") { " ? , ?" },
        JsonNode::class.java,
        node,
        *value.map {
            listOf(pathCheck(pathJoin(it.first)), json_val(it.second))
        }.flatMap { it }
            .toTypedArray()
    )

    /**
     * convert value to json value
     * @param value String
     * @return  [Field]<[JsonNode]>
     * @throws JsonPathErrorException
     */
    @Support(SQLDialect.MYSQL_5_7, SQLDialect.MYSQL_8_0)
    @JvmStatic
    @Throws(JsonPathErrorException::class)
    fun json_quote(value: String) = field(
        "JSON_QUOTE( ? )",
        JsonNode::class.java,
        value
    )

    /**
     * convert json_val to string
     * @param json_val String
     * @return [Field]<[String]>
     * @throws JsonPathErrorException
     */
    //TODO: check types?
    @Support(SQLDialect.MYSQL_5_7, SQLDialect.MYSQL_8_0)
    @JvmStatic
    @Throws(JsonPathErrorException::class)
    fun json_unquote(json_val: String) = field(
        "JSON_UNQUOTE( ? )",
        String::class.java,
        json_val
    )

    /**
     * create json object **will convert entity value to json string**
     * @param entity Array<out Pair<String, Any>>
     * @return [Field]<[JsonNode]>
     * @throws JsonPathErrorException
     */
    @Support(SQLDialect.MYSQL_5_7, SQLDialect.MYSQL_8_0)
    @JvmStatic
    @Throws(JsonPathErrorException::class)
    fun json_object(vararg entity: Pair<String, Any>) = field(
        entity.joinToString(",", prefix = "JSON_OBJECT( ", postfix = ")") { " ? , ? " },
        JsonNode::class.java,
        *entity.flatMap { listOf(pathCheck(pathJoin(it.first)), json_val(it.second)) }
            .toTypedArray()
    )

    /**
     * create json object
     * @param entity Array<out Pair<String, [Field]<[JsonNode]>>>
     * @return [Field]<[JsonNode]>
     * @throws JsonPathErrorException
     */
    @Support(SQLDialect.MYSQL_5_7, SQLDialect.MYSQL_8_0)
    @JvmStatic
    @Throws(JsonPathErrorException::class)
    fun json_object_node(vararg entity: Pair<String, Field<JsonNode>>) = field(
        entity.joinToString(",", prefix = "JSON_OBJECT( ", postfix = ")") { " ? , ? " },
        JsonNode::class.java,
        *entity.flatMap { listOf(pathCheck(pathJoin(it.first)), it.second) }
            .toTypedArray()
    )

    /**
     *  create json array
     * @param value Array<out Any>
     * @return [Field]<[JsonNode]>
     * @throws JsonPathErrorException
     */
    @Support(SQLDialect.MYSQL_5_7, SQLDialect.MYSQL_8_0)
    @JvmStatic
    @Throws(JsonPathErrorException::class)
    fun json_array(vararg value: Any) = field(
        value.joinToString(",", prefix = "JSON_ARRAY( ", postfix = ")") { " ? " },
        JsonNode::class.java,
        *value.map { json_val(it) }.toTypedArray()
    )

    /**
     *  create json array
     * @param value Array<out [Field]<[JsonNode]>>
     * @return [Field]<[JsonNode]>
     * @throws JsonPathErrorException
     */
    @Support(SQLDialect.MYSQL_5_7, SQLDialect.MYSQL_8_0)
    @JvmStatic
    @Throws(JsonPathErrorException::class)
    fun json_array(vararg value: Field<JsonNode>) = field(
        value.joinToString(",", prefix = "JSON_ARRAY( ", postfix = ")") { " ? " },
        JsonNode::class.java,
        *value.map { it }.toTypedArray()
    )


    //</editor-fold>

    //<editor-fold desc="SQL 5.7.22+">
    /*    @Support(SQLDialect.MYSQL_8_0)
        @JvmStatic
        @Throws(JsonPathErrorException::class)
        fun json_table(node: Field<JsonNode>, vararg path: Any) = field(

        )*/
    /**
     *  only supported by MySql 5.7.22+
     * @param col String
     * @return [Field]<[JsonNode]>
     */
    @Support(SQLDialect.MYSQL_5_7, SQLDialect.MYSQL_8_0)
    @JvmStatic
    fun json_arrayagg(col: String) = field(
        "JSON_ARRAYAGG( ? )",
        JsonNode::class.java,
        col
    )

    /**
     * only supported by MySql 5.7.22+
     * @param col [SelectField]<*>
     * @return [Field]<[JsonNode]>
     */

    @Support(SQLDialect.MYSQL_5_7, SQLDialect.MYSQL_8_0)
    @JvmStatic
    fun json_arrayagg(col: SelectField<*>) = field(
        "JSON_ARRAYAGG( ? )",
        JsonNode::class.java,
        col
    )

    /**
     *  only supported by MySql 5.7.22+
     * @param key String
     * @param value String
     * @return [Field]<[JsonNode]>
     */

    @Support(SQLDialect.MYSQL_5_7, SQLDialect.MYSQL_8_0)
    @JvmStatic
    fun json_objectagg(key: String, value: String) = field(
        "JSON_OBJECTAGG( ? , ? )",
        JsonNode::class.java,
        key, value
    )

/*    */
    /**
     * only supported by MySql 5.7.22+
     * @param value [SelectFieldOrAsterisk]
     * @return [Field]<[JsonNode]>
     *//*

    @Support(SQLDialect.MYSQL_5_7, SQLDialect.MYSQL_8_0)
    @JvmStatic
    fun json_objectagg(value: SelectFieldOrAsterisk) = field(
        "JSON_OBJECTAGG( ? )",
        JsonNode::class.java,
        value
    )*/
    //</editor-fold>
    //<editor-fold desc="Helper">
    /**
     * Join path into mysql path
     * @param path Array<out Any> [Int] or [String]
     * @return String
     */
    @JvmStatic
    fun pathJoin(vararg path: Any): String = when {
        path.size == 1 && path[0] is String && (path[0] as String).startsWith("$") -> path[0].toString()/*when {
//            !(path[0] as String).endsWith(".") -> path[0].toString().split(".").toMutableList().let {
//                it.removeAt(0)
//                pathJoin(*it.toTypedArray())
//            }
            else -> throw JsonPathErrorException(path[0].toString())
        }*/
        else -> path.joinToString("", prefix = "$") {
            """${when (it) {
                is Int -> "[$it]"
                else -> """."${DSL.escape(it.toString(), '\\')}""""
            }
            }"""
        }
    }

    /**
     * Before use should set Jackson ObjectMapper
     * @param m [ObjectMapper]
     */
    fun setMapper(m: ObjectMapper) {
        this.mapper = m
    }

    /**
     * chec path is vaild json path
     * @param path String
     * @return String
     * @throws JsonPathErrorException
     */
    @JvmStatic
    @Throws(JsonPathErrorException::class)
    fun pathCheck(path: String) =
            when {
                !path.startsWith("$.") && !path.startsWith("$[") -> throw JsonPathErrorException(path)
                path.equals("$.") && path.equals("$[") -> throw JsonPathErrorException(path)
                path.contains(".[") -> throw JsonPathErrorException(path)
                path.contains("[\"") || path.contains("\"]") -> throw JsonPathErrorException(path)
                """\[[a-zA-Z\"]+\]""".toRegex().containsMatchIn(path) -> throw JsonPathErrorException(path)
                path.endsWith(".") -> throw JsonPathErrorException(path)
                else -> path
            }

    /**
     * convert value to mysql json_val
     * @param a Any
     * @param tojson Boolean
     * @return (java.io.Serializable..java.io.Serializable?)
     * @throws JsonMapperErrorException
     */
    @JvmStatic
    @Throws(JsonMapperErrorException::class)
    fun json_val(a: Any, tojson: Boolean = true, convert: Boolean = true) = when (a) {
        is String -> DSL.escape(a, '\\')
        is Number -> a
        else -> when {
            !convert -> a
            tojson -> "CAST('${DSL.escape(mapper?.writeValueAsString(a)
                                          ?: throw JsonMapperErrorException(), '\\')}' AS JSON)"
            else -> DSL.escape(mapper?.writeValueAsString(a) ?: throw JsonMapperErrorException(), '\\')
        }
    }

    //</editor-fold>
    //<editor-fold desc="Extentions">
    fun Field<JsonNode>.json(path: Any, vararg more: Any) = json_extract(this, path, *more)
    fun Field<JsonNode>.jsonText(path: Any, vararg more: Any) = json_extract_text(this, path, *more)
    fun Field<JsonNode>.jsonSet(path: Pair<String, Any>, vararg more: Pair<String, Any>) = json_set(this, path, *more)
    //</editor-fold>
    enum class JSON_PATH_TYPE(val value: String) {
        ONE("one"),
        ALL("all"),
    }
    enum class JSON_TYPE(val value: String) {
        INT("INTEGER"),
        NULL("NULL"),
        OBJECT("OBJECT"),
        ARRAY("ARRAY"),
        BOOLEAN("BOOLEAN"),
        DOUBLE("DOUBLE"),
        DECIMAL("DECIMAL"),
        DATETIME("DATETIME"),
        DATE("DATE"),
        TIME("TIME"),
        STRING("STRING"),
        BLOB("BLOB"),
        BIT("BIT"),
        OPAQUE("OPAQUE"),
    }
}
