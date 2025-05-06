package com.example.demo.swing.listener;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * 监听键盘按键
 */
public class KeyListenerTest {

    public static void main(String[] args) {
        JFrame jf = new JFrame();

        jf.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                // 获取键值，和 KeyEvent.VK_XXXX 常量比较确定所按下的按键
                int keyCode = e.getKeyCode();
                System.out.println("按下: " + e.getKeyCode());
            }

            @Override
            public void keyTyped(KeyEvent e) {
                // e.getKeyChar() 获取键入的字符
                System.out.println("键入: " + e.getKeyChar());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                System.out.println("释放: " + e.getKeyCode());
            }
        });
    }



}
