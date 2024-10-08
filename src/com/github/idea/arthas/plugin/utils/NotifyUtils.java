package com.github.idea.arthas.plugin.utils;

import com.github.idea.arthas.plugin.action.terminal.tunnel.ArthasTerminalManager;
import com.github.idea.arthas.plugin.setting.AppSettingsState;
import com.github.idea.arthas.plugin.ui.ArthasTunnelTerminalPretreatmentDialog;
import com.intellij.ide.BrowserUtil;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationListener;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.event.HyperlinkEvent;
import java.util.function.Consumer;

/**
 * 通知消息
 *
 * @author 汪小哥
 * @date 21-12-2019
 */
public class NotifyUtils {


    public static final String COMMAND_COPIED = "arthas command copied to clipboard,open arthas to execute command";

    /**
     * 通知消息
     *
     * @param project
     */
    public static void notifyMessageDefault(Project project) {
        notifyMessage(project, COMMAND_COPIED);
    }

    /**
     * 通知消息
     *
     * @param project
     */
    public static void notifyMessageOpenTerminal(Project project, String message, String command, Editor editor) {

        boolean autoOpenArthasTerminal = AppSettingsState.getInstance(project).autoOpenArthasTerminal;

        // 没有运行的时候执行打开对话框的动作
        ArthasTerminalManager instance = ArthasTerminalManager.getInstance(project);
        boolean runningTerminal = false;
        if (instance != null) {
            runningTerminal = instance.isRunning();
        }

        if (autoOpenArthasTerminal && !runningTerminal) {
            new ArthasTunnelTerminalPretreatmentDialog(project, command, editor).open();
            return;
        }
        notifyMessage(project, message);

//        notifyMessage(project, StringUtils.defaultString(message, COMMAND_COPIED), NotificationType.INFORMATION, (Notification arthas) -> {
//            arthas.addAction(new AnActionButton("Open Arthas Terminal") {
//                @Override
//                public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
//                    new ArthasTunnelTerminalPretreatmentDialog(project, command, editor).open();
//                }
//            });
//        });
    }

    /**
     * 消息
     *
     * @param project
     * @param message
     */
    public static void notifyMessage(Project project, String message) {
        notifyMessage(project, message, NotificationType.INFORMATION, null);
    }

    /**
     * 推送消息哦
     *
     * @param project
     * @param message
     * @param type
     */
    public static void notifyMessage(Project project, String message, @NotNull NotificationType type) {
        notifyMessage(project, message, type, null);
    }

    public static void notifyMessage(Project project, String message, @NotNull NotificationType type, Consumer<Notification> buttonHandler) {
        try {
            Notification arthas = NotificationGroupManager.getInstance().getNotificationGroup("arthas").createNotification(message, type);
            arthas.setTitle("Arthas idea plugin");
            arthas.setListener(new NotificationListener.Adapter() {
                @Override
                protected void hyperlinkActivated(@NotNull Notification notification, @NotNull HyperlinkEvent event) {
                    if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                        String url = event.getDescription();
                        BrowserUtil.browse(url);
                    }
                }
            });
            if (buttonHandler != null) {
                buttonHandler.accept(arthas);
            }
            arthas.notify(project);
        } catch (Exception e) {
            //
        }
    }

}
