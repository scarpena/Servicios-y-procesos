package org.example;

import com.google.gson.Gson;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class Main {

    public interface ApiService {
        @POST("/post")
        Call<ResponseBody> post(@Body HashMap<String, Object> params);
    }

    public static void main(String[] args) {
        Gson gson = new Gson();
        RequestBody student  = new RequestBody("Sergio", 35);

        ResponseBody response = post1(student);
        System.out.println("HttpURLConnection: \n" + response.getArgs());

        ResponseBody response2 = postRetrofit(student);
        assert response2 != null;
        System.out.println("Retrofit: \n" + response2.getArgs());


    }

    public static ResponseBody post1(RequestBody requestBody){
        HttpURLConnection conn = null;
        HashMap<String, Object> args =new HashMap<>();

        try {
            String jsonInputString = new  Gson().toJson(requestBody);

            URL url = new URL("https://httpbin.org/post");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream());
            os.write(jsonInputString);
            os.flush();
            os.close();
            String statusCode = String.valueOf(conn.getResponseCode());
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            System.out.println("Código de respuesta: " + statusCode);
            args = new HashMap<>();
            args.put("args", new Gson().fromJson(jsonResponse.getJSONObject("args").toString(), HashMap.class));
            args.put("data", jsonResponse.getString("data"));
            args.put("headers", new Gson().fromJson(jsonResponse.getJSONObject("headers").toString(), HashMap.class));
            args.put("origin", jsonResponse.getString("origin"));

            return new ResponseBody(args);

        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
        finally {
            if (conn != null)
                conn.disconnect();
        }
        return new ResponseBody(args);
    }

    public static ResponseBody postRetrofit(RequestBody requestBody){
        try {
            String url = "https://httpbin.org/post/";

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiService service = retrofit.create(ApiService.class);

            HashMap<String, Object> params = new HashMap<>();
            params.put("nombre", requestBody.getNombre());
            params.put("edad", requestBody.getEdad());

            Call<ResponseBody> call = service.post(params);
            Response<ResponseBody> response = call.execute();

            HashMap<String, Object> args = new HashMap<>();
            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();
                assert responseBody != null;
                args.put("args", responseBody.getArgs());
                args.put("data", responseBody.getData());
                args.put("headers", responseBody.getHeaders());
                args.put("origin", responseBody.getOrigin());
            }

            return new ResponseBody(args);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}