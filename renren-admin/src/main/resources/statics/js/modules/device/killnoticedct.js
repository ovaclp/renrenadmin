$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'device/killnoticedct/list',
        datatype: "json",
        colModel: [			
			{ label: 'recnumber', name: 'recnumber', index: 'RecNumber', width: 50, key: true,hidden:true },
			{ label: '警告消息编码', name: 'notice', index: 'Notice', width: 80 }, 			
			{ label: '警告消息含义', name: 'noticemeans', index: 'NoticeMeans', width: 80 }			
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
		killnoticedct: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.killnoticedct = {};
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
			var url = vm.killnoticedct.recnumber == null ? "device/killnoticedct/save" : "device/killnoticedct/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.killnoticedct),
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
				    url: baseURL + "device/killnoticedct/delete",
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
			$.get(baseURL + "device/killnoticedct/info/"+recnumber, function(r){
                vm.killnoticedct = r.killnoticedct;
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