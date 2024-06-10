# SnapIt
SnapIt is an Android application that allows users to share their stories along with images and GPS-tagged locations. Users can effortlessly write their stories, add photos, and let the app automatically record the location. Others can view these stories on a map, making it simple to connect and explore experiences from around the world.
## Features
### Seamless Login and Registration
The app utilizes custom Material Text Input layouts for login and registration. These inputs enhance security and usability by detecting passwords shorter than 8 characters and incorrectly formatted email addresses.
### Share Stories with Images and Locations
Users can seamlessly upload images with text descriptions using Retrofit. Additionally, by checking the "Post with location" checkbox, they can share their location via GPS, ensuring precise location details are included.
### Secure Data Storage
Upon successful login, the app securely stores your token data and user information in the DataStore. This ensures a smooth and personalized experience each time you use the app.
### Login Checking Preferences
When user has logged in previously and opened the application again, the user does not need to enter email & password again to enter the main page. This makes it easier for users to access content faster.
### Map Marker Integration
SnapIt integrates the Google Maps API, allowing users to open a map within the app that displays markers at the locations where stories have been shared. This feature enables users to visually explore and navigate through various stories based on their geographic locations.
### Support English & Indonesia Language
Users can choose to use English or Indonesian using settings for localization. This creates a user friendly impression for those who want to use other languages.
### Widget Stack
Users can also add special widgets to view other people's stories with easier and simpler access without needing to open the application.
### Infinite Scrolling
The app employs Paging 3 and Remote Mediator to offer an infinite scrolling experience. Data is stored in a Room database, ensuring smooth and efficient access to stories even when offline.
## Screenshots
