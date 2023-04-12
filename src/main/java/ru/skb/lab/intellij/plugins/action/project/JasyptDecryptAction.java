package ru.skb.lab.intellij.plugins.action.project;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jetbrains.annotations.NotNull;
import ru.skb.lab.intellij.plugins.ui.PluginSettings;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;

/**
 * newTemplate 30.03.2023
 *
 * @author Сергей
 */
public class JasyptDecryptAction extends AnAction {
    private static final String JASYPT_ENV = "FUSE_ENC";
    private static final String JASYPT_ALGORITHM = "PBEWithMD5AndDES";
    private String jasyptPassword = "";
    //private String jasyptPassword = System.getProperty(JASYPT_ENV);
    transient PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();

    public JasyptDecryptAction() {
        super();
        encryptor.setPoolSize(4);
        //encryptor.setPassword(jasyptPassword);
        encryptor.setAlgorithm(JASYPT_ALGORITHM);
    }


    @Override
    public void actionPerformed(@NotNull AnActionEvent actionEvent) {
        final DataContext dataContext = actionEvent.getDataContext();
        final Project project = CommonDataKeys.PROJECT.getData(dataContext);
        if (StringUtils.isBlank(jasyptPassword)) {
            final PluginSettings poidemSettings = project.getService(PluginSettings.class);
            if(StringUtils.isNotBlank(poidemSettings.getJasyptPassword())) {
                jasyptPassword = poidemSettings.getJasyptPassword();
                encryptor.setPassword(jasyptPassword);
            } else {
                Messages.showMessageDialog("Set a password!", "Jasypt decrypt", Messages.getInformationIcon());
                return;
            }
        }
        Editor editor = LangDataKeys.EDITOR.getData(dataContext);
        SelectionModel selectionModel = editor.getSelectionModel();
        String text = selectionModel.getSelectedText();
        try {
            String returnValue = encryptor.decrypt(text);
            WriteCommandAction.writeCommandAction(project).run(() -> {
                editor.getDocument().replaceString(selectionModel.getSelectionStart(), selectionModel.getSelectionEnd(), returnValue);
            });
        } catch (Exception e) {
            if (e.getClass().isAssignableFrom(EncryptionOperationNotPossibleException.class)) {
                Messages.showMessageDialog("Decryption operation not possible", "Jasypt decrypt", Messages.getWarningIcon());
            } else {
                Messages.showMessageDialog(e.getLocalizedMessage(), "Jasypt decrypt", Messages.getWarningIcon());
            }
        }
    }
}
