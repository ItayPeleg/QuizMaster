import java.util.*;

public class QuizTaker {
    private final String name;
    private final Timer timer;

    public QuizTaker(String name) {
        this.name = name;
        timer = new Timer();
    }

    public int takeQuiz(Quiz quiz, Scanner input) {
        System.out.println("Welcome to the quiz!\nThere will be presented 10 random questions.\nLets start!\n");
        List<Integer> playerAnswers = new ArrayList<>();

        // Set the fixed duration for the quiz (5 minutes)
        int durationInSeconds = 300;

        // Convert duration from seconds to minutes and seconds
        int minutes = durationInSeconds / 60;

        // Print the initial message with timer duration
        System.out.println("You have 0" + minutes + ":00 minutes to complete the quiz.\n");

        // Start the timer
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Time's up! Quiz ended.");
                timer.cancel();
            }
        }, durationInSeconds * 1000); // Convert seconds to milliseconds

        for (Question question : quiz.questions) {
            question.displayQuestion();
            playerAnswers.add(getValidAnswer(input));
        }
        int score = quiz.scoreQuiz(playerAnswers);

        System.out.println("The quiz score is: " + score + "/10");

        input.nextLine();

        timer.cancel();

        return score;
    }

    // Method to get a valid answer from the user
    private int getValidAnswer(Scanner input) {
        int userAnswer = 0;
        boolean validInput = false;

        while (!validInput) {
            System.out.println("Please enter your answer: ");
            if (input.hasNextInt()) {
                userAnswer = input.nextInt();
                if (userAnswer >= 0 && userAnswer <= 2) {
                    validInput = true;
                } else {
                    System.out.println("Invalid input. Please enter a number between 0 and 2.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                input.next(); // Consume the invalid input
            }
        }
        return userAnswer;
    }
}
