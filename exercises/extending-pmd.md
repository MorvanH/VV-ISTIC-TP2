# Extending PMD

Use XPath to define a new rule for PMD to prevent complex code. The rule should detect the use of three or more nested `if` statements in Java programs so it can detect patterns like the following:

```Java
if (...) {
    ...
    if (...) {
        ...
        if (...) {
            ....
        }
    }

}
```
Notice that the nested `if`s may not be direct children of the outer `if`s. They may be written, for example, inside a `for` loop or any other statement.
Write below the XML definition of your rule.

You can find more information on extending PMD in the following link: https://pmd.github.io/latest/pmd_userdocs_extending_writing_rules_intro.html, as well as help for using `pmd-designer` [here](https://github.com/selabs-ur1/VV-ISTIC-TP2/blob/master/exercises/designer-help.md).

Use your rule with different projects and describe you findings below. See the [instructions](../sujet.md) for suggestions on the projects to use.

## Answer

Here is the XPath to found all three or more nested `if` statements : `//IfStatement[.//IfStatement[.//IfStatement]]`

It gives me the following xml rule :

```xml
<rule name="NestedIfStatements"
language="java"
message="Avoid nested if statements (3 levels)"
class="net.sourceforge.pmd.lang.rule.XPathRule">
<description>
<![CDATA[
Avoid the use of deeply nested if statements.
]]>
</description>
<priority>3</priority>
<properties>
<property name="xpath">
<value><![CDATA[
                //IfStatement[.//IfStatement[.//IfStatement]]
            ]]></value>
</property>
</properties>
</rule>
```

I used it on `commons-collections` by Apache and even here, the rule raised multiple warning about nested if.

That shows how usefully could be a static analysis in the workflow of pipelines used for integration of any company.