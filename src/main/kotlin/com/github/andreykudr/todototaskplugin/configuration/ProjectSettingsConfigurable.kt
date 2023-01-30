package com.github.andreykudr.todototaskplugin.configuration

import AppSettingsComponent
import com.github.andreykudr.todototaskplugin.service.SettingsState
import com.intellij.openapi.options.Configurable
import javax.swing.JComponent


class ProjectSettingsConfigurable: Configurable {

    private lateinit var settingsComponent: AppSettingsComponent

    override fun createComponent(): JComponent {
        settingsComponent = AppSettingsComponent()
        return settingsComponent.panel
    }

    override fun isModified(): Boolean {
        val settings: SettingsState = SettingsState.instance
        return settingsComponent.url != settings.url
                || settingsComponent.token != settings.token
                || settingsComponent.descriptionPrefix != settings.descriptionPrefix
    }

    override fun apply() {
        val settings: SettingsState = SettingsState.instance
        settings.url = settingsComponent.url
        settings.token = settingsComponent.token
        settings.descriptionPrefix = settingsComponent.descriptionPrefix
    }
    override fun reset() {
        val settings: SettingsState = SettingsState.instance
        settingsComponent.url = settings.url
        settingsComponent.token = settings.token
        settingsComponent.descriptionPrefix = settings.descriptionPrefix
    }

    override fun getDisplayName(): String {
        return "TODO to Task"
    }

    override fun getPreferredFocusedComponent(): JComponent {
        return settingsComponent.preferredFocusedComponent
    }
}