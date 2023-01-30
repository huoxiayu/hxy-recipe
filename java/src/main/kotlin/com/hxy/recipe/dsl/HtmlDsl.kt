package com.hxy.recipe.dsl

import kotlinx.html.*
import kotlinx.html.dom.createHTMLDocument
import kotlinx.html.dom.serialize

class HtmlDsl {
}

fun main() {
    val tree = createHTMLDocument().html {
        body {
            h1 {
                +"header"
            }
            div {
                +"content"
                span {
                    +"yo"
                }
            }
        }
    }

    println(tree.serialize(true))

}
