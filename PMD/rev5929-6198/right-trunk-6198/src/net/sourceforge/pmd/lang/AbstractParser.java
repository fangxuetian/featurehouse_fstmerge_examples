
package net.sourceforge.pmd.lang;

import java.io.Reader;


public abstract class AbstractParser implements Parser {
    private String excludeMarker;

    public TokenManager getTokenManager(String fileName, Reader source) {
	TokenManager tokenManager = createTokenManager(source);
	tokenManager.setFileName(fileName);
	return tokenManager;
    }

    protected abstract TokenManager createTokenManager(Reader source);

    public String getExcludeMarker() {
	return excludeMarker;
    }

    public void setExcludeMarker(String excludeMarker) {
	this.excludeMarker = excludeMarker;
    }
}
