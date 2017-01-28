package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Taylor on 1/18/2017.
 */
public class Menu extends JFrame implements ActionListener {
    public static void main(String[] args) {
        new Menu().setVisible(true);
    }
    GridBagConstraints gbc= new GridBagConstraints();
    public Menu() {
        super("Main Menu");
        setSize(500, 200);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        gbc.insets = new Insets(5,5,5,5);

        initialize();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonClicked = e.getActionCommand();

        if (buttonClicked.equals("Start")){
            System.out.println("Starting...");
        } else if (buttonClicked.equals("Exit")){
            System.exit(3);
        }

    }

    private void initialize() {
        /*JLabel labelText = new JLabel("<html>Welcome to Data Collection!<br></html>", JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        add(labelText, gbc);
        */

        JButton startButton = new JButton("Start");
        gbc.gridx = 0;
        gbc.gridy = 3;
        startButton.setActionCommand("Start");
        add(startButton, gbc);

        JButton exitButton = new JButton("Exit");
        gbc.gridx = 0;
        gbc.gridy = 5;
        exitButton.setActionCommand("Exit");
        add(exitButton,gbc);

        startButton.addActionListener(this);
        exitButton.addActionListener(this);




    }
}
