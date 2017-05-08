import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.checkpoint.CheckpointFactory
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testcase.TestCaseFactory
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testdata.TestDataFactory
import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.TestObjectProperty
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords
import com.kms.katalon.core.util.KeywordUtil
import org.openqa.selenium.Keys as Keys
import internal.GlobalVariable as GlobalVariable
import com.kms.katalon.core.testobject.RequestObject as RequestObject
import java.util.List
import java.util.Map
import groovy.json.*
import groovy.json.JsonSlurper as JsonSlurper
import groovy.json.JsonParserType as JsonParserType
import MobileBuiltInKeywords as Mobile
import WSBuiltInKeywords as WS
import WebUiBuiltInKeywords as WebUI
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Random;
public class General {
	

	@Keyword
	def String GetOAuthToken(String clientId) {
		long ts = System.currentTimeMillis() / 1000L
		Random random = new Random();
		int randomNumber = random.nextInt(1100000 - 90000) + 30000;
		def nonce = randomNumber
		def authScheme = "protected_resource"
		// create test object properties as HTTP header name:value
		TestObjectProperty hdr1 = new TestObjectProperty()
		TestObjectProperty hdr2 = new TestObjectProperty()
		hdr1.setName('Content-Type')
		hdr1.setValue('application/json')
		hdr2.setName('Authorization')
		hdr2.setValue('Basic realm=DEVINTEASTBBCATUSER.TRANSACTSP.NET&grant_type=password&username=shib@transactsp.net&password=password.1&timestamp=' + ts + '&nonce=4Ka0Qd&client_id=' + clientId + '')
		// put headers into a list
		List<TestObjectProperty> headers = new ArrayList<TestObjectProperty>()
		headers.add(hdr1)
		headers.add(hdr2)
		
		// build a new HTTP request
		RequestObject req = new RequestObject('oauthToken')
		req.setRestUrl(GlobalVariable.OAuthHost + '/api/auth/oauth2/token?format=json')
		req.setRestRequestMethod('POST')
		req.setHttpHeaderProperties(headers)
		def respRaw = WS.sendRequest(req)
		def resp = respRaw.getResponseText()
		
		// if HTTP request failed, throw an error
		if(respRaw.getStatusCode() != 200) {
			KeywordUtil.markErrorAndStop('Token request has failed.')
			return
		}
		
		// parse JSON response and return auth header
		def jsonSlurper = new JsonSlurper()
		Map parsedResp = jsonSlurper.parseText(resp)
		String token = parsedResp.get("access_token")
		//Build AuthHeader String
		
		
		def AuthHeader = ("Bearer grant_type=${authScheme}&nonce=${nonce}&client_id=${clientId}&access_token=${token}&timestamp=${ts}")
		
		
		return AuthHeader
	}
	
}
