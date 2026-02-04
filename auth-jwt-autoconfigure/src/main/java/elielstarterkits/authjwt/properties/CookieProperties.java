package elielstarterkits.authjwt.properties;

import jakarta.validation.constraints.NotBlank;

public class CookieProperties {

    @NotBlank
    private String accessName = "ACCESS_TOKEN";

    @NotBlank
    private String refreshName = "REFRESH_TOKEN";

    /**
     * Access cookie path : / (décision)
     */
    @NotBlank
    private String accessPath = "/";

    /**
     * Refresh cookie path : /auth/refresh (décision)
     */
    @NotBlank
    private String refreshPath = "/auth/refresh";

    private boolean httpOnly = true;

    /**
     * En prod: true. En local dev: souvent false.
     * (décision: true par défaut)
     */
    private boolean secure = true;

    /**
     * SameSite par défaut: Strict (décision)
     * Valeurs attendues: Strict, Lax, None
     */
    @NotBlank
    private String sameSite = "Strict";

    /**
     * Domain optionnel. Null => host-only (plus sûr).
     */
    private String domain;

    public String getAccessName() { return accessName; }
    public void setAccessName(String accessName) { this.accessName = accessName; }

    public String getRefreshName() { return refreshName; }
    public void setRefreshName(String refreshName) { this.refreshName = refreshName; }

    public String getAccessPath() { return accessPath; }
    public void setAccessPath(String accessPath) { this.accessPath = accessPath; }

    public String getRefreshPath() { return refreshPath; }
    public void setRefreshPath(String refreshPath) { this.refreshPath = refreshPath; }

    public boolean isHttpOnly() { return httpOnly; }
    public void setHttpOnly(boolean httpOnly) { this.httpOnly = httpOnly; }

    public boolean isSecure() { return secure; }
    public void setSecure(boolean secure) { this.secure = secure; }

    public String getSameSite() { return sameSite; }
    public void setSameSite(String sameSite) { this.sameSite = sameSite; }

    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }
}
