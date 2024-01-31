import java.util.List;

public class Quiz {
    protected List<Question> questions;

    public Quiz(List<Question> questions) {
        this.questions = questions;
    }

    public int scoreQuiz(List<Integer> playerAnswers) {
        int score = 0;
        int answerIndex = 0;
        for (Question question : questions) {
            if (question.isCorrectAnswer(playerAnswers.get(answerIndex))) {
                score++;
            }
            answerIndex++;
        }
        return score;
    }
}
