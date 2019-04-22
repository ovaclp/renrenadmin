$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'device/devicedyninfonew/list',
        datatype: "json",
        colModel: [
            {label: 'recnumber', name: 'recnumber', index: 'RecNumber', width: 50, key: true, hidden: true},
            {label: '设备唯一标识', name: 'devid', index: 'DevID', width: 120},
            {label: '密码服务状态', name: 'encservicestate', index: 'EncServiceState', width: 50, formatter: encservicestateFormat},
            {label: '密钥失步监视', name: 'keyversion', index: 'KeyVersion', width: 80, hidden: true},
            {label: '密钥更新状态', name: 'keyupdatestate', index: 'KeyUpdateState', width: 80, hidden: true},
            {label: '隧道联通性', name: 'tunnelconnectivity', index: 'TunnelConnectivity', width: 80, hidden: true},
            {label: 'IP地址是否变化', name: 'devipstate', index: 'DevIPState', width: 50, formatter: devipstateFormat, hidden: true},
            {label: '密钥算法使用率', name: 'algrescontion', index: 'AlgResContion', width: 50, formatter: algFormat},
            {label: '密钥资源利用率', name: 'keyrescondition', index: 'KeyResCondition', width: 50, formatter: keyFormat},
            {label: '设备状态', name: 'devstate', index: 'DevState', width: 40, formatter: devstateFormat},
            {label: '随机数发生器校验状态', name: 'rngCheck', index: 'RNG_Check', width: 80, hidden: true},
            {label: '关键程序校验', name: 'kernelprogcheck', index: 'KernelProgCheck', width: 80, hidden: true},
            {label: '系统通知', name: 'notice', index: 'Notice', width: 50},
            {label: '最近修改时间', name: 'lastmodifytime', index: 'LastModifyTime', width: 60, formatter: dateFormat}
        ],
        viewrecords: true,
        height: 385,
        rowNum: 10,
        rowList: [10, 30, 50],
        rownumbers: true,
        rownumWidth: 25,
        autowidth: true,
        multiselect: true,
        beforeSelectRow: beforeSelectRow,
        pager: "#jqGridPager",
        jsonReader: {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order"
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });
    var myGrid = $("#jqGrid");
    $("#cb_"+myGrid[0].id).hide();//隐藏全选框
    /*setInterval(function(){
        var page = $("#jqGrid").jqGrid('getGridParam', 'page');
        $("#jqGrid").jqGrid('setGridParam', {
            page: page
        }).trigger("reloadGrid");
    }, 8000);*/
});


function beforeSelectRow() {
    $("#jqGrid").jqGrid('resetSelection');
    return (true);
}
function algFormat(cellvalue, options, rowObject) {
    var a, b, c;
    a = 'Sym_Alg:' + cellvalue.toString().substring(0, 2) + '%,';
    b = 'Asym_Alg:' + cellvalue.toString().substring(2, 4) + '%,';
    c = 'Hash_Alg:' + cellvalue.toString().substring(4) + '%';
    return (a + '\n'+b +'\n'+ c);
}

function keyFormat(cellvalue, options, rowObject) {
    var a, b, c;
    a = 'Sym_Alg:' + cellvalue.toString().substring(0, 2) + '%,';
    b = 'Asym_Alg:' + cellvalue.toString().substring(2, 4) + '%,';
    c = 'Hash_Alg:' + parseInt(cellvalue.toString().substring(4)).toString() + '‰';
    return (a +'\n'+ b +'\n'+ c);
}

function devstateFormat(cellvalue, options, rowObject) {
    if (cellvalue == 0) {
        return '<span class="label label-success">在线</span>';
    }
    if (cellvalue == 1) {
        return '<span class="label label-warning">离线</span>';
    }
    if (cellvalue == 2) {
        return '<span class="label label-danger">已毙杀</span>';
    }
}

function encservicestateFormat(cellvalue, options, rowObject) {
    if (cellvalue == 0) {
        return '<span class="label label-success">正常</span>';
    }
    if (cellvalue == 1) {
        return '<span class="label label-warning">异常</span>';
    }
}
function devipstateFormat(cellvalue, options, rowObject) {
    if (cellvalue == 0) {
        return '<span class="label label-success">未变更</span>';
    }
    if (cellvalue == 1) {
        return '<span class="label label-warning">已变更</span>';
    }
}

function dateFormat(cellvalue, options, rowObject) {
    var timestamp = new Date(cellvalue);//直接用 new Date(时间戳) 格式转化获得当前时间
    //console.log(timestamp);1531726200000
    //console.log(timestamp.toLocaleDateString().replace(/\//g, "-") + " " + timestamp.toTimeString().substr(0, 8));
    return timestamp.toLocaleDateString().replace(/\//g, "-") + " " + timestamp.toTimeString().substr(0, 8);
}


var vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        title: null,
        devicedyninfonew: {},
        ok:true
    },
    methods: {
        check: function (event) {
            var recnumber = getSelectedRow();
            if (recnumber == null) {
                return;
            }
            vm.showList = false;
            vm.title = "设备动态信息详情";

            vm.getInfo(recnumber)
        },
        refresh: function (event) {
            var recnumber = getSelectedRow();
            if (recnumber == null) {
                return;
            }
            if (!vm.ok) {
                alert("查询间隔小于3秒！");
                return;//非true退出
            }
            vm.ok = false;
            setTimeout(function () {
                vm.ok = true;
            }, 3000);

            //调用刷新动态数据
            $.get(baseURL + "device/devicedyninfonew/info/" + recnumber, function (r) {
                $.ajax({
                    type: "POST",
                    url: baseURL + "device/devicedyninfo/mes",
                    contentType: "application/json",
                    data: JSON.stringify(r.devicedyninfonew),
                    success: function (r) {
                        if (r.code == 0) {
                            alert('操作成功', function (index) {
                                vm.reload();
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });

            });
            //调用刷新动态数据

        },
        getInfo: function (recnumber) {
            $.get(baseURL + "device/devicedyninfonew/info/" + recnumber, function (r) {
                vm.devicedyninfonew = r.devicedyninfonew;
                var a,b,c;
                var alg = vm.devicedyninfonew.algrescontion.toString();
                a = 'Sym_Alg:' + alg.substring(0, 2) + '%,';
                b = 'Asym_Alg:' + alg.substring(2, 4) + '%,';
                c = 'Hash_Alg:' + alg.substring(4) + '%';
                vm.devicedyninfonew.algrescontion = (a+b+c);
                var key = vm.devicedyninfonew.keyrescondition.toString();
                a = 'Sym_Alg:' + key.substring(0, 2) + '%,';
                b = 'Asym_Alg:' + key.substring(2, 4) + '%,';
                c = 'Hash_Alg:' + parseInt(key.substring(4)).toString() + '‰';
                vm.devicedyninfonew.keyrescondition = (a+b+c);
                if (vm.devicedyninfonew.devstate == 0) {
                    vm.devicedyninfonew.devstate = "在线";
                }
                if (vm.devicedyninfonew.devstate == 1) {
                    vm.devicedyninfonew.devstate = "离线";
                }
                if (vm.devicedyninfonew.devstate == 2) {
                    vm.devicedyninfonew.devstate = "已毙杀";
                }
                if (vm.devicedyninfonew.keyversion == 0) {
                    vm.devicedyninfonew.keyversion = "未失步";
                }
                if (vm.devicedyninfonew.keyversion == 1) {
                    vm.devicedyninfonew.keyversion = "已失步";
                }
                if (vm.devicedyninfonew.encservicestate == 0) {
                    vm.devicedyninfonew.encservicestate = "正常";
                }
                if (vm.devicedyninfonew.encservicestate == 1) {
                    vm.devicedyninfonew.encservicestate = "异常";
                }
                if (vm.devicedyninfonew.keyupdatestate == 0) {
                    vm.devicedyninfonew.keyupdatestate = "未到更新周期";
                }
                if (vm.devicedyninfonew.keyupdatestate == 1) {
                    vm.devicedyninfonew.keyupdatestate = "待更新";
                }
                if (vm.devicedyninfonew.keyupdatestate == 2) {
                    vm.devicedyninfonew.keyupdatestate = "正在更新";
                }
                if (vm.devicedyninfonew.keyupdatestate == 3) {
                    vm.devicedyninfonew.keyupdatestate = "已更新";
                }
                if (vm.devicedyninfonew.keyupdatestate == 4) {
                    vm.devicedyninfonew.keyupdatestate = "更新失败";
                }
                if (vm.devicedyninfonew.tunnelconnectivity == 0) {
                    vm.devicedyninfonew.tunnelconnectivity = "已连通";
                }
                if (vm.devicedyninfonew.tunnelconnectivity == 1) {
                    vm.devicedyninfonew.tunnelconnectivity = "未连通";
                }
                if (vm.devicedyninfonew.devipstate == 0) {
                    vm.devicedyninfonew.devipstate = "未变更";
                }
                if (vm.devicedyninfonew.devipstate == 1) {
                    vm.devicedyninfonew.devipstate = "已变更";
                }
                if (vm.devicedyninfonew.rngCheck == 0) {
                    vm.devicedyninfonew.rngCheck = "正常";
                }
                if (vm.devicedyninfonew.rngCheck == 1) {
                    vm.devicedyninfonew.rngCheck = "异常";
                }
                if (vm.devicedyninfonew.kernelprogcheck == 0) {
                    vm.devicedyninfonew.kernelprogcheck = "正常";
                }
                if (vm.devicedyninfonew.kernelprogcheck == 1) {
                    vm.devicedyninfonew.kernelprogcheck = "异常";
                }

                var timestamp = new Date(vm.devicedyninfonew.lastmodifytime);//直接用 new Date(时间戳) 格式转化获得当前时间
                vm.devicedyninfonew.lastmodifytime = timestamp.toLocaleDateString().replace(/\//g, "-") + " " + timestamp.toTimeString().substr(0, 8);
            });
        },
        outputInfo: function () {
            var recnumber = getSelectedRow();
            if (recnumber == null) {
                return;
            }
            $.get(baseURL + "device/devicedyninfonew/info/" + recnumber, function (r) {
                var mes = r.devicedyninfonew;
                console.log(mes);
                var a,b,c;
                var alg = mes.algrescontion.toString();
                a = 'Sym_Alg:' + alg.substring(0, 2) + '%,';
                b = 'Asym_Alg:' + alg.substring(2, 4) + '%,';
                c = 'Hash_Alg:' + alg.substring(4) + '%';
                mes.algrescontion = (a+b+c);
                var key = mes.keyrescondition.toString();
                a = 'Sym_Alg:' + key.substring(0, 2) + '%,';
                b = 'Asym_Alg:' + key.substring(2, 4) + '%,';
                c = 'Hash_Alg:' + parseInt(key.substring(4)).toString() + '‰';
                mes.keyrescondition = (a+b+c);
                if (mes.devstate == 0) {
                    mes.devstate = "在线";
                }
                if (mes.devstate == 1) {
                    mes.devstate = "离线";
                }
                if (mes.devstate == 2) {
                    mes.devstate = "已毙杀";
                }
                if (mes.keyversion == 0) {
                    mes.keyversion = "未失步";
                }
                if (mes.keyversion == 1) {
                    mes.keyversion = "已失步";
                }
                if (mes.encservicestate == 0) {
                    mes.encservicestate = "正常";
                }
                if (mes.encservicestate == 1) {
                    mes.encservicestate = "异常";
                }
                if (mes.keyupdatestate == 0) {
                    mes.keyupdatestate = "未到更新周期";
                }
                if (mes.keyupdatestate == 1) {
                    mes.keyupdatestate = "待更新";
                }
                if (mes.keyupdatestate == 2) {
                    mes.keyupdatestate = "正在更新";
                }
                if (mes.keyupdatestate == 3) {
                    mes.keyupdatestate = "已更新";
                }
                if (mes.keyupdatestate == 4) {
                    mes.keyupdatestate = "更新失败";
                }
                if (mes.tunnelconnectivity == 0) {
                    mes.tunnelconnectivity = "已连通";
                }
                if (mes.tunnelconnectivity == 1) {
                    mes.tunnelconnectivity = "未连通";
                }
                if (mes.devipstate == 0) {
                    mes.devipstate = "未变更";
                }
                if (mes.devipstate == 1) {
                    mes.devipstate = "已变更";
                }
                if (mes.rngCheck == 0) {
                    mes.rngCheck = "正常";
                }
                if (mes.rngCheck == 1) {
                    mes.rngCheck = "异常";
                }
                if (mes.kernelprogcheck == 0) {
                    mes.kernelprogcheck = "正常";
                }
                if (mes.kernelprogcheck == 1) {
                    mes.kernelprogcheck = "异常";
                }
                var timestamp = new Date(mes.lastmodifytime);//直接用 new Date(时间戳) 格式转化获得当前时间
                mes.lastmodifytime = timestamp.toLocaleDateString().replace(/\//g, "-") + " " + timestamp.toTimeString().substr(0, 8);
                var blob = new Blob([JSON.stringify(mes)], {type: "text/plain;charset=utf-8"});
                saveAs(blob, "devicedyInfonew.txt");
            });


        },
        reload: function (event) {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                page: page
            }).trigger("reloadGrid");
        },
        /*query: function () {
            vm.reload();
        },
        add: function(){
            vm.showList = false;
            vm.title = "新增";
            vm.devicedyninfonew = {};
        },
        update: function (event) {
            var recnumber = getSelectedRow();
            if(recnumber == null){
                return ;
            }
            vm.showList = false;
            vm.title = "修改";

            vm.getInfo(recnumber)
        },
        saveOrUpdate: function (event) {
            var url = vm.devicedyninfonew.recnumber == null ? "device/devicedyninfonew/save" : "device/devicedyninfonew/update";
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.devicedyninfonew),
                success: function(r){
                    if(r.code === 0){
                        alert('操作成功', function(index){
                            vm.reload();
                        });
                    }else{
                        alert(r.msg);
                    }
                }
            });
        },
        del: function (event) {
            var recnumbers = getSelectedRows();
            if(recnumbers == null){
                return ;
            }

            confirm('确定要删除选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: baseURL + "device/devicedyninfonew/delete",
                    contentType: "application/json",
                    data: JSON.stringify(recnumbers),
                    success: function(r){
                        if(r.code == 0){
                            alert('操作成功', function(index){
                                $("#jqGrid").trigger("reloadGrid");
                            });
                        }else{
                            alert(r.msg);
                        }
                    }
                });
            });
        },*/

    }
});