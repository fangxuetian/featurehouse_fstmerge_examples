
package net.sf.jabref;

import java.util.Enumeration;
import java.util.Map;
import java.util.Vector;
import java.util.regex.PatternSyntaxException;

public class SearchRuleSet implements SearchRule {
    protected Vector<SearchRule> ruleSet = new Vector<SearchRule>();

    public void addRule(SearchRule newRule) {
        ruleSet.add(newRule);
    }

    public void clearRules() {
        ruleSet.clear();
    }

    public int applyRule(Map<String, String> searchString, BibtexEntry bibtexEntry) throws PatternSyntaxException{
        int score = 0;
        Enumeration<SearchRule> e = ruleSet.elements();
        while (e.hasMoreElements()) {
            score += e.nextElement().applyRule(searchString,
                    bibtexEntry);
        }
        return score;
    }
}
