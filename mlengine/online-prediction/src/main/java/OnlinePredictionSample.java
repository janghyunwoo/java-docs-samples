/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.UriTemplate;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.discovery.Discovery;
import com.google.api.services.discovery.model.JsonSchema;
import com.google.api.services.discovery.model.RestDescription;
import com.google.api.services.discovery.model.RestMethod;
import java.io.File;

/*
 * Sample code for doing Cloud Machine Learning Engine online prediction in Java.
 */

public class OnlinePredictionSample {
  public static void main(String[] args) throws Exception {
	  System.out.println(System.getenv("GOOGLE_APPLICATION_CREDENTIALS"));
	  
    HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
    Discovery discovery = new Discovery.Builder(httpTransport, jsonFactory, null).build();

    RestDescription api = discovery.apis().getRest("ml", "v1").execute();
    RestMethod method = api.getResources().get("projects").getMethods().get("predict");

    JsonSchema param = new JsonSchema();
    String projectId = "hypnotic-maker-206205";
    // You should have already deployed a model and a version.
    // For reference, see https://cloud.google.com/ml-engine/docs/deploying-models.
    String modelId = "census";
    String versionId = "YOUR_VERSION_ID";
    param.set(
//        "name", String.format("projects/%s/models/%s/versions/%s", projectId, modelId, versionId));
    	  "name", String.format("projects/%s/models/%s", projectId, modelId));

    GenericUrl url =
        new GenericUrl(UriTemplate.expand(api.getBaseUrl() + method.getPath(), param, true));
    System.out.println(url);

    String contentType = "application/json";
    File requestBodyFile = new File("input.txt");
    HttpContent content = new FileContent(contentType, requestBodyFile);
    System.out.println(content.getLength());

    
    GoogleCredential credential = GoogleCredential.getApplicationDefault();
    System.out.println(credential);
//    GoogleCredential credential = GoogleCredential.getApplicationDefault(httpTransport, jsonFactory);
    HttpRequestFactory requestFactory = httpTransport.createRequestFactory(credential);
    System.out.println(requestFactory);
    HttpRequest request = requestFactory.buildRequest(method.getHttpMethod(), url, content);
    System.out.println(request);

    String response = request.execute().parseAsString();
    System.out.println(response);
  }
}

