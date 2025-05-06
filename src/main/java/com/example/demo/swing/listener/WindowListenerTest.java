package com.example.demo.swing.listener;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;

/**
 * 监听键盘按键
 */
public class WindowListenerTest {

    public static void main(String[] args) {
        JFrame jf = new JFrame();

        jf.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
                System.out.println("windowOpened: 窗口首次变为可见时调用");
            }

            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("windowClosing: 用户试图从窗口的系统菜单中关闭窗口时调用");
            }

            @Override
            public void windowClosed(WindowEvent e) {
                System.out.println("windowClosed: 窗口调用 dispose 而将其关闭时调用");
            }

            @Override
            public void windowIconified(WindowEvent e) {
                System.out.println("windowIconified: 窗口从正常状态变为最小化状态时调用");
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                System.out.println("windowDeiconified: 窗口从最小化状态变为正常状态时调用");
            }

            @Override
            public void windowActivated(WindowEvent e) {
                System.out.println("windowActivated: 窗口变为活动状态时调用");
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                System.out.println("windowDeactivated: 窗口变为不再是活动状态时调用");
            }
        });

        // 窗口焦点监听器
        jf.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                System.out.println("windowGainedFocus: 窗口得到焦点");
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                System.out.println("windowLostFocus: 窗口失去焦点");
            }
        });

        // 窗口状态监听器
        jf.addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                System.out.println("windowStateChanged: " + e.getNewState());
            }
        });
    }




}
