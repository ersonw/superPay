webpackJsonp([25],{G9Qu:function(e,t){},eBPz:function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a=n("qUen"),c=n("7Zj/"),s={data:function(){return{activeName:"icons",iconList:a.a,elIconList:c.a}},methods:{copyIcon:function(e){var t=document.createElement("input");t.setAttribute("value",e),document.body.appendChild(t),t.select(),document.execCommand("copy"),document.body.removeChild(t),this.$message.success({message:"复制成功",duration:1500})}}},i={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"page-content"},[n("el-tabs",{attrs:{type:"card"},model:{value:e.activeName,callback:function(t){e.activeName=t},expression:"activeName"}},[n("el-tab-pane",{attrs:{label:"Icons",name:"icons"}}),e._v(" "),n("el-tab-pane",{attrs:{label:"Element icon",name:"element"}})],1),e._v(" "),n("div",{staticClass:"el-tabs-content"},[n("ul",{directives:[{name:"show",rawName:"v-show",value:"icons"===e.activeName,expression:"activeName === 'icons'"}],staticClass:"icon-list"},e._l(e.iconList,function(t){return n("li",{key:t,on:{click:function(n){return e.copyIcon(t)}}},[n("i",{staticClass:"iconfont",domProps:{innerHTML:e._s(t)}}),e._v(" "),n("span",[e._v(e._s(t))])])}),0),e._v(" "),n("ul",{directives:[{name:"show",rawName:"v-show",value:"element"===e.activeName,expression:"activeName === 'element'"}],staticClass:"icon-list"},e._l(e.elIconList,function(t){return n("li",{key:t,on:{click:function(n){return e.copyIcon(t)}}},[n("i",{class:t}),e._v(" "),n("span",[e._v(e._s(t))])])}),0)])],1)},staticRenderFns:[]};var o=n("VU/8")(s,i,!1,function(e){n("G9Qu")},"data-v-4ad38e17",null);t.default=o.exports}});