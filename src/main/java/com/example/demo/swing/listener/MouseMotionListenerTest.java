package com.example.demo.swing.listener;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * 鼠标移动/拖动监听
 */
public class MouseMotionListenerTest {

    public static void main(String[] args) {
        JPanel panel = new JPanel();

        panel.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // 鼠标保持按下状态移动即为拖动
                System.out.println("鼠标拖动");
            }
            @Override
            public void mouseMoved(MouseEvent e) {
                System.out.println("鼠标移动");
            }
        });
    }

}
