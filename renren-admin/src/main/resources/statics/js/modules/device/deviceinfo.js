$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'device/deviceinfo/list',
        datatype: "json",
        colModel: [
            {label: 'recnumber', name: 'recnumber', index: 'RecNumber', width: 50, hidden: true, key: true},
            {label: '设备唯一标识', name: 'deviceid', index: 'DeviceID', width: 85},
            //{ label: '系统版本号', name: 'deviceversion', index: 'DeviceVersion', width: 80 },
            //{ label: '管理协议版本号', name: 'standardversion', index: 'StandardVersion', width: 80 },
            // { label: '非对称算法', name: 'asymalgability0', index: 'AsymAlgAbility0', width: 80 },
            // { label: '非对称算法', name: 'asymalgability1', index: 'AsymAlgAbility1', width: 80 },
            // { label: '杂凑算法', name: 'hashalgability', index: 'HashAlgAbility', width: 80 },
            // { label: '对称算法', name: 'symalgability', index: 'SymAlgAbility', width: 80 },
            {label: '设备IP地址', name: 'deviceipv4', index: 'DeviceIPV4', width: 45},
            {label: '设备名称', name: 'devicename', index: 'DeviceName', width: 45},
            {label: '设备编号/序列号', name: 'deviceserial', index: 'DeviceSerial', width: 60},
            // { label: '设备生产厂商名称', name: 'issuername', index: 'IssuerName', width: 100 },
            {label: '设备型号', name: 'devicetype', index: 'DeviceType', width: 60},
            {label: '最近修改时间', name: 'lastmodifytime', index: 'LastModifyTime', width: 50, formatter: dateFormat}
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
    $("#cb_" + myGrid[0].id).hide();//隐藏全选框
    /* setInterval(function(){
         var page = $("#jqGrid").jqGrid('getGridParam','page');
         $("#jqGrid").jqGrid('setGridParam',{
             page:page
         }).trigger("reloadGrid");
     }, 8000);*/
});


function beforeSelectRow() {
    $("#jqGrid").jqGrid('resetSelection');
    return (true);
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
        deviceinfo: {},
        ok: true
    },
    methods: {
        check: function (event) {
            var recnumber = getSelectedRow();
            if (recnumber == null) {
                return;
            }
            vm.showList = false;
            vm.title = "设备基本信息详情";

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

            $.get(baseURL + "device/deviceinfo/info/" + recnumber, function (r) {
                $.ajax({
                    type: "POST",
                    url: baseURL + "device/deviceinfo/mes",
                    contentType: "application/json",
                    data: JSON.stringify(r.deviceinfo),
                    success: function (r) {
                        if (r.code == 0) {
                            alert('操作成功', function (index) {
                                // $("#jqGrid").trigger("reloadGrid");
                                vm.reload();
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });

            });
            //调用刷新静态数据

        },
        getInfo: function (recnumber) {
            $.get(baseURL + "device/deviceinfo/info/" + recnumber, function (r) {
                vm.deviceinfo = r.deviceinfo;
                var timestamp = new Date(vm.deviceinfo.regdate);//直接用 new Date(时间戳) 格式转化获得当前时间
                vm.deviceinfo.regdate = timestamp.toLocaleDateString().replace(/\//g, "-") + " " + timestamp.toTimeString().substr(0, 8);
                //翻译支持算法
                if (vm.deviceinfo.asymalgability0 == 198656) {
                    vm.deviceinfo.asymalgability0 = "Alg:SGD_RSA/Pub_Key:1024/Pri_Key:1024";
                }
                if (vm.deviceinfo.asymalgability0 == 196864) {
                    vm.deviceinfo.asymalgability0 = "Alg:SGD_RSA/Pub_Key:2048/Pri_Key:2048";
                }
                if (vm.deviceinfo.asymalgability0 == 133632) {
                    vm.deviceinfo.asymalgability0 = "Alg:SGD_SM2_1/Pub_Key:512/Pri_Key:512";
                }
                if (vm.deviceinfo.asymalgability1 == 12663) {
                    vm.deviceinfo.asymalgability1 = "Alg:SGD_SM2/Pub_Key:512/Pri_Key:256";
                }
                if (vm.deviceinfo.asymalgability1 == 65399) {
                    vm.deviceinfo.asymalgability1 = "Alg:SGD_SM2_3/Pub_Key:512/Pri_Key:256";
                }
                if (vm.deviceinfo.asymalgability1 == 13105) {
                    vm.deviceinfo.asymalgability1 = "Alg:SGD_SM2_3/Pub_Key:512/Pri_Key:256";
                }
                if (vm.deviceinfo.symalgability == 1282) {
                    vm.deviceinfo.symalgability = "SGD_SM1_CBC；SGD_SM4_CBC";
                }
                if (vm.deviceinfo.symalgability == 16844033) {
                    vm.deviceinfo.symalgability = "SGD_SM1_ECB；SGD_SM4_ECB；IGD_AES_ECB";
                }
                if (vm.deviceinfo.symalgability == 1795) {
                    vm.deviceinfo.symalgability = "SGD_SM1_ECB；SGD_SM1_CBC；SGD_SSF33_ECB；SGD_SSF33_CBC；SGD_SM4_ECB；SGD_SM4_CBC";
                }
                if (vm.deviceinfo.hashalgability == 5) {
                    vm.deviceinfo.hashalgability = "SGD_SM3；SGD_SHA256";
                }
                if (vm.deviceinfo.hashalgability == 7) {
                    vm.deviceinfo.hashalgability = "SGD_SM3；SGD_SHA1；SGD_SHA256";
                }
                if (vm.deviceinfo.hashalgability == 37) {
                    vm.deviceinfo.hashalgability = "SGD_SM3；SGD_SHA256；SGD_MD5";
                }

            });
        },
        //导出设备静态信息
        outputInfo: function () {
            var recnumber = getSelectedRow();
            if (recnumber == null) {
                return;
            }
            $.get(baseURL + "device/deviceinfo/info/" + recnumber, function (r) {
                var mes = r.deviceinfo;
                var timestamp = new Date(mes.regdate);//直接用 new Date(时间戳) 格式转化获得当前时间
                mes.regdate = timestamp.toLocaleDateString().replace(/\//g, "-") + " " + timestamp.toTimeString().substr(0, 8);
                //翻译支持算法
                if (mes.asymalgability0 == 198656) {
                    mes.asymalgability0 = "Alg:SGD_RSA/Pub_Key:1024/Pri_Key:1024";
                }
                if (mes.asymalgability0 == 196864) {
                    mes.asymalgability0 = "Alg:SGD_RSA/Pub_Key:2048/Pri_Key:2048";
                }
                if (mes.asymalgability0 == 133632) {
                    mes.asymalgability0 = "Alg:SGD_SM2_1/Pub_Key:512/Pri_Key:512";
                }
                if (mes.asymalgability1 == 12663) {
                    mes.asymalgability1 = "Alg:SGD_SM2/Pub_Key:512/Pri_Key:256";
                }
                if (mes.asymalgability1 == 65399) {
                    mes.asymalgability1 = "Alg:SGD_SM2_3/Pub_Key:512/Pri_Key:256";
                }
                if (mes.asymalgability1 == 13105) {
                    mes.asymalgability1 = "Alg:SGD_SM2_3/Pub_Key:512/Pri_Key:256";
                }
                if (mes.symalgability == 1282) {
                    mes.symalgability = "SGD_SM1_CBC；SGD_SM4_CBC";
                }
                if (mes.symalgability == 16844033) {
                    mes.symalgability = "SGD_SM1_ECB；SGD_SM4_ECB；IGD_AES_ECB";
                }
                if (mes.symalgability == 1795) {
                    mes.symalgability = "SGD_SM1_ECB；SGD_SM1_CBC；SGD_SSF33_ECB；SGD_SSF33_CBC；SGD_SM4_ECB；SGD_SM4_CBC";
                }
                if (mes.hashalgability == 5) {
                    mes.hashalgability = "SGD_SM3；SGD_SHA256";
                }
                if (mes.hashalgability == 7) {
                    mes.hashalgability = "SGD_SM3；SGD_SHA1；SGD_SHA256";
                }
                if (mes.hashalgability == 37) {
                    mes.hashalgability = "SGD_SM3；SGD_SHA256；SGD_MD5";
                }
                var blob = new Blob([JSON.stringify(mes)], {type: "text/plain;charset=utf-8"});
                saveAs(blob, "deviceInfo.txt");
            });

        },
        kill: function () {
            var recnumber = getSelectedRow();
            if (recnumber == null) {
                return;
            }
            confirm('确定要毙杀选中的设备？', function () {
                $.ajax({
                    type: "POST",
                    url: baseURL + "device/deviceinfo/kill",
                    data: "recnumber=" + recnumber,
                    success: function (r) {
                        if (r.code === 0) {
                            alert('操作成功', function () {
                                vm.reload();
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        reload: function (event) {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                page: page
            }).trigger("reloadGrid");
        }
    }
});