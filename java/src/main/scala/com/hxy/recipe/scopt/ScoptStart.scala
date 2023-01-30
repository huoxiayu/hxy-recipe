package com.hxy.recipe.scopt

import scopt.OptionParser

object ScoptStart {

	case class Config(num: Int, path: String, enable: Boolean)

	val configParser: OptionParser[Config] = new OptionParser[Config]("ScoptStart") {
		override def showUsageOnError = true

		head("app", "v1.0")

		opt[Int]('n', "num") required() action {
			(t, cfg) => cfg.copy(num = t)
		} text "num"

		opt[String]('p', "path") required() action {
			(s, cfg) => cfg.copy(path = s)
		} text "path"

		opt[Boolean]('e', "enable") required() action {
			(w, cfg) => cfg.copy(enable = w)
		} text "enable"


		help("help") text "please input necessary parameters"
	}

	// program arguments: -n 1 -p /a/b/c -e true
	def main(args: Array[String]): Unit = {
		for (config <- configParser.parse(args, Config(0, "", enable = false))) {
			println(config)
		}
	}

}
