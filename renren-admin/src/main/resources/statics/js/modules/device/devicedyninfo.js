$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'device/devicedyninfo/list',
        datatype: "json",
        colModel: [			
			{ label: 'recnumber', name: 'recnumber', index: 'RecNumber', width: 50, key: true ,hidden:true},
			{ label: '设备唯一标识', name: 'devid', index: 'DevID', width: 110 },
            {label: '密码服务状态', name: 'encservicestate', index: 'EncServiceState', width: 50, formatter: encservicestateFormat},
            /*{ label: '密钥失步监视', name: 'keyversion', index: 'KeyVersion', width: 80 },
            { label: '密钥更新状态', name: 'keyupdatestate', index: 'KeyUpdateState', width: 80 },
            { label: '隧道联通性', name: 'tunnelconnectivity', index: 'TunnelConnectivity', width: 80 },
            { label: 'IP地址是否变化', name: 'devipstate', index: 'DevIPState', width: 40 ,formatter:devipstateFormat},*/
			{ label: '密钥算法使用率', name: 'algrescontion', index: 'AlgResContion', width: 60 , formatter: algFormat},
			{ label: '密钥资源利用率', name: 'keyrescondition', index: 'KeyResCondition', width: 60, formatter: keyFormat },
			{ label: '设备状态', name: 'devstate', index: 'DevState', width: 40 ,formatter:devstateFormat},
            { label: '系统通知', name: 'notice', index: 'DevState', width: 40 },
            { label: '最近修改时间', name: 'lastmodifytime', index: 'LastModifyTime', width: 60 ,formatter:dateFormat},
			/*{ label: '随机数发生器校验状态', name: 'rngCheck', index: 'RNG_Check', width: 80 },
			{ label: '关键程序校验', name: 'kernelprogcheck', index: 'KernelProgCheck', width: 80 }	*/
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        beforeSelectRow: beforeSelectRow,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
    var myGrid = $("#jqGrid");
    $("#cb_"+myGrid[0].id).hide();
});


function beforeSelectRow() {
    $("#jqGrid").jqGrid('resetSelection');
    return (true);
}

function encservicestateFormat(cellvalue, options, rowObject) {
    if (cellvalue == 0) {
        return '<span class="label label-success">正常</span>';
    }
    if (cellvalue == 1) {
        return '<span class="label label-warning">异常</span>';
    }
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

function devstateFormat(cellvalue, options, rowObject ){
    if(cellvalue== 0){
        return '<span class="label label-success">在线</span>';
    }
    if(cellvalue == 1){
        return '<span class="label label-warning">离线</span>';
    }
    if(cellvalue == 2){
        return '<span class="label label-danger">已毙杀</span>';
    }
}
function devipstateFormat(cellvalue, options, rowObject) {
    if(cellvalue== 0){
        return '<span class="label label-success">未变更</span>';
    }
    if(cellvalue == 1){
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
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		devicedyninfo: {}
	},
	methods: {
        check: function (event) {
            var recnumber = getSelectedRow();
            if(recnumber == null){
                return ;
            }
            vm.showList = false;
            vm.title = "设备动态信息详情";

            vm.getInfo(recnumber)
        },
        refresh: function (event) {
            var recnumber = getSelectedRow();
            if(recnumber == null){
                return ;
            }
            //调用刷新动态数据
            $.get(baseURL + "device/devicedyninfo/info/"+recnumber, function(r){
                $.ajax({
                    type: "POST",
                    url: baseURL + "device/devicedyninfo/mes",
                    contentType: "application/json",
                    data: JSON.stringify(r.devicedyninfo),
                    success: function(r){
                        if(r.code == 0){
                            alert('操作成功', function(index){
                                vm.reload();
                            });
                        }else{
                            alert(r.msg);
                        }
                    }
                });

            });
            //调用刷新动态数据

        },
        getInfo: function(recnumber){
            $.get(baseURL + "device/devicedyninfo/info/"+recnumber, function(r){
                vm.devicedyninfo = r.devicedyninfo;
                var a,b,c;
                var alg = vm.devicedyninfo.algrescontion.toString();
                a = 'Sym_Alg:' + alg.substring(0, 2) + '%,';
                b = 'Asym_Alg:' + alg.substring(2, 4) + '%,';
                c = 'Hash_Alg:' + alg.substring(4) + '%';
                vm.devicedyninfo.algrescontion = (a+b+c);
                var key = vm.devicedyninfo.keyrescondition.toString();
                a = 'Sym_Alg:' + key.substring(0, 2) + '%,';
                b = 'Asym_Alg:' + key.substring(2, 4) + '%,';
                c = 'Hash_Alg:' + parseInt(key.substring(4)).toString() + '‰';
                vm.devicedyninfo.keyrescondition = (a+b+c);
                if(vm.devicedyninfo.devstate == 0){
                    vm.devicedyninfo.devstate = "在线";
                }
                if(vm.devicedyninfo.devstate == 1){
                    vm.devicedyninfo.devstate = "离线";
                }
                if(vm.devicedyninfo.devstate == 2){
                    vm.devicedyninfo.devstate = "已毙杀";
                }
                if (vm.devicedyninfo.keyversion == 0) {
                    vm.devicedyninfo.keyversion = "未失步";
                }
                if (vm.devicedyninfo.keyversion == 1) {
                    vm.devicedyninfo.keyversion = "已失步";
                }
                if (vm.devicedyninfo.encservicestate == 0) {
                    vm.devicedyninfo.encservicestate = "正常";
                }
                if (vm.devicedyninfo.encservicestate == 1) {
                    vm.devicedyninfo.encservicestate = "异常";
                }
                if (vm.devicedyninfo.keyupdatestate == 0) {
                    vm.devicedyninfo.keyupdatestate = "未到更新周期";
                }
                if (vm.devicedyninfo.keyupdatestate == 1) {
                    vm.devicedyninfo.keyupdatestate = "待更新";
                }
                if (vm.devicedyninfo.keyupdatestate == 2) {
                    vm.devicedyninfo.keyupdatestate = "正在更新";
                }
                if (vm.devicedyninfo.keyupdatestate == 3) {
                    vm.devicedyninfo.keyupdatestate = "已更新";
                }
                if (vm.devicedyninfo.keyupdatestate == 4) {
                    vm.devicedyninfo.keyupdatestate = "更新失败";
                }
                if (vm.devicedyninfo.tunnelconnectivity == 0) {
                    vm.devicedyninfo.tunnelconnectivity = "已连通";
                }
                if (vm.devicedyninfo.tunnelconnectivity == 1) {
                    vm.devicedyninfo.tunnelconnectivity = "未连通";
                }
                if (vm.devicedyninfo.devipstate == 0) {
                    vm.devicedyninfo.devipstate = "未变更";
                }
                if (vm.devicedyninfo.devipstate == 1) {
                    vm.devicedyninfo.devipstate = "已变更";
                }
                if (vm.devicedyninfo.rngCheck == 0) {
                    vm.devicedyninfo.rngCheck = "正常";
                }
                if (vm.devicedyninfo.rngCheck == 1) {
                    vm.devicedyninfo.rngCheck = "异常";
                }
                if (vm.devicedyninfo.kernelprogcheck == 0) {
                    vm.devicedyninfo.kernelprogcheck = "正常";
                }
                if (vm.devicedyninfo.kernelprogcheck == 1) {
                    vm.devicedyninfo.kernelprogcheck = "异常";
                }
                var timestamp = new Date(vm.devicedyninfo.lastmodifytime);//直接用 new Date(时间戳) 格式转化获得当前时间
                vm.devicedyninfo.lastmodifytime = timestamp.toLocaleDateString().replace(/\//g, "-") + " " + timestamp.toTimeString().substr(0, 8);
            });
        },
        reload: function (event) {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                page:page
            }).trigger("reloadGrid");
        }
		// query: function () {
		// 	vm.reload();
		// },
		// add: function(){
		// 	vm.showList = false;
		// 	vm.title = "新增";
		// 	vm.devicedyninfo = {};
		// },
		// update: function (event) {
		// 	var recnumber = getSelectedRow();
		// 	if(recnumber == null){
		// 		return ;
		// 	}
		// 	vm.showList = false;
        //     vm.title = "修改";
        //
        //     vm.getInfo(recnumber)
		// },
		// saveOrUpdate: function (event) {
		// 	var url = vm.devicedyninfo.recnumber == null ? "device/devicedyninfo/save" : "device/devicedyninfo/update";
		// 	$.ajax({
		// 		type: "POST",
		// 	    url: baseURL + url,
        //         contentType: "application/json",
		// 	    data: JSON.stringify(vm.devicedyninfo),
		// 	    success: function(r){
		// 	    	if(r.code === 0){
		// 				alert('操作成功', function(index){
		// 					vm.reload();
		// 				});
		// 			}else{
		// 				alert(r.msg);
		// 			}
		// 		}
		// 	});
		// },
		// del: function (event) {
		// 	var recnumbers = getSelectedRows();
		// 	if(recnumbers == null){
		// 		return ;
		// 	}
		//
		// 	confirm('确定要删除选中的记录？', function(){
		// 		$.ajax({
		// 			type: "POST",
		// 		    url: baseURL + "device/devicedyninfo/delete",
        //             contentType: "application/json",
		// 		    data: JSON.stringify(recnumbers),
		// 		    success: function(r){
		// 				if(r.code == 0){
		// 					alert('操作成功', function(index){
		// 						$("#jqGrid").trigger("reloadGrid");
		// 					});
		// 				}else{
		// 					alert(r.msg);
		// 				}
		// 			}
		// 		});
		// 	});
		// },
		// getInfo: function(recnumber){
		// 	$.get(baseURL + "device/devicedyninfo/info/"+recnumber, function(r){
        //         vm.devicedyninfo = r.devicedyninfo;
        //     });
		// },

	}
});