export PATH=/usr/bin/env:/var/lib/jenkins/tools/jenkins.plugins.nodejs.tools.NodeJSInstallation/nodejs/bin:/data/supplier-submission/play-1.2.4/:$PATH

# Get the content
git submodule init && git submodule update

# Install node things
npm cache clean
npm install

# Generate the content as a properties file
node run_grunt.js content

# Generate assets into public
node run_grunt.js production

# Run Jasmine tests of client side code
node run_grunt.js test

# Build artefact for preview
./scripts/updateEnvironment.sh ssp-preview ${ENCRYPTION_KEY} false
play clean
play deps
play war -o /data/supplier-submission/last-successful/preview-${BUILD_TAG}.war

# Build artefact for staging
./scripts/updateEnvironment.sh ssp-staging ${ENCRYPTION_KEY} false
play clean
play deps
play war -o /data/supplier-submission/last-successful/staging-${BUILD_TAG}.war

# Build artefact for live
./scripts/updateEnvironment.sh ssp-live ${ENCRYPTION_KEY} true
play clean
play deps
play war -o /data/supplier-submission/last-successful/live-${BUILD_TAG}.war
