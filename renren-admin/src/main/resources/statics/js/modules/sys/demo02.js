$(function () {
    var id = getQueryString("id");
    $.ajax({
        url:'/renren-admin/sys/demo01/info/'+id,
        data:{},
        type:'GET',
        success:function (result) {
            vm.demo01.id = result.demo01.id;
            vm.demo01.name = result.demo01.name;
        }
    })
    console.log(id);
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		demo01: {
		    id: '',
            name: ''
        }
	},
	methods: {
        query: function () {
            vm.reload();
        },
        add: function () {
            vm.showList = false;
            vm.title = "新增";
            vm.demo01 = {};
        },
        saveOrUpdate: function (event) {
            console.log("aa");
            console.log(vm.demo01.name);
            // $('#btnSaveOrUpdate').button('loading').delay(1000).queue(function () {
            var url = vm.demo01.id == null ? "sys/demo01/save" : "sys/demo01/update";
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.demo01),
                success: function (r) {
                    if (r.code === 0) {
                        layer.msg("操作成功", {icon: 1});
                        vm.reload();
                        $('#btnSaveOrUpdate').button('reset');
                        $('#btnSaveOrUpdate').dequeue();
                    } else {
                        layer.alert(r.msg);
                        $('#btnSaveOrUpdate').button('reset');
                        $('#btnSaveOrUpdate').dequeue();
                    }
                }
            });
        },
        getInfo: function (id) {
            $.get(baseURL + "sys/demo01/info/" + id, function (r) {
                vm.demo01 = r.demo01;
            });
        },
        reload: function (event) {
            // vm.showList = true;
            // var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            // $("#jqGrid").jqGrid('setGridParam', {
            //     page: page
            // }).trigger("reloadGrid");
            window.location.href='/renren-admin/modules/sys/demo01.html?';
        },
        addLayer: function () {
            vm.demo01={}
            console.log(vm.demo01.name);
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "添加demo",
                area: ['600px', '350px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#addLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    // var node = ztree.getSelectedNodes();
                    // //选择上级部门
                    // vm.dept.parentId = node[0].deptId;
                    // vm.dept.parentName = node[0].name;
                    vm.saveOrUpdate();
                    //layer.close(index);
                }
            });
        },
    }
});