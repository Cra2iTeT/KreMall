package com.kdfus.system;

/**
 * @author Cra2iTeT
 * @date 2022/6/1 0:28
 */
public class RedisConstants {
    public static final String LOGIN_ADMIN_TOKEN_KEY = "Login:Admin:TOKEN:";
    public static final Long LOGIN_ADMIN_TTL = 1440L;
    public static final Long LOGIN_ADMIN_DELAY_TTL = 360L;

    public static final String LOGIN_USER_TOKEN_KEY = "Login:User:TOKEN:";
    public static final String LOGIN_USER_MAPPING_KEY = "Login:User:INFO:";
    public static final Long LOGIN_USER_TOKEN_TTL = 4320L;
    public static final Long LOGIN_USER_INFO_TTL = 4320L;
    public static final Long LOGIN_USER_TOKEN_DELAY_TTL = 360L;

    public static final String USER_ADDRESS_USUAL_KEY = "User:Address:Usual:";
    public static final Long USER_ADDRESS_USUAL_TTL = 4320L;
    public static final Long USER_ADDRESS_USUAL_DELAY_TTL = 360L;

    public static final String USER_ADDRESS_DEFAULT_KEY = "User:Address:Default:";
    public static final Long USER_ADDRESS_DEFAULT_TTL = 4320L;
    public static final Long USER_ADDRESS_DEFAULT_DELAY_TTL = 360L;
}
