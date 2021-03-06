package org.jruby.compiler.ir.dataflow.analyses;

import org.jruby.compiler.ir.dataflow.DataFlowProblem;
import org.jruby.compiler.ir.dataflow.FlowGraphNode;
import org.jruby.compiler.ir.operands.LocalVariable;
import org.jruby.compiler.ir.representations.BasicBlock;
import org.jruby.compiler.ir.representations.CFG;

import java.util.HashSet;
import java.util.ListIterator;
import java.util.Set;

public class BindingLoadPlacementProblem extends DataFlowProblem
{
/* ----------- Public Interface ------------ */
    public BindingLoadPlacementProblem()
    { 
        super(DataFlowProblem.DF_Direction.BACKWARD);
        _initLoadsOnExit = new java.util.HashSet<LocalVariable>();
        _bindingHasEscaped = false;
    }

    public String        getName() { return "Binding Loads Placement Analysis"; }
    public FlowGraphNode buildFlowGraphNode(BasicBlock bb) { return new BindingLoadPlacementNode(this, bb);  }
    public String        getDataFlowVarsForOutput() { return ""; }
    public void          initLoadsOnScopeExit(Set<LocalVariable> loads) { _initLoadsOnExit = loads; }
    public Set<LocalVariable> getLoadsOnScopeExit() { return _initLoadsOnExit; }
    public boolean       bindingHasEscaped() { return _bindingHasEscaped; }
    public void          setBindingHasEscaped(boolean flag) { _bindingHasEscaped = flag; }

    public boolean scopeDefinesVariable(LocalVariable v) { 
        return getCFG().definesLocalVariable(v);
    }

    public boolean scopeUsesVariable(LocalVariable v) { 
        return getCFG().usesLocalVariable(v);
    }

    public void addLoads()
    {
        for (FlowGraphNode n: _fgNodes) {
            BindingLoadPlacementNode blpn = (BindingLoadPlacementNode)n;
            blpn.addLoads();
        }
    }

/* ----------- Private Interface ------------ */
    private Set<LocalVariable> _initLoadsOnExit;
    private boolean _bindingHasEscaped; // Has this method's (or the containing method's binding in the case of a closure) binding escaped?
}
