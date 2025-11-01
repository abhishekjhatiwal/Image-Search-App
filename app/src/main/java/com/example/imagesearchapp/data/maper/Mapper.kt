package com.example.imagesearchapp.data.maper

interface Mapper<F,T> {
    fun map(from : F) : T
}

fun <F,T> Mapper<F,T>.mapAll(list:List<F>) = list.map{map(it)}