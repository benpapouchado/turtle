package benpapouchado.Turtle.Quiz;

import java.util.List;

public class Quiz {
    private List<Round> rounds;

    public Quiz(List<Round> rounds) {
        this.rounds = rounds;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public void setRounds(List<Round> rounds) {
        this.rounds = rounds;
    }

    public void add_round(Round round){
        getRounds().add(round);
    }
}
