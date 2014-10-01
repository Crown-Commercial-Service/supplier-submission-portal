(function () {

  'use strict';

  var root = this,
      $ = this.jQuery,
      GOVUK = this.GOVUK || {};

  GOVUK.GDM = GOVUK.GDM || {};

  var counterClass = 'wordCount',
      attach = function() {
        var $textarea = $('textarea[data-max-length-in-words]')
        
        if (!$textarea.length) { return; }
        $textarea
          .after('<p class="' + counterClass + '" role="status" aria-live="assertive" aria-relevant="text" id="wordcount" />')
          .attr('aria-controls', 'wordcount')
          .on('change keyup paste', showCount);
        showCount.call($textarea[0]);
      },
      showCount = function() {
        var $textarea = $(this),
            contents = $textarea.val(),
            numberOfWords = countWords(contents),
            maxNumberOfWords = $textarea.data('max-length-in-words'),
            remainingNumberOfWords = maxNumberOfWords - numberOfWords,
            message = getMessageText(remainingNumberOfWords);

        $textarea
          .next('.' + counterClass)
          .html(message);
      },
      countWords = function(text) {
        var tokens = text.match(/\S+/g) || []; // Matches consecutive non-whitespace chars

        return tokens.length;
      },
      getMessageText = function(count) {
        var displayedCount = Math.abs(count); // Don't show negative numbers

        if (count < -1) {
          return displayedCount + ' words too many';
        }

        if (count === -1) {
          return  '1 word too many';
        }

        if (count === 1) {
          return '1 word remaining';
        }

        if (count === 0 || count > 1) {
          return displayedCount + ' words remaining';
        }
      };

  GOVUK.GDM.wordCounter = function () {
    attach();
  };

  this.GOVUK = GOVUK;

}).call(this);
