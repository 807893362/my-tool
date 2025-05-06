package com.example.demo.swing.listener;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * 鼠标事件监听
 */
public class MouseListenerTest {


    public static void main(String[] args) {
        JPanel panel = new JPanel();

        panel.addMouseListener(new MouseListener() {
            @Override
            public void mouseEntered(MouseEvent e) {
                System.out.println("鼠标进入组件区域");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                System.out.println("鼠标离开组建区域");
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // 获取按下的坐标（相对于组件）
                e.getPoint();
                e.getX();
                e.getY();

                // 获取按下的坐标（相对于屏幕）
                e.getLocationOnScreen();
                e.getXOnScreen();
                e.getYOnScreen();

                // 判断按下的是否是鼠标右键
                e.isMetaDown();

                System.out.println("鼠标按下");
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                System.out.println("鼠标释放");
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // 鼠标在组件区域内按下并释放（中间没有移动光标）才识别为被点击
                System.out.println("鼠标点击");
            }
        });
    }


}
