package backend.helper;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class HttpHelper {

    public static String sendPost(Long id, Double value) throws IOException {

        String result = "";
        HttpPost post = new HttpPost("https://localhost:8080/api/v1/temperature");

        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"id\":\""+id+"\",");
        json.append("\"value\":\""+value+"\"");
        json.append("}");

        // send a JSON data
        post.setEntity(new StringEntity(json.toString()));

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {

            result = EntityUtils.toString(response.getEntity());
        }

        return result;
    }
}
