package g1; // TODO modify the package name to reflect your team

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sim.Game;
import sim.GameHistory;
import sim.SimPrinter;

public class Player extends sim.Player {

     /**
      * Player constructor
      *
      * @param teamID      team ID
      * @param rounds      number of rounds
      * @param seed        random seed
      * @param simPrinter  simulation printer
      *
      */
     public Player(Integer teamID, Integer rounds, Integer seed, SimPrinter simPrinter) {
          super(teamID, rounds, seed, simPrinter);
     }

     /**
      * Reallocate player goals
      *
      * @param round             current round
      * @param gameHistory       cumulative game history from all previous rounds
      * @param playerGames       state of player games before reallocation
      * @param opponentGamesMap  state of opponent games before reallocation (map of opponent team IDs to their games)
      * @return                  state of player games after reallocation
      *
      */
     public List<Game> reallocate(Integer round, GameHistory gameHistory, List<Game> playerGames, Map<Integer, List<Game>> opponentGamesMap) {
          // TODO add your code here to reallocate player goals

          //don't use:
          //nothing with gameHistory
          //disregard opponentGamesMap
               // used to detect trends in opponent's reallocations 
          
          
          //use:
          //player games, indexed by round
          //how many we have available to reallocate?
          
          
          // if our team won, remove as many points as possible (half at most)
          // this ensures a win while maximizing points 


          // if it was a draw, either remove all points or do not remove any and hope the opponent removes points
          
          // return null; // TODO modify the return statement to return your list of reallocated player games

             // strategy for future deliverable? vvv
          //sort by won games how much we won by
		//sort lost games by how much we lost by
		//match these up

          // if we won:
               // take away our score - opponents score + 1 so long as our score / 2 is not > this number 
           
          List<Game> reallocatedPlayerGames = new ArrayList<>();
          
          List<Game> wonGames = getWinningGames(playerGames);
          List<Game> drawnGames = getDrawnGames(playerGames);
          List<Game> lostGames = getLosingGames(playerGames);
          
          List<Game> lostOrDrawnGamesWithReallocationCapacity = new ArrayList<>(lostGames);
          lostOrDrawnGamesWithReallocationCapacity.addAll(drawnGames);
          for(Game lostGame : lostGames)
               if(lostGame.maxPlayerGoalsReached())
                    lostOrDrawnGamesWithReallocationCapacity.remove(lostGame);
          for(Game drawnGame : drawnGames)
               if(drawnGame.maxPlayerGoalsReached())
                    lostOrDrawnGamesWithReallocationCapacity.remove(drawnGame);
          
               
          

               //if we're more than double the score of the opponent, remove score/2
               //else remove our score-their score - 1

               //allocate to random game

          for(Game winningGame : wonGames) {    		 
               
               if(lostOrDrawnGamesWithReallocationCapacity.size() == 0)
                    break;

               Game randomLostOrDrawnGame = lostOrDrawnGamesWithReallocationCapacity.get(this.random.nextInt(lostOrDrawnGamesWithReallocationCapacity.size()));

               int halfNumPlayerGoals = winningGame.getHalfNumPlayerGoals();
               //int numRandomGoals = (int) Math.min(this.random.nextInt(halfNumPlayerGoals) + 1, Game.getMaxGoalThreshold() - randomLostOrDrawnGame.getNumPlayerGoals());

			   int numRandomGoals;

			   int ourScore = winningGame.getScore().getNumPlayerGoals();
			   int theirScore = winningGame.getScore().getNumOpponentGoals();

			   if(halfNumPlayerGoals > theirScore) {
				   numRandomGoals = halfNumPlayerGoals;
			   }
			   else {
				   numRandomGoals = ourScore - theirScore - 1;
			   }

               winningGame.setNumPlayerGoals(winningGame.getNumPlayerGoals() - numRandomGoals);
               randomLostOrDrawnGame.setNumPlayerGoals(randomLostOrDrawnGame.getNumPlayerGoals() + numRandomGoals);
               
               if(randomLostOrDrawnGame.maxPlayerGoalsReached())
                    lostOrDrawnGamesWithReallocationCapacity.remove(randomLostOrDrawnGame);
          }
          
          reallocatedPlayerGames.addAll(wonGames);
          reallocatedPlayerGames.addAll(drawnGames);
          reallocatedPlayerGames.addAll(lostGames);

          if(checkConstraintsSatisfied(playerGames, reallocatedPlayerGames))
               return reallocatedPlayerGames;
          return playerGames;

     }

     // stolen from random >:D
     private List<Game> getWinningGames(List<Game> playerGames) {
    	 List<Game> winningGames = new ArrayList<>();
    	 for(Game game : playerGames) {
    		 int numPlayerGoals = game.getNumPlayerGoals();
    		 int numOpponentGoals = game.getNumOpponentGoals();
    		 if(numPlayerGoals > numOpponentGoals)
    			 winningGames.add(game.cloneGame());
    	 }
    	 return winningGames;
     }

     private List<Game> getDrawnGames(List<Game> playerGames) {
    	 List<Game> drawnGames = new ArrayList<>();
    	 for(Game game : playerGames) {
    		 int numPlayerGoals = game.getNumPlayerGoals();
    		 int numOpponentGoals = game.getNumOpponentGoals();
    		 if(numPlayerGoals == numOpponentGoals)
    			 drawnGames.add(game.cloneGame());
    	 }
    	 return drawnGames;
     }
     
     private List<Game> getLosingGames(List<Game> playerGames) {
    	 List<Game> losingGames = new ArrayList<>();
    	 for(Game game : playerGames) {
    		 int numPlayerGoals = game.getNumPlayerGoals();
    		 int numOpponentGoals = game.getNumOpponentGoals();
    		 if(numPlayerGoals < numOpponentGoals)
    			 losingGames.add(game.cloneGame());
    	 }
    	 return losingGames;
     }
}
