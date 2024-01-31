import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuizDataManager {
    // Save quiz scores to a file
    public static void saveQuizScores(Map<String, ArrayList<Integer>> quizScores, String filePath) {
        ensureFileExists(filePath); // Ensure file exists before saving
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(quizScores);
            System.out.println("Quiz scores saved successfully.\n");
        } catch (IOException e) {
            System.err.println("Error saving quiz scores: " + e.getMessage());
        }
    }

    // Load quiz scores from a file
    public static Map<String, ArrayList<Integer>> loadQuizScores(String filePath) {
        ensureFileExists(filePath); // Ensure file exists before loading
        Map<String, ArrayList<Integer>> quizScores = new HashMap<>();

        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) {
            return quizScores; // Return empty map
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            quizScores = (Map<String, ArrayList<Integer>>) ois.readObject();
            System.out.println("Quiz scores loaded successfully.\n");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading quiz scores: " + e.getMessage());
        }
        return quizScores;
    }

    // Ensure that the file exists, create if it doesn't
    private static void ensureFileExists(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
                System.out.println("New file created at: " + filePath);
            } catch (IOException e) {
                System.err.println("Error creating new file: " + e.getMessage());
            }
        }
    }
}
