# kotlin基本语法
内容均来自https://www.kotlincn.net/docs/reference/basic-syntax.html
## 1.定义包

包的生命应处于源文件顶部:

 ```kotlin
package my.demo

import java.util.*
// ......
 ```

目录与包的结构无需匹配:源代码可以在文件的任意位置

## 2.定义函数

带有两个```Int```参数,返回```Int```的函数:

```kotlin
fun sum(a:Int,b:Int):Int{
    return a + b
}
```

将表达式作为函数体,返回值类型自动推断的函数:

```kotlin
fun sum(a :Int,b:Int) = a + b
```

函数返回无意义的值:

```kot
fun printSum(a:Int,b:Int): Unit {
    println("sum of $a and $b is ${a + b})
}
```

```Unit```返回类型可以省略:



```kotlin
fun printSum(a:Int,b:Int) {
    println("sum of $a and $b is ${a + b})
}
```

## 3.定义变量

一次赋值(只读)的局部变量:

```kotlin
val a: Int = 1 //立即赋值
val b = 2 //自动推断出 `Int` 类型
val c = Int // 如果没有初始值类型不能忽略
c = 3 //明确赋值
```

可变变量:

```kotlin
var x = 5 //自动推断出 `Int` 类型
x += 1
```

顶层变量:

```kotlin
val PI = 3.14
var x = 0

fun incrementX(){
	x += 1
}
```

## 4.注释

 正如 Java 和JavaScript,Kotlin支持行注释及块注释

```
// 这是一个行注释

/* 这是一个多行的
   块注释。 */
```

 与java不同的是,Kotlin的块注释可以嵌套.

## 5.使用字符串模版

```kotlin
var a = 1
//模版中的简单名称:
var s1 = "a is $a"

a = 2
// 模版中的任意表达式
val s2 = "${s1.replace("is","was")}, but now is $a"
```

## 6.使用条件表达式

```kotlin
fun maxOf(a: Int,b: Int):Int {
    if(a > b){
		return a
    }else {
        return b
    }
}
```

使用 `if`作为表达式

```kotlin
fun maxOf(a: Int,b: Int) = if(a > b) a else b 
```

## 7.使用可空值及`null`检测

当某个变量的值可以为`null`的时候,必须在声明处的类型后添加`?`来标识该引用可为空.

如果`str`的内容不是数字返回null:

```kotlin
fun parseInt(str: String) :Int?{
	//......
}

```

使用返回可空值的函数:

```kotlin
fun printProduct(arg1: String,arg2 :String){
	val x = parseInt(arg1)
    val y = parseInt(arg2)
    //直接使用 `x * y`会导致编译错误,因为他们可能为null
    if(x != null && y != null){
        //在空检测后,x 和 y 会自动转换为非空值(non-nullable)
        print(x * y)
    }else {
        println("either `$arg1` or `$arg2` is not a number) 
    }
}
```

或者

```kotlin
 // ……
if (x == null) {
    println("Wrong number format in arg1: '$arg1'")
    return
}
if (y == null) {
    println("Wrong number format in arg2: '$arg2'")
    return
}
// 在空检测后，x 和 y 会自动转换为非空值
println(x * y)
```

参见[空安全](https://www.kotlincn.net/docs/reference/null-safety.html)。

## 8.使用类型检测及自动类型转换

`is` 运算符检测一个表达式是否某类型的一个实例.如果一个不可变的局部变量或属性已经判断出为类型,那么检测后的分支中可以直接当作该类型使用,无需显式转换:

```kotlin
fun getStringLength(obj: Any): Int? {
    if (obj is String) {
        // `obj` 在该条件分支内自动转换成 `String`
        return obj.length
    }
    // 在离开类型检测分支后，`obj` 仍然是 `Any` 类型
    return null
}
```

或者

```kotlin
fun getStringLength(obj: Any): Int? {
    // `obj` 在 `&&` 右边自动转换成 `String` 类型
    if (obj is String && obj.length > 0) {
        return obj.length
    }
    return null
}
fun getStringLength(obj: Any): Int? {
    // `obj` 在 `&&` 右边自动转换成 `String` 类型
    if (obj is String && obj.length > 0) {
        return obj.length
    }

    return null
}
```

注: 自动转换只支持方法内变量或者不可变类变量 可变类变量无法进行自动转换

## 9.使用`for`循环

```kotlin
val items = listOf("apple", "banana", "kiwifruit")
for (item in items) {
    println(item)
}
```

或者

```kotin
val items = listOf("apple", "banana", "kiwifruit")
for (index in items.indices) {
    println("item at $index is ${items[index]}")
}
```

## 10.使用`when`表达式

```kotlin
fun describe(obj: Any): String =
when (obj) {
    1          -> "One" //obj  == 1
    "Hello"    -> "Greeting" //obj == "Hello"
    is Long    -> "Long" //obj is Long 
    !is String -> "Not a string"// ob !is String
    else       -> "Unknown" //else 
}
```

## 11.使用区间(range)

使用`in`运算符来检测某个数字是否在指定区间内:

```kotlin
val x = 10
val y = 9
if (x in 1..y+1) {
    println("fits in range")
}
```

检测某个数字是否在指定区间外:

```kotlin
val list = listOf("a", "b", "c")

if (-1 !in 0..list.lastIndex) {
    println("-1 is out of range")
}
if (list.size !in list.indices) {
    println("list size is out of valid list indices range too")
}
```

区间迭代:

```kotlin
for(x in 1..5){
    print(x)
}
```

数列迭代:

```kotlin
for(x in 1..10 step 2){
	print(x)//打印出13579
}
pringln()
for (x in 9 downTo 0 step 3){
    print(x) //打印出 9630
}
```

## 12.使用集合

对集合进行迭代:

```kotlin
for(item in items){
println(item)
}
```

使用`in`运算符来判断集合内是否包含其实例:

```kotl
when{
    "orange" in items -> println("juicy)
    "apple" in items -> println("apple is fine too")
}

```

使用lambda 表达式来过滤(filter) 和映射(map)集合:

```kotlin
fruits
.filter{it.startsWith("a")}
.sortedBy{it}
.map{it.toUpperCase()}
.forEach{println(it)}
```

## 13.创建基本类实例

```kotlin
val rectangle = Rectangle(5.0,2.0) // 不需要"new"关键字
val triangle = Trangle(3.0,4.0,5.0)
```

