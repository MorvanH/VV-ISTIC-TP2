# Code of your exercise

````java
// Control Flow Graph (CFG) class
public class CFGNode {
    enum NodeType{
        START,
        END,
        COND,
        RETURN,
        BASIC
    }

    private final NodeType type;
    private CFGNode left;
    private CFGNode right;

    public CFGNode(NodeType type){
        this.type = type;
        this.left = null;
        this.right = null;
    }

    public CFGNode(NodeType type, CFGNode left, CFGNode right){
        this.type = type;
        this.left = left;
        this.right = right;
    }



    public void setNode(CFGNode newNode){
        switch(this.type){
            case START:
                this.left = newNode;
                break;
            case END:
                System.out.println("Error: Node end cannot have children");
                exit(1);
            case COND:
                if(this.left == null || this.left.type == NodeType.END){
                    this.left = newNode;
                }
                else if(this.right == null){
                    this.right = newNode;
                }
                else{
                    System.out.println("Error: Node already has two children");
                    exit(1);
                }
                break;
            case RETURN:
                if (this.left == null) {
                    this.left = newNode;
                }
                else{
                    System.out.println("Error: Node already has a child");
                    exit(1);
                }
                break;
            case BASIC:
                if (this.left == null || this.left.type == NodeType.END) {
                    this.left = newNode;
                } else {
                    System.out.println("Error: Node already has a child");
                    exit(1);
                }
                break;
        }
    }

    public float getCyclomaticComplexity() {
        int nbNodes = getNbNodes(new HashSet<>());
        int nbEdges = getNbEdges(new HashSet<>());
        return nbEdges - nbNodes + 2;
    }

    private int getNbNodes(Set<CFGNode> visitedNodes){
        if(visitedNodes.contains(this)){
            return 0;
        }
        visitedNodes.add(this);
        switch (this.type){
            case START:
                return 2 + this.left.getNbNodes(visitedNodes); // +2 for the start and end nodes
            case RETURN:
                return 1;
            case COND:
                if(this.right == null){
                    return 1 + this.left.getNbNodes(visitedNodes);
                }
                else{
                    return 1 + this.left.getNbNodes(visitedNodes) + this.right.getNbNodes(visitedNodes);
                }
            case BASIC:
                return 1 + this.left.getNbNodes(visitedNodes);
        }

        return 0;
    }

    private int getNbEdges(Set<CFGNode> visitedNodes){
        if(visitedNodes.contains(this)){
            return 0;
        }
        visitedNodes.add(this);
        switch (this.type){
            case START, BASIC:
                return 1 + this.left.getNbEdges(visitedNodes);
            case END:
                return 0;
            case COND:
                if(this.right == null){
                    return 1 + this.left.getNbEdges(visitedNodes);
                }
                else{
                    return 2 + this.left.getNbEdges(visitedNodes) + this.right.getNbEdges(visitedNodes);
                }
            case RETURN:
                return 1;
        }

        return 0;
    }
}
````

````java
// Cyclomatic Complexity Calculator class
class CyclomaticComplexityCalculator extends VoidVisitorAdapter<Void> {
    private CFGNode root;
    private CFGNode currentNode;
    private CFGNode lastNode;

    public float getCyclomaticComplexity() {
        return root.getCyclomaticComplexity();
    }

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        for(MethodDeclaration method : declaration.getMethods()) {
            String currentMethod = method.getNameAsString();
            this.root = new CFGNode(CFGNode.NodeType.START);
            this.currentNode = this.root;
            this.lastNode = new CFGNode(CFGNode.NodeType.END);
            root.setNode(lastNode);

            method.getBody().ifPresent(body -> body.accept(this, null));

            System.out.println("Method: " + currentMethod + " - Cyclomatic Complexity: " + getCyclomaticComplexity());
        }
    }

    @Override
    public void visit(IfStmt stmt, Void arg) {
        CFGNode ifNode = new CFGNode(CFGNode.NodeType.COND);
        currentNode.setNode(ifNode);
        currentNode = ifNode;

        stmt.getThenStmt().accept(this, null);
        currentNode = ifNode;
        if(stmt.hasElseBlock()){
            stmt.getElseStmt().get().accept(this, null);
        }
    }

    @Override
    public void visit(WhileStmt stmt, Void arg) {
        CFGNode whileNode = new CFGNode(CFGNode.NodeType.COND);
        currentNode.setNode(whileNode);
        currentNode = whileNode;
        stmt.getBody().accept(this, null);

        // If the condition is false, the loop is not executed
        currentNode.setNode(whileNode);
        currentNode = whileNode;
    }

    @Override
    public void visit(ForStmt stmt, Void arg) {
        CFGNode assignNode = new CFGNode(CFGNode.NodeType.BASIC);
        currentNode.setNode(assignNode);
        currentNode = assignNode;
        CFGNode forCondNode = new CFGNode(CFGNode.NodeType.COND);
        currentNode.setNode(forCondNode);
        currentNode = forCondNode;
        stmt.getBody().accept(this, null);

        // If the condition is false, the loop is not executed
        currentNode.setNode(forCondNode);
        currentNode = forCondNode;

    }

    @Override
    public void visit(DoStmt stmt, Void arg) {
        CFGNode doNode = new CFGNode(CFGNode.NodeType.COND);
        currentNode.setNode(doNode);
        currentNode = doNode;
        stmt.getBody().accept(this, null);

        // If the condition is false, the loop is not executed
        currentNode.setNode(doNode);
        currentNode = doNode;
    }

    @Override
    public void visit(ReturnStmt stmt, Void arg) {
        CFGNode returnNode = new CFGNode(CFGNode.NodeType.RETURN);
        returnNode.setNode(lastNode);
        currentNode.setNode(returnNode);
        currentNode = returnNode;
    }

    @Override
    public void visit(ExpressionStmt stmt, Void arg) {
        CFGNode blockNode = new CFGNode(CFGNode.NodeType.BASIC);
        currentNode.setNode(blockNode);
        currentNode = blockNode;
        blockNode.setNode(lastNode);
    }
}
````

````java
// Test class
public class Test {
    private int a = 0;

    public int getA() {
        return a;
    }

    public static int max(int a, int b, int c) {
        if (a > b) {
            if(a > c) {
                return a;
            }
            else {
                return c;
            }
        }
        else {
            if (b > c) {
                return b;
            }
            else {
                return c;
            }
        }
    }

    public static int CFGWhileTest(int a) {
        int b = 10;
        while(a > 0) {
            a--;
            if(a % b == 0) {
                return a;
            }
        }

        return a;
    }

    public static int CFGIfTest(int a) {
        int b = 10;
        if(a > b) {
            return a;
        }
        return b;
    }

    public static int CFGIfElseTest(int a) {
        int b = 10;
        if(a > b) {
            return a;
        }
        else {
            return b;
        }
    }

    public static int CFGDoWhileTest(int a) {
        int b = 10;
        do {
            a--;
            if(a % b == 0) {
                return a;
            }

            int c = 10;
            while (c > 0) {
                c--;
            }
        } while(a > 0);

        return a;
    }

    public static int CFGForTest(int a) {
        int b = 10;
        for(int i = 0; i < a; i++) {
            if(i - b == 0) {
                return i;
            }
            b--;
        }

        return a;
    }
}
````

## Output

````text
Method: getA - Cyclomatic Complexity: 1.0
Method: max - Cyclomatic Complexity: 4.0
Method: CFGWhileTest - Cyclomatic Complexity: 3.0
Method: CFGIfTest - Cyclomatic Complexity: 2.0
Method: CFGIfElseTest - Cyclomatic Complexity: 2.0
Method: CFGDoWhileTest - Cyclomatic Complexity: 4.0
Method: CFGForTest - Cyclomatic Complexity: 3.0
````