package com.example.demo.swing.listener;

import javax.swing.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * 鼠标滚动监听
 */
public class MouseWheelListenerTest {

    public static void main(String[] args) {
        JPanel panel = new JPanel();

        panel.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                // e.getWheelRotation() 为滚轮滚动多少的度量
                System.out.println("mouseWheelMoved: " + e.getWheelRotation());
            }
        });
    }


}
