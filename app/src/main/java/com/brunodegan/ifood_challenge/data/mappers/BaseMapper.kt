package com.brunodegan.ifood_challenge.data.mappers

interface BaseMapper<in IN, out OUT> {
    fun map(input: IN): OUT
}