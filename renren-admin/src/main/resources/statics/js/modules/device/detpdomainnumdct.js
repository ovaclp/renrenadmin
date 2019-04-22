$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'device/detpdomainnumdct/list',
        datatype: "json",
        colModel: [			
			{ label: 'recnumber', name: 'recnumber', index: 'RecNumber', width: 50, key: true ,hidden:true},
			{ label: '监控中心所属域编号', name: 'deptDomainnum', index: 'dept_domainnum', width: 80 }, 			
			{ label: '监控中心所属域', name: 'deptDomain', index: 'dept_domain', width: 80 }			
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
		detpdomainnumdct: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.detpdomainnumdct = {};
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
			var url = vm.detpdomainnumdct.recnumber == null ? "device/detpdomainnumdct/save" : "device/detpdomainnumdct/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.detpdomainnumdct),
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
				    url: baseURL + "device/detpdomainnumdct/delete",
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
			$.get(baseURL + "device/detpdomainnumdct/info/"+recnumber, function(r){
                vm.detpdomainnumdct = r.detpdomainnumdct;
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