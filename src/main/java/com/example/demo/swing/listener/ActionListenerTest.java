package com.example.demo.swing.listener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 动作监听 一般点击动作，常用的基础组件都支持
 */
public class ActionListenerTest {

    public static void main(String[] args) {
        final String COMMAND_OK = "OK";
        final String COMMAND_CANCEL = "Cancel";

        JButton okBtn = new JButton("OK");
        okBtn.setActionCommand(COMMAND_OK);             // 按钮绑定动作命令

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setActionCommand(COMMAND_CANCEL);     // 按钮绑定动作命令

        // 创建一个动作监听器实例
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 获取事件源，即触发事件的组件（按钮）本身
                // e.getSource();

                // 获取动作命令
                String command = e.getActionCommand();

                // 根据动作命令区分被点击的按钮
                if (COMMAND_OK.equals(command)) {
                    System.out.println("OK 按钮被点击");

                } else if (COMMAND_CANCEL.equals(command)) {
                    System.out.println("Cancel 按钮被点击");
                }
            }
        };

        // 设置两个按钮的动作监听器（使用同一个监听器实例）
        okBtn.addActionListener(listener);
        cancelBtn.addActionListener(listener);
    }

}
