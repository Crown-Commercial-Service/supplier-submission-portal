describe("ListEntryField", function () {
  var entryFieldTemplate = Hogan.compile('<div class="list-entry">' +
                                          '<label for="{{{id}}}" class="text-box-number-label">' + 
                                            '<span class="hidden">Fieldset legend number </span>{{number}}.' +
                                          '</label>' +
                                          '<input type="text" name="{{{id}}}" id="{{{id}}}" class="text-box" value="">' +
                                        '</div>'
                            ),
      wrapperHTML = '<div class="input-list">' +
                      '<fieldset class="question">' + 
                        '<legend class="question-heading question-heading-with-hint ">Service features</legend>' + 
                        '<p class="question-hint">Include the technical features of your product, eg graphical workflow, remote access. (Maximum 10 words per feature. Maximum 10 features.)</p>' +
                      '</fieldset>' +
                    '</div>',
      $wrapper;

  beforeEach(function () {
    $wrapper = $(wrapperHTML);

    for (idx in [1,2,3,4,5,6,7,8,9,10]) {
      $wrapper.append(entryFieldTemplate.render({
        'id' : 'p1q1val' + idx,
        'number' : idx
      })); 
    }
    $(document.body).append($wrapper);
  });

  afterEach(function () {
    $wrapper.remove();
  });

  describe("When called", function () {
    it("Should remove all the fields except the first 2 if no values are present on page load", function () {
      expect($wrapper.find('.list-entry').length).toEqual(10);
      GOVUK.GDM.listEntry();
      expect($wrapper.find('.list-entry').length).toEqual(2);
    });

    it("Should remove all the fields except those with values if some have values on page load", function () {
      $wrapper.find('.list-entry input').eq(0).val('Hosting');
      $wrapper.find('.list-entry input').eq(1).val('Domain provision');
      $wrapper.find('.list-entry input').eq(2).val('CMS');
      $wrapper.find('.list-entry input').eq(3).val('Databases');
      GOVUK.GDM.listEntry();
      expect($wrapper.find('.list-entry').length).toEqual(4);
    });

    it("Should add 'remove' buttons to all fields except the first", function () {
      GOVUK.GDM.listEntry();
      expect($wrapper.find('.list-entry').eq(0).find('button.list-remove-entry').length).toEqual(0);
      expect($wrapper.find('.list-entry').eq(1).find('button.list-remove-entry').length).toEqual(1);
    });

    it("Should add an 'add feature' button to the bottom of the list", function () {
      GOVUK.GDM.listEntry();
      expect($wrapper.find('button.list-add-entry').length).toEqual(1);
    });
  });

  describe("When the 'remove' button is clicked", function () {
    it("Should remove the associated field", function () {

    });

    it("Should leave the list with the right numbers", function () {

    });
  }); 

  describe("When the 'add feature' button is clicked", function () {
    it("Should add a new field", function () {

    });

    it("Should update the number of fields users are allowed to enter to consider those already entered", function () {

    });
  });
});
