package com.example.demo.java;

import javax.swing.*;
import java.util.Date;
import java.util.StringTokenizer;

/**
 *
 */
public class main {

    public static void main(String[] args) {

        // 运算符顺叙

        // 位移运算符 1>>32 = 1 ，因为  int只有32位  右移32相当于没动
        System.err.println(1>>33);

        int i = 0;
        int j = 3;
        System.err.println(++i + j);
//        System.err.println(i+++j);
        System.err.println(1.0 + j / 2);

        // 运算符

        // 会街区这个字符串 要搞懂含义
        System.err.println(new Date().toString());
        // Sat Oct 07 15:54:30 CST 2023
        // 星期 月  日  时分秒   时区  年
        // substring 从0计数 截取的为 [x,y)
        System.err.println(new Date().toString().substring(11));

        // synchronized notify notifyall  wait

        // java util 单词切割
        StringTokenizer stringTokenizer = new StringTokenizer("hello world!");
        String s = stringTokenizer.nextToken();
        System.err.println(s);

        byte[] bytes = s.getBytes();
        char[] chars = s.toCharArray();


        // 创建一个窗口 ？ 轻量级组件 重量级组件
        // 子窗口 继承 JFrame 通过 super(s) 命名
        // 为对象添加监视器的方法是addActionListener;类必须实现的方法是public void actionPerformed(ActionEvent e)。


        // ActionListener actionPerformed
        // public void actionPerformed(ActionEvent e) 、e.getActionCommand();
        // ActionEvent public Object getSource() 可以获取发生ActionEvent事件的事件源对象的引用
        // ActionEvent public String getActionCommand() 和该事件相关的一个“命令”字符串，对于文本框，当发生ActionEvent事件是，默认的“命令”字符串是文本框中的文本
        // Graphics , paint显示图片，repaint清除上次的paint在执行paint，update自动调用 repaint
        // 菜单条JMenuBar 、 菜单JMenu 、菜单项JMenuItem

        // AudioClip

        // JDBC
        // A.与数据库建立连接
        // B.通过JDBC-API向数据库发送SQL语句
        // C.通过JDBC-API执行SQL语句

        // asc码
        /*
        字符'a'char类型对于的二进制ASCII值为:97
        字符'z'char类型对于的二进制ASCII值为:122
        字符'A'char类型对于的二进制ASCII值为:65
        字符'Z'char类型对于的二进制ASCII值为:90
        字符' 'char类型对于的二进制ASCII值为:32
        字符'0'char类型对于的二进制ASCII值为:48
        字符'9'char类型对于的二进制ASCII值为:57
        字符'/'char类型对于的二进制ASCII值为:47
        */

        // 重写 重载
        // 重载是定义相同的方法名，参数不同；重写是子类重写父类的方法。
        // 重载对返回类型没有要求，而重写要求返回类型，有兼容的返回类型。

        // char 包装类 Character

    }

}
