package org.browserscope.uaparser;

import static org.junit.Assert.*;

import java.util.List;

import org.browserscope.uaparser.UserAgentParser;
import org.junit.Test;

public class UserAgentParserTest {

    @Test
    public void testMatchSpans() {
        UserAgentParser parser = new UserAgentParser("[a-z]+");
        List<int[]> spans = parser.matchSpans("foo ::: bar");
        assertEquals(spans.size(), 2);
        assertEquals(spans.get(0)[0], 0);
        assertEquals(spans.get(0)[1], 3);
        assertEquals(spans.get(1)[0], 8);
        assertEquals(spans.get(1)[1], 11);
    }
    
    
}
