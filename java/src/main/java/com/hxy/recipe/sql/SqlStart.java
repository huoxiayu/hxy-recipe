package com.hxy.recipe.sql;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.sql.parser.SQLStatementParser;

public class SqlStart {

    public static void main(String[] args) {
        String sql = "select date, sum(pv) from t_pv where sex = 'female' group by date order by 1 desc";
        SQLStatementParser parser = new SQLStatementParser(sql);
        SQLStatement sqlStatement = parser.parseStatement();
        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        sqlStatement.accept(visitor);

        System.out.println(visitor.getDbType());
        System.out.println(visitor.getTables());
        System.out.println(visitor.getColumns());
        System.out.println(visitor.getConditions());
        System.out.println(visitor.getAggregateFunctions());
    }

}
