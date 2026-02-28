package benpapouchado.Turtle.Quiz;

public class Question {
    String question;
    String[] answers;
    int correct_index;

    public Question(String question, String[] answers, int correct_index) throws Exception{
        this.question = question;
        this.answers = answers;
        this.correct_index = correct_index;

        if(answers.length > 4){
            throw new Exception("Only 4 answers allowed");
        }
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String[] getAnswers() {
        return answers;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }

    public int getCorrect_index() {
        return correct_index;
    }

    public void setCorrect_index(int correct_index) {
        this.correct_index = correct_index;
    }
}
