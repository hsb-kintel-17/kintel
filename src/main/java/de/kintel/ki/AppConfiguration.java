package de.kintel.ki;

import com.google.common.eventbus.EventBus;
import de.kintel.ki.model.Board;
import de.kintel.ki.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class AppConfiguration {
    @Bean
    @Scope("singleton")
    public EventBus getEventBus() {
        return new EventBus(getClass().getName());
    }

    @Bean
    @Scope("singleton")
    public Board board(@Value("${board.height}") int boardHeight, @Value("${board.width}") int boardWidth) {
        return new Board(boardHeight, boardWidth);
    }

    @Bean
    public Player getDefaultPlayer() {
        return Player.SCHWARZ;
    }
}
