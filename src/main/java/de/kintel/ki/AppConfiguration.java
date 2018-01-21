package de.kintel.ki;

import com.google.common.eventbus.EventBus;
import de.kintel.ki.cli.TablePrinter;
import de.kintel.ki.model.Board;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    @Scope("singleton")
    public TablePrinter tablePrinter() {
        List<String> columns = IntStream.rangeClosed(0, 9).boxed().map(String::valueOf).collect(Collectors.toList());;
        return new TablePrinter("", columns);
    }
}
