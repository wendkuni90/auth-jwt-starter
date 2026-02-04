package elielstarterkits.authjwt.token;

import elielstarterkits.authjwt.properties.JwtProperties;

public final class TestJwtPropsFactory {

    private TestJwtPropsFactory() {}

    public static JwtProperties defaultProps() {
        JwtProperties props = new JwtProperties();
        props.setSecret("super-secret-key-super-secret-key-super-secret");
        props.setIssuer("auth-jwt-starter");
        props.setAudience("api");
        return props;
    }
}
