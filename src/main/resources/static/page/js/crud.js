//提交的方法名称
var method = "";
var ty = "post";
//静态传参需要的参数
var listParam = "";
var saveParam = "";
$(function () {

    //初始化文件上传窗口
    $("#uploadDlg").dialog({
        title: '数据导入',
        width: 300,
        height: 150,
        closed: true,
        cache: false,
        modal: true,
        buttons: [{
            text: '导入',
            iconCls: 'icon-ok',
            handler: function () {
                //获得文件上传表单
                $.ajax({
                    url: "supplier_doImport",
                    data: new FormData($('#fileUploadForm')[0]),
                    dataType: "json",
                    type: "post",
                    processData: false,//告诉jquery，发送的是字节流，不能转成字符集
                    contentType: false,
                    success: function (rtn) {
                        $.messager.alert("提示", rtn.message, "info");
                        //刷新列表
                        $("#uploadDlg").dialog("close");
                        $("#grid").datagrid("reload");
                    }
                });
            }
        }, {
            text: '取消',
            iconCls: 'icon-cancel',
            handler: function () {
                $("#uploadDlg").dialog("close");
            }
        }]
    });

    //加载表格数据
    $('#grid').datagrid({
        url: "/admin/" + name + '/listWithoutState' + listParam,
        columns: columns,
        singleSelect: true,
        pagination: true,
        toolbar: [{
            text: '新增',
            iconCls: 'icon-add',
            handler: function () {
                //设置保存按钮提交的方法为add
                method = "add";
                $('#editForm').form('clear');            //更改过
                //关闭编辑窗口
                $('#editDlg').dialog('open');            //更改过
            }
        },
            {
                text: '导出',
                iconCls: 'icon-excel',
                handler: function () {
                    $.messager.confirm("提示", "确定将数据导出吗?", function (yes) {
                        if (yes) {
                            //把表单数据转换成json对象
                            var formData = $('#searchForm').serializeJSON();
                            formData['t1.type'] = type;
                            $.download("supplier_export", formData);
                        }
                    });
                }
            }, '-', {
                text: '导入',
                iconCls: 'icon-edit',
                handler: function () {
                    $("#uploadDlg").dialog("open");
                }
            }],
        //迷糊查询自动提示
        mode: "remote",
        //双击显示详情
        onDblClickCell: function (rowIndex, field, value) {
            if (undefined == value || null == value) {
                return;
            }
            $.messager.alert('详情', "<textarea style='width: 100%;height: 130px'>" + value + "</textarea>");
        }
    });

    //点击查询按钮
    $('#btnSearch').bind('click', function () {
        //把表单数据转换成json对象
        var formData = $('#searchForm').serializeJSON();
        $('#grid').datagrid('load', formData);
    });

    $('#btnReset').bind('click', function () {
        $("#searchForm").form("reset");
    });

    //初始化编辑窗口
    $('#editDlg').dialog({
        title: '编辑',//窗口标题
        width: 480,//窗口宽度
        height: 450,
        closed: true,//窗口是是否为关闭状态, true：表示关闭
        modal: true,//模式窗口
        buttons: [{
            text: '保存',
            iconCls: 'icon-save',
            handler: function () {
                //用记输入的部门信息
                var formData = new FormData($("#editForm")[0]);
                $.ajax({
                    url: '/admin/' + name + '/' + method,
                    data: formData,
                    type: ty,
                    async: false,
                    cache: false,
                    contentType: false,
                    processData: false,
                    success: function (rtn) {
                        //{success:true, message: 操作失败}
                        $.messager.alert('提示', rtn.message, 'info', function () {
                            // if (rtn.success) {
                            //关闭弹出的窗口
                            $('#editDlg').dialog('close');
                            //刷新表格
                            $('#grid').datagrid('reload');
                            // }
                        });
                    }
                });
            }
        }, {
            text: '关闭',
            iconCls: 'icon-cancel',
            handler: function () {
                //关闭弹出的窗口
                $('#editDlg').dialog('close');
            }
        }]
    });

});

//检查是否登录
function checkisLogin() {
    $.ajax({
        url: "user_isLogin",
        dataType: "json",
        type: "post",
        success: function (rtn) {
            if (!rtn.success) {
                location.href = "login.html";
            }
        }
    });
}


/**
 * 删除
 */
function del(id) {
    $.messager.confirm("确认", "确认要删除吗？", function (yes) {
        if (yes) {
            $.ajax({
                url: "/admin/" + name + '/delete?id=' + id,
                dataType: 'json',
                type: 'post',
                success: function (rtn) {
                    $.messager.alert("提示", rtn.message, 'info', function () {
                        //刷新表格数据
                        $('#grid').datagrid('reload');
                    });
                }
            });
        }
    });
}

/**
 * 修改
 */
function edit(id) {
    //弹出窗口
    $('#editDlg').dialog('open');

    //清空表单内容
    $('#editForm').form('clear');

    //设置保存按钮提交的方法为update
    method = "update";

    ty = "post";

    //加载数据
    $('#editForm').form('load', '/admin/' + name + '/findById/' + id);
}

