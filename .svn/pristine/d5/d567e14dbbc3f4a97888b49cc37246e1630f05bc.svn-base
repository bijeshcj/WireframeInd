package com.verizontelematics.indrivemobile.connection.utils;

/**
 * @author bijesh
 */
public class Constants {

    private static Constants mConstants;

    private Constants() {
    }

    public static Constants getInstance() {
        if(mConstants == null)
            mConstants = new Constants();
        return mConstants;
    }

    public static final String PROTOCOL = "https://";
    public static final String GATEWAY_SERVICES = "/HTIWebGateway/EnterpriseGatewayServices";
    public static final String AUTHENTICATE_SERVICE = "/VWUS_AuthorizationServiceV5_0/Authenticate";
    public static final String PAIRING_SERVICE = "/MobileDeviceRegisterPairingServiceV5_0/Registration";
    public static final String GET_CERT_ID = "/SecurityServiceV5_0/GetCertID";
    public static final String CERT_SIGN_REQ = "/VWUS_AuthorizationServiceV5_0/ValidateRegistration";
    public static final String GET_REG_CODE = "/outputRegistrationCodes?accountid=";
    public static final String APP_JSON = "application/json";
    public static final String API_KEY = "258418e2062469a73bf179805e13b98f";//"582c33ebcbebb02d9bc77558c9e7b025";//"c704ab35e57b1568b4b0a24f94a06a5a";
    public static final String AUTHORIZATION = "Authorization";
    public static final String AUTHORIZATION_DATA = "{\"Data\": {\"Type\": \"primary\", \"SourceName\": \"MAPP\" } }";
    public static final String TOKEN = "token";
    public static final String CONTEXT = "context";
    public static final String SOURCE_NAME = "SourceName"; // "MAPP"
    public static final String TRANSACTION_ID = "TransactionId"; // "SXM-JMDev"
    public static final String TIME_STAMP = "Timestamp"; // "2014-12-18T14:54:31.502Z"
    public static final String ORG = "Organization"; // "VW"
    public static final String REGION = "Region";  // "US"
    public static final String APP_NAME = "ApplicationName";  // "CWP"
    public static final String DATA = "Data";
    public static final String USER_ID = "userId";
    public static final String EMAIL = "email"; // "testuser@yahoo.com"
    public static final String PHONE = "phone";
    public static final String REG_CODE = "registrationCode";
    public static final String DEVICE_NAME = "deviceName";
    public static final String CSR = "csr";

    private String port;

    public String getHost() {
//        return host;
        return "api-sit.vtitel.com";
    }

    public void setHost(String host) {
        String host1 = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
