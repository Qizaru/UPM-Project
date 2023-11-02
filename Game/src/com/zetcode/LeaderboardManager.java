package com.zetcode;

import javax.swing.*;
import java.awt.Font;
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
            for (String entry : leaderboardData) {
                leaderboardModel.addElement(entry);
            }

            JList<String> leaderboardList = new JList<>(leaderboardModel);

            // Use a smaller font size
            leaderboardList.setFont(new Font("Arial", Font.PLAIN, 14));

            frame.add(new JScrollPane(leaderboardList));
            frame.setLocation(650, 320);
            ImageIcon licon = new ImageIcon("src/resources/images/licon.png");
            frame.setIconImage(licon.getImage());
            frame.setSize(200, 200);
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
