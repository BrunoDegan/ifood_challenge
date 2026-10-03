package com.brunodegan.androidplayground.data.mappers

interface BaseMapper<in IN, out OUT> {
    fun map(input: IN): OUT
}
