$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'device/deviceloginfo/list',
        datatype: "json",
        colModel: [			
			{ label: 'recnumber', name: 'recnumber', index: 'RecNumber', width: 50, key: true ,hidden:true},
			{ label: '设备唯一标识', name: 'devid', index: 'DevID', width: 80},
			{ label: '日志级别', name: 'loglevel', index: 'LogLevel', width: 50, formatter: loglevelFormat},
			{ label: '日志时间', name: 'logtime', index: 'LogTime', width: 80 ,formatter:dateFormat},
			{ label: '日志内容', name: 'logcontent', index: 'LogContent', width: 80,hidden:true},
            { label: '日志含义', name: 'logmeaning', index: 'logMeaning', width: 80}
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: false,
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

function loglevelFormat(cellvalue, options, rowObject ){
    if(cellvalue== 1){
        return '<span class="label label-success">信息</span>';
    }
    if(cellvalue == 2){
        return '<span class="label label-warning">一般警告</span>';
    }
    if(cellvalue == 3){
        return '<span class="label label-danger">设备警告</span>';
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
        devid:null,
        devInfos:null,//取到的本级设备信息
        allInfo:null,
        dateStart:null,
        dateEnd:null,
		showList:false,
        searchType:0,
        page:{
            list:{},
        },
        ok:true
		//title:null,
		//deviceloginfo:{}
	},
	methods: {
		query: function () {
			vm.reload();
		},
        centerData:function(){
            //document.getElementById("option0").style.visibility="visible";
            document.getElementById("option0").style.display="inline";
		    vm.showList = false;
            vm.dateStart = null;
            vm.dateEnd = null;
        },
        agentData:function(){
            //document.getElementById("option0").style.visibility="hidden";
            document.getElementById("option0").style.display="none";
            vm.showList = true;
            vm.dateStart = null;
            vm.dateEnd = null;
        },
        getLog:function(){
            if (!vm.ok) {
                alert("查询间隔小于3秒！");
                return;//非true退出
            }
            vm.ok = false;
            setTimeout(function () {
                vm.ok = true;
            }, 3000);
            var relog = {
                devid : null,
                dateStart : null,
                dateEnd : null
            }
            relog.devid = vm.devid;

            if(vm.searchType == 0){//查数据库设备日志
                vm.reload();
                // if(relog.devid == vm.allInfo){//查数据库所有日志信息
                //     vm.reload();
                // }
                // else {//查数据库中信息的某一设备日志
                //     vm.reload();
                // }
            }
            else {//查代理设备日志

                if(relog.devid == vm.allInfo){//改为查数据库所有日志信息
                    //vm.searchType = 0;
                    //vm.showList = false;
                    alert('设备查询不支持全部信息！', function(index){
                        vm.reload();
                    });
                    return;
                }
                else {//查代理设备某条设备最新日志信息

                    //relog.dateStart = vm.dateStart.toString().replace(/-/g,"").replace(/:/g,"").replace(/T/g,"");
                    //relog.dateEnd = vm.dateEnd.toString().replace(/-/g,"").replace(/:/g,"").replace(/T/g,"");

                    if(vm.devid == null||vm.dateStart == null||vm.dateEnd == null){
                        alert('请输入完整查询信息！');
                        return;
                    }
                    relog.dateStart = vm.dateStart.toString().replace(/T/g," ");

                    relog.dateEnd = vm.dateEnd.toString().replace(/T/g," ");
                    if(relog.dateStart.length < 19 && relog.dateStart.length >= 16){
                        relog.dateStart = relog.dateStart + ":00";
                    }
                    if(relog.dateEnd.length < 19 && relog.dateStart.length >= 16){
                        relog.dateEnd = relog.dateEnd + ":00";
                    }
                    $.ajax({
                        type:"POST",
                        url:baseURL + "device/deviceloginfo/loglist",
                        contentType: "application/json",
                        data: JSON.stringify(relog),
                        success: function(r){
                            if(r.code == 0){
                                vm.reload();
                            }else{
                                alert(r.msg);
                            }
                        }
                    });
                }
            }
        },
		reload: function (event) {
            var relog = {
                dateStart : null,
                dateEnd : null
            }
            if(vm.dateStart!=null){
                relog.dateStart = vm.dateStart.toString().replace(/T/g," ");
                if(relog.dateStart.length < 19 && relog.dateStart.length >= 16){
                    relog.dateStart = relog.dateStart + ":00";
                }
            }
            if(vm.dateEnd!=null) {
                relog.dateEnd = vm.dateEnd.toString().replace(/T/g, " ");
                if(relog.dateEnd.length < 19 && relog.dateStart.length >= 16){
                    relog.dateEnd = relog.dateEnd + ":00";
                }
            }
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData:{
                    'devid': vm.devid,
                    'dateStart':relog.dateStart,
                    'dateEnd':relog.dateEnd
                },
                page:page,
            }).trigger("reloadGrid");

		}
	}
});
$(function () {
    //关联数据库中deciceinfo表中的sysNumber字段与sys_dept表中的dept_id字段，取二者相同的数据
    //"device/deviceloginfo/devlist"
    $.get(baseURL + "device/deviceloginfo/devlist", function (r) {
        vm.devInfos = r;
    });
});