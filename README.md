# Github Repo Viewer

This repo contains an app to visualize the first 100 most starred github repositories

#### Instructions
1. Clone the repository
2. Open the project in Android Studio
3. Build the project
4. Run the app on a device or an emulator

Make sure the test phone or emulator has internet access

#### Notes
* I attempted to play around with the new shared element transition API in Jetpack Compose but while there is an animation when going from list to detail, the animation is not correct
* Because each repository has its own contributor URL, instead of showing the top contributor in the list item, I put it in the detail view to only fetch when needed
* The shimmer effect is done using a 3rd party library
