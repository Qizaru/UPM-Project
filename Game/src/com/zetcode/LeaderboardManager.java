package com.zetcode;

import javax.swing.*;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class LeaderboardManager {
    public static boolean displayLeaderboard(int highScore) {
        JFrame frame = new JFrame("Leaderboard");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        List<String> leaderboardData = loadLeaderboardData("leaderboard.txt");

        String playerName = JOptionPane.showInputDialog("Enter your name: ");
        int playerScore = highScore;

        String newEntry = playerName + ": " + playerScore;
        leaderboardData.add(newEntry);

        Collections.sort(leaderboardData, (s1, s2) -> {
            int score1 = Integer.parseInt(s1.split(": ")[1]);
            int score2 = Integer.parseInt(s2.split(": ")[1]);
            return Integer.compare(score2, score1); // Descending order
        });

        int maxEntries = 5;
        if (leaderboardData.size() > maxEntries) {
            leaderboardData = leaderboardData.subList(0, maxEntries);
        }

        DefaultListModel<String> leaderboardModel = new DefaultListModel<>();
        for (String entry : leaderboardData) {
            leaderboardModel.addElement(entry);
        }

        JList<String> leaderboardList = new JList<>(leaderboardModel);

        // Use a smaller font size
        leaderboardList.setFont(new Font("Arial", Font.PLAIN, 14));

        frame.add(new JScrollPane(leaderboardList));
        
        frame.setLocation(650,320 );
        ImageIcon licon = new ImageIcon("src/resources/images/licon.png");
        frame.setIconImage(licon.getImage());
        
        frame.setSize(200, 200);
        frame.setVisible(true);

        final boolean[] leaderboardClosed = {false}; // Create an array to hold the flag

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                frame.dispose(); // Close the leaderboard window
                leaderboardClosed[0] = true; // Set the flag to indicate the leaderboard was closed
            }
        });

        saveLeaderboardData(leaderboardData, "leaderboard.txt");

        return leaderboardClosed[0];
    }

    private static List<String> loadLeaderboardData(String fileName) {
        List<String> leaderboardData = new ArrayList<>();
        try {
            File file = new File(fileName);
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    leaderboardData.add(line);
                }
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return leaderboardData;
    }

    private static void saveLeaderboardData(List<String> leaderboardData, String fileName) {
        try {
            File file = new File(fileName);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (String entry : leaderboardData) {
                writer.write(entry);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
