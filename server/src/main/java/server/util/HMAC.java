package server.util;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Sydney
 *
 */

public class HMAC {
	
	protected static Logger getLog() {
		return LoggerFactory.getLogger(HMAC.class);
	}
	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

	public static String getHmac(String key, String string) 
		{
	        byte[] message = string.getBytes();
	        byte[] sharedKey = key.getBytes();
	    
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
