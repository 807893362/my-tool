package com.example.demo.swing.base;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * 付了钱了
 * https://blog.csdn.net/xietansheng/article/details/72814492
 */
public class JFrameTest {
    // (A、B、C、D、E) (A-B AC-E C-D)
    //

    public static void main(String[] args) {
        // 1. 创建一个顶层容器（窗口）
        JFrame jf = new JFrame("测试窗口");          // 创建窗口
        jf.setSize(500, 600);                       // 设置窗口大小 weith, hight
        jf.setLocationRelativeTo(null);             // 把窗口位置设置到屏幕中心
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // 当点击窗口的关闭按钮时退出程序（没有这一句，程序不会退出）

        // 2. 创建中间容器（面板容器）
        JPanel panel = new JPanel(new FlowLayout());                // 创建面板容器，使用默认的布局管理器
//        JPanel panel = new JPanel(new GridLayout(3, 1));                // 创建面板容器，使用默认的布局管理器

        // 标签
        JLabel label = new JLabel();
        panel.add(label);

        // 文本框
        JTextField textField = new JTextField("textField");
        panel.add(textField);

        // 多行文本框
        JTextArea textArea = new JTextArea();
        textArea.append("textArea" + 1);
        textArea.append("textArea" + 2);
        textArea.insert("textArea" + 3, 3);
        panel.add(textArea);

        // 按钮
        JButton btn = new JButton("测试按钮");
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == btn){
                    String fieldText = textField.getText();
                    String text = label.getText();
                    if(text.startsWith("我点了")){
                        label.setText("我又点了" + fieldText);
                    } else {
                        label.setText("我点了" + fieldText);
                    }
                }
            }
        };
        btn.addActionListener(actionListener);
        panel.add(btn);

        // 进度条
        JProgressBar progressBar = new JProgressBar();
        panel.add(progressBar);

        // 滚动条
        JSlider jSlider = new JSlider();
        jSlider.setMinimum(1);
        jSlider.setMaximum(1000);
        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = jSlider.getValue();
                label.setText("滚动了：" + value);
                int maximum = jSlider.getMaximum();
                progressBar.setValue(value*100/maximum);
            }
        };
        jSlider.addChangeListener(changeListener);
        panel.add(jSlider);

        // 列表(多选)
        JList<String> list = new JList();
        list.setListData(new String[]{"- A", "- B", "- C"});
        ListSelectionListener listener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                List<String> selectedValuesList = list.getSelectedValuesList();
                label.setText("多选：" + selectedValuesList.toString());
            }
        };
        list.addListSelectionListener(listener);
        panel.add(list);

        // 下拉列表框
        JComboBox<String> comboBox = new JComboBox<>(new String[]{"A", "B", "C"});
        ActionListener boxListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == comboBox){
                    label.setText("下拉列表框选：" + comboBox.getSelectedIndex() + comboBox.getSelectedItem());
                }
            }
        };
        comboBox.addActionListener(boxListener);
        panel.add(comboBox);

        // 单选
        JRadioButton radioButtonA = new JRadioButton("A");
        JRadioButton radioButtonB = new JRadioButton("B");
        JRadioButton radioButtonC = new JRadioButton("C");
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(radioButtonA);
        buttonGroup.add(radioButtonB);
        ActionListener radioListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == radioButtonA){
                    label.setText("单选：" + radioButtonA.getText());
                }
                if(e.getSource() == radioButtonB){
                    label.setText("单选：" + radioButtonB.getText());
                }
            }
        };
        radioButtonA.addActionListener(radioListener);
        radioButtonB.addActionListener(radioListener);
        panel.add(radioButtonA);
        panel.add(radioButtonB);
        panel.add(radioButtonC);

        // 复选框
        JCheckBox checkBoxA = new JCheckBox("A");
        JCheckBox checkBoxB = new JCheckBox("B");
        JCheckBox checkBoxC = new JCheckBox("C");
        ActionListener checkBoxListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean A = checkBoxA.isSelected();
                boolean B = checkBoxB.isSelected();
                boolean C = checkBoxC.isSelected();
                label.setText("复选框" + (A?checkBoxA.getText():"") + (B?checkBoxB.getText():"") + (C?checkBoxC.getText():""));
            }
        };
        checkBoxA.addActionListener(checkBoxListener);
        checkBoxB.addActionListener(checkBoxListener);
        checkBoxC.addActionListener(checkBoxListener);
        panel.add(checkBoxA);
        panel.add(checkBoxB);
        panel.add(checkBoxC);

        // 滚动条面板
        JScrollPane scrollPane = new JScrollPane(new JTextArea("测试下 JScrollPane A\nA\nA\nA\nA\nA\nA\nA\nA\nA\nA\nA\nA\nA\nA\nA\nA\nA\nA\nA\nA\nA\nA\nA\nA\nA\nA\nA\nA\nA\nA\nA\nA\nA\nA\nA\n")
                , ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS
                , ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // 层级面板
        JLayeredPane layeredPane = new JLayeredPane();
        // x,y,weith,hight
        panel.setBounds(0, 100, 250, 500);
        scrollPane.setBounds(250, 100, 250, 500);
        layeredPane.add(panel, 1, 0);
        layeredPane.add(scrollPane, 2, 1);

        // 菜单栏
        JMenuBar menuBar = new JMenuBar();
        JMenu menuA = new JMenu("A");
        JMenu menuB = new JMenu("B");
        JMenu menuC = new JMenu("C");
        menuBar.add(menuA);
        menuBar.add(menuB);
        menuBar.add(menuC);
        menuA.add(new JMenuItem("A1"));
        menuA.addSeparator();
        menuA.add(new JMenuItem("A2"));
        menuB.add(new JMenuItem("B1"));
        menuB.add(new JMenuItem("B2"));
        menuC.add(new JMenuItem("C1"));
        menuC.add(new JMenuItem("C2"));
        jf.setJMenuBar(menuBar);

        // 4. 把 面板容器 作为窗口的内容面板 设置到 窗口
        jf.setContentPane(layeredPane);

        // 5. 显示窗口，前面创建的信息都在内存中，通过 jf.setVisible(true) 把内存中的窗口显示在屏幕上。
        jf.setVisible(true);
    }

}

