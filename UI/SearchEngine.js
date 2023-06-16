const icon = document.querySelector('.icon');
const search = document.querySelector('.search');
const input = document.querySelector('#letssearch');
const clear = document.querySelector('.clear');
icon.onclick = function(){
	search.classList.toggle('active');}
	document.querySelector('#letssearch').addEventListener('keydown', function(event) {
		// console.log("here1");
      if (event.key === 'Enter') {
		console.log(event.key);
        performSearch(document.querySelector('#letssearch').value);
      }
    });
// Listen for click events on list items and use them as search queries

    //suggestions
    const searchInput = document.getElementById('letssearch');
    const list = document.getElementById('sentences-list');
    // Listen for changes to the search input
    searchInput.addEventListener('input', () => {
      // Get the user's query
      const query = searchInput.value.toLowerCase();
    
      // If the query is empty, clear the search results and return early
      if (query.trim() === ''||document.getElementById('letssearch').value === '') {
        list.innerHTML = '';
        return;
      }    
      fetch('queries.txt')
        .then(response => response.text())
        .then(contents => {
          // Split the contents of the file into a list of sentences
          const sentences = contents.split(/\r?\n/);
    
          // Clear the previous search results
          list.innerHTML = '';
    
          // Iterate over the list of sentences and create a list item element for each sentence that matches the query
          sentences.forEach(sentence => {
            if (sentence.trim().toLowerCase().startsWith(query)) {
              const listItem = document.createElement('li');
              listItem.textContent = sentence.trim();
              list.appendChild(listItem);
            }
          });
        });
    });
    function performSearch(query) {
      // Add your code here to perform a search with the query value
      console.log('Performing search for: ' + query);
// Displaysuggestions(query);

    }
    list.addEventListener('click', (event) => {
      // Check if the clicked element is a list item
      if (event.target.tagName === 'LI') {
        // Set the value of the input field to the text of the clicked element
        input.value = event.target.textContent;
        // Perform the search
        performSearch(input.value);
  list.innerHTML = '';
      }
    });

//voice recongnition
// const serachform=document.querySelector("#search-containerm");
const searchContainer = document.querySelector('.search-container');
const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
if(SpeechRecognition)
{
  console.log("Your browser supports voice recognition");
// serachform.insertAdjacentHTML("afterend",'<button type="button" id="voice-input-btn"><i class="fas fa-microphone"></i></button>');
searchContainer.insertAdjacentHTML('beforeend', '<button id="voice-input-btn"><i class="fas fa-microphone"></i></button>');
const micbtn=searchContainer.querySelector("button");
const micIcon =micbtn.querySelector("i");
const recognition=new SpeechRecognition();
micbtn.addEventListener("click" , micBtnClick);
function micBtnClick(){
if(micIcon.classList.contains("fa-microphone"))
{//start speech recognition
  recognition.start();
}
else{//STOP SPEECH RECOGNITION
recognition.stop();
}

recognition.addEventListener("start",startSpeechRecognition);
function startSpeechRecognition()
{
  micIcon.classList.remove("fa-microphone");
  micIcon.classList.add("fa-microphone-slash");
searchContainer.focus();
console.log("speech recognition active");
}
recognition.addEventListener("end",endSpeechRecognition);
function endSpeechRecognition()
{
  micIcon.classList.remove("fa-microphone-slash");
micIcon.classList.add("fa-microphone");
searchContainer.focus();
console.log("speech recognition disconnected");
}
recognition.addEventListener("result",resultofSpeechRecognition);
function resultofSpeechRecognition(event)
{ 
  const transcript =event.results[0][0].transcript;
console.log(event);
input.value=transcript;
//to submit automatically without need to click enter
setTimeout(()=>{performSearch(transcript);},750);
}
}
}
else 
console.log("Your browser doesn't support voice recognition");




    
    // document.querySelector('#letssearch').addEventListener('input',function (query){
    //   fetch('queries.txt')
    //     .then(response => response.text())
    //     .then(contents => {
    //       // Split the contents of the file into a list of sentences
    //       const sentences = contents.split(/\r?\n/);
    //       console.log("hhh");
      
    //       // Get the unordered list element
    //       const list = document.getElementById('sentences-list');
      
    //       // Iterate over the list of sentences and create a list item element for each sentence
    //       sentences.forEach(sentence => {
    //         if (sentence.trim().startsWith(query)) {
    //           const listItem = document.createElement('li');
    //           listItem.textContent = sentence.trim();
    //           console.log(listItem.textContent);
    //           list.appendChild(listItem);
    //         }
    //       });
    //     });
      
    //   } );
    
// const searchInput = document.querySelector('#letssearch');
// const searchSuggestions = document.querySelector('#search-suggestions');

// searchInput.addEventListener('input', function() {
//   const query = searchInput.value.trim();

//   // Clear previous search suggestions
//   searchSuggestions.innerHTML = '';

//   if (query.length > 0) {
//     // Fetch search suggestions from server
//     fetch('/suggest?q=' + encodeURIComponent(query))
//       .then(response => response.json())
//       .then(suggestions => {
//         // Display search suggestions in dropdown
//         suggestions.forEach(suggestion => {
//           const li = document.createElement('li');
//           li.innerText = suggestion;
//           li.addEventListener('click', function() {
//             searchInput.value = suggestion;
//             searchSuggestions.innerHTML = '';
//           });
//           searchSuggestions.appendChild(li);
//         });
//       });
//   }
// });
