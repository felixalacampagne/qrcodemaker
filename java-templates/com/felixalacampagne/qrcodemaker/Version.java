package com.felixalacampagne.qrcodemaker;

public class Version
{
   public static final String VERSION = "${project.version}";
   public static final String APPNAME = "${project.name}";
   // POS maven doesn't expose the build variables!
   //public static final String BLDTIME = "${build.localtime}";
   // POS maven doesn't even expand the build variables in a properties field
   // Why is it that every single simple trivial thing to do with maven takes hours of
   // searching the internet to figure out how to make it work (or not even find a solution!)
   //public static final String BLDTIME = "${timestamp}";
   //public static final String BLDTIME = "${maven.build.timestamp}";
   
}
