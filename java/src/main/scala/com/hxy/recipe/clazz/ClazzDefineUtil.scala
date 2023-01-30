package com.hxy.recipe.clazz

import java.net.URI

import javax.tools.JavaFileObject.Kind

object ClazzDefineUtil {

	val clazzPath: String = "com/hxy/DefinedClass"
	val clazzContent: String =
		"""
		  |package com.hxy;
		  |
		  |public class DefinedClass {
		  |
		  |   public static void print() {
		  |        System.out.println("$$");
		  |    }
		  |
		  |}
		""".stripMargin

	def clazzUrl(clazzPath: String): URI = URI.create(clazzPath + Kind.SOURCE.extension)

	def clazzContent(replace: String): String = clazzContent.replace("$$", replace)

}
