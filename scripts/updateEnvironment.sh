#!/bin/sh
# PARAMS: <new environement value> <cookie enc key> <SSL t/f>
appengine_web="./war/WEB-INF/appengine-web.xml"
application_conf="./conf/application.conf"
app_yaml="./war/WEB-INF/app.yaml"

DIR=`dirname $0`
echo "DIR $DIR"
"${DIR}/generateWebDescriptor.groovy" $1 $2 $3

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