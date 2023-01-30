package com.github.andreykudr.todototaskplugin.service

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil


@State(
    name = "org.intellij.sdk.settings.AppSettingsState",
    storages = [Storage("SdkSettingsPlugin.xml")]
)
class SettingsState : PersistentStateComponent<SettingsState?> {

    var url = "https://app.asana.com/api/1.0/tasks?projects=your-project-id"
    var token = "your access token"
    var descriptionPrefix = "Technical dept task:"

    override fun getState(): SettingsState = this

    override fun loadState(state: SettingsState) = XmlSerializerUtil.copyBean(state, this)

    companion object {
        val instance: SettingsState
            get() = ApplicationManager.getApplication().getService(SettingsState::class.java)
    }
}

