package ru.skb.lab.intellij.plugins.action.project;

import com.intellij.icons.AllIcons;
import com.intellij.ide.IdeView;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiPackage;
import org.jetbrains.annotations.NotNull;
import ru.skb.lab.intellij.plugins.ui.AppType;
import ru.skb.lab.intellij.plugins.ui.JavaType;
import ru.skb.lab.intellij.plugins.ui.ProfileType;
import ru.skb.lab.intellij.plugins.ui.ProjectType;
import ru.skb.lab.intellij.plugins.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateSmxBaseBeansAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        final DataContext dataContext = e.getDataContext();
        final IdeView view = LangDataKeys.IDE_VIEW.getData(dataContext);
        if (view == null) {
            return;
        }
        final Project project = CommonDataKeys.PROJECT.getData(dataContext);
        final PsiDirectory psiDirectory = view.getOrChooseDirectory();
        if (psiDirectory == null || project == null) return;

        List<PsiClass> psiClassList = new ArrayList<>();
        Map<String, String> additionalProperties = new HashMap<>();
        additionalProperties.put("GIT_BRANCH", Util.getGitBranch(project));
        additionalProperties.put("COMMENT", "");
        additionalProperties.put("JAVA_VERSION", "JAVA_11");
        additionalProperties.put("PROJECT_PACKAGE", "");
        additionalProperties.put("ACTIVEMQ_ENABLED", String.valueOf(true));
        additionalProperties.put("SSLCONTEXT_ENABLED", String.valueOf(true));
        additionalProperties.put("JDBCTEMPLATE_ENABLED", String.valueOf(false));
        additionalProperties.put("SQLCOMPONENT_ENABLED", String.valueOf(false));
        additionalProperties.put("CRYPTOPRO_METRICS_ENABLED", String.valueOf(false));
        additionalProperties.put("DATASOURCE_ENABLED", String.valueOf(false));
        additionalProperties.put("KAFKACOMPONENT_ENABLED", String.valueOf(false));
        additionalProperties.put("HTTPCLIENTCONFIGURER_ENABLED", String.valueOf(false));

        psiClassList.add(JavaDirectoryService.getInstance().createClass(psiDirectory, "Custom", "SLT_RouteBuilder.java", true, additionalProperties));
        additionalProperties.put("REG_NAME", "operation");
        psiClassList.add(JavaDirectoryService.getInstance().createClass(psiDirectory, "Operation", "SLT_Bean.java", true, additionalProperties));
        additionalProperties.remove("REG_NAME");
        additionalProperties.put("BEAN", "Operation");
        additionalProperties.put("ROUTE_BUILDER", "CustomRouteBuilder");
        psiClassList.add(JavaDirectoryService.getInstance().createClass(psiDirectory, "", "SLT_Main.java", true, additionalProperties));
        for (PsiClass pc: psiClassList) {
            FileEditorManager manager = FileEditorManager.getInstance(project);
            manager.openFile(pc.getContainingFile().getVirtualFile(), true, true);
        }
    }

    public boolean isAvailable(DataContext dataContext) {
        Editor editor = CommonDataKeys.EDITOR.getData(dataContext);
        if (editor != null && editor.getSelectionModel().hasSelection()) {
            return false;
        }
        final Project project = CommonDataKeys.PROJECT.getData(dataContext);
        final IdeView view = LangDataKeys.IDE_VIEW.getData(dataContext);
        if (view == null) {
            return false;
        }
        final PsiDirectory psiDirectory = view.getOrChooseDirectory();
        if (psiDirectory == null || project == null) return false;
        PsiPackage pkg = JavaDirectoryService.getInstance().getPackage(psiDirectory);
        if (pkg == null) {
            return false;
        }
        return view.getDirectories().length != 0;
    }

    @Override
    public void update(@NotNull AnActionEvent anActionEvent) {
        anActionEvent.getPresentation().setIcon(AllIcons.Actions.ListFiles);
        anActionEvent.getPresentation().setVisible(isAvailable(anActionEvent.getDataContext()));
        super.update(anActionEvent);
    }
}
