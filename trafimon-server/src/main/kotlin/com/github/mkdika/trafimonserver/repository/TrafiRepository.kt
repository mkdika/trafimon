package com.github.mkdika.trafimonserver.repository

import com.github.mkdika.trafimonserver.model.Trafi
import org.springframework.data.mongodb.repository.MongoRepository

interface TrafiRepository : MongoRepository<Trafi, String> {

    fun findByUserId(userId: String): List<Trafi>

}