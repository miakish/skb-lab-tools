package ru.skb.lab.intellij.plugins.action.project;

import com.intellij.icons.AllIcons;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.ide.actions.CreateFromTemplateAction;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiPackage;
import org.jetbrains.annotations.Nullable;
import ru.skb.lab.intellij.plugins.util.Util;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * newTemplate 12.04.2023
 *
 * @author Сергей
 * Обработчик кастомных шаблонов
 */
public class CustomFileTemplate extends CreateFromTemplateAction<PsiFile> {

    public static final String ACTION_TITLE = "New java file";

    public static final Icon ICON = AllIcons.Nodes.Template;

    public CustomFileTemplate() {
        super("Create from custom template", "Creates a java file from the specified template", ICON);
    }

    @Override
    protected @Nullable PsiFile createFile(String name, String templateName, PsiDirectory dir) {
        Map<String, Object> additionalProperties = new HashMap<>();
        additionalProperties.put("GIT_BRANCH", Util.getGitBranch(dir.getProject()));
        final FileTemplate template = FileTemplateManager.getInstance(dir.getProject()).getInternalTemplate(templateName);
        try {
            return FileTemplateUtil.createFromTemplate(template, name, additionalProperties, dir, null).getContainingFile();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void buildDialog(Project project, PsiDirectory directory, CreateFileFromTemplateDialog.Builder builder) {

        CreateFileFromTemplateDialog.Builder b = builder.setTitle(ACTION_TITLE);
        PsiPackage pkg = JavaDirectoryService.getInstance().getPackage(directory);
        for (FileTemplate ft : FileTemplateManager.getInstance(project).getAllTemplates()) {
            if (ft.getName().contains("SLT-")) {
                if ((ft.getExtension().equals("java") && pkg!=null) || !ft.getExtension().equals("java")) {
                    String tmp = ft.getName().replace("SLT-", "");
                    String templateName = Character.isLetter(tmp.charAt(0)) ? tmp : tmp.substring(1);
                    b.addKind(templateName, FileTemplateUtil.getIcon(ft), ft.getName());
                }
            }
        }
    }

    @Override
    protected String getActionName(PsiDirectory directory, String newName, String templateName) {
        return "Create file " + newName;
    }

    @Override
    public boolean startInWriteAction() {
        return false;
    }
}
