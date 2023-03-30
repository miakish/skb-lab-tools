package ru.skb.lab.intellij.plugins.action.project;

import com.intellij.icons.AllIcons;
import com.intellij.ide.IdeView;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.file.PsiJavaDirectoryImpl;
import org.jetbrains.annotations.NotNull;
import ru.skb.lab.intellij.plugins.ui.CreateSMXProjectDialog;
import ru.skb.lab.intellij.plugins.ui.JavaType;
import ru.skb.lab.intellij.plugins.ui.ProjectType;
import ru.skb.lab.intellij.plugins.util.Util;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * newTemplate 28.03.2023
 *
 * @author Сергей
 * Создание нового проекта
 */
public class CreateSMXProjectAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent actionEvent)  {
        final DataContext dataContext = actionEvent.getDataContext();
        final IdeView view = LangDataKeys.IDE_VIEW.getData(dataContext);
        if (view == null) {
            return;
        }
        final Project project = CommonDataKeys.PROJECT.getData(dataContext);
        final PsiDirectory psiDirectory = view.getOrChooseDirectory();

        if (psiDirectory == null || project == null) return;
//        PsiElement psiElement = actionEvent.getData(LangDataKeys.PSI_ELEMENT);
//        if (!(psiElement.getClass().isAssignableFrom(PsiJavaDirectoryImpl.class))) {
//            return;
//        }
//        Messages.showMessageDialog(psiElement.getClass().getCanonicalName(), "actionPerformed", Messages.getInformationIcon());

        CreateSMXProjectDialog dialog = new CreateSMXProjectDialog(project, psiDirectory);
        dialog.show();
        if (dialog.getExitCode() == DialogWrapper.OK_EXIT_CODE) {
            try {
                Map<String, Object> additionalProperties = dialog.getProperties();
                List<PsiFile> psiClassList = new ArrayList<>();

                String root = "%s/%s/%s";
                String path = psiDirectory.getVirtualFile().getPath();
                String projectName = additionalProperties.get("ARTIFACT_ID").toString();
                String projectPackage = additionalProperties.get("PROJECT_PACKAGE").toString().replace(".","/");
                ProjectType projectType = ProjectType.fromValue(additionalProperties.get("PROJECT_TYPE").toString());
                JavaType javaType = JavaType.fromValue(additionalProperties.get("JAVA_VERSION").toString());

                try {
                    VfsUtil.createDirectories(String.format(root, path, projectName, "src/main/java/"+projectPackage));
                    VfsUtil.createDirectories(String.format(root, path, projectName, "src/main/resources"));
                    VfsUtil.createDirectories(String.format(root, path, projectName, "src/test/java"));
                    if (javaType.equals(JavaType.JAVA_CRYPTO_PRO)) {
                        VfsUtil.createDirectories(String.format(root, path, projectName, "src/main/resources/cert"));
                    }
                    if(projectType.equals(ProjectType.SMX_K8S)) {
                        VfsUtil.createDirectories(String.format(root, path, projectName, "src/main/resources/etc"));
                        VfsUtil.createDirectories(String.format(root, path, projectName, "src/main/resources/dev"));
                        VfsUtil.createDirectories(String.format(root, path, projectName, "src/main/resources/test"));
                        VfsUtil.createDirectories(String.format(root, path, projectName, "src/main/resources/reg"));
                        VfsUtil.createDirectories(String.format(root, path, projectName, "src/main/resources/stage"));
                    } else if(projectType.equals(ProjectType.SMX_INVEST)) {
                        VfsUtil.createDirectories(String.format(root, path, projectName, "src/main/resources/etc"));
                        VfsUtil.createDirectories(String.format(root, path, projectName, "src/main/resources/test"));
                    }
                }
                catch (IOException e) {
                    Messages.showMessageDialog(e.getMessage(), "createDirectoriess", Messages.getErrorIcon());
                }

                PsiDirectory prjDir = psiDirectory.findSubdirectory(projectName);
                //Messages.showMessageDialog(prjDir.getVirtualFile().getPath(), "prjDir", Messages.getInformationIcon());
                String myPackagePath = projectName+"/src/main/java/"+projectPackage;
                PsiDirectory packageDir = getSubdirByPath(psiDirectory, myPackagePath);
                //Messages.showMessageDialog(packageDir.getVirtualFile().getPath(), "packageDir", Messages.getInformationIcon());

                if(prjDir!=null) {

                    psiClassList.add(createFromTemplate("SLT_SMX_pom.xml", "pom.xml", additionalProperties, project, prjDir));
                    String myResourcesPath = projectName+"/src/main/resources";
                    PsiDirectory resourcesDir = getSubdirByPath(psiDirectory, myResourcesPath);
                    if (javaType.equals(JavaType.JAVA_CRYPTO_PRO)) {
                        psiClassList.add(createFromTemplate("SLT_SMX_jul.properties", "jul.properties", additionalProperties, project, resourcesDir));
                        psiClassList.add(createFromTemplate("SLT_SMX_CamelListener.java", "CamelListener.java", additionalProperties, project, packageDir));
                        psiClassList.add(createFromTemplate("SLT_SMX_GOST.java", "GOST.java", additionalProperties, project, packageDir));
                    }
                    if(projectType.equals(ProjectType.SMX_K8S)) {
                        additionalProperties.put("TEST", "true");
                        psiClassList.add(createFromTemplate("SLT_SMX_app.properties", "app.properties", additionalProperties, project, resourcesDir.findSubdirectory("etc")));
                        psiClassList.add(createFromTemplate("SLT_SMX_app.properties", "app.properties", additionalProperties, project, resourcesDir.findSubdirectory("dev")));
                        psiClassList.add(createFromTemplate("SLT_SMX_app.properties", "app.properties", additionalProperties, project, resourcesDir.findSubdirectory("test")));
                        additionalProperties.remove("TEST");
                        psiClassList.add(createFromTemplate("SLT_SMX_app.properties", "app.properties", additionalProperties, project, resourcesDir.findSubdirectory("reg")));
                        psiClassList.add(createFromTemplate("SLT_SMX_app.properties", "app.properties", additionalProperties, project, resourcesDir.findSubdirectory("stage")));

                        psiClassList.add(createFromTemplate("SLT_SMX_config.yml", "config.yml", additionalProperties, project, resourcesDir.findSubdirectory("dev")));
                        psiClassList.add(createFromTemplate("SLT_SMX_config.yml", "config.yml", additionalProperties, project, resourcesDir.findSubdirectory("test")));
                        psiClassList.add(createFromTemplate("SLT_SMX_config.yml", "config.yml", additionalProperties, project, resourcesDir.findSubdirectory("reg")));
                        psiClassList.add(createFromTemplate("SLT_SMX_config.yml", "config.yml", additionalProperties, project, resourcesDir.findSubdirectory("stage")));
                    } else if(projectType.equals(ProjectType.SMX_INVEST)) {
                        additionalProperties.put("TEST", "true");
                        psiClassList.add(createFromTemplate("SLT_SMX_app.properties", "app.properties", additionalProperties, project, resourcesDir.findSubdirectory("etc")));
                        psiClassList.add(createFromTemplate("SLT_SMX_app.properties", "app.properties", additionalProperties, project, resourcesDir.findSubdirectory("test")));
                        psiClassList.add(createFromTemplate("SLT_SMX_config.yml", "config.yml", additionalProperties, project, resourcesDir.findSubdirectory("test")));
                        additionalProperties.remove("TEST");
                    }

                    //SmartPsiElementPointer<PsiFile> pointer = SmartPointerManager.getInstance(project).createSmartPsiElementPointer(psiFile);
                    additionalProperties.put("GIT_BRANCH", Util.getGitBranch(project));
                    additionalProperties.put("COMMENT", "");
                    additionalProperties.put("NAME", "Custom");
                    psiClassList.add(createFromTemplate("SLT_RouteBuilder.java", "CustomRouteBuilder.java", additionalProperties, project, packageDir));

                    additionalProperties.put("REG_NAME", "operation");
                    additionalProperties.remove("NAME");
                    additionalProperties.put("NAME", "Operation");
                    psiClassList.add(createFromTemplate("SLT_Bean.java", "Operation.java", additionalProperties, project, packageDir));

                    additionalProperties.put("ROUTE_BUILDER", "CustomRouteBuilder");
                    additionalProperties.remove("REG_NAME");
                    additionalProperties.put("BEAN", "Operation");
                    additionalProperties.remove("NAME");
                    additionalProperties.put("NAME", "");
                    psiClassList.add(createFromTemplate("SLT_Main.java", "Main.java", additionalProperties, project, packageDir));
//                    for (PsiFile pc : psiClassList) {
//                        FileEditorManager manager = FileEditorManager.getInstance(project);
//                        manager.openFile(pc.getContainingFile().getVirtualFile(), true, true);
//                    }
                } else {
                    Messages.showMessageDialog("prjDir=null", "prjDir", Messages.getErrorIcon());
                }
            }  catch (Exception e) {
                Messages.showMessageDialog(e.getMessage(), "packageDir", Messages.getErrorIcon());
            }
        }
    }

    private PsiFile createFromTemplate(String templateName, String fileName, Map<String, Object> additionalProperties, Project project, PsiDirectory psiDirectory) throws Exception {
        final FileTemplate template = FileTemplateManager.getInstance(project).getInternalTemplate(templateName);
        return FileTemplateUtil.createFromTemplate(template, fileName, additionalProperties, psiDirectory, null).getContainingFile();
    }

    public PsiDirectory getSubdirByPath(PsiDirectory baseDir, String path) {
        String[] dirs = path.split("/");
        PsiDirectory directory = baseDir;
        for (String dir : dirs) {
            directory = directory.findSubdirectory(dir);
        }
        return directory;
    }

    @Override
    public void update(@NotNull AnActionEvent anActionEvent) {
        anActionEvent.getPresentation().setIcon(AllIcons.Actions.ModuleDirectory);
        PsiElement psiElement = anActionEvent.getData(LangDataKeys.PSI_ELEMENT);
        if (!(psiElement.getClass().isAssignableFrom(PsiJavaDirectoryImpl.class))) {
            anActionEvent.getPresentation().setVisible(false);
        }
        super.update(anActionEvent);
    }
}
