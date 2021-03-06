package net.sf.jabref.net;

import java.net.CookieHandler;
import java.net.URI;
import java.util.*;
import java.io.IOException;


public class CookieHandlerImpl extends CookieHandler {

    
    
    private List<Cookie> cache = new LinkedList<Cookie>();

    

    public void put(
            URI uri,
            Map<String, List<String>> responseHeaders)
            throws IOException {

        List<String> setCookieList =
                responseHeaders.get("Set-Cookie");
        if (setCookieList != null) {
            for (String item : setCookieList) {
                Cookie cookie = new Cookie(uri, item);
                
                
                for (Cookie existingCookie : cache) {
                    if ((cookie.getURI().equals(
                            existingCookie.getURI())) &&
                            (cookie.getName().equals(
                                    existingCookie.getName()))) {
                        cache.remove(existingCookie);
                        break;
                    }
                }
                cache.add(cookie);
            }
        }
    }

    

    public Map<String, List<String>> get(
            URI uri,
            Map<String, List<String>> requestHeaders)
            throws IOException {

        
        
        StringBuilder cookies = new StringBuilder();
        for (Cookie cookie : cache) {
            
            if (cookie.hasExpired()) {
                cache.remove(cookie);
            } else if (cookie.matches(uri)) {
                if (cookies.length() > 0) {
                    cookies.append(", ");
                }
                cookies.append(cookie.toString());
            }
        }

        
        Map<String, List<String>> cookieMap =
                new HashMap<String, List<String>>(requestHeaders);

        
        if (cookies.length() > 0) {
            List<String> list =
                    Collections.singletonList(cookies.toString());
            cookieMap.put("Cookie", list);
        }
        return Collections.unmodifiableMap(cookieMap);
    }
}


