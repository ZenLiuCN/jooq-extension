/*
 * Copyright (c) 2018.
 * Authored By Zen.Liu
 */

package cn.zenliu.jooq.ext.postgres

import cn.zenliu.jooq.ext.model.Tables
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.sql.DriverManager

internal class JsonbDSLTest {
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
    fun jsonb() {
    }
}
