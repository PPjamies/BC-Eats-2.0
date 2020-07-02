# BC-Eats-2.0
Refactored version of BC Eats 1.0

Authors: Pazuzu Jindrich, Max Henderson.
QA tester: Jason Guo

Summary: BC Eats attempts to reduce food waste from campus events by notifying students of free surplus foods.

This version of BC Eats added:
-Mvvm architecture.
-Improved UI with navigation drawer and collapsing toolbar.
-Users are saved into database.
-Users can now view, edit, and delete their listings.
-Users can view and edit their phone number tied to their account.
-Real phone numbers are authenticated by firebase.
-Cloud function triggers whenever new posting is created.
-Cloud function successfully sends data payload to users with valid push tokens.
-Push notifications are recieved in foreground and background using FCM.

This version of BC Eats fixed:
-Saving images from camera to device storage.
-Android API 23+ prompt users for external read/write permissions.

This version includes following issues:
-FCM does not work on all android devices whenever app is killed -therefore, not all
phones will recieve a push notification when app is killed.
-Fake phone numbers are still being saved into database even though they are not authenticated
by firebase.
