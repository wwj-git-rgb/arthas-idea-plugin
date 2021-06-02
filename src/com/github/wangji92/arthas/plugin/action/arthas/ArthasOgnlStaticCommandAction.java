package com.github.wangji92.arthas.plugin.action.arthas;

import com.github.wangji92.arthas.plugin.common.command.CommandContext;
import com.github.wangji92.arthas.plugin.common.enums.ShellScriptCommandEnum;
import com.github.wangji92.arthas.plugin.common.enums.ShellScriptVariableEnum;
import com.github.wangji92.arthas.plugin.ui.ArthasActionStaticDialog;
import com.github.wangji92.arthas.plugin.utils.OgnlPsUtils;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * static 方法处理
 *
 * @author 汪小哥
 * @date 21-12-2019
 */
public class ArthasOgnlStaticCommandAction extends AnAction {

    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
        DataContext dataContext = e.getDataContext();
        PsiElement psiElement = CommonDataKeys.PSI_ELEMENT.getData(dataContext);
        boolean anonymousClass = OgnlPsUtils.isAnonymousClass(psiElement);
        if (anonymousClass) {
            e.getPresentation().setEnabled(false);
            return;
        }
        if (OgnlPsUtils.isStaticMethodOrField(psiElement)) {
            e.getPresentation().setEnabled(true);
            return;
        }
        e.getPresentation().setEnabled(true);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        DataContext dataContext = event.getDataContext();
        Project project = CommonDataKeys.PROJECT.getData(dataContext);
        Editor editor = CommonDataKeys.EDITOR.getData(dataContext);
        if (editor == null || project == null) {
            return;
        }
        CommandContext commandContext = new CommandContext(event);
        String command = ShellScriptCommandEnum.OGNL_GETSTATIC.getArthasCommand(commandContext);
        String className = commandContext.getKeyValue(ShellScriptVariableEnum.CLASS_NAME);
        new ArthasActionStaticDialog(project, className, command, "").open("Ognl To Get Static Method Field");
    }

}
