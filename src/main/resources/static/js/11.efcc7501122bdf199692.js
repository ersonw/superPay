webpackJsonp([11,25],{"4r3V":function(t,n){},A2EQ:function(t,n,i){"use strict";Object.defineProperty(n,"__esModule",{value:!0});var e=i("Eaqu"),a=i("TQvf"),l=i.n(a),d=i("rRGo"),r={components:{Icon:i("eBPz").default},data:function(){return{Tishiname:"",url:"",api_id:"",api_key:"",JsonList:d.b,refresh:!1,url_key:0,content:"",config:{},dialogVisible:!1,addresslist:"",items:[{id:1,name:"一键更换",show:!1},{id:2,name:"对接文档"},{id:3,name:"初始化"}]}},created:function(){},mounted:function(){this.content=d.b[0].biaoti;var t=JSON.parse(localStorage.getItem("config"));this.config=t,this.url=this.config.api_url,this.Tishiname=this.config.user_token_tip,this.config.api_urls&&(this.refresh=!0),this.getUsegetApi()},methods:{refresh_token:function(){var t=this;Object(e.A)().then(function(n){n.code&&(t.$message.success(n.msg),t.api_id=n.data.api_id,t.api_key=n.data.api_key)})},getUsegetApi:function(){var t=this;Object(e.C)().then(function(n){t.api_id=n.data.api_id,t.api_key=n.data.api_key,t.url=n.data.api_url})},shuaxin1:function(){this.dialogVisible=!0;var t=JSON.parse(localStorage.getItem("config")).api_urls.split(",");this.addresslist=t},shuaxin2:function(t){var n=this,i=new l.a(".btn88");i.on("success",function(t){n.$message({message:"复制网址成功",type:"success"}),i.destroy()})},handleClose:function(){this.dialogVisible=!1},fuzhi1:function(){var t=this,n=new l.a(".btn1");n.on("success",function(i){t.$message({message:"复制网址成功",type:"success"}),n.destroy()})},fuzhi2:function(){var t=this,n=new l.a(".btn2");n.on("success",function(i){n.destroy(),t.$message({message:"复制商户ID成功",type:"success"})})},fuzhi3:function(){var t=this,n=new l.a(".btn3");n.on("success",function(i){t.$message({message:"复制商户密钥成功",type:"success"}),n.destroy()})},getToken:function(){},dianji:function(t){this.content=t.biaoti}}},s={render:function(){var t=this,n=t.$createElement,e=t._self._c||n;return e("div",{staticStyle:{"background-color":"#fff"}},[e("div",{staticClass:"title"},[e("div",{staticClass:"title_1"},[e("table",[e("tr",[e("th",[t._v("接口地址:")]),t._v(" "),e("th",[e("div",{staticClass:"table_list_one"},[e("span",[t._v(t._s(t.url))]),t._v(" "),e("img",{staticClass:"btn1",attrs:{src:i("eVt3"),"data-clipboard-text":t.url},on:{click:t.fuzhi1}}),t._v(" "),t.refresh?e("el-button",{staticStyle:{"margin-left":"10px"},attrs:{type:"success",size:"mini"},on:{click:t.shuaxin1}},[t._v("更多\n              ")]):t._e()],1)])]),t._v(" "),e("tr",[e("td",[t._v("商户ID:")]),t._v(" "),e("td",[e("div",{staticClass:"table_list_one"},[e("span",[t._v(t._s(t.api_id))]),t._v(" "),e("img",{staticClass:"btn2",attrs:{"data-clipboard-text":t.api_id,src:i("eVt3")},on:{click:t.fuzhi2}}),t._v(" "),e("el-button",{staticStyle:{"margin-left":"10px"},attrs:{type:"waring",size:"mini"},on:{click:t.refresh_token}},[t._v("一键更换\n              ")])],1)])]),t._v(" "),e("tr",[e("td",[t._v("商户密钥:")]),t._v(" "),e("td",[e("div",{staticClass:"table_list_one"},[e("span",[t._v(t._s(t.api_key))]),t._v(" "),e("img",{staticClass:"btn3",attrs:{"data-clipboard-text":t.api_key,src:i("eVt3")},on:{click:t.fuzhi3}})])])])])]),t._v(" "),e("div",{staticClass:"title_2"},[e("div",{staticClass:"title2_1"},[e("div",{staticClass:"title2_1_1"},[e("el-alert",{attrs:{center:""}},[t._v(t._s(t.Tishiname))])],1),t._v(" "),e("div",{staticClass:"title2_1_2"},[e("el-button",{staticStyle:{width:"49%"},attrs:{type:"primary"}},[t._v("sdk1下载地址\n          ")]),t._v(" "),e("el-button",{staticStyle:{width:"49%"},attrs:{type:"primary"}},[t._v("sdk2下载地址\n          ")])],1)])])]),t._v(" "),e("div",{staticClass:"title_3"},[e("div",{staticClass:"title_3_1"},t._l(t.JsonList,function(n,i){return e("div",{key:i,staticClass:"title_3_1_1",on:{click:function(i){return t.dianji(n)}}},[t._v("\n        "+t._s(n.name)+"\n      ")])}),0),t._v(" "),e("div",{staticClass:"title_3_2"},[e("div",{staticClass:"title_3_2_1"},[e("div",{domProps:{innerHTML:t._s(t.content)}})])])]),t._v(" "),e("el-dialog",{attrs:{title:"更换地址",visible:t.dialogVisible,width:"30%","before-close":t.handleClose},on:{"update:visible":function(n){t.dialogVisible=n}}},[e("div",{staticClass:"genghuanaddress"},[e("div",{staticStyle:{"margin-bottom":"10px"}},[e("span",[t._v("主地址")]),t._v(" "),e("div",[t._v(t._s(t.url))])]),t._v(" "),e("div",[e("span",[t._v("备用地址")]),t._v(" "),e("el-table",{staticStyle:{width:"100%"},attrs:{data:t.addresslist,height:"150",border:"","show-header":""}},[e("el-table-column",{attrs:{label:"接口地址",align:"center"},scopedSlots:t._u([{key:"default",fn:function(n){return[e("span",[t._v(t._s(n.row))])]}}])}),t._v(" "),e("el-table-column",{attrs:{label:"操作",align:"center"},scopedSlots:t._u([{key:"default",fn:function(n){return[e("el-button",{staticClass:"btn88",attrs:{type:"primary","data-clipboard-text":n.row},on:{click:function(i){return t.shuaxin2(n.row)}}},[t._v("复制\n              ")])]}}])})],1)],1)])])],1)},staticRenderFns:[]};var p=i("VU/8")(r,s,!1,function(t){i("4r3V")},"data-v-3c5ee0ac",null);n.default=p.exports},G9Qu:function(t,n){},eBPz:function(t,n,i){"use strict";Object.defineProperty(n,"__esModule",{value:!0});var e=i("qUen"),a=i("7Zj/"),l={data:function(){return{activeName:"icons",iconList:e.a,elIconList:a.a}},methods:{copyIcon:function(t){var n=document.createElement("input");n.setAttribute("value",t),document.body.appendChild(n),n.select(),document.execCommand("copy"),document.body.removeChild(n),this.$message.success({message:"复制成功",duration:1500})}}},d={render:function(){var t=this,n=t.$createElement,i=t._self._c||n;return i("div",{staticClass:"page-content"},[i("el-tabs",{attrs:{type:"card"},model:{value:t.activeName,callback:function(n){t.activeName=n},expression:"activeName"}},[i("el-tab-pane",{attrs:{label:"Icons",name:"icons"}}),t._v(" "),i("el-tab-pane",{attrs:{label:"Element icon",name:"element"}})],1),t._v(" "),i("div",{staticClass:"el-tabs-content"},[i("ul",{directives:[{name:"show",rawName:"v-show",value:"icons"===t.activeName,expression:"activeName === 'icons'"}],staticClass:"icon-list"},t._l(t.iconList,function(n){return i("li",{key:n,on:{click:function(i){return t.copyIcon(n)}}},[i("i",{staticClass:"iconfont",domProps:{innerHTML:t._s(n)}}),t._v(" "),i("span",[t._v(t._s(n))])])}),0),t._v(" "),i("ul",{directives:[{name:"show",rawName:"v-show",value:"element"===t.activeName,expression:"activeName === 'element'"}],staticClass:"icon-list"},t._l(t.elIconList,function(n){return i("li",{key:n,on:{click:function(i){return t.copyIcon(n)}}},[i("i",{class:n}),t._v(" "),i("span",[t._v(t._s(n))])])}),0)])],1)},staticRenderFns:[]};var r=i("VU/8")(l,d,!1,function(t){i("G9Qu")},"data-v-4ad38e17",null);n.default=r.exports},eVt3:function(t,n){t.exports="data:image/png;base64,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"},rRGo:function(t,n,i){"use strict";i.d(n,"b",function(){return e}),i.d(n,"c",function(){return a}),i.d(n,"a",function(){return l});var e=[{id:1,name:"发起支付",biaoti:'<h3 style="margin-top: 20px; margin-bottom: 10px; font-family: inherit; font-weight: 500; line-height: 1.1; color: inherit; font-size: 24px;">\n    发起支付请求\n</h3>\n<div style="width:300px;height:1px;border:soild 2px #333333"></div>\n<p style="margin-bottom: 10px;">\n    请求地址：/submit.php\n</p>\n<p style="margin-bottom: 10px;">\n    请求方式：POST | GET\n</p>\n<p style="margin-bottom: 10px;">\n    请求参数：\n</p>\n\n<table  border="1" bordercolor="#d8d8d8" class="table table-bordered table-hover" width="847">\n    <thead>\n        <tr class="firstRow">\n            <th style="padding: 8px; text-align: left; line-height: 1.42857; vertical-align: bottom; border-top: 0px; border-bottom-width: 2px;">\n                字段名\n            </th>\n            <th style="padding: 8px; text-align: left; line-height: 1.42857; vertical-align: bottom; border-top: 0px; border-bottom-width: 2px;">\n                变量名\n            </th>\n            <th style="padding: 8px; text-align: left; line-height: 1.42857; vertical-align: bottom; border-top: 0px; border-bottom-width: 2px;">\n                必填\n            </th>\n            <th style="padding: 8px; text-align: left; line-height: 1.42857; vertical-align: bottom; border-top: 0px; border-bottom-width: 2px;">\n                类型\n            </th>\n            <th style="padding: 8px; text-align: left; line-height: 1.42857; vertical-align: bottom; border-top: 0px; border-bottom-width: 2px;">\n                示例值\n            </th>\n            <th style="padding: 8px; text-align: left; line-height: 1.42857; vertical-align: bottom; border-top: 0px; border-bottom-width: 2px;">\n                描述\n            </th>\n        </tr>\n    </thead>\n    <tbody>\n        <tr>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                商户ID\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                pid\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                是\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                Int\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                1001\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;"></td>\n        </tr>\n        <tr>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                支付方式\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                type\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                是\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                String\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                alipay\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                alipay:支付宝,wxpay:微信支付<br/>qqpay:QQ钱包\n            </td>\n        </tr>\n        <tr>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                商户订单号\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                out_trade_no\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                是\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                String\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                20160806151343349\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;"></td>\n        </tr>\n        <tr>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                异步通知地址\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                notify_url\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                是\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                String\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                http://test.test/notify_url.php\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                服务器异步通知地址\n            </td>\n        </tr>\n        <tr>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                跳转通知地址\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                return_url\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                是\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                String\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                http://test.test/return_url.php\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                页面跳转通知地址\n            </td>\n        </tr>\n        <tr>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                商品名称\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                name\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                是\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                String\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                VIP会员\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;"></td>\n        </tr>\n        <tr>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                商品金额\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                money\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                是\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                String\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                1.00\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;"></td>\n        </tr>\n        <tr>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                签名字符串\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                sign\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                是\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                String\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                202cb962ac59075b964b07152d234b70\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                详见签名规则一节\n            </td>\n        </tr>\n        <tr>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                签名类型\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                sign_type\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                是\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                String\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                MD5\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                默认为MD5\n            </td>\n        </tr>\n    </tbody>\n</table>\n<p>\n    <br/>\n</p>\n<p>\n    <br/>\n</p>'},{id:2,name:"异步通知",biaoti:'<h3 style="margin-top: 20px; margin-bottom: 10px; font-family: &quot;Microsoft YaHei&quot;, Arial, &quot;Hiragino Sans GB&quot;, STHeiti, &quot;WenQuanYi Micro Hei&quot;, SimSun, sans-serif; font-weight: 500; line-height: 1.1; font-size: 24px; white-space: normal; background-color: rgb(255, 255, 255);">\n    <b>支付结果通知</b>\n</h3>\n<p style="margin-bottom: 10px; font-family: &quot;Microsoft YaHei&quot;, Arial, &quot;Hiragino Sans GB&quot;, STHeiti, &quot;WenQuanYi Micro Hei&quot;, SimSun, sans-serif; font-size: 14px; background-color: rgb(255, 255, 255);">\n    通知类型：服务器异步通知（notify_url）、页面跳转通知（return_url）\n</p>\n<p style="margin-bottom: 10px; font-family: &quot;Microsoft YaHei&quot;, Arial, &quot;Hiragino Sans GB&quot;, STHeiti, &quot;WenQuanYi Micro Hei&quot;, SimSun, sans-serif; font-size: 14px; background-color: rgb(255, 255, 255);">\n    请求方式：GET\n</p>\n<p style="margin-bottom: 10px; font-family: &quot;Microsoft YaHei&quot;, Arial, &quot;Hiragino Sans GB&quot;, STHeiti, &quot;WenQuanYi Micro Hei&quot;, SimSun, sans-serif; font-size: 14px; background-color: rgb(255, 255, 255);">\n    请求参数说明：\n</p>\n<table border="1" bordercolor="#d8d8d8" class="table table-bordered table-hover" width="847">\n    <thead>\n        <tr class="firstRow">\n            <th style="padding: 8px; text-align: left; line-height: 1.42857; vertical-align: bottom; border-top: 0px; border-bottom-width: 2px;">\n                字段名\n            </th>\n            <th style="padding: 8px; text-align: left; line-height: 1.42857; vertical-align: bottom; border-top: 0px; border-bottom-width: 2px;">\n                变量名\n            </th>\n            <th style="padding: 8px; text-align: left; line-height: 1.42857; vertical-align: bottom; border-top: 0px; border-bottom-width: 2px;">\n                必填\n            </th>\n            <th style="padding: 8px; text-align: left; line-height: 1.42857; vertical-align: bottom; border-top: 0px; border-bottom-width: 2px;">\n                类型\n            </th>\n            <th style="padding: 8px; text-align: left; line-height: 1.42857; vertical-align: bottom; border-top: 0px; border-bottom-width: 2px;">\n                示例值\n            </th>\n            <th style="padding: 8px; text-align: left; line-height: 1.42857; vertical-align: bottom; border-top: 0px; border-bottom-width: 2px;">\n                描述\n            </th>\n        </tr>\n    </thead>\n    <tbody>\n        <tr>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                商户ID\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                pid\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                是\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                Int\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                1001\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;"></td>\n        </tr>\n        <tr>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                系统订单号\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                trade_no\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                是\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                String\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                20160806151343349021\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                平台的订单号\n            </td>\n        </tr>\n        <tr>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                商户订单号\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                out_trade_no\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                是\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                String\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                20160806151343349\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                商户提交时传入的订单号\n            </td>\n        </tr>\n        <tr>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                支付方式\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                type\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                是\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                String\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                alipay\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                alipay:支付宝,wxpay:微信支付<br/>qqpay:QQ钱包\n            </td>\n        </tr>\n        <tr>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                商品名称\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                name\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                是\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                String\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                VIP会员\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;"></td>\n        </tr>\n        <tr>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                商品金额\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                money\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                是\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                String\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                1.00\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;"></td>\n        </tr>\n        <tr>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                支付状态\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                trade_status\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                是\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                String\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                TRADE_SUCCESS\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                只有TRADE_SUCCESS是成功\n            </td>\n        </tr>\n        <tr>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                签名字符串\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                sign\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                是\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                String\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                202cb962ac59075b964b07152d234b70\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                详见签名算法一节\n            </td>\n        </tr>\n        <tr>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                签名类型\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                sign_type\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                是\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                String\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                MD5\n            </td>\n            <td style="padding: 8px; line-height: 1.42857; vertical-align: top;">\n                默认为MD5\n            </td>\n        </tr>\n    </tbody>\n</table>\n<p>\n    <br/>\n</p>'},{id:3,name:"签名规则",biaoti:'<h2 style="margin-bottom: 15px;font-family: &quot;Microsoft YaHei&quot;, Arial, &quot;Hiragino Sans GB&quot;, STHeiti, &quot;WenQuanYi Micro Hei&quot;, SimSun, sans-serif; font-weight: 500; line-height: 1.1; font-size: 24px; white-space: normal;"><b>MD5签名算法</b></h2>\n\n<p style="margin-bottom: 15px">所有接口都需签名验证 方式统一 回调签名也是同理<br />\n空值不参与签名 除外所有参数都参与签名 所有值都是原生的不要进行任何处理</p>\n\n<p style="margin-bottom: 15px">假如签名数据为 A<br />\n1 . 将A按照 字段名的ASCII码从小到大排序<br />\n2 . 除Sign 与 空值参数外 所有参数进行Url字符串形式的拼接 例：appid=12345678&amp;out_trade_no=xxx....<br />\n假设Url字符串为B<br />\n3 . 将B与商户对接Key进行对接 b+key 注意 + 为各语言的分隔符 并不是字符！ 直接拼接！<br />\n<strong style="margin-bottom: 3px">最后MD5一下即为Sign签名</strong></p>\n\n<p style="margin-bottom: 3px">简单实例：<br />\n相关语言可直接复制调用<br />\n<em>PHP：</em></p>\n\n<pre>\n<code class="language-php">function getSign()\n{\n    $signPars = "";\n    ksort($signPars);\n    foreach ($signPars as $k=&gt;$v) {\n        if($k != "sign" &amp;&amp; $v != ""){\n            $signPars .= $k."=".$v."&amp;";\n        }\n    }\n    $signPars = trim($signPars,"&amp;");\n    $signPars .= $key;\n    $sign = md5($signPars);\n    return $sign;\n}</code></pre>\n\n<p><em>JAVA：</em></p>\n\n<pre><code>\n/**\n * 签名生成算法\n * @param HashMap params 请求参数集，所有参数必须已转换为字符串类型\n * @param String secret 签名密钥\n * @return 签名\n * @throws IOException\n */\npublic static String getSignature(HashMap<String,String> params, String secret) throws IOException\n{// 先将参数以其参数名的字典序升序进行排序\n    Map<String, String> sortedParams =newTreeMap<String, String>(params);\n    Set<Entry<String, String>> entrys = sortedParams.entrySet();// 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起\n    StringBuilder basestring =newStringBuilder();for(Entry<String, String> param : entrys){\n        basestring.append(param.getKey()).append("=").append(param.getValue());}\n    basestring.append(secret);// 使用MD5对待签名串求签\n    byte[] bytes =null;try{\n        MessageDigest md5 = MessageDigest.getInstance("MD5");\n        bytes = md5.digest(basestring.toString().getBytes("UTF-8"));}catch(GeneralSecurityException ex){thrownewIOException(ex);}// 将MD5输出的二进制结果转换为小写的十六进制\n    StringBuilder sign =newStringBuilder();for(int i =0; i < bytes.length; i++){\n        String hex = Integer.toHexString(bytes[i]&0xFF);if(hex.length()==1){\n            sign.append("0");}\n        sign.append(hex);}return sign.toString();}\n</code></pre>\n\n<p><em>易语言：</em></p>\n\n<pre><code>\n.子程序 sign计算, 文本型, 公开\n.参数 param, 文本型\n.参数 key, 文本型\n.局部变量 signpars, 文本型\n.局部变量 分割, 文本型,,"0".局部变量 x, 整数型\n.局部变量 k, 文本型\n.局部变量 v, 文本型\n.局部变量 sign, 文本型\n\n分割 ＝ 分割文本 (param, “&”,)\n数组_排序 (分割,0,).计次循环首 (取数组成员数 (分割), x)\n    k ＝ 文本_取左边 (分割 [x], “=”,,)\n    v ＝ 文本_取右边 (分割 [x], “=”,,).如果真 (k ≠ “sign” 且 v ≠ “”).如果 (x ＝ 取数组成员数 (分割))\n            signpars ＝ signpars ＋ k ＋ “=” ＋ v\n        .否则\n            signpars ＝ signpars ＋ k ＋ “=” ＋ v ＋ “&”\n        .如果结束\n\n    .如果真结束\n\n.计次循环尾 ()\nsignpars ＝ signpars ＋ key\nsign ＝ 取数据摘要 (到字节集 (signpars))\n返回 (sign)\n</code></pre>\n\n<p><em>C#：</em></p>\n\n<pre><code>\n/// 计算参数签名///\n/// 请求参数集，所有参数必须已转换为字符串类型/// 签名密钥/// 签名\npublic static string getSignature(IDictionary<string, string> parameters, string secret){// 先将参数以其参数名的字典序升序进行排序\n    IDictionary<string, string> sortedParams =newSortedDictionary<string, string>(parameters);\n    IEnumerator<KeyValuePair<string, string>> iterator= sortedParams.GetEnumerator();// 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起\n    StringBuilder basestring=newStringBuilder();while(iterator.MoveNext()){\n            string key = iterator.Current.Key;\n            string value = iterator.Current.Value;if(!string.IsNullOrEmpty(key)&&!string.IsNullOrEmpty(value)){\n                basestring.Append(key).Append("=").Append(value);}}\n    basestring.Append(secret);// 使用MD5对待签名串求签\n    MD5 md5 = MD5.Create();\n    byte[] bytes = md5.ComputeHash(Encoding.UTF8.GetBytes(basestring.ToString()));// 将MD5输出的二进制结果转换为小写的十六进制\n    StringBuilder result =newStringBuilder();for(int i =0; i < bytes.Length; i++){\n            string hex = bytes[i].ToString("x");if(hex.Length ==1){\n                result.Append("0");}\n            result.Append(hex);}return result.ToString();}\n</code></pre>'}],a=[{id:1,pic:"https://img.alicdn.com/imgextra/i3/2905577656/O1CN01E4v4WA26QV06DZhwS_!!2905577656.png",name:"个人认证",biaoti:"用个人身份信息进行认证",quanxian:1},{id:2,pic:"https://img.alicdn.com/imgextra/i1/2905577656/O1CN01xvAILK26QUzziMaBu_!!2905577656.png",name:"企业认证",biaoti:"用企业身份信息进行认证",quanxian:0}],l=[{value:"1",label:"大陆居民身份证"},{value:"2",label:"港澳通行证"},{value:"3",label:"台湾通行证"},{value:"4",label:"港澳居住证"},{value:"5",label:"台湾居住证"}]}});