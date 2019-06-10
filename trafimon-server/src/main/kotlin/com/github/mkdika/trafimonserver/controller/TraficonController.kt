package com.github.mkdika.trafimonserver.controller

import com.github.mkdika.trafimonserver.model.Trafi
import com.github.mkdika.trafimonserver.model.trafihttp.TrafiPlaceSearchResponse
import com.github.mkdika.trafimonserver.service.TrafiService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class TraficonController {

    @Autowired
    lateinit var trafiService: TrafiService

    @GetMapping("/place/search", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun searchPlaceByKeyWord(
        @RequestParam(name = "keyword",
                      required = false,
                      defaultValue = "") keyword : String ): ResponseEntity<List<TrafiPlaceSearchResponse>> {

        val result = trafiService.searchPlace(keyword)

        return ResponseEntity.ok().body(result)
    }

    @GetMapping("/trafi", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getTrafiByUserId(): ResponseEntity<List<Trafi>> {

        val result = trafiService.getTrafiByUser("system")

        return ResponseEntity.ok().body(result)
    }

    @PostMapping("/trafi",
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun saveOrUpdateTrafi(@Valid @RequestBody trafi: Trafi): ResponseEntity<Trafi> {
        return ResponseEntity.ok().body(trafiService.saveOrUpdateTrafi("system", trafi))
    }

    @DeleteMapping("/trafi/{id}")
    fun removeTrafiById(@PathVariable(value = "id") id: String): ResponseEntity<Void> {

       return trafiService.getTrafiById(id).map {
           trafiService.removeTrafi(it)
           ResponseEntity<Void>(HttpStatus.NO_CONTENT)
       }.orElse(ResponseEntity.notFound().build())
    }
}