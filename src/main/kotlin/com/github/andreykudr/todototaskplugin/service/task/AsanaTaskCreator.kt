package com.github.andreykudr.todototaskplugin.service.task

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.andreykudr.todototaskplugin.exception.TaskCreationException
import com.github.andreykudr.todototaskplugin.service.SettingsState
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.Charset
import java.util.stream.Collectors
import java.util.stream.Stream

class AsanaTaskCreator : TaskCreator {

    private val responseFieldJsonPath = "/data/permalink_url"
    private val objectMapper = ObjectMapper()
    private val settings = SettingsState.instance

    override fun create(name: String, description: String): String {
        val client = HttpClient.newBuilder().build();
        val request = HttpRequest.newBuilder()
            .uri(URI.create(settings.url))
            .POST(HttpRequest.BodyPublishers.ofString(urlEncodedBody(name, description)))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .header("Authorization", "Bearer ${settings.token}")
            .build();

        val response = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        val jsonTree = objectMapper.readTree(response)
        return jsonTree.at(responseFieldJsonPath).textValue()
            ?: throw TaskCreationException("Can't get created task link \n${jsonTree.toPrettyString()}");
    }

    private fun urlEncodedBody(originalText: String, description: String): String {
        return Stream.of(
            "name" to originalText,
            "notes" to description,
            "assignee" to "me"
        )
            .map { (key, value) -> "$key=${encodeParam(value)}" }
            .collect(Collectors.joining("&"))
    }

    private fun encodeParam(originalText: String): String? = URLEncoder.encode(originalText, Charset.defaultCharset())
}