import com.google.common.io.CharStreams
import org.apache.log4j.Logger
import SPSClient
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse

Logger logger = Logger.getLogger("file")

ServletRequest req = request
ServletResponse res = response
def requestBody = req.inputStream.text.replaceAll("[\n\r]", "")//CharStreams.toString req.getReader()

logger.info("requestBody = ${requestBody}")

/*
<ACCESSGW>
 <MODULE>SDP</MODULE>
 <MESSAGE_TYPE>NOTIFY</MESSAGE_TYPE>
 <COMMAND>
    <queueID>3765026</queueID>
    <resultCode>0</resultCode>
    <errorDesc/>
    <startTime>1492740550032</startTime>
    <startTimeCP>1492740550032</startTimeCP>
    <cpURL>http://10.33.68.30:7001/sdp_sms/receiveMO</cpURL>
    <regID>13105600</regID>
    <msisdn>84912555755</msisdn>
    <regType>1</regType>
    <channel>SMS/WAP</channel>
    <service_id>1000161</service_id>
    <package_id>1000241</package_id>
    <originalprice>1000</originalprice>
    <price>1000</price>
    <commandcode>Y VT03</commandcode>
    <serviceCode>VTRUYEN</serviceCode>
    <packageCode>VT03</packageCode>
    <subpackageCode>VT03</subpackageCode>
    <autoRenew>1</autoRenew>
    <subcribeTime>20170421091106</subcribeTime>
    <expiredTime>20170422000000</expiredTime>
    <updateTime/>
 </COMMAND>
</ACCESSGW>
*/
//println requestBody
def ACCESSGW = new XmlSlurper().parseText requestBody

//def test = SPSClient.testActiveService(ACCESSGW.COMMAND.msisdn,"MI")//SPSClient.save 123
def error_id = 1
def error_desc = "unknown reqType (valid value = 0|1)"
try{
    if(ACCESSGW.COMMAND.regType == "1"){
        error_desc = SPSClient.createSDPService(
                ACCESSGW.COMMAND.msisdn,
                ACCESSGW.COMMAND.serviceCode,
                ACCESSGW.COMMAND.subcribeTime,
                ACCESSGW.COMMAND.expiredTime
        )
        logger.info("msisdn=${ACCESSGW.COMMAND.msisdn} SPSClient.createSDPService(${ACCESSGW.COMMAND.msisdn},${ACCESSGW.COMMAND.serviceCode},${ACCESSGW.COMMAND.subcribeTime},${ACCESSGW.COMMAND.expiredTime}) = ${error_desc}")
        if (error_desc.startsWith("0|")) error_id = 0
    }else if(ACCESSGW.COMMAND.regType == "2"){
        error_desc = SPSClient.deleteSDPService(
                ACCESSGW.COMMAND.msisdn,
                ACCESSGW.COMMAND.serviceCode,
                ACCESSGW.COMMAND.subcribeTime,
                ACCESSGW.COMMAND.expiredTime
        )
        logger.info("msisdn=${ACCESSGW.COMMAND.msisdn} SPSClient.deleteSDPService(${ACCESSGW.COMMAND.msisdn},${ACCESSGW.COMMAND.serviceCode},${ACCESSGW.COMMAND.subcribeTime},${ACCESSGW.COMMAND.expiredTime}) = ${error_desc}")
        if (error_desc.startsWith("0|")) error_id = 0
    }
}catch (Exception e){
    error_desc = e.getMessage()
    logger.error(error_desc, e)
}
def retBody =
"""<ACCESSGW>
    <MODULE>SDP NOTIFIER</MODULE>
    <MESSAGE_TYPE>RESPONSE</MESSAGE_TYPE>
    <COMMAND>
        <error_id>${error_id}</error_id>
        <error_desc>${error_desc}</error_desc>
        <queueID>${ACCESSGW.COMMAND.queueID}</queueID>
        <msisdn>${ACCESSGW.COMMAND.msisdn}</msisdn>
        <service_id>${ACCESSGW.COMMAND.service_id}</service_id>
        <package_id>${ACCESSGW.COMMAND.package_id}</package_id>
    </COMMAND>
</ACCESSGW>""".replaceAll("[\n\r]", "")
logger.info("msisdn=${ACCESSGW.COMMAND.msisdn} error_id=${error_id}, error_desc=${error_desc} = ${retBody}")
/* *
def post = new URL("https://httpbin.org/post").openConnection();
def message = '{"message":"this is a message"}'
post.setRequestMethod("POST")
post.setDoOutput(true)
post.setRequestProperty("Content-Type", "application/json")
post.getOutputStream().write(message.getBytes("UTF-8"));
def postRC = post.getResponseCode();
println(postRC);
if(postRC.equals(200)) {
    println(post.getInputStream().getText());
}
* */

res.contentType = "application/xml; charset=utf-8"

println retBody