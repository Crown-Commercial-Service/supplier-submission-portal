// Copied from http://stackoverflow.com/questions/1227286/get-class-list-for-element-with-jquery
(function ($) {

  $.fn.classes = function () {

    var classes = [];

    $.each(this, function (i, v) {
      var splitClassName = v.className.split(/\s+/);
      if ("" === v.className) return true;
      for (var j in splitClassName) {
        var className = splitClassName[j];
        if (-1 === classes.indexOf(className)) {
          classes.push(className);
        }
      }
    });

    return classes;

  };

})(jQuery);
