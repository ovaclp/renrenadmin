$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'device/devicereginfo/list',
        datatype: "json",
        colModel: [			
			{ label: '序号', name: 'recnumber', index: 'RecNumber', width: 50, key: true,hidden:true },
			{ label: '设备唯一标识', name: 'devid', index: 'DevID', width: 80 }, 			
			{ label: '生产厂商名称', name: 'issuername', index: 'IssuerName', width: 80 },
			{ label: '设备型号', name: 'devname', index: 'DevName', width: 80 }, 			
			{ label: '设备编号', name: 'devserial', index: 'DevSerial', width: 80 }, 			
			{ label: '设备证书', name: 'devcertificate', index: 'DevCertificate', width: 80 }, 			
			{ label: '管理员身份证', name: 'adminorid', index: 'AdminorID', width: 80,hidden:true },
			{ label: '联系电话', name: 'adminorphone', index: 'AdminorPhone', width: 80,hidden:true },
			{ label: '监管中心编号', name: 'sysnumber', index: 'SysNumber', width: 80 },
			{ label: '使用单位编号', name: 'devdepartment', index: 'DevDepartment', width: 80 },
			{ label: '注册日期', name: 'regdate', index: 'RegDate', width: 80 }			
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
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
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		devicereginfo: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.devicereginfo = {};
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
			var url = vm.devicereginfo.recnumber == null ? "device/devicereginfo/save" : "device/devicereginfo/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.devicereginfo),
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
				    url: baseURL + "device/devicereginfo/delete",
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
		},
		getInfo: function(recnumber){
			$.get(baseURL + "device/devicereginfo/info/"+recnumber, function(r){
                vm.devicereginfo = r.devicereginfo;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		}
	}
});