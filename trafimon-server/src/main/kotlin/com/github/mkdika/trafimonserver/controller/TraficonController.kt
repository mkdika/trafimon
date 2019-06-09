package com.github.mkdika.trafimonserver.controller

import com.github.mkdika.trafimonserver.model.gmphttp.PlaceSearchResponse
import com.github.mkdika.trafimonserver.service.TraficonService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class TraficonController {

    @Autowired
    lateinit var traficonService: TraficonService

    @GetMapping("/place/search",
        produces = [MediaType.APPLICATION_JSON_VALUE])
    fun searchPlaceByKeyWord(
        @RequestParam(name = "keyword",
                       required = false,
                       defaultValue = "") keyword : String
    ): ResponseEntity<List<PlaceSearchResponse>> {

        val result = traficonService.searchPlace(keyword)

        return ResponseEntity.ok().body(result)
    }
}