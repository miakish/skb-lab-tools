package ru.skb.lab.intellij.plugins.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import git4idea.GitUtil;
import git4idea.repo.GitRepository;
import git4idea.repo.GitRepositoryManager;

/**
 * 09.11.2020
 *
 * @author SSalnikov
 */
public class Util {
    public static String getGitBranch(Project project) {
        final GitRepositoryManager manager = GitUtil.getRepositoryManager(project);
        GitRepository repository = manager.getRepositoryForRootQuick(ProjectUtil.guessProjectDir(project));
        if(repository!=null) {
            return repository.getCurrentBranch().getName();
        } else {
            return "";
        }
    }
}
