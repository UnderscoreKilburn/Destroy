package com.petrolpark.destroy.chemistry.serializer;

import java.util.*;
import java.util.Map.Entry;

import com.petrolpark.destroy.chemistry.legacy.LegacyAtom;
import com.petrolpark.destroy.chemistry.legacy.LegacyElement;
import com.petrolpark.destroy.chemistry.legacy.LegacyBond.BondType;

public class Node {
    private LegacyAtom atom;
    public Boolean visited;
    private List<Edge> edges;
    private Branch branch;
    private Map<Branch, BondType> sideBranches;

    public Node(LegacyAtom atom) {
        this.atom = atom;
        visited = false;
        edges = new ArrayList<>();
        sideBranches = new LinkedHashMap<>();
    };

    public String serialize() {
        String string = getAtom().getElement().getSymbol();
        Boolean isTerminal = true;
        Edge nextEdge = null;
        for (Edge edge : edges) {
            if (edge.getSourceNode() == this) {
                isTerminal = false;
                nextEdge = edge;
                break;
            };
        };
        if (atom.rGroupNumber != 0 && atom.getElement() == LegacyElement.R_GROUP) {
            string += atom.rGroupNumber;
        };
        if (atom.formalCharge != 0) {
            string += "^"+((atom.formalCharge % 1.0 != 0) ? String.format("%s", atom.formalCharge) :String.format("%.0f", atom.formalCharge));
        };
        if (!isTerminal && nextEdge != null) { // Also checking if the next edge is null is sort of redundant, but at least it gets rid of that nasty yellow squiggly line
            string += nextEdge.bondType.getFROWNSCode(); // It thinks 'nextEdge' can be null
        };
        for (Entry<Branch, BondType> entry : getSideBranches().entrySet()) {
            string += "(" + entry.getValue().getFROWNSCode() + entry.getKey().serialize() + ")"; // It thinks "nextEdge" is null
        };
        if (!isTerminal && nextEdge != null) {
            string += nextEdge.getDestinationNode().serialize();
        };
        return string;
    };

    public LegacyAtom getAtom() {
        return this.atom;
    };

    public Node addEdge(Edge edge) {
        edges.add(edge);
        return this;
    };

    public Node deleteEdge(Edge edge) {
        edges.remove(edge);
        return this;
    };

    public List<Edge> getEdges() {
        return edges;
    };

    public Node setBranch(Branch branch) {
        this.branch = branch;
        return this;
    };

    public Branch getBranch() {
        return this.branch;
    };

    public Node addSideBranch(Branch branch, BondType bondType) {
        sideBranches.put(branch, bondType);
        return this;
    };

    public Map<Branch, BondType> getSideBranches() {
        return sideBranches;
    };

    public List<Entry<Branch, BondType>> getOrderedSideBranches() {
        List<Entry<Branch, BondType>> sideBranchesAndBondTypes = new ArrayList<>(getSideBranches().entrySet());
        Collections.sort(sideBranchesAndBondTypes, (entry1, entry2) -> entry1.getKey().getMassOfLongestChain().compareTo(entry2.getKey().getMassOfLongestChain()));
        return sideBranchesAndBondTypes;
    };
};
