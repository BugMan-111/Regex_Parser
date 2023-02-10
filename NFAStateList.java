/**
* Build the FSM(E-NFA) of regex
* Generate the transition table for the built FSM. 
*/


import java.util.*;


public class NFAStateList{

    // variables
    // assume the first element will always be the starting state
    private ArrayList<NFAState> States = new ArrayList<NFAState>(); 
    private int nextStateNumber = 0;
    private int finalState = -1;
    

    /**
     * Constructor of the NFAStateList
     * @param void
     * @return void
     */
    public NFAStateList()
    {
        // create the initial state and insert it into the list(graph)
        // increment the nextstateNumber
        States.add(new NFAState(nextStateNumber));
        this.nextStateNumber++;
    }


    /**
     * get the NFAStateList as an arrayList 
     * @param void
     * @return ArrayList<NFAState>
     */
    public ArrayList<NFAState> getNFAList()
    {
        return this.States;
    }


     /**
     * get the index of the final state
     * @param void
     * @return int
     */
    public int getFinalState()
    {
        return this.finalState;
    }


    /**
     * Use the verified regular expression to build a NFAList
     * @param String
     * @return NFAStateList
     */
    public void buildNFAList(String regex)
    {
        // initially bracketStarter is set to be -1(the pos of the starting state of a bracket structure)
        // this value will be set to the starting point of '(', if '(' is encountered
        int bracketStarter = -1;

        // keep track of the current node(prt) of this.State
        // 0 = len(array) - 1 = 1 - 1
        int currentNodeIndex = 0;

        // keep track of the nodeIndex of the closing bracket
        int bracketCloser = -1; 

        for(int i = 0; i < regex.length(); i++)
        {
            // get the current character
            // i = current position
            char currentChar = regex.charAt(i);

            // if the current value is a character(lowercase, uppercase, space)
            if(Constant.characters.contains(String.valueOf(currentChar)))
            {
                // create a new NFA node and append it to the list
                this.createAndAppendNewNFANode();

                // check the letter next to the character(upper,lower,space) if there is
                char nextChar = '&';
                if( i + 1 < regex.length())
                    nextChar = regex.charAt(i + 1);

                // get the current node
                NFAState currentNode =  this.States.get(currentNodeIndex);
                
                // if the character next to the current english characters is '+' or '*'
                if(nextChar == '+')
                {
                    // the current node needs to use currentChar to get to the next Node
                    currentNode.addNewLinks(this.States.size() - 1, currentChar);

                    // get the next Node
                    // for the next Node, it can goto itself use currentChar
                    NFAState NextNode =  this.States.get(this.States.size() - 1);
                    NextNode.addNewLinks(this.States.size() - 1, currentChar);
                    
                    // skip the nextChar
                    i++;
                }
                else if(nextChar == '*')
                {
                    // the current node needs to use empty to get to the next Node
                    currentNode.addNewLinks(this.States.size() - 1, '@');

                    // get the next Node
                    // for the next Node, it can goto itself use currentChar
                    NFAState NextNode =  this.States.get(this.States.size() - 1);
                    NextNode.addNewLinks(this.States.size() - 1, currentChar);

                    // skip the nextChar
                    i++;
                }
                else
                {
                    // the current node will need to use currentChar to transit to the new NFAstate
                    currentNode.addNewLinks(this.States.size() - 1, currentChar);
                }

                // move the node prt to the newly created Node(i.e. the top one)
                currentNodeIndex = this.States.size() - 1;
            }
            // if the current value is a openning bracket'('
            else if(currentChar == '(')
            {
                // create a new NFA node and append it to the end of list
                this.createAndAppendNewNFANode();

                // record the node which should be linked to starting position of the bracket
                bracketStarter = this.States.size() - 1;

                // the current node will need to use epsilon(empty string/nothing) to transit to the new NFAstate
                // empty char(epsilon) will be represented by character @ in this program
                NFAState currentNode =  this.States.get(currentNodeIndex);
                currentNode.addNewLinks(this.States.size() - 1, '@');

                currentNodeIndex = this.States.size() - 1;
            }
            // if the current value is a character(lowercase, uppercase, space)
            else if(currentChar == ')')
            {
                NFAState currentNode =  this.States.get(currentNodeIndex);

                // if the final state of the bracket has been created
                if(bracketCloser != -1)
                {
                    // use the current node connect to the end state of the bracket
                    currentNode.addNewLinks(bracketCloser, '@');

                    // change the currentnode to the end state of the bracket
                    currentNodeIndex = bracketCloser;
                }
                else if(bracketCloser == -1)
                {
                    // create a new NFA node and append it to the end of list
                    this.createAndAppendNewNFANode();

                    // connect the current node to the new node
                    currentNode.addNewLinks(this.States.size() - 1, '@');

                     // change the currentnode to the end state of the bracket
                     currentNodeIndex = this.States.size() - 1;   
                }

                // get the current node
                currentNode =  this.States.get(currentNodeIndex);

                // check the letter next to the character(upper,lower,space) if there is
                char nextChar = '&';
                if( i + 1 < regex.length())
                    nextChar = regex.charAt(i + 1);

                // if the character next to the current english characters is '+' or '*'
                if(nextChar == '+')
                {
                   // make the end of bracket state connect to all the nodes(states) that the openning bracket state connects to
                   currentNode.addNewLinksInGroups(this.States.get(bracketStarter).getNextStates());

                   // increment the string
                   i++;
                }
                else if(nextChar == '*')
                {
                   // make the end of bracket state connect to all the nodes(states) that the openning bracket state connects to
                   currentNode.addNewLinksInGroups(this.States.get(bracketStarter).getNextStates());

                   // make the openning bracket state be able to connect to the closing state with only empty symbol
                   this.States.get(bracketStarter).addNewLinks(currentNodeIndex, '@');

                   // increment the string
                   i++;
                }
                // now the bracket has been closed, set the bracketStarter and bracket closer back to -1
                bracketStarter = -1;
                bracketCloser = -1;
            }
            else if(currentChar == '|')
            {
                // if the '|'(alteration) is found inside a bracket: return to the root node of the bracket
                if (bracketStarter != -1)
                {
                    // if the end state of the bracket has not been created
                    if(bracketCloser == -1)
                    {
                        // create the end state of the bracket
                        this.createAndAppendNewNFANode();

                        // remember this position
                        bracketCloser = this.States.size() - 1;

                        // connect the current node to this postion with epsilon(empty, no requirement)
                        // get the current node
                        NFAState currentNode =  this.States.get(currentNodeIndex);
                        currentNode.addNewLinks(bracketCloser, '@');
                    }
                    // if the end state of the bracket has been created
                    else
                    {
                        // connect the current node to the end position
                        // get the current node
                        NFAState currentNode =  this.States.get(currentNodeIndex);
                        currentNode.addNewLinks(bracketCloser, '@');
                    }
                    currentNodeIndex = bracketStarter;
                } 
                // if the '|' is found outside a bracket: return to the root of the list(i.e.the stating state)
                else
                {
                    // if the final state of the has not been created
                    if(this.finalState == -1)
                    {
                        // create the end state of the bracket
                        this.createAndAppendNewNFANode();

                        // remember this position
                        this.finalState = this.States.size() - 1;

                        // connect the current node to this postion with epsilon(empty, no requirement)
                        // get the current node
                        NFAState currentNode =  this.States.get(currentNodeIndex);
                        currentNode.addNewLinks(this.finalState, '@');
                    }
                    // if the end state of the bracket has been created
                    else
                    {
                        // connect the current node to the end position
                        // get the current node
                        NFAState currentNode =  this.States.get(currentNodeIndex);
                        currentNode.addNewLinks(this.finalState, '@');
                    }
                    currentNodeIndex = 0;
                }
            }
            else
                System.out.println("Unexpected character in the checked input string!!!!!");
        }

        // insert the final state
        NFAState currentNode =  this.States.get(currentNodeIndex);
        if(this.finalState == -1)
        {
            //create a new finalstate
            this.createAndAppendNewNFANode();
            this.finalState = this.States.size() - 1;
        }
        currentNode.addNewLinks(this.finalState, '@');
    }


    /**
     * append a new NFAState to the NFAStateList
     * @param void
     * @return void
     */
    private void createAndAppendNewNFANode()
    {
        // create a new NFAState object
        // append the new state to the state list(graph)
        // insert the state number and increment the gloabl state number
        NFAState newNFAState = new NFAState(this.nextStateNumber);
        this.States.add(newNFAState);
        this.nextStateNumber++;
    }


    /**
     * clear the content of the NFAStateList
     * @param void
     * @return void
     */
    public void clearNFAStateList()
    {
        // create the initial state and insert it into the list(graph)
        // increment the nextstateNumber
        this.nextStateNumber = 0;
        this.finalState = -1;
        this.States.clear();
        this.States.add(new NFAState(nextStateNumber));
        this.nextStateNumber++;
    }
    
}
