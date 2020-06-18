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

// loads aboutme.html page
function displayAbout() {
    const title = document.getElementById('content-title');
    title.innerText = "Here is some background information about me:";
    $(function(){
        $("#content-panel").load("pages/aboutme.html"); 
    });
}

// loads hobbies.html page
function displayHobbies() {
    const title = document.getElementById('content-title');
    title.innerText = "This is a list of my main hobbies:";
    $(function(){
        $("#content-panel").load("pages/hobbies.html"); 
    });
}

// deactivate previous button and activate clicked button
function activateListItem() {
    $(document).ready(function() { 
            $('li').click(function() { 
                $('li.list-group-item.active').removeClass("active"); 
                $(this).addClass("active"); 
            }); 
        });
}

// fetch comments 
function getComments() {
    fetch('/data').then(response => response.json()).then(commentsList => {
        const commentContainer = document.getElementById('comment-container');
        commentsList.forEach(comment => {
            commentContainer.append(
                createListElement(
                    "[" + convertISOtoString(comment.timestamp) + "] " + 
                    comment.name + ": " + 
                    comment.comment
                )
            );
        });
    });
}

// function to create comment form
function createCommentForm() {
    fetch('/login').then(response => response.json()).then(loginDetails => {
        const commentFormContainer = document.getElementById('comment-form');

        console.log(loginDetails);
        
        // display button to log in
        if (!loginDetails.loggedIn) {
            // create login button
            const form = document.createElement('form');
            form.setAttribute('action', loginDetails.loginUrl);

            const label = document.createElement('label');
            label.setAttribute('for', 'user-login');
            label.innerText = "Please log in to submit your comments.";

            const input = document.createElement('input');
            input.setAttribute('type', 'submit');
            input.setAttribute('value', "Log In");
            input.setAttribute('name', 'user-login');

            form.append(label, document.createElement('br'), input);
            commentFormContainer.append(form);
        }

        // display comment form
        else {
            const form = document.createElement('form');
            form.setAttribute('action', '/data');
            form.setAttribute('method', 'POST');

            // create textarea
            const label = document.createElement('label');
            label.setAttribute('for', 'user-comment');
            label.innerText = "Leave your comments here: ";

            const textarea = document.createElement('textarea');
            textarea.setAttribute('name', 'user-comment');

            const input = document.createElement('input');
            input.setAttribute('type', 'submit');

            form.append(
                label, document.createElement('br'), 
                textarea, document.createElement('br'), 
                input
            );

            // create logout button
            const logoutForm = document.createElement('form');
            logoutForm.setAttribute('action', loginDetails.logoutUrl);

            const logOutButton = document.createElement('input');
            logOutButton.setAttribute('type', 'submit');
            logOutButton.setAttribute('value', "Log Out");
            logOutButton.setAttribute('name', 'user-logout');

            logoutForm.append(logOutButton);
            commentFormContainer.append(form, logoutForm);
        } 
    });
}

// make list element from text
function createListElement(text){
    const listElement = document.createElement('li');
    listElement.innerText = text;
    listElement.setAttribute('class', 'list-group-item');
    listElement.setAttribute('onclick', 'activateListItem()')
    return listElement;
}

//convert ISO timezone to yyyy-mm-dd hh:mm
function convertISOtoString(timestamp) {
    timestamp = new Date(timestamp);

    year = "" + timestamp.getFullYear();
    month = padZero(timestamp.getMonth() + 1);
    day = padZero(timestamp.getDate());
    hour = padZero(timestamp.getHours());
    minute = padZero(timestamp.getMinutes());

    return year + "-" + month + "-" + day + " " + hour + ":" + minute;
}

// utility function to pad zero in front of month, day, hour, minute
function padZero(text) {
    text = "" + text;
    if (text.length < 2) {
        text = "0" + text;
    }
    return text;
}