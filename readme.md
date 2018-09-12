# jooq extensions in kotlin

now in progress

## Mysql extension

### json type
for code genatrate
```xml
<forcedTypes>
    <forcedType>
        <userType>com.fasterxml.jackson.databind.JsonNode</userType>
        <binding>cn.zenliu.jooq.ext.mysql.JsonBinding</binding>
        <expression>.*</expression>
        <types>JSON</types>
        <nullability>ALL</nullability>
    </forcedType>
</forcedTypes>
```

## Postgres extentison

### Jsonb type

inprogress
