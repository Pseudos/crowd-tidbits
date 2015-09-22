package server.util;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HMAC {
	
	protected Logger getLog() {
		return LoggerFactory.getLogger(getClass());
	}
	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

	public String getHmac(String password, String username) 
		{
	        byte[] message = username.getBytes();
	        byte[] sharedKey = password.getBytes();
	    
			String hmac;
			try {
				SecretKey signingKey = new SecretKeySpec(sharedKey, HMAC_SHA1_ALGORITHM);
			
				Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
				mac.init(signingKey);
				
				byte[] rawHmac = mac.doFinal(message);
				hmac = Base64.encodeBase64String(rawHmac);
				
		        hmac = hmac.replace("/", "_");
		        hmac = hmac.replace("+", "-");
		        hmac = hmac.replace("=", "");
			}
			catch (Exception e) {
				getLog().error("Failed to generate HMAC!",e);
				hmac = "ERROR";
			}
			return hmac;
		}
}
