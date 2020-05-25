#### scala quick start
可以使用[scala在线ide](https://scalafiddle.io/)  
或者scala交互式解释器REPL(read eval print loop)  
或者[scala exercises](https://www.scala-exercises.org/)

#### scala中的类型
scala并不刻意区分基本类型和包装类型。
基本类型和包装类型之间的转换是scala编译器的工作。
举例来说：scala中的Int数组，最终在虚拟机中得到的是int[]数组。

#### scala中的类型转换
在scala中，推荐使用方法而不是强制类型转换
```scala
    1.toDouble
    "123".toInt
```
#### scala中的操作符其实是方法，如下两种写法是等价的
```scala
    1 + 2
    1.+(2)
```
例如你可以像操作普通数字的方式来使用BigInt(即可以定义+-*/等方法)
```scala
    val x: BigInt = 123456789
    val y: BigInt = 987654321
    println(x + y)
```
亦或者
```scala
    "string" * 5
```

#### scala中无参数方法调用可以不带圆括号，比如你可以
```scala
    "hello".distinct
```

#### 一般来讲，推荐无参且无副作用的方法不带圆括号

#### scala中没有++或者--，因为Int是不可变的

#### 中缀表示法(以下两种写法是等价的)
```scala
    1 to 10
    1.to(10)
```

#### scala中通常使用伴生对象的apply方法来构造对象，如
```scala
    BigInt(123) * BigInt(321)
```

#### scala中表达式是有值的
```scala
    val x = 3
    val y = if (x % 2 == 0) x * 2 else x + 1
    println(y)  // 4
    println(if (x < 3) 1) // (), Unit类型，代表"无值占位符"
```

#### 严格来讲，void没有值，Unit有一个代表"无值"的值(空钱包和钱包里的钞票写着"0元")

#### 赋值操作的返回是Unit类型，因此不能连写
```scala
    var i: Int = 0
    println(i = 1)  // ()
```

#### 块表达式常用于初始化操作需要多步才能完成的情况

```scala
    import math._
    val x0 = 1
    val y0 = 2
    val x1 = 3
    val y1 = 4
    val distance = {
      val dx = x0 - x1
      val dy = y0 - y1
      sqrt(dx * dx + dy * dy)
    }
    println(distance)
```

#### scala中没有受检异常，但有特殊的NoThing类型，这在if/else表达式中很有用
````scala
    def sqrt(x: Double): Double = {
      if (x >= 0) {
        math.sqrt(x)
      } else {
        throw new IllegalArgumentException("negative!")
      }
    }

    println(sqrt(4)) // 2.0
    println(sqrt(-1)) // java.lang.IllegalArgumentException: negative!
````
