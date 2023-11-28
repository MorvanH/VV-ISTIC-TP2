# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

You can use the default [rule base](https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/rulesets/java/quickstart.xml) available on the source repository of PMD.

## Answer

I found a true positive : 

ClassWithOnlyPrivateConstructorsShouldBeFinal:	This class has only private constructors and may be final

The solution is given in the issue, make it final 


I found a false positive : 

UncommentedEmptyConstructor:	Document empty constructor

The empty constructor is not necessary, but it helps to understand the context by showing an implicit constructor.
