(function () {

  'use strict';

  var root = this,
      $ = this.jQuery;

  var lists = [],
      listEntry,
      ListEntry;

  ListEntry = function (elm) {
    var $elm = $(elm),
        idPattern = this.getIdPattern($elm.find('input')[0]);

    if (!idPattern) { return false; }
    this.idPattern = idPattern;
    this.elementSelector = '.list-entry, .list-remove-entry';
    this.$wrapper = $elm;
    this.entries = [];
    this.minEntries = 2;
    this.getValues();
    this.trimEntries();
    this.render();
  };
  ListEntry.prototype.entryTemplate = Hogan.compile(
    '<div class="list-entry">' +
      '<label for="{{{id}}}" class="text-box-number-label">' +
        '<span class="hidden">Fieldset legend number </span>{{number}}.' +
      '</label>' +
      '<input type="text" name="{{{name}}}" id="{{{id}}}" class="text-box" value="{{{value}}}">' +
      '{{#button}}' +
        '<button type="button" class="button-secondary list-remove-entry">' +
          'Remove<span class="hidden"> Fieldset legend number {{number}}</span>' +
        '</button>' +
      '{{/button}}' +
    '</div>'
  );
  ListEntry.prototype.addButtonTemplate = Hogan.compile(
    '<button type="button" class="button-secondary list-add-entry">Add another {{subject}}</button>'
  );
  ListEntry.prototype.getIdPattern = function (input) {
    var pattern = input.id.match(/(p\d+q\d+val)\d+$/);

    if (pattern !== null) {
      return pattern[1];
    } else {
      return false;
    }
  };
  ListEntry.prototype.getValues = function () {
    this.$wrapper.find('input').each(function (idx, elm) {
      var val = $(elm).val();

      this.entries.push(val);
    }.bind(this));
  };
  ListEntry.prototype.trimEntries = function () {
    this.entries = $.map(this.entries, function (entry) {
      if (entry === '') {
        return null
      } else {
        return this;
      }
    }.bind(this));
    if (this.entries.length < this.minEntries) {
      this.entries = ['', ''];
    }
  };
  ListEntry.prototype.getId = function (num) {
    return this.idPattern + num;
  };
  ListEntry.prototype.render = function () {
    this.$wrapper.find(this.elementSelector).remove();
    $.each(this.entries, function (idx, entry) {
      var entryNumber = idx + 1,
          dataObj = {
            'id' : this.getId(entryNumber),
            'number' : entryNumber,
            'name' : this.getId(entryNumber),
            'value' : entry
          };

      if (entryNumber > 1) {
        dataObj.button = true;
      }
      this.$wrapper.append(this.entryTemplate.render(dataObj));
    }.bind(this));
    this.$wrapper.append(this.addButtonTemplate.render({
      'subject' : 'subject'
    }));
  };

  listEntry = function () {
    $('.input-list').each(function () {
      lists.push(new ListEntry(this));
    });
  };

  this.GOVUK = this.GOVUK || {};
  this.GOVUK.GDM = this.GOVUK.GDM || {};
  this.GOVUK.GDM.listEntry = listEntry;
}).call(this);
