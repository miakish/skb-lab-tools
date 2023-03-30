package ru.skb.lab.intellij.plugins.action.project;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jetbrains.annotations.NotNull;

/**
 * newTemplate 30.03.2023
 *
 * @author Сергей
 */
public class JasyptDecryptAction extends AnAction {
    private static final String JASYPT_ENV = "FUSE_ENC";
    private static final String JASYPT_ALGORITHM = "PBEWithMD5AndDES";
    private String jasyptPassword = "dozer6";
    //private String jasyptPassword = System.getProperty(JASYPT_ENV);
    transient PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();

    public JasyptDecryptAction() {
        super();
        encryptor.setPoolSize(4);
        encryptor.setPassword(jasyptPassword);
        encryptor.setAlgorithm(JASYPT_ALGORITHM);
    }


    @Override
    public void actionPerformed(@NotNull AnActionEvent actionEvent) {
        final DataContext dataContext = actionEvent.getDataContext();
        Editor editor = LangDataKeys.EDITOR.getData(dataContext);
        SelectionModel selectionModel = editor.getSelectionModel();
        String text = selectionModel.getSelectedText();
        String returnValue = encryptor.decrypt(text);
        final Project project = CommonDataKeys.PROJECT.getData(dataContext);
        WriteCommandAction.writeCommandAction(project).run(() -> {
            editor.getDocument().replaceString(selectionModel.getSelectionStart(), selectionModel.getSelectionEnd(), returnValue);
        });
    }
}
