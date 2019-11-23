package com.athaydes.wasm.c;

public class Main {
    public static void main( String[] args ) {
        if ( args.length != 2 ) {
            throw new RuntimeException( "Please provide two integer values to be added." );
        }
        int a = Integer.parseInt( args[ 0 ] );
        int b = Integer.parseInt( args[ 1 ] );

        System.out.println( new Adder( 0 ).add( a, b ) );
    }
}
