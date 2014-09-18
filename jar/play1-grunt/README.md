# Play Grunt

<http://gruntjs.com/>

Run Grunt on application start in Play dev mode using "grunt run --force".

this plugin is only supposed to work while you develop.
It needs to have [NPM](npmjs.org) installed

> **Note** It may or may not work on windows, since it was developed on a mac and not tested with windows.

# Installation

add the plugin to the `conf/dependencies.yml`

	require:
		- play
		- play -> grunt 1.0

Add the grunt configuration to `conf/application.conf`

	# Enable the grunt runner
	grunt.enabled=true

	# Specify where the Gruntfile.js is located
	grunt.home=ui

	# Specify if you want grunt to output to the play console
	grunt.log=true



# Sample
In the sample folder you can see a setup where we use grunt together with bower and angular.
<https://github.com/digiPlant/play1-grunt/tree/master/sample>
