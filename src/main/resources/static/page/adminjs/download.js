// Ajax 文件下载
$.download = function(url, data){    // 获得url和data
    var inputs = '';    
    if(data != undefined){
        $.each(data, function(name, value) {
            inputs+='<input type="hidden" name="'+ name +'" value="'+ value +'" />';
        });
    }
    $('<form action="'+ url +'" method="post">'+inputs+'</form>')
    .appendTo('body').submit().remove();
};