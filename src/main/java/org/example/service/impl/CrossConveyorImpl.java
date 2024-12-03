package org.example.service.impl;

import org.example.model.Node;
import org.example.service.CrossConveyor;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public class CrossConveyorImpl implements CrossConveyor {
    private final Map<String, Queue<Node>> conveyors;
    private final int nodesPerConveyor;

    public CrossConveyorImpl(int nodesPerConveyor) {
        this.conveyors = new ConcurrentHashMap<>();
        this.nodesPerConveyor = nodesPerConveyor;

        // shared nodes
        Node sharedNode1 = new Node();
        sharedNode1.setValue(10);
        sharedNode1.addConveyor("A");
        sharedNode1.addConveyor("B");
        sharedNode1.setShared(true);

        Node sharedNode2 = new Node();
        sharedNode2.setValue(20);
        sharedNode2.addConveyor("A");
        sharedNode2.addConveyor("B");
        sharedNode2.setShared(true);

        // conveyor A
        LinkedList<Node> conveyorA = new LinkedList<>();
        conveyorA.add(new Node(3));
        conveyorA.add(new Node(6));
        conveyorA.add(new Node(1));
        conveyorA.add(sharedNode1);
        conveyorA.add(new Node(3));
        conveyorA.add(new Node(2));
        conveyorA.add(sharedNode2);
        conveyorA.add(new Node(3));

        // conveyor B
        LinkedList<Node> conveyorB = new LinkedList<>();
        conveyorB.add(new Node(3));
        conveyorB.add(new Node(2));
        conveyorB.add(new Node(5));
        conveyorB.add(sharedNode1);
        conveyorB.add(new Node(6));
        conveyorB.add(new Node(3));
        conveyorB.add(sharedNode2);
        conveyorB.add(new Node(5));

        // add conveyors to map
        conveyors.put("A", conveyorA);
        conveyors.put("B", conveyorB);
    }

    @Override
    public ConveyorAccessor conveyor(String id) {
        return new ConveyorAccessorImpl(id, this);
    }

    public Queue<Node> getConveyor(String id) {
        return conveyors.get(id);
    }

    public int getNodesPerConveyor() {
        return nodesPerConveyor;
    }

    public void updateSharedNodes(LinkedList<Node> conveyor, String id) {
        for (int i = 1; i < conveyor.size(); i++) {
            Node currentNode = conveyor.get(i);

            if (currentNode.isShared()) {

                // find previous node to mark it as shared
                Node previousNode = conveyor.get(i - 1);
                previousNode.addConveyors(currentNode.getConveyors());
                previousNode.setShared(true);

                // find and change the shared node in both lists
                for (Map.Entry<String, Queue<Node>> entry : conveyors.entrySet()) {
                    String key = entry.getKey();
                    LinkedList<Node> otherConveyor = (LinkedList<Node>) entry.getValue();

                    if (!key.equals(id)) {
                        for (int j = 0; j < otherConveyor.size(); j++) {
                            if (otherConveyor.get(j) == currentNode) {
                                otherConveyor.set(j, previousNode);
                            }
                        }
                    }
                }

                // mark the old node as not shared
                currentNode.setShared(false);
                currentNode.getConveyors().clear();
            }
        }
    }

    private void printAllConveyors() {
        conveyors.forEach((key, queue) -> {
            System.out.println("Conveyor " + key + ": " + queue);
        });
    }

    private static class ConveyorAccessorImpl implements ConveyorAccessor {
        private final String id;
        private final CrossConveyorImpl conveyorSystem;

        public ConveyorAccessorImpl(String id, CrossConveyorImpl conveyorSystem) {
            this.id = id;
            this.conveyorSystem = conveyorSystem;
        }

        @Override
        public int put(int value) {
            System.out.println("A and B conveyors BEFORE updating.");
            conveyorSystem.printAllConveyors();
            LinkedList<Node> conveyor = (LinkedList<Node>) conveyorSystem.getConveyor(id);
            if (conveyor == null) {
                throw new IllegalArgumentException("Conveyor with ID " + id + " does not exist");
            }

            // add new node in the first of the list
            Node newNode = new Node(value);
            conveyor.addFirst(newNode);

            // find the last node of the list, which we are going to remove
            Node removedNode = null;
            if (conveyor.size() > conveyorSystem.getNodesPerConveyor()) {
                removedNode = conveyor.removeLast();
            }

            // update the shared nodes
            conveyorSystem.updateSharedNodes(conveyor, id);

            System.out.println("A and B conveyors AFTER updating.");
            conveyorSystem.printAllConveyors();

            return removedNode != null ? removedNode.getValue() : -1;
        }

    }
}
