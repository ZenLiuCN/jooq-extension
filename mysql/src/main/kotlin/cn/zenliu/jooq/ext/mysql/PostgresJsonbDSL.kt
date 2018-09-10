@Support(SQLDialect.POSTGRES)
fun Field<JsonElement>.jsonb(vararg key: Any) = DSL.field(
    key.joinToString("->", qualifiedName.toString().let {
        if (it.contains("->")) {
            name
        } else {
            it
        }
    } + "->") {
        when (it) {
            is Number -> it.toInt().toString()
            else -> "'$it'"
        }
    },
    JsonElement::class.java)

@Support(SQLDialect.POSTGRES)
fun Field<JsonElement>.jsonb_text(vararg key: Any) = DSL.field(
    key.joinToString("->", this.qualifiedName.toString() + "->") {
        when (it) {
            is Number -> it.toInt().toString()
            else -> "'$it'"
        }
    }.fixToText(),
    String::class.java)

@Support(SQLDialect.POSTGRES)
fun Field<JsonElement>.jsonb_set(value: Any?, vararg key: Any) = DSL.field(
    "jsonb_set(${
    qualifiedName.toString().let {
        if (it.contains("jsonb_set")) {
            name
        } else {
            it
        }
    }},'{${key.joinToString(",") { it.toString() }}}', '${BeanUtil.get<Gson>().toJson(value)}'::jsonb)",
    JsonElement::class.java
)

@Support(SQLDialect.POSTGRES_10)
fun Field<JsonElement>.jsonb_insert(value: Any?, insertafter: Boolean = false, vararg key: Any) = DSL.field(
    "jsonb_insert($qualifiedName,'{${key.joinToString(",") { it.toString() }}}','${BeanUtil.get<Gson>().toJson(value)}'::jsonb,$insertafter)",
    JsonElement::class.java
)

@Support(SQLDialect.POSTGRES)
fun Field<JsonElement>.jsonb_strip_nulls() = DSL.field(
    "jsonb_strip_nulls($qualifiedName)",
    JsonElement::class.java
)

@Support(SQLDialect.POSTGRES)
fun Field<JsonElement>.path(vararg key: Any) =
        DSL.field(
            "${this.qualifiedName.toString()}#>'{${key.joinToString(",") { it.toString() }}}'",
            JsonElement::class.java)

@Support(SQLDialect.POSTGRES)
fun Field<JsonElement>.pathText(vararg key: Any) =
        DSL.field(
            "${this.qualifiedName.toString()}#>>'{${key.joinToString(",") { it.toString() }}}'",
            String::class.java)

@Support(SQLDialect.POSTGRES)
fun Field<JsonElement>.contain(value: Any) =
        DSL.field(
            "${this.qualifiedName.toString()}@>'${BeanUtil.get<Gson>().toJson(value)}'::jsonb",
            Boolean::class.java)

@Support(SQLDialect.POSTGRES)
fun Field<JsonElement>.containBy(value: Any) =
        DSL.field(
            "${this.qualifiedName.toString()}@<'${BeanUtil.get<Gson>().toJson(value)}'::jsonb",
            Boolean::class.java)

@Support(SQLDialect.POSTGRES)
fun Field<JsonElement>.hasTopKey(value: String) =
        DSL.field(
            "${this.qualifiedName.toString()}?'$value'",
            Boolean::class.java)

@Support(SQLDialect.POSTGRES)
fun Field<JsonElement>.anyTopKeys(vararg value: String) =
        DSL.field(
            "${this.qualifiedName.toString()}?| ARRAY[${value.joinToString(",", "'", "'") { it }}]",
            Boolean::class.java)

@Support(SQLDialect.POSTGRES)
fun Field<JsonElement>.allTopKeys(vararg value: String) =
        DSL.field(
            "${this.qualifiedName.toString()}?& ARRAY[${value.joinToString(",", "'", "'") { it }}]",
            Boolean::class.java)

@Support(SQLDialect.POSTGRES)
fun Field<JsonElement>.concatenate(value: Any) =
        DSL.field(
            "${this.qualifiedName.toString()}|| '${BeanUtil.get<Gson>().toJson(value)}'::jsonb",
            JsonElement::class.java)

@Support(SQLDialect.POSTGRES)
fun Field<JsonElement>.remove(value: String) =
        DSL.field(
            "${this.qualifiedName.toString()}- '$value'",
            JsonElement::class.java)

@Support(SQLDialect.POSTGRES)
fun Field<JsonElement>.remove(value: Int) =
        DSL.field(
            "${this.qualifiedName.toString()}- $value",
            JsonElement::class.java)

@Support(SQLDialect.POSTGRES)
fun Field<JsonElement>.jsonb_array_length() =
        DSL.field(
            "jsonb_array_length(${this.qualifiedName.toString()})",
            Int::class.java)

/**
 * delete field via path
 * @receiver Field<JsonElement>
 * @param value Array<out String>
 * @return (org.jooq.Field<(kotlin.Boolean..kotlin.Boolean?)>..org.jooq.Field<(kotlin.Boolean..kotlin.Boolean?)>?)
 */
@Support(SQLDialect.POSTGRES)
fun Field<JsonElement>.delete(vararg value: String) =
        DSL.field(
            "${this.qualifiedName.toString()} #- '{${value.joinToString(",") { it }}}'",
            Boolean::class.java)

/**
 * convert any field to jsonb
 * @receiver Field<T>
 * @return (org.jooq.Field<(kotlin.Any..kotlin.Any?)>..org.jooq.Field<(kotlin.Any..kotlin.Any?)>?)
 */
@Support(SQLDialect.POSTGRES)
fun <T> Field<T>.to_jsonb() = DSL.field("to_jsonb(${this.qualifiedName.toString()})", JsonElement::class.java)
//TODO: jsonb_to_recordset,jsonb_to_record,jsonb_array_elements_text
/*fun <T:Collection<*>> Field<T>.array_to_json(pretty:Boolean=false)=DSL.field("array_to_json(${this.qualifiedName.toString()}.$pretty)",JsonElement::class.java)

fun Record.row_to_json()=DSL.field("row_to_json($this)")*/

private fun String.fixToText() = buildString {
    append(this@fixToText.subSequence(0, this@fixToText.lastIndexOf('>')))
    append(">")
    append(this@fixToText.subSequence(this@fixToText.lastIndexOf('>'), this@fixToText.length))
}
