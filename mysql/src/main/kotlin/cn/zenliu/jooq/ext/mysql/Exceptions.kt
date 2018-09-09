/*
 * Copyright (c) 2018.
 * Authored By Zen.Liu
 */

package cn.zenliu.jooq.ext.mysql

class JsonPathErrorException(target:String?,message: String?="json path not vaild,should be $.\"pathstring\"[\"pathindex\"]\n path is ${target} " , throwable:Throwable?=null ) : Exception(message,throwable)
class JsonMapperErrorException(message: String?="mapper not set!" , throwable:Throwable?=null ) : Exception(message,throwable)
