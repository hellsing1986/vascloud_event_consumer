import org.apache.log4j.Logger

class SPSClient {
    final static Logger logger = Logger.getLogger("file")
    def static save(id){
        return "saved saved $id"
    }

    def static postSPI(message){
        
    }

    def static testActiveService(msisdn,groupCode){
        try{
            def reqBody = """<Envelope xmlns="http://schemas.xmlsoap.org/soap/envelope/">
                <Body>
                    <getActiveService xmlns="urn:Webservice">
                        <in0>0</in0>
                        <in1>${msisdn}</in1>
                        <in2>${groupCode}</in2>
                        <in3>20170726080000</in3>
                    </getActiveService>
                </Body>
            </Envelope>"""
            def resBody = postSPI(reqBody)
            //println resBody

            def Envelope = new XmlSlurper().parseText(resBody)
            /*
            <?xml version="1.0" encoding="UTF-8"?>
            <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
                <soapenv:Body>
                    <ns1:getActiveServiceResponse soapenv:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/" xmlns:ns1="urn:Webservice">
                        <getActiveServiceReturn xsi:type="soapenc:string" xmlns:soapenc="http://schemas.xmlsoap.org/soap/encoding/">0|277906360|MI_MAX|MI_MAXINKM1|20170713022406|20170812022405|N|0</getActiveServiceReturn>
                    </ns1:getActiveServiceResponse>
                </soapenv:Body>
            </soapenv:Envelope>
            */

            String msg = Envelope.Body.getActiveServiceResponse.getActiveServiceReturn
            String multiRef = Envelope.Body.multiRef
            return  (multiRef != null && multiRef == "0") || (msg != null && msg == "0")
        }catch (Exception e){
            return false
        }
    }

    def static createSDPService(msisdn,serviceCode,effectiveTime,expireTime){
        try{
            def reqBody="""<Envelope xmlns="http://schemas.xmlsoap.org/soap/envelope/">
                <Body>
                    <createSDPServices xmlns="urn:Webservice">
                        <in0>${msisdn}</in0>
                        <in1>${serviceCode}</in1>
                        <in2>${effectiveTime}</in2>
                        <in3>${expireTime}</in3>
                    </createSDPServices>
                </Body>
            </Envelope>"""

            def resBody = postSPI(reqBody)

            logger.error("sps createSDPService response = ${resBody}")

            def Envelope = new XmlSlurper().parseText(resBody)
            /*<?xml version="1.0" encoding="UTF-8"?>
            <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
                <soapenv:Body>
                    <ns1:createSDPServicesResponse soapenv:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/" xmlns:ns1="urn:Webservice">
                        <createSDPServicesReturn href="#id0"/>
                    </ns1:createSDPServicesResponse>
                    <multiRef id="id0" soapenc:root="0" soapenv:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/" xsi:type="xsd:int" xmlns:soapenc="http://schemas.xmlsoap.org/soap/encoding/">20999</multiRef>
                </soapenv:Body>
            </soapenv:Envelope>*/

            String msg = Envelope.Body.createSDPServicesResponse.createSDPServicesReturn
            String multiRef = Envelope.Body.multiRef
            return msg ?: multiRef
        }catch (Exception e){
            logger.error(e.getMessage(),e)
            return null
        }
    }

    def static deleteSDPService(msisdn,serviceCode,effectiveTime,expireTime){
        try{
            def reqBody="""<Envelope xmlns="http://schemas.xmlsoap.org/soap/envelope/">
                <Body>
                    <deleteSDPServices xmlns="urn:Webservice">
                        <in0>${msisdn}</in0>
                        <in1>${serviceCode}</in1>
                        <in2>${effectiveTime}</in2>
                        <in3>${expireTime}</in3>
                    </deleteSDPServices>
                </Body>
            </Envelope>"""

            def resBody = postSPI(reqBody)
            logger.error("sps deleteSDPService response = ${resBody}")
            def Envelope = new XmlSlurper().parseText(resBody)
            /*<?xml version="1.0" encoding="UTF-8"?>
            <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
                <soapenv:Body>
                    <ns1:deleteSDPServicesResponse soapenv:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/" xmlns:ns1="urn:Webservice">
                        <deleteSDPServicesReturn href="#id0"/>
                    </ns1:deleteSDPServicesResponse>
                    <multiRef id="id0" soapenc:root="0" soapenv:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/" xsi:type="xsd:int" xmlns:soapenc="http://schemas.xmlsoap.org/soap/encoding/">20999</multiRef>
                </soapenv:Body>
            </soapenv:Envelope>*/

            String msg = Envelope.Body.deleteSDPServicesResponse.deleteSDPServicesReturn
            String multiRef = Envelope.Body.multiRef
            return msg ?: multiRef
        }catch (Exception e){
            logger.error(e.getMessage(),e)
            return null
        }
    }
}