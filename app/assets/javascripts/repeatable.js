var addItemToList = function(event) {

  $(this)
  .parents("fieldset")
  .find(".repeatable")
  .filter(":hidden")
  .eq(0)
    .show();

  doRepeatableControls.apply(this, []);
  return false;
},
removeItemFromList = function(event) {

  $(this)
  .parents(".repeatable")
    .hide()
  .find("input, select")
    .val("");

  doRepeatableControls.apply(this, []);
  return false;
},
doRepeatableControls = function() {

  var $repeatables = $(this).parents("fieldset").find(".repeatable");

  $repeatables.parents("fieldset").find(".listAdd").toggle(
    $repeatables.filter(":visible").length !== $repeatables.length
  );

  $repeatables
  .find(".listRemove")
    .hide()
  .end()
  .filter(":visible")
  .not(":first-of-type")
  .eq(-1)
  .find(".listRemove")
    .show();

};
