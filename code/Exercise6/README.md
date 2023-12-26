# Code of your exercise

```java
// TCC class
public class TCCNode {
    List<String> instanceVariable;

    public TCCNode(List<String> instanceVariable) {
        this.instanceVariable = instanceVariable;
    }

    public boolean sharesInstanceVariable(TCCNode other) {
        for(String variable : instanceVariable) {
            if(other.instanceVariable.contains(variable)) {
                return true;
            }
        }
        return false;
    }
}
```

```java
// TCC Calculator class
public class TCCCalculator extends VoidVisitorAdapter<Void> {
    private List<String> instanceVariable;
    private List<String> methodInstanceVariable;
    private List<TCCNode> nodes;

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        instanceVariable = declaration.getFields()
                .stream()
                .map(FieldDeclaration::getVariables)
                .flatMap(List::stream)
                .map(v -> v.getName().asString())
                .toList();
        nodes = new ArrayList<>();

        // Create all TCC nodes
        for(MethodDeclaration method : declaration.getMethods()) {
            String currentMethod = method.getNameAsString(); // Used for debug
            methodInstanceVariable = new ArrayList<>();
            method.getBody().ifPresent(body -> body.accept(this, null));
            nodes.add(new TCCNode(methodInstanceVariable));
        }

        // Print TCC
        String currentClass = declaration.getFullyQualifiedName().get(); // Used for debug
        System.out.println("Class: " + currentClass + " - TCC: " + getTCC());
    }

    @Override
    public void visit(FieldAccessExpr expr, Void arg) {
        if(instanceVariable.contains(expr.getNameAsString()) && !methodInstanceVariable.contains(expr.getNameAsString())) {
            methodInstanceVariable.add(expr.getNameAsString());
        }
    }

    public float getTCC(){
        int nbEdges = 0;
        int nbPairs = 0;

        // Count edges and pairs
        for(int i = 0; i <nodes.size(); i++) {
            for(int j = i + 1; j < nodes.size(); j++) {
                nbPairs++;
                if(nodes.get(i).sharesInstanceVariable(nodes.get(j))) {
                    nbEdges++;
                }
            }
        }

        return (float) nbEdges / nbPairs;
    }
}
```

```java
// Test class
public class Point {
    private double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double dot(Point p) {
        return x*p.x + y*p.y;
    }

    public Point sub(Point p) {
        return new Point(x - p.x, y - p.y);
    }
}
```

## Output

```text
Class: Test.Point - TCC: 0.8333333
```