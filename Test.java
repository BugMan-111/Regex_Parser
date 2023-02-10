import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class Test {
    @Test
    public void testSyntaxChecker()
    {
        // test the pre-checking functon of the regex engine
        // create a syntax checker
        RegexInputChecker c = new RegexInputChecker();

        // check the valid inputs
        assertEquals(c.checkRegex("1"), true);
        assertEquals(c.checkRegex("a"), true);
        assertEquals(c.checkRegex("B"), true);
        assertEquals(c.checkRegex(" "), true);
        assertEquals(c.checkRegex("12345678910"), true);
        assertEquals(c.checkRegex("abcdefghijklnm"), true);
        assertEquals(c.checkRegex("abcdefghijklnm "), true);
        assertEquals(c.checkRegex("abcdefghijklnm 12345678910"), true);
        assertEquals(c.checkRegex("abcdefghijklnm 12345678910 ABCDEFADSASDASD"), true);
        assertEquals(c.checkRegex("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ "), true);
       
        // check the syntax

        // check the kleen stars
        assertEquals(c.checkRegex("a"), true);
        assertEquals(c.checkRegex("a+b"), true);
        assertEquals(c.checkRegex("ab+"), true);
        assertEquals(c.checkRegex("a+b+"), true);
        assertEquals(c.checkRegex("+ab"), false);

        assertEquals(c.checkRegex("a"), true);
        assertEquals(c.checkRegex("a*b"), true);
        assertEquals(c.checkRegex("ab*"), true);
        assertEquals(c.checkRegex("a*b*"), true);
        assertEquals(c.checkRegex("*ab"), false);

        assertEquals(c.checkRegex("a"), true);
        assertEquals(c.checkRegex("a+b*"), true);
        assertEquals(c.checkRegex("a*b+"), true);
        assertEquals(c.checkRegex("a*b*"), true);
        assertEquals(c.checkRegex("*ab+"), false);

        assertEquals(c.checkRegex("*"), false);
        assertEquals(c.checkRegex("+"), false);
        assertEquals(c.checkRegex("*+"), false);
        assertEquals(c.checkRegex("+*"), false);


        // check the brackets
        // () will be accepted as valid regex
        assertEquals(c.checkRegex("()"), true);

        // testing the brackets
        assertEquals(c.checkRegex("("), false);
        assertEquals(c.checkRegex(")"), false);
        assertEquals(c.checkRegex("(()"), false);
        assertEquals(c.checkRegex("(("), false);
        assertEquals(c.checkRegex("))"), false);

        // note nested brackets will be seen as unavilable input
        assertEquals(c.checkRegex("(())"), false);
        assertEquals(c.checkRegex("((()))"), false);
        assertEquals(c.checkRegex("(((())))"), false);

        // check the alterantions
        // note regex of only one alteration symbol will be accpeted as a valid regex
        assertEquals(c.checkRegex("|"), true);
        assertEquals(c.checkRegex("|a"), true);
        assertEquals(c.checkRegex("a|"), true);
        assertEquals(c.checkRegex("a|a|"), true);


        // combinations
        // kleens + alteration + characters(upper,lower,number,space)
        assertEquals(c.checkRegex("*|"), false);
        assertEquals(c.checkRegex("|*"), false);
        assertEquals(c.checkRegex("*|*"), false);

        assertEquals(c.checkRegex("+|"), false);
        assertEquals(c.checkRegex("|+"), false);
        assertEquals(c.checkRegex("+|+"), false);

        assertEquals(c.checkRegex("+|*"), false);
        assertEquals(c.checkRegex("*|+"), false);

        assertEquals(c.checkRegex("12abAB*|ok89AD+"), true);
        assertEquals(c.checkRegex("12abAB+|ok89AD*"), true);
        assertEquals(c.checkRegex("1+2+3+4+5+6+|1+2+3+4+5+6"), true);
        assertEquals(c.checkRegex("1*2*3*4*5*6*|1*2*3*4*5*6"), true);
        assertEquals(c.checkRegex("1+2+3+4+5+6*|1+2+3+4+5+6*"), true);
        assertEquals(c.checkRegex("1+2*|1*2+|1*2*|1+2+|1+2+3+a+b*c*d*|e*|f*|g"), true);
        assertEquals(c.checkRegex("1+2*|1*2+|1*2*|1+2+|1+2+3+a+b*c*d|*|e*|f*|g"), false);
        assertEquals(c.checkRegex("1+2*|1*2+|1*2*|1+2+|1+2+3+a+b*c*d*|e|*|f*|g"), false);

        // brackets + kleens + alteration + characters(upper,lower,number,space)
        assertEquals(c.checkRegex("(a*)*"), true);
        assertEquals(c.checkRegex("(a+)+"), true);
        assertEquals(c.checkRegex("(a*)*"), true);
    }

    @Test
    public void testRegexStep()
    {
        // test simple cases
        // the case where the regex only contains character(s)(upper, lower, number, space)
        NFAStateList l = new NFAStateList();
        l.buildNFAList("a");
        assertEquals(NFAStateStep.BFS(l, "a"), true);
        assertEquals(NFAStateStep.BFS(l, ""), false);
        assertEquals(NFAStateStep.BFS(l, " "), false);
        assertEquals(NFAStateStep.BFS(l, "aa"), false);
        l.clearNFAStateList();

        l.buildNFAList("a1b2c3CD ");
        assertEquals(NFAStateStep.BFS(l, "a1b2c3CD "), true);
        assertEquals(NFAStateStep.BFS(l, ""), false);
        assertEquals(NFAStateStep.BFS(l, " "), false);
        assertEquals(NFAStateStep.BFS(l, "A1B2C3cd"), false);
        assertEquals(NFAStateStep.BFS(l, "a1b2c3CD"), false);
        l.clearNFAStateList();

        // special case
        l.buildNFAList(" ");
        assertEquals(NFAStateStep.BFS(l, " "), true);
        assertEquals(NFAStateStep.BFS(l, ""), false);
        assertEquals(NFAStateStep.BFS(l, "  "), false);
        l.clearNFAStateList();


        // the case where the regex contains ccharacter(s)(upper, lower, number, space) and kleen stars
        l.buildNFAList("a+");
        assertEquals(NFAStateStep.BFS(l, "a"), true);
        assertEquals(NFAStateStep.BFS(l, "aaaa"), true);
        assertEquals(NFAStateStep.BFS(l, "aaaaa"), true);
        assertEquals(NFAStateStep.BFS(l, "aaaaaa"), true);
        assertEquals(NFAStateStep.BFS(l, "aaaaaaa"), true);
        assertEquals(NFAStateStep.BFS(l, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"), true);
        assertEquals(NFAStateStep.BFS(l, ""), false);
        assertEquals(NFAStateStep.BFS(l, " "), false);
        assertEquals(NFAStateStep.BFS(l, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab"), false);
        l.clearNFAStateList();

        l.buildNFAList("a*");
        assertEquals(NFAStateStep.BFS(l, "a"), true);
        assertEquals(NFAStateStep.BFS(l, "aaaa"), true);
        assertEquals(NFAStateStep.BFS(l, "aaaaa"), true);
        assertEquals(NFAStateStep.BFS(l, "aaaaaa"), true);
        assertEquals(NFAStateStep.BFS(l, "aaaaaaa"), true);
        assertEquals(NFAStateStep.BFS(l, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"), true);
        assertEquals(NFAStateStep.BFS(l, " "), false);
        assertEquals(NFAStateStep.BFS(l, ""), true);
        assertEquals(NFAStateStep.BFS(l, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab"), false);
        l.clearNFAStateList();

        l.buildNFAList("a+a+a+a+");
        assertEquals(NFAStateStep.BFS(l, "aaaa"), true);
        assertEquals(NFAStateStep.BFS(l, "aaaaa"), true);
        assertEquals(NFAStateStep.BFS(l, "aaaaaa"), true);
        assertEquals(NFAStateStep.BFS(l, "aaaaaaa"), true);
        assertEquals(NFAStateStep.BFS(l, "aaaaaaaa"), true);
        assertEquals(NFAStateStep.BFS(l, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"), true);
        assertEquals(NFAStateStep.BFS(l, " "), false);
        assertEquals(NFAStateStep.BFS(l, ""), false);
        assertEquals(NFAStateStep.BFS(l, "aaaaaaa a"), false);
        assertEquals(NFAStateStep.BFS(l, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab"), false);
        l.clearNFAStateList();

        l.buildNFAList("a*a*a*a*");
        assertEquals(NFAStateStep.BFS(l, "aaaa"), true);
        assertEquals(NFAStateStep.BFS(l, "aaaaa"), true);
        assertEquals(NFAStateStep.BFS(l, "aaaaaa"), true);
        assertEquals(NFAStateStep.BFS(l, "aaaaaaa"), true);
        assertEquals(NFAStateStep.BFS(l, "aaaaaaaa"), true);
        assertEquals(NFAStateStep.BFS(l, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"), true);
        assertEquals(NFAStateStep.BFS(l, " "), false);
        assertEquals(NFAStateStep.BFS(l, ""), true);
        assertEquals(NFAStateStep.BFS(l, "aaaaaaa a"), false);
        assertEquals(NFAStateStep.BFS(l, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab"), false);
        l.clearNFAStateList();

        // the case where the regex contains character(s)(upper, lower, number, space) and alterations
        l.buildNFAList("a|a");
        assertEquals(NFAStateStep.BFS(l, "a"), true);
        assertEquals(NFAStateStep.BFS(l, ""), false);
        assertEquals(NFAStateStep.BFS(l, " "), false);
        l.clearNFAStateList();

        l.buildNFAList("a|1|A| ");
        assertEquals(NFAStateStep.BFS(l, "a"), true);
        assertEquals(NFAStateStep.BFS(l, "1"), true);
        assertEquals(NFAStateStep.BFS(l, "A"), true);
        assertEquals(NFAStateStep.BFS(l, " "), true);
        assertEquals(NFAStateStep.BFS(l, ""), false);
        l.clearNFAStateList();

        l.buildNFAList(" | ");
        assertEquals(NFAStateStep.BFS(l, " "), true);
        l.clearNFAStateList();

        // the case where the regex contains character, alterations and kleen stars
        l.buildNFAList("abc|123|ABC|   ");
        assertEquals(NFAStateStep.BFS(l, "abc"), true);
        assertEquals(NFAStateStep.BFS(l, "123"), true);
        assertEquals(NFAStateStep.BFS(l, "ABC"), true);
        assertEquals(NFAStateStep.BFS(l, "   "), true);
        l.clearNFAStateList();

        l.buildNFAList("abc*|123*|ABC*|   *");
        assertEquals(NFAStateStep.BFS(l, "abc"), true);
        assertEquals(NFAStateStep.BFS(l, "ab"), true);
        assertEquals(NFAStateStep.BFS(l, "abcccc"), true);
        assertEquals(NFAStateStep.BFS(l, "ABC"), true);
        assertEquals(NFAStateStep.BFS(l, "AB"), true);
        assertEquals(NFAStateStep.BFS(l, "ABCCCC"), true);
        assertEquals(NFAStateStep.BFS(l, "123"), true);
        assertEquals(NFAStateStep.BFS(l, "12"), true);
        assertEquals(NFAStateStep.BFS(l, "123333333333"), true);
        assertEquals(NFAStateStep.BFS(l, "ab "), false);
        assertEquals(NFAStateStep.BFS(l, "AB "), false);
        assertEquals(NFAStateStep.BFS(l, "12 "), false);
        l.clearNFAStateList();

        // the case where the regex contains everything
        l.buildNFAList("(1|1|1+)+(1|1|1)+(1|1|1)+");
        assertEquals(NFAStateStep.BFS(l, ""), false);
        assertEquals(NFAStateStep.BFS(l, "1"), false);
        assertEquals(NFAStateStep.BFS(l, "11"), false);
        assertEquals(NFAStateStep.BFS(l, "111"), true);
        assertEquals(NFAStateStep.BFS(l, "1111"), true);
        assertEquals(NFAStateStep.BFS(l, "11112"), false);
        l.clearNFAStateList();
    }   
  }
