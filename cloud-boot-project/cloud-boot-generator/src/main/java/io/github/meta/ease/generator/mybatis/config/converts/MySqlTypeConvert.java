/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.meta.ease.generator.mybatis.config.converts;

import io.github.meta.ease.generator.mybatis.config.GlobalConfig;
import io.github.meta.ease.generator.mybatis.config.ITypeConvert;
import io.github.meta.ease.generator.mybatis.config.rules.DbColumnType;
import io.github.meta.ease.generator.mybatis.config.rules.IColumnType;

import static io.github.meta.ease.generator.mybatis.config.converts.TypeConverts.contains;
import static io.github.meta.ease.generator.mybatis.config.converts.TypeConverts.containsAny;
import static io.github.meta.ease.generator.mybatis.config.rules.DbColumnType.*;

/**
 * MYSQL 数据库字段类型转换
 * bit类型数据转换 bit(1) -> Boolean类型  bit(2->64)  -> Byte类型
 *
 * @author hubin, hanchunlin, xiaoliang
 * @since 2017-01-20
 */
public class MySqlTypeConvert implements ITypeConvert {
    public static final MySqlTypeConvert INSTANCE = new MySqlTypeConvert();

    /**
     * 转换为日期类型
     *
     * @param config 配置信息
     * @param type   类型
     * @return 返回对应的列类型
     */
    public static IColumnType toDateType(GlobalConfig config, String type) {
        String dateType = type.replaceAll("\\(\\d+\\)", "");
        switch (config.getDateType()) {
            case ONLY_DATE:
                return DbColumnType.DATE;
            case SQL_PACK:
                switch (dateType) {
                    case "date":
                    case "year":
                        return DbColumnType.DATE_SQL;
                    case "time":
                        return DbColumnType.TIME;
                    default:
                        return DbColumnType.TIMESTAMP;
                }
            case TIME_PACK:
                switch (dateType) {
                    case "date":
                        return DbColumnType.LOCAL_DATE;
                    case "time":
                        return DbColumnType.LOCAL_TIME;
                    case "year":
                        return DbColumnType.YEAR;
                    default:
                        return DbColumnType.LOCAL_DATE_TIME;
                }
        }
        return STRING;
    }

    /**
     * @inheritDoc
     */
    @Override
    public IColumnType processTypeConvert(GlobalConfig config, String fieldType) {
        return TypeConverts.use(fieldType)
                .test(containsAny("char", "text", "json", "enum").then(STRING))
                .test(contains("bigint").then(LONG))
                .test(containsAny("tinyint(1)", "bit(1)").then(BOOLEAN))
                .test(contains("bit").then(BYTE))
                .test(contains("int").then(INTEGER))
                .test(contains("decimal").then(BIG_DECIMAL))
                .test(contains("clob").then(CLOB))
                .test(contains("blob").then(BLOB))
                .test(contains("binary").then(BYTE_ARRAY))
                .test(contains("float").then(FLOAT))
                .test(contains("double").then(DOUBLE))
                .test(containsAny("date", "time", "year").then(t -> toDateType(config, t)))
                .or(STRING);
    }

}
