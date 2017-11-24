var DisplayName = function(){
    //保存所有已经查询过得到的值，key=访问的url，value=返回值['key']
    var dataMap={};
    //保存正在请求的url跟对应的元素id, 相同url的请求，则进id对进累加，用逗号分割开
    //当请求返回后，再对callingMap[url]里的每个元素的id进行赋值显示
    var callingMap = {};
    return {
		//url: 发ajax请求给服务端的地址
		//id: span的id, 返回结果将会显示到这个元素上
		//value：要查询的值，有值就发ajax，没值则不发
		//key: ajax返回json对象里的某个key值。key所对应的value值将会显示到id绑定的元素上
        showName: function(url, id, value, key){
            if(value){
                var keyValue = dataMap[url];
                if(!keyValue){
                    //判断是否存在正在查询的
                    if(!callingMap[url]){
                        callingMap[url] = id + ",";
                        $.ajax({
                            url: url,
                            dataType: 'json',
                            type: 'post',
                            success: function(rtn){
                                //保存请求返回的值到map中缓存
                                dataMap[url]=rtn[key];
                                //把所有属于当前请求url的元素id字符串拿出后，进行分割成数组，去掉最后一个元素(为空元素)
                                var ids = callingMap[url].split(",").slice(0, -1);
                                //通过元素id，给每个元素显示内容
                                $.each(ids,function(i,eleId){
                                    $('#' + eleId).html(rtn[key]);
                                });
                                //移除url
                                delete callingMap[url];
                            }
                        });
                    }else{
                        //不需要查询，但要记录它的<span>的id
                        if(callingMap[url].indexOf(id + ",") == -1){
                            callingMap[url] += id + ",";
                        }
                    }
                }else{
                    //如果已经有查询过结果值的，则直接显示内容
                    $('#' + id).html(keyValue);
                }
            }
        }
    };
}();