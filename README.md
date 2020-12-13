# Orangee

Orangee is an android app built for grocery shopping. The main UI elements I used are RecyclerView, TabLayout, ViewPager, and Navigation drawer. For networking, I made use of an http library called Volley and used sqlite database for storing some of the data.

<h2 id="screenshots">Screenshots</h2>

<p float="left">
  <img src="https://github.com/AnneJ17/grocery-app/blob/master/assets/screenshots/splash-screen.png" width="200" height ="400" />
  <img src="https://github.com/AnneJ17/grocery-app/blob/master/assets/screenshots/home-screen.png" width="200" height ="400" />
  <img src="https://github.com/AnneJ17/grocery-app/blob/master/assets/screenshots/nav-drawer.png" width="200" height ="400" />
  <img src="https://github.com/AnneJ17/grocery-app/blob/master/assets/screenshots/sub-categories.png" width="200" height ="400" />
  <img src="https://github.com/AnneJ17/grocery-app/blob/master/assets/screenshots/detail-screen.png" width="200" height ="400" />
  <img src="https://github.com/AnneJ17/grocery-app/blob/master/assets/screenshots/cart-items.png" width="200" height ="400" />
  <img src="https://github.com/AnneJ17/grocery-app/blob/master/assets/screenshots/empty-cart.png" width="200" height ="400" />
  <img src="https://github.com/AnneJ17/grocery-app/blob/master/assets/screenshots/adress-list.png" width="200" height ="400" />
  <img src="https://github.com/AnneJ17/grocery-app/blob/master/assets/screenshots/checkout-screen.png" width="200" height ="400" />
  <img src="https://github.com/AnneJ17/grocery-app/blob/master/assets/screenshots/confirmation-page.png" width="200" height ="400" />
  <img src="https://github.com/AnneJ17/grocery-app/blob/master/assets/screenshots/order-history.png" width="200" height ="400" />
</p>

<h2 id="development">Development</h2>

This app is built with Android Studio. Once you have the [Android Studio](https://developer.android.com/studio) installed, start by cloning the repo:

```
$ git clone https://github.com/AnneJ17/grocery-app.git
```

From there you can now run Orangee on the emulator you wish. However, this app was tested and run on on an emulator (Pixel 2) running API 29 (Android 10+ / R). 

Below is the command you can use if you're using terminal to run the emulator.
```
$ cd /Users/<user-name>/Library/Android/sdk/emulator/ && ./emulator -avd Pixel_2_API_29
```
