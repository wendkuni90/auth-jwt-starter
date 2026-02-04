package elielstarterkits.authjwt.properties;

public class BlacklistProperties {

    /**
     * Blacklist activée par défaut (décision)
     */
    private boolean enabled = true;

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
