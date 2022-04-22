package com.hxy.recipe.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController("/")
class KotlinController {

    @RequestMapping(value = ["/ping"], method = [RequestMethod.GET])
    fun ping(): String {
        return "kotlin-pong"
    }

}