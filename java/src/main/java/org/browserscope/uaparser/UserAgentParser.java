package org.browserscope.uaparser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserAgentParser {

    private final Pattern uaPattern;
    private final String familyReplacement;
    private final String v1Replacement;
    private final String v2Replacement;
    
    public UserAgentParser(String pattern) {
        this(pattern, null);
    }
    
    public UserAgentParser(String pattern, String familyReplacement) {
        this(pattern, familyReplacement, null);
    }
    
    public UserAgentParser(String pattern, String familyReplacement, String v1Replacement) {
        this(pattern, familyReplacement, v1Replacement, null);
    }
    
    public UserAgentParser(String pattern, String familyReplacement, String v1Replacement, String v2Replacement) {
        uaPattern = Pattern.compile(pattern);
        this.familyReplacement = familyReplacement;
        this.v1Replacement = v1Replacement;
        this.v2Replacement = v2Replacement;
    }
    
    public List<int[]> matchSpans(String userAgent) {
        List<int[]> matchSpans = new ArrayList<int[]>();
        Matcher m = uaPattern.matcher(userAgent);
        while (m.find()) {
            matchSpans.add(new int[] { m.start(), m.end() });
        }
        
        return matchSpans;
    }
    
    public String[] parse(String userAgent) {
        String family = null;
        String v1 = null;
        String v2 = null;
        String v3 = null;
        Matcher m = uaPattern.matcher(userAgent);
        if (m.find()) {
            if (familyReplacement != null) {
                family = m.group(1);
                family = family.replaceAll(family, familyReplacement);
            } else {
                family = m.group(1);
            }
            
            if (m.groupCount() >= 2) {
                if (v1Replacement != null) {
                    v1 = m.group(2);
                    v1 = v1.replaceAll(v1, v1Replacement);
                } else {
                    v1 = m.group(2);
                }
                if (m.groupCount() >= 3) {
                    if (v2Replacement != null) {
                        v2 = m.group(3);
                        v2.replaceAll(v2, v2Replacement);
                    } else {
                        v2 = m.group(3);
                    }
                    if (m.groupCount() >= 4) {
                        v3 = m.group(4);
                    }
                }
            }
        }
        
        return new String[] { family, v1, v2, v3 };
    }
    
}
