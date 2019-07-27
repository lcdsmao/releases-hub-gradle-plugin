package com.releaseshub.gradle.plugin.artifacts.api

import com.google.gson.reflect.TypeToken
import com.jdroid.java.http.Server
import com.jdroid.java.http.api.AbstractApiService
import com.jdroid.java.http.mock.AbstractMockHttpService
import com.jdroid.java.http.parser.json.GsonParser
import com.releaseshub.gradle.plugin.artifacts.Artifact

class AppService : AbstractApiService() {

    fun getArtifactsTooUpgrade(artifacts: List<Artifact>): List<Artifact> {
        val httpService = newPostService("artifactsToUpgrade")
        autoMarshall(httpService, artifacts)
        return httpService.execute(GsonParser(object : TypeToken<Collection<Artifact>>() {}.type))
    }

    override fun getServer(): Server {
        return AppServer.DEV
    }

    override fun getAbstractMockHttpServiceInstance(vararg urlSegments: Any?): AbstractMockHttpService {
        return null!!
    }

    override fun isHttpMockEnabled(): Boolean {
       return false
    }
}