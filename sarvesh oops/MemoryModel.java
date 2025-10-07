import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemoryModel {
    private List<String> cards;
    private boolean[] matched;

    public MemoryModel() {
        resetGame();
    }

    public void resetGame() {
        cards = new ArrayList<>();
        String[] values = {
            "apple.png","banana.png","berry.png","grape.png",
            "kiwi.png","lemon.png","orange.png","watermelon.png"
        };
        for (String v : values) {
            cards.add(v);
            cards.add(v);
        }
        Collections.shuffle(cards);
        matched = new boolean[cards.size()];
    }

    public String getCardValue(int index) {
        return cards.get(index);
    }

    public boolean isMatched(int index) {
        return matched[index];
    }

    public void setMatched(int i1, int i2) {
        matched[i1] = true;
        matched[i2] = true;
    }

    public boolean allMatched() {
        for (boolean b : matched)
            if (!b) return false;
        return true;
    }

    public int getSize() {
        return cards.size();
    }
}
