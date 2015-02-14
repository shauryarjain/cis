HW2_README.txt
Hi there.
I wrote this basic Android App in conjunction with the CIS 350 Software Engineering class taught by Chris Murphy (Spring 2015) at the University of Pennsylvania. I used Android Studio (v. 1.0.1) and OSX (v. 10.9.5). The app is intended for Nexus 5 emulators (API 21).

About:
 Users select the number of locations/vertices they would like to play with at the main menu screen, and attempt to solve the Traveling Salesman problem using popular locations at the University of Pennsylvania. The main menu is handled by a MainActivity and XML View file, and the map/drawing/logic is handled in a separate GameActivity and custom GameView. The app involves the use of Spinners, ActionBars, Intents, 2D Graphics classes (Paint/Point/Path) and serves as an comprehensive project to familiarize students with Android.

Misc:
I added Genymotion as an emulator for Android Studio, which I would highly recommend for its speed and ease of use. Here's a quick tutorial:
https://tleyden.github.io/blog/2013/11/22/android-studio-plus-genymotion-emulator/

If you are (still) experiencing problems running the app, you may want to consider syncing the project with Gradle, pressing : "Sync Project with Gradle Files". This should fix any XML formatting problems.

Also If you plan on moving this app to/from another directory, the Refactor -> Move option (shortcut F6) in Android Studio is also quite helpful.

