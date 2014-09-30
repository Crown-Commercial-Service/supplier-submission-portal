#!/bin/sh
# PARAMS: <new environement value> <cookie enc key>
appengine_web="./war/WEB-INF/appengine-web.xml"
application_conf="./conf/application.conf"
app_yaml="./war/WEB-INF/app.yaml"

# Check that exactly 3 values were passed in
if [ $# -eq 0 ]; then
echo  'ERROR: You must enter the value of the envionment.'
exit 127
fi


# ---------- appengine-web.xml -------------
echo "OUTPUT (appengine): Starting... [Ok]"
echo "OUTPUT (appengine): Getting $appengine_web, finding <application> and replacing its value with '$1'"

# Creating a temporary file for sed to write the changes to
temp_file="repl.temp"

# Adding an empty last line for sed to pick up
echo " ">> $appengine_web

# Extracting the value from the <$2> element
el_value=`grep "<application>.*<.application>" $appengine_web | sed -e "s/^.*<application/<application/" | cut -f2 -d">"| cut -f1 -d"<"`

echo 'OUTPUT (appengine): Found the current value for the element <application> - '$el_value''

# Replacing elemen’s value with $3
sed -e "s/<application>$el_value<\/application>/<application>$1<\/application>/g" $appengine_web > $temp_file

echo 'OUTPUT (appengine): Replaced '$el_value' with '$1

# Writing our changes back to the original file ($1)
chmod 666 $appengine_web
mv $temp_file $appengine_web

if [ $# -eq 2 ]; then
# ---------- appengine-web.xml (change cookie encryption key) -------------
echo "OUTPUT (appengine): Starting... [Ok]"
echo "OUTPUT (appengine): Getting $appengine_web, add cookie enc key"

# Creating a temporary file for sed to write the changes to
temp_file="repl.temp"

# Adding an empty last line for sed to pick up
echo " ">> $appengine_web

# Extracting the value from the <$2> element
el_value='@{encKey}'

echo 'OUTPUT (appengine): Found the current value for the element <application> - '$el_value''

# Replacing elemen’s value with $3
sed -e "s/@{encKey}/$2/g" $appengine_web > $temp_file

echo 'OUTPUT (appengine): Replaced '$el_value' with '$2

# Writing our changes back to the original file ($1)
chmod 666 $appengine_web
mv $temp_file $appengine_web
fi

# ---------- application.conf -------------
echo "OUTPUT (application_conf): Starting... [Ok]"
echo "OUTPUT (application_conf): Getting $application_conf, finding <application> and replacing its value with '$1'"

# Creating a temporary file for sed to write the changes to
temp_file="repl.temp"

# Adding an empty last line for sed to pick up
echo " ">> $application_conf

# Extracting the value from the <$2> element
el_value2=`grep "application.name=.*" $application_conf | cut -f2 -d"="`

echo 'OUTPUT (application_conf): Found the current value for application.name - '$el_value2''

# Replacing elemen’s value with $3
sed -e "s/application.name=$el_value2/application.name=$1/g" $application_conf > $temp_file

echo 'OUTPUT (application_conf): Replaced '$el_value2' with '$1

# Writing our changes back to the original file ($1)
chmod 666 $application_conf
mv $temp_file $application_conf


# ---------- app.yaml -------------
echo "OUTPUT (app_yaml): Starting... [Ok]"
echo "OUTPUT (app_yaml): Getting $app_yaml, finding <application> and replacing its value with '$1'"

# Creating a temporary file for sed to write the changes to
temp_file="repl.temp"

# Adding an empty last line for sed to pick up
echo " ">> $app_yaml

# Extracting the value from the <$2> element
el_value3=`grep "application:.*" $app_yaml | cut -f2 -d":"`

echo 'OUTPUT (app_yaml): Found the current value for application: - '$el_value3''

# Replacing elemen’s value with $3
sed -e "s/application:$el_value3/application: $1/g" $app_yaml > $temp_file

echo 'OUTPUT (app_yaml): Replaced '$el_value3' with '$1

# Writing our changes back to the original file ($1)
chmod 666 $app_yaml
mv $temp_file $app_yaml