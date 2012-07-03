package org.browserscope.uaparser;

import java.io.IOException;
import java.util.Map;

import org.browserscope.uaparser.ParserManager;
import org.junit.Before;
import org.junit.Test;

public class ParserManagerTest {

    private String yamlPath;
    
    @Before
    public void setUp() {
        yamlPath = System.getProperty("basedir") + "/../" + "regexes.yaml";
    }
    
    @Test
    public void testConstructor() {
        String yamlPath = System.getProperty("basedir") + "/../" + "regexes.yaml";
        try {
            ParserManager pm = new ParserManager(yamlPath);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private void prettyPrintResult(Map<String,Map<String,String>> parsedMap) {
        StringBuilder prettyResultBuilder = new StringBuilder();
        Map<String,String> uaMap = parsedMap.get("user_agent");
        prettyResultBuilder.append(uaMap.get("family"));
        if (uaMap.get("major") != null) {
            prettyResultBuilder.append(" ").append(uaMap.get("major"));
            if (uaMap.get("minor") != null) {
                prettyResultBuilder.append(".").append(uaMap.get("minor"));
                if (uaMap.get("patch") != null) {
                    // TODO: check if patch is number or alphanumeric
                    prettyResultBuilder.append(".").append(uaMap.get("patch"));
                }
            }
        }

        Map<String,String> osMap = parsedMap.get("os");
        prettyResultBuilder.append(" os => ").append(osMap.get("os"));
        if (osMap.get("major") != null) {
            prettyResultBuilder.append(" ").append(osMap.get("major"));
            if (osMap.get("minor") != null) {
                prettyResultBuilder.append(".").append(osMap.get("minor"));
                if (osMap.get("patch") != null) {
                    prettyResultBuilder.append(".").append(osMap.get("patch"));
                    if (osMap.get("patch_minor") != null) {
                        prettyResultBuilder.append(".").append(osMap.get("patch_minor"));
                    }
                }
            }
        }
        
        Map<String,String> deviceMap = parsedMap.get("device");
        prettyResultBuilder.append(" device => ").append(deviceMap.get("device"));
        
        System.out.println(prettyResultBuilder.toString());
    }
    
    private void printResult(Map<String,Map<String,String>> parsedMap) {
        Map<String,String> uaMap = parsedMap.get("user_agent");
        for (Map.Entry<String, String> uaEntry : uaMap.entrySet()) {
            System.out.println(uaEntry.getKey() + " => " + uaEntry.getValue());
        }
        Map<String,String> osMap = parsedMap.get("os");
        for (Map.Entry<String, String> osEntry : osMap.entrySet()) {
            System.out.println(osEntry.getKey() + " => " + osEntry.getValue());
        }
        Map<String,String> deviceMap = parsedMap.get("device");
        for (Map.Entry<String, String> deviceEntry : deviceMap.entrySet()) {
            System.out.println(deviceEntry.getKey() + " => " + deviceEntry.getValue());
        }
    }
    
    @Test
    public void testParse() {
        String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:16.0) Gecko/16.0 Firefox/16.0a1";
        userAgent = "Mozilla/5.0 (X11; Linux x86_64; rv:15.0) Gecko/20120608 Firefox/15.0a2 Iceweasel/15.0a2";
        userAgent = "Mozilla/5.0 (Windows NT 6.1; de;rv:12.0) Gecko/20120403211507 Firefox/12.0";
        userAgent = "Mozilla/5.0 (Android; Linux armv7l; rv:9.0) Gecko/20111216 Firefox/9.0 Fennec/9.0";
        userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_3) AppleWebKit/534.55.3 (KHTML, like Gecko) Version/5.1.3 Safari/534.53.10";
        //userAgent = "Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543a Safari/419.3";
        try {
            ParserManager pm = new ParserManager(yamlPath);
            Map<String,Map<String,String>> parsedMap = pm.parse(userAgent);
            prettyPrintResult(parsedMap);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
