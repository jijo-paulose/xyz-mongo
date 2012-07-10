
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />


<link href="../css/main.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div>
     很好，添加用户成功，用户_id是:<strong>${_id}</strong><br>

   <a href="../../user!findById!jsp!/page/findById.ds?id=${_id}">jsp查看结果：</a><br>
     <a href="../../user!findById!ftl!/page/findById.ds?id=${_id}">freemarker查看结果：</a><br>
     <a href="../../user!findById!vm!/page/findById.ds?id=${_id}">velocity查看结果：</a><br>
      <a href="../../user!findById!json!id.ds?id=${_id}">json查看结果：</a><br>
      <a href="javascript:jsonView();">json查看结果Alert：</a><br>
      <a href="../../user!findById!jsonp!findById.ds?id=${_id}">jsonp查看结果：</a><br>
      <a href="javascript:jsonpView();">jsonp查看结果Alert：</a><br>
</div>
<script language="javascript" src="../../js/xyz.js?dd"></script> 
 <script>
   function jsonView(){
       xyz.json("get",
           "../../user!findById!json!id.ds?id=${_id}",
           function(resText){
               alert("resText:"+resText);
           },
           function(xhr){
               alert("failer"+xhr);
           },
           function(){
               alert("time out!");
           }
       );
       
   }
   function findById_jsonp(data){
        alert("jsonp {id:"+data._id+",uName:"+data.uName+",others}");
   }
   function jsonpView(){
       //本服务器的url是localhost，用json，xmlHttpRequest是不可以滴
       xyz.jsonp(
           "http://127.0.0.1:8080/xyz.mongo.ds.mvc.servlet.demo/user!findById!jsonp!findById.ds?id=${_id}"
       );
       
   }
</script> 
</body>
</html>
