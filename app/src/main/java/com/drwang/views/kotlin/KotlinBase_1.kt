package com.drwang.views.kotlin

import android.util.Log

/**
 * Created by Administrator on 2017/7/12.
 */
class KotlinBase_1() {
    //带两个参数
    fun sum(a: Int, b: Int): Int {
        return a + b;
    }

    val TAG = "KotlinBase_1"
    fun sum2(a: Int, b: Int) = a + b;
    //返回值为unit  可以省略 无意义
    fun printSum(a: String, b: String): Unit {
        Log.d(a, b)

    }

    //使用条件表达式
    fun maxOf(a: Int, b: Int): Int {
        if (a > b) {
            return a
        } else {
            return b
        }
    }

    fun test() {
        //定义局部变量
        val a: Int = 1//立即赋值
        val b = 2;//自动推断出int类型
        val c: Int//如果没有初始类型则不能省略
        c = 3   //赋值
        //可变变量
        var x = 5;
        x += 1
        //字符串模板
        val i = 10;
        val s = "i = $i"
        val ss = "abc"
        val str = "$s.length is {$s.length}"
    }

    fun sumOx(a: Int, b: Int) = if (a > b) a else b

    fun returnInt(str: String): Int? {
        //....

        return 9
    }

    //使用返回可空值得函数
    fun printInt(arg1: String, arg2: String) {
        val x = returnInt("a")
        val y = returnInt("y")
        if (x != null && y != null) {
            Log.d(TAG, x.toString() + y.toString())
        } else {
            print("either $x or $y is not a number")
        }

    }

    //for
    fun forFor() {
        val items = listOf("apple", "ad", "asdf")

        //for 循环
        for (item in items) {
            Log.d(TAG, item)
        }
        //for 循环 带index
        for (index in items.indices) {
            Log.d(TAG, items.get(index))
            Log.d(TAG, "item at $index is ${items[index]}")
        }
    }

    fun whileWhile() {
        val items = listOf("apple", "ad", "asdf")
        //while 循环
        var index = 0;
        while (index < items.size) {
            Log.d(TAG, "item at $index is ${items.get(index)}")
            index++
        }
    }

    fun whenWhen() {
        var index: Int = 0
        //when 表达式 相当于switch
        var aa = when (index) {
            1 and 2 -> sum(index, index)
            2 -> sumOx(index, index)
            else -> "as"
        }
        Log.d(TAG,aa.toString());
    }

    fun inIn() {
        //使用in  运算符来检测某个数字是否在指定区间内 包含头 包含尾
        val x = 10;
        val y = 9
        if (x in 1..y + 1) {
            Log.d(TAG, "fits in ranges")
        }
    }

    fun outOut() {
        //检测某个数字是否在区间外
        val list2 = listOf("a", "b", "c")
        if (-1 !in 0..list2.lastIndex) {
            Log.d(TAG, "-1 is out of range")
        }
        if (list2.size !in list2.indices) {
            Log.d(TAG, "list size it out of valid list indices range too")
        }
    }

    fun diedai() {
        //区域迭代
        for (x in 1..5) {
            Log.d(TAG, x.toString()) //12345
        }
        //数列迭代
        for (x in 1..10 step 2) {
            Log.d(TAG, x.toString()) //1 3 5 7 9
        }
        for (x in 9 downTo 0 step 3) {
            Log.d(TAG, x.toString()) //9 6 3 0
        }
        //对集合进行迭代
        val items = listOf("a", "b", "c")
        for (item in items) {
            Log.d(TAG, item) //a b c
        }
    }

    fun inObj() {
        val items = listOf("apple", "b", "c")
        //使用in 运算符判断集合内是否包含某实例
        when {
            "orange" in items -> Log.d(TAG, "juicy")
            "apple" in items -> Log.d(TAG, "hello apple")
        }
    }

    fun lambda() {
        //使用lambda 表达式来过滤和映射集合
        var fruits = listOf("banana", "avocado", "apple", "kiwi")
        fruits.filter { it.startsWith("a") }.sortedBy { it }.map { it.toUpperCase() }.forEach { Log.d(TAG,it) }
    }

    //获取str.length
    fun getStrLength(obj: Any): Int? {
        if (obj is String) {
            //obj在此条件下 自动转换为String
            return obj.length
        } else {
            return null
        }
        if (obj !is String) return null
        //obj 在此条件下 自动转换为String
        return obj.length
//        obj 在&& 右边 自动转换为String
        if (obj is String && obj.length > 0) {
            return obj.length
        }
        return null
    }
}