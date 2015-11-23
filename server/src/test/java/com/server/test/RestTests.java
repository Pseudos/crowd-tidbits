package com.server.test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;

import server.entity.Post;
import server.entity.User;

public class RestTests {
    public static String getHmac(String string, String key) 
    {
        byte[] message = string.getBytes();
        byte[] sharedKey = key.getBytes();
    
        String hmac;
        try {
            SecretKey signingKey = new SecretKeySpec(sharedKey, "HmacSHA1");
        
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            
            byte[] rawHmac = mac.doFinal(message);
            hmac = Base64.encodeBase64String(rawHmac);
            
            hmac = hmac.replace("/", "_");
            hmac = hmac.replace("+", "-");
            hmac = hmac.replace("=", "");
        }
        catch (Exception e) {
            hmac = "ERROR";
        }
        return hmac;
    }
        
    /*
    / Registration:
    / JSON {"username":"Pseudos","email":"cidwick@gmail.com","password":"KNSktX00n_cYAim0dk6cC6QZXAw"}
    / Server http://devnode.dev.afrigis.co.za:8080/crowdbits/user/register
    / Accepts application/json
    / Content-type application/json
    / Method POST
    */
        /*<dependency>
            <groupId>org.apache.httpcomponents</groupId>
            <artifactId>httpclient</artifactId>
            <version>4.3.5</version>
        </dependency>
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.2.4</version>
        </dependency>
        <dependency>
            <groupId>commons-io</groupId>
            <artifactId>commons-io</artifactId>
            <version>2.4</version>
        </dependency>*/
    @Test
    public void register() throws Exception
    {
        System.out.println("Registration Test");
        System.out.println("=================");
        //Get these values from the registration form
        String user = "Sydney";
        String password = "blabla";
        String email = "foo@something.bar";
        
        //Generate the authcode
        String hmac = getHmac(user, password);
        //String server = "http://devnode.dev.afrigis.co.za:8080/crowdbits/user/register";
        String server = "http://127.0.0.1:8080/crowdbits/user/register";
        //String server = "http://172.20.10.6:8080/crowdbits/user/register";
    
        //Build parameter json string
        StringBuilder sb = new StringBuilder();
        sb.append("{\"username\":\"").append(user).append("\",");
        sb.append("\"email\":\"").append(email).append("\",");
        sb.append("\"password\":\"").append(hmac).append("\"}");
        String jsonString = sb.toString();
        //And put it in an entity for the request
        StringEntity input = new StringEntity(jsonString);
        input.setContentType("application/json");
        
        //Create http connection, set headers + entity
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(server);
        post.addHeader("content-type", "application/json");
        post.addHeader("accepts","application/json");
        post.setEntity(input);
        
        //Make the call
        HttpResponse response;
        response = client.execute(post);
        
        //Check the status of the response
        //202 is good. 200 - user exists. 400 - parameters missing (Should do validation before sending though) Anything else... 
        //Throw a hissy fit and let them try again
        //Should also have something for timeout, for no network. Not sure how that would look but probably 404?
        System.out.println(response.getStatusLine().getStatusCode());
        if (response.getStatusLine().getStatusCode() == 202) {
            //Handle the response (i.e register success, log them in)
            System.out.println("Created");
        }
        else if (response.getStatusLine().getStatusCode() == 200) {
            //Tell them the username/email already exists
            //Get response message to determine which
            
            InputStream body = response.getEntity().getContent();
            String bodyString = IOUtils.toString(body, "UTF-8"); 
            IOUtils.closeQuietly(body);
            
            JSONObject result = new JSONObject(bodyString);
            String message = result.getString("message");
            System.out.println(message);
            
            if(message.equalsIgnoreCase("Username exists"))
            {
                //Username exists
                //"Username already in use, please choose a different one"
                System.out.println("Username exists");
            }
            else
            {
                //Email exists
                //"Email already in use, you might already have an account"
                System.out.println("Email exists");
            }
        }
        //Everything else! (400, 404, 500, etc etc)
        else {
            System.out.println("+++Out of cheese error+++");
            //Tell them there was an error, and send them back to registration screen.
            //"Whoops something went wrong with the registration. Please check your network and try again. Let us know if the problem persists."
        }
        
        client.getConnectionManager().shutdown();
    }
    
    /*
    / Authentication:
    / JSON {"authHash":"Ff-jCcLzZ9ihpd9-NPX8K9y_QnA","username":"pseudos","timestamp":"100"}
    / Hash of registration hash, timestamp as key
    / Server http://devnode.dev.afrigis.co.za:8080/crowdbits/user/authenticate
    / Accepts application/json
    / Content-type application/json
    / Method POST
    */
    @Test
    public void authenticate() throws Exception
    {
        System.out.println("Authentication");
        System.out.println("==============");
        //Get these values from the registration form
        String user = "Sydney";
        String password = "blabla";
        Long stamp = Calendar.getInstance().getTimeInMillis();
        String timestamp = stamp.toString();
        
        //Generate the authcode
        String storedhmac = getHmac(user, password);
        String hmac = getHmac(storedhmac, timestamp);
        //String server = "http://devnode.dev.afrigis.co.za:8080/crowdbits/user/authenticate";
        String server = "http://127.0.0.1:8080/crowdbits/user/authenticate";
    
        //Build parameter json string
        StringBuilder sb = new StringBuilder();
        sb.append("{\"username\":\"").append(user).append("\",");
        sb.append("\"timestamp\":\"").append(timestamp).append("\",");
        sb.append("\"authHash\":\"").append(hmac).append("\"}");
        String jsonString = sb.toString();
        //And put it in an entity for the request
        StringEntity input = new StringEntity(jsonString);
        input.setContentType("application/json");
        
        //Create http connection, set headers + entity
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(server);
        post.addHeader("content-type", "application/json");
        post.addHeader("accepts","application/json");
        post.setEntity(input);
        
        //Make the call
        HttpResponse response;
        response = client.execute(post);
        
        //Check the status of the response
        //200 is good. 401 - Unauthorised. Anything else...
        //Throw a hissy fit and let them try again
        //Should also have something for timeout, for no network. Not sure how that would look but probably 404?
        System.out.println(response.getStatusLine().getStatusCode());
        if (response.getStatusLine().getStatusCode() == 200) {
            //Handle the response (i.e login success, log them in)
            InputStream body = response.getEntity().getContent();
            String bodyString = IOUtils.toString(body, "UTF-8"); 
            IOUtils.closeQuietly(body);
            
            JSONObject result = new JSONObject(bodyString);
            String message = result.getString("message");
            System.out.println(message);
            
        }
        else if (response.getStatusLine().getStatusCode() == 401) {
            //User not authorised! Say so, and let them try again?
            //"Incorrect username and password combination. Try again."
            System.out.println("No auth");
        }
        //Everything else! (400, 404, 500, etc etc)
        else {
            //Tell them there was an error, and send them back to registration screen.
            //"Whoops something went wrong while logging in. Please check your network and try again. Let us know if the problem persists."
            System.out.println("+++Reinstall universe+++");
        }
        
        client.getConnectionManager().shutdown();
    }
    
    /*
    / Submit post:
    / JSON {'description':'post text','latitude':0.0,'longitude':0.0,'poster':'userEmail','priority':0/1/2,'hash':'base64 SHA-1 HMAC'}
    / Hash of password (hash) generated with key - email
    / Server http://devnode.dev.afrigis.co.za:8080/crowdbits/post/submit
    / Accepts application/json
    / Content-type application/json
    / Method POST
    */
    @Test
    public void submit() throws Exception
    {
        System.out.println("Post submission");
        System.out.println("===============");
        //Get these values from the registration form
        String text = "post text";
        String latitude = "-25.126";
        String longitude = "28.123";
        String author = "foo@something.bar";
        String priority = "1";
        
        //Should be stored on the phone, or in the application
        String user = "Sydney";
        String password = "blabla";
        
        //Generate the authcode
        String storedhmac = getHmac(user, password);
        String hmac = getHmac(storedhmac, author);
        
        //String server = "http://devnode.dev.afrigis.co.za:8080/crowdbits/post/submit";
        String server = "http://127.0.0.1:8080/crowdbits/post/submit";
    
        //Build parameter json string
        StringBuilder sb = new StringBuilder();
        sb.append("{\"description\":\"").append(text).append("\",");
        sb.append("\"latitude\":\"").append(latitude).append("\",");
        sb.append("\"longitude\":\"").append(longitude).append("\",");
        sb.append("\"poster\":\"").append(author).append("\",");
        sb.append("\"priority\":\"").append(priority).append("\",");
        sb.append("\"hash\":\"").append(hmac).append("\"}");
        String jsonString = sb.toString();
        //And put it in an entity for the request
        StringEntity input = new StringEntity(jsonString);
        input.setContentType("application/json");
        
        //Create http connection, set headers + entity
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(server);
        post.addHeader("content-type", "application/json");
        post.addHeader("accepts","application/json");
        post.setEntity(input);
        
        //Make the call
        HttpResponse response;
        response = client.execute(post);
        
        //Check the status of the response
        //200 is good. 401 - Unauthorised. Anything else...
        //Throw a hissy fit and let them try again
        //Should also have something for timeout, for no network. Not sure how that would look but probably 404?
        System.out.println(response.getStatusLine().getStatusCode());
        if (response.getStatusLine().getStatusCode() == 202) {
            //Post was added! Go back to map screen??
            System.out.println("Posted");
        }
        else if (response.getStatusLine().getStatusCode() == 401) {
            //User not authorised! Say so, and let them try again?
            //This shouldn't be able to happen, since they already logged in
            //"Incorrect username and password combination. Try again." ???
            System.out.println("No auth");
        }
        //Everything else! (400, 404, 500, etc etc)
        else {
            //Tell them there was an error, and send them back to registration screen.
            //"Whoops something went wrong while posting. Please check your network and try again. Let us know if the problem persists."
            System.out.println("+++Require frog pills+++");
        }
        
        client.getConnectionManager().shutdown();
    }
    
    /*
    / Bounding box:
    / JSON {'trLat':0.0,'trLon':0.0,'blLat':0.0,'blLon':0.0,'priority':'1'}
    / Server http://devnode.dev.afrigis.co.za:8080/crowdbits/post/box
    / Accepts application/json
    / Content-type application/json
    / Method POST
    */
    @Test
    public void box() throws Exception
    {
        System.out.println("Bounding box Test");
        System.out.println("=================");
        
        //Top right and bottom left corners of bounding box
        //Suuplied from application
        String trLat = "-25.120";
        String trLon = "28.780";
        String blLat = "-25.150";
        String blLon = "28.100";
        String priority = "0";
        
        //String server = "http://devnode.dev.afrigis.co.za:8080/crowdbits/post/box";
        String server = "http://127.0.0.1:8080/crowdbits/post/box";
    
        //Build parameter json string
        StringBuilder sb = new StringBuilder();
        sb.append("{\"trLat\":\"").append(trLat).append("\",");
        sb.append("\"trLon\":\"").append(trLon).append("\",");
        sb.append("\"blLat\":\"").append(blLat).append("\",");
        sb.append("\"blLon\":\"").append(blLon).append("\",");
        sb.append("\"priority\":\"").append(priority).append("\"}");
        String jsonString = sb.toString();
        //And put it in an entity for the request
        StringEntity input = new StringEntity(jsonString);
        input.setContentType("application/json");
        
        //Create http connection, set headers + entity
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(server);
        post.addHeader("content-type", "application/json");
        post.addHeader("accepts","application/json");
        post.setEntity(input);
        
        //Make the call
        HttpResponse response;
        response = client.execute(post);
        
        //Check the status of the response
        //200 is good. Anything else... 
        //Throw a hissy fit and maybe allow page refresh
        //Should also have something for timeout, for no network. Not sure how that would look but probably 404?
        System.out.println(response.getStatusLine().getStatusCode());
        if (response.getStatusLine().getStatusCode() == 200) {
                        
            InputStream body = response.getEntity().getContent();
            String bodyString = IOUtils.toString(body, "UTF-8"); 
            IOUtils.closeQuietly(body);
            
            System.out.println(bodyString);
            
            JSONObject result = new JSONObject(bodyString);
            //TODO from here!
            
            List<Post> postsToShow = new ArrayList<Post>();
            JSONArray posts = result.getJSONArray("posts");
            
            for(int i=0; i<posts.length(); i++)
            {
                JSONObject postJson = (JSONObject) posts.get(i);
                JSONObject userJson = postJson.getJSONObject("poster");
                Post postObj = new Post();
                User userObj = new User();
                
                userObj.setEmail(userJson.getString("email"));
                userObj.setUsername(userJson.getString("username"));
                
                postObj.setDescription(postJson.getString("description"));
                postObj.setId(postJson.getLong("id"));
                postObj.setLatitude(postJson.getDouble("latitude"));
                postObj.setLongitude(postJson.getDouble("longitude"));
                postObj.setPriority(postJson.getInt("priority"));
                postObj.setPostTime(new Date(postJson.getLong("postTime")));
                postObj.setPoster(userObj);
                
                postsToShow.add(postObj);
            }
            
            for(Post pst : postsToShow)
            {
                System.out.println(pst.getDescription() + " lat: " + pst.getLatitude() + " lon: " + pst.getLongitude());
                System.out.println("Author: " + pst.getPoster().getEmail() + " priority: " + pst.getPriority() + " time:" + pst.getPostTime().toString());
            }
        }
        //Everything else! (400, 404, 500, etc etc)
        else {
            System.out.println("+++Out of cheese error+++");
            //Tell them there was an error, and send them back to registration screen.
            //"Whoops something went wrong with the registration. Please check your network and try again. Let us know if the problem persists."
        }
        
        client.getConnectionManager().shutdown();
    }
    
    /*
    / Radius:
    / JSON {'lat':0.0,'lon':0.0,'distance':0.0,'priority','1'}
    / Server http://devnode.dev.afrigis.co.za:8080/crowdbits/post/box
    / Accepts application/json
    / Content-type application/json
    / Method POST
    */
    @Test
    public void radius() throws Exception
    {
        System.out.println("Radius search Test");
        System.out.println("==================");
        
        //Top right and bottom left corners of bounding box
        //Suuplied from application
        String userLat = "-25.126";
        String userLon = "28.123";
        String priority = "1";
        String distance = "100";
        
        //String server = "http://devnode.dev.afrigis.co.za:8080/crowdbits/post/radius";
        String server = "http://127.0.0.1:8080/crowdbits/post/radius";
    
        //Build parameter json string
        StringBuilder sb = new StringBuilder();
        sb.append("{\"lat\":\"").append(userLat).append("\",");
        sb.append("\"lon\":\"").append(userLon).append("\",");
        sb.append("\"distance\":\"").append(distance).append("\",");
        sb.append("\"priority\":\"").append(priority).append("\"}");
        String jsonString = sb.toString();
        //And put it in an entity for the request
        StringEntity input = new StringEntity(jsonString);
        input.setContentType("application/json");
        
        //Create http connection, set headers + entity
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(server);
        post.addHeader("content-type", "application/json");
        post.addHeader("accepts","application/json");
        post.setEntity(input);
        
        //Make the call
        HttpResponse response;
        response = client.execute(post);
        
        //Check the status of the response
        //200 is good. Anything else... 
        //Throw a hissy fit and maybe allow page refresh
        //Should also have something for timeout, for no network. Not sure how that would look but probably 404?
        System.out.println(response.getStatusLine().getStatusCode());
        if (response.getStatusLine().getStatusCode() == 200) {
                        
            InputStream body = response.getEntity().getContent();
            String bodyString = IOUtils.toString(body, "UTF-8"); 
            IOUtils.closeQuietly(body);
            
            System.out.println(bodyString);
            
            JSONObject result = new JSONObject(bodyString);
            //TODO from here!
            
            List<Post> postsToShow = new ArrayList<Post>();
            JSONArray posts = result.getJSONArray("posts");
            
            for(int i=0; i<posts.length(); i++)
            {
                JSONObject postJson = (JSONObject) posts.get(i);
                JSONObject userJson = postJson.getJSONObject("poster");
                Post postObj = new Post();
                User userObj = new User();
                
                userObj.setEmail(userJson.getString("email"));
                userObj.setUsername(userJson.getString("username"));
                
                postObj.setDescription(postJson.getString("description"));
                postObj.setId(postJson.getLong("id"));
                postObj.setLatitude(postJson.getDouble("latitude"));
                postObj.setLongitude(postJson.getDouble("longitude"));
                postObj.setPriority(postJson.getInt("priority"));
                postObj.setPostTime(new Date(postJson.getLong("postTime")));
                postObj.setPoster(userObj);
                
                postsToShow.add(postObj);
            }
            
            for(Post pst : postsToShow)
            {
                System.out.println(pst.getDescription() + " lat: " + pst.getLatitude() + " lon: " + pst.getLongitude());
                System.out.println("Author: " + pst.getPoster().getEmail() + " priority: " + pst.getPriority() + " time:" + pst.getPostTime().toString());
            }
        }
        //Everything else! (400, 404, 500, etc etc)
        else {
            System.out.println("+++Out of cheese error+++");
            //Tell them there was an error, and send them back to registration screen.
            //"Whoops something went wrong with the registration. Please check your network and try again. Let us know if the problem persists."
        }
        
        client.getConnectionManager().shutdown();
    }
}
