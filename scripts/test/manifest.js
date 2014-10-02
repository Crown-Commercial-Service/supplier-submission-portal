// files are loaded from the /scripts/test/support folder so paths are relative to that
var manifest = {
  support : [
    '../../../node_modules/jquery-browser/lib/jquery.js',
    '../../../public/javascripts/hogan.js',
    '../../../public/javascripts/listEntry.js',
    '../../../public/javascripts/wordCounter.js'
  ],
  test : [
    '../unit/WordCountSpec.js',
    '../unit/ListEntrySpec.js'
  ]
};
