package com.github.mkdika.trafimonserver.repository

import com.github.mkdika.trafimonserver.model.Trafi
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface TrafiRepository : MongoRepository<Trafi, UUID> {

    fun findByUserId(userId: String): List<Trafi>
}