function overrideSubmit(){
var frm = $('#recognitionForm');

       frm.submit(function (e) {

           e.preventDefault();
           var formData = new FormData(this);
           
           $.ajax({
               type: frm.attr('method'),
               url: frm.attr('action'),
               data: formData,
               cache: false,
               contentType: false,
               processData: false,
               
               success: function (data) {
               	$("#div1").html(data);
                   console.log('Submission was successful.');
                   console.log(data);
               },
               error: function (data) {
                   console.log('An error occurred.');
                   console.log(data);
               },
           });
       });
}