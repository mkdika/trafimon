package com.github.mkdika.trafimonserver.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "user")
data class User(

    @Id
    @JsonProperty("id")
    val id: String = "",

    @JsonProperty("email")
    val email: String = "",

    @JsonProperty("name")
    val name: String = "",

    @JsonProperty("pictureUrl")
    val pictureUrl: String = "",

    @JsonProperty("createAt")
    val createAt: Date = Date()
)