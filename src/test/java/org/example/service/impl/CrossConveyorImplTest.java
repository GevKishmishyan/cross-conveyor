package org.example.service.impl;

import org.example.model.Node;
import org.example.service.CrossConveyor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

class CrossConveyorImplTest {

    private CrossConveyor conveyor;

    @BeforeEach
    void setUp() {
        conveyor = new CrossConveyorImpl(8);
    }

    @Test
    void testPutAddsNodeToConveyorA() {
        // given
        int removedValue = conveyor.conveyor("A").put(7);
        Queue<Node> conveyorA = ((CrossConveyorImpl) conveyor).getConveyor("A");

        // then
        assertEquals(3, removedValue, "The oldest node in Conveyor A should be removed when exceeding the limit.");
        assertNotNull(conveyorA, "Conveyor A should not be null.");
        assertEquals(8, conveyorA.size(), "Conveyor A should maintain its size limit of 8.");
        assertEquals(7, ((LinkedList<Node>) conveyorA).getFirst().getValue(), "The first node in Conveyor A should have the value 7.");
    }


    @Test
    void testPutAddsNodeToConveyorB() {
        // given
        int removedValue = conveyor.conveyor("B").put(11);
        Queue<Node> conveyorB = ((CrossConveyorImpl) conveyor).getConveyor("B");

        // then
        assertEquals(5, removedValue, "The oldest node in Conveyor B should be removed when exceeding the limit.");
        assertNotNull(conveyorB, "Conveyor B should not be null.");
        assertEquals(8, conveyorB.size(), "Conveyor B should maintain its size limit of 8.");
        assertEquals(11, ((LinkedList<Node>) conveyorB).getFirst().getValue(), "The first node in Conveyor B should have the value 11.");
    }


    @Test
    void testRemoveNodeWhenExceedingLimit() {
        // given
        Queue<Node> conveyorA = ((CrossConveyorImpl) conveyor).getConveyor("A");
        int removedValue = -1;

        // when
        for (int i = 0; i < 9; i++) {
            removedValue = conveyor.conveyor("A").put(i); // Capture the removed value
        }

        // then
        assertEquals(8, conveyorA.size(), "Conveyor A should not exceed the size limit of 8.");
        assertEquals(0, removedValue, "The first inserted value should be removed when exceeding the limit.");
    }


    @Test
    void testSharedNodeUpdate() {
        // given
        Queue<Node> conveyorA = ((CrossConveyorImpl) conveyor).getConveyor("A");
        Queue<Node> conveyorB = ((CrossConveyorImpl) conveyor).getConveyor("B");

        // when
        conveyor.conveyor("A").put(7);
        conveyor.conveyor("B").put(11);


        // then
        assertNotNull(conveyorA, "Conveyor A should not be null.");
        assertNotNull(conveyorB, "Conveyor B should not be null.");

        Node sharedNodeInA = conveyorA.stream()
                .filter(Node::isShared)
                .findFirst()
                .orElse(null);

        Node sharedNodeInB = conveyorB.stream()
                .filter(Node::isShared)
                .findFirst()
                .orElse(null);

        assertNotNull(sharedNodeInA, "There should be a shared node in Conveyor A.");
        assertNotNull(sharedNodeInB, "There should be a shared node in Conveyor B.");
        assertSame(sharedNodeInA, sharedNodeInB, "The shared node in Conveyor A and Conveyor B should be the same instance.");
    }

    @Test
    void testUpdateSharedNodes() {
        // given
        Queue<Node> conveyorA = ((CrossConveyorImpl) conveyor).getConveyor("A");
        Queue<Node> conveyorB = ((CrossConveyorImpl) conveyor).getConveyor("B");

        // when
        conveyor.conveyor("A").put(7);

        // then
        Node updatedSharedNodeInA = conveyorA.stream()
                .filter(node -> node.getValue() == 1 && node.isShared()) // The updated shared node
                .findFirst()
                .orElse(null);
        Node updatedSharedNodeInB = conveyorB.stream()
                .filter(node -> node.getValue() == 1 && node.isShared()) // The updated shared node
                .findFirst()
                .orElse(null);

        assertNotNull(updatedSharedNodeInA, "Updated shared node in Conveyor A should exist.");
        assertNotNull(updatedSharedNodeInB, "Updated shared node in Conveyor B should exist.");
        assertSame(updatedSharedNodeInA, updatedSharedNodeInB, "The updated shared node should be the same instance in both conveyors.");
    }

    @Test
    void testPutThrowsExceptionForInvalidConveyorId() {
        // given
        String invalidConveyorId = "InvalidID";

        // when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            conveyor.conveyor(invalidConveyorId).put(7);
        });

        // then
        String expectedMessage = "Conveyor with ID " + invalidConveyorId + " does not exist";
        assertEquals(expectedMessage, exception.getMessage(), "The exception message should match the expected message.");
    }

    @Test
    void testPutReturnsMinusOneWhenNoNodeRemoved() {
        // give
        CrossConveyorImpl conveyor = new CrossConveyorImpl(10);
        Queue<Node> conveyorA = conveyor.getConveyor("A");
        conveyorA.clear();
        conveyorA.add(new Node(1));

        // when
        int result = conveyor.conveyor("A").put(99);

        // then
        assertEquals(-1, result, "The method should return -1 when no node is removed.");
        assertEquals(2, conveyorA.size(), "Conveyor A should contain 2 nodes.");
        assertEquals(99, conveyorA.peek().getValue(), "The first node in Conveyor A should have the value 99.");
    }


}
