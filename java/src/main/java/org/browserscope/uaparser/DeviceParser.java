package org.browserscope.uaparser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeviceParser {

    private final Pattern devicePattern;
    private final String deviceReplacement;
    
    public DeviceParser(String pattern, String deviceReplacement) {
        devicePattern = Pattern.compile(pattern);
        this.deviceReplacement = deviceReplacement;
    }
    
    public List<int[]> matchSpans(String userAgent) {
        List<int[]> matchSpans = new ArrayList<int[]>();
        Matcher m = devicePattern.matcher(userAgent);
        while (m.find()) {
            matchSpans.add(new int[] { m.start(), m.end() });
        }
        
        return matchSpans;
    }
    
    public String parse(String userAgent) {
        String device = null;
        Matcher m = devicePattern.matcher(userAgent);
        if (m.find()) {
            if (deviceReplacement != null) {
                device = m.group(1);
                device = device.replaceAll(device, deviceReplacement);
            } else {
                device = m.group(1);
            }
        }
        
        return device;
    }
}
