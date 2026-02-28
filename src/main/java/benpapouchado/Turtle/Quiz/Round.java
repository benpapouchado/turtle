package benpapouchado.Turtle.Quiz;

import java.util.List;

public class Round {
    private List<Question> round;

    protected Round(List<Question> round) throws Exception {
        this.round = round;

        if(round.size() > 6){
            throw new Exception("Max size reached");
        }
    }

    public List<Question> getRound() {
        return round;
    }

    public void setRound(List<Question> round) {
        this.round = round;
    }

    private void add_question(Question question){
        getRound().add(question);
    }
}
