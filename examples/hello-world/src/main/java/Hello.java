public final class Hello {
    public static void main( String[] args ) {
        System.out.println( "WASM says " + new HelloWasm().hello() );
        System.out.println(new add( 131072 ).add( 10, 20 ));
    }
}
