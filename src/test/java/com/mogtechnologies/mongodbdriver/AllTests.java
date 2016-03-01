package com.mogtechnologies.mongodbdriver;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        DatabaseTests.class,
        RestApiTests.class,
        IntegrationTests.class
})
public class AllTests {}
/*
    // REST API Tests
    public void testRestAPI(){
        // EntryPoint
        testEntryPoint();
        // Add more APIs here
    }

    // Integration tests | Every test but now without mocks
    public void testIntegration(){
        // testDBIntegration();
        // testRestAPIIntegration();
    }
    // TODO: use mockito to test RESTful API

    public void testEntryPoint() {
        // POST Object | /object/addobject

        // GET Object | /object/{id}
        int id = 0;
        InputStream response = httpGet("http://127.0.0.1:8080/object/" + id);

        if( response == null )
            assertFalse(true);
        else {
            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader(response));
            JsonObject rootObj = root.getAsJsonObject();

            assertEquals(rootObj.get("id").getAsInt(), id);
        }
    }

    private InputStream httpGet(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            return response.getEntity().getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

/*
        try {
            int id = 2;
            String urlString = "http://127.0.0.1:8080/object/" + id;
            URL url = new URL(urlString);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            JsonObject rootObj = root.getAsJsonObject();

            assertEquals(rootObj.get("id").getAsInt(), id);
        } catch (MalformedURLException e) {
            System.out.println("Error building URL.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error opening connection to host.");
            e.printStackTrace();
        }
 */