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
import com.google.gson.GsonBuilder;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.sps.data.Login; 
import com.google.sps.servlets.JsonUtility;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    // returns empty string if user is logged in and the login url if user is not logged in  
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        String loginUrl=null, logoutUrl=null;
        String userEmail=null;
        Boolean loggedIn=userService.isUserLoggedIn();

        // user not logged in, return login link
        if (!loggedIn) {
            loginUrl = userService.createLoginURL("/");
        }

        // user logged in, return logout link
        else {
            logoutUrl = userService.createLogoutURL("/");
            userEmail = userService.getCurrentUser().getEmail();
        }

        Login loginDetails = new Login(loggedIn, loginUrl, logoutUrl, userEmail);

        response.setContentType("application/json");
        response.getWriter().println(JsonUtility.toJsonDisableHtmlEscaping(loginDetails));
        return;
    }
}