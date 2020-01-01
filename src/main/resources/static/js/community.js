
function post() {
    var questionId = $("#question-id").val();
    var content = $("#comment-content").val();
    if(!content){
        alert("不能回复空内容");
        return;
    }
    $.ajax({
        type: 'POST',
        contentType:'application/json',
        url:'/comment',
        data:JSON.stringify({
            "parentId":questionId,
            "content":content,
            "type":1
        }),
        success: function(response){
            if(response.code == 200){
                window.location.reload();
            }else{
                if (response.code == 2003){
                    var isAccepted = confirm(response.message);
                    if(isAccepted){
                        window.open("https://github.com/login/oauth/authorize?client_id=285decf57fd9b0de65fd&redirect_uri=http://localhost:8080/callback&scope=user&state=1")
                        window.localStorage.setItem("closable",true);
                        window.focus();
                    }
                }else {
                    alert(response.message);
                }
            }
        },
        dataType:'json'
    })
    console.log(questionId);
    console.log(content);
}