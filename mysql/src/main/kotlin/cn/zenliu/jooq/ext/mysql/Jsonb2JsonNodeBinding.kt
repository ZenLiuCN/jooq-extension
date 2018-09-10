package package cn.zenliu.jooq.ext.postgres

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.jooq.*
import org.jooq.impl.DSL
import java.sql.SQLException
import java.sql.SQLFeatureNotSupportedException
import java.sql.Types
import java.util.*

@Allow(SQLDialect.MYSQL_5_7, SQLDialect.MYSQL_8_0)
class Jsonb2JsonNodeBinding(val mapper: ObjectMapper) : Binding<String, JsonNode> {
    override fun converter(): Converter<String, JsonNode> = Converter.of(
            String::class.java,
            JsonNode::class.java,
            {
                mapper.readTree(mapper.writeValueAsString(it))
            },
            {
                mapper.readValue(it.toString(), String::class.java)
            }
    )
    // Rending a bind variable for the binding context's value and casting it to the json type
    @Throws(SQLException::class)
    override fun sql(ctx: BindingSQLContext<JsonNode>) {
        // Depending on how you generate your SQL, you may need to explicitly distinguish
        // between jOOQ generating bind variables or inlined literals. If so, use this check:
        // ctx.render().paramType() == INLINED
        ctx.render().visit(DSL.`val`(ctx.convert(converter()).value())).sql("::jsonb")
    }

    // Registering VARCHAR types for JDBC CallableStatement OUT parameters
    @Throws(SQLException::class)
    override fun register(ctx: BindingRegisterContext<JsonNode>) {
        ctx.statement().registerOutParameter(ctx.index(), Types.VARCHAR)
    }

    // Converting the JsonElement to a String value and setting that on a JDBC PreparedStatement
    @Throws(SQLException::class)
    override fun set(ctx: BindingSetStatementContext<JsonNode>) {
        ctx.statement().setString(ctx.index(), Objects.toString(ctx.convert(converter()).value(), null))
    }

    // Getting a String value from a JDBC ResultSet and converting that to a JsonElement
    @Throws(SQLException::class)
    override fun get(ctx: BindingGetResultSetContext<JsonNode>) {
        ctx.convert(converter()).value(ctx.resultSet().getString(ctx.index()))
    }

    // Getting a String value from a JDBC CallableStatement and converting that to a JsonElement
    @Throws(SQLException::class)
    override fun get(ctx: BindingGetStatementContext<JsonNode>) {
        ctx.convert(converter()).value(ctx.statement().getString(ctx.index()))
    }

    // Setting a value on a JDBC SQLOutput (useful for Oracle OBJECT types)
    @Throws(SQLException::class)
    override fun set(ctx: BindingSetSQLOutputContext<JsonNode>) {
        throw SQLFeatureNotSupportedException()
    }

    // Getting a value from a JDBC SQLInput (useful for Oracle OBJECT types)
    @Throws(SQLException::class)
    override fun get(ctx: BindingGetSQLInputContext<JsonNode>) {
        throw SQLFeatureNotSupportedException()
    }
}
