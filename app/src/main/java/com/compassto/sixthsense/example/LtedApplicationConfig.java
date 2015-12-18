package com.compassto.sixthsense.example;


public class LtedApplicationConfig {

    public static String LTED_APP_ID = "com.hugleberry.hugproximitysdk";

    public static String ENS_SERVER_ADDRESS = "publicExpr.dynamicMngd";
    public static String ENS_PROJECT_ADDRESS = "CompassToMwcV2";

    public static String LTED_DISCOVERY_TASK = "discovery";

    public static String API_KEY = "L8WUsI1Zsw3sCJqPPWH7KSfD_01xvWzOrTkLnjScJPI";

    public static String getEnsProjectAddress() {
        return ENS_SERVER_ADDRESS + "." + ENS_PROJECT_ADDRESS;
    }
}