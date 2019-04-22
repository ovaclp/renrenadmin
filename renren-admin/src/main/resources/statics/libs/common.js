/* 函数定义区  */
function fLayuiLayer (layerTitle,objId,layerW,layerH) {
	var nThisIndex = null;
	layui.use("layer",function(){
		var layer = layui.layer;
		nThisIndex = layer.open({
			id: (new Date()).valueOf(), //设定一个id，防止重复弹出 
			type: 1,
			title: [layerTitle,"font-size: 18px;color: #000;"],
			content: $("#"+objId),
			area:[layerW,layerH],
			shadeClose: false,
			closeBtn: 2,
			maxmin: true
		});
	});
	return nThisIndex;
};


//初始化表格
function fCreateTable(t) {
    layui.use(['table'], function () {
        var table = layui.table; //表格
        //执行一个 table 实例
        table.render({
            elem: '#'+t.id,//指定表格容器
            id: t.indexName,//设置容器唯一ID
            height: t.height||200,//设定容器高度
            url: t.url, //数据接口
            data: t.data,//直接赋值数据（与"接口数据"二取一）
            where: t.where||null,//接口的其它参数
            method: t.method,//接口http请求类型 默认get
            page: t.page, //开启分页
            cols: [//设置表头
                t.cols
            ]
        });
 
    });
};

var loadPage = function(url,id){
	$("#"+id).load(url,function(result){
		var $result = $(result);
		$(result).find("script").appendTo("#"+id);
	});
}




