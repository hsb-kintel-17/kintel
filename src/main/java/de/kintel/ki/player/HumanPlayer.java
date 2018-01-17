package de.kintel.ki.player;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;

import de.kintel.ki.model.Board;
import de.kintel.ki.model.Field;
import de.kintel.ki.model.Move;
import de.kintel.ki.model.Player;
import de.kintel.ki.model.UMLMove;
import de.kintel.ki.ruleset.IRulesChecker;

public class HumanPlayer extends Participant {

    private final IRulesChecker rc;
    private Map<String, Integer> mapBoardLetters = new LinkedHashMap<>();
    private Board board;
    
    @Autowired
    public HumanPlayer(Player player, IRulesChecker rc){
        super(player);
        this.rc = rc; 
        
        this.mapBoardLetters.put("A", 1);
        this.mapBoardLetters.put("B", 2);
        this.mapBoardLetters.put("C", 3);
        this.mapBoardLetters.put("D", 4);
        this.mapBoardLetters.put("E", 5);
        this.mapBoardLetters.put("F", 6);
        this.mapBoardLetters.put("G", 7);       
        
    }
    
    
    

    @Override
    public Move getNextMove(Board board, int depth) {
        this.board = board;
        Scanner s = new Scanner(System.in);
        
        boolean inputCorrect = false;
        while(!inputCorrect)
        {
            System.out.println("" + this.getPlayer() + " ist am Zug:");
            String humanInput = s.nextLine();
            //TODO Umwandlung der Eingabe
            Move humanMove = this.transform(humanInput);
            if(rc.isValidMove(humanMove)){
                inputCorrect = true;
                System.out.println("Eingabe ist kein valider Zug!");
            }
               
        }       
         
         return humanMove;
    }
    
    
    private Move transform(String humanInput)
    {
        //check lenght of input: Must be 4
        if(humanInput.length() != 4)
        {
            return null;
        }
        
        //Check if Letters on position 0 and 2 are in keyMap
        //Check if Integers on position 1 and 3 are between 1 and 9 (1 and 9 inclusive)
        for(int i = 0; i < 4; i++)
        {
            if(i % 2 == 0)
            {
                //checks if letter is in List
                if(!this.mapBoardLetters.containsKey(humanInput.charAt(i)))
                {
                    return null;
                }
            }
            else
            {
                if(1 <= Character.getNumericValue(humanInput.charAt(i)) &&  Character.getNumericValue(humanInput.charAt(i)) <= 9)
                {
                    return null;
                }
            }
        }
        
        int coordinates[] = {humanInput.charAt(0), humanInput.charAt(1), humanInput.charAt(2),humanInput.charAt(3)};
        
        Field startField = board.getField(coordinates[0], coordinates[1]);
        Field targetField = board.getField(coordinates[2], coordinates[3]);
        
        Move move = new UMLMove(this.board, startField, targetField, this.getPlayer());
              
        return move;
        
    }


}
