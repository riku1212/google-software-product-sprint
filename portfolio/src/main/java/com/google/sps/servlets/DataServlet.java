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
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private ArrayList<String> commentsList;

  public void init(){
    commentsList = new ArrayList<String>();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json;");
    response.getWriter().println(convertToJsonUsingGson(commentsList));
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
    commentsList.add(userComment);

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
