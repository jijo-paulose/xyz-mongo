<h1>xyz.mongo.core</h1><p>
提供工程的基础接口和缺省实现<p>
<h1>xyz.mongo.ds</h1><p>
<ol>
<blockquote><li><h3>为什么需要Mongo DS</h3></li>
<blockquote><ol>
<blockquote><li>mongodb driver编程复杂，用户容易陷入技术细节之陷阱，忽略业务逻辑</li>
<li>spring mongodb template编程简单，但是由于基于域对象，所以，在mongo collection发生任何变化时，需要改变Domain class，而mongodb最大的设计优势就是无模式，易于扩展，基于对象的设计，将丧失这个设计优势</li>
</blockquote><li>基于Domain对象的设计，获取mongodb json对象后要转换成domain对象，但是在数据传输时的DTO，或者使用的VO，往往又是一个json，这样，就需要再次反转换，白白转换两次，对CPU是个大大的浪费</li>
</ol>
</blockquote><li><h3>Mongo DS提供什么</h3></li>
<blockquote><ol>
<blockquote><li>提供count,distinct,exist,group,insert,list,one,page,remove,save,son,top,update,mulit,transvalue等15种基础操作，而且，开发者可以轻易的扩展ICollectionExceuter实现自己的基础操作</li>
</blockquote><blockquote><li>基于基础操作，使用json配置文件提供各种对Collection的操作服务，由于json和mongo shell一致，有利于用户掌握。对于开发者来说，也可以轻松实现自己的配置文件管理，比如，基于Excel文件，xml文件，甚至mongodb 表数据文件</li>
</blockquote><blockquote><li>基于freemarker的动态查找支持</li>
<li>基于basetable.json的基础操作，对于写入basetable.json里的操作，用户甚至可以不写一行代码，就可以直接调用该操作</li>
</blockquote></ol>
</blockquote></blockquote><blockquote><li><h3>Mongo DS的优势</h3></li>
<blockquote><ol>
<blockquote><li>开发速度优势，由于绝大部分开发只需要几行json字符串，而这几行json字符串又只需要从mongo shell拷贝过来，根据freemarker语法改吧改吧，所以，开发速度加快</li>
<li>运行性能优势，相对于spring mongodb template，由于少了Converter转换，速度大大加快</li>
</blockquote></ol>
</blockquote><li><h3>Mongo DS的注意事项</h3></li>
<blockquote><ol>
<blockquote><li>由于Mongo DS的代码过于简单，所以文档、注释必须齐全</li>
</blockquote></ol>
</ol></blockquote></blockquote>

<h1>xyz.mongo.ds.servlet</h1><p>
<ol>
<blockquote><li><h3>为什么Mogno Ds Servlet</h3></li>
<ol>
<blockquote><li>Java Servlet只提供RequestParameter的访问功能，不能讲其转换成DS需要的Json对象</li>
<li>Struts，Spring MVC等只提供RequestParameter转换成Action中pojo属性对象的功能，还需要再次转换成JSON对象</li>
</blockquote></ol>
<li><h3>Mogno Ds Servlet带来什么</h3></li>
<ol>
<blockquote><li>自动将RequestParameter，Attribute，HttpSession，HttpServletContext放入json上下文</li>
<li>可扩展的Result实现和五种缺省的Result（jsp，freemarker，velocity，json，jsonp），而且这五种Result的VO都是json</li>
</blockquote></ol>
</ol></blockquote>

<h1>xyz.mongo.ds.servlet.demo</h1><p>
利用xyz.mongo.core，xyz.mongo.ds，xyz.mongo.ds.servlet开发一个java web工程，提供一个简单表的CURD操作<p>
<h1>xyz.mongo.objan,objan</h1><p>
注解和面向对象的合体，我不是一个注解迷，但是，在java中，利用注解和aop，确实可以做很多事情，几乎是一切想做的事情，这个工程只不过是xyz.mongo.ds的Domain驱动实现，对于DDD爱好者来说，确实是一个好一点的选择<p>