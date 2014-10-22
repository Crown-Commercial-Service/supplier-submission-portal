#!/bin/sh

WAR_LOCATION=/tmp/ssp-preview.war

npm cache clean
npm install
node run_grunt.js content
node run_grunt.js production
rm -rf $WAR_LOCATION 
play clean
play deps -sync
./scripts/updateEnvironment.sh ssp-preview Bar12345Bar12345 false
play war -o $WAR_LOCATION --%preview
~/google-cloud-sdk/bin/appcfg.sh --oauth2 update $WAR_LOCATION

