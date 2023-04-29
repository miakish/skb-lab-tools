package ru.skb.lab.intellij.plugins.action.project;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.apache.commons.codec.binary.Base64;
import org.jetbrains.annotations.NotNull;

/**
 * newTemplate 30.03.2023
 *
 * @author Сергей
 */
public class Base64DecryptAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent actionEvent) {
        final DataContext dataContext = actionEvent.getDataContext();
        Editor editor = LangDataKeys.EDITOR.getData(dataContext);
        SelectionModel selectionModel = editor.getSelectionModel();
        String text = selectionModel.getSelectedText();

        try {
            if(Base64.isBase64(text)) {
                String returnValue = new String(Base64.decodeBase64(text));
                final Project project = CommonDataKeys.PROJECT.getData(dataContext);
                WriteCommandAction.writeCommandAction(project).run(() -> {
                    editor.getDocument().replaceString(selectionModel.getSelectionStart(), selectionModel.getSelectionEnd(), returnValue);
                });
            } else {
                Messages.showMessageDialog("Decode operation not possible", "Base64 decrypt", Messages.getWarningIcon());
            }
        } catch (Exception e) {
            Messages.showMessageDialog(e.getLocalizedMessage(), "Base64 decrypt", Messages.getWarningIcon());
        }
    }
}
