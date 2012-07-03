package org.browserscope.uaparser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OSParser {

    private final Pattern osPattern;
    private final String osReplacement;
    private final String v1Replacement;
    private final String v2Replacement;
    
    public OSParser(String pattern) {
        this(pattern, null);
    }
    
    public OSParser(String pattern, String osReplacement) {
        this(pattern, osReplacement, null);
    }
    
    public OSParser(String pattern, String osReplacement, String v1Replacement) {
        this(pattern, osReplacement, v1Replacement, null);
    }
    
    public OSParser(String pattern, String osReplacement, String v1Replacement, String v2Replacement) {
        osPattern = Pattern.compile(pattern);
        this.osReplacement = osReplacement;
        this.v1Replacement = v1Replacement;
        this.v2Replacement = v2Replacement;
    }
    
    public List<int[]> matchSpans(String userAgent) {
        List<int[]> matchSpans = new ArrayList<int[]>();
        Matcher m = osPattern.matcher(userAgent);
        while (m.find()) {
            matchSpans.add(new int[] { m.start(), m.end() });
        }
        
        return matchSpans;
    }
    
    public String[] parse(String userAgent) {
        String os = null;
        String v1 = null;
        String v2 = null;
        String v3 = null;
        String v4 = null;
        Matcher m = osPattern.matcher(userAgent);
        if (m.find()) {
            if (osReplacement != null) {
                os = m.group(1);
                os = os.replaceAll(os, osReplacement);
            } else {
                os = m.group(1);
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
                        v2 = v2.replaceAll(v2, v2Replacement);
                    } else {
                        v2 = m.group(3);
                    }
                    if (m.groupCount() >= 4) {
                        v3 = m.group(4);
                        if (m.groupCount() >= 5) {
                            v4 = m.group(5);
                        }
                    }
                }
            }
        }
        
        return new String[] { os, v1, v2, v3, v4 };
    }
    
}
