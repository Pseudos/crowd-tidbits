package com.example.collen.crowd_tidbits;





import android.widget.Toast;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by COLLEN on 11/21/2015.
 */
public class ConnectionHandler {
    public String myResponse = "";
    public ConnectionHandler(){}

    public String getHmac(String string, String key){
        String hmac = "";
        byte[] message = string.getBytes();
        byte[] sharedKey = key.getBytes();

        try{
            SecretKey signingKey = new SecretKeySpec(sharedKey,"HmacSHA1");

            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);

            byte[] rawHmac = mac.doFinal(message);
            hmac = new String(Base64.encodeBase64(rawHmac));

            hmac = hmac.replace("/","_");
            hmac = hmac.replace("+","-");
            hmac = hmac.replace("=","");

        }catch(Exception ex){
            hmac = "Error";
        }

        return hmac;

    }

    public String register(String usern, String em, String pass) throws Exception{


        System.out.println("Registration Test");
        System.out.println("=================");
        //Get these values from the registration form
        String user = usern;
        String password = pass;
        String email = em;

        //Generate the authcode
        String hmac = getHmac(user, password);
        String server = "http://172.20.10.8:8080/crowdbits/user/register";

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
        final HttpClient client = new DefaultHttpClient();
        final HttpPost post = new HttpPost(server);
        post.addHeader("content-type", "application/json");
        post.addHeader("accepts","application/json");
        post.setEntity(input);

        //Make the call
        final HttpResponse[] responseArr = new HttpResponse[1];
        System.err.println("Out side");
        Thread thread = new Thread() {
            @Override
            public void run() {
                System.err.println("Inside the thread");
                    try {
                        responseArr[0] = client.execute(post);
                        System.err.println(" part 1 : "+ responseArr[0]);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

            }
        };

        thread.start();
        thread.join();

            HttpResponse response = responseArr[0];
        //Check the status of the response
        //202 is good. 200 - user exists. 400 - parameters missing (Should do validation before sending though) Anything else...
        //Throw a hissy fit and let them try again
        //Should also have something for timeout, for no network. Not sure how that would look but probably 404?
        System.out.println(response.getStatusLine().getStatusCode());
        if(response.getStatusLine().getStatusCode() == 202){
            //Handle the response (i.e register success, log them in)
            System.out.println("Created");
            myResponse = "Created";


        }else if(response.getStatusLine().getStatusCode() == 200){
            //Tell them the username/email already exists
            //Get response message to determine which

            InputStream body = response.getEntity().getContent();
            String bodyString = IOUtils.toString(body, "UTF-8");
            IOUtils.closeQuietly(body);

            JSONObject result = new JSONObject(bodyString);
            String message = result.getString("message");
            System.out.println(message);

            if(message.equalsIgnoreCase("Username exist")){

                //Username exists
                //"Username already in use, please choose a different one"
                System.out.println("Username exists");
                myResponse = "Username exists";

            }else{

                //Email exists
                //"Email already in use, you might already have an account"
                System.out.println("Email exists");
                myResponse = "Email exists";
            }

        }
        //Everything else! (400, 404, 500, etc etc)
        else{
            System.out.println("+++Out of cheese error+++");
            myResponse = "+++Out of cheese error+++";
            //Tell them there was an error, and send them back to registration screen.
            //"Whoops something went wrong with the registration. Please check your network and try again. Let us know if the problem persists."
        }
        client.getConnectionManager().shutdown();

        return myResponse;
    }

    public String submit(String msg, String lat, String lon, String us,String em, String ps, String pr) throws Exception
    {
        System.out.println("Post submission");
        System.out.println("===============");
        //Get these values from the registration form
        String text = msg;
        String latitude = lat;
        String longitude = lon;
        String author = em;
        String priority = pr;

        //Should be stored on the phone, or in the application
        String user = us;
        String password = ps;

        //Generate the authcode
        String storedhmac = getHmac(user, password);
        String hmac = getHmac(storedhmac, author);
        String server = "http://172.20.10.8:8080/crowdbits/post/submit";

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
        final HttpClient client = new DefaultHttpClient();
        final HttpPost post = new HttpPost(server);
        post.addHeader("content-type", "application/json");
        post.addHeader("accepts", "application/json");
        post.setEntity(input);

        //Make the call
        final HttpResponse[] responseArr = new HttpResponse[1];
        //responseArr[0] = client.execute(post);

        Thread thread = new Thread() {
            @Override
            public void run() {
                System.err.println("Inside the thread");
                try {
                    responseArr[0] = client.execute(post);
                    System.err.println(" part 1 : "+ responseArr[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };

        thread.start();
        thread.join();

        HttpResponse response = responseArr[0];

        //Check the status of the response
        //200 is good. 401 - Unauthorised. Anything else...
        //Throw a hissy fit and let them try again
        //Should also have something for timeout, for no network. Not sure how that would look but probably 404?
        System.out.println(response.getStatusLine().getStatusCode());
        if (response.getStatusLine().getStatusCode() == 202) {
            //Post was added! Go back to map screen??
            System.out.println("Posted");
            myResponse = "Posted";
        }
        else if (response.getStatusLine().getStatusCode() == 401) {
            //User not authorised! Say so, and let them try again?
            //This shouldn't be able to happen, since they already logged in
            //"Incorrect username and password combination. Try again." ???
            System.out.println("No auth");
            myResponse = "No auth";
        }
        //Everything else! (400, 404, 500, etc etc)
        else {
            //Tell them there was an error, and send them back to registration screen.
            //"Whoops something went wrong while posting. Please check your network and try again. Let us know if the problem persists."
            System.out.println("+++Require frog pills+++");
            myResponse = "+++Require frog pills+++";
        }

        client.getConnectionManager().shutdown();

        return myResponse;
    }

    public String authenticate(String usern, String pass) throws Exception
    {
        System.out.println("Authentication");
        System.out.println("==============");
        //Get these values from the registration form
        String user = usern;
        String password = pass;
        Long stamp = Calendar.getInstance().getTimeInMillis();
        String timestamp = stamp.toString();

        //Generate the authcode
        String storedhmac = getHmac(user, password);
        String hmac = getHmac(storedhmac, timestamp);
        String server = "http://172.20.10.8:8080/crowdbits/user/authenticate";

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
        final HttpClient client = new DefaultHttpClient();
        final HttpPost post = new HttpPost(server);
        post.addHeader("content-type", "application/json");
        post.addHeader("accepts","application/json");
        post.setEntity(input);

        //Make the call
        final HttpResponse[] responseArr = new HttpResponse[1];
        //responseArr[0] = client.execute(post);

        Thread thread = new Thread() {
            @Override
            public void run() {
                System.err.println("Inside the thread");
                try {
                    responseArr[0] = client.execute(post);
                    System.err.println(" part 1 : "+ responseArr[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };

        thread.start();
        thread.join();

        final HttpResponse response = responseArr[0];

        //Check the status of the response
        //200 is good. 401 - Unauthorised. Anything else...
        //Throw a hissy fit and let them try again
        //Should also have something for timeout, for no network. Not sure how that would look but probably 404?
        System.out.println(response.getStatusLine().getStatusCode());
        if (response.getStatusLine().getStatusCode() == 200) {
            //Handle the response (i.e login success, log them in)
            //Handle the response (i.e login success, log them in)


            Thread thread1 = new Thread() {
                @Override
                public void run() {
                    System.err.println("Inside the thread");
                    try {
                        InputStream body = response.getEntity().getContent();
                        String bodyString = IOUtils.toString(body, "UTF-8");
                        IOUtils.closeQuietly(body);

                        JSONObject result = new JSONObject(bodyString);
                        String message = result.getString("message");

                        System.out.println(message);
                        System.out.println("Authenticated-"+message);
                        myResponse = "Authenticated-"+message;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            };

            thread1.start();
            thread1.join();
        }
        else if (response.getStatusLine().getStatusCode() == 401) {
            //User not authorised! Say so, and let them try again?
            //"Incorrect username and password combination. Try again."
            System.out.println("No auth-nothing");
            myResponse = "No auth-nothing";
        }
        //Everything else! (400, 404, 500, etc etc)
        else {
            //Tell them there was an error, and send them back to registration screen.
            //"Whoops something went wrong while logging in. Please check your network and try again. Let us know if the problem persists."
            System.out.println("+++Reinstall universe+++");
            myResponse = "+++Reinstall-universe+++";
        }

        client.getConnectionManager().shutdown();

        return myResponse;
    }


    public List<Post> radius(String lat, String lon, String pri) throws Exception
    {
        System.out.println("Radius search Test");
        System.out.println("==================");

        //Top right and bottom left corners of bounding box
        //Suuplied from application
        String userLat = lat;
        String userLon = lon;
        String priority = pri;
        String distance = "1000";

        List<Post> postsToShow = new ArrayList<Post>();

        //String server = "http://devnode.dev.afrigis.co.za:8080/crowdbits/post/radius";
        String server = "http://172.20.10.8:8080/crowdbits/post/radius";

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
        final HttpClient client = new DefaultHttpClient();
        final HttpPost post = new HttpPost(server);
        post.addHeader("content-type", "application/json");
        post.addHeader("accepts", "application/json");
        post.setEntity(input);

        //Make the call

        final HttpResponse[] responseArr = new HttpResponse[1];
        //responseArr[0] = client.execute(post);

        Thread thread = new Thread() {
            @Override
            public void run() {
                System.err.println("Inside the thread");
                try {
                    responseArr[0] = client.execute(post);
                    System.err.println(" part 1 : "+ responseArr[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };

        thread.start();
        thread.join();

        final HttpResponse response = responseArr[0];


        //Check the status of the response
        //200 is good. Anything else...
        //Throw a hissy fit and maybe allow page refresh
        //Should also have something for timeout, for no network. Not sure how that would look but probably 404?
        System.out.println(response.getStatusLine().getStatusCode());
        if (response.getStatusLine().getStatusCode() == 200) {


            final String[] bodyString = new String[1];

            Thread thread1 = new Thread() {
                @Override
                public void run() {
                    System.err.println("Inside the thread");
                    try {
                        InputStream body = response.getEntity().getContent();
                         bodyString[0] = IOUtils.toString(body, "UTF-8");
                        IOUtils.closeQuietly(body);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            };

            thread1.start();
            thread1.join();

            System.out.println(bodyString[0]);

            JSONObject result = new JSONObject(bodyString[0]);
            //TODO from here!


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

        return postsToShow;
    }

    public class User{
        String username,email,password;
        boolean enabled;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }


    }
    public class Post{
        Long id;
        String description;
        double latitude,longitude;
        User poster;
        int priority;
        Date postTime;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public User getPoster() {
            return poster;
        }

        public void setPoster(User poster) {
            this.poster = poster;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public Date getPostTime() {
            return postTime;
        }

        public void setPostTime(Date postTime) {
            this.postTime = postTime;
        }
    }


}
