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

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['Hello world!', '¡Hola Mundo!', '你好，世界！', 'Bonjour le monde!'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

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

//adds hello message using fetch
function getHelloMessage() {
    fetch('/data').then(response => response.text()).then((message) => {
        document.getElementById('message-container').innerHTML = message;
  });
}
