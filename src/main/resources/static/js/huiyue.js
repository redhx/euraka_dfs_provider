
function post(input_name,url) {
	var info = $("input[name='" + input_name + "']").val().trim();
    var noCache = Date();
    var url = url;
    $.post(url, {
        noCache: noCache,
        info: info
    }, function (data) {
    	if(data=="success"){
    		alert("修改成功！")
    	}else{
    		alert("修改失败！")
    	}
    });
}