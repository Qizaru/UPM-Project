package com.zetcode;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class LeaderboardManager {
    public static boolean saver = false;

    public static boolean displayLeaderboard(int highScore, String playerName) {
        JFrame frame = new JFrame("Leaderboard");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        List<String> leaderboardData = loadLeaderboardData("leaderboard.txt");

        String newEntry = playerName + ": " + highScore;

        boolean scoreAdded = false;

        if (leaderboardData.isEmpty() || leaderboardData.size() < 5) {
            leaderboardData.add(newEntry);
            scoreAdded = true;
        } else {
            int lowestScore = Integer.MAX_VALUE;
            int lowestScoreIndex = -1;

            for (int i = 0; i < leaderboardData.size(); i++) {
                int score = Integer.parseInt(leaderboardData.get(i).split(": ")[1]);
                if (score < lowestScore) {
                    lowestScore = score;
                    lowestScoreIndex = i;
                }
            }

            if (highScore > lowestScore) {
                leaderboardData.remove(lowestScoreIndex);
                leaderboardData.add(newEntry);
                scoreAdded = true;
            }
        }

        if (scoreAdded) {
            Collections.sort(leaderboardData, (s1, s2) -> {
                int score1 = Integer.parseInt(s1.split(": ")[1]);
                int score2 = Integer.parseInt(s2.split(": ")[1]);
                return Integer.compare(score2, score1); // Descending order
            });

            DefaultListModel<String> leaderboardModel = new DefaultListModel<>();
            for (int i = 0; i < leaderboardData.size(); i++) {
                String entry = leaderboardData.get(i);
                String[] parts = entry.split(": ");
                String name = parts[0];
                int score = Integer.parseInt(parts[1]);
                String rank = Integer.toString(i + 1); // Ranking numbers

                // Use a custom renderer to format and style the leaderboard
                String formattedEntry = String.format("  %-2s %-15s %s", rank, name, score);
                leaderboardModel.addElement(formattedEntry);
            }

            JList<String> leaderboardList = new JList<>(leaderboardModel);

            // Use a monospaced font for consistent alignment
            leaderboardList.setFont(new Font("Monospaced", Font.BOLD, 16));
            leaderboardList.setForeground(Color.YELLOW);
            leaderboardList.setBackground(Color.BLACK);
            leaderboardList.setSelectionBackground(Color.RED);

            JScrollPane scrollPane = new JScrollPane(leaderboardList);
            frame.add(scrollPane);
            
            // Calculate the frame height based on the number of entries
            int numEntries = leaderboardModel.getSize();
            int frameHeight = Math.min(600, numEntries * 22 + 50); // Limit max height
            
            frame.setLocation(650, 320);
            ImageIcon licon = new ImageIcon("src/resources/images/licon.png");
            frame.setIconImage(licon.getImage());
            
            frame.setSize(300, frameHeight); // Set frame size
            
            frame.setVisible(true);

            saver = saveLeaderboardData(leaderboardData, "leaderboard.txt");
        } else {
            JOptionPane.showMessageDialog(null, "Sorry, your score is too low!!");
        }

        return true;
    }

   private static List<String> loadLeaderboardData(String fileName) {
        List<String> leaderboardData = new ArrayList<>();
        try {
            File file = new File(fileName);
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        leaderboardData.add(line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return leaderboardData;
    }

    private static boolean saveLeaderboardData(List<String> leaderboardData, String fileName) {
        try {
            File file = new File(fileName);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String entry : leaderboardData) {
                    writer.write(entry);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
