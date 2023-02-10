/**
* provide the implementation of a single NFA Node
*/


import java.util.*;


public class NFAState {

    // variables
    private int currentStateNumber = -1;
    private ArrayList<Tuple> nextStates = new ArrayList<Tuple>(); 


    /**
     * constructor which creates a node single NFA node 
     * @param currentStateNumber
     * @return void
     */
    public NFAState(int currentStateNumber)
    {
        this.currentStateNumber = currentStateNumber;
    }


    /**
     * getter, get the currentStateNumber
     * @param void
     * @return currentStateNumber
     */
    public int getCurrentStateNumber()
    {
        return this.currentStateNumber;
    }


    /**
     * constructor which creates a node single NFA node 
     * @param void
     * @return nextStates
     */
    public ArrayList<Tuple> getNextStates()
    {
        return this.nextStates;
    }


    /**
     * This method links the current node to another existed node 
     * @param pos,symbol 
     * @return void
     */
    public void addNewLinks(int pos, char symbol)
    {
        // We will always assume 
        // the first element of the tuple is state to be linked
        // the second element of the tuple is the requirement(input symbol) to go to that state
        nextStates.add(new Tuple(pos, symbol));
    }


    /**
     * This method links the current node to another existed node 
     * @param pos,symbol 
     * @return void
     */
    public void addNewLinksInGroups(ArrayList<Tuple> nextStates)
    {
        for(int i = 0; i < nextStates.size(); i++)
            this.nextStates.add(new Tuple(nextStates.get(i).getPos(), nextStates.get(i).getSymbol()));
    }


    /**
     * This method checks whether the current node should be connected to the final state
     * @param pos,symbol 
     * @return void
     */
    public Boolean isFinal()
    {
        if(nextStates.size() == 0)
            return true;
        return false;
    }


    /**
     * This method checks whether this node is connected to the final state node
     * @param pos,symbol 
     * @return void
     */
    public Boolean isNextFinal(int FinalPosition)
    {
        for(int i = 0; i < this.nextStates.size(); i++)
        {
            if(this.nextStates.get(i).getPos() == FinalPosition)
                return true;
        }
        return false;
    }


    /**
     * This method will turn the NFA state into a string, thus it will make it easier to debug the code
     * @param pos,symbol 
     * @return void
     */
    public void ToString()
    {
        String result = "";
        result = result + "Node:" + String.valueOf(this.currentStateNumber) + "   ";
        for(int i = 0; i < this.nextStates.size(); i++)
        {
            char symbol = this.nextStates.get(i).getSymbol();
            String Symbol = "";
            if(symbol == '@')
                Symbol = "episilon";
            else
                Symbol += symbol;
            result = result + "[" + "Input:" + Symbol + "->" + "Node:" + this.nextStates.get(i).getPos() + "]" + "   ";
        }
        System.out.println(result);
    }
    
}
