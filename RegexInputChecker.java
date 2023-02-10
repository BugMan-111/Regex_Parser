/**
* A regular expression syntax checker which helps the main program 
* checking the input regular expression is valid. 
*/


import java.util.*;


public class RegexInputChecker {

    // default constructor
    public RegexInputChecker(){}


    /**
     * This is the aggregated regular expression language checker
     * which checks whether the input regular expression is valid
     * the function will first check any invalid characters in the regular expression,
     * then it will check the syntax of the regular expression.
     * At the final stage it will check whether parentheses are matched(and check is there any nested brackets) in the given regular expression.
     * @param regex the input regular expression.
     * @return true/false.
     */
    public Boolean checkRegex(String regex)
    {
        if(this.checkInvalidCharacters(regex) == false)
            return false;
        if(this.checkSyntax(regex) == false)
            return false;
        if(this.checkParenthese(regex) == false)
            return false;
        if(this.checkNoNestedBrackets(regex) == false)
            return false;
        return true;
    }


    /**
     * This internal function will check is there any invalid characters in the given regular expression 
     * @param regex the input regular expression.
     * @return true/false.
     */
    private Boolean checkInvalidCharacters(String regex)
    {
        for( int i = 0; i < regex.length(); i++)
        {   
            char current = regex.charAt(i);
            Boolean result = false;
            if(Constant.allSet.contains(String.valueOf(current)))
                result = true;
            if(result == false)
                return result;
        }
        return true;
    }


    /**
     * This internal function will check is there any syntax error in the regular expression
     * @param regex the input regular expression.
     * @return true/false.
     */
    private Boolean checkSyntax(String regex)
    {   
        if(regex.charAt(0) == '*' || regex.charAt(0) == '+')
            return false;
        for(int i = 1; i < regex.length(); i++)
        {
            char current = regex.charAt(i);

            // check the kleens
            if(current == '*' || current == '+')
            {
                // if the first character is a kleen, the regluar expression is not valid
                if(i == 0)
                    return false;

                // always check the previous character, make sure it is a character(not symbol)
                char prev = regex.charAt(i - 1);
                if(!Constant.characters.contains(String.valueOf(prev)) && !(prev == ')') )
                    return false;
            }
        }
        return true;
    }


    /**
     * This internal function will check is there any unmatched brackets in the regular expression
     * @param regex the input regular expression.
     * @return true/false.
    */
    private Boolean checkParenthese(String regex)
    {
        // check whether the regex is bracket free
        if((regex.contains(")") || regex.contains("(")) == false)
            return true;
        Deque<Character> stack = new ArrayDeque<Character>();

        for (int i = 0; i < regex.length(); i++)
        {
            char current = regex.charAt(i);
 
            if (current == '(')
            {
                // Push the element in the stack
                stack.push(current);
                continue;
            }
 
            // If an closing bracket is found, and stack is empty. This suggests the brackets are unmatched
            if (current == ')' && stack.isEmpty())
                return false;

            if (current == ')')
                stack.pop();
        }
 
        // Check Empty Stack
        return (stack.isEmpty());
    }


    /**
     * This internal function will check is there any nested brackets in the regular expression
     * @param regex the input regular expression.
     * @return true/false.
    */
    private Boolean checkNoNestedBrackets(String regex)
    {
        boolean inBracket = false;
        for(int i = 0; i < regex.length() - 1; i++)
        {
            char current = regex.charAt(i);

            if(inBracket == true && current == '(' )
                return false;

            if(current == '(')
                inBracket = true;
            else if(current == ')')
                inBracket = false;
        }

        return true;
    }
}
