$(document).ready(function() {
    $('#toSend').click(function() {
            var data = $('#inputNumber').val();
            var json = {"youNumber": data};
            if((data !=='') && (data.length===4)){
                document.getElementById("error").textContent="";
                $('input[type="text"]').css("border", "0");
            $.ajax({
                url: urlAttempt,
                type: "POST",
                headers: {
                    "X-CSRF-TOKEN": $('#_csrf_token').attr('value')
                },
                contentType: 'application/json',

                data: JSON.stringify(json),
                cache: false,
                success: function (responseText) {
                   $('#textarea').append(responseText['youNumber'] +"\n");
                   document.getElementById("inputNumber").value = "";
                    if (responseText['properties']!=="null"){
                        alert(responseText['properties']);
                    }
                },
                error: function (jqXHR) {
                    alert("error:" + jqXHR.status + " exception:" + jqXHR.responseText);
                }
            });
        }
        else{
                if(data==='') {
                    document.getElementById("error").textContent = "Введите число";
                    $('input[type="text"]').css("border", "2px solid red");
                }
                if((data.length<4)&&(data.length>=1)){
                    document.getElementById("error").textContent = "Введите ровно 4 цифры";
                    $('input[type="text"]').css("border", "2px solid red");
                }
        }
    });

    $('#history').click(function() {
            $.ajax({
                url: urlHistory,
                type: "POST",
                data: JSON.stringify(
                    {
                    "clear":"no"
                }),
                headers: {
                    "X-CSRF-TOKEN": $('#_csrf_token').attr('value')
                },
                contentType: "text/plain;",

                success: function (responseText) {
                    $('#textareaHistory').text(responseText);
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    alert("error:" + jqXHR.status + " exception:" + jqXHR.responseText);
                }
            });
    });

    $('#clearHistory').click(function() {
        $.ajax({
            url : urlHistory,
            type: "POST",
            data: JSON.stringify({"clear":"yes"}),
            headers: {
                "X-CSRF-TOKEN": $('#_csrf_token').attr('value')
            },
            contentType: "text/plain;",

            success: function (responseText) {
                $('#textareaHistory').text('');
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert("error:" + jqXHR.status + " exception:" + jqXHR.responseText);
            }
        });
    });

});

function check(input) {
    input.value = input.value.replace(/[^\d]/g, '');
    if (input.value.length===4) {
        if (input.value[0] === input.value[1]  || input.value[0] === input.value[2]||
            input.value[0] === input.value[3]  || input.value[1] === input.value[2] ||
            input.value[1] === input.value[3] || input.value[2] === input.value[3]) {
            input.value = input.value.replace(/^[0-9]{4}$/, "");
        }
    }
}


