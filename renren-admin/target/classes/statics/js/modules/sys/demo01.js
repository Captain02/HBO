$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/demo01/list',
        datatype: "json",
        colModel: [
            {label: 'id', name: 'id', index: 'id', width: 50, key: true},
            {label: '', name: 'name', index: 'name', width: 80}
        ],
        viewrecords: true,
        height: 385,
        rowNum: 10,
        rowList: [10, 30, 50],
        rownumbers: true,
        rownumWidth: 25,
        autowidth: true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader: {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order"
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });
});

var vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        title: null,
        demo01: {
            id: null,
            name: '',
        }
    },
    methods:
        {
            query: function () {
                vm.reload();
            },
            add: function () {
                vm.showList = false;
                vm.title = "新增";
                vm.demo01 = {};
            },
            update: function (event) {
                var id = getSelectedRow();
                if (id == null) {
                    return;
                }
                window.location.href='/renren-admin/modules/sys/demo01update.html?id='+id;
                // $.ajax({
                //     url:"/demo01update"
                // })
                // vm.showList = false;
                // vm.title = "修改";
                //
                // vm.getInfo(id)
            },
            saveOrUpdate: function (event) {
                // console.log("aa");
                console.log("+++++++++++"+vm.demo01.name);
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
                // });
            },
            del: function (event) {
                var ids = getSelectedRows();
                if (ids == null) {
                    return;
                }
                var lock = false;
                layer.confirm('确定要删除选中的记录？', {
                    btn: ['确定', '取消'] //按钮
                }, function () {
                    if (!lock) {
                        lock = true;
                        $.ajax({
                            type: "POST",
                            url: baseURL + "sys/demo01/delete",
                            contentType: "application/json",
                            data: JSON.stringify(ids),
                            success: function (r) {
                                if (r.code == 0) {
                                    layer.msg("操作成功", {icon: 1});
                                    $("#jqGrid").trigger("reloadGrid");
                                    // reload();
                                } else {
                                    layer.alert(r.msg);
                                }
                            }
                        });
                    }
                }, function () {
                });
            },
            getInfo: function (id) {
                $.get(baseURL + "sys/demo01/info/" + id, function (r) {
                    vm.demo01 = r.demo01;
                });
            },
            reload: function (event) {
                console.log("aaaa");
                vm.showList = true;
                var page = $("#jqGrid").jqGrid('getGridParam', 'page');
                $("#jqGrid").jqGrid('setGridParam', {
                    page: page
                }).trigger("reloadGrid");
            },
            addLayer: function () {
                // vm.demo01={}
                //     console.log(vm.demo01.name);
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
                        layer.close(index);

                    }
                });
            },
        }
});