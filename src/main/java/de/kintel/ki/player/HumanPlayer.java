package de.kintel.ki.player;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;

import de.kintel.ki.algorithm.KI;
import de.kintel.ki.model.Board;
import de.kintel.ki.model.Move;
import de.kintel.ki.ruleset.IRulesChecker;
import de.kintel.ki.ruleset.RulesChecker;

public class HumanPlayer extends Player {

    private final IRulesChecker rc;
    
    @Autowired
    public HumanPlayer(Player.PlayerColor player, IRulesChecker rc){
        super(player);
        this.rc = rc; 
    }
       
    

    @Override
    public Move getNextMove(Board board, int depth) {
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

        
         
         return null;
    }
    
    
    private Move transform(String humanInput)
    {
        //TODO umwandeln
        
    }


}
