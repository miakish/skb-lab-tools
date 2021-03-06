package ru.skb.lab.intellij.plugins.action.project;

import com.intellij.icons.AllIcons;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.ide.actions.JavaCreateTemplateInPackageAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import ru.skb.lab.intellij.plugins.util.Util;

import java.util.HashMap;
import java.util.Map;

/**
 * 09.11.2020
 *
 * @author SSalnikov
 */
public class MyFileTemplate extends JavaCreateTemplateInPackageAction<PsiClass> {

    public static final String ACTION_TITLE = "New java file";

    public MyFileTemplate() {
        super("Create from template", "Creates a java file from the specified template", AllIcons.Nodes.TestGroup,true);
    }

    @Override
    protected void buildDialog(Project project, PsiDirectory directory, CreateFileFromTemplateDialog.Builder builder) {

        builder.setTitle(ACTION_TITLE)
            .addKind("Class", AllIcons.Nodes.Class, "SLT_Class.java")
            .addKind("Enum", AllIcons.Nodes.Enum, "SLT_Enum.java")
            .addKind("Interface", AllIcons.Nodes.Interface, "SLT_Interface.java")
            .addKind("Bean", AllIcons.Nodes.Interface, "SLT_Bean.java")
            .addKind("SMX Main", AllIcons.Actions.Run_anything, "SLT_Main.java")
            .addKind("RouteBuilder", AllIcons.Nodes.Interface, "SLT_RouteBuilder.java")
        ;
    }

    @Override
    protected String getActionName(PsiDirectory directory, String newName, String templateName) {
        return "Create java file " + newName;
    }

    @Override
    protected PsiElement getNavigationElement(@NotNull PsiClass createdElement) {
        return createdElement.getLBrace();
    }

    @Override
    protected final PsiClass doCreate(PsiDirectory dir, String className, String templateName) throws IncorrectOperationException {
        final Project project = dir.getProject();
        Map<String, String> additionalProperties = new HashMap<>();
        additionalProperties.put("GIT_BRANCH", Util.getGitBranch(project));
        additionalProperties.put("BEAN", "");
        additionalProperties.put("ROUTE_BUILDER", "");
        return JavaDirectoryService.getInstance().createClass(dir, className, templateName, true, additionalProperties);
    }

    @Override
    public boolean startInWriteAction() {
        return false;
    }
}
