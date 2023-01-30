package com.hxy.recipe.dsl

import scalatags.Text.all._

object HtmlDsl {

  def main(args: Array[String]): Unit = {
    val header = Seq("col1", "col2", "col3")
    val lines = Seq(
      Seq("v11", "v12", "v13"), Seq("v21", "v22", "v23"), Seq("v31", "v32", "v33")
    )
    val content = html(
      head(script(src := "...")),
      body(
        div(h1(id := "title", "This is a title")),
        b(style := "font-size:14px;",
          "This is a sub title",
          table(
            id := "summary",
            border := "1",
            style := "text-align:center",
            width := "95%",
            tr(header.map(th(_))),
            lines.map(line => line.map(th(_)))
          )
        )
      )
    )

    println(content)
  }

}
