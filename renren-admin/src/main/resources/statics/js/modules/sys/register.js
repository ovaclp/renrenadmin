var vm = new Vue({
    el: '#rrapp',
    data: {
        title: "监控中心注册",
        isCenterid: false,//中心唯一标识是否生成
        isjoint:false,//是否拼接了"使用单位名称"字段
        isTopCenter:false,
        top:1,
        sysDept: {
            agentPort:6000,
        },
        deptDomainInfos: {},
        dataprovince: null,
        datacity: null,
        citynum: null,
    },
    methods: {
        selected: function (r) {//所选城市列表:  console.log(r.target.selectedOptions);
            var citynum = r.target.selectedOptions[0].dataset.code;//取到所选城市的编号
            vm.citynum = citynum.substring(0, 4);
        },
        validator: function () {//生成中心唯一标识前的输入检验
            if (isBlank(vm.dataprovince)) {
                alert("请先选择本级监控中心地址！");
                return true;
            }
            if (isBlank(vm.datacity)) {
                alert("请先选择本级监控中心地址！");
                return true;
            }
            if (isBlank(vm.sysDept.deptUser)) {
                alert("使用单位名称不能为空！");
                return true;
            }
            if (isBlank(vm.sysDept.name)) {
                alert("本级监控中心名称不能为空！");
                return true;
            }
            if (isBlank(vm.sysDept.ipAddr)) {
                alert("本级监控中心的IP地址不能为空！");
                return true;
            }
            if (isBlank(vm.sysDept.agentPort)) {
                alert("代理向监控中心报数据的端口号不能为空！");
                return true;
            }
            if (isBlank(vm.sysDept.sendStrategy)) {
                alert("请选择一个上报策略！");
                return true;
            }
            if (isBlank(vm.sysDept.killStrategy)) {
                alert("请选择一个毙杀策略！");
                return true;
            }
            if (isBlank(vm.sysDept.deptDomainnum)) {
                alert("请选择一个本级监控中心所属域！");
                return true;
            }
        },
        mergeCenterId: function () {
            if (vm.validator()) {//注册信息填写完全才能生成中心唯一标识
                return;
            }

            var deptusername = vm.randomString(4);//生成一个4B的随机字符串
            vm.sysDept.deptId = vm.citynum + deptusername;//生成8B的监控中心编号

            var timestamp = (new Date()).getTime().toString();//获取当前时间(从1970.1.1开始的毫秒数)
            var change = 16 - timestamp.length;//timestamp长度与16的差值
            while(change > 0){//不够16的位数补零
                timestamp = timestamp + "0";
                change--;
            }

            var out = vm.sysDept.deptDomainnum + vm.sysDept.deptId + timestamp;//生成监控中心唯一标识
            vm.sysDept.centerId = out;//赋值
            $('#out1').val(vm.sysDept.centerId);
            vm.isCenterid = true;
        },
        save: function (event) {
            //加入判断：是否已经注册本级监控中心，若就已经注册，提示“已注册”，调用reload()

            if (vm.validator()) {//注册信息填写完全才能注册
                return;
            }
            if (!vm.isCenterid) {//中心唯一标识生成后才能注册
                alert("中心唯一标识未生成！")
                return;
            }
           /* if (isBlank(vm.sysDept.parentIp)) {//必须要有上级监控中心IP地址
                alert("请输入上级监控中心IP地址！");
                return;
            }*/
            if(!vm.isjoint){
                var deptuser = vm.dataprovince + vm.datacity + vm.sysDept.deptUser;//xx省xx市xx单位
                vm.sysDept.deptUser = deptuser;//xx省xx市xx单位（赋值回去）
                vm.isjoint = true;
            }

            //这里需要加与上级中心通讯逻辑，获取到上级的数据再插数据库
            var url = "sys/dept/register";

            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.sysDept),
                success: function (r) {
                    if (r.code === 0) {
                        alert('注册成功', function (index) {
                            //window.location.href = "http://localhost:8080/renren-admin/index.html#modules/sys/dept.html";
                            vm.reload();
                        });
                    } else {
                        alert(r.msg);
                    }
                }
            });

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
        isTop:function(){
            vm.isTopCenter = true;
        },
        isNotTop:function(){
            vm.isTopCenter = false;
        },
        reload: function (event) {
            vm.sysDept = {
                agentPort:6000,
            }; //重置
            vm.isCenterid = false;//置"中心唯一标识是否生成"为假
            vm.isjoint = false;//置"是否拼接了'使用单位名称'字段"为假
        },
    },
});

$(function () {
    //取数据库中Detpdomainnumdct表内的deptDomainnum和deptDomain两列的值存入vm.deptDomainInfos
    $.get(baseURL + "device/detpdomainnumdct/list", function (r) {
        vm.deptDomainInfos = r.page.list;
    });
});


