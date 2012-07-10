
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="../../css/main.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div>
   <table width="99%" border="0" cellpadding="0" cellspacing="1" class="table_bg">
    <tr class="th_bg">
      <th>_id</th><th>用户名</th><th>密码</th><th>email</th><th>权限</th><th>模块</th><th>团队</th><th>操作</th>
    </tr>
    <#list result as user>
     <tr onmouseover="this.className='td_bg_01';" onmouseout="this.className='td_bg';" class="td_bg">
      <td>${user._id}</td><td>${user.uName}</td><td>${user.psword}</td><td>${user.email}</td><td>${user.role}</td><td>${user.moduls}</td><td>${user.team}</td>
      <td><a href="javascript:jsonView('${user._id}');">json查看</a></td>
    </tr>
    </#list>
    <tr class="th_bg">
       <td colspan=8 align=center>
       <a href="../../user!findPage!ftl!/page/findPage.ds?page.number=1&page.size=6">第一页</a>||
       <#if true>
       <a href="../../user!findPage!ftl!/page/findPage.ds?page.number=${number-1}&page.size=6">前页</a>||
       </#if>
       <a href="../../user!findPage!ftl!/page/findPage.ds?page.number=${number}&page.size=6">当前页</a>||
       <#if true >
       <a href="../../user!findPage!ftl!/page/findPage.ds?page.number=${number+1}&page.size=6">后页</a>||
       </#if>
       <a href="../../user!findPage!ftl!/page/findPage.ds?page.number=${totalPages}&page.size=6">最后一页</a>
       </td>
    </tr>
   </table>
     
</div>
<script language="javascript" src="../../js/xyz.js?1"></script> 
 <script>
   function jsonView(myid){
       xyz.json("get",
           "../../user!findById!json!id.ds?id="+myid,
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
  </script>
</body>
</html>
