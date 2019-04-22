
var vm = new Vue({
    el:'#rrapp',
    data:{
        showList: true,
        title: null,
        sysDept: {},
        deptDomainInfos: {},
        dept:{
            parentName:null,
            parentId:0,
            orderNum:0
        }
    },
    methods: {
        add: function(){
            vm.showList = false;
            vm.title = "注册";
        },
        update: function () {
            var recnumber = getDeptId();
            console.log("recnumber:");
            console.log(recnumber);
            if(recnumber == null){
                return ;
            }
            $.get(baseURL + "sys/dept/info/"+recnumber, function(r){
                vm.showList = false;
                vm.title = "修改";
                console.log("r.dept:");
                console.log(r.dept);
                vm.sysDept = r.dept;
            });
        },
        saveOrUpdate: function (event) {
            var url = "sys/dept/update";
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.sysDept),
                success: function(r){
                    if(r.code === 0){
                        alert('修改成功', function(){
                            vm.reload();
                        });
                    }else{
                        alert(r.msg);
                    }
                }
            });
        },
        reload: function () {
            vm.showList = true;
            vm.sysDept = {}; //置空
            Dept.table.refresh();
        }
    }
});

var Dept = {
    id: "deptTable",
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Dept.initColumn = function () {
    var columns = [
        {field: 'selectItem', radio: true},
        // {title: '记录编号', field: 'recnumber', visible: false, align: 'center', valign: 'middle', width: '60px'},
        {field: 'recnumber', width: '0px'},
        //{title: '监管中心ID', field: 'deptId', align: 'center', valign: 'middle', width: '80px'},
        {title: '监控中心名称', field: 'name', align: 'center', valign: 'middle', sortable: true, width: '100px'},
        {title: '监控中心地址', field: 'ipAddr', align: 'center', valign: 'middle', sortable: true, width: '100px'},
        {title: '监控中心唯一标识', field: 'centerId', align: 'center', valign: 'middle', sortable: true, width: '180px'},
        {title: '上级监管中心', field: 'parentName', align: 'center', valign: 'middle', sortable: true, width: '100px'},
        //{title: '管理代理端口', field: 'agentPort', align: 'center', valign: 'middle', sortable: true, width: '100px'},
        {title: '是否本级', field: 'isCurrent', align: 'center', valign: 'middle', sortable: true, width: '60px', formatter: function(item, index){
            if(item.isCurrent === 0){
                return '<span class="label label-primary">否</span>';
            }
            if(item.isCurrent === 1){
                return '<span class="label label-success">是</span>';
            }
        }}]
    return columns;
};


function getDeptId () {
    var selected = $('#deptTable').bootstrapTreeTable('getSelections');
    if (selected.length == 0) {
        alert("请选择一条记录");
        return null;
    } else {
        return selected[0].id;//取id域(recnumber)内的数据
    }
}


$(function () {
    $.get(baseURL + "sys/dept/info", function(r){
        var colunms = Dept.initColumn();
        var table = new TreeTable(Dept.id, baseURL + "sys/dept/list", colunms);
        table.setRootCodeValue(r.deptId);//设置根节点
        table.setExpandColumn(2);// 在哪一列上面显示展开按钮
        table.setIdField("recnumber");//设置ID域
        table.setCodeField("deptId");//
        table.setParentCodeField("parentId");//
        table.setExpandAll(true);// 是否全部展开
        table.init();//初始化
        Dept.table = table;//
    });
    //取数据库中Detpdomainnumdct表内的deptDomainnum和deptDomain两列的值存入vm.deptDomainInfos
    $.get(baseURL + "device/detpdomainnumdct/list", function (r) {
        vm.deptDomainInfos = r.page.list;
    });
});
