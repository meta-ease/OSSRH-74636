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
package com.boot.generator.mybatis.keywords;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * mysql关键字处理
 * 这里选取了mysql5.7文档中的关键字和保留字（含移除）https://dev.mysql.com/doc/refman/5.7/en/keywords.html
 *
 * @author nieqiurong 2020/5/7.
 * @since 3.3.2
 */
public class MySqlKeyWordsHandler extends BaseKeyWordsHandler {

    private static final List<String> KEY_WORDS = new ArrayList<>(Arrays.asList("ACCESSIBLE",
            "ACCOUNT", "ACTION", "ADD", "AFTER", "AGAINST",
            "AGGREGATE", "ALGORITHM", "ALL", "ALTER",
            "ALWAYS", "ANALYSE", "ANALYZE", "AND", "ANY",
            "AS", "ASC", "ASCII", "ASENSITIVE", "AT",
            "AUTOEXTEND_SIZE", "AUTO_INCREMENT", "AVG",
            "AVG_ROW_LENGTH", "BACKUP", "BEFORE", "BEGIN",
            "BETWEEN", "BIGINT", "BINARY", "BINLOG", "BIT",
            "BLOB", "BLOCK", "BOOL", "BOOLEAN", "BOTH",
            "BTREE", "BY", "BYTE", "CACHE", "CALL",
            "CASCADE", "CASCADED", "CASE", "CATALOG_NAME",
            "CHAIN", "CHANGE", "CHANGED", "CHANNEL",
            "CHAR", "CHARACTER", "CHARSET", "CHECK",
            "CHECKSUM", "CIPHER", "CLASS_ORIGIN", "CLIENT",
            "CLOSE", "COALESCE", "CODE", "COLLATE",
            "COLLATION", "COLUMN", "COLUMNS",
            "COLUMN_FORMAT", "COLUMN_NAME", "COMMENT",
            "COMMIT", "COMMITTED", "COMPACT", "COMPLETION",
            "COMPRESSED", "COMPRESSION", "CONCURRENT",
            "CONDITION", "CONNECTION", "CONSISTENT",
            "CONSTRAINT", "CONSTRAINT_CATALOG",
            "CONSTRAINT_NAME", "CONSTRAINT_SCHEMA",
            "CONTAINS", "CONTEXT", "CONTINUE", "CONVERT",
            "CPU", "CREATE", "CROSS", "CUBE", "CURRENT",
            "CURRENT_DATE", "CURRENT_TIME",
            "CURRENT_TIMESTAMP", "CURRENT_USER", "CURSOR",
            "CURSOR_NAME", "DATA", "DATABASE", "DATABASES",
            "DATAFILE", "DATE", "DATETIME", "DAY",
            "DAY_HOUR", "DAY_MICROSECOND", "DAY_MINUTE",
            "DAY_SECOND", "DEALLOCATE", "DEC", "DECIMAL",
            "DECLARE", "DEFAULT", "DEFAULT_AUTH",
            "DEFINER", "DELAYED", "DELAY_KEY_WRITE",
            "DELETE", "DESC", "DESCRIBE", "DES_KEY_FILE",
            "DETERMINISTIC", "DIAGNOSTICS", "DIRECTORY",
            "DISABLE", "DISCARD", "DISK", "DISTINCT",
            "DISTINCTROW", "DIV", "DO", "DOUBLE", "DROP",
            "DUAL", "DUMPFILE", "DUPLICATE", "DYNAMIC",
            "EACH", "ELSE", "ELSEIF", "ENABLE", "ENCLOSED",
            "ENCRYPTION", "END", "ENDS", "ENGINE",
            "ENGINES", "ENUM", "ERROR", "ERRORS", "ESCAPE",
            "ESCAPED", "EVENT", "EVENTS", "EVERY",
            "EXCHANGE", "EXECUTE", "EXISTS", "EXIT",
            "EXPANSION", "EXPIRE", "EXPLAIN", "EXPORT",
            "EXTENDED", "EXTENT_SIZE", "FALSE", "FAST",
            "FAULTS", "FETCH", "FIELDS", "FILE",
            "FILE_BLOCK_SIZE", "FILTER", "FIRST", "FIXED",
            "FLOAT", "FLOAT4", "FLOAT8", "FLUSH",
            "FOLLOWS", "FOR", "FORCE", "FOREIGN", "FORMAT",
            "FOUND", "FROM", "FULL", "FULLTEXT",
            "FUNCTION", "GENERAL", "GENERATED", "GEOMETRY",
            "GEOMETRYCOLLECTION", "GET", "GET_FORMAT",
            "GLOBAL", "GRANT", "GRANTS", "GROUP",
            "GROUP_REPLICATION", "HANDLER", "HASH",
            "HAVING", "HELP", "HIGH_PRIORITY", "HOST",
            "HOSTS", "HOUR", "HOUR_MICROSECOND",
            "HOUR_MINUTE", "HOUR_SECOND", "IDENTIFIED",
            "IF", "IGNORE", "IGNORE_SERVER_IDS", "IMPORT",
            "IN", "INDEX", "INDEXES", "INFILE",
            "INITIAL_SIZE", "INNER", "INOUT",
            "INSENSITIVE", "INSERT", "INSERT_METHOD",
            "INSTALL", "INSTANCE", "INT", "INT1", "INT2",
            "INT3", "INT4", "INT8", "INTEGER", "INTERVAL",
            "INTO", "INVOKER", "IO", "IO_AFTER_GTIDS",
            "IO_BEFORE_GTIDS", "IO_THREAD", "IPC", "IS",
            "ISOLATION", "ISSUER", "ITERATE", "JOIN",
            "JSON", "KEY", "KEYS", "KEY_BLOCK_SIZE",
            "KILL", "LANGUAGE", "LAST", "LEADING", "LEAVE",
            "LEAVES", "LEFT", "LESS", "LEVEL", "LIKE",
            "LIMIT", "LINEAR", "LINES", "LINESTRING",
            "LIST", "LOAD", "LOCAL", "LOCALTIME",
            "LOCALTIMESTAMP", "LOCK", "LOCKS", "LOGFILE",
            "LOGS", "LONG", "LONGBLOB", "LONGTEXT", "LOOP",
            "LOW_PRIORITY", "MASTER",
            "MASTER_AUTO_POSITION", "MASTER_BIND",
            "MASTER_CONNECT_RETRY", "MASTER_DELAY",
            "MASTER_HEARTBEAT_PERIOD", "MASTER_HOST",
            "MASTER_LOG_FILE", "MASTER_LOG_POS",
            "MASTER_PASSWORD", "MASTER_PORT",
            "MASTER_RETRY_COUNT", "MASTER_SERVER_ID",
            "MASTER_SSL", "MASTER_SSL_CA",
            "MASTER_SSL_CAPATH", "MASTER_SSL_CERT",
            "MASTER_SSL_CIPHER", "MASTER_SSL_CRL",
            "MASTER_SSL_CRLPATH", "MASTER_SSL_KEY",
            "MASTER_SSL_VERIFY_SERVER_CERT",
            "MASTER_TLS_VERSION", "MASTER_USER", "MATCH",
            "MAXVALUE", "MAX_CONNECTIONS_PER_HOUR",
            "MAX_QUERIES_PER_HOUR", "MAX_ROWS", "MAX_SIZE",
            "MAX_STATEMENT_TIME", "MAX_UPDATES_PER_HOUR",
            "MAX_USER_CONNECTIONS", "MEDIUM", "MEDIUMBLOB",
            "MEDIUMINT", "MEDIUMTEXT", "MEMORY", "MERGE",
            "MESSAGE_TEXT", "MICROSECOND", "MIDDLEINT",
            "MIGRATE", "MINUTE", "MINUTE_MICROSECOND",
            "MINUTE_SECOND", "MIN_ROWS", "MOD", "MODE",
            "MODIFIES", "MODIFY", "MONTH",
            "MULTILINESTRING", "MULTIPOINT",
            "MULTIPOLYGON", "MUTEX", "MYSQL_ERRNO", "NAME",
            "NAMES", "NATIONAL", "NATURAL", "NCHAR", "NDB",
            "NDBCLUSTER", "NEVER", "NEW", "NEXT", "NO",
            "NODEGROUP", "NONBLOCKING", "NONE", "NOT",
            "NO_WAIT", "NO_WRITE_TO_BINLOG", "NULL",
            "NUMBER", "NUMERIC", "NVARCHAR", "OFFSET",
            "OLD_PASSWORD", "ON", "ONE", "ONLY", "OPEN",
            "OPTIMIZE", "OPTIMIZER_COSTS", "OPTION",
            "OPTIONALLY", "OPTIONS", "OR", "ORDER", "OUT",
            "OUTER", "OUTFILE", "OWNER", "PACK_KEYS",
            "PAGE", "PARSER", "PARSE_GCOL_EXPR", "PARTIAL",
            "PARTITION", "PARTITIONING", "PARTITIONS",
            "PASSWORD", "PHASE", "PLUGIN", "PLUGINS",
            "PLUGIN_DIR", "POINT", "POLYGON", "PORT",
            "PRECEDES", "PRECISION", "PREPARE", "PRESERVE",
            "PREV", "PRIMARY", "PRIVILEGES", "PROCEDURE",
            "PROCESSLIST", "PROFILE", "PROFILES", "PROXY",
            "PURGE", "QUARTER", "QUERY", "QUICK", "RANGE",
            "READ", "READS", "READ_ONLY", "READ_WRITE",
            "REAL", "REBUILD", "RECOVER", "REDOFILE",
            "REDO_BUFFER_SIZE", "REDUNDANT", "REFERENCES",
            "REGEXP", "RELAY", "RELAYLOG",
            "RELAY_LOG_FILE", "RELAY_LOG_POS",
            "RELAY_THREAD", "RELEASE", "RELOAD", "REMOVE",
            "RENAME", "REORGANIZE", "REPAIR", "REPEAT",
            "REPEATABLE", "REPLACE", "REPLICATE_DO_DB",
            "REPLICATE_DO_TABLE", "REPLICATE_IGNORE_DB",
            "REPLICATE_IGNORE_TABLE",
            "REPLICATE_REWRITE_DB",
            "REPLICATE_WILD_DO_TABLE",
            "REPLICATE_WILD_IGNORE_TABLE", "REPLICATION",
            "REQUIRE", "RESET", "RESIGNAL", "RESTORE",
            "RESTRICT", "RESUME", "RETURN",
            "RETURNED_SQLSTATE", "RETURNS", "REVERSE",
            "REVOKE", "RIGHT", "RLIKE", "ROLLBACK",
            "ROLLUP", "ROTATE", "ROUTINE", "ROW", "ROWS",
            "ROW_COUNT", "ROW_FORMAT", "RTREE",
            "SAVEPOINT", "SCHEDULE", "SCHEMA", "SCHEMAS",
            "SCHEMA_NAME", "SECOND", "SECOND_MICROSECOND",
            "SECURITY", "SELECT", "SENSITIVE", "SEPARATOR",
            "SERIAL", "SERIALIZABLE", "SERVER", "SESSION",
            "SET", "SHARE", "SHOW", "SHUTDOWN", "SIGNAL",
            "SIGNED", "SIMPLE", "SLAVE", "SLOW",
            "SMALLINT", "SNAPSHOT", "SOCKET", "SOME",
            "SONAME", "SOUNDS", "SOURCE", "SPATIAL",
            "SPECIFIC", "SQL", "SQLEXCEPTION", "SQLSTATE",
            "SQLWARNING", "SQL_AFTER_GTIDS",
            "SQL_AFTER_MTS_GAPS", "SQL_BEFORE_GTIDS",
            "SQL_BIG_RESULT", "SQL_BUFFER_RESULT",
            "SQL_CACHE", "SQL_CALC_FOUND_ROWS",
            "SQL_NO_CACHE", "SQL_SMALL_RESULT",
            "SQL_THREAD", "SQL_TSI_DAY", "SQL_TSI_HOUR",
            "SQL_TSI_MINUTE", "SQL_TSI_MONTH",
            "SQL_TSI_QUARTER", "SQL_TSI_SECOND",
            "SQL_TSI_WEEK", "SQL_TSI_YEAR", "SSL",
            "STACKED", "START", "STARTING", "STARTS",
            "STATS_AUTO_RECALC", "STATS_PERSISTENT",
            "STATS_SAMPLE_PAGES", "STATUS", "STOP",
            "STORAGE", "STORED", "STRAIGHT_JOIN", "STRING",
            "SUBCLASS_ORIGIN", "SUBJECT", "SUBPARTITION",
            "SUBPARTITIONS", "SUPER", "SUSPEND", "SWAPS",
            "SWITCHES", "TABLE", "TABLES", "TABLESPACE",
            "TABLE_CHECKSUM", "TABLE_NAME", "TEMPORARY",
            "TEMPTABLE", "TERMINATED", "TEXT", "THAN",
            "THEN", "TIME", "TIMESTAMP", "TIMESTAMPADD",
            "TIMESTAMPDIFF", "TINYBLOB", "TINYINT",
            "TINYTEXT", "TO", "TRAILING", "TRANSACTION",
            "TRIGGER", "TRIGGERS", "TRUE", "TRUNCATE",
            "TYPE", "TYPES", "UNCOMMITTED", "UNDEFINED",
            "UNDO", "UNDOFILE", "UNDO_BUFFER_SIZE",
            "UNICODE", "UNINSTALL", "UNION", "UNIQUE",
            "UNKNOWN", "UNLOCK", "UNSIGNED", "UNTIL",
            "UPDATE", "UPGRADE", "USAGE", "USE", "USER",
            "USER_RESOURCES", "USE_FRM", "USING",
            "UTC_DATE", "UTC_TIME", "UTC_TIMESTAMP",
            "VALIDATION", "VALUE", "VALUES", "VARBINARY",
            "VARCHAR", "VARCHARACTER", "VARIABLES",
            "VARYING", "VIEW", "VIRTUAL", "WAIT",
            "WARNINGS", "WEEK", "WEIGHT_STRING", "WHEN",
            "WHERE", "WHILE", "WITH", "WITHOUT", "WORK",
            "WRAPPER", "WRITE", "X509", "XA", "XID", "XML",
            "XOR", "YEAR", "YEAR_MONTH", "ZEROFILL"));

    public MySqlKeyWordsHandler() {
        super(new HashSet<>(KEY_WORDS));
    }

    public MySqlKeyWordsHandler(@Nonnull List<String> keyWords) {
        super(new HashSet<>(keyWords));
    }

    public MySqlKeyWordsHandler(@Nonnull Set<String> keyWords) {
        super(keyWords);
    }

    @Override
    public @Nonnull
    String formatStyle() {
        return "`%s`";
    }

}
