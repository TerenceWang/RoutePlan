import java.util.*;

public class Player {
    /**
     * Performs a move
     *
     * @param gameState
     *            the current state of the board
     * @param deadline
     *            time before which we must have returned
     * @return the next state the board is in after our move
     */
	final int[][] inline = {
	        {0,1,2,3},{4,5,6,7},{8,9,10,11},{12,13,14,15},{16,17,18,19},{20,21,22,23},{24,25,26,27},{28,29,30,31},{32,33,34,35},
	        {36,37,38,39},{40,41,42,43},{44,45,46,47},{48,49,50,51},{52,53,54,55},{56,57,58,59},{60,61,62,63},
	        {0,4,8,12},{1,5,9,13},{2,6,10,14},{3,7,11,15}, {16,20,24,28},{17,21,25,29},{18,22,26,30},{19,23,27,31},{32,36,40,44},
	        {33,37,41,45},{34,38,42,46},{35,39,43,47},{48,52,56,60},{49,53,57,61},{50,54,58,62},{51,55,59,63},
	        {0,16,32,48},{1,17,33,49},{2,28,34,50},{3,19,35,51},{4,20,36,52},{5,21,37,53},{6,22,38,54},{7,23,39,55},
	        {8,24,40,56},{9,25,41,57},{10,26,42,58},{11,27,43,59},{12,28,44,60},{13,29,45,61},{14,30,46,62},{15,31,47,63},
	        {0,5,10,15}, {3,6,9,12},{16,21,26,31},{19,22,25,28},{32,37,42,47},{35,38,41,44},{48,53,58,63},{51,54,57,60},
	        {0,20,40,60},{12,24,36,48},{1,21,41,61},{13,25,37,49},{2,22,42,62}, {14,26,38,50},{3,23,43,63},{15,27,39,51},
	        {0,17,34,51},{3,18,33,48},{4,21,38,55},{7,22,37,52},{8,25,42,59}, {11,26,41,56}, {12,29,46,63},{15,30,45,60},
	        {0,21,42,63}, {3,22,41,60}, {12,25,38,51},{15,26,37,48}
	        };
	public int infinity=1000000;
	final int HeuristicScoreEdge[][]= {
			{-10,-5,-15,-10000,-1000000},
	        {4,0,0,0,0},
	        {15,0,0,0,0},
	        {1000,0,0,0,0},
	        {1000000,0,0,0,0}
	     	};
	
    public GameState play(final GameState gameState, final Deadline deadline) {
        Vector<GameState> nextstates = new Vector<GameState>();
        gameState.findPossibleMoves(nextstates);
                
    	Vector<Integer> R_mm =new Vector<Integer>();//store the results of minimax function

    	if (nextstates.size() == 0) {
            // Must play "pass" move if there are no other moves possible.
            return new GameState(gameState, new Move());
        }

        /**
         * Here you should write your algorithms to get the best next move, i.e.
         * the best next state. This skeleton returns a random move instead.
         */
    	int alpha,beta;  
    	int depth = 2;   
    	int bestScore = -infinity;
    	int count = 0;
    	int bestStateIndex = 0;
        for(int i=0;i<nextstates.size();i++){
            alpha=-infinity;
            beta=infinity;
            int score = alphabeta(nextstates.get(i),depth,alpha,beta);
            if(score>bestScore){
            	bestScore = score;
            	bestStateIndex = count;
            }
            count++;
        }


//        int max=-infinity,min=infinity,count=0;
//        for(int i=0;i<nextstates.size();i++){
//            if(gameState.getNextPlayer()==Constants.CELL_O){
//                if(R_mm.get(i)>max){
//                    max=R_mm.get(i);
//                    count=i;
//                   }
//            }
//
//            else{
//
//                if(R_mm.get(i)<min){
//                    min=R_mm.get(i);
//                    count=i;
//                }
//
//            }
//
//        }

        return nextstates.get(bestStateIndex);
        
  
    }  
    
    private  int alphabeta(final GameState gameState, int depth, int alpha, int beta) {
        int bestpossible;
        if(gameState.isEOG()){                  
            if(gameState.isOWin()) return -1000000;
            else if(gameState.isXWin()) return 1000000;
            else return 0;
        }
        
        if (depth == 0  ) {
            return heuristic(gameState);
        } else {
            if (gameState.getNextPlayer() == 2) {
                bestpossible = -infinity;
                Vector<GameState> nextStates = new Vector<GameState>();
                gameState.findPossibleMoves(nextStates);
                for (int i = 0; i < nextStates.size(); i++) {
                    int v = alphabeta(nextStates.get(i), depth - 1,alpha,beta);
                    bestpossible = Math.max(bestpossible, v);
                    alpha= Math.max(alpha,bestpossible);
                    if(beta<=alpha)
                        break;
                }
            } else {
                bestpossible = infinity;
                Vector<GameState> nextStates = new Vector<GameState>();
                gameState.findPossibleMoves(nextStates);
                for (int i = 0; i < nextStates.size(); i++) {
                    int v = alphabeta(nextStates.get(i), depth - 1,alpha,beta);
                    bestpossible = Math.min(bestpossible, v);
                    beta=Math.min(beta,bestpossible);
                    if(beta<=alpha)
                        break;
                }
            }
        }
        return bestpossible;
    }

    
    public int heuristic(final GameState state){
    	 int score = 0;
    	 int piece  = 0;
    	 
    	 for(int i = 0; i < 76; i++)  {
    		 int players = 0;
    		 int opps = 0;
             for (int j = 0; j < 4; j++)  {
            // 	 d1=(inline[i][j]%16)/4;
            // 	 d2=inline[i][j]%4;
            // 	 d3=inline[i][j]/16;
            	 piece = state.at(inline[i][j]);
            	 if(piece == Constants.CELL_X) {
                     players++;                	 
                 }
            	 else if(piece == Constants.CELL_O){
                	 opps++;
                 }
             }
             score += HeuristicScoreEdge[players][opps];
                 
         }	
    	 if(score>infinity){
    		 score=infinity;
    	 }
    	 else if(score<-infinity){
    		 score=-infinity;
    	 }
//    	 System.err.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//    	 System.err.println( state.toString(state.getNextPlayer()));
//    	 System.err.println("Score is : "+ score);
//    	 System.err.println("(((((((((((((((((((((((((((((((((((((((((((((((((((");
    	 
    	 return score;
    	
    	 
    }
   
    
    
}
