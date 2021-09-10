# keeper_app
Android app for NFC receipts

## Try keeper_app on our website
Website: http://csclub.uwaterloo.ca/~t4shu/website_frontend/index.html

HT6 Demo video: https://youtu.be/DoTj7_8motA

## Keeper was chosen as a HT6 Finalist!!!
![Screenshot](Screenshot%202021-08-22%20181121.jpg)

---
## Project Overview
Keeper is a mobile application that digitizes the collection of receipts onto a comprehensive cloud and local platform,  making smart receipts a reality. A simple installation of Keeper would enable users to manage their receipts in an efficient manner, back it up safely on the cloud, while simultaneously contributing to the elimination of environmental concerns associated with paper receipt production. Businesses save on the cost of buying receipt rolls, and increase customer service (no more jammed printers!). Plus, no need to worry about losing your receipts for returns or warranties anymore - it’s backed up on the cloud.

## Inspiration
We all make purchases on a daily basis, whether it be on food, tech, or movie tickets for a Friday evening. In any of these transactions, receipts play a vital role in confirming purchases, tracking spendings, and processing returns and warranties. Furthermore, receipts waste time. Organizing receipts for tax returns and reimbursements is a time-consuming and arduous task that can be automated.
However, the use of such an obvious part of our daily purchases has a less obvious impact on the environment, generating at least 302 million pounds of waste and over 4 billion pounds of CO2 in just the United States alone (ClimateAction, 2019). It is evident that this outdated process has to be revisited and revamped. 

Plus, one of our members has a receipt hoarding problem 👀.

## What it does
1. Users enable NFC on their phone and tap on the store’s machine equipped with a NFC chip. 
2. The receipt data of the purchased item is transferred as a JSON file via NFC, even if the user doesn’t 3. have the app. They can always download the app to view and upload/retrieve the receipt later.
4. Users can view their NFC receipt (stored locally) immediately on the Keeper app OR
5. Users can upload their receipt to the cloud for backup, and retrieve their old receipts from the cloud.
6. Users can view past receipts individually and see individual items and prices.

## Features to be added
1. Users will be able to search for individual items (not yet implemented).
2. Users will be able to sort by merchant, amount paid, and date (date already implemented)
3. UserID tagging system to tie user IDs to receipts to prevent duplication.
4. Machine learning to analyze and categorize purchases into types

## How we built it
We wireframed the app’s interface and design on Figma and draw.io. We then built the Keeper app natively in Java with Android Studio, and connected it to a Flask instance hosted on Heroku and MySQL instance hosted on Google Cloud Platform. A recycled Galaxy S6 stood-in as the merchant’s NFC-capable device, which sent the receipt data (JSON file) to the customer’s phone (Galaxy A8) via the Near Field Communication (NFC)/Android Beam API. The data was then converted on the Android app to a Java String and sent using an async POST request to the Heroku Flask Python instance, and stored in the MySQL cloud database hosted on Google Cloud. The data was then retrieved using a similar method to send a GET request, and converted from JSONArray back to string.

## Challenges we ran into & Complexities
The biggest challenge we faced was using the Android SDK Near-Field Communication API to send the JSON file via NFC and displaying the result (JSON file contents of the receipt) using Java JSON libraries on the frontend of the app. Connecting the android app to the databases through GET and POST requests was also difficult due to the lack of documentation and libraries for Java (compared to Python). We also used IO and JSON libraries for input stream and JSON parsing, Asynctasks for network requests, and the java.Net library for sending GET and POST requests directly from the Android phone. This was the first time the main android app developer learned these libraries, and applying it was tough. It was challenging to become familiar with Android Studio as it was our first time using it, but we quickly adapted and began to get the hang of it. Learning to use databases, Flask, and hosting on cloud was a challenge, but we got through it. 

## Accomplishments that we're proud of
We are glad that we managed to execute the functionality of the NFC chip in transferring data via tap. We are proud of finishing the database of the app where the receipt file can be stored and displayed on the app. Overall, we had a valuable experience learning about both back and frontend development for mobile applications. Combining our unique skills and experiences, our team collaborated very well and the members were always willing to help one another out when needed. With an average of less than 3 hackathons each, it was the first time our members have developed an Android app, first time using Flask, and the first time using and linking to a cloud-hosted database.

## What we learned
- NFC and app integration
- Project Management; Integrating Frontend and Backend
- Flask, SQL and Databases
- Hosting databases on Cloud
- Sending HTTP requests from an Android app
- Java and Android Studio
- The power of persistence (I’m looking at you NFC 😫) 

## What's next for Keeper
- Host-based card emulation instead of Android Beam
- Revolutionize your next social gathering by pooling receipts and splitting bills through a Groups feature
- Integrate machine learning data analysis to monitor the users’ spending patterns and assist them with - financial goal setting 
- Allow users to access their receipt collections from multiple devices through an account/login system 
- Offer a user feedback system for product quality assessment
- Improve UI/UX design 
- Develop dedicated, low-cost NFC kiosk

## References: 
ClimateAction. (2019, August 2019). Report: Skip the Slip - Environmental Costs & Human Health Risks of Paper Receipts. Retrieved from https://www.climateaction.org/news/report-skip-the-slip-environmental-costs-human-health-risks-of-paper-receip

