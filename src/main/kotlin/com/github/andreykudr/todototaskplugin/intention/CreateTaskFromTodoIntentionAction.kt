package com.github.andreykudr.todototaskplugin.intention

import com.github.andreykudr.todototaskplugin.service.SettingsState
import com.github.andreykudr.todototaskplugin.service.task.TaskCreator
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.ide.actions.CopyFileWithLineNumberPathProvider
import com.intellij.openapi.application.ApplicationManager.getApplication
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.search.PsiTodoSearchHelper
import javax.swing.JTextArea


class CreateTaskFromTodoIntentionAction : PsiElementBaseIntentionAction() {

    private val settings = SettingsState.instance
    private val pathProvider = CopyFileWithLineNumberPathProvider()
    private val taskCreator = getApplication().getService(TaskCreator::class.java)

    override fun startInWriteAction(): Boolean = true

    override fun getText(): String = "Create task from TODO"

    override fun getFamilyName(): String = "TODO"

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement) =
        (element is PsiComment || element.prevSibling is PsiComment)
                && currentTodoItem(project, element) != null

    private fun currentTodoItem(project: Project, element: PsiElement) =
        PsiTodoSearchHelper.SERVICE.getInstance(project)
            .findTodoItems(element.containingFile)
            .find { element.textRange.intersects(it.textRange) }

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        editor?.document?.let { document ->
            currentTodoItem(project, element)?.textRange?.let { todoRange ->
                val originalText = todoRange.substring(document.text)
                val pathToElement = pathProvider.getPathToElement(project, element.containingFile.virtualFile, editor)
                val taskUrl = createTask(originalText, description(originalText, pathToElement, project.name))
                document.replaceString(todoRange.startOffset, todoRange.endOffset, "$originalText $taskUrl")
            }
        }
    }

    private fun createTask(originalText: String, description: String) =
        try {
            taskCreator.create(originalText, description)
        } catch (e: Exception) {
            showErrorPopup(e).let { "" }
        }

    private fun showErrorPopup(e: Exception) {
        JBPopupFactory.getInstance()
            .createComponentPopupBuilder(
                JTextArea(e.message), null
            )
            .setTitle("Task creation error")
            .setAdText("${e.javaClass.name}. Try check plugin settings")
            .createPopup()
            .showInFocusCenter()
    }

    private fun description(originalText: String, pathToElement: String?, projectName: String) =
        """
        ${settings.descriptionPrefix}
        
        $originalText
        
        Path: $pathToElement
        Project: $projectName
        """.trimIndent()
}