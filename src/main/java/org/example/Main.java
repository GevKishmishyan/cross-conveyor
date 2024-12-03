package org.example;


import org.example.service.CrossConveyor;
import org.example.service.impl.CrossConveyorImpl;

public class Main {
    public static void main(String[] args) {
        CrossConveyor conveyor = new CrossConveyorImpl(8);

        System.out.println("Removed value in A after adding 7: " + conveyor.conveyor("A").put(7));
        System.out.println();
        System.out.println("Removed value in B after adding 11: " + conveyor.conveyor("B").put(11));
        System.out.println();
    }
}
