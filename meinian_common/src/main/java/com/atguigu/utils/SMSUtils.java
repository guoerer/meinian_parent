package com.atguigu.utils;

import com.atguigu.utils.HttpUtils;
import org.apache.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

/**  https://apigatewaynext.console.aliyun.com/?spm=5176.730006-56956004-56928004-cmapi023305.content.8.404f5a38MVQbc6#/cn-beijing/apps/testApi/58f07d2b4656453b9475dd508660992c/9de0394ed0414b8c88b152d125565089/RELEASE/203905171/-/CloudMarket?_console_base_disable_=TOP~TOOLKIT
 * 山东鼎信（第三方短信发送），查看请求参数和参数类型
 */
public class SMSUtils {

    /**
     * 发送短信
     * @param phoneNumbers 手机号码
     * @param param 4位或6位数字的验证码
     * @throws Exception
     */
    public static void sendShortMessage(String phoneNumbers,String param) throws Exception {
        String host = "http://dingxin.market.alicloudapi.com";
        String path = "/dx/sendSms";
        String method = "POST";
        //自己开通服务的appcode的app秘钥
        String appcode = "e34d685b7bc8459692456d1a04f77153";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", phoneNumbers);
        querys.put("param", "code:"+param);
        querys.put("tpl_id", "TP1711063");  //发送的短信模板类型
        Map<String, String> bodys = new HashMap<String, String>();

        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}