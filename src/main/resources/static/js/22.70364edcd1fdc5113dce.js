webpackJsonp([22],{qGPb:function(t,e){},uvtf:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var r=a("vLgD");var l={data:function(){return{search:{method:"",path:"",status:""},list:[],page:{page:1,pageSize:14,total:0}}},mounted:function(){this.getOperationList()},methods:{getOperationList:function(){var t,e=this,a=this.page,l=a.page,o=a.pageSize,s=this.search,n=s.method,i=s.path,c=s.status;(t={page:l,pageSize:o,method:n,path:i,status:c},r.a.get({url:"/sysOperationRecord/getSysOperationRecordList",data:t}).then(function(t){return t})).then(function(t){if(0===t.code){var a=t.data,r=a.list,l=a.page,o=a.pageSize,s=a.total;e.list=r,e.page={page:l,pageSize:o,total:s}}})},resetSearchForm:function(){this.search={path:"",description:"",apiGroup:""},this.getOperationList()},onSubmit:function(){},deleteUser:function(t){this.$confirm("您确定要删除当前服务器吗","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"error"}).then(function(){}).catch(function(){})}}},o={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"page-content"},[a("table-bar",{attrs:{showTop:!1}},[a("div",{attrs:{slot:"top"},slot:"top"},[a("el-form",{attrs:{"label-width":"70px"}},[a("el-row",{attrs:{gutter:20}},[a("el-col",{attrs:{span:4}},[a("el-form-item",{attrs:{label:"请求方法："}},[a("el-input",{attrs:{placeholder:"请求方法",clearable:""},model:{value:t.search.method,callback:function(e){t.$set(t.search,"method","string"==typeof e?e.trim():e)},expression:"search.method"}})],1)],1),t._v(" "),a("el-col",{attrs:{span:4}},[a("el-form-item",{attrs:{label:"请求路径："}},[a("el-input",{attrs:{placeholder:"请求路径",clearable:""},model:{value:t.search.path,callback:function(e){t.$set(t.search,"path","string"==typeof e?e.trim():e)},expression:"search.path"}})],1)],1),t._v(" "),a("el-col",{attrs:{span:4}},[a("el-form-item",{attrs:{label:"状态码："}},[a("el-input",{attrs:{placeholder:"状态码",clearable:""},model:{value:t.search.status,callback:function(e){t.$set(t.search,"status","string"==typeof e?e.trim():e)},expression:"search.status"}})],1)],1),t._v(" "),a("el-row",{staticStyle:{float:"right","margin-right":"10px"},attrs:{span:4}},[a("el-button",{attrs:{type:"primary"},on:{click:t.getOperationList}},[t._v("搜索")]),t._v(" "),a("el-button",{on:{click:function(e){return t.resetSearchForm()}}},[t._v("重置")])],1)],1)],1)],1)]),t._v(" "),a("tao-table",{attrs:{data:t.list,page:t.page},on:{changePage:t.getOperationList}},[a("el-table-column",{attrs:{label:"操作人",prop:"user"},scopedSlots:t._u([{key:"default",fn:function(e){return[t._v("\n        "+t._s(e.row.user.userName)+"\n      ")]}}])}),t._v(" "),a("el-table-column",{attrs:{label:"日期",prop:"CreatedAt"}}),t._v(" "),a("el-table-column",{attrs:{label:"状态码",prop:"user"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("el-tag",{attrs:{type:"success"}},[t._v(t._s(e.row.status))])]}}])}),t._v(" "),a("el-table-column",{attrs:{label:"请求ip",prop:"ip"}}),t._v(" "),a("el-table-column",{attrs:{label:"请求方法",prop:"method"}}),t._v(" "),a("el-table-column",{attrs:{label:"请求路径",prop:"method"}}),t._v(" "),a("el-table-column",{attrs:{label:"请求",prop:"method"}}),t._v(" "),a("el-table-column",{attrs:{label:"响应",prop:"method"}}),t._v(" "),a("el-table-column",{attrs:{fixed:"right",label:"操作",width:"150px"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("el-button",{staticStyle:{color:"#FA6962"},attrs:{type:"text",icon:"el-icon-delete"},on:{click:function(a){return t.deleteUser(e)}}},[t._v("\n          删除\n        ")])]}}])})],1)],1)},staticRenderFns:[]};var s=a("VU/8")(l,o,!1,function(t){a("qGPb")},"data-v-719c44d8",null);e.default=s.exports}});