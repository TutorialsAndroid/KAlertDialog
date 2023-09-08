![](https://github.com/TutorialsAndroid/KAlertDialog/blob/master/sample/src/main/res/mipmap-xxhdpi/ic_launcher.png)

# New version released 20.5.8 on 08-09-2023
## Changelogs
- Added option to set default text in input field.
### Read the changes in README

Alert Dialog ![API](https://img.shields.io/badge/API-19%2B-brightgreen.svg?style=flat) [![Known Vulnerabilities](https://snyk.io/test/github/TutorialsAndroid/KAlertDialog/badge.svg?targetFile=library%2Fbuild.gradle)](https://snyk.io/test/github/TutorialsAndroid/KAlertDialog?targetFile=library%2Fbuild.gradle) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-KAlertDiaog-blue.svg?style=flat)](https://android-arsenal.com/details/1/7588) [![License](https://img.shields.io/badge/License-Apache%202.0-green.svg)](https://opensource.org/licenses/Apache-2.0)
===================
AlertDialog for Android, a beautiful and material alert dialog to use in your android app.

`Older verion of this library has been removed please use new version of this library.`

## Will you buy a coffee for me

[!["Buy Me A Coffee"](https://www.buymeacoffee.com/assets/img/custom_images/orange_img.png)](https://www.paypal.com/paypalme/tusharmasram)


## And Don't Forget To Follow Me On Instagram / Twitter

<p align="center">Follow me on instagram to stay up-to-date https://instagram.com/a.masram444
<p align="center">Follow me on twitter to stay up-to-date https://twitter.com/a_masram444

## Contributors

[NassB (Nassim B.)](https://github.com/NassB)

[moisoni97 (Moisoni Ioan)](https://github.com/moisoni97)

[paulocoutinhox (Paulo Coutinho)](https://github.com/paulocoutinhox)


`Latest version of this library is migrated to androidx`

## Features
- Materialistic alert dialog
- Auto dark mode
- Change font style
- Change text color
- Change button color and background
- Change button text color
- Show vector drawable image with tint option in dark mode
- Show Custom Image from URL in dialog (type: circleCrop, full-size)
- Change the content text-alignment as you want
- Change the title text alignment
- Progress type dialog
- Input field dialog
- More features are coming soon

## ScreenShot
![](https://github.com/TutorialsAndroid/KAlertDialog/blob/master/art/Screenshot_1665583187.png)

## Setup
The simplest way to use AlertDialog is to add the library as dependency to your build.

## Gradle

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
            mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}

Step 2. On your root build.gradle, add mavenCentral() to the allprojects section. On your module build.gradle, add

	dependencies {
          implementation 'io.github.tutorialsandroid:kalertdialog:20.4.8'
          implementation 'com.github.TutorialsAndroid:progressx:v6.0.19' //required for kalertdialog lib
	}

## Usage

**Show material progress**

    KAlertDialog pDialog = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE);
    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
    pDialog.setTitleText("Loading");
    pDialog.setCancelable(false);
    pDialog.show();


You can customize progress bar dynamically with materialish-progress methods via **KAlertDialog.getProgressHelper()**:
- resetCount()
- isSpinning()
- spin()
- stopSpinning()
- getProgress()
- setProgress(float progress)
- setInstantProgress(float progress)
- getCircleRadius()
- setCircleRadius(int circleRadius)
- getBarWidth()
- setBarWidth(int barWidth)
- getBarColor()
- setBarColor(int barColor)
- getRimWidth()
- setRimWidth(int rimWidth)
- getRimColor()
- setRimColor(int rimColor)
- getSpinSpeed()
- setSpinSpeed(float spinSpeed)

A basic message：

![](https://github.com/TutorialsAndroid/KAlertDialog/blob/master/art/photo_2022-10-12_19-42-18.png)

    new KAlertDialog(this)
        .setTitleText("Here's a message!")
        .show();

A title with a text under：

![](https://github.com/TutorialsAndroid/KAlertDialog/blob/master/art/photo_2022-10-12_19-42-18.png)

    new KAlertDialog(this)
        .setTitleText("Here's a message!")
        .setContentText("It's pretty, isn't it?")
        .show();

A title with gravity changed：

![](https://github.com/TutorialsAndroid/KAlertDialog/blob/master/art/photo_2022-10-12_19-42-30.png)

    new KAlertDialog(this, KAlertDialog.NORMAL_TYPE)
        .setTitleText("Lorem Ipsum")
        .setTitleTextGravity(Gravity.START) //you can specify your own gravity
        .setContentText("Lorem Ipsum is simply dummy text of the printing and typesetting industry.")
        .setConfirmClickListener("OK", null)
        .show();

A error message：

    new KAlertDialog(this, KAlertDialog.ERROR_TYPE)
        .setTitleText("Oops...")
        .setContentText("Something went wrong!")
        .show();

A warning message：

    new KAlertDialog(this, KAlertDialog.WARNING_TYPE)
        .setTitleText("Are you sure?")
        .setContentText("Won't be able to recover this file!")
        .setConfirmClickListener("Yes,delete it!", null)
        .show();

A success message：

![](https://github.com/TutorialsAndroid/KAlertDialog/blob/master/art/photo_2022-10-12_19-41-45.png)

    new KAlertDialog(this, KAlertDialog.SUCCESS_TYPE)
        .setTitleText("Good job!")
        .setContentText("You clicked the button!")
        .show();

A message with a custom icon：

    new KAlertDialog(this, KAlertDialog.CUSTOM_IMAGE_TYPE)
        .setTitleText("Sweet!")
        .setContentText("Here's a custom image.")
        .setCustomImage(R.drawable.custom_img)
        .show();

A message with a custom vector drawable with tint option in dark mode: 

    new KAlertDialog(this, KAlertDialog.CUSTOM_IMAGE_TYPE)
        .setTitleText("Sweet!")
        .setContentText("Here's a custom image.")
        .setCustomImage(R.drawable.vector_drawable)
        .setDrawableTintOnNightMode(true, R.color.red) //it will work only if your app is running in night mode
        .show();

A message with a custom image URL

![](https://github.com/TutorialsAndroid/KAlertDialog/blob/master/art/photo_2022-10-12_19-42-12.png)

    displayType - KAlertDialog.IMAGE_BIG - For full size image
    displayType - KAlertDialog.IMAGE_CIRCLE - For Circle Crop image

    Note: This feature is still in development

    new KAlertDialog(this, KAlertDialog.URL_IMAGE_TYPE)
                    .setTitleText("KAlertDialog")
                    .setContentText("Here's a custom image.")
                    .setURLImage("put your image url", displayType)
                    .setConfirmClickListener("OK", null)
                    .show();

A dialog with input-field

![](https://github.com/TutorialsAndroid/KAlertDialog/blob/master/art/photo_2022-10-12_19-42-36.png)

    KAlertDialog dialog = new KAlertDialog(this, KAlertDialog.INPUT_TYPE);
    dialog.setInputFieldHint("Write message");
    dialog.setInputFieldText("Hello World!"); //If you want to set default text in input field
    dialog.setTitleText("Edit Text");
    dialog.setConfirmClickListener("OK", kAlertDialog -> {
        kAlertDialog.dismissWithAnimation();
        kAlertDialog.getInputText(); //you get the input text by calling this
        Toast.makeText(this, kAlertDialog.getInputText(), Toast.LENGTH_SHORT).show();
    });
    dialog.show();
    //below this line is necessary to show keyboard when using input-field
    dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            |WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM); //this will allow to show keyboard

To Hide Cancel And Confirm Button：

    new KAlertDialog(this, KAlertDialog.CUSTOM_IMAGE_TYPE)
        .setTitleText("Sweet!")
        .setContentText("Here's a custom image.")
        .setCustomImage(R.drawable.custom_img)
        .showConfirmButton(false) //to hide the confirm button set it to false
        .showCancelButton(false) //to hide the cancel button set it to false
        .show();

To Change the font of only title：

    To apply custom downloaded fonts you have place your font file in "assest/fonts" folder or under "res/font" folder. Below 
    is the screen-shot for reference about how to place your font file in assest folder

![](https://github.com/TutorialsAndroid/KAlertDialog/blob/master/art/structure.png)

    new KAlertDialog(this, KAlertDialog.NORMAL_TYPE)
        .setTitleText("Lorem Ipsum")
        .setTitleFontAssets("fonts/os.ttf")
        .setContentText("Lorem Ipsum is simply dummy text of the printing and typesetting industry.")
        .setConfirmClickListener("OK", null)
        .show();

To Change the font of only content：

    To apply custom downloaded fonts you have place your font file in "assest/fonts" folder or under "res/font" folder. Below 
    is the screen-shot for reference about how to place your font file in assest folder

![](https://github.com/TutorialsAndroid/KAlertDialog/blob/master/art/structure.png)

    new KAlertDialog(this, KAlertDialog.NORMAL_TYPE)
        .setTitleText("Lorem Ipsum")
        .setContentText("Lorem Ipsum is simply dummy text of the printing and typesetting industry.")
        .setContentFont(R.font.sf)
        .setConfirmClickListener("OK", null)
        .show();

To Change the color of title and content

    .setTitleColor(R.color.yourColorName)
    .setContentColor(R.color.yourColorName)

To Change the content textAlignment

    //Text alignment start
    .setContentTextAlignment(View.TEXT_ALIGNMENT_VIEW_START, Gravity.START)
    
    or

    //Text alignment center
    .setContentTextAlignment(View.TEXT_ALIGNMENT_CENTER, Gravity.CENTER)    

Bind the listener to confirm button：

    new KAlertDialog(this, KAlertDialog.WARNING_TYPE, 0)
        .setTitleText("Are you sure?")
        .setContentText("Won't be able to recover this file!")
        .setConfirmClickListener("Yes,delete it!",new KAlertDialog.KAlertClickListener() {
            @Override
            public void onClick(KAlertDialog sDialog) {
                sDialog.dismissWithAnimation();
            }
        })
        .show();

Show the cancel button and bind listener to it：

    new KAlertDialog(this, KAlertDialog.WARNING_TYPE, 0)
        .setTitleText("Are you sure?")
        .setContentText("Won't be able to recover this file!")
        .setConfirmClickListener("Yes,delete it!", null)
        .showCancelButton(true)
        .setCancelClickListener("No,cancel plx!", new KAlertDialog.KAlertClickListener() {
            @Override
            public void onClick(KAlertDialog sDialog) {
                sDialog.cancel();
            }
        })
        .show();

Customizing the alert dialog

    //if your app is night mode then you can tint your vector drawable to specific color you want
    .setDrawableTintOnNightMode(true, R.color.white)

    // you can change the color of confirm button
    .confirmButtonColor(R.color.colorPrimary)

    // you can change the color of cancel button
    .cancelButtonColor(R.color.colorAccent) 

    //you can change the color of button text
    .setConfirmClickListener("OK", R.color.black, clickListener) 

    //you can change the color of button text 
    .setCancelClickListener("CANCEL", R.color.black, clickListener)

    // you can change the content text size
    .setContentTextSize(50) 

    //you can use html in title text and same in content text
    .setTitleText("<h2>Title</h2><br><p>Description here</p>")
  

And if you want to change the button corners with color create a drawable file

        <?xml version="1.0" encoding="utf-8"?>
        <selector xmlns:android="http://schemas.android.com/apk/res/android">
            <item android:state_pressed="true">
                <shape android:shape="rectangle">
                    <solid android:color="#FF5474" />
                    <corners android:radius="6dp"/>
                </shape>
            </item>
            <item>
                <shape android:shape="rectangle">
                    <solid android:color="#FF1744" />
                    <corners android:radius="6dp"/>
                </shape>
            </item>
        </selector>

And then call this method when you create drawable

      .confirmButtonColor(R.drawable.button_background) // you can change border and color of button
      
And if you want to hide Title Text and Content Text of alert dialog

	.setTitleText("Are you sure?") //just don't write this line if you want to hide title text
	.setContentText("Won't be able to recover this file!") // don't write this line if you want to hide content text

And if you want to hide Title Text and Content Text on alert type change

	new KAlertDialog(this, KAlertDialog.WARNING_TYPE)
        .setTitleText("Are you sure?")
        .setContentText("Won't be able to recover this file!")
        .showCancelButton(true)
        .setCancelClickListener("No,cancel plx!", sDialog -> 
            sDialog.setTitleText(null)
                .setContentText("Your imaginary file is safe :)")
                .showCancelButton(false)
                .setConfirmClickListener("OK", null)
                .changeAlertType(KAlertDialog.ERROR_TYPE))
        .setConfirmClickListener("Yes,delete it!",sDialog -> 
            sDialog.setTitleText("Deleted!")
                .showCancelButton(false)
                .setContentText(null)
                .setConfirmClickListener("OK",null)
                .changeAlertType(KAlertDialog.SUCCESS_TYPE))
        .show();

**Change** the dialog style upon confirming：

    new KAlertDialog(this, KAlertDialog.WARNING_TYPE, 0)
        .setTitleText("Are you sure?")
        .setContentText("Won't be able to recover this file!")
        .setConfirmText("Yes,delete it!")
        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
            @Override
            public void onClick(KAlertDialog sDialog) {
                sDialog
                    .setTitleText("Deleted!")
                    .setContentText("Your imaginary file has been deleted!")
                    .setConfirmClickListener("OK", null)
                    .changeAlertType(KAlertDialog.SUCCESS_TYPE);
            }
        })
        .show();
        
 ## License

* [Apache Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

```
Copyright 2019 KAlertDialog

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
       
