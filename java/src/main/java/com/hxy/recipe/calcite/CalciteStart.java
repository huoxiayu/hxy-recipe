package com.hxy.recipe.calcite;

import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.dialect.CalciteSqlDialect;
import org.apache.calcite.sql.parser.SqlParser;

// https://calcite.apache.org/docs/tutorial.html
// https://calcite.apache.org/docs/stream.html
// https://calcite.apache.org/develop/#source-code
public class CalciteStart {

    public static void main(String[] args) throws Exception {
        SqlParser sqlParser = SqlParser.create("select * from tb");
        SqlNode sqlNode = sqlParser.parseStmt();
        System.out.println(sqlNode);
    }

}
