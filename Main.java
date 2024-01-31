import java.io.File;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // File path of the quiz scores.
        String filePath = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "quiz_scores.dat";

        // Save the user score.
        int userScore;

        // HashMap<String, ArrayList> named quizScores
        HashMap<String, ArrayList<Integer>> quizScores = (HashMap<String, ArrayList<Integer>>) QuizDataManager.loadQuizScores(filePath);

        // Create an instance of the Quiz class and populate it with questions using the populateQuestions and generateRandomQuiz method.
        Quiz quiz = new Quiz(generateRandomQuiz());

        Scanner input = new Scanner(System.in);

        // Choice of the menu.
        int choice;
        String playerName = "";

        while (true) {
            // Display main menu options
            System.out.println("\nMain Menu:");
            System.out.println("1. Start Quiz");
            System.out.println("2. Display Results For a Specific Player");
            System.out.println("3. Display Results For All Players");
            System.out.println("4. Display Leaderboard");
            System.out.println("5. Exit");
            System.out.println("Enter your choice: ");

            choice = getMenuChoice(input);

            // Handle user choice
            switch (choice) {
                case 1:
                    // Start Quiz
                    playerName = getPlayerNameAndId(input);

                    // Create an instance of the QuizTaker class
                    QuizTaker quizTaker = new QuizTaker(playerName);
                    userScore = quizTaker.takeQuiz(quiz, input);

                    // Retrieve existing scores for the player, or create a new list if none exists
                    ArrayList<Integer> playerScores = quizScores.getOrDefault(playerName, new ArrayList<>());

                    // Add the new score to the list of player scores
                    playerScores.add(userScore);

                    // Update the quizScores map with the player's scores
                    quizScores.put(playerName, playerScores);

                    // Save updated quiz scores to file
                    QuizDataManager.saveQuizScores(quizScores, filePath);

                    printQuizScores(quizScores);
                    break;
                case 2:
                    // Display Results
                    displayResultsForPlayer(quizScores, input);
                    break;
                case 3:
                    // Display Results
                    printQuizScores(quizScores);
                    break;
                case 4:
                    // Display Leaderboard
                    displayLeaderboard(quizScores);
                    break;
                case 5:
                    // Exit
                    System.out.println("Exiting Program. Goodbye!");
                    input.close(); // Close the scanner
                    return; // Exit the main method
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private static int getMenuChoice(Scanner input) {
        int choice = -1;
        boolean validInput = false;

        while (!validInput) {
            if (input.hasNextInt()) {
                choice = input.nextInt();
                input.nextLine();
                if (choice >= 1 && choice <= 5) {
                    validInput = true;
                } else {
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
                input.nextLine(); // Consume the invalid input
            }
        }
        return choice;
    }

    // Method to get player name and ID from user input
    private static String getPlayerNameAndId(Scanner input) {
        String name = "";
        String id = "";
        boolean validName = false;
        boolean validId = false;

        while (!validName) {
            System.out.println("Enter player's name (letters only): ");
            name = input.nextLine(); // Read the entire line
            validName = name.matches("[a-zA-Z]+");
            if (!validName) {
                System.out.println("Invalid input. Player's name should contain only letters.");
            }
        }

        while (!validId) {
            System.out.println("Enter player's ID (numbers only, last four digits): ");
            id = input.nextLine(); // Read the entire line
            validId = id.matches("[0-9]{4}");
            if (!validId) {
                System.out.println("Invalid input. Player's ID should contain only 4 digits.");
            }
        }

        return name + "_" + id;
    }

    //Function to display results for specific player.
    private static void displayResultsForPlayer(HashMap<String, ArrayList<Integer>> quizScores, Scanner input) {
        String playerName = getPlayerNameAndId(input);

        if (quizScores.containsKey(playerName)) {
            ArrayList<Integer> scores = quizScores.get(playerName);
            System.out.println("Results for " + playerName + ": " + scores);
        } else {
            System.out.println("Player not found.\n");
        }
    }

    //Function to display results for all players.
    private static void printQuizScores(HashMap<String, ArrayList<Integer>> quizScores) {
        if (quizScores.isEmpty()) {
            System.out.println("No quiz scores available.\n");
        } else {
            for (Map.Entry<String, ArrayList<Integer>> entry : quizScores.entrySet()) {
                String username = entry.getKey();
                ArrayList<Integer> scores = entry.getValue();

                System.out.println("Username: " + username);
                System.out.println("Quiz Scores: " + scores);
                System.out.println();
            }
        }
    }

    // Function to display the leaderboard
    private static void displayLeaderboard(HashMap<String, ArrayList<Integer>> quizScores) {
        if (quizScores.isEmpty()) {
            System.out.println("No quiz scores available.\n");
            return;
        }

        // List to store users and their maximum scores
        List<Map.Entry<String, Integer>> leaderboard = new ArrayList<>();

        // Iterate through each user and find their maximum score
        for (Map.Entry<String, ArrayList<Integer>> entry : quizScores.entrySet()) {
            String username = entry.getKey();
            ArrayList<Integer> scores = entry.getValue();

            // Find maximum score for the user
            int maxScore = scores.stream().max(Integer::compareTo).orElse(0);

            // Add user and their maximum score to the leaderboard list
            leaderboard.add(new AbstractMap.SimpleEntry<>(username, maxScore));
        }

        // Sort the leaderboard based on maximum scores in descending order
        Collections.sort(leaderboard, (a, b) -> b.getValue().compareTo(a.getValue()));

        // Display the leaderboard
        System.out.println("Leaderboard:");
        for (int i = 0; i < leaderboard.size(); i++) {
            Map.Entry<String, Integer> entry = leaderboard.get(i);
            System.out.println((i + 1) + ". " + entry.getKey() + ": " + entry.getValue());
        }
    }

    //Function that generates random questions from the repository.
    private static List<Question> generateRandomQuiz() {
        List<Question> allQuestions = populateQuestions();

        if (allQuestions.size() < 10) {
            throw new IllegalArgumentException("Insufficient questions to generate a quiz.");
        }

        // Shuffle the list of all questions
        Collections.shuffle(allQuestions);

        // Select the first 10 questions (randomly shuffled)
        return allQuestions.subList(0, 10);
    }

    //Function that populates questions for the quiz.
    private static List<Question> populateQuestions() {
        List<Question> questions = new ArrayList<>();

        // Question 1
        List<String> options1 = Arrays.asList("0.Berlin", "1.Paris", "2.Madrid");
        questions.add(new Question("What is the capital of France?", options1, 1));

        // Question 2
        List<String> options2 = Arrays.asList("0.Mars", "1.Jupiter", "2.Saturn");
        questions.add(new Question("Which planet is known as the Red Planet?", options2, 0)); // Answer: Mars

        // Question 3
        List<String> options3 = Arrays.asList("0.New York", "1.Los Angeles", "2.Chicago");
        questions.add(new Question("What is the most populous city in the United States?", options3, 0)); // Answer: New York

        // Question 4
        List<String> options4 = Arrays.asList("0.Shakespeare", "1.Chaucer", "2.Milton");
        questions.add(new Question("Who wrote 'Romeo and Juliet'?", options4, 0)); // Answer: Shakespeare

        // Question 5
        List<String> options5 = Arrays.asList("0.Einstein", "1.Newton", "2.Galileo");
        questions.add(new Question("Who developed the theory of relativity?", options5, 0)); // Answer: Einstein

        // Question 6
        List<String> options6 = Arrays.asList("0.Tokyo", "1.Beijing", "2.Seoul");
        questions.add(new Question("What is the capital of Japan?", options6, 0)); // Answer: Tokyo

        // Question 7
        List<String> options7 = Arrays.asList("0.Nile", "1.Amazon", "2.Mississippi");
        questions.add(new Question("Which river is the longest in the world?", options7, 0)); // Answer: Nile

        // Question 8
        List<String> options8 = Arrays.asList("0.Celsius", "1.Fahrenheit", "2.Kelvin");
        questions.add(new Question("What is the standard unit for temperature in most of the world?", options8, 0)); // Answer: Celsius

        // Question 9
        List<String> options9 = Arrays.asList("0.Australia", "1.Africa", "2.Europe");
        questions.add(new Question("Which continent is known as the 'Down Under'?", options9, 0)); // Answer: Australia

        // Question 10
        List<String> options10 = Arrays.asList("0.Gold", "1.Silver", "2.Bronze");
        questions.add(new Question("What is the third-place medal in the Olympic Games typically made of?", options10, 2)); // Answer: Bronze

        // Question 11
        List<String> options11 = Arrays.asList("0.Cuba", "1.Puerto Rico", "2.Dominican Republic");
        questions.add(new Question("In which Caribbean island did the Cuban Missile Crisis take place?", options11, 0)); // Answer: Cuba

        // Question 12
        List<String> options12 = Arrays.asList("0.Great Barrier Reef", "1.Mount Everest", "2.Amazon Rainforest");
        questions.add(new Question("What is the largest coral reef system in the world?", options12, 0)); // Answer: Great Barrier Reef

        // Question 13
        List<String> options13 = Arrays.asList("0.India", "1.China", "2.Russia");
        questions.add(new Question("Which country has the largest population?", options13, 1)); // Answer: China

        // Question 14
        List<String> options14 = Arrays.asList("0.Harry Potter", "1.Lord of the Rings", "2.The Chronicles of Narnia");
        questions.add(new Question("Which book series was written by J.K. Rowling?", options14, 0)); // Answer: Harry Potter

        // Question 15
        List<String> options15 = Arrays.asList("0.Louis Armstrong", "1.Miles Davis", "2.John Coltrane");
        questions.add(new Question("Who is considered one of the greatest jazz trumpeters of all time?", options15, 0)); // Answer: Louis Armstrong

        // Question 16
        List<String> options16 = Arrays.asList("0.English", "1.Spanish", "2.Mandarin");
        questions.add(new Question("Which language is the most spoken in the world?", options16, 2)); // Answer: Mandarin

        // Question 17
        List<String> options17 = Arrays.asList("0.Africa", "1.Asia", "2.Europe");
        questions.add(new Question("Which continent is the largest by land area?", options17, 1)); // Answer: Asia

        // Question 18
        List<String> options18 = Arrays.asList("0.Mona Lisa", "1.Starry Night", "2.The Scream");
        questions.add(new Question("Which painting is famous for its mysterious smile?", options18, 0)); // Answer: Mona Lisa

        // Question 19
        List<String> options19 = Arrays.asList("0.Mercury", "1.Venus", "2.Uranus");
        questions.add(new Question("Which planet is the hottest in our solar system?", options19, 0)); // Answer: Mercury

        // Question 20
        List<String> options20 = Arrays.asList("0.Eiffel Tower", "1.Statue of Liberty", "2.Colosseum");
        questions.add(new Question("Which iconic landmark is located in Paris, France?", options20, 0)); // Answer: Eiffel Tower

        // Question 21
        List<String> options21 = Arrays.asList("0.Leonardo da Vinci", "1.Vincent van Gogh", "2.Pablo Picasso");
        questions.add(new Question("Who painted the 'Starry Night'?", options21, 1)); // Answer: Vincent van Gogh

        // Question 22
        List<String> options22 = Arrays.asList("0.North America", "1.South America", "2.Africa");
        questions.add(new Question("Which continent is the Amazon Rainforest primarily located in?", options22, 1)); // Answer: South America

        // Question 23
        List<String> options23 = Arrays.asList("0.Napoleon Bonaparte", "1.Alexander the Great", "2.Julius Caesar");
        questions.add(new Question("Who was the first Emperor of Rome?", options23, 0)); // Answer: Napoleon Bonaparte

        // Question 24
        List<String> options24 = Arrays.asList("0.Japan", "1.China", "2.Korea");
        questions.add(new Question("Which country is known as the 'Land of the Rising Sun'?", options24, 0)); // Answer: Japan

        // Question 25
        List<String> options25 = Arrays.asList("0.Leonardo da Vinci", "1.Michelangelo", "2.Raphael");
        questions.add(new Question("Who painted the 'School of Athens'?", options25, 1)); // Answer: Michelangelo

        // Question 26
        List<String> options26 = Arrays.asList("0.Dogs", "1.Cats", "2.Fish");
        questions.add(new Question("Which animal is known as 'Man's best friend'?", options26, 0)); // Answer: Dogs

        // Question 27
        List<String> options27 = Arrays.asList("0.Brazil", "1.Argentina", "2.Peru");
        questions.add(new Question("Which country is home to Machu Picchu?", options27, 2)); // Answer: Peru

        // Question 28
        List<String> options28 = Arrays.asList("0.Egypt", "1.Sudan", "2.Algeria");
        questions.add(new Question("Which country is home to the Pyramids of Giza?", options28, 0)); // Answer: Egypt

        // Question 29
        List<String> options29 = Arrays.asList("0.Socrates", "1.Plato", "2.Aristotle");
        questions.add(new Question("Who was the teacher of Alexander the Great?", options29, 1)); // Answer: Plato

        // Question 30
        List<String> options30 = Arrays.asList("0.Portuguese", "1.Italian", "2.German");
        questions.add(new Question("What is the official language of Brazil?", options30, 0)); // Answer: Portuguese

        return questions;
    }

}
