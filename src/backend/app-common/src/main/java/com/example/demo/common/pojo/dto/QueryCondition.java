package com.example.demo.common.pojo.dto;

import lombok.Data;
import lombok.Getter;

import java.util.function.Function;

/**
 * 表示三元查询条件
 *
 * @author martix
 * @description
 * @time 5/12/25 7:36 PM
 */
@Data
public class QueryCondition {
    // 字段
    private String field;
    // 操作符
    private String op;
    // 比较值
    private String value;

    public QueryCondition(String field, String op, String value) {
        this.field = field;
        this.op = op;
        this.value = value;
        // check
        QueryOperation.of(op);
    }

    @Getter
    public enum QueryOperation {
        // 等于
        EQ("="),
        // 不等于
        NEQ("!="),
        // 大于
        GT(">"),
        // 大于等于
        GET(">="),
        // 小于
        LT("<"),
        // 小于等于
        LET("<="),
        // 近似, 通常只用于字符串比较
        LIKE("like"),
        ;

        // 前端传入的参数对应
        private final String tag;

        QueryOperation(String tag) {
            this.tag = tag;
        }

        /**
         * 转枚举
         */
        public static QueryOperation of(String tag) {
            return switch (tag) {
                case "=" -> QueryOperation.EQ;
                case "!=" -> QueryOperation.NEQ;
                case ">" -> QueryOperation.GT;
                case ">=" -> QueryOperation.GET;
                case "<" -> QueryOperation.LT;
                case "<=" -> QueryOperation.LET;
                case "like" -> QueryOperation.LIKE;
                default -> throw new IllegalArgumentException("查询条件非法： " + tag);
            };
        }
    }

    // 默认字段转换规则
    public static Function<String, String> defaultFieldConverter = field -> field != null ? field.replaceAll("([A-Z])", "_$1") : null;
}
