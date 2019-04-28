<template>
  <div id="app">
    <el-container>
        <!-- 左侧导航栏 -->
        <el-aside width="auto">   
            <el-menu style="float:left" default-active="1-4-1" class="el-menu-vertical-demo"
                    @open="handleOpen" @close="handleClose" :collapse="isCollapse">
                <el-menu-item index="0">                       
                        <div><i class="el-icon-s-home"></i></div> 
                        <span>EPMS</span>               
                    </el-menu-item>
                <el-menu-item index="1">                       
                    <i class="el-icon-s-home"></i>
                     <span slot="title">我的主页</span>           
                </el-menu-item>
                <el-menu-item index="2">
                    <i class="el-icon-menu"></i>
                    <span slot="title">项目管理</span>
                </el-menu-item>
                <el-menu-item index="3">
                    <i class="el-icon-document"></i>
                    <span slot="title">考勤管理</span>
                </el-menu-item>
                <el-menu-item index="4">
                    <i class="el-icon-setting"></i>
                    <span slot="title">审批管理</span>
                </el-menu-item>
                <el-menu-item index="5">
                    <i class="el-icon-notebook-1"></i>
                    <span slot="title">工作报告</span>
                </el-menu-item>
                <el-menu-item index="6">
                    <i class="el-icon-takeaway-box"></i>
                    <span slot="title">自定义表单</span>
                </el-menu-item>
                <el-menu-item index="7">
                    <i class="el-icon-setting"></i>
                    <span slot="title">日程管理</span>
                </el-menu-item>
                <el-menu-item index="8">
                    <i class="el-icon-setting"></i>
                    <span slot="title">客户管理</span>
                </el-menu-item>
                <el-menu-item index="9">
                    <i class="el-icon-s-promotion"></i>
                    <span slot="title">销售管理</span>
                </el-menu-item>
                <el-menu-item index="10">
                    <i class="el-icon-setting"></i>
                    <span slot="title">知识社区</span>
                </el-menu-item>
                <el-menu-item index="11">
                    <i class="el-icon-picture"></i>
                    <span slot="title">员工相册</span>
                </el-menu-item>
                <el-menu-item index="12">
                    <i class="el-icon-folder"></i>
                    <span slot="title">文档管理</span>
                </el-menu-item>
                <el-menu-item index="13">
                    <i class="el-icon-setting"></i>
                    <span slot="title">简历管理</span>
                </el-menu-item>
            </el-menu>     
        </el-aside>
        <el-checkbox-group v-model="isCollapse" style="margin-bottom: 20px;float:left">
            <el-checkbox-button  :label="true">
                <p>—</p>
                <p>—</p>
                <p>—</p>
            </el-checkbox-button>
        </el-checkbox-group> 
        <el-container>
            <!-- 头部 -->
            <el-header>
                <div class="b_flex">   
                    <el-dropdown trigger="click">
                        <i class="el-icon-message"></i>
                        <el-dropdown-menu slot="dropdown" class="drop_menu_style" style="width:300px;background:#fff;height:auto;">
                            <div class="message">你有 0 最新信息</div> 
                            <div class="message_now"> 目前还没有最新消息</div>
                            <div class="more">查看更多</div>                                                                   
                        </el-dropdown-menu>
                    </el-dropdown>
                    <div class="user_style">
                        <div style="height:30px;width:30px;margin-top:10px;">
                            <img class="w_100" src="http://epms-ent.demo.zhidieyun.com/static/uploadfile/2019-4/3/27a43661e9ee84463b6b29ef171bfa6b-cropper.jpg" alt="">
                        </div>
                        <el-dropdown trigger="click">
                            <span class="el-dropdown-link">Jakiee
                                <i class="el-icon-caret-bottom"></i>
                            </span>
                            <el-dropdown-menu slot="dropdown">
                                <el-dropdown-item icon="el-icon-s-custom">个人主页</el-dropdown-item>
                                <el-dropdown-item icon="el-icon-s-tools">基本资料</el-dropdown-item>
                                <el-dropdown-item icon="el-icon-camera-solid">更换头像</el-dropdown-item>
                                <el-dropdown-item icon="el-icon-s-tools">更换密码</el-dropdown-item>
                                <el-dropdown-item icon="el-icon-s-tools">聊天</el-dropdown-item>
                                <el-dropdown-item icon="el-icon-s-tools">退出</el-dropdown-item>
                            </el-dropdown-menu>
                        </el-dropdown>                        
                    </div> 
                </div>       
            </el-header>
            <!-- 主体 -->
            <el-main>
              <router-view/>
            </el-main>
        </el-container>
      </el-container>
    </el-container>              
  </div>
</template>
  <style>
    body{
        background:#f0f0f0;
        padding:0;
        margin:0;
    }
    /* 左侧导航栏 */
    .el-menu-vertical-demo:not(.el-menu--collapse) {
        width: 200px;
        min-height: 400px;
    }
    .el-menu-item:first-child{
        text-align: center;
        font-size:1.3rem;
        line-height:0px;
    }
    .el-menu-item:first-child span{
        display:block;
        height:auto;
        margin-top:15px;
    }
    .el-menu-item{
        height:56px;
        line-height:56px;
    }
     .el-menu-vertical-demo.el-menu--collapse.el-menu li{
        width:100%;
        height:56px;
        line-height:56px; 
    }
    .el-menu-item{
        color:#fff;
        font-size:0.9rem;
    }
    .el-menu-item i{
        color:#fff;
    }
    .el-menu-item:hover{
        background: rgb(29, 25, 29);
    } 
    .el-menu-item.is-active{
        color:#65cea7;
        background:#3b4555
    } 
    .el-menu-item i span{
        margin-left:15px;
    }
    p{
        height:5px;
        margin:0;
        padding:0px;
        font-weight:bolder;
    }
    /* 导航栏按钮 */
    .el-checkbox-button__inner{
        height:50px !important;
        border-radius: 0px;
        background:#fff;
    }
    /* 导航栏展开时，按钮样式 */
    .el-checkbox-button.is-checked .el-checkbox-button__inner{
        background:#fff;
        border-radius: 0px;
        color:#000;
        border:0;
        border-left-color:#fff;
    }
    .el-checkbox-button.is-checked .el-checkbox-button__inner:hover{
        background:#65cea7;
        color:#fff;
    }
    /* 导航栏合并时，按钮样式 */
    .el-checkbox-button.is-focus .el-checkbox-button__inner{
        border-color:#fff !important;
        color:#000;
    }
    .el-checkbox-button.is-focus .el-checkbox-button__inner:hover{
        background:#65cea7;
        color:#fff;
    }
    /* 头部 */
    .el-header{
        height:50px !important;
        background:#fff;
        border-left:1px solid #ccc;
    }
    /* 信息下拉列表 */
    .b_flex{
        display:flex;
        float:right;
        width:200px;
        justify-content:space-around;
    }
    .b_flex i.el-icon-message{
        height:50px;
        width:50px;
        line-height:50px;
        text-align: center;
    }
    .b_flex i.el-icon-message:hover{
        cursor: pointer;
        background:#424f63;
        color:#65cea7;        
    }
    .b_flex i:active{
        background:#424f63;
        color:#65cea7;
    }
    .b_flex i:focus{
        background:#424f63;
        color:#65cea7;
    }
    .w_100{
        width:100%; 
    }
    /* 信息下拉列表内容 */
    .user_style{
          width:120px;
          line-height:50px;
          display:flex;
          justify-content:space-around;     
      }
      .user_style:hover{
        cursor: pointer;
        background:#424f63;
        color:#65cea7;
      }
      .user_style:active{
        background:#424f63;
        color:#65cea7;
    }
    .user_style:focus{
        background:#424f63;
        color:#65cea7;
    }
    .el-dropdown-link{
        color:#999 !important;
    }
    .el-dropdown-link:hover{
        color:#65cea7 !important;
    }
    .drop_menu_style .message{
        color:#fff;
        background:#65cea7;
        height:40px;
        line-height:40px;
        font-size:0.8rem;
        margin-top:-13px;
        margin-left:-1px;
        padding-left:10px;
    }
    .message_now{
        width:100%;
        height:50px;
        text-align: center;
        line-height:50px;
        font-size:0.9rem;
        border-bottom:1px solid #ccc;
        color:#ccc;
    }
    .more{
        height:30px;
        line-height:40px;
        font-size:0.9rem;
        padding-left:10px;
    }
    /*用户下拉列表  */
    .el-menu{
        background-color:#424f63;
        color:#fff;
    }
    .el-menu-item:first-child:hover{
        background-color:#424f63;
        color:#fff;
    } 
    .el-dropdown-link {
        cursor: pointer;
        color: #409EFF;
    }
    .el-icon-arrow-down {
        font-size: 12px;
    }
    .demonstration {
        display: block;
        color: #8492a6;
        font-size: 14px;
        margin-bottom: 20px;
    }
    .el-dropdown-menu{
        background:#65cea7;
        width:200px;
        position:absolute;
        top:37px !important;
        left:900px;
    }
    .el-dropdown-menu li{
        color:#fff
    }
    .el-dropdown-menu li:hover{
        background:#3b4555;
        color:#fff;
    }
    .el-popper[x-placement^=bottom] .popper__arrow::after{
        border-bottom-color:#65cea7;
    }
  </style> 
  <script>
    export default{
      el: '#app',
      data(){
        return { isCollapse: true }
      },
    methods: {
      handleOpen(key, keyPath) {
        console.log(key, keyPath);
      },
      handleClose(key, keyPath) {
        console.log(key, keyPath);
      }
    }
    }
  </script>
