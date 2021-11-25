package src;

import java.math.BigInteger;

public abstract class Factor implements Derivation {
    protected String symbols = "";
    protected BigInteger exp = BigInteger.ZERO;
    protected Term derivation;

    public String toString() {
        return null;
    }

    public Factor setDerivation() {
        return null;
    }
}
