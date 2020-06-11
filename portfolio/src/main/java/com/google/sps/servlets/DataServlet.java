// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.gson.Gson;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import java.util.ArrayList;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("userComment");
    
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    ArrayList<String> commentList = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
        String comment = (String) entity.getProperty("comment");
        commentList.add(comment);
    }

    response.setContentType("application/json;");
    response.getWriter().println(convertToJsonUsingGson(commentList));
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String userComment = request.getParameter("user-comment");

    // check whether userComment is empty or null
    if (checkEmpty(userComment)){
        response.setContentType("text/html");
        response.getWriter().println("Please input your comment.");
        return;
    }

    Entity commentEntity = new Entity("userComment");
    commentEntity.setProperty("comment", userComment);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);

    // redirect back 
    response.sendRedirect("/index.html");
  }

  // check whether user text input is valid or not
  // return True if empty and False otherwise
  private Boolean checkEmpty(String textInput) {
    if (textInput == null || textInput.trim().isEmpty()) {
        return true;
    }
    return false;
  }

  // convert to JSON using GSON
  private String convertToJsonUsingGson(ArrayList<String> commentsList) {
    Gson gson = new Gson();
    String json = gson.toJson(commentsList);
    return json;
  }
}
