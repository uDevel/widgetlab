# Typing indicator or wait progress animation.

<img src="https://media.giphy.com/media/CSIyqIAxVFzhK/giphy.gif" width="30%" /> <img src="https://media.giphy.com/media/kPZRmEfmctbck/giphy.gif" width="30%" /> <img src="https://media.giphy.com/media/CSwfaFSSBvL6U/giphy.gif" width="30%" />


## Video of Sample app:

[![Youtube video of Sample app](http://img.youtube.com/vi/tNltD2vnbsw/0.jpg)](http://www.youtube.com/watch?v=tNltD2vnbsw "Sample app")

## How to use:
Just put it in your xml layout.  It can't get any easier than that.
```xml
 <com.udevel.widgetlab.TypingIndicatorView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"/>
```
This gives you the default.

## Bunch of configuration available
```xml
<declare-styleable name="TypingIndicatorView">
        <attr name="dotSize" format="dimension"/>
        <attr name="dotCount" format="integer"/>
        <attr name="dotColor" format="color"/>
        <attr name="dotSecondColor" format="color"/>
        <attr name="dotHorizontalSpacing" format="dimension"/>
        <attr name="dotMaxCompressRatio" format="fraction"/>
        <attr name="dotAnimationDuration" format="integer"/>
        <attr name="dotAnimationType" format="enum">
            <enum name="Grow" value="0"/>
            <enum name="Wink" value="1"/>
            <enum name="Disappear" value="2"/>
            <enum name="Sliding" value="3"/>
            <enum name="BouncingSliding" value="4"/>
        </attr>
        <attr name="showBackground" format="boolean"/>
        <attr name="backgroundColor" format="color"/>
        <attr name="backgroundType" format="enum">
            <enum name="square" value="0"/>
            <enum name="rounded" value="1"/>
        </attr>
        <attr name="animateFrequency" format="integer"/>
        <attr name="animationOrder" format="enum">
            <enum name="random" value="0"/>
            <enum name="sequence" value="1"/>
            <enum name="circular" value="2"/>
            <enum name="lastOneFirst" value="3"/>
        </attr>
    </declare-styleable>
```    

Example for "Sliding with lastOneFirst animation order" from the gif:
```xml
<com.udevel.widgetlab.TypingIndicatorView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:padding="8dp"
            app:animateFrequency="700"
            app:animationOrder="lastOneFirst"
            app:backgroundColor="#eaeaea"
            app:backgroundType="rounded"
            app:dotAnimationDuration="600"
            app:dotAnimationType="Sliding"
            app:dotColor="#a4a4a4"
            app:dotCount="3"
            app:dotHorizontalSpacing="4dp"
            app:dotMaxCompressRatio="70%"
            app:dotSecondColor="#6ba4a4a4"
            app:dotSize="10dp"
            app:showBackground="true"/>
```

## Gradle:
```xml
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

```xml
dependencies {
          compile 'com.github.uDevel:widgetlab:0.9.1'
}
```
