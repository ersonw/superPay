(function(w){

    var showLoadingOnOff = false;
    var loadingBox = null;
    var loadingTxt = null;
    var loadingTips = null;
    var timer = null;
    var oW = null;
    function startLoading(objInfo){

        var mTitle = objInfo && objInfo.title || "请稍候";
        var tips = objInfo && objInfo.tips || "加载中...";

        if(showLoadingOnOff == false){

            loadingBox = document.createElement("div");
            loadingBox.style.cssText = `width:120px;height:120px;position:absolute;top:50%;left:50%;margin-left:-60px;margin-top:-60px;background:rgba(0,0,0,0.5);border-radius:10px;overflow:hidden;`;

            loadingTips = document.createElement("p");
            loadingTips.style.cssText = `white-space:nowrap;height:30px;text-align:center;margin:30px auto 20px;color:white;`;

            loadingTxt = document.createElement("p");
            loadingTxt.style.cssText = `text-align:center;color:#f5f5f5;font-size:15px;`;

            loadingBox.appendChild(loadingTips);
            loadingBox.appendChild(loadingTxt);
            document.body.appendChild(loadingBox);
        }
        loadingTxt.innerHTML = mTitle;
        loadingTips.innerHTML = tips;
        showLoadingOnOff = true;
        loadingBox.style.display = "block";

        oW = loadingTips.offsetWidth;
        loadingTips.style.width = "0px";
        loadingTips.style.overflow = "hidden";

        var iW = 0;
        timer = setInterval(function(){

            iW += 16;
            if(iW >= oW){

                iW=0;
            }
            loadingTips.style.width = iW+"px";
        },100);
    }

    function stopLoading(){

        clearInterval(timer);
        loadingBox.style.display = "none";
        loadingTips.style.width = oW+"px";
        timer = null;
    }
    w.startLoading = startLoading;
    w.stopLoading = stopLoading;

})(window);