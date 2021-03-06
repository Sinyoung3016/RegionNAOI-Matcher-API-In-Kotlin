package com.sia.matcher_kotlin_api.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.sia.matcher_kotlin_api.fixture.AreaForTest.name
import com.sia.matcher_kotlin_api.fixture.AreaForTest.pointsOfMap
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class AOIControllerIntegrationTest @Autowired constructor(val mockMvc: MockMvc) {

    @Container
    private val postgresqlContainer = PostgreSQLContainer<Nothing>(
        DockerImageName.parse("postgis/postgis")
            .asCompatibleSubstituteFor("postgres")
    )

    @Test
    fun postAOITest() {
        val uri = "/aois"
        val body = mapOf("name" to name, "area" to pointsOfMap)
        val content = jacksonObjectMapper().writeValueAsString(body)

        mockMvc.perform(
            MockMvcRequestBuilders.post(uri)
                .content(content)
                .contentType(APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
            .andDo(MockMvcResultHandlers.print())
    }
}