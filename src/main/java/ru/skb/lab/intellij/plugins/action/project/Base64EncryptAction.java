package ru.skb.lab.intellij.plugins.action.project;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.apache.commons.codec.binary.Base64;
import org.jetbrains.annotations.NotNull;

public class Base64EncryptAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent actionEvent) {
        final DataContext dataContext = actionEvent.getDataContext();
        Editor editor = LangDataKeys.EDITOR.getData(dataContext);
        SelectionModel selectionModel = editor.getSelectionModel();
        String text = selectionModel.getSelectedText();
        try {
            String returnValue = Base64.encodeBase64String(text.getBytes());
            final Project project = CommonDataKeys.PROJECT.getData(dataContext);
            WriteCommandAction.writeCommandAction(project).run(() -> {
                editor.getDocument().replaceString(selectionModel.getSelectionStart(), selectionModel.getSelectionEnd(), returnValue);
            });
        } catch (Exception e) {
            Messages.showMessageDialog(e.getLocalizedMessage(), "Base64 encrypt", Messages.getWarningIcon());
        }
    }
}
