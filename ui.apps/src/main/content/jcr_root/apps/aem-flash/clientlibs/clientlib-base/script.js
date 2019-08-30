$(document).ready(function() {

    $('body').hide().fadeIn(1000);
    $('#submit').click(function() {
        //read the values
        var assetPath = $('#assetPath').val();
        var myemail = $('#email').val();
        //alert(myemail) ; 

        //Perform an AJAX operation
        $.ajax({
            type: 'GET',
            url: '/content/aem-flash/en.workflow.html',
            data: 'assetPath=' + assetPath + '&email=' + myemail,
            success: function(msg) {
                alert(msg); //display the data returned by the servlet
            }
        });

    });
});