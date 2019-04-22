var setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "menuId",
            pIdKey: "parentId",
            rootPId: -1
        },
        key: {
            url: "nourl"
        }
    }
};
var ztree;

var vm = new Vue({
    el: '#rrapp',
    data: {
        /*定义循环初始值*/
        devtypeinfos: null,//所有设备型号,从数据库获取形成下拉菜单
        devicedeptinfos: null,//所有设备使用单位,从数据库获取形成下拉菜单
        agentinfos: null,//所有代理信息，从数据库获取形成下拉菜单
        /*定义select选中值*/
        devtypeinfo: {},//单条设备型号
        devicedeptinfo: {},//单条设备使用单位
        agentinfo: {
            agentPort: 7000,
            centerid: "00000000",
        },//单条代理信息

        inputType: null,//是否手动输入代理信息
        showList: true,//注册设备
        showAgent: false,//手动注册代理
        showSelect: false,//选择代理
        showButton: true,//手动注册代理按钮部分
        isbuildCertificate: false,//设备证书是否生成
        isDevid: false,//设备唯一标识是否生成
        isAgentid: false,//代理唯一标识是否生成
        isAgent: false,//代理信息是否已经填写或选择
        isOutputInfo: false,//注册信息是否已导出
        title: null,
        titleAgent: "代理信息",
        //设备注册信息
        devicereginfo: {
            sysnumber: "00000000",
            agentnum: null,
        },
        sysdept: {},//本级监控中心数据
        menu: {
            parentName: null,
            parentId: 0,
            type: 1,
            orderNum: 0
        }
    },
    methods: {
        report: function () {
            //导出设备所有信息
            var devid = getMenuId();
            if (devid == null) {
                return;
            }
            console.log("devid:");
            console.log(devid);
            // $.get(baseURL + "//info/"+devid, function(r){
            //     vm.menu = r.menu;
            // });
        },
        getMenu: function (menuId) {
            //加载菜单树
            $.get(baseURL + "sys/menu/select", function (r) {
                ztree = $.fn.zTree.init($("#menuTree"), setting, r.menuList);
                var node = ztree.getNodeByParam("menuId", vm.menu.parentId);
                ztree.selectNode(node);

                vm.menu.parentName = node.name;
            })
        },
        add: function () {
            vm.showList = false;
            vm.title = "设备注册";
            //把vm.sysdept.deptId字段值填入devicereginfo.sysnumber
            vm.devicereginfo.sysnumber = vm.sysdept.deptId;
        },
        buildCertificate: function () {
            //生成随机字符串进行赋值
            var out0 = "123123123";
            vm.devicereginfo.devcertificate = out0;
            $('#out0').val(vm.devicereginfo.devcertificate);
            vm.isbuildCertificate = true;
        },
        mergeDevid: function () {//生成32位设备唯一标识
            if (vm.validator()) {//设备信息填写完全才能生成唯一标识
                return;
            }
            var in1 = vm.devicereginfo.devdepartment;
            var in2 = vm.devicereginfo.devname;
            var in3 = vm.devicereginfo.devserial;
            var out1 = in1 + in2 + "000" + in3;
            if (out1.length < 32) {
                vm.isDevid = false;
                alert("设备信息输入有误！提示：设备使用单位长度为4字节，" +
                    "设备编号长度为16字节，请输入足够长度的数据！");
                return;
            }
            vm.devicereginfo.deviceid = out1;
            $('#out1').val(vm.devicereginfo.deviceid);
            vm.isDevid = true;
        },
        selectAgentInfo: function () {//选择代理框显示
            if (vm.validator()) {//设备信息填写完全才能选择代理信息
                return;
            }
            if (!vm.isDevid) {
                alert("设备唯一标识未生成！");
                return true;
            }
            vm.isAgent = false;//代理信息未选择，置"代理信息是否已经填写或选择"为假
            vm.agentinfo = {};//置空agentinfo
            vm.showAgent = false;
            vm.showSelect = true;
            vm.titleAgent = "选择代理";
            //加数据获取逻辑，取数据库代理表信息
            //取数据库中agentinfo表所有存入vm.agentinfos
            $.get(baseURL + "device/agentinfo/list", function (r) {
                vm.agentinfos = r.page.list;
            });
        },
        selected: function (r) {//所选代理信息存入vm.agentinfo
            var recnumber = r.target.value;//r.target.value为用户选中代理数据库中的记录编号
            // console.log("reconumber");
            // console.log(recnumber);
            $.get(baseURL + "device/agentinfo/info/" + recnumber, function (r) {
                vm.agentinfo = r.agentinfo;
            });
            vm.isAgent = true;//代理信息已选择，置"代理信息是否已经填写或选择"为真
        },
        inputAgentInfo: function () {//注册代理框显示
            if (vm.validator()) {//设备信息填写完全才能注册代理
                return;
            }
            if (!vm.isDevid) {
                alert("设备唯一标识未生成！");
                return true;
            }
            vm.isAgent = false;//代理信息未填写，置"代理信息是否已经填写或选择"为假
            vm.agentinfo = {};//置空agentinfo
            vm.showSelect = false;
            vm.showAgent = true;
            vm.titleAgent = "注册代理";
        },

        mergeAgentid: function () {//生成32位代理唯一标识
            var depeid = vm.sysdept.deptId.toString();//获取本级监控中心编号
            var timestamp = (new Date()).getTime().toString();//获取当前时间(从1970.1.1开始的毫秒数)
            var change = 16 - timestamp.length;//timestamp长度与16的差值
            while (change > 0) {//不够16的位数补零，得到16B的日期字符串
                timestamp = timestamp + "0";
                change--;
            }
            var randomnum = vm.randomString(8);//生成8B的流水号
            var out2 = depeid + timestamp + randomnum;//生成32B代理唯一标识
            vm.agentinfo.agentid = out2;//赋值
            $('#out2').val(vm.agentinfo.agentid);
            vm.isAgentid = true;//置"代理唯一标识是否生成"为真
        },
        randomString: function (len) {
            len = len || 32;
            var $chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678';
            /****默认去掉了容易混淆的字符oOLl,9gq,Vv,Uu,I1****/
            var maxPos = $chars.length;
            var pwd = '';
            for (i = 0; i < len; i++) {
                pwd += $chars.charAt(Math.floor(Math.random() * maxPos));
            }
            return pwd;
        },
        //注册代理
        registeAgent: function () {
            //1.判断是否已经生成了代理唯一标识
            //2.把代理数据提交数据库(成功后：vm.isAgent = true;)
            //3.置"手动注册代理按钮部分"为假，置选择代理单选按钮为不可用（不会）
            if (!vm.isAgentid) {
                alert("代理唯一标识未生成！");
                return;
            }
            vm.agentinfo.centerid = vm.sysdept.centerId;//填写本级监控中心ID
            var url = "device/agentinfo/save";
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.agentinfo),
                success: function (r) {
                    if (r.code === 0) {
                        alert('代理注册成功！');
                        vm.showButton = false;//置"手动注册代理按钮部分"为假
                        //置选择代理单选按钮为不可用（不会）
                    } else {
                        alert(r.msg);
                    }
                }
            });
            vm.isAgent = true;//代理信息已填写，置"代理信息是否已经填写或选择"为真
        },
        //导出注册信息
        outputInfo: function () {

            if (!vm.isAgent) {//代理信息检测
                alert("代理信息未填写或选择！");
                return;
            }
            //var d = Object.assign({},a,b);这样不会改变原对象
            //var mes = Object.assign({},vm.devicereginfo,vm.agentinfo);
            // 合并代理与设备信息(修改：按老师要求导出注册信息)
            var mes = {
                deviceid: vm.devicereginfo.deviceid,
                agentid: vm.agentinfo.agentid,
                agentip:vm.agentinfo.agentip,
                agentport: vm.agentinfo.agentport,//7000
                centerId:vm.sysdept.centerId,
                centeripAddr:vm.sysdept.ipAddr,
                centerport:vm.sysdept.agentPort,//6000
            };
            var blob = new Blob([JSON.stringify(mes)], {type: "text/plain;charset=utf-8"});
            saveAs(blob, "registeInfo.txt");
            vm.isOutputInfo = true;//注册信息已导出，置"注册信息是否已导出"为真
            //导出成功后才可以注册设备（否则点击按钮提示先要导出注册信息）
        },
        registeDev: function () {//注册设备
            if (vm.validator()) {//设备信息检测
                return;
            }
            if (!vm.isAgent) {//代理信息检测
                alert("代理信息未填写或选择！");
                return;
            }
            if (!vm.isOutputInfo) {//是否导出检测
                alert("注册信息未导出！");
                return;
            }
            var agentid = vm.agentinfo.agentid;
            $.get(baseURL + "device/agentinfo/infoAgent/" + agentid, function (r) {
                vm.devicereginfo.agentnum = r.agentinfo.recnumber;//加入代理记录编号字段
                var url = "device/deviceinfo/save";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.devicereginfo),
                    success: function (r) {
                        if (r.code === 0) {
                            alert('设备注册成功', function () {
                                vm.reload();
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        reload: function () {
            vm.isbuildCertificate = false;//置"设备证书是否生成"为假
            vm.isDevid = false;//置"设备唯一标识是否生成"为假
            vm.isAgentid = false;//置"代理唯一标识是否生成"为假
            vm.isAgent = false;//置"代理信息是否已经填写或选择"为假
            vm.isOutputInfo = false;//置"注册信息是否已导出"为假
            vm.inputType = null;//置"是否手动输入代理信息"为空
            vm.showButton = true;//置"手动注册代理按钮部分"为真
            vm.agentinfo = {centerid: "00000000"};//重置代理信息
            vm.devicereginfo = {sysnumber: "00000000"};//重置设备注册信息
            vm.showList = true;
            vm.showAgent = false;
            vm.showSelect = false;
            var a = document.getElementById("mySelect");//mySelect是select 的Id
            a.options[0].selected = true;
            Menu.table.refresh();
            //Menu.table.refresh();
        },
        validator: function () {//注册设备或输入(选择)代理信息前的各方面检验

            if (!vm.isbuildCertificate) {
                alert("设备证书未生成！");
                return;
            }
            if (isBlank(vm.devicereginfo.issuername)) {
                alert("设备生产厂商名称不能为空");
                return true;
            }
            if (isBlank(vm.devicereginfo.devname)) {
                alert("设备型号不能为空");
                return true;
            }
            if (isBlank(vm.devicereginfo.devserial)) {
                alert("设备编号不能为空");
                return true;
            }
            if (isBlank(vm.devicereginfo.devcertificate)) {
                alert("设备证书不能为空");
                return true;
            }
            if (isBlank(vm.devicereginfo.adminorid)) {
                alert("设备注册管理员身份证编号不能为空");
                return true;
            }
            if (isBlank(vm.devicereginfo.adminorphone)) {
                alert("设备管理员联系电话不能为空");
                return true;
            }
            if (isBlank(vm.devicereginfo.sysnumber)) {
                alert("监管中心编号不能为空");
                return true;
            }
            if (isBlank(vm.devicereginfo.devdepartment)) {
                alert("设备使用单位不能为空");
                return true;
            }
        },
        back: function () {//取消手动输入代理信息
            vm.agentinfo = {};//置空agentinfo
            vm.inputType = null;//置单选按钮为空
            vm.titleAgent = "代理信息";
            vm.showAgent = false;
        },
        update: function () {
            var menuId = getMenuId();
            if (menuId == null) {
                return;
            }
            $.get(baseURL + "sys/menu/info/" + menuId, function (r) {
                vm.showList = false;
                vm.title = "修改";
                vm.menu = r.menu;
                vm.getMenu();
            });
        },
        del: function () {
            var menuId = getMenuId();
            if (menuId == null) {
                return;
            }

            confirm('确定要删除选中的记录？', function () {
                $.ajax({
                    type: "POST",
                    url: baseURL + "sys/menu/delete",
                    data: "menuId=" + menuId,
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
        kill: function () {//毙杀设备
            var devid = getMenuId();
            if (devid == null) {
                return;
            }
            console.log("devid:");
            console.log(devid);

            confirm('确定要毙杀选中的设备？', function () {
                $.ajax({
                    type: "POST",
                    url: baseURL + "device/devicedyninfo/kill",
                    contentType: "application/json",
                    data: JSON.stringify(devid),
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

    }
});


var Menu = {
    id: "menuTable",
    table: null,
    layerIndex: -1,
};

/**
 * 初始化表格的列
 */
Menu.initColumn = function () {
    var columns = [
        {field: 'selectItem', radio: true},
        {title: '监管中心名称', field: 'name', align: 'center', valign: 'middle', sortable: true, width: '120px'},
        {title: '设备唯一标识', field: 'devID', align: 'center', valign: 'middle', sortable: true, width: '180px'},
        {title: '设备型号', field: 'devName', align: 'center', valign: 'middle', sortable: true, width: '100px'},
        {title: '设备名称', field: 'deviceName', align: 'center', valign: 'middle', sortable: true, width: '100px'},
        {title: '系统通知', field: 'notice', align: 'center', valign: 'middle', sortable: true, width: '100px'},
        {
            title: '设备状态',
            field: 'devstate',
            align: 'center',
            valign: 'middle',
            sortable: true,
            width: '60px',
            formatter: function (item, index) {
                if (item.devstate == "0") {
                    return '<span class="label label-success">在线</span>';
                }
                if (item.devstate == "1") {
                    return '<span class="label label-warning">离线</span>';
                }
                if (item.devstate == "2") {
                    return '<span class="label label-danger">已毙杀</span>';
                }
            }
        }]
    return columns;
};


function getMenuId() {
    var selected = $('#menuTable').bootstrapTreeTable('getSelections');
    if (selected.length == 0) {
        alert("请选择一条记录");
        return null;
    } else {
        return selected[0].id;
    }
}


$(function () {
    var colunms = Menu.initColumn();
    var table = new TreeTable(Menu.id, baseURL + "device/deviceinfo/alllist", colunms);
    table.setExpandColumn(1);
    table.setIdField("devID");
    table.setCodeField("deptId");
    table.setParentCodeField("parentId");
    table.setExpandAll(true);
    table.init();
    Menu.table = table;
    //取数据库中decicetypeinfo表内的DevName和DeviceName两列的值存入vm.devtypeinfos
    $.get(baseURL + "device/devtypeinfo/list", function (r) {
        vm.devtypeinfos = r.page.list;
    });
    //取数据库中devicedeptinfo表内的devdepartment和devdeptname两列的值存入vm.devicedeptinfos
    $.get(baseURL + "device/devicedeptinfo/list", function (r) {
        vm.devicedeptinfos = r.page.list;
    });
    //取出数据库中sys_dept表中isCurrent=1的deptId字段值填入vm.sysdept
    $.get(baseURL + "sys/dept/rowcurrent", function (r) {
        if (r.code === 0) {
            vm.sysdept = r.sysDeptEntity;
        }
        else {
            alert(r.msg);
        }
    });
});