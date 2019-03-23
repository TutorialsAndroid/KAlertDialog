Alert Dialog
===================
Alert for Android, a beautiful and material alert dialog

## ScreenShot
![image]()

## Setup
The simplest way to use AlertDialog is to add the library as aar dependency to your build.

**Gradle**

    repositories {
        mavenCentral()
    }

    dependencies {
        compile 'cn.pedant.sweetalert:library:1.3'
    }

## Usage

show material progress

    KAlertDialog pDialog = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE);
    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
    pDialog.setTitleText("Loading");
    pDialog.setCancelable(false);
    pDialog.show();

![image](https://github.com/pedant/sweet-alert-dialog/raw/master/play_progress.gif)

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

thanks to the project [materialish-progress](https://github.com/pnikosis/materialish-progress) and [@croccio](https://github.com/croccio) participation.

more usages about progress, please see the sample.

A basic message：

    new KAlertDialog(this)
        .setTitleText("Here's a message!")
        .show();

A title with a text under：

    new KAlertDialog(this)
        .setTitleText("Here's a message!")
        .setContentText("It's pretty, isn't it?")
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
        .setConfirmText("Yes,delete it!")
        .show();

A success message：

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

Bind the listener to confirm button：

    new KAlertDialog(this, KAlertDialog.WARNING_TYPE)
        .setTitleText("Are you sure?")
        .setContentText("Won't be able to recover this file!")
        .setConfirmText("Yes,delete it!")
        .setConfirmClickListener(new KAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(KAlertDialog sDialog) {
                sDialog.dismissWithAnimation();
            }
        })
        .show();

Show the cancel button and bind listener to it：

    new KAlertDialog(this, KAlertDialog.WARNING_TYPE)
        .setTitleText("Are you sure?")
        .setContentText("Won't be able to recover this file!")
        .setCancelText("No,cancel plx!")
        .setConfirmText("Yes,delete it!")
        .showCancelButton(true)
        .setCancelClickListener(new KAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(KAlertDialog sDialog) {
                sDialog.cancel();
            }
        })
        .show();

**Change** the dialog style upon confirming：

    new KAlertDialog(this, KAlertDialog.WARNING_TYPE)
        .setTitleText("Are you sure?")
        .setContentText("Won't be able to recover this file!")
        .setConfirmText("Yes,delete it!")
        .setConfirmClickListener(new KAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(KAlertDialog sDialog) {
                sDialog
                    .setTitleText("Deleted!")
                    .setContentText("Your imaginary file has been deleted!")
                    .setConfirmText("OK")
                    .setConfirmClickListener(null)
                    .changeAlertType(KAlertDialog.SUCCESS_TYPE);
            }
        })
        .show();