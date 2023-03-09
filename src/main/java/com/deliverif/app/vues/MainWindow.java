package com.deliverif.app.vues;

import javax.swing.*;

public class MainWindow extends JFrame {
    public MainWindow() {
        super();
        this.setTitle("Hello World");
        this.setContentPane(this.map);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }
    public Map map;

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
