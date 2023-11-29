# Code of your exercise

```xml
<rule name="MyFirstRule"
language="java"
message="IfStatement should never have a depth > 3"
class="net.sourceforge.pmd.lang.rule.XPathRule">
<description>

   </description>
   <priority>3</priority>
   <properties>
      <property name="version" value="3.1"/>
      <property name="xpath">
         <value>
<![CDATA[
//IfStatement//IfStatement//IfStatement
]]>
         </value>
      </property>
   </properties>
</rule>

```
