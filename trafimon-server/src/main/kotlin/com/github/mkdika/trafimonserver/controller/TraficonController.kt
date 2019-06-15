package com.github.mkdika.trafimonserver.controller

import com.github.mkdika.trafimonserver.model.Trafi
import com.github.mkdika.trafimonserver.model.trafihttp.TrafiConditionResponse
import com.github.mkdika.trafimonserver.model.trafihttp.TrafiPlaceSearchResponse
import com.github.mkdika.trafimonserver.service.TrafiService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api")
class TraficonController {

    @Autowired
    lateinit var trafiService: TrafiService

    @Autowired
    lateinit var authorizedClientService: OAuth2AuthorizedClientService

    @GetMapping("/place/search", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun searchPlaceByKeyWord(
        @RequestParam(name = "keyword",
            required = false,
            defaultValue = "") keyword: String): ResponseEntity<List<TrafiPlaceSearchResponse>> {

        val result = trafiService.searchPlace(keyword)
        return ResponseEntity.ok().body(result)
    }

    @PostMapping("/trafi",
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun saveOrUpdateTrafi(@RequestBody trafi: Trafi): ResponseEntity<Trafi> {
        return ResponseEntity.ok().body(trafiService.saveOrUpdateTrafi("system", trafi))
    }

    @GetMapping("/trafi", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getTrafiByUserId(auth: OAuth2AuthenticationToken, model: Model): ResponseEntity<List<Trafi>> {

        val result = trafiService.getTrafiByUser("system")
        return ResponseEntity.ok().body(result)
    }

    @DeleteMapping("/trafi/{id}")
    fun removeTrafiById(@PathVariable(value = "id") id: String): ResponseEntity<Void> {

        trafiService.removeTrafi(id)
        return ResponseEntity.noContent().build<Void>()
    }

    @GetMapping("/trafi/condition/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getTrafiConditionByTrafi(@PathVariable(value = "id") id: String): ResponseEntity<TrafiConditionResponse> {

        val result = trafiService.getTrafficCondition(id)
        return ResponseEntity.ok().body(result)
    }
}