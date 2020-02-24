# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\SDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Allow obfuscation of android.support.v7.internal.view.menu.**
# to avoid problem on Samsung 4.2.2 devices with appcompat v21
# see https://code.google.com/p/android/issues/detail?id=78377
-keep class !android.support.v7.internal.view.menu.**,android.support.** {*;}


-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

##---------------End: proguard configuration for Gson  ----------
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-dontshrink
-verbose

#-injars bin/classes
#-injars libs
#-outjars bin/classes-processed.jar

#-libraryjars <java.home>/jre/lib/rt.jar
#-libraryjars <java.home>/lib/tools.jar
#-libraryjars /libs/gson-2.4.jar
#-libraryjars /libs/jcifs-1.3.18.jar
#-libraryjars /libs/RootTools.jar
#-libraryjars /libs/zip4j_1.3.2.jar


-dontwarn org.apache.**
-dontwarn org.slf4j.**
-dontwarn org.json.*
-dontwarn org.mortbay.**
-dontwarn org.apache.log4j.**
-dontwarn org.apache.commons.logging.**
-dontwarn org.apache.commons.logging.**
-dontwarn org.apache.commons.codec.binary.**
-dontwarn javax.xml.**
-dontwarn javax.management.**
-dontwarn java.lang.management.**
-dontwarn android.support.**
-dontwarn com.google.code.**
-dontwarn oauth.signpost.**
-dontwarn twitter4j.**

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-keep class javax.**  { *; }
-keep class org.**  { *; }
-keep class twitter4j.**  { *; }
-keep class java.lang.management.**  { *; }
-keep class com.google.code.**  { *; }
-keep class oauth.signpost.**  { *; }
-keepattributes InnerClasses

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-dontwarn javax.servlet.**
-dontwarn jcifs.http.NetworkExplorer

-keep,allowoptimization,allowobfuscation class eu.masconsult.android_ntlm.* {*;}

################################################################################
## Fresco ##
# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip

# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**
## end Fresco proguard

-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

-ignorewarnings
-keep class net.lingala.zip4j.** { *; }

## startapp
-keep class com.startapp.** {
      *;
}
-keepattributes Exceptions, InnerClasses, Signature, Deprecated,  SourceFile, LineNumberTable, *Annotation*, EnclosingMethod
-dontwarn android.webkit.JavascriptInterface



-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }
-keep public class com.androidrocker.voicechanger.** { *; }

-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

-keep class !android.support.v7.view.menu.*MenuBuilder, android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }

#thuckechsu
-keep public class com.passcode.lockscreen.photo.controller.LockScreenService
-keeppackagenames org.jsoup.nodes

