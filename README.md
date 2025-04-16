Summarized tasks
The application you will build must:

Include Main screen with a list of clickable asteroids as seen in the provided design using a RecyclerView with its adapter. You could insert some fake manually created asteroids to try this before downloading any data.
Include a Details screen that displays the selected asteroid data once it’s clicked in the Main screen as seen in the provided design. The images in the details screen are going to be provided with the starter code: an image for a potentially hazardous asteroid and another one for the non-hazardous ones, you have to display the correct image depending on the isPotentiallyHazardous asteroid parameter. Navigation xml file is already included with starter code.
Download and parse the data from NASA NeoWS (Near Earth Object Web Service) API. As this response cannot be parsed directly with Moshi, we are providing a method to parse the data “manually” for you, it’s called parseAsteroidsJsonResult inside NetworkUtils class, we recommend trying for yourself before using this method or at least take a close look at it as it is an extremely common problem in real-world apps. For this response we need retrofit-converter-scalars instead of Moshi, you can check this dependency in build.gradle (app) file.
When asteroids are downloaded, save them in the local database.
Fetch and display the asteroids from the database and only fetch the asteroids from today onwards, ignoring asteroids before today. Also, display the asteroids sorted by date (Check SQLite documentation to get sorted data using a query).
Be able to cache the data of the asteroid by using a worker, so it downloads and saves today's asteroids in background once a day when the device is charging and wifi is enabled.
Download Picture of Day JSON, parse it using Moshi and display it at the top of Main screen using Picasso Library. (You can find Picasso documentation here: https://square.github.io/picasso/(opens in a new tab)) You could use Glide if you are more comfortable with it, although be careful as we found some problems displaying NASA images with Glide.
Add content description to the views: Picture of the day (Use the title dynamically for this), details images and dialog button. Check if it works correctly with talk back.
Make sure the entire app works without an internet connection.
Bonus Tasks
The following are bonus tasks to challenge yourself, although they are not going to be taken into account for grading your project, you can implement them to learn more:

Modify the app to support multiple languages, device sizes, and orientations.
Make the app delete asteroids from the previous day once a day using the same workManager that downloads the asteroids.
Match the styles for the details screen subtitles and values to make it consistent, and make it look like what is in the designs.



Project: Build an Asteroid Radar App
RecyclerView
Criteria	Submission Requirements
Create and reuse views in an Android app using RecyclerView

The app displays a list of asteroids in the Main Screen by using a RecyclerView, when tapping an item the app opens Details screen.

Network
Criteria	Submission Requirements
Build an application that connects to an internet server to retrieve and display live data

The asteroids displayed in the screens are downloaded from the NASA API

Use networking and image best practices to fetch data and images

The NASA image of the day is displayed in the Main Screen

Database and Caching
Criteria	Submission Requirements
Create a database to store and access user data over time

The app can save the downloaded asteroids in the database and then display them also from the database. The app filters asteroids from the past

Implement offline caching to allow users to interact with online content offline

The app downloads the next 7 days asteroids and saves them in the database once a day using workManager with requirements of internet connection and device plugged in. The app can display saved asteroids from the database even if internet connection is not available

Accessibility
Criteria	Submission Requirements
Add talkback and push-button navigation to make an Android app accessible

The app works correctly in talk back mode, it provides descriptions for all the texts and images: Asteroid images in details screen and image of the day. It also provides description for the details screen help button

Suggestions to Make Your Project Stand Out
Modify the app to support multiple languages, device sizes and orientations.
Make the app delete asteroids before today once a day from the same workManager that downloads the asteroids.
Provide styles for the details screen subtitles and values to make it consistent, and make it look like in the designs.