package io.github.ohmry.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Curl {
    private String curlUrl;
    private String method;
    private Map<String, String> header;
    private Integer responseCode;
    private boolean isSuccess;

    public Curl(String url) {
        this.curlUrl = url;
        this.method = "GET";
        this.header = new HashMap<>();
        this.responseCode = null;
        this.isSuccess = false;
    }

    public void setMethod(String method) {
        if (!method.equals("GET") && !method.equals("POST") && !method.equals("PUT") && !method.equals("DELETE")) {
            throw new IllegalArgumentException("method는 GET, POST, PUT, DELETE 값만 사용할 수 있습니다.");
        }
        this.method = method;
    }

    public void setHeader(String key, String value) {
        this.header.put(key, value);
    }

    public Integer getResponseCode() {
        return this.responseCode;
    }

    public boolean isSuccess() {
        return this.isSuccess;
    }

    public String fetch() {
        try {
            URL url = new URL(this.curlUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(this.method);

            if (this.header.size() > 0) {
                for (String key : this.header.keySet()) {
                    connection.setRequestProperty(key, this.header.get(key));
                }
            }

            this.responseCode = connection.getResponseCode();
            this.isSuccess = responseCode >= 200 && responseCode < 300;
            BufferedReader reader;

            if (responseCode >= 200 && responseCode < 300) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line.replaceAll("\r\n", "").replaceAll("\\t", ""));
            }

            return stringBuilder.toString();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
