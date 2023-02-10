/**
* provide the implementation of a single NFA Node
*/

import java.util.*;

class NFAStateStep
{
     /**
     * Using BFS(breath-first search) to go through the NFAStateList
     * If the pattern is match return true also return false
     * @param NFAStateList, String
     * @return Boolean
     */
    public static Boolean BFS(NFAStateList s, String input)
    {
        // get the list
        ArrayList<NFAState> list = s.getNFAList();

        // get final state
        int FinalState = s.getFinalState();

        // Create a tuple to record [pos_of_list, pos_of_string]
        // Create a queue for BFS
        ArrayList<int[]> queue = new ArrayList<int[]>();

        // Enqueue the initial state, and make the current position of the string as -1
        int[] newInit = {0,0};
        queue.add(newInit);
 
        while (queue.size() != 0)
        {
            // dequeue
            int[] current =  queue.remove(0);

            // check if the current state is final
            if(current[0] == FinalState)
            {
                if(current[1] == input.length())
                    return true;
            }

            // get the all the nextstates of the current NFAState
            NFAState currentState = list.get(current[0]);
            ArrayList<Tuple> nextStates = currentState.getNextStates();

            // go through all the next states, find possible next states which can be transited
           
            for(int i = 0; i < nextStates.size(); i++)
            {
                if(current[1] < input.length() && nextStates.get(i).getSymbol() == input.charAt(current[1]))
                {
                    
                    int[] newPair = {nextStates.get(i).getPos(), current[1] + 1};
                    queue.add(newPair);
                }
            }
           
            for(int i = 0; i < nextStates.size(); i++)
            {
                if(nextStates.get(i).getSymbol() == '@')
                {
                    int[] newPair = {nextStates.get(i).getPos(), current[1]};
                    queue.add(newPair);
                }
            }
        
        }
        return false;
    }



    /**
     * create a NFA state table for verbose mode
     * @param NFAStateList
     * @return void
     */
    public static void NFAStateListTableCreator(NFAStateList l)
    {
        System.out.println("\nSize: " + l.getNFAList().size());
        System.out.println("StartingState: " + 0);
        System.out.println("FinalState: " + l.getFinalState());
        for(int i = 0; i < l.getNFAList().size(); i++)
        {
            if(i != l.getFinalState())
                l.getNFAList().get(i).ToString();
        }
        System.out.println("node:" + l.getFinalState() + "   FinalState");
        System.out.println();
    }
}
