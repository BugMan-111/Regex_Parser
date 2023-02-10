/**
* A data structure providing utilities for the main program
*/

public class Tuple {
    private int pos = -1;
    private char symbol = '*';
    
    public Tuple(int pos, char symbol)
    {
        this.pos = pos;
        this.symbol = symbol;
    }

    public int getPos()
    {
        return this.pos;
    }

    public char getSymbol()
    {
        return symbol;
    }
}
