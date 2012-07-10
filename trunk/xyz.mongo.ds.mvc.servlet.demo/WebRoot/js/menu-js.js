<!--
// Weboffice 左菜单Javascript代码
function turnit(ss)
{
//新加的
 sss = document.getElementById(ss);
 if(sss!=undefined)ss=sss;
//
 if (ss.style.display=="none") 
  {ss.style.display="";
  }
 else
  {ss.style.display="none"; 
  }
}
function turnitoff(ss)
{
if (ss.style.display=="block") 
  {ss.style.display="none";
  }
 else
  {ss.style.display="none"; 
  }
}
function turniton(ss)
{
if (ss.style.display=="block") 
  {ss.style.display="block";
  }
 else
  {ss.style.display="block"; 
  }
}
function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}
//-->