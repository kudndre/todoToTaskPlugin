package com.github.andreykudr.todototaskplugin.service.task

interface TaskCreator {

    /**
     * @return task id
     */
    fun create(name: String, description: String): String
}