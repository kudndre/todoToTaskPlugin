import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JPasswordField


class AppSettingsComponent {

    internal val panel: JPanel

    private val myUrl = JBTextField()
    private val myToken = JPasswordField()
    private val myDescriptionPrefix = JBTextField()

    init {
        panel = FormBuilder.createFormBuilder()
            .addLabeledComponent("Asana project url", myUrl)
            .addLabeledComponent("Asana user token", myToken)
            .addLabeledComponent("Description prefix", myDescriptionPrefix)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    val preferredFocusedComponent: JComponent
        get() = myUrl

    var url: String
        get() = myUrl.text
        set(newText) {
            myUrl.text = newText
        }

    var token: String
        get() = myToken.text
        set(newText) {
            myToken.text = newText
        }

    var descriptionPrefix: String
        get() = myDescriptionPrefix.text
        set(newText) {
            myDescriptionPrefix.text = newText
        }
}