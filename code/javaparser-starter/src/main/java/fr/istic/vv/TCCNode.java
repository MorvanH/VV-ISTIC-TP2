package fr.istic.vv;

import java.util.ArrayList;
import java.util.List;

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
