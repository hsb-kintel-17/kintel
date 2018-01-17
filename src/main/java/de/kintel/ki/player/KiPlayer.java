package de.kintel.ki.player;
import de.kintel.ki.algorithm.KI;
import de.kintel.ki.model.Board;
import de.kintel.ki.model.Move;
import de.kintel.ki.model.Player;
import org.springframework.beans.factory.annotation.Autowired;

public class KiPlayer extends Participant {
    
    private final KI ki;

    public KiPlayer(Board board, Player player, KI ki) {
        super( board, player );
        this.ki = ki;
    }
    
    @Override
    public Move getNextMove(int depth) {
        ki.setCurrentPlayer(getPlayer());
        return ki.getBestMove(depth);
    }

}
