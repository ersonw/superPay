webpackJsonp([36],{Sy9B:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var n=a("Eaqu"),s=a("3KG0"),i={data:function(){return{show1:!1,show2:!1,orderlist:!0,money:"",id:"",dialogVisible:!1,start_time:"",end_time:"",status:"",pay_type:"",mode:"",up_trade_no:"",trade_no:"",out_trade_no:"",name:"",status_content:"",tableid:"",tableurl:"",pagesize:15,currentPage:1,total:null,tableData:[],zhuangtais:[{value:"0",label:"未支付",type:0},{value:"1",label:"已支付",type:1},{value:"2",label:"已冻结",type:2},{value:"3",label:"退款中",type:3},{value:"4",label:"已退款",type:4}],zhifus:[{value:"wxpay",label:"微信"},{value:"alipay",label:"支付宝"},{value:"qqpay",label:"QQ支付"}],moshis:[{value:0,label:"订单加费"},{value:1,label:"余额扣费"}],dingdans:[{value:"quanbu",label:"全部订单"},{value:"dongjie",label:"被冻结订单"},{value:"zhengchang",label:"正常订单"}]}},created:function(){},mounted:function(){this.handonchangUser()},filters:{formatDate:function(t){t*=1e3;var e=new Date(t);return Object(s.a)(e,"yyyy-MM-dd hh:mm")}},methods:{tuikuanres:function(){this.$router.push("/user/settlement")},handleSizeChange:function(t){this.pagesize=t},handleCurrentChange:function(t){var e=this;Object(n.r)({page:this.currentPage,start_time:this.start_time,end_time:this.end_time,status:this.status,pay_type:this.pay_type,mode:this.mode,up_trade_no:this.up_trade_no,trade_no:this.trade_no,out_trade_no:this.out_trade_no,money:this.money,name:this.name}).then(function(t){e.tableData=t.data.rows,e.total=t.data.total})},handonchangUser:function(){var t=this;Object(n.r)({page:this.currentPage}).then(function(e){t.tableData=e.data.rows,t.total=e.data.total})},statuschangge:function(t){this.status=t[0]},pay_typechangeg:function(t){this.pay_type=t[0]},modechange:function(t){this.mode=t[0]},Search:function(){var t=this;Object(n.z)({page:this.currentPage,start_time:this.start_time,end_time:this.end_time,status:this.status,pay_type:this.pay_type,mode:this.mode,up_trade_no:this.up_trade_no,trade_no:this.trade_no,out_trade_no:this.out_trade_no,money:this.money,name:this.name}).then(function(e){t.tableData=e.data.rows,t.total=e.data.total})},handleClose:function(){this.dialogVisible=!1},handleClose1:function(){this.show1=!1},handleClose2:function(){this.show2=!1},chakan:function(t){this.status_content=t.status_content,this.show1=!0},tuikuan:function(t){this.money=t.money,this.id=t.id,this.show2=!0},TuikuanApi:function(){var t=this;Object(n.B)({id:this.id,money:this.money}).then(function(e){1==e.code?(t.$message({message:e.msg,type:"success"}),setTimeout(function(){this.show2=!1},1e3)):t.$message.error(e.msg)})},tongzhi:function(t){this.tableid=t.id,this.tableurl=t.return_url_log,this.dialogVisible=!0},yibuurl:function(){var t=this;Object(n.O)({id:this.tableid}).then(function(e){1==e.code&&t.$message({message:e.msg,type:"success"})})},tongbuurl:function(){window.open(this.tableurl,"_blank")}}},l={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticStyle:{"background-color":"#fff"}},[1==t.orderlist?a("div",{staticClass:"pailie"},[a("div",{staticClass:"riqi"},[a("el-date-picker",{staticStyle:{width:"49%"},attrs:{format:"yyyy - MM - dd","value-format":"yyyy-MM-dd 00:00:00",type:"date",placeholder:"开始日期"},model:{value:t.start_time,callback:function(e){t.start_time=e},expression:"start_time"}}),t._v(" "),a("span",[t._v("-")]),t._v(" "),a("el-date-picker",{staticStyle:{width:"49%"},attrs:{format:"yyyy - MM - dd","value-format":"yyyy-MM-dd 23:59:59",type:"date",placeholder:"结束日期"},model:{value:t.end_time,callback:function(e){t.end_time=e},expression:"end_time"}})],1),t._v(" "),a("div",{staticClass:"pailie_1"},[a("el-cascader",{attrs:{options:t.zhuangtais,clearable:""},on:{change:t.statuschangge},model:{value:t.status,callback:function(e){t.status=e},expression:"status"}})],1),t._v(" "),a("div",{staticClass:"pailie_1"},[a("el-cascader",{attrs:{options:t.zhifus,clearable:""},on:{change:t.pay_typechangeg},model:{value:t.pay_type,callback:function(e){t.pay_type=e},expression:"pay_type"}})],1),t._v(" "),a("div",{staticClass:"pailie_1"},[a("el-cascader",{attrs:{options:t.moshis,clearable:""},on:{change:t.modechange},model:{value:t.mode,callback:function(e){t.mode=e},expression:"mode"}})],1),t._v(" "),a("div",{staticClass:"pailie_1"},[a("el-input",{attrs:{placeholder:"代收平台上游订单号"},model:{value:t.up_trade_no,callback:function(e){t.up_trade_no=e},expression:"up_trade_no"}})],1),t._v(" "),a("div",{staticClass:"pailie_1"},[a("el-input",{attrs:{placeholder:"输入系统订单号"},model:{value:t.trade_no,callback:function(e){t.trade_no=e},expression:"trade_no"}})],1),t._v(" "),a("div",{staticClass:"pailie_1"},[a("el-input",{attrs:{placeholder:"商户订单号"},model:{value:t.out_trade_no,callback:function(e){t.out_trade_no=e},expression:"out_trade_no"}})],1),t._v(" "),a("div",{staticClass:"pailie_1"},[a("el-input",{attrs:{placeholder:"输入商品金额"},model:{value:t.money,callback:function(e){t.money=e},expression:"money"}})],1),t._v(" "),a("div",{staticClass:"pailie_1"},[a("el-input",{attrs:{placeholder:"输入商品名称"},model:{value:t.name,callback:function(e){t.name=e},expression:"name"}})],1),t._v(" "),a("div",{staticClass:"pailie_1",staticStyle:{width:"200px"}},[a("el-button",{attrs:{icon:"el-icon-search",type:"primary"},on:{click:t.Search}},[t._v("搜索")]),t._v(" "),a("el-button",{attrs:{icon:"el-icon-money"},on:{click:t.tuikuanres}},[t._v("退款记录")])],1)]):a("div",{staticClass:"ordlistcl"},[a("el-button",{staticClass:"ordlistshow",attrs:{icon:"el-icon-plus"},on:{click:function(e){t.orderlist=!0}}},[t._v("展开搜索")]),t._v(" "),a("el-button",{attrs:{type:"primary",icon:"el-icon-money"},on:{click:t.tuikuanres}},[t._v("退款记录")])],1),t._v(" "),a("div",{staticClass:"table_list"},[a("el-table",{staticStyle:{width:"100%"},attrs:{data:t.tableData,border:""}},[a("el-table-column",{attrs:{label:"商户/系统/上游单号",align:"center",width:"230"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("div",[t._v(t._s(e.row.out_trade_no))]),t._v(" "),a("div",[t._v(t._s(e.row.trade_no))]),t._v(" "),e.row.up_trade_no?a("div",[t._v(t._s(e.row.up_trade_no))]):t._e()]}}])}),t._v(" "),a("el-table-column",{attrs:{label:"支付方式",align:"center"},scopedSlots:t._u([{key:"default",fn:function(e){return["qqpay"==e.row.pay_type?a("span",[t._v("QQ支付")]):t._e(),t._v(" "),"wxpay"==e.row.pay_type?a("span",[t._v("微信支付")]):t._e(),t._v(" "),"alipay"==e.row.pay_type?a("span",[t._v("支付宝支付")]):t._e()]}}])}),t._v(" "),a("el-table-column",{attrs:{label:"状态",align:"center"},scopedSlots:t._u([{key:"default",fn:function(e){return[0==e.row.status?a("span",[a("el-tag",{attrs:{type:"info"}},[t._v("未支付")])],1):t._e(),t._v(" "),1==e.row.status?a("span",[a("el-tag",{attrs:{type:"success"}},[t._v("已支付")])],1):t._e(),t._v(" "),2==e.row.status?a("span",[a("el-tag",[t._v("退款中")])],1):t._e(),t._v(" "),3==e.row.status?a("span",[a("el-tag",{attrs:{type:"warning"}},[t._v("已退款")])],1):t._e(),t._v(" "),4==e.row.status?a("span",[a("el-tag",{attrs:{type:"danger"}},[t._v("已冻结")])],1):t._e()]}}])}),t._v(" "),a("el-table-column",{attrs:{label:"回调",align:"center"},scopedSlots:t._u([{key:"default",fn:function(e){return[0==e.row.notify_status?a("span",[a("el-tag",{attrs:{type:"danger"}},[t._v("失败")])],1):t._e(),t._v(" "),1==e.row.notify_status?a("span",[a("el-tag",{attrs:{type:"success"}},[t._v("成功")])],1):t._e()]}}])}),t._v(" "),a("el-table-column",{attrs:{prop:"name",label:"名称",align:"center"}}),t._v(" "),a("el-table-column",{attrs:{prop:"money",label:"金额/到账/费率",align:"center",width:"200"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("div",{staticStyle:{display:"flex","flex-direction":"row","align-items":"center","justify-content":"center"}},[a("div",{staticStyle:{width:"50px","font-weight":"bold"}},[t._v("\n              "+t._s(e.row.money)+"\n            ")]),t._v("\n            |\n            "),a("div",{staticStyle:{width:"50px","font-weight":"bold"}},[t._v("\n              "+t._s(e.row.realmoney?e.row.realmoney:0)+"\n            ")]),t._v("\n            |\n            "),a("div",{staticStyle:{width:"50px"}},[t._v("\n              "+t._s(e.row.rate?e.row.rate:"100%")+"\n            ")])])]}}])}),t._v(" "),a("el-table-column",{attrs:{label:"创建/支付时间",align:"center",width:"200"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("div",[t._v(t._s(t._f("formatDate")(e.row.created_at)))]),t._v(" "),1==e.row.status?a("div",[t._v("\n            "+t._s(t._f("formatDate")(e.row.updated_at))+"\n          ")]):t._e()]}}])}),t._v(" "),a("el-table-column",{attrs:{label:"操作",align:"center",fixed:"right"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("div",{staticClass:"anniuzhu"},[a("div",[1==e.row.status&&0==e.row.notify_status||4==e.row.status?a("el-button",{attrs:{size:"mini"},on:{click:function(a){return t.chakan(e.row)}}},[t._v("查看")]):t._e()],1),t._v(" "),a("div",[1!=e.row.status&&3!=e.row.status||1!=e.row.is_refund?t._e():a("el-button",{attrs:{size:"mini",type:"warning"},on:{click:function(a){return t.tuikuan(e.row)}}},[t._v("退款")])],1),t._v(" "),a("div",[0!=e.row.status?a("el-button",{attrs:{size:"mini",type:"primary"},on:{click:function(a){return t.tongzhi(e.row)}}},[t._v("通知")]):t._e()],1),t._v(" "),0==e.row.status?a("span",[t._v("无")]):t._e()])]}}])})],1)],1),t._v(" "),a("el-dialog",{attrs:{title:"查看信息",width:"30%",visible:t.show1,"before-close":t.handleClose1},on:{"update:visible":function(e){t.show1=e}}},[a("span",[t._v(t._s(t.status_content))])]),t._v(" "),a("el-dialog",{attrs:{title:"退款信息",width:"30%",visible:t.show2,"before-close":t.handleClose2},on:{"update:visible":function(e){t.show2=e}}},[a("div",{staticClass:"tuijianmoney"},[a("div",{staticStyle:{width:"100%","margin-bottom":"10px"}},[t._v("退款金额")]),t._v(" "),a("el-input",{attrs:{placeholder:"请输入退款金额"},model:{value:t.money,callback:function(e){t.money=e},expression:"money"}}),t._v(" "),a("div",{staticClass:"tuikuananniu",staticStyle:{width:"100%"}},[a("el-button",{attrs:{type:"primary"},on:{click:t.TuikuanApi}},[t._v("确认退款")])],1)],1)]),t._v(" "),a("div",{staticClass:"fenye"},[a("el-pagination",{attrs:{background:"",small:"","current-page":t.currentPage,"page-size":t.pagesize,layout:"prev, pager, next, jumper",total:t.total},on:{"size-change":t.handleSizeChange,"current-change":t.handleCurrentChange,"update:currentPage":function(e){t.currentPage=e},"update:current-page":function(e){t.currentPage=e}}})],1),t._v(" "),a("div",[a("el-dialog",{attrs:{title:"通知",visible:t.dialogVisible,width:"30%","before-close":t.handleClose},on:{"update:visible":function(e){t.dialogVisible=e}}},[a("div",{staticClass:"tiaozhuanurl"},[a("el-button",{staticStyle:{width:"49.5%"},attrs:{type:"primary"},on:{click:t.yibuurl}},[t._v("异步通知")]),t._v(" "),a("el-button",{staticStyle:{width:"49.5%"},attrs:{type:"success"},on:{click:t.tongbuurl}},[t._v("同步跳转")])],1)])],1)],1)},staticRenderFns:[]};var o=a("VU/8")(i,l,!1,function(t){a("h4LQ")},"data-v-db391f06",null);e.default=o.exports},h4LQ:function(t,e){}});