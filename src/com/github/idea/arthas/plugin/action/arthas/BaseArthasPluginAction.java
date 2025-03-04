package com.github.idea.arthas.plugin.action.arthas;

import com.github.idea.arthas.plugin.utils.OgnlPsUtils;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * @author 汪小哥
 * @date 21-12-2019
 */
public abstract class BaseArthasPluginAction extends AnAction {
    /**
     * 是否支持 枚举
     */
    private Boolean supportEnum = true;

    public Boolean getSupportEnum() {
        return supportEnum;
    }

    public void setSupportEnum(Boolean supportEnum) {
        this.supportEnum = supportEnum;
    }

    public BaseArthasPluginAction() {
    }


    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
        DataContext dataContext = e.getDataContext();
        Project project = CommonDataKeys.PROJECT.getData(dataContext);
        if (project == null) {
            e.getPresentation().setEnabled(false);
            return;
        }
        PsiElement psiElement = CommonDataKeys.PSI_ELEMENT.getData(dataContext);
        if (OgnlPsUtils.isPsiFieldOrMethodOrClass(psiElement)) {
//            final boolean psiElementInEnum = OgnlPsUtils.psiElementInEnum(psiElement);
//            if(Boolean.FALSE.equals(getSupportEnum()) && psiElementInEnum){
//                e.getPresentation().setEnabled(false);
//                return;
//            }
            e.getPresentation().setEnabled(true);
            return;
        }
        e.getPresentation().setEnabled(false);
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        /**
         * {@link com.intellij.ide.actions.CopyReferenceAction}
         */
        DataContext dataContext = event.getDataContext();
        Project project = CommonDataKeys.PROJECT.getData(dataContext);
        if (project == null) {
            return;
        }
        PsiElement psiElement = CommonDataKeys.PSI_ELEMENT.getData(dataContext);
        String className = OgnlPsUtils.getCommonOrInnerOrAnonymousClassName(psiElement);
        String methodName = OgnlPsUtils.getMethodName(psiElement);
       // https://github.com/WangJi92/arthas-idea-plugin/issues/161
        Editor editor = CommonDataKeys.EDITOR.getData(dataContext);
        doCommand(className, methodName, project, psiElement, editor);
    }

    /**
     * 构造命令信息,处理命令后续操作
     *
     * @param className
     * @param methodName
     * @param project
     * @param psiElement
     * @param editor
     * @return
     */
    public void doCommand(String className, String methodName, Project project, PsiElement psiElement, Editor editor) {
    }



}
