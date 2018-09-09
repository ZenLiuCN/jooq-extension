package cn.zenliu.jooq.ext.mysql

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.NullNode
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class Json2JacksonBindingTest {

    @Test
    @DisplayName("json2JsonNode mapper 测试")
    fun converter() {
        val jo = ObjectMapper()
        val src = mapOf("json" to "string", "int" to 0,"null" to null)
        val mapper = Json2JsonNodeBinding(jo).converter()
        mapper.from(src).let {
            println("${it}<=>${jo.writeValueAsString(src)}")
            assert(it.toString() == jo.readTree(jo.writeValueAsBytes(src)).toString())
        }
        mapper.to(NullNode.instance).let{
            println("${it}<=>${jo.writeValueAsString(null)}")
        }

    }
}
