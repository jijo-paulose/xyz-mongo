(function( window, undefined ){
    if(!window.xyz){
       window.xyz=(function(){
          //定义并且初始化XmlHttpRequest
          this.xhr=function(window){
              if(window.ActiveXObject) {
                  return new ActiveXObject('Msxml2.XMLHTTP');
             }else if(window.XMLHttpRequest){
                 return new XMLHttpRequest();
             }else{
                 alert("no xmlhttpRequest support!");
             }
          }(window);
          //定义json函数
          this.json=function(type,url,sucess,failure,timeout){
              if(window.ActiveXObject){
                  xhr=new ActiveXObject('Msxml2.XMLHTTP');
              };
              xhr.onreadystatechange = function(){
                 if(xhr.readyState == 4){
                      try{
                        var s = xhr.status;
                        if(s>= 200 && s < 300){
                           sucess(xhr.responseText);
                       }else{
                           failure(xhr);
                       }                      
                      }catch(e){
                          alert("e--"+e);
                      }
               }else{
                   //now do nothing
               }
             };
             //xhr.ontimeout = timeout;
             if(xhr.ontimeout){
                 try{xhr.ontimeout = timeout;}catch(e){};
             }
            xhr.open(type, url, false); 
            xhr.send(null);
          };
          //定义jsonp函数,跨域，但是只能get，不能post啦
           this.jsonp=function(url){
              var script = document.createElement('script');
              script.setAttribute('src', url);
              document.getElementsByTagName('head')[0].appendChild(script); 
          };
          return this;
      })(window);
    };
 })(window);