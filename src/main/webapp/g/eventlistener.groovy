import com.google.common.io.CharStreams
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse

ServletRequest req = request
ServletResponse res = response
def requestBody = CharStreams.toString req.getReader()
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
def ACCESSGW = new XmlSlurper().parseText requestBody
def retBody =
"""<ACCESSGW>
    <MODULE>SDP NOTIFIER</MODULE>
    <MESSAGE_TYPE>RESPONSE</MESSAGE_TYPE>
    <COMMAND>
        <error_id>0</error_id>
        <error_desc>successfully</error_desc>
        <queueID>${ACCESSGW.COMMAND.queueID}</queueID>
        <msisdn>${ACCESSGW.COMMAND.msisdn}</msisdn>
        <service_id>${ACCESSGW.COMMAND.service_id}</service_id>
        <package_id>${ACCESSGW.COMMAND.package_id}</package_id>
    </COMMAND>
</ACCESSGW>"""

res.setContentType "application/xml; charset=utf-8"

println retBody