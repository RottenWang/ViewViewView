package com.drwang.views.kotlin

import java.io.File

/**
 * Created by Administrator on 2017/7/13.
 */
// bean类 构造方法 会为data clss 提供 getter  setter     equals  tostring  hashcode
data class KotlinBase_2(val name: String, val email: String) {
    //方法的默认参数值
    fun foo(a: Int = 0, b: String = "") {
        val listOf = listOf("a", "b", "c")
        //过滤list
        val filter = listOf.filter { a > 0 }
        //遍历map形list
        val hashMap = HashMap<String, Integer>()
        for ((k, v) in hashMap) {

        }
        //区间
        for (i in 1..100) {
        }  // 闭区间：包含 100
        for (i in 1 until 100) {
        } // 半开区间：不包含 100
        for (x in 2..10 step 2) {
        }
        for (x in 10 downTo 1) {
        }
        var x: Long = 1
        if (x in 1..10) {
        }
        //只读list
        var list = listOf("a", "b", "c")
        //只读map\
        val mapOf = mapOf("a" to 1, "b" to 2)
        //访问map
        mapOf["key"];

//        If not null 缩写

        val files = File("Test").listFiles()

        println(files?.size)
//        If not null and else 缩写

        val files2 = File("Test").listFiles()

        println(files2?.size ?: "empty")
    }

    fun MutableList<Int>.swap(index1: Int, index2: Int) {
        val tmp = this[index1] // “this”对应该列表
        this[index1] = this[index2]
        this[index2] = tmp
    }


    fun a() {
        val l = mutableListOf(1, 2, 3)
        l.swap(0, 2) // “swap()”内部的“this”得到“l”的值
    }


}