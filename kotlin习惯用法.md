# 习惯用法

## 1.创建DOTS(POJOs/POCOs)

```kotlin
data class Customer(val name: String, val email: String)
```

会为 `Customer` 类提供以下功能：

- 所有属性的 getters （对于 *var* 定义的还有 setters）
- `equals()`
- `hashCode()`
- `toString()`
- `copy()`
- 所有属性的 `component1()`、 `component2()`……等等（参见[数据类](https://www.kotlincn.net/docs/reference/data-classes.html)）

## 2.函数的默认参数

```kotlin
fun foo(a: Int = 0, b: String = ""){...}
```

## 3.过滤list

```kotlin
val positives = list.filter{ x -> x > 0}
```

或者可以更短

```kotlin
val positives = list.filter{ it > 0 }
```

## 4.String 内插

```kotlin
println("Name $name")
```

## 5.类型判断

```kotlin
when (x) {
    is Foo ->// ...
    is Bar -> //...
    else -> //...
}
```

## 6.遍历map/pair型list

```kotlin
for ((k, v) in map){
println("$k -> $v")
}
```

`k`,`v` 可以改成任意名字

## 7.使用区间

```kotlin
for (i in 1..100){...}//闭区间 :包含100
for(i in 1 until 100){...}//半开区间: 不包含100
for(x in 2..10 step 2){...}
for(x in 10 downTo 1){...}
if(x in 1..10){...}
```



## 8.只读list

```kotlin
val list = listOf("a","b","c")
```

## 9.只读map

```kotlin
val map = mapOf("a" to 1,"b" to 2,"c" to 3)
```

## 10.访问map

```kotlin
println(map["key"])
map["key"] = value
```

## 11.延迟属性

```kotlin
val p :String by lazy{
    //......
}
```

## 12.扩展函数

```kotlin
fun String.spaceToCamlCase(){.......}
"convert this to camelCase".spaceToCamelCase()
```

## 13.创建单例

```kotlin
object Resource {
	val name = "Name"
}
```

## 14.if not null 缩写

```kotlin
val files = File("Test").listFiles()
println(files?.size)
```

## 15.if not null and else 缩写

```kotlin
val files = File("Test").listFiles()
println(files?.size ?: "empty")
```

## 16.if null 执行一个语句

```kotlin
val values = ......
val emial = values["email"]?: throw IllegalStateException("Email is missing!")
```

## 17.在可能会空的集合中取第一元素

```kotlin
val emails = ...//可能会是空集合
val mainEmail = emails.firstOrNull() ?: ""
```

## 18.if not null 执行代码

```kotlin
val value = ......
value?.let{
	...//代码会执行到此处,加入data 不为null
}
```

## 19.映射可空值(如果非空的话)

```kotlin
val value = ......
val mapped = value?.let{transfromValue(it)} ?:defaultValueIfIsNull
```

## 20.返回when表达式

```kotlin
fun transform(color: String): Int{
    return when(color){
    "Red" -> 0 
    "Green" -> 1
    "Blue" -> 2
    else ->throw IllegalArgumentException("Invalid color param value")
    }
 
}
```

## 21.try/catch 表达式

```kotlin
fun test(){
    val result = try{
        count()
    } catch(e: ArithmeticException){
        throw IllegalStateException(e)
    }
    //使用result
}
```

## 22.if 表达式

```kotlin
fun foo(param: Int){
	val result = if(param == 1){
		"one"
	}else if(param == 2){
    	"two"
	}else {
    	"three"
	}
}
```

## 23.返回类型为Unit的方法的Builder风格用法

```kotlin
fun arrayOfMinusOnes(size: Int):IntArray{
	return IntArray(size).apply{fill(-1)}
}
```

## 24.单表达式函数

```kotlin
fun theAnswer() = 42
//等价于
fun theAnswer(): Int{
    return 42
}
```

单表达式函数与其他惯用法一起使用能简化代码例如和when表达式一起使用:

```kotlin
fun transform(color:String) :Int = when (color){
	"Red" -> 0
    "Green"->1
    "Blue" ->2
    else -> throw IllegalArgumentException("Invalid color param value")
}
```

## 25.对一个对象实例调用多个方法(with)

```kotlin
class Trutle{
    fun penDown()
    fun penUp()
    fun turn(degress: Double)
    fun forward(pixels: Double)
}

val myTurtle = turtle()
with(myTurtle){//画一个100像素的正方形
    penDown()
    for(i in 1..4){
        forward(100.0)
        turn(90.0)
    }
    penUp()  
}
```

## 26.Java 7 的 try with resources

```kotlin
val stream = Files.newInputStream(Paths.get("/some/file.txt"))
stream.buffered().reader().user{
    reader->println(reader.readText())
}
```

## 27.对于需要泛型信息的泛型函数的适宜形式

```kotlin
//  public final class Gson {
//     ……
//     public <T> T fromJson(JsonElement json, Class<T> classOfT) throws JsonSyntaxException {
//     ……

inline fun <reified T: Any> Gson.fromJson(json: JsonElement): T = this.fromJson(json, T::class.java)
```

## 28.使用可空布尔

```kotlin
val b: Boolean? = ......
if (b == true){
......
}else {
	//`b`是 false 或者 null
}
```



