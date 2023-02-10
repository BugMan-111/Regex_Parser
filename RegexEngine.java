import java.util.Scanner;

public class RegexEngine 
{
    public static void main(String[] args) 
    {
        try (// create a new scanner
        Scanner s = new Scanner(System.in);) 
        {
            // create a syntax checker object
            RegexInputChecker r = new RegexInputChecker();

            // create a NFAList12
            NFAStateList l = new NFAStateList();
            
            // normal mode
            // the first input should be the regex
            int counter = 0;
            while(true)
            {
                String currentLine = s.nextLine();
                if(counter == 0 && r.checkRegex(currentLine) == false)
                {
                    System.out.println("Invalid input Regex !!!!");
                    break;
                }

                // print Ready
                if(counter == 0)
                {
                    // create the NFA
                    l.buildNFAList(currentLine);
                    System.out.println("ready");
                }

                // check the input
                if(counter != 0)
                    System.out.println(NFAStateStep.BFS(l, currentLine));

                counter = 1;  
            }
        }
    }
}
