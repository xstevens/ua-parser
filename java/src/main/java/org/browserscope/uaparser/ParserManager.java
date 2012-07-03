package org.browserscope.uaparser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class ParserManager {
    
    private List<UserAgentParser> uaParsers;
    private List<OSParser> osParsers;
    private List<DeviceParser> deviceParsers;
    private List<String> mobileUserAgentFamilies;
    private List<String> mobileOSFamilies;
    
    @SuppressWarnings("unchecked")
    public ParserManager(String yamlPath) throws IOException {
        System.out.println(yamlPath);
        LinkedHashMap<String,List<Object>> yamlMap = new LinkedHashMap<String,List<Object>>();
        Yaml yaml = new Yaml();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(yamlPath), "UTF-8"));
            yamlMap = (LinkedHashMap<String,List<Object>>)yaml.load(reader);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        
        // Setup User-Agent parsers
        uaParsers = new ArrayList<UserAgentParser>(); 
        List<Object> userAgentRegexes = (List<Object>)yamlMap.get("user_agent_parsers");
        for (Object o : userAgentRegexes) {
            Map<String,String> uaRegex = (Map<String,String>)o;
            String re = uaRegex.get("regex");
            String familyReplacement = uaRegex.get("family_replacement");
            String v1Replacement = uaRegex.get("v1_replacement");
            String v2Replacement = uaRegex.get("v2_replacement");
            UserAgentParser uap = new UserAgentParser(re, familyReplacement, v1Replacement, v2Replacement);
            uaParsers.add(uap);
        }
        
        // Setup OS parsers
        osParsers = new ArrayList<OSParser>();    
        List<Object> osRegexes = yamlMap.get("os_parsers");
        for (Object o : osRegexes) {
            Map<String,String> osRegex = (Map<String,String>)o;
            String re = osRegex.get("regex");
            String osReplacement = osRegex.get("family_replacement");
            OSParser op = new OSParser(re, osReplacement);
            osParsers.add(op);
        }
        
        // Setup device parsers
        deviceParsers = new ArrayList<DeviceParser>();    
        List<Object> deviceRegexes = yamlMap.get("device_parsers");
        for (Object o : deviceRegexes) {
            Map<String,String> deviceRegex = (Map<String,String>)o;
            String re = deviceRegex.get("regex");
            String deviceReplacement = deviceRegex.get("device_replacement");
            DeviceParser dp = new DeviceParser(re, deviceReplacement);
            deviceParsers.add(dp);
        }
        
        // TODO: Is there a way to get Java to just cast this as List<String> without complaining
        mobileUserAgentFamilies = new ArrayList<String>();
        List<Object> muaFamilyObjs = yamlMap.get("mobile_user_agent_families");
        for (Object o : muaFamilyObjs) {
            mobileUserAgentFamilies.add((String)o);
        }
        
        // TODO: Is there a way to get Java to just cast this as List<String> without complaining
        mobileOSFamilies = new ArrayList<String>();
        List<Object> mosFamilyObjs = yamlMap.get("mobile_os_families");
        for (Object o : mosFamilyObjs) {
            mobileOSFamilies.add((String)o);
        }
    }
    
    private Map<String,String> parseUserAgent(String userAgent) {
        Map<String,String> result = new HashMap<String,String>();
        for (UserAgentParser parser : uaParsers) {
            String[] parserResult = parser.parse(userAgent);
            if (parserResult.length > 0 && parserResult[0] != null && parserResult[0].length() > 0) {
                result.put("family", parserResult[0]);
                result.put("major", parserResult[1]);
                result.put("minor", parserResult[2]);
                result.put("patch", parserResult[3]);
                break;
            }
        }
        
        if (!result.containsKey("family")) {
            result.put("family", "Other");
        }
        
        return result;
    }
    
    private Map<String,String> parseOS(String userAgent) {
        Map<String,String> result = new HashMap<String,String>();
        for (OSParser parser : osParsers) {
            String[] parserResult = parser.parse(userAgent);
            if (parserResult.length > 0 && parserResult[0] != null && parserResult[0].length() > 0) {
                result.put("os", parserResult[0]);
                result.put("major", parserResult[1]);
                result.put("minor", parserResult[2]);
                result.put("patch", parserResult[3]);
                result.put("patch_minor", parserResult[4]);
                break;
            }
        }
        
        if (!result.containsKey("os")) {
            result.put("os", "Other");
        }
        
        return result;
    }
    
    private Map<String,String> parseDevice(String userAgent) {
        Map<String,String> result = new HashMap<String,String>();
        for (DeviceParser parser : deviceParsers) {
            String parserResult = parser.parse(userAgent);
            if (parserResult != null && parserResult.length() > 0) {
                result.put("device", parserResult);
                break;
            }
        }
        
        if (!result.containsKey("device")) {
            result.put("device", "Other");
        }
        
        return result;
    }
    
    public Map<String, Map<String,String>> parse(String userAgent) {
        Map<String, Map<String,String>> result = new HashMap<String, Map<String,String>>();
        result.put("user_agent", parseUserAgent(userAgent));
        result.put("os", parseOS(userAgent));
        result.put("device", parseDevice(userAgent));
        
        return result;
    }
}
