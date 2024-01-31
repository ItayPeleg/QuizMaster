import java.util.List;

public class Question {
    private final String questionText;
    private final List<String> options;
    private final int correctOptionIndex;

    public Question(String questionText, List<String> options, int correctOptionIndex) {
        this.questionText = questionText;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
    }

    public boolean isCorrectAnswer(int answerIndex) {
        return this.correctOptionIndex == answerIndex;
    }

    public void displayQuestion() {
        System.out.println(this.questionText + "\n" + this.options);
    }
}
