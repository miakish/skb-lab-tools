package ru.skb.lab.intellij.plugins.action.project;

import com.intellij.icons.AllIcons;
import com.intellij.ide.IdeView;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import org.jetbrains.annotations.NotNull;
import ru.skb.lab.intellij.plugins.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateAllAction extends AnAction {

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

    @Override
    public void update(@NotNull AnActionEvent anActionEvent) {
        anActionEvent.getPresentation().setIcon(AllIcons.Actions.ListFiles);
        super.update(anActionEvent);
    }
}
