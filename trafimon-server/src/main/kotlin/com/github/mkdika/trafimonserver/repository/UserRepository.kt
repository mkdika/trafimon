package com.github.mkdika.trafimonserver.repository

import com.github.mkdika.trafimonserver.model.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, Long>
