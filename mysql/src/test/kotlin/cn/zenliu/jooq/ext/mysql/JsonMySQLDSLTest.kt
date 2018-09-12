/*
 * Copyright (c) 2018.
 * Authored By Zen.Liu
 */

package cn.zenliu.jooq.ext.mysql

import cn.zenliu.jooq.ext.model.Tables
import cn.zenliu.jooq.ext.mysql.JsonDSL.json
import cn.zenliu.jooq.ext.mysql.JsonDSL.jsonText
import cn.zenliu.jooq.ext.mysql.JsonDSL.json_contains
import cn.zenliu.jooq.ext.mysql.JsonDSL.json_contains_path
import com.fasterxml.jackson.databind.ObjectMapper
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.jooq.tools.jdbc.MockConnection
import org.jooq.tools.jdbc.MockResult
import org.junit.jupiter.api.Test
import java.sql.DriverManager


internal class JsonMySQLDSLTest {
    lateinit var ctx: DSLContext
    val T=Tables.TEST_
    init {
        createDSL()
    }

    fun createDSL() {
        Class.forName("com.mysql.cj.jdbc.Driver")

//        val conn = DriverManager.getConnection("jdbc:mysql://192.168.99.100:3306/test?useSSL=false&serverTimezone=UTC", "cmnt", "cmnt");
        val conn=MockConnection{
            arrayOf(MockResult(0))
        }
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
    fun functionTest(){
        JsonDSL.setMapper(ObjectMapper())




        println(JsonDSL.json_arrayagg("{}").toString())
        println(JsonDSL.json_arrayagg(T.JSON).toString())

        println(JsonDSL.json_objectagg("a",1).toString())
        println(JsonDSL.json_objectagg("b","1").toString())
        println(JsonDSL.json_objectagg("a",T.JSON).toString())



        println(JsonDSL.json_quote("{}").toString())
        println(JsonDSL.json_unquote("{}").toString())

        println(JsonDSL.json_valid("{}").toString())
        println(JsonDSL.json_valid(T.JSON).toString())

        println(JsonDSL.json_remove(T.JSON,"{}").toString())

        println(JsonDSL.json_replace(T.JSON,"$.a" to "{}").toString())

        println(JsonDSL.json_merge_patch(T.JSON,"{}").toString())
        println(JsonDSL.json_merge_patch(T.JSON,T.ID).toString())

        println(JsonDSL.json_merge_preserve(T.JSON,"{}").toString())
        println(JsonDSL.json_merge_preserve(T.JSON,T.ID).toString())

        println(JsonDSL.json_length(T.JSON).toString())

        println(JsonDSL.json_type(T.JSON).toString())

        println(JsonDSL.json_type("{}").toString())

        println(JsonDSL.json_keys(T.JSON).toString())
        println(JsonDSL.json_keys(T.JSON,"$.A").toString())

        println(JsonDSL.json_search(T.JSON,JsonDSL.JSON_PATH_TYPE.ALL,"A").toString())

        println(JsonDSL.json_depth(T.JSON).toString())

        println(JsonDSL.json_set(T.JSON,"$[1]" to 1).toString())

        println(JsonDSL.json_insert(T.JSON,"$[1]" to 1).toString())

        println(JsonDSL.json_extract(T.JSON,"a",1).toString())

        println(JsonDSL.json_extract_text(T.JSON,"a",1).toString())

        println(JsonDSL.json_contains(T.JSON,1).toString())
        println(JsonDSL.json_contains_path(T.JSON, JsonDSL.JSON_PATH_TYPE.ALL, "$[1]").toString())

        println(JsonDSL.json_array_append(T.JSON,"$[1]" to 1).toString())
        println(JsonDSL.json_array_insert(T.JSON,"$[1]" to 1).toString())

        println(JsonDSL.json_array(true, 1, "s", mapOf("a" to 1)).toString())
        println(JsonDSL.json_array(T.JSON,T.ID,"s").toString())

        println(JsonDSL.json_object("A" to 2,"b" to "\\n").toString()) //TODO::
        println(JsonDSL.json_object("A" to 2,"b" to 1,"c" to "s").toString())
        println(JsonDSL.json_object("A" to T.JSON,"B" to T.ID).toString())
        println(JsonDSL.json_object("A" to T.JSON,"B" to T.ID,"C" to T.ID).toString())
    }

}
