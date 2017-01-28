package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.*;
import java.util.Arrays;



public class DataCollection{

    final static int SUBOUTLENGTH = 17, TYPEOUTLENGTH = 13, QWORDLENGTH = 7, AUXVERBOUTLENGTH = 14, NEGOUTLENGTH = 3, MAINVERBOUTLENGTH = 13, STYPEOUTLENGTH = 13;

    static String qWord, type, sType, question, ansIntend, ansGiven, testStr;
    static String qWordOut, typeOut, sTypeOut, questionOut, negOut;
    static String subjectOutput, mainVerbOutput, auxVerbOutput;
    static String choiceIntendQuestion, choiceGivenQuestion;
    static String filename, currentDirectory, defaultDirectory = null, defaultFilename = null, newDirectory="", newFilename;
    static String filenameForm;
    static String subjectAmtStr, choiceAmtStr, mainVerbAmtStr, auxVerbAmtStr, sTypeStr, qWordStr, typeStr, isNegStr, yesNoIntendStr, yesNoGivenStr, choiceIntendStr, choiceGivenStr;
    static String resumePoint;
    static String dividerLine = "", borderLine;

    static String[] outLine = new String[40];
    static String[] choice = new String[10];
    static String[] subject = new String[10], mainVerb = new String[10], auxVerb = new String[10];
    static String[] subjectOut = new String[10], mainVerbOut = new String[10], auxVerbOut = new String[10];

    static int isDone = 1, isNeg, useDefault, setDefault, cancelChoice;
    static int yesNoIntend, yesNoGiven, choiceIntend, choiceGiven;
    static int typeNum, qWordNum, sTypeNum;
    static int subjectAmt, choiceAmt, mainVerbAmt = 0, auxVerbAmt;
    static int subjectForm, auxVerbForm, mainVerbForm, qWordForm, typeForm, sTypeForm, negForm, questionLength, lineNum, choiceLineNum = 0;
    static int auxLengthMax, mainLengthMax, subLengthMax, greatestAmt, greatestLength;
    static int oldLineIndex;
    static int[] subLength = new int[10];
    static int[] mainLength = new int[10];
    static int[] auxLength = new int[10];
    static int[] allAmts = new int[5];
    static int[] allLengths = new int[10];
    static int[] lineIndex = new int[10];
    static char questionEndChar;
    static boolean isBe = false, brSet = false;
    static boolean needsResume = false;
    static boolean haveLineIndex = false;

    static JFileChooser chooser = new JFileChooser();
    static BufferedReader br = null;
    static BufferedWriter bw = null;
    static FileWriter fw = null;

    static JFrame menu;
    static JPanel textPanel, buttonPanel;
    static JButton startButton, exitButton, defaultsButton, aboutButton;
    static JLabel labelText;
    static ActionListener actionListener;
    static GridBagConstraints gbc = new GridBagConstraints();

    public DataCollection() {
        menu();
    }

    public static void menu(){
        menu = new JFrame("Main Menu");
        menu.setVisible(true);
        menu.setSize(400, 200);
        menu.setResizable(false);
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menu.setLayout(new GridLayout(2,1));

        textPanel = new JPanel(new GridBagLayout());
        textPanel.setBackground(Color.WHITE);
        buttonPanel = new JPanel(null);
        buttonPanel.setBackground(Color.WHITE);

        actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String buttonClicked = e.getActionCommand();
                System.out.println(buttonClicked);
                menu.setVisible(false);
                if (buttonClicked.equals("Start")){
                    System.out.println("Starting...");
                    mainProgram();
                } else if (buttonClicked.equals("Exit")){
                    System.exit(3);
                } else if (buttonClicked.equals("Defaults")){
                    resetDefaults();
                } else if (buttonClicked.equals("About")){
                    //TODO: open README
                }

            }
        };

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        //initialize();


        labelText = new JLabel("Welcome to Data Collection!");
        labelText.setFont(new Font("", Font.PLAIN, 30));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        textPanel.add(labelText, gbc);


        startButton = new JButton("Start");
        startButton.setBounds(70, 5, 125, 30);
        gbc.gridx = 0;
        gbc.gridy = 3;
        startButton.setActionCommand("Start");
        buttonPanel.add(startButton,gbc );

        exitButton = new JButton("Exit");
        exitButton.setBounds(205, 45, 125, 30);
        gbc.gridx = 2;
        gbc.gridy = 5;
        exitButton.setActionCommand("Exit");
        buttonPanel.add(exitButton,gbc );

        aboutButton = new JButton("About");
        aboutButton.setBounds(70, 45, 125, 30);
        gbc.gridx = 0;
        gbc.gridy = 5;
        aboutButton.setActionCommand("About");
        buttonPanel.add(aboutButton,gbc );

        defaultsButton = new JButton("Reset Defaults");
        defaultsButton.setBounds(205, 5, 125, 30);
        gbc.gridx = 2;
        gbc.gridy = 3;
        defaultsButton.setActionCommand("Defaults");
        buttonPanel.add(defaultsButton,gbc );

        menu.add(textPanel);
        menu.add(buttonPanel);

        startButton.addActionListener(actionListener);
        exitButton.addActionListener(actionListener);
        aboutButton.addActionListener(actionListener);
        defaultsButton.addActionListener(actionListener);
    }

    public static void main(String[] args) {
        new DataCollection();
    }

    public static void mainProgram() {
        //Get report file directory
        //Locate appdata.txt
        chooser.setCurrentDirectory(new File("."));
        currentDirectory = (chooser.getCurrentDirectory()).toString();
        System.out.println(currentDirectory);
        while (!brSet) {
            try {
                br = new BufferedReader(new FileReader(currentDirectory + "\\appdata.txt"));
                brSet = true;
            } catch (FileNotFoundException e) {
                brSet = false;
                e.printStackTrace();
                //If there is no appdata.txt: make it!
                System.out.println("Appdata file not found.");
                try {
                    fw = new FileWriter(currentDirectory + "\\appdata.txt");
                    bw = new BufferedWriter(fw);
                    bw.write("NoDefault");
                    bw.newLine();
                    bw.write("NoDefault");
                    System.out.println("Appdata file created, no default report directory set");
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                } finally {
                    try {
                        if (bw != null) {
                            bw.close();
                        }
                        if (fw != null) {
                            fw.close();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        //Find default save location
        try {
            assert br != null; //Because of while loop above (while !brset)
            defaultDirectory = br.readLine();
            defaultFilename = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //If there is no default...
        assert defaultDirectory != null;
        assert defaultFilename != null;
        if (defaultDirectory.equals("NoDefault") || defaultFilename.equals("NoDefault")) {
            do {
                JOptionPane.showMessageDialog(null, new JLabel("Please choose a default location for the report file", JLabel.CENTER), "File Location", JOptionPane.PLAIN_MESSAGE);
                chooser.setCurrentDirectory(null);
                chooser.setDialogTitle("Choose File Save Location");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    defaultDirectory = (chooser.getSelectedFile()).toString();
                }
                defaultFilename = JOptionPane.showInputDialog(null, new JLabel("Enter a default name for the report file", JLabel.CENTER), "File Location", JOptionPane.PLAIN_MESSAGE);
                while (defaultFilename.contains(Character.toString('.')) || defaultFilename.contains(Character.toString('/')) || defaultFilename.contains(Character.toString('\\')) || defaultFilename.contains(Character.toString('*')) || defaultFilename.contains(Character.toString('?')) || defaultFilename.contains(Character.toString('<')) || defaultFilename.contains(Character.toString('>')) || defaultFilename.contains(Character.toString('\"')) || defaultFilename.contains(Character.toString(':')) || defaultFilename.contains(Character.toString('|'))) {
                    defaultFilename = JOptionPane.showInputDialog(null, "Invalid input. File name cannot contain any of the following characters:\n     .  >  <  ?  /  \\  \"  :  |\n\nEnter a default name for the report file", "ERROR_Input", JOptionPane.PLAIN_MESSAGE);
                }
            } while (defaultDirectory.equals("NoDefault") || defaultFilename.equals("NoDefault"));

            //Write defaults to appdata.txt
            try {
                fw = new FileWriter(currentDirectory + "\\appdata.txt");
                bw = new BufferedWriter(fw);
                bw.write(defaultDirectory);
                bw.newLine();
                bw.write(defaultFilename);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bw != null) {
                        bw.close();
                    }
                    if (fw != null) {
                        fw.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            filename = defaultDirectory + "\\" + defaultFilename;
            JOptionPane.showMessageDialog(null, new JLabel("Default file location set successfully", JLabel.CENTER), "File Location", JOptionPane.PLAIN_MESSAGE);
        }
        while (isDone == 1) {
            try {
                //Question
                resumePoint = "question";
                enterQuestion();

                //Sentence Type
                resumePoint = "stype";
                enterSentenceType();

                //Question Type
                resumePoint = "qtype";
                enterQuestionType();

                //Choices
                resumePoint = "choices";
                enterChoices();

                //Question Word
                resumePoint = "qword";
                enterQuestionWord();

                //Subject
                resumePoint = "subject";
                enterSubject();

                //Main Verb
                resumePoint = "mainverb";
                enterMainVerb();

                //Auxiliary Verb (there is no aux verb if main verb is "be")
                resumePoint = "auxverb";
                enterAuxVerb();

                //Negativity
                resumePoint = "neg";
                enterNegativity();

                //Intended Answer
                resumePoint = "intendedans";
                enterIntendedAnswer();

                //Given Answer
                resumePoint = "givenans";
                enterGivenAnswer();


                //Final Output

                outLine[0] = "Question: " + questionOut + "\n";
                outLine[1] = " ";
                outLine[2] = "   ----------------------------------------------- ";
                outLine[3] = "  | Sentence Type | Question Type | Q. Word | Neg |";
                outLine[4] = "  |---------------|---------------|---------|-----|";
                outLine[5] = "  | " + sTypeOut + " | " + typeOut + " | " + qWordOut + " | " + negOut + " |";
                outLine[6] = "   ----------------------------------------------- ";

                //Find maximum length of each term to be placed in 2nd chart
                for (int auxLengthCount = 0; auxLengthCount < auxVerbAmt; auxLengthCount++) {
                    auxLength[auxLengthCount] = auxVerb[auxLengthCount].length();
                }
                for (int mainVerbLengthCount = 0; mainVerbLengthCount < mainVerbAmt; mainVerbLengthCount++) {
                    mainLength[mainVerbLengthCount] = mainVerb[mainVerbLengthCount].length();
                }
                for (int subLengthCount = 0; subLengthCount < subjectAmt; subLengthCount++) {
                    subLength[subLengthCount] = subject[subLengthCount].length();
                }
                auxLengthMax = Arrays.stream(auxLength).max().getAsInt();
                mainLengthMax = Arrays.stream(mainLength).max().getAsInt();
                subLengthMax = Arrays.stream(subLength).max().getAsInt();

                //Find greatest number of terms
                allAmts[0] = subjectAmt;
                allAmts[1] = mainVerbAmt;
                allAmts[2] = auxVerbAmt;
                greatestAmt = Arrays.stream(allAmts).max().getAsInt();

                //Find greatest length
                allLengths[0] = subLengthMax;
                allLengths[1] = auxLengthMax;
                allLengths[2] = mainLengthMax;
                greatestLength = Arrays.stream(allLengths).max().getAsInt();

                System.out.println(greatestLength);

                //Format Output for Subject
                subjectOutput = "";
                for (int subFormatCount = 0; subFormatCount < greatestAmt; subFormatCount++) {
                    try {
                        if (subject[subFormatCount].equals(null)) {
                            subjectOut[subFormatCount] = "N/A";
                        }
                    } catch (NullPointerException subNull) {
                        subNull.printStackTrace();
                        subject[subFormatCount] = "N/A";
                    }
                    subjectOut[subFormatCount] = subject[subFormatCount];
                    subjectForm = greatestLength - subject[subFormatCount].length();
                    for (int subjectCount = 0; subjectCount < subjectForm; subjectCount++) {
                        subjectOut[subFormatCount] += " ";
                    }
                    subjectOutput = subjectOutput + subjectOut[subFormatCount] + " | ";
                }

                //Format Output for Main Verb
                mainVerbOutput = "";
                MainVerbOutput:
                for (int mainFormatCount = 0; mainFormatCount < greatestAmt; mainFormatCount++) {
                    try {
                        if (mainVerb[mainFormatCount].equals(null)) {
                            mainVerb[mainFormatCount] = "N/A";
                        }
                        mainVerbOut[mainFormatCount] = mainVerb[mainFormatCount];
                    } catch (NullPointerException mainVerbNull) {
                        mainVerbNull.printStackTrace();
                        mainVerb[mainFormatCount] = "N/A";
                    }
                    mainVerbOut[mainFormatCount] = mainVerb[mainFormatCount];
                    mainVerbForm = greatestLength - mainVerb[mainFormatCount].length();
                    for (int mainVerbCount = 1; mainVerbCount <= mainVerbForm; mainVerbCount++) {
                        mainVerbOut[mainFormatCount] = mainVerbOut[mainFormatCount] + " ";
                    }
                    mainVerbOutput = mainVerbOutput + mainVerbOut[mainFormatCount] + " | ";
                }

                //Format Output for Aux Verb
                auxVerbOutput = "";
                AuxVerbOutput:
                for (int auxFormatCount = 0; auxFormatCount < greatestAmt; auxFormatCount++) {
                    try {
                        if (auxVerb[auxFormatCount].equals(null)) {
                            auxVerb[auxFormatCount] = "N/A";
                        }
                    } catch (NullPointerException auxVerbNull) {
                        auxVerbNull.printStackTrace();
                        auxVerb[auxFormatCount] = "N/A";
                    }
                    auxVerbOut[auxFormatCount] = auxVerb[auxFormatCount];

                    auxVerbForm = greatestLength - auxVerb[auxFormatCount].length();
                    for (int auxVerbCount = 1; auxVerbCount <= auxVerbForm; auxVerbCount++) {
                        auxVerbOut[auxFormatCount] = auxVerbOut[auxFormatCount] + " ";
                    }
                    auxVerbOutput = auxVerbOutput + auxVerbOut[auxFormatCount] + " | ";
                }
                //ToDo: Finish this
                //Goes up to the | before subject
                //Need to add 3 more dashes per term
                outLine[7] = "   ------------------------";
                for (int outLine7Count = 1; outLine7Count <= greatestAmt; outLine7Count++) {
                    for (int outLine7Count2 = 1; outLine7Count2 <= greatestLength; outLine7Count2++) {
                        outLine[7] = outLine[7] + "-";
                    }
                }
                for (int outLine7Count3 = 1; outLine7Count3 <= greatestAmt; outLine7Count3++) {
                    outLine[7] = outLine[7] + "---";
                }

                outLine[7] = outLine[7] + " ";
                borderLine = outLine[7];
                outLine[8] = "  | Subject(s):        | " + subjectAmt + " | " + subjectOutput;

                dividerLine = "  " + outLine[8].substring((outLine[8].indexOf("|")), (outLine[8].length() - 1)).replaceAll("[^|]", "-");

                outLine[9] = dividerLine;
                outLine[10] = "  | Main Verb(s):      | " + mainVerbAmt + " | " + mainVerbOutput;
                outLine[11] = dividerLine;
                outLine[12] = "  | Auxiliary Verb(s): | " + auxVerbAmt + " | " + auxVerbOutput;
                outLine[13] = borderLine;
                outLine[14] = " ";

                //Print choices
                if (typeNum == 3) {
                    outLine[15] = "  Choices:";
                    for (int choiceOutput = 0; choiceOutput <= choiceAmt; choiceOutput++) {
                        try {
                            if (!(choice[choiceOutput].equals(null))) {
                                outLine[choiceOutput + 16] = "    " + choice[choiceOutput];
                            } else {
                                //Print answers if choices
                                outLine[choiceOutput + 16] = " ";
                                outLine[choiceOutput + 17] = "Intended Answer: " + ansIntend;
                                outLine[choiceOutput + 18] = "Given Answer: " + ansGiven;
                            }
                        } catch (NullPointerException endChoicesOut) {
                            //Print answers if choices
                            outLine[choiceOutput + 16] = " ";
                            outLine[choiceOutput + 17] = "Intended Answer: " + ansIntend;
                            outLine[choiceOutput + 18] = "Given Answer: " + ansGiven;
                            lineNum = choiceOutput + 18;
                        }
                    }
                } else {
                    //Print answers if no choices
                    outLine[15] = "Intended Answer: " + ansIntend;
                    outLine[16] = "Given Answer: " + ansGiven;
                    lineNum = 16;
                }


                for (int finalOutCount = 0; finalOutCount <= 38; finalOutCount++) {
                    System.out.println(outLine[finalOutCount]);
                }


                //Write data to file
                try {
                    fw = new FileWriter(filename, true);
                    bw = new BufferedWriter(fw);
                    bw.newLine();
                    for (int writeCount = 0; writeCount <= lineNum; writeCount++) {
                        if (!(outLine[writeCount].equals(null))) {
                            bw.write(outLine[writeCount]);
                            bw.newLine();
                        }
                    }
                    bw.newLine();
                    bw.write("*****************************************************************************************");
                    bw.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (bw != null) {
                            bw.close();
                        }
                        if (fw != null) {
                            fw.close();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

                //Ask to continue
                isDone = Integer.parseInt(JOptionPane.showInputDialog(null, "Continue entering questions?\n     1) Yes\n     2) No", "Continue", JOptionPane.PLAIN_MESSAGE));
                while (isDone > 2 || isDone < 1) {
                    isDone = Integer.parseInt(JOptionPane.showInputDialog(null, "Invalid input.\n\nContinue entering questions?\n     1) Yes\n     2) No", "ERROR_Input", JOptionPane.PLAIN_MESSAGE));
                }
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(null, new JLabel("<html>Something went wrong.<br>Restart the program and try again.</html>", SwingConstants.CENTER), "ERROR", JOptionPane.ERROR_MESSAGE);
                exception.printStackTrace();
                isDone = 1;
            }
        }
    }

    public static void resetDefaults(){
        //Get report file directory
        //Locate appdata.txt
        chooser.setCurrentDirectory(new File("."));
        currentDirectory = (chooser.getCurrentDirectory()).toString();
        System.out.println(currentDirectory);
        while (!brSet) {
            try {
                br = new BufferedReader(new FileReader(currentDirectory + "\\appdata.txt"));
                brSet = true;
            } catch (FileNotFoundException e) {
                brSet = false;
                e.printStackTrace();
                //If there is no appdata.txt: make it!
                System.out.println("Appdata file not found.");
                try {
                    fw = new FileWriter(currentDirectory + "\\appdata.txt");
                    bw = new BufferedWriter(fw);
                    bw.write("NoDefault");
                    bw.newLine();
                    bw.write("NoDefault");
                    System.out.println("Appdata file created, no default report directory set");
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                } finally {
                    try {
                        if (bw != null) {
                            bw.close();
                        }
                        if (fw != null) {
                            fw.close();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        //Find default save location
        try {
            assert br != null; //Because of while loop above (while !brset)
            defaultDirectory = br.readLine();
            defaultFilename = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //If there is no default...
        assert defaultDirectory != null;
        assert defaultFilename != null;
        if (defaultDirectory.equals("NoDefault") || defaultFilename.equals("NoDefault")) {
            do {
                JOptionPane.showMessageDialog(null, new JLabel("Please choose a default location for the report file", JLabel.CENTER), "File Location", JOptionPane.PLAIN_MESSAGE);
                chooser.setCurrentDirectory(null);
                chooser.setDialogTitle("Choose File Save Location");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    defaultDirectory = (chooser.getSelectedFile()).toString();
                }
                defaultFilename = JOptionPane.showInputDialog(null, new JLabel("Enter a default name for the report file", JLabel.CENTER), "File Location", JOptionPane.PLAIN_MESSAGE);
                while (defaultFilename.contains(Character.toString('.')) || defaultFilename.contains(Character.toString('/')) || defaultFilename.contains(Character.toString('\\')) || defaultFilename.contains(Character.toString('*')) || defaultFilename.contains(Character.toString('?')) || defaultFilename.contains(Character.toString('<')) || defaultFilename.contains(Character.toString('>')) || defaultFilename.contains(Character.toString('\"')) || defaultFilename.contains(Character.toString(':')) || defaultFilename.contains(Character.toString('|'))) {
                    defaultFilename = JOptionPane.showInputDialog(null, "Invalid input. File name cannot contain any of the following characters:\n     .  >  <  ?  /  \\  \"  :  |\n\nEnter a default name for the report file", "ERROR_Input", JOptionPane.PLAIN_MESSAGE);
                }
            } while (defaultDirectory.equals("NoDefault") || defaultFilename.equals("NoDefault"));

            //Write defaults to appdata.txt
            try {
                fw = new FileWriter(currentDirectory + "\\appdata.txt");
                bw = new BufferedWriter(fw);
                bw.write(defaultDirectory);
                bw.newLine();
                bw.write(defaultFilename);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bw != null) {
                        bw.close();
                    }
                    if (fw != null) {
                        fw.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            filename = defaultDirectory + "\\" + defaultFilename;
            JOptionPane.showMessageDialog(null, new JLabel("Default file location set successfully", JLabel.CENTER), "File Location",  JOptionPane.PLAIN_MESSAGE);
        } else {
            useDefault = JOptionPane.showConfirmDialog(null, new JLabel ("Are you sure you want to change the default file location?", JLabel.CENTER), "File Location", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(useDefault == 1){
                filename = defaultDirectory + "\\" + defaultFilename;
                new DataCollection();
            } else {
                //Set default directory
                JOptionPane.showMessageDialog(null, new JLabel("Choose a default location for the report file", JLabel.CENTER), "File Location", JOptionPane.PLAIN_MESSAGE);
                chooser.setCurrentDirectory(null);
                chooser.setDialogTitle("Choose File Save Location");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    defaultDirectory = (chooser.getSelectedFile()).toString();
                }
                defaultFilename = JOptionPane.showInputDialog(null, new JLabel("Enter a default name for the report file", JLabel.CENTER), "File Location", JOptionPane.PLAIN_MESSAGE);
                while (defaultFilename.contains(Character.toString('.')) || defaultFilename.contains(Character.toString('/')) || defaultFilename.contains(Character.toString('\\')) || defaultFilename.contains(Character.toString('*')) || defaultFilename.contains(Character.toString('?')) || defaultFilename.contains(Character.toString('<')) || defaultFilename.contains(Character.toString('>')) || defaultFilename.contains(Character.toString('\"')) || defaultFilename.contains(Character.toString(':')) || defaultFilename.contains(Character.toString('|'))) {
                    defaultFilename = JOptionPane.showInputDialog(null, "Invalid input. File name cannot contain any of the following characters:\n     . > < ? / \\ \" : |\n\nEnter a default name for the report file", "ERROR_Input", JOptionPane.PLAIN_MESSAGE);
                }

                //Write defaults to appdata.txt
                try {
                    fw = new FileWriter(currentDirectory + "\\appdata.txt");
                    bw = new BufferedWriter(fw);
                    bw.write(defaultDirectory);
                    bw.newLine();
                    bw.write(defaultFilename);

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (bw != null) {
                            bw.close();
                        }
                        if (fw != null) {
                            fw.close();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                JOptionPane.showMessageDialog(null, new JLabel("Default file location set successfully", JLabel.CENTER), "File Location", JOptionPane.PLAIN_MESSAGE);
                filename = defaultDirectory + "\\" + defaultFilename;
                new DataCollection();
            }
        }
        //Add ".txt"
        filenameForm = filename.substring(filename.length() - 4);
        if (!filenameForm.equals(".txt")) {
            filename += ".txt";
        }
        System.out.println("Report will be saved as " + filename);

    }

    public static void enterQuestion(){
        do {
            try {
                needsResume = false;
                question = JOptionPane.showInputDialog(null, new JLabel("Please enter the entire question.", JLabel.CENTER), "Question", JOptionPane.PLAIN_MESSAGE);
                //Format Output for Question
                questionLength = question.length();
                questionEndChar = question.charAt(questionLength - 1);
                if (questionEndChar != '?') {
                    questionOut = question + "?";
                } else {
                    questionOut = question;
                }
            } catch (java.lang.NullPointerException cancel) {
                needsResume = true;
                cancel.printStackTrace();
                Object[] cancelOptions = {"Resume", "Quit"};
                cancelChoice = JOptionPane.showOptionDialog(null, new JLabel("<html>Are you sure you want to exit?</html>", JLabel.CENTER), "Cancel", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, cancelOptions, "Resume");
                if (cancelChoice == 1) {
                    System.exit(2);
                }
            } catch (java.lang.StringIndexOutOfBoundsException noInput){
                needsResume = true;
                JOptionPane.showMessageDialog(null, "Something went wrong.\nBe sure to enter at least one character at every entry window.", "ERROR_NoInput", JOptionPane.ERROR_MESSAGE);
            } catch (java.lang.NumberFormatException numEx){
                needsResume = true;
                JOptionPane.showMessageDialog(null, new JLabel ("<html>Something went wrong.<br>Be sure to enter a numeric value where requested.</html>", SwingConstants.CENTER), "ERROR_NumForm", JOptionPane.ERROR_MESSAGE);
                numEx.printStackTrace();

            }
        } while (needsResume);
    }

    public static void enterSentenceType(){
        do {
            try {
                needsResume = false;
                sTypeStr = JOptionPane.showInputDialog(null, "What is the SENTENCE TYPE?\n     1) Simple\n     2) Compound\n     3) Complex\n     4) Compound-complex", "Sentence Type", JOptionPane.PLAIN_MESSAGE);
                if (sTypeStr.equals(null)){
                    throw new NullPointerException();
                } else {
                    sTypeNum = Integer.parseInt(sTypeStr);
                }

                while (sTypeNum < 1 || sTypeNum > 4) {
                    sTypeStr = JOptionPane.showInputDialog(null, "Invalid input.\n\nWhat is the SENTENCE TYPE?\n     1) Simple\n     2) Compound\n     3) Complex\n     4) Compound-complex", "Sentence Type", JOptionPane.PLAIN_MESSAGE);
                    if (sTypeStr.equals(null)){
                        throw new NullPointerException();
                    } else {
                        sTypeNum = Integer.parseInt(sTypeStr);
                    }
                }
            } catch (java.lang.NullPointerException cancel) {
                needsResume = true;
                cancel.printStackTrace();
                Object[] cancelOptions = {"Resume", "Quit"};
                cancelChoice = JOptionPane.showOptionDialog(null, new JLabel("<html>Are you sure you want to exit?</html>", JLabel.CENTER), "Cancel", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, cancelOptions, "Resume");
                if (cancelChoice == 1) {
                    System.exit(2);
                }
            } catch (java.lang.StringIndexOutOfBoundsException noInput){
                needsResume = true;
                JOptionPane.showMessageDialog(null, "Something went wrong.\nBe sure to enter at least one character at every entry window.", "ERROR_NoInput", JOptionPane.ERROR_MESSAGE);
            } catch (java.lang.NumberFormatException numEx){
                needsResume = true;
                JOptionPane.showMessageDialog(null, new JLabel ("<html>Something went wrong.<br>Be sure to enter a numeric value where requested.</html>", SwingConstants.CENTER), "ERROR_NumForm", JOptionPane.ERROR_MESSAGE);
                numEx.printStackTrace();

            }
        } while (needsResume);

        if (sTypeNum == 1) {
            sType = "Simple";
        } else if (sTypeNum == 2) {
            sType = "Compound";
        } else if (sTypeNum == 3) {
            sType = "Complex";
        } else if (sTypeNum == 4) {
            sType = "Compound-complex";
        } else {
            sType = "";
        }

        //Format Output for Sentence Type
        sTypeOut = sType;
        sTypeForm = STYPEOUTLENGTH - sType.length();
        for (int sTypeCount = 1; sTypeCount <= sTypeForm; sTypeCount++) {
            sTypeOut = sTypeOut + " ";
        }
    }

    public static void enterQuestionType() {
        do {
            try {
                needsResume = false;
                typeStr = JOptionPane.showInputDialog(null, "What is the QUESTION TYPE?\n     1) Question Word\n     2) Yes or No\n     3) Choice", "Question Type", JOptionPane.PLAIN_MESSAGE);
                if (typeStr.equals(null)) {
                    throw new NullPointerException();
                } else {
                    typeNum = Integer.parseInt(typeStr);
                }
                while ((typeNum < 1) || (typeNum > 3)) {
                    typeStr = JOptionPane.showInputDialog(null, "Invalid input.\n\nWhat is the QUESTION TYPE?\n     1) Question Word\n     2) Yes or No\n     3) Choice", "Question Type", JOptionPane.PLAIN_MESSAGE);
                    if (typeStr.equals(null)) {
                        throw new NullPointerException();
                    } else {
                        typeNum = Integer.parseInt(typeStr);
                    }
                }

                if (typeNum == 1) {
                    type = "Question Word";
                } else if (typeNum == 2) {
                    type = "Yes/No";
                } else if (typeNum == 3) {
                    type = "Choice";
                } else {
                    type = "ERROR";
                }
                //Format Output for Question Type
                typeForm = TYPEOUTLENGTH - type.length();
                typeOut = type;
                for (int typeCount = 1; typeCount <= typeForm; typeCount++) {
                    typeOut = typeOut + " ";
                }
            } catch (java.lang.NullPointerException cancel) {
                needsResume = true;
                cancel.printStackTrace();
                Object[] cancelOptions = {"Resume", "Quit"};
                cancelChoice = JOptionPane.showOptionDialog(null, new JLabel("<html>Are you sure you want to exit?</html>", JLabel.CENTER), "Cancel", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, cancelOptions, "Resume");
                if (cancelChoice == 1) {
                    System.exit(2);
                }
            } catch (java.lang.StringIndexOutOfBoundsException noInput){
                needsResume = true;
                JOptionPane.showMessageDialog(null, "Something went wrong.\nBe sure to enter at least one character at every entry window.", "ERROR_NoInput", JOptionPane.ERROR_MESSAGE);
            } catch (java.lang.NumberFormatException numEx){
                needsResume = true;
                JOptionPane.showMessageDialog(null, new JLabel ("<html>Something went wrong.<br>Be sure to enter a numeric value where requested.</html>", SwingConstants.CENTER), "ERROR_NumForm", JOptionPane.ERROR_MESSAGE);
                numEx.printStackTrace();

            }
        } while (needsResume);
    }

    public static void enterChoices(){
        do {
            try {
                needsResume = false;
                choiceAmt = 0;
                if (typeNum == 3) {
                    choiceAmtStr = JOptionPane.showInputDialog(null, new JLabel("How many CHOICES are there?", JLabel.CENTER), "Choices", JOptionPane.PLAIN_MESSAGE);
                    if (choiceAmtStr.equals(null)) {
                        throw new NullPointerException();
                    } else {
                        choiceAmt = Integer.parseInt(choiceAmtStr);
                    }
                    while (choiceAmt < 1 || choiceAmt > 9) {
                        choiceAmtStr = JOptionPane.showInputDialog(null, "Invalid input.\nThere cannot be less than 1 or more than 9 choices.\n\nHow many CHOICES are there?", "ERROR_Input", JOptionPane.PLAIN_MESSAGE);
                        if (choiceAmtStr.equals(null)) {
                            throw new NullPointerException();
                        } else {
                            choiceAmt = Integer.parseInt(choiceAmtStr);
                        }
                    }
                    for (int choiceCount = 0; choiceCount < choiceAmt; choiceCount++) {
                        choice[choiceCount] = JOptionPane.showInputDialog(null, new JLabel("What is CHOICE " + (choiceCount + 1) + "?", JLabel.CENTER), "Choices", JOptionPane.PLAIN_MESSAGE);
                    }
                }
            } catch (java.lang.NullPointerException cancel) {
                needsResume = true;
                cancel.printStackTrace();
                Object[] cancelOptions = {"Resume", "Quit"};
                cancelChoice = JOptionPane.showOptionDialog(null, new JLabel("<html>Are you sure you want to exit?</html>", JLabel.CENTER), "Cancel", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, cancelOptions, "Resume");
                if (cancelChoice == 1) {
                    System.exit(2);
                }
            }catch (java.lang.StringIndexOutOfBoundsException noInput){
                needsResume = true;
                JOptionPane.showMessageDialog(null, "Something went wrong.\nBe sure to enter at least one character at every entry window.", "ERROR_NoInput", JOptionPane.ERROR_MESSAGE);
            } catch (java.lang.NumberFormatException numEx){
                needsResume = true;
                JOptionPane.showMessageDialog(null, new JLabel ("<html>Something went wrong.<br>Be sure to enter a numeric value where requested.</html>", SwingConstants.CENTER), "ERROR_NumForm", JOptionPane.ERROR_MESSAGE);
                numEx.printStackTrace();

            }
        } while (needsResume);
    }

    public static void enterQuestionWord(){
        do {
            try {
                needsResume = false;
                if (typeNum == 1) {
                    qWordStr = JOptionPane.showInputDialog(null, "What is the QUESTION WORD?\n     1) What\n     2) When\n     3) How\n     4) Who\n     5) Where\n     6) Why","Question Word", JOptionPane.PLAIN_MESSAGE);
                    if (qWordStr.equals(null)){
                        throw new NullPointerException();
                    } else {
                        qWordNum = Integer.parseInt(qWordStr);
                    }

                    while (qWordNum < 1 || qWordNum > 6) {
                        qWordStr = JOptionPane.showInputDialog(null, "Invalid input.\n\nWhat is the QUESTION WORD?\n     1) What\n     2) When\n     3) How\n     4) Who\n     5) Where\n     6) Why","Question Word", JOptionPane.PLAIN_MESSAGE);
                        if (qWordStr.equals(null)){
                            throw new NullPointerException();
                        } else {
                            qWordNum = Integer.parseInt(qWordStr);
                        }
                    }
                    if (qWordNum == 1) {
                        qWord = "What";
                    } else if (qWordNum == 2) {
                        qWord = "When";
                    } else if (qWordNum == 3) {
                        qWord = "How";
                    } else if (qWordNum == 4) {
                        qWord = "Who";
                    } else if (qWordNum == 5) {
                        qWord = "Where";
                    } else if (qWordNum == 6) {
                        qWord = "Why";
                    } else {
                        qWord = "N/A";
                    }
                } else {
                    qWord = "N/A";
                }
                //Format Output for Question Word
                qWordForm = QWORDLENGTH - qWord.length();
                qWordOut = qWord;
                for (int qWordCount = 1; qWordCount <= qWordForm; qWordCount++) {
                    qWordOut = qWordOut + " ";
                }
            } catch (java.lang.NullPointerException cancel) {
                needsResume = true;
                cancel.printStackTrace();
                Object[] cancelOptions = {"Resume", "Quit"};
                cancelChoice = JOptionPane.showOptionDialog(null, new JLabel("<html>Are you sure you want to exit?</html>", JLabel.CENTER), "Cancel", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, cancelOptions, "Resume");
                if (cancelChoice == 1) {
                    System.exit(2);
                }
            }catch (java.lang.StringIndexOutOfBoundsException noInput){
                needsResume = true;
                JOptionPane.showMessageDialog(null, "Something went wrong.\nBe sure to enter at least one character at every entry window.", "ERROR_NoInput", JOptionPane.ERROR_MESSAGE);
            } catch (java.lang.NumberFormatException numEx){
                needsResume = true;
                JOptionPane.showMessageDialog(null, new JLabel ("<html>Something went wrong.<br>Be sure to enter a numeric value where requested.</html>", SwingConstants.CENTER), "ERROR_NumForm", JOptionPane.ERROR_MESSAGE);
                numEx.printStackTrace();

            }
        } while (needsResume);
    }

    public static void enterSubject(){
        do {
            try {
                needsResume = false;
                subjectAmtStr = JOptionPane.showInputDialog(null, new JLabel("How many SUBJECTS are there?", JLabel.CENTER),"Subject", JOptionPane.PLAIN_MESSAGE);
                if (subjectAmtStr.equals(null)){
                    throw new NullPointerException();
                } else {
                    subjectAmt = Integer.parseInt(subjectAmtStr);
                }
                while (subjectAmt < 1 || subjectAmt > 9) {
                    subjectAmtStr = JOptionPane.showInputDialog(null, new JLabel("<html>Invalid input.<br>There cannot be less than 0 or more than 9 subjects.<br><br>How many SUBJECTS are there?</html>", JLabel.CENTER), "ERROR_Input", JOptionPane.PLAIN_MESSAGE);
                    if (subjectAmtStr.equals(null)) {
                        throw new NullPointerException();
                    } else {
                        subjectAmt = Integer.parseInt(subjectAmtStr);
                    }
                }
                if (subjectAmt == 1) {
                    subject[0] = JOptionPane.showInputDialog(null, new JLabel("What is the SUBJECT?", JLabel.CENTER),"Subject", JOptionPane.PLAIN_MESSAGE);
                } else {
                    for (int subAmtCount = 0; subAmtCount <= (subjectAmt - 1); subAmtCount++) {
                        subject[subAmtCount] = JOptionPane.showInputDialog(null, new JLabel("What is SUBJECT " + (subAmtCount + 1) + "?", JLabel.CENTER),"Subject", JOptionPane.PLAIN_MESSAGE);
                    }
                }
            } catch (java.lang.NullPointerException cancel) {
                needsResume = true;
                cancel.printStackTrace();
                Object[] cancelOptions = {"Resume", "Quit"};
                cancelChoice = JOptionPane.showOptionDialog(null, new JLabel("<html>Are you sure you want to exit?</html>", JLabel.CENTER), "Cancel", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, cancelOptions, "Resume");
                if (cancelChoice == 1) {
                    System.exit(2);
                }
            } catch (java.lang.StringIndexOutOfBoundsException noInput){
                needsResume = true;
                JOptionPane.showMessageDialog(null, "Something went wrong.\nBe sure to enter at least one character at every entry window.", "ERROR_NoInput", JOptionPane.ERROR_MESSAGE);
            } catch (java.lang.NumberFormatException numEx){
                needsResume = true;
                JOptionPane.showMessageDialog(null, new JLabel ("<html>Something went wrong.<br>Be sure to enter a numeric value where requested.</html>", SwingConstants.CENTER), "ERROR_NumForm", JOptionPane.ERROR_MESSAGE);
                numEx.printStackTrace();

            }
        } while (needsResume);
    }

    public static void enterMainVerb(){
        do {
            try {
                needsResume = false;
                if (sTypeNum == 1) {
                    mainVerb[0] = JOptionPane.showInputDialog(null, new JLabel("What is the MAIN VERB?", JLabel.CENTER), "Main Verb", JOptionPane.PLAIN_MESSAGE);
                    mainVerbAmt = 1;
                } else {
                    mainVerbAmtStr = JOptionPane.showInputDialog(null, new JLabel("How many MAIN VERBS are there?", JLabel.CENTER), "Main Verb", JOptionPane.PLAIN_MESSAGE);
                    if (mainVerbAmtStr.equals(null)) {
                        throw new NullPointerException();
                    } else {
                        mainVerbAmt = Integer.parseInt(mainVerbAmtStr);
                        while (mainVerbAmt < 0 || mainVerbAmt > 9){
                            mainVerbAmtStr = JOptionPane.showInputDialog(null, new JLabel("<html>Invalid input.<br>There cannot be less than 0 or more than 9 main verbs.<br><br>How many MAIN VERBS are there?</html>", JLabel.CENTER), "ERROR_Input", JOptionPane.PLAIN_MESSAGE);
                            if (mainVerbAmtStr.equals(null)) {
                                throw new NullPointerException();
                            } else {
                                mainVerbAmt = Integer.parseInt(mainVerbAmtStr);
                            }
                        }
                        if (mainVerbAmt == 1){
                            mainVerb[0] = JOptionPane.showInputDialog(null, new JLabel("What is the MAIN VERB?", JLabel.CENTER), "Main Verb", JOptionPane.PLAIN_MESSAGE);
                        }
                        for (int mainInCount = 0; mainInCount < mainVerbAmt; mainInCount++) {
                            mainVerb[mainInCount] = JOptionPane.showInputDialog(null, new JLabel("What is MAIN VERB " + (mainInCount + 1) + "?", JLabel.CENTER), "Main Verb", JOptionPane.PLAIN_MESSAGE);
                        }
                    }
                }
            } catch (java.lang.NullPointerException cancel) {
                needsResume = true;
                cancel.printStackTrace();
                Object[] cancelOptions = {"Resume", "Quit"};
                cancelChoice = JOptionPane.showOptionDialog(null, new JLabel("<html>Are you sure you want to exit?</html>", JLabel.CENTER), "Cancel", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, cancelOptions, "Resume");
                if (cancelChoice == 1) {
                    System.exit(2);
                }
            } catch (java.lang.StringIndexOutOfBoundsException noInput){
                needsResume = true;
                JOptionPane.showMessageDialog(null, "Something went wrong.\nBe sure to enter at least one character at every entry window.", "ERROR_NoInput", JOptionPane.ERROR_MESSAGE);
            } catch (java.lang.NumberFormatException numEx){
                needsResume = true;
                JOptionPane.showMessageDialog(null, new JLabel ("<html>Something went wrong.<br>Be sure to enter a numeric value where requested.</html>", SwingConstants.CENTER), "ERROR_NumForm", JOptionPane.ERROR_MESSAGE);
                numEx.printStackTrace();

            }
        } while (needsResume);
    }

    public static void enterAuxVerb(){
        do {
            try {
                needsResume = false;
                auxVerbAmtStr = JOptionPane.showInputDialog(null, new JLabel("How many AUXILIARY VERBS are there?", JLabel.CENTER), "Auxiliary Verb", JOptionPane.PLAIN_MESSAGE);
                System.out.println(auxVerbAmtStr);
                if (auxVerbAmtStr.equals(null)){
                    throw new NullPointerException();
                } else {
                    auxVerbAmt = Integer.parseInt(auxVerbAmtStr);
                }
                while (auxVerbAmt > 9 || auxVerbAmt < 0) {
                    auxVerbAmtStr = JOptionPane.showInputDialog(null, new JLabel("<html>Invalid input.<br>There cannot be less than 0 or more than 9 auxiliary verbs.<br><br>How many AUXILIARY VERBS are there?</html>", JLabel.CENTER), "ERROR_Input", JOptionPane.PLAIN_MESSAGE);
                    if (auxVerbAmtStr.equals(null)) {
                        throw new NullPointerException();
                    } else {
                        auxVerbAmt = Integer.parseInt(mainVerbAmtStr);
                    }
                }
                System.out.println(auxVerbAmt);
                for (int auxInCount = 0; auxInCount < auxVerbAmt; auxInCount++) {
                    System.out.println(mainVerb[auxInCount]);
                    if (auxVerbAmt == 0) {
                        auxVerb[auxInCount] = "N/A";
                    } else if (auxVerbAmt > 1 && auxVerbAmt < 9) {
                        auxVerb[auxInCount] = JOptionPane.showInputDialog(null, new JLabel("What is AUXILIARY VERB " + (auxInCount + 1) + "?", JLabel.CENTER), "Auxiliary Verb", JOptionPane.PLAIN_MESSAGE);
                    } else if (auxVerbAmt == 1){
                        auxVerb[auxInCount] = JOptionPane.showInputDialog(null, new JLabel("What is the AUXILIARY VERB?", JLabel.CENTER), "Auxiliary Verb", JOptionPane.PLAIN_MESSAGE);
                    } else {
                        auxVerb[auxInCount] = "ERROR";
                    }
                    if (!(auxVerb[auxInCount].equalsIgnoreCase("N/A")) && !(auxVerb[auxInCount].equals("ERROR"))){ //Makes aux verbs lowercase
                        auxVerb[auxInCount] = auxVerb[auxInCount].toLowerCase();
                    }
                }
            } catch (java.lang.NullPointerException cancel) {
                needsResume = true;
                cancel.printStackTrace();
                Object[] cancelOptions = {"Resume", "Quit"};
                cancelChoice = JOptionPane.showOptionDialog(null, new JLabel("<html>Are you sure you want to exit?</html>", JLabel.CENTER), "Cancel", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, cancelOptions, "Resume");
                if (cancelChoice == 1) {
                    System.exit(2);
                }
            } catch (java.lang.StringIndexOutOfBoundsException noInput){
                needsResume = true;
                JOptionPane.showMessageDialog(null, "Something went wrong.\nBe sure to enter at least one character at every entry window.", "ERROR_NoInput", JOptionPane.ERROR_MESSAGE);
            } catch (java.lang.NumberFormatException numEx){
                needsResume = true;
                JOptionPane.showMessageDialog(null, new JLabel ("<html>Something went wrong.<br>Be sure to enter a numeric value where requested.</html>", SwingConstants.CENTER), "ERROR_NumForm", JOptionPane.ERROR_MESSAGE);
                numEx.printStackTrace();

            }
        } while (needsResume);
    }

    public static void enterNegativity(){
        do {
            try {
                needsResume = false;
                isNegStr = JOptionPane.showInputDialog(null, "Is the question NEGATIVE?\n     1) Yes\n     2) No", "Negative", JOptionPane.PLAIN_MESSAGE);
                if (isNegStr.equals(null)){
                    throw new NullPointerException();
                } else {
                    isNeg = Integer.parseInt(isNegStr);
                }
                while (isNeg > 2 || isNeg < 1){
                    isNeg = Integer.parseInt(JOptionPane.showInputDialog(null, "Invalid input.\n\nIs the question NEGATIVE?\n     1) Yes\n     2) No", "ERROR_Input", JOptionPane.PLAIN_MESSAGE));
                }
                if (isNeg == 1) {
                    negOut = "Yes";
                } else {
                    negOut = "No";
                }

                //Format Output for Negativity
                negForm = NEGOUTLENGTH - negOut.length();
                for (int negOutCount = 1; negOutCount <= negForm; negOutCount++) {
                    negOut = negOut + " ";
                }
            } catch (java.lang.NullPointerException cancel) {
                needsResume = true;
                cancel.printStackTrace();
                Object[] cancelOptions = {"Resume", "Quit"};
                cancelChoice = JOptionPane.showOptionDialog(null, new JLabel("<html>Are you sure you want to exit?</html>", JLabel.CENTER), "Cancel", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, cancelOptions, "Resume");
                if (cancelChoice == 1) {
                    System.exit(2);
                }
            } catch (java.lang.StringIndexOutOfBoundsException noInput){
                needsResume = true;
                JOptionPane.showMessageDialog(null, "Something went wrong.\nBe sure to enter at least one character at every entry window.", "ERROR_NoInput", JOptionPane.ERROR_MESSAGE);
            } catch (java.lang.NumberFormatException numEx){
                needsResume = true;
                JOptionPane.showMessageDialog(null, new JLabel ("<html>Something went wrong.<br>Be sure to enter a numeric value where requested.</html>", SwingConstants.CENTER), "ERROR_NumForm", JOptionPane.ERROR_MESSAGE);
                numEx.printStackTrace();

            }
        } while (needsResume);
    }

    public static void enterIntendedAnswer(){
        do {
            try {
                needsResume = false;
                ansIntend = "ERROR";
                if (typeNum == 1) {
                    ansIntend = JOptionPane.showInputDialog(null, new JLabel("What was the INTENDED ANSWER?", JLabel.CENTER), "Intended Answer", JOptionPane.PLAIN_MESSAGE);
                } else if (typeNum == 2) {
                    yesNoIntendStr = JOptionPane.showInputDialog(null, "What was the INTENDED ANSWER?\n     1) Yes\n     2) No","Intended Answer", JOptionPane.PLAIN_MESSAGE);
                    if (yesNoIntendStr.equals(null)){
                        throw new NullPointerException();
                    } else {
                        yesNoIntend = Integer.parseInt(yesNoIntendStr);
                    }

                    while (yesNoIntend <1 || yesNoIntend > 2) {
                        yesNoIntendStr = JOptionPane.showInputDialog(null, "Invalid input.\n\nWhat was the INTENDED ANSWER?\n     1) Yes\n     2) No","ERROR_Input", JOptionPane.PLAIN_MESSAGE);
                        if (yesNoIntendStr.equals(null)){
                            throw new NullPointerException();
                        } else {
                            yesNoIntend = Integer.parseInt(yesNoIntendStr);
                        }
                    }
                    if (yesNoIntend == 1) {
                        ansIntend = "Yes";
                    } else if(yesNoIntend == 2) {
                        ansIntend = "No";
                    }
                } else if (typeNum == 3){
                    choiceIntendQuestion = "What was the INTENDED ANSWER?";
                    for (int choiceIntendNumCount = 0; choiceIntendNumCount < choiceAmt; choiceIntendNumCount++) {
                        choiceIntendQuestion = choiceIntendQuestion + "\n     " + (choiceIntendNumCount+1) + ") " + choice[choiceIntendNumCount];
                    }
                    choiceIntendStr = JOptionPane.showInputDialog(null, choiceIntendQuestion,"Intended Answer", JOptionPane.PLAIN_MESSAGE);
                    if (choiceIntendStr.equals(null)){
                        throw new NullPointerException();
                    } else {
                        choiceIntend = Integer.parseInt(choiceIntendStr);
                    }
                    while (choiceIntend < 1 || choiceIntend > choiceAmt){
                        choiceIntendStr = JOptionPane.showInputDialog(null, "Invalid input.\n\n" + choiceIntendQuestion,"ERROR_Input", JOptionPane.PLAIN_MESSAGE);
                        if (choiceIntendStr.equals(null)){
                            throw new NullPointerException();
                        } else {
                            choiceIntend = Integer.parseInt(choiceIntendStr);
                        }
                    }
                    for (int choiceIntendOutCount = 0; choiceIntendOutCount < choiceAmt; choiceIntendOutCount++){
                        if (choiceIntend == (choiceIntendOutCount+1)){
                            ansIntend = choice[choiceIntendOutCount];
                        }
                    }
                }
            } catch (java.lang.NullPointerException cancel) {
                needsResume = true;
                cancel.printStackTrace();
                Object[] cancelOptions = {"Resume", "Quit"};
                cancelChoice = JOptionPane.showOptionDialog(null, new JLabel("<html>Are you sure you want to exit?</html>", JLabel.CENTER), "Cancel", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, cancelOptions, "Resume");
                if (cancelChoice == 1) {
                    System.exit(2);
                }
            } catch (java.lang.StringIndexOutOfBoundsException noInput){
                needsResume = true;
                JOptionPane.showMessageDialog(null, "Something went wrong.\nBe sure to enter at least one character at every entry window.", "ERROR_NoInput", JOptionPane.ERROR_MESSAGE);
            } catch (java.lang.NumberFormatException numEx){
                needsResume = true;
                JOptionPane.showMessageDialog(null, new JLabel ("<html>Something went wrong.<br>Be sure to enter a numeric value where requested.</html>", SwingConstants.CENTER), "ERROR_NumForm", JOptionPane.ERROR_MESSAGE);
                numEx.printStackTrace();

            }
        } while (needsResume);
    }

    public static void enterGivenAnswer() {
        do {
            try {
                needsResume = false;
                ansGiven = "ERROR";
                if (typeNum == 1) {
                    ansGiven = JOptionPane.showInputDialog(null, new JLabel("What was the GIVEN ANSWER?", JLabel.CENTER), "Given Answer", JOptionPane.PLAIN_MESSAGE);
                } else if (typeNum == 2) {
                    yesNoGivenStr = JOptionPane.showInputDialog(null, "What was the GIVEN ANSWER?\n     1) Yes\n     2) No", "Given Answer", JOptionPane.PLAIN_MESSAGE);
                    if (yesNoGivenStr.equals(null)) {
                        throw new NullPointerException();
                    } else {
                        yesNoGiven = Integer.parseInt(yesNoGivenStr);
                    }
                    while (yesNoGiven < 1 || yesNoGiven > 2) {
                        yesNoGivenStr = JOptionPane.showInputDialog(null, "Invalid input.\n\nWhat was the GIVEN ANSWER?\n     1) Yes\n     2) No", "ERROR_Input", JOptionPane.PLAIN_MESSAGE);
                        if (yesNoGivenStr.equals(null)) {
                            throw new NullPointerException();
                        } else {
                            yesNoGiven = Integer.parseInt(yesNoGivenStr);
                        }
                    }
                    if (yesNoGiven == 1) {
                        ansGiven = "Yes";
                    } else if (yesNoGiven == 2) {
                        ansGiven = "No";
                    }
                } else if (typeNum == 3) {
                    choiceGivenQuestion = "What was the GIVEN ANSWER?";
                    for (int choiceGivenNumCount = 0; choiceGivenNumCount < choiceAmt; choiceGivenNumCount++) {
                        choiceGivenQuestion = choiceGivenQuestion + "\n     " + (choiceGivenNumCount + 1) + ") " + choice[choiceGivenNumCount];
                    }
                    choiceGivenStr = JOptionPane.showInputDialog(null, choiceGivenQuestion, "Given Answer", JOptionPane.PLAIN_MESSAGE);
                    if (choiceGivenStr.equals(null)) {
                        throw new NullPointerException();
                    } else {
                        choiceGiven = Integer.parseInt(choiceGivenStr);
                    }
                    while (choiceGiven < 1 || choiceGiven > choiceAmt) {
                        choiceGivenStr = JOptionPane.showInputDialog(null, "Invalid input\n\n" + choiceGivenQuestion, "ERROR_Input", JOptionPane.PLAIN_MESSAGE);
                        if (choiceGivenStr.equals(null)) {
                            throw new NullPointerException();
                        } else {
                            choiceGiven = Integer.parseInt(choiceGivenStr);
                        }
                    }
                    for (int choiceGivenOutCount = 0; choiceGivenOutCount < choiceAmt; choiceGivenOutCount++) {

                        if (choiceGiven == (choiceGivenOutCount + 1)) {
                            ansGiven = choice[choiceGivenOutCount];
                        }
                    }
                }
            } catch (java.lang.NullPointerException cancel) {
                needsResume = true;
                cancel.printStackTrace();
                Object[] cancelOptions = {"Resume", "Quit"};
                cancelChoice = JOptionPane.showOptionDialog(null, new JLabel("<html>Are you sure you want to exit?</html>", JLabel.CENTER), "Cancel", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, cancelOptions, "Resume");
                if (cancelChoice == 1) {
                    System.exit(2);
                }
            } catch (java.lang.StringIndexOutOfBoundsException noInput){
                needsResume = true;
                JOptionPane.showMessageDialog(null, "Something went wrong.\nBe sure to enter at least one character at every entry window.", "ERROR_NoInput", JOptionPane.ERROR_MESSAGE);
            } catch (java.lang.NumberFormatException numEx){
                needsResume = true;
                JOptionPane.showMessageDialog(null, new JLabel ("<html>Something went wrong.<br>Be sure to enter a numeric value where requested.</html>", SwingConstants.CENTER), "ERROR_NumForm", JOptionPane.ERROR_MESSAGE);
                numEx.printStackTrace();

            }
        } while (needsResume);
    }

}
