package com.github.idea.arthas.plugin.action.arthas;

import com.github.idea.arthas.plugin.common.command.CommandContext;
import com.github.idea.arthas.plugin.common.enums.ShellScriptCommandEnum;
import com.github.idea.arthas.plugin.utils.ClipboardUtils;
import com.github.idea.arthas.plugin.utils.NotifyUtils;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;

import static com.github.idea.arthas.plugin.utils.NotifyUtils.COMMAND_COPIED;

/**
 * Monitor method execution statistics, e.g. total/success/failure count, average rt, fail rate, etc.
 *
 * @author 汪小哥
 * @date 09-01-2020
 */
public class ArthasMonitorCommandAction extends BaseArthasPluginAction {

    public ArthasMonitorCommandAction() {
        this.setSupportEnum(true);
    }

    @Override
    public void doCommand(String className, String methodName, Project project, PsiElement psiElement, Editor editor) {
        CommandContext commandContext = new CommandContext(project, psiElement);
        String command = ShellScriptCommandEnum.MONITOR.getArthasCommand(commandContext);
        ClipboardUtils.setClipboardString(command);
        NotifyUtils.notifyMessageOpenTerminal(project, COMMAND_COPIED, command, editor);
    }
}
