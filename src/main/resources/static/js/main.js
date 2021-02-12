(function () {
  'use strict'

  var forms = document.querySelectorAll('.needs-validation')

  Array.prototype.slice.call(forms)
    .forEach(function (form) {
      form.addEventListener('submit', function (event) {
        if (!form.checkValidity()) {
          event.preventDefault()
          event.stopPropagation()
        }

        form.classList.add('was-validated')
      }, false)
    })
})()

function activePerson(active) {
    if (active) {
        $("#firstName").attr('readOnly', false);
        $("#lastName").attr('readOnly', false);
        $("#email").attr('readOnly', false);
        $("#group").attr('disabled', false);
        $("#comment").attr('readOnly', false);
    }
    else {
        $("#firstName").attr('readOnly', true);
        $("#lastName").attr('readOnly', true);
        $("#email").attr('readOnly', true);
        $("#group").attr('disabled', true);
        $("#comment").attr('readOnly', true);
    }
}

function activeGroup(active) {
    if (active) {
        $("#groupName").attr('readOnly', false);
    }
    else {
        $("#groupName").attr('readOnly', true);
    }
}

function activeCourse(active) {
    if (active) {
        $("#courseName").attr('readOnly', false);
        $("#author").attr('disabled', false);
        $("#description").attr('readOnly', false);
    }
    else {
        $("#courseName").attr('readOnly', true);
        $("#author").attr('disabled', true);
        $("#description").attr('readOnly', true);
    }
}

function isSelectedGroup(groupId) {
    if (groupId=="") {
        $("#addGroup").attr('disabled', true);
    }
    else {
        $("#addGroup").attr('disabled', false);
    }
}
