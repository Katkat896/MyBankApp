package com.kath.mybankapp;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        // Java module fix for FlatLaf
        try {
            Module swing = javax.swing.plaf.basic.BasicLookAndFeel.class.getModule();
            Module current = MainDashboard.class.getModule();
            swing.addOpens("javax.swing.plaf.basic", current);
        } catch (Exception ignored) {}

        // Set FlatLaf and apply look and feel
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());

            // 🔵 ROUND CORNERS
            UIManager.put("Component.arc", 15);
            UIManager.put("ProgressBar.arc", 15);
            UIManager.put("TextComponent.arc", 15);
            UIManager.put("Button.arc", 15);

        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Launch app
        new MainDashboard();
    }
}
