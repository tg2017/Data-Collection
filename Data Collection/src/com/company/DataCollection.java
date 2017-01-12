package com.company;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.lang.*;



public class DataCollection{

    public static void main(String[] args){

        final int SUBOUTLENGTH = 17, TYPEOUTLENGTH = 13, QWORDLENGTH = 7, AUXVERBOUTLENGTH = 14, NEGOUTLENGTH = 8, MAINVERBOUTLENGTH = 13, STYPEOUTLENGTH = 15;

        String qWord, type, sType, question, ansIntend, ansGiven, testStr;
        String qWordOut, typeOut, sTypeOut, questionOut, negOut;
        String choiceIntendQuestion, choiceGivenQuestion;
        String filename, currentDirectory, defaultDirectory = null, defaultFilename = null, newDirectory="", newFilename;
        String filenameForm;
        String subjectAmtStr, choiceAmtStr, mainVerbAmtStr, auxVerbAmtStr, sTypeStr, qWordStr, typeStr;
        String resumeStr;

        String[] outLine = new String[30];
        String[] choice = new String[10];
        String[] subject = new String[5], mainVerb = new String[10], auxVerb = new String[10];
        String[] subjectOut = new String[5], mainVerbOut = new String[5], auxVerbOut = new String[10];
        int subjectForm, auxVerbForm, mainVerbForm, qWordForm, typeForm, sTypeForm, negForm, questionLength, lineNum, choiceLineNum = 0;
        int isDone = 1, isNeg, useDefault, setDefault, cancelChoice;
        int yesNoIntend, yesNoGiven, choiceIntend, choiceGiven;
        int typeNum, qWordNum, sTypeNum;
        int subjectAmt, choiceAmt, mainVerbAmt = 0, auxVerbAmt;
        char questionEndChar;
        boolean isBe = false, multSub = false, brSet = false;

        String resumePlace;
        JFileChooser chooser = new JFileChooser();
        BufferedReader br = null;
        BufferedWriter bw = null;
        FileWriter fw = null;



        //ToDo: Make fancy-looking startup menu
        //ToDo: accomodate for all sentence types
        //ToDo: Data control to ensure Strings are not parsed to Ints


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
            assert br != null;
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
            useDefault = JOptionPane.showConfirmDialog(null, new JLabel ("Use default file location?", JLabel.CENTER), "File Location", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(useDefault == 0){
                filename = defaultDirectory + "\\" + defaultFilename;
            } else {
                setDefault = JOptionPane.showConfirmDialog(null, new JLabel("Set new default file location?", JLabel.CENTER), "File Location", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (setDefault == 0){
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
                } else {
                    JOptionPane.showMessageDialog(null, new JLabel("Choose a location for the report file", JLabel.CENTER), "File Location", JOptionPane.PLAIN_MESSAGE);
                    chooser.setCurrentDirectory(null);
                    chooser.setDialogTitle("Choose File Save Location");
                    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    chooser.setAcceptAllFileFilterUsed(false);

                    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                        newDirectory = (chooser.getSelectedFile()).toString();
                    }
                    newFilename = JOptionPane.showInputDialog(null, new JLabel("Enter a name for the report file", JLabel.CENTER), "File Location", JOptionPane.PLAIN_MESSAGE);
                    while (newFilename.contains(Character.toString('.')) || newFilename.contains(Character.toString('/')) || newFilename.contains(Character.toString('\\')) || newFilename.contains(Character.toString('*')) || newFilename.contains(Character.toString('?')) || newFilename.contains(Character.toString('<')) || newFilename.contains(Character.toString('>')) || newFilename.contains(Character.toString('\"')) || newFilename.contains(Character.toString(':')) || newFilename.contains(Character.toString('|'))) {
                        newFilename = JOptionPane.showInputDialog(null, "Invalid input. File name cannot contain any of the following characters:\n     . > < ? / \\ \" : |\n\nEnter a name for the report file", "ERROR_Input", JOptionPane.PLAIN_MESSAGE);
                    }
                    filename = newDirectory + "\\" + newFilename;
                    JOptionPane.showMessageDialog(null, new JLabel("File location set successfully", JLabel.CENTER), "File Location",  JOptionPane.PLAIN_MESSAGE);

                }
            }
        }
        //Add ".txt"
        filenameForm = filename.substring(filename.length() - 4);
        if (!filenameForm.equals(".txt")) {
            filename += ".txt";
        }
        System.out.println("Report will be saved as " + filename);



        while (isDone == 1){
            try{
                //Question
                question = JOptionPane.showInputDialog(null, new JLabel("Please enter the entire question.", JLabel.CENTER), "Question", JOptionPane.PLAIN_MESSAGE);

                //Format Output for Question
                questionLength = question.length();
                questionEndChar = question.charAt(questionLength - 1);
                if (questionEndChar != '?') {
                    questionOut = question + "?";
                } else {
                    questionOut = question;
                }


                //Sentence Type
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

                //Question Type
                typeStr = JOptionPane.showInputDialog(null, "What is the QUESTION TYPE?\n     1) Question Word\n     2) Yes or No\n     3) Choice" ,"Question Type", JOptionPane.PLAIN_MESSAGE);
                if (typeStr.equals(null)){
                    throw new NullPointerException();
                } else {
                    typeNum = Integer.parseInt(typeStr);
                }
                while ((typeNum < 1) || (typeNum > 3)) {
                    typeStr = JOptionPane.showInputDialog(null, "Invalid input.\n\nWhat is the QUESTION TYPE?\n     1) Question Word\n     2) Yes or No\n     3) Choice" ,"Question Type", JOptionPane.PLAIN_MESSAGE);
                    if (typeStr.equals(null)){
                        throw new NullPointerException();
                    } else {
                        typeNum = Integer.parseInt(typeStr);
                    }                }

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


                //Choices
                choiceAmt = 0;
                if (typeNum == 3) {
                    choiceAmtStr = JOptionPane.showInputDialog(null, new JLabel ("How many choices are there?", JLabel.CENTER),"Choices", JOptionPane.PLAIN_MESSAGE);
                    if (choiceAmtStr.equals(null)){
                        throw new NullPointerException();
                    } else {
                        choiceAmt = Integer.parseInt(choiceAmtStr);
                    }
                    while (choiceAmt < 1 || choiceAmt > 10) {
                        choiceAmt = Integer.parseInt(JOptionPane.showInputDialog(null, "Invalid input.\n\nHow many choices are there?" ,"ERROR_Input", JOptionPane.PLAIN_MESSAGE));
                    }
                    for (int choiceCount = 0; choiceCount < choiceAmt; choiceCount++) {
                        choice[choiceCount] = JOptionPane.showInputDialog(null, new JLabel("What is choice " + (choiceCount + 1) + "?", JLabel.CENTER),"", JOptionPane.PLAIN_MESSAGE);
                    }

                }


                //Question Word
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


                //Subject
                subjectAmtStr = JOptionPane.showInputDialog(null, new JLabel("How many SUBJECTS are there?", JLabel.CENTER),"Subject", JOptionPane.PLAIN_MESSAGE);
                if (subjectAmtStr.equals(null)){
                    throw new NullPointerException();
                } else {
                    subjectAmt = Integer.parseInt(subjectAmtStr);
                }
                if (subjectAmt == 1) {
                    subject[0] = JOptionPane.showInputDialog(null, new JLabel("What is the SUBJECT?", JLabel.CENTER),"Subject", JOptionPane.PLAIN_MESSAGE);
                } else if (subjectAmt < 1 || subjectAmt > 5) {
                    subjectAmt = Integer.parseInt(JOptionPane.showInputDialog(null, "Invalid input.\n\nHow many SUBJECTS are there?","ERROR_Input", JOptionPane.PLAIN_MESSAGE));
                } else {
                    for (int subAmtCount = 0; subAmtCount <= (subjectAmt - 1); subAmtCount++) {
                        subject[subAmtCount] = JOptionPane.showInputDialog(null, new JLabel("What is SUBJECT " + (subAmtCount + 1) + "?", JLabel.CENTER),"Subject", JOptionPane.PLAIN_MESSAGE);
                        multSub = true;
                    }
                }

                //Format Output for Subject
                for (int subFormCount = 0; subFormCount <= subjectAmt - 1; subFormCount++) {
                    subject[subFormCount] = subject[subFormCount].toLowerCase();
                    subjectForm = SUBOUTLENGTH - subject[subFormCount].length();
                    subjectOut[subFormCount] = subject[subFormCount];
                    for (int subCount = 1; subCount <= subjectForm; subCount++) {
                        subjectOut[subFormCount] = subjectOut[subFormCount] + " ";
                    }
                }


                //Main Verb
                if (sTypeNum == 1) {
                    mainVerb[0] = JOptionPane.showInputDialog(null, new JLabel("What is the MAIN VERB?", JLabel.CENTER), "Main Verb", JOptionPane.PLAIN_MESSAGE);
                } else {
                    mainVerbAmtStr = JOptionPane.showInputDialog(null, new JLabel("How many MAIN VERBS are there?", JLabel.CENTER), "Main Verb", JOptionPane.PLAIN_MESSAGE);
                    if (mainVerbAmtStr.equals(null)) {
                        throw new NullPointerException();
                    } else {
                        for (int mainInCount = 0; mainInCount < mainVerbAmt; mainInCount++) {
                            mainVerb[mainInCount] = JOptionPane.showInputDialog(null, new JLabel("What is MAIN VERB?" + (mainInCount + 1), JLabel.CENTER), "Main Verb", JOptionPane.PLAIN_MESSAGE);
                        }
                    }
                }

                //ToDo: Format Output for Main Verb
                for (int mainArrayCount = 0; mainArrayCount < mainVerbAmt; mainArrayCount++) {
                    mainVerb[mainArrayCount] = mainVerb[mainArrayCount].toLowerCase();
                    mainVerbOut[mainArrayCount] = mainVerb[mainArrayCount];
                    mainVerbForm = MAINVERBOUTLENGTH - mainVerb[mainArrayCount].length();
                    for (int mainVerbCount = 1; mainVerbCount <= mainVerbForm; mainVerbCount++) {
                        mainVerbOut[mainArrayCount] = mainVerbOut[mainArrayCount] + " ";
                    }
                }

                //ToDo: Auxiliary Verb (there is no aux verb if main verb is "be")
                auxVerbAmtStr = JOptionPane.showInputDialog(null, new JLabel("How many AUXILIARY VERBS are there?", JLabel.CENTER), "Auxiliary Verb", JOptionPane.PLAIN_MESSAGE);
                if (auxVerbAmtStr.equals(null)){
                    throw new NullPointerException();
                } else {
                    auxVerbAmt = Integer.parseInt(auxVerbAmtStr);
                }
                for (int auxInCount = 0; auxInCount < auxVerbAmt; auxInCount++) {
                    if (mainVerb[auxInCount].equals("be") || mainVerb[auxInCount].equals("is") || mainVerb[auxInCount].equals("are") || mainVerb[auxInCount].equals("were") || mainVerb[auxInCount].equals("was") || mainVerb[auxInCount].equals("am")) {
                        isBe = true;
                    }
                    if (isBe) {
                        auxVerb[auxInCount] = "N/A";
                    } else if (auxVerbAmt > 1) {
                        auxVerb[auxInCount] = JOptionPane.showInputDialog(null, new JLabel("What is AUXILIARY VERB?" + (auxInCount + 1), JLabel.CENTER), "Auxiliary Verb", JOptionPane.PLAIN_MESSAGE);
                    } else if (auxVerbAmt == 1){
                        auxVerb[auxInCount] = JOptionPane.showInputDialog(null, new JLabel("What is the AUXILIARY VERB?", JLabel.CENTER), "Auxiliary Verb", JOptionPane.PLAIN_MESSAGE);
                    } else {
                        auxVerb[auxInCount] = "ERROR";
                    }
                    //TODO: Fix NullPointerException upon reaching this point
                    if (!(auxVerb[auxInCount].equals("N/A"))) {
                        auxVerb[auxInCount] = auxVerb[auxInCount].toLowerCase();
                    }

                    //ToDo: Format Output for Auxiliary Verb
                    auxVerbOut[auxInCount] = auxVerb[auxInCount];
                    auxVerbForm = AUXVERBOUTLENGTH - auxVerb[auxInCount].length();
                    for (int auxVerbCount = 1; auxVerbCount <= auxVerbForm; auxVerbCount++) {
                        auxVerbOut[auxInCount] = auxVerbOut[auxInCount] + " ";
                    }
                }


                //Negativity
                isNeg = Integer.parseInt(JOptionPane.showInputDialog(null, "Is the question NEGATIVE?\n     1) Yes\n     2) No", "Negative", JOptionPane.PLAIN_MESSAGE));
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


                //Intended Answer
                ansIntend = "ERROR";
                if (typeNum == 1) {
                    ansIntend = JOptionPane.showInputDialog(null, new JLabel("What was the INTENDED ANSWER?", JLabel.CENTER), "Intended Answer", JOptionPane.PLAIN_MESSAGE);
                } else if (typeNum == 2) {
                    yesNoIntend = Integer.parseInt(JOptionPane.showInputDialog(null, "What was the INTENDED ANSWER?\n     1) Yes\n     2) No","", JOptionPane.PLAIN_MESSAGE));
                    while (yesNoIntend <1 || yesNoIntend > 2) {
                        yesNoIntend = Integer.parseInt(JOptionPane.showInputDialog(null, "Invalid input.\n\nWhat was the INTENDED ANSWER?\n     1) Yes\n     2) No", "ERROR_Input", JOptionPane.PLAIN_MESSAGE));
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
                    choiceIntend = Integer.parseInt(JOptionPane.showInputDialog(null, choiceIntendQuestion,"Intended Answer", JOptionPane.PLAIN_MESSAGE));
                    while (choiceIntend < 1 || choiceIntend > choiceAmt){
                        choiceIntend = Integer.parseInt(JOptionPane.showInputDialog(null, "Invalid input.\n\n" + choiceIntendQuestion,"ERROR_Input", JOptionPane.PLAIN_MESSAGE));
                    }
                    for (int choiceIntendOutCount = 0; choiceIntendOutCount < choiceAmt; choiceIntendOutCount++){
                        if (choiceIntend == (choiceIntendOutCount+1)){
                            ansIntend = choice[choiceIntendOutCount];
                        }
                    }
                }

                //Given Answer
                ansGiven = "ERROR";
                if (typeNum == 1) {
                    ansGiven = JOptionPane.showInputDialog(null, new JLabel("What was the GIVEN ANSWER?", JLabel.CENTER),"Given Answer", JOptionPane.PLAIN_MESSAGE);
                } else if (typeNum == 2) {
                    yesNoGiven = Integer.parseInt(JOptionPane.showInputDialog(null, "What was the GIVEN ANSWER?\n     1) Yes\n     2) No","Given Answer", JOptionPane.PLAIN_MESSAGE));
                    while (yesNoGiven <1 || yesNoGiven > 2) {
                        yesNoGiven = Integer.parseInt(JOptionPane.showInputDialog(null, "Invalid input.\n\nWhat was the GIVEN ANSWER?\n     1) Yes\n     2) No","ERROR_Input", JOptionPane.PLAIN_MESSAGE));
                    }
                    if (yesNoGiven == 1) {
                        ansGiven = "Yes";
                    } else if(yesNoGiven == 2) {
                        ansGiven = "No";
                    }
                } else if (typeNum == 3){
                    choiceGivenQuestion = "What was the GIVEN ANSWER?";
                    for (int choiceGivenNumCount = 0; choiceGivenNumCount < choiceAmt; choiceGivenNumCount++) {
                        choiceGivenQuestion = choiceGivenQuestion + "\n     " + (choiceGivenNumCount+1) + ") " + choice[choiceGivenNumCount];
                    }
                    choiceGiven = Integer.parseInt(JOptionPane.showInputDialog(null, choiceGivenQuestion,"Given Answer", JOptionPane.PLAIN_MESSAGE));
                    while (choiceGiven < 1 || choiceGiven > choiceAmt){
                        choiceGiven = Integer.parseInt(JOptionPane.showInputDialog(null, "Invalid input.\n\n" + choiceGivenQuestion,"ERROR_Input", JOptionPane.PLAIN_MESSAGE));
                    }
                    for (int choiceGivenOutCount = 0; choiceGivenOutCount < choiceAmt; choiceGivenOutCount++){
                        if (choiceGiven == (choiceGivenOutCount+1)){
                            ansGiven = choice[choiceGivenOutCount];
                        }
                    }
                }




                //Final Output
                //TODO: "Negative" -> "Neg" (?)
                //ToDo: New layout
/*
            if (multSub) {
                outLine[0] = "Question: " + questionOut + "\n";
                outLine[1] = " |  Sentence Type  | Question Type | Q. Word | Auxiliary Verb |";
                for (int finalSubOutCount = 1; finalSubOutCount <= subjectAmt; finalSubOutCount++) {
                    outLine[1] = outLine[1] + "     Subject " + finalSubOutCount + "     |";
                }
                outLine[1] = outLine[1] + ("   Main Verb   | Negative |");
                outLine[2] = (" |-----------------|---------------|---------|----------------|-------------------|");
                for (int finalLineOutCount = 1; finalLineOutCount <= subjectAmt - 1; finalLineOutCount++) {
                    outLine[2] = outLine[2] + "-------------------|";
                }
                outLine[2] = outLine[2] + ("---------------|----------|\n");
                outLine[3] = (" | " + sTypeOut + " | " + typeOut + " | " + qWordOut + " | " + auxVerbOut + " | " + subjectOut[0] + " | ");
                for (int multSubOutCount = 1; multSubOutCount < subjectAmt; multSubOutCount++) {
                    outLine[3] = outLine[3] + subjectOut[multSubOutCount] + " | ";
                }
                outLine[3] = outLine[3] + (mainVerbOut + " | " + negOut + " |");
                outLine[4] = "";

                //Output for Choices
                if (typeNum == 3) {
                    for (int choiceOutCount = 0; choiceOutCount < choiceAmt; choiceOutCount++) {
                        outLine[(choiceOutCount + 5)] = "Choice 1: " + choice[choiceOutCount];
                        choiceLineNum = 4 + choiceOutCount + 1;
                    }
                    outLine[(choiceLineNum + 1)] = "";
                    outLine[(choiceLineNum + 2)] = "  Intended answer: \"" + ansIntend + "\"";
                    outLine[(choiceLineNum + 3)] = "  Answer Received: \"" + ansGiven + "\"";
                    lineNum = choiceLineNum + 3;
                } else {
                    outLine[5] = "  Intended answer: \"" + ansIntend + "\"";
                    outLine[6] = "  Answer Received: \"" + ansGiven + "\"";
                    lineNum = 6;
                }
                //Took out for loop that System.out.println the data, moved to outside this If block
            } else {
                outLine[0] = "Question: " + questionOut + "\n";
                outLine[1] = " |  Sentence Type  | Question Type | Q. Word | Auxiliary Verb |      Subject      |   Main Verb   | Negative |";
                outLine[2] = " |-----------------|---------------|---------|----------------|-------------------|---------------|----------|";
                outLine[3] = " | " + sTypeOut + " | " + typeOut + " | " + qWordOut + " | " + auxVerbOut + " | " + subjectOut[0] + " | " + mainVerbOut + " | " + negOut + " |\n";
                outLine[4] = "";

                //Output for Choices
                if (typeNum == 3) {
                    for (int choiceOutCount = 0; choiceOutCount < choiceAmt; choiceOutCount++) {
                        outLine[(choiceOutCount + 5)] = "Choice " + (choiceOutCount + 1) + ": " + choice[choiceOutCount];
                        choiceLineNum = 4 + choiceOutCount + 1;
                    }
                    outLine[(choiceLineNum + 1)] = "";
                    outLine[(choiceLineNum + 2)] = "  Intended answer: \"" + ansIntend + "\"";
                    outLine[(choiceLineNum + 3)] = "  Answer Received: \"" + ansGiven + "\"";
                    lineNum = choiceLineNum + 3;
                } else {
                    outLine[5] = "  Intended answer: \"" + ansIntend + "\"";
                    outLine[6] = "  Answer Received: \"" + ansGiven + "\"";
                    lineNum = 6;
                }
                //Moved For loop for printing data to outside If/Else block
            }
            for (int outCount = 0; outCount <= lineNum; outCount++) {
                System.out.println(outLine[outCount]);
            }
            JOptionPane.showMessageDialog(null, new JLabel("Check system messages for final chart.", JLabel.CENTER),"", JOptionPane.PLAIN_MESSAGE);


            //Write data to file
            try {
                fw = new FileWriter(filename, true);
                bw = new BufferedWriter(fw);
                bw.newLine();
                bw.newLine();
                for (int writeCount = 0; writeCount <= lineNum; writeCount++) {
                    bw.write(outLine[writeCount]);
                    bw.newLine();
                }
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
*/

                //Ask to continue
                isDone = Integer.parseInt(JOptionPane.showInputDialog(null, "Continue entering questions?\n     1) Yes\n     2) No","", JOptionPane.PLAIN_MESSAGE));
                while (isDone > 2 || isDone < 1) {
                    isDone = Integer.parseInt(JOptionPane.showInputDialog(null, "Invalid input.\n\nContinue entering questions?\n     1) Yes\n     2) No", "ERROR_Input", JOptionPane.PLAIN_MESSAGE));
                }

            } catch (java.lang.NumberFormatException numEx){
                JOptionPane.showMessageDialog(null, new JLabel ("<html>Something went wrong.<br>Be sure to enter a numeric value where requested.</html>", SwingConstants.CENTER), "ERROR_NumForm", JOptionPane.ERROR_MESSAGE);
                numEx.printStackTrace();
            } catch (java.lang.NullPointerException cancel){
                cancel.printStackTrace();
                Object[] cancelOptions = {"Restart", "Quit"};
                cancelChoice = JOptionPane.showOptionDialog(null, new JLabel("<html>Are you sure you want to exit?</html>", JLabel.CENTER), "Cancel", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, cancelOptions, "Restart");
                if (cancelChoice == 1){
                    System.exit(2);
                }
            } catch (java.lang.StringIndexOutOfBoundsException noInput){
                JOptionPane.showMessageDialog(null, "Something went wrong.\nBe sure to enter at least one character at every entry window.", "ERROR_NoInput", JOptionPane.ERROR_MESSAGE);
            }

        }
    }
}


