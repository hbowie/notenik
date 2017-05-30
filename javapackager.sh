#!/bin/sh

Name="Notenik"
name="notenik"
docs="/Users/hbowie/PSPub Docs"
appf="dist/appfolder"
cp 	-p -v "$docs/plists/$Name/Info.plist" package/macosx
mkdir -p -v $appf
mkdir -p -v $appf/help
cp -p -v "$docs/help/$name-user-guide.html" $appf/help
cp -p -v "$docs/help/$name-history.html" $appf/help
cp -p -v "$docs/help/styles.css" $appf/help
cp -p -R "$docs/jars/help/$name-intro" $appf/help
mkdir -p -v $appf/logos
cp -p -v "$docs/logos/$name.png" $appf/logos/$name.png
mkdir -p -v $appf/reports
cp -p -R "$docs/resources/$name/reports" $appf

mkdir -p dist/appfolder
jdk=$(/usr/libexec/java_home)
$jdk/bin/javapackager -version
$jdk/bin/javapackager -deploy \
	-native dmg \
	-srcdir dist \
    -srcfiles notenik.jar \
    -srcfiles lib \
    -srcfiles appfolder \
    -appclass com.powersurgepub.notenik.Notenik \
    -name Notenik \
    -title "Note taking app" \
    -BappVersion=3.00 \
    -outdir deploy \
    -outfile notenik \
    -v

# cp deploy/bundles/ShowTime-1.0.dmg show-time-installer.dmg
# ls -l
