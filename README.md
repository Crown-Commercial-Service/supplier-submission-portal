Digital Marketplace - Supplier Submission Portal
================================================

Application to enable suppliers to submit new services for inclusion in the Digital Marketplace.

Installation Instructions
-------------------------

Download and unzip the Play Framework version 1.2.4:
http://downloads.typesafe.com/releases/play-1.2.4.zip

Add the 'play' run script to your path in whatever manner you like (I created a symlink to it from /usr/local/bin and that works just fine. If you have other versions of play already installed then you can call the link 'play1' or whatever instead, to be sure you're firing up the right one.)

`~/workspace$ git clone git@github.com:alphagov/supplier-submission-portal.git`

`~/workspace$ cd supplier-submission-portal`

Pull in the required modules (you should only need to do this once, or when dependencies are updated):

`~/workspace/supplier-submission-portal$ play dependencies`

Install the Node modules required for the front end and generate the static assets:

`npm install && node run_grunt.js dev`

Getting the content
-------------------

You need to pull in the submodule that contains the content:

`git submodule update --init --recursive`

A grunt task called `content` builds `conf/content.properties` file from the YAML files in `conf/digital-marketplace-ssp-content`.

When the task is run `conf/content.properties` is stored as an artefact ignored by Git.

This can be done for development by running the following:

`node run_grunt.js content`

Note: the task uses new NPM modules so you'll have to run `npm install` before running it.

Start her up!
-------------

`~/workspace/supplier-submission-portal$ play run`

Then point a browser to [http://localhost:9000/](http://localhost:9000/) to see where we're at.

Before you can add new services you need to load the fixtures by visiting [http://localhost:9000/fixtures](http://localhost:9000/fixtures).

If you want to use the IntelliJ Idea IDE then do the following before importing the project (other import methods don't seem to work as nicely as this one); there are also similar commands for other popular IDEs:

`~/workspace/supplier-submission-portal$ play idealize`

Then in Idea open "ssp-spike.ipr" (check the filename as there may be other IntelliJ files in the project that don't import as nicely).



Useful Play Commands
--------------------

To run all tests (this is the command executed on Jenkins before a new build is deployed):

`~/workspace/supplier-submission-portal$ play auto-test`

To pick a selection of tests to run:

`~/workspace/supplier-submission-portal$ play test`

Then point a browser to [http://localhost:9000/@tests](http://localhost:9000/@tests) to pick the tests to run.

Play Documentation
------------------

Documentation for Play 1.2.4 is here: https://www.playframework.com/documentation/1.2.4/home

