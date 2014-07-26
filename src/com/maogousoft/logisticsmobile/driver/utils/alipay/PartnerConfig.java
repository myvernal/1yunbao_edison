package com.maogousoft.logisticsmobile.driver.utils.alipay;

public class PartnerConfig {

    /** 合作商户ID。用签约支付宝账号登录ms.alipay.com后，在账户信息页面获取。 */
    public static final String PARTNER = "2088901784003103";

    /** 商户收款的支付宝账号 */
    public static final String SELLER = "2589350190@qq.com";

    /** 商户（RSA）私钥 */
    public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAL4YX+Ttl3g85BfXT4PUwAH6f4hXZurT6FKRVW7td6noR+aPV+RDJvq2gSwrLQDi8R/+i6PBf5GCb+gH1l6cgoYnxXWLg1zdr17Y84v0lMMf47lkz6pQjBXoKU9Vk8KxPXXcjm9UA1TMpKg3wQPMXH24ADUqYaE03WUV3g8YeSBTAgMBAAECgYBHHE8jEE4NSJn64Sx5oGiDVPXIRwnU5piN6pgO+v48rQvYj97NIOG//+2qXCxlnH+e/FW4WHYkNKswHV5v3hRk/TuwCCvH8/Yvkjiqt18pZmU5KE1a9AWywf+pPeMXjCtkAVjPPBW5LT/44ErJ27Aicl95vhl+D9WgZzNOPJlq4QJBAPNiL9oVbSMuXPdm7Lw/yfwkQVTJUw/bbgIBAWXg1oFbTq0Mju+OsqtG58e/+BGv8BEdoBBQknvHJZPQxyUtFicCQQDH8wbVL3MCkwO28VuuBC9BawR5C/ITHRmYsXBWZ46XWvzoc0sPZ5ac5+ZxuFiOwR97V5M/X795Wekp4Z4pbsv1AkBr/m1tP4Slz7TAspLpFQTzNMModAy/RIaTrQ6JvjJwQ6utbZ0e/xqYlWXTZIMWlhLYBWU42AenlL4/0KwM+uyRAkAK6o/C79Y9m70mLcMtmvjhf3A9E5Phy0LsfTN+5CR6yvLn1fORIbytUFPafZAIKy8G0t8tJJMVkv0R1kmGRZBJAkEA7eK42qrameRjKYBvmZZu2bmYEUfGlaz0unKbzOitYf8zOcuDs4sN1dLWiyMcrTDCffb7nVi2SBenow3M4MuFfA==";

    /** 支付宝（RSA）公钥 用签约支付宝账号登录ms.alipay.com后，在密钥管理页面获取。 */
    public static final String RSA_ALIPAY_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCMleSXrJCqxWujA++s6f3ybs7OI7lnPxM5kIs2mObXZxNOH/HhKw1Omkfo5DvfyhFLN8DkWgZTrLnTNtgLlXlc698xkYPUosUdauqhYOv67q97Ev3jM3+vyq7MpbBJ2pogUsr8t8OBiSNmwgKtnU5IvW/fQkPv0LzVWD4TaNbtxwIDAQAB";

    /** 支付宝安全支付服务apk的名称，必须与assets目录下的apk名称一致 */
    public static final String ALIPAY_PLUGIN_NAME = "alipay_plugin_20120428msp.apk";

    public final static String SERVER_URL = "http://www.1yunbao.com/pay/alipayCallBack";

}
