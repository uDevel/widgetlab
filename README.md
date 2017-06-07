[![](https://jitpack.io/v/uDevel/widgetlab.svg)](https://jitpack.io/#uDevel/widgetlab)

# Typing indicator or wait progress animation.

## Gif of Sample app:
<img src="./github_assets/show_case_demo.gif" width="30%" /> <img src="./github_assets/attribute_demo1.gif" width="30%" /> <img src="./github_assets/attribute_demo2.gif" width="30%" />


## Video of Sample app:

[![Youtube video of Sample app](http://img.youtube.com/vi/tNltD2vnbsw/0.jpg)](http://www.youtube.com/watch?v=tNltD2vnbsw "Sample app")

## How to use:
Just put it in your xml layout.  It can't get any easier than that.
```xml
 <com.udevel.widgetlab.TypingIndicatorView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"/>
```
This gives you the default.  There are many optional [configurations](https://github.com/uDevel/widgetlab/wiki/Configurations) available.  This is recommended if you want to have unique animation; there are examples that you can modify from.

Ex. BouncingSliding animation type:    
<img src="./github_assets/bouncing_sliding.gif" width="8%" />

## Gradle:
Add to your project level build.gradle's allprojects
```xml
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

Next add to your module level (app) build.gradle's dependencies block like this
```xml
dependencies {
          compile 'com.github.uDevel:widgetlab:0.9.2'
}
```

Lastest version   
[![](https://jitpack.io/v/uDevel/widgetlab.svg)](https://jitpack.io/#uDevel/widgetlab)
## Todos:
- Optimizing gc.
- More animation type.
