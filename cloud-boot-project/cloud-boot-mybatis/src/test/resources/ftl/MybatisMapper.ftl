<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="${mouldName}.${entityPackage}.${className}">
    <#--结果集-->
    <resultMap id="BaseResultMap" type="${mouldName}.${entityPackage}.${className}">
        <#list cloums as c>
            <result column="${c.columnNameL}" property="${c.columnName}" jdbcType="${c.jdbcType}"/>
        </#list>
    </resultMap>

    <#--数据表名称-->
    <sql id="Table_Name">
        ${tableName}
    </sql>

    <#--数据表列-->
    <sql id="Base_Column">
        <#list cloums as c>
            <#if c_has_next>
                ${ c.columnNameL},
            </#if>
            <#if !c_has_next>
                ${ c.columnNameL}
            </#if>
        </#list>
    </sql>

    <#--where条件-->
    <sql id="Base_Where">
        <trim suffixOverrides="AND">
            <#list cloums as c>
            <#if c.javaType ?? &&  c.javaType =="String">
            <if test="${c.columnName} != null and  ${c.columnName} != '' ">
                <#else>
                <if test="${c.columnName} != null ">
                    </#if>
                    ${ c.columnNameL} = ${r"#{"}${ c.columnName}} AND
                </if>
                </#list>
        </trim>
    </sql>

    <sql id="Base_Select">
        SELECT
        <include refid="Base_Column"/>
        FROM
        <include refid="Table_Name"/>
        <where>
            <include refid="Base_Where"/>
        </where>
    </sql>

    <insert id="insert">
        insert into
        <include refid="Table_Name"/>
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <#list cloums as c>
            <#if c.javaType ?? &&  c.javaType =="String">
            <if test="${c.columnName} != null and  ${c.columnName} != '' ">
                <#else>
                <if test="${c.columnName} != null ">
                    </#if>
                    ${ c.columnNameL},
                </if>
                </#list>
        </trim>
        <trim prefix="values (" suffix=")" suffixOverrides=",">
            <#list cloums as c>
            <#if c.javaType ?? &&  c.javaType =="String">
            <if test="${c.columnName} != null and  ${c.columnName} != '' ">
                <#else>
                <if test="${c.columnName} != null ">
                    </#if>
                    ${r"#{"}${c.columnName}},
                </if>
                </#list>
        </trim>
    </insert>

    <update id="update">
        UPDATE
        <include refid="Table_Name"/>
        <set>
            <#list cloums as c>
            <#if c.pkFlag == "N">
            <#if c.javaType ?? &&  c.javaType =="String">
            <if test="${c.columnName} != null and  ${c.columnName} != '' ">
                <#else>
                <if test="${c.columnName} != null ">
                    </#if>
                    ${ c.columnNameL} = ${r"#{"}${ c.columnName}},
                </if>
                </#if>
                </#list>
        </set>
        <where>
            <trim suffixOverrides="AND">
                <#list cloums as c>
                <#if c.pkFlag == "Y">
                <#if c.javaType ?? &&  c.javaType =="String">
                <if test="${c.columnName} != null and  ${c.columnName} != '' ">
                    <#else>
                    <if test="${c.columnName} != null ">
                        </#if>
                        ${ c.columnNameL} = ${r"#{"}${ c.columnName}} AND
                    </if>
                    </#if>
                    </#list>
            </trim>
        </where>
    </update>

    <update id="updateByEntity">
        UPDATE
        <include refid="Table_Name"/>
        <set>
            <#list cloums as c>
            <#if c.javaType ?? &&  c.javaType =="String">
            <if test="s.${c.columnName} != null and s.${c.columnName} != '' ">
                <#else>
                <if test="s.${c.columnName} != null ">
                    </#if>
                    ${c.columnNameL} = ${r"#{"}s.${ c.columnName}},
                </if>
                </#list>
        </set>
        <where>
            <trim prefix="(" suffix=")" suffixOverrides="AND">
                <#list cloums as c>
                <#if c.javaType ?? &&  c.javaType =="String">
                <if test="w.${c.columnName} != null and w.${c.columnName} != '' ">
                    <#else>
                    <if test="w.${c.columnName} != null ">
                        </#if>
                        ${c.columnNameL} = ${r"#{"}w.${ c.columnName}}
                        AND
                    </if>
                    </#list>
            </trim>
        </where>
    </update>
    <delete id="delete">
        DELETE FROM
        <include refid="Table_Name"/>
        <where>
            <include refid="Base_Where"/>
        </where>
    </delete>

    <select id="count" parameterType="java.util.Map" resultType="int">
        select count(1) from
        <include refid="Table_Name"/>
        <where>
            <include refid="Base_Where"/>
        </where>
    </select>

    <select id="selectOne" resultMap="BaseResultMap">
        <include refid="Base_Select"/>
    </select>

    <select id="selectList" resultMap="BaseResultMap">
        <include refid="Base_Select"/>
    </select>

    <select id="selectForUpdate" resultMap="BaseResultMap" useCache="false">
        <include refid="Base_Select"/>
        for update
    </select>

    <#if tablePkSize =="Y">
        <!--根据主键查询-->
        <select id="selectByPrimaryKey" resultMap="BaseResultMap">
            SELECT
            <include refid="Base_Column"/>
            FROM
            <include refid="Table_Name"/>
            <where>
                <trim suffixOverrides="AND">
                    <#list cloums as c>
                    <#if c.pkFlag == "Y">
                    <#if c.javaType ?? &&  c.javaType =="String">
                    <if test="${c.columnName} != null and  ${c.columnName} != '' ">
                        <#else>
                        <if test="${c.columnName} != null ">
                            </#if>
                            ${ c.columnNameL} = ${r"#{"}${ c.columnName}} AND
                        </if>
                        </#if>
                        </#list>
                </trim>
            </where>
        </select>
    </#if>

    <#if tablePkSize =="Y">
        <!--根据主键删除-->
        <delete id="deleteByPrimaryKey">
            DELETE FROM
            <include refid="Table_Name"/>
            <where>
                <trim suffixOverrides="AND">
                    <#list cloums as c>
                    <#if c.pkFlag == "Y">
                    <#if c.javaType ?? &&  c.javaType =="String">
                    <if test="${c.columnName} != null and  ${c.columnName} != '' ">
                        <#else>
                        <if test="${c.columnName} != null ">
                            </#if>
                            ${ c.columnNameL} = ${r"#{"}${ c.columnName}} AND
                        </if>
                        </#if>
                        </#list>
                </trim>
            </where>
        </delete>
    </#if>
</mapper>