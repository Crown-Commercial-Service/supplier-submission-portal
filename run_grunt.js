var fs = require('fs'),
    argv = require('minimist')(process.argv.slice(2)),
    grunt = require(__dirname + '/node_modules/grunt/lib/grunt.js'),
    pidFile = __dirname + '/.start.pid',
    fileOptions = { encoding : 'utf-8' },
    gruntOptions;

// start grunt
if (argv._.length) {
  gruntOptions = argv._;
  grunt.cli(gruntOptions);
} else {
  grunt.cli();
}

if (!fs.existsSync(pidFile)) {
  fs.writeFileSync(pidFile, process.pid, fileOptions);
  process.on('SIGINT', function() {
    var pid = fs.readFileSync(pidFile, fileOptions);

    fs.unlink(pidFile);
    process.kill(pid, 'SIGTERM');
    process.exit();
  });
}
