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
    console.log(timestamp);
    timestamp = new Date(timestamp);
    console.log(timestamp);

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