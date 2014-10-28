export PATH=/usr/bin/env:/var/lib/jenkins/tools/jenkins.plugins.nodejs.tools.NodeJSInstallation/nodejs/bin:/data/supplier-submission/play-1.2.4/:$PATH

# Get the content
git submodule init && git submodule update

# Install node things
npm cache clean
npm install

# Generate the content as a properties file
node run_grunt.js content

# Run Jasmine tests of client side code
node run_grunt.js dev
node run_grunt.js test

# Generate assets into public
node run_grunt.js production

# Run unit and Selenium tests within play
./scripts/updateEnvironment.sh ssp-preview ${ENCRYPTION_KEY} false gds-g6-submission-bucket-preview
play clean
play deps
play auto-test

# Build artefact for staging
./scripts/updateEnvironment.sh ssp-staging ${ENCRYPTION_KEY} false gds-g6-submission-bucket-staging
play clean
play deps
play war -o /data/supplier-submission/last-successful/submissions-staging-${BUILD_NUMBER}.war --%staging

# Build artefact for live
./scripts/updateEnvironment.sh ssp-live ${ENCRYPTION_KEY} true gds-g6-submission-bucket-live

## Build cron jobs XML for live environment
./scripts/generateCron.groovy

## use production web.xml
mv ./war/WEB-INF/web.xml ./war/WEB-INF/web-not-production.xml
mv ./war/WEB-INF/web-production.xml ./war/WEB-INF/web.xml
play clean
play deps
play war -o /data/supplier-submission/last-successful/submissions-live-${BUILD_NUMBER}.war --%prod
## switch back to original web.xml
mv ./war/WEB-INF/web.xml ./war/WEB-INF/web-production.xml
mv ./war/WEB-INF/web-not-production.xml ./war/WEB-INF/web.xml

## Delete cron jobs XML so clean for next time
rm -f ./war/WEB-INF/cron.xml

git tag -a jenkins-build-${BUILD_NUMBER} -m "To deploy this build, use the following build number in Jenkins: ${BUILD_NUMBER}"
git push --tags
