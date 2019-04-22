$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'device/agentinfo/list',
        datatype: "json",
        colModel: [			
			{ label: 'recnumber', name: 'recnumber', index: 'RecNumber', width: 50, key: true ,hidden:true},
			{ label: '管理代理唯一标识', name: 'agentid', index: 'AgentID', width: 80 }, 			
			{ label: '管理代理的IP地址', name: 'agentip', index: 'AgentIP', width: 80 }, 			
			{ label: '管理代理服务器端口号', name: 'agentport', index: 'AgentPort', width: 80 }, 			
			{ label: '所属监控中心唯一标识', name: 'centerid', index: 'CenterId', width: 80 }			
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

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		agentinfo: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.agentinfo = {};
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
			var url = vm.agentinfo.recnumber == null ? "device/agentinfo/save" : "device/agentinfo/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.agentinfo),
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
				    url: baseURL + "device/agentinfo/delete",
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
			$.get(baseURL + "device/agentinfo/info/"+recnumber, function(r){
                vm.agentinfo = r.agentinfo;
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