/*
 * Copyright (c) 2018.
 * Authored By Zen.Liu
 */

package cn.zenliu.jooq.ext.mysql

import cn.zenliu.jooq.ext.model.Tables
import com.fasterxml.jackson.databind.JsonNode
import org.jooq.DSLContext
import org.jooq.Record1
import org.jooq.Result
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.junit.jupiter.api.Test
import java.sql.DriverManager
import cn.zenliu.jooq.ext.mysql.JsonMySQLDSL.json
import cn.zenliu.jooq.ext.mysql.JsonMySQLDSL.jsonText


internal class JsonMySQLDSLTest {
    lateinit var ctx: DSLContext
    val T=Tables.TEST_
    init {
        createDSL()
    }

    fun createDSL() {
        Class.forName("com.mysql.cj.jdbc.Driver")
        val conn = DriverManager.getConnection("jdbc:mysql://192.168.99.100:3306/test?useSSL=false&serverTimezone=UTC", "cmnt", "cmnt");
        this.ctx = DSL.using(conn, SQLDialect.MYSQL_5_7)
/*        //language=MySQL
        this.ctx.execute("""
            CREATE TABLE if not exists `test`  (
              `id` int(11) NOT NULL AUTO_INCREMENT,
              `json` json NOT NULL,
              PRIMARY KEY (`id`) USING BTREE
            ) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;
        """.trimIndent())
        if(ctx.fetchCount(T)==0){
            ctx.insertInto(T).set(mapOf("json" to listOf<String>()))
        }
         test = ctx.select(T.JSON).from(T).fetch()*/
    }

    @Test
    fun json_extract() {
        val q1=JsonMySQLDSL.json_extract(T.JSON, "$.a").toString()
        val q2=JsonMySQLDSL.json_extract(T.JSON, "a",1,2).toString()
        println(q1)
        println(q2)
        assert("\"test\".\"test\".\"json\" -> '\$.a'"==q1.toString())
        assert(""""test"."test"."json" -> '${'$'}."a"[1][2]'"""==q2.toString())
    }

    @Test
    fun json_extract_unquote() {
        val q1=JsonMySQLDSL.json_extract_text(T.JSON, "$.a").toString()
        val q2=JsonMySQLDSL.json_extract_text(T.JSON, "a", 1).toString()
        println(q1)
        println(q2)
        assert("\"test\".\"test\".\"json\" ->> '\$.a'"==q1.toString())
        assert(""""test"."test"."json" ->> '${'$'}."a"[1]'"""==q2.toString())
    }

    @Test
    fun json() {
        val q1=T.JSON.json("$.a").toString()
        val q2=T.JSON.json("a",1).toString()
        println(q1)
        println(q2)
        assert("\"test\".\"test\".\"json\" -> '\$.a'"==q1.toString())
        assert("\"test\".\"test\".\"json\" -> '\$.\"a\"[1]'"==q2.toString())
    }

    @Test
    fun jsonText() {
        val q1=T.JSON.jsonText("$.a").toString()
        val q2=T.JSON.jsonText("a",1).toString()
        println(q1)
        println(q2)
        assert("\"test\".\"test\".\"json\" ->> '\$.a'"==q1.toString())
        assert("\"test\".\"test\".\"json\" ->> '\$.\"a\"[1]'"==q2.toString())
    }

    @Test
    fun json_set() {
        val q1=JsonMySQLDSL.json_set(T.JSON, "$.a" to 1,"$.b" to "a").toString()
        val q2=JsonMySQLDSL.json_set(T.JSON, "$.a" to 1).toString()
        println(q1)
        println(q2)
        assert("""JSON_SET( "test"."test"."json" , '${'$'}.a' , 1 , '${'$'}.b' , 'a' )"""==q1.toString())
        assert("""JSON_SET( "test"."test"."json" , '${'$'}.a' , 1 )"""==q2.toString())

    }

    @Test
    fun json_remove() {
        val q1=JsonMySQLDSL.json_remove(T.JSON,"$.a" ).toString()
        val q2=JsonMySQLDSL.json_remove(T.JSON,"$.a" ,"$.c").toString()
        println(q1)
        println(q2)
        assert("""JSON_REMOVE( "test"."test"."json" , '${'$'}.a' )"""==q1)
        assert("""JSON_REMOVE( "test"."test"."json" , '${'$'}.a' , '${'$'}.c' )"""==q2)
    }

    @Test
    fun json_replace() {
        val q1=JsonMySQLDSL.json_replace(T.JSON,"$.a" to "a").toString()
        val q2=JsonMySQLDSL.json_replace(T.JSON,"$.a" to 1 ,"$.c" to "a").toString()
        println(q1)
        println(q2)
        assert("""JSON_REMOVE( "test"."test"."json" , '${'$'}.a' )"""==q1)
        assert("""JSON_REMOVE( "test"."test"."json" , '${'$'}.a' , '${'$'}.c' )"""==q2)
    }

    @Test
    fun json_length() {
    }

    @Test
    fun json_type() {
    }

    @Test
    fun json_type1() {
    }

    @Test
    fun json_valid() {
    }

    @Test
    fun json_valid1() {
    }

    @Test
    fun json_array() {
    }

    @Test
    fun json_array_append() {
    }

    @Test
    fun json_insert() {
    }

    @Test
    fun json_contains() {
    }

    @Test
    fun json_keys() {
    }

    @Test
    fun json_search() {
    }

    @Test
    fun json_merge_patch() {
    }

    @Test
    fun json_merge_preserve() {
    }

    @Test
    fun json_merge_patch1() {
    }

    @Test
    fun json_merge_preserve1() {
    }

    @Test
    fun json_contains_path() {
    }

    @Test
    fun json_depth() {
    }

    @Test
    fun json_array_insert() {
    }

    @Test
    fun json_quote() {
    }

    @Test
    fun json_unquote() {
    }

    @Test
    fun json_object() {
    }

    @Test
    fun pathJoin() {
    }

    @Test
    fun pathCheck() {
    }

    @Test
    fun json_arrayagg() {
    }

    @Test
    fun json_arrayagg1() {
    }

    @Test
    fun json_objectagg() {
    }

    @Test
    fun json_objectagg1() {
     listOf(1 to 2,3 to 4,5 to 6).flatMap { listOf(it.first,it.second) }.forEach(::println)
    }
}
