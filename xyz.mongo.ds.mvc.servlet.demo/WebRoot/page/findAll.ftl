
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>脚部文件</title>

<link href="../../css/main.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div>
   <table width="99%" border="0" cellpadding="0" cellspacing="1" class="table_bg">
    <tr class="th_bg">
      <th>_id</th><th>用户名</th><th>密码</th><th>email</th><th>权限</th><th>模块</th><th>团队</th>
    </tr>
    <#list root as user>
     <tr onmouseover="this.className='td_bg_01';" onmouseout="this.className='td_bg';" class="td_bg">
      <td>${user._id}</td><td>${user.uName}</td><td>${user.psword}</td><td>${user.email}</td><td>${user.role}</td><td>${user.moduls}</td><td>${user.team}</td>
    </tr>
    </#list>
   </table>
     
</div>
</body>
</html>
