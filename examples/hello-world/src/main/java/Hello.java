public final class Hello {
    public static void main( String[] args ) {
        System.out.println( "WASM says " + new HelloWasm().hello() );
    }
}
